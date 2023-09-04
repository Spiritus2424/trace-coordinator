package org.eclipse.trace.coordinator.core.timegraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.eclipse.trace.coordinator.core.action.IAction;
import org.eclipse.trace.coordinator.core.graph.GraphService;
import org.eclipse.trace.coordinator.core.style.CriticalPathStyle;
import org.eclipse.trace.coordinator.core.traceserver.TraceServer;
import org.eclipse.trace.coordinator.core.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.api.graph.Direction;
import org.eclipse.tsp.java.client.api.graph.HostThread;
import org.eclipse.tsp.java.client.api.graph.TcpEventKey;
import org.eclipse.tsp.java.client.api.graph.Vertex;
import org.eclipse.tsp.java.client.api.graph.Worker;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphModel;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphState;
import org.eclipse.tsp.java.client.api.timegraph.dto.GetTimeGraphStatesRequestDto;
import org.eclipse.tsp.java.client.shared.query.Body;
import org.eclipse.tsp.java.client.shared.query.QueryInterval;
import org.eclipse.tsp.java.client.shared.query.TimeRange;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import jakarta.inject.Inject;
import lombok.Getter;

public class CriticalPathAction implements IAction<TimeGraphModel> {

	@Inject
	private TimeGraphService timeGraphService;
	@Inject
	private TraceServerManager traceServerManager;
	@Inject
	private GraphService graphService;
	@Getter
	private TraceServer hostTraceServer;
	@Getter
	private UUID experimentUuid;

	public CriticalPathAction(TraceServer traceServer, UUID experimentUuid) {
		this.hostTraceServer = traceServer;
		this.experimentUuid = experimentUuid;
	}

	public void compute(TimeGraphModel timeGraphModel) {
		/*
		 * Fetch All the traceServer Vertex indexes
		 */
		// Map<TraceServer, Map<Vertex, TcpEventKey>> indexesCause =
		// this.fetchVertexIndexes(experimentUuid,
		// Direction.CAUSE);
		// Map<TraceServer, Map<Vertex, TcpEventKey>> indexesEffect =
		// this.fetchVertexIndexes(experimentUuid,
		// Direction.EFFECT);

		Map<TraceServer, Map<Vertex, TcpEventKey>> indexes = this.fetchVertexIndexes(Direction.EFFECT);

		/*
		 * Build a cluster without the traceServer that initiate the analysis
		 */
		List<TraceServer> cluster = this.traceServerManager.getTraceServers().stream()
				.filter((TraceServer traceServer) -> traceServer.getId() != hostTraceServer.getId())
				.collect(Collectors.toList());
		/*
		 * Find all the Network states in the TimeGraphModel
		 */
		List<TimeGraphState> networkStates = this.findStyleState(timeGraphModel, CriticalPathStyle.NETWORK);

		for (TimeGraphState timeGraphState : networkStates) {
			/*
			 * Find unmatched vertex mapped the network state for the host TraceServer
			 */
			List<Vertex> unmatchedEffectVertexes = this.getUnmatchedVertexes(timeGraphState).join();

			/*
			 * Find the TcpEventKey for the unmatchedVertex for each NetworkState
			 */
			List<TcpEventKey> tcpEventKeys = this.getTcpEventKeys(indexes.get(this.hostTraceServer),
					unmatchedEffectVertexes);

			/*
			 * Match the tcpEventKey with other traceServer using the indexes
			 */
			Multimap<TraceServer, Vertex> matchedVertexes = ArrayListMultimap.create();
			for (TcpEventKey tcpEventKey : tcpEventKeys) {
				matchedVertexes.putAll(this.getMatchedVertexes(cluster, indexes, tcpEventKey));
			}
			/*
			 * Find the Workers on each TraceServer needed to complete the criticalPath
			 */
			Multimap<TraceServer, Worker> traceServerWorkers = this.findTraceServerWorkers(cluster, matchedVertexes);
			System.out.println(traceServerWorkers);

			/*
			 * Apply Action Critical for Worker and Get TimeGraphState from outputId
			 * CriticalPathDataProvider
			 */
			for (Entry<TraceServer, Worker> workerEntry : traceServerWorkers.entries()) {
				GenericResponse<TimeGraphModel> genericResponse = this.getStates(workerEntry.getKey(),
						workerEntry.getValue().getHostThread(),
						new QueryInterval(timeGraphState.getStart(), timeGraphState.getEnd()));

				System.out.println(genericResponse);
			}

		}

	}

	private Map<TraceServer, Map<Vertex, TcpEventKey>> fetchVertexIndexes(Direction direction) {
		final Map<TraceServer, Map<Vertex, TcpEventKey>> indexes = new ConcurrentHashMap<>();
		this.traceServerManager.getTraceServers().forEach(traceServer -> {
			this.graphService
					.getVertexIndexes(traceServer, this.experimentUuid,
							direction == null ? Optional.empty() : Optional.of(direction))
					.thenApply(traceServerIndexes -> indexes.put(traceServer, traceServerIndexes));
		});

		return indexes;
	}

	private List<TimeGraphState> findStyleState(TimeGraphModel timeGraphModel, CriticalPathStyle criticalPathStyle) {
		return timeGraphModel.getRows().stream()
				.map(timeGraphRow -> timeGraphRow.getStates())
				.flatMap(List::stream)
				.filter(timeGraphState -> timeGraphState.getStyle().getParentKey().equals(criticalPathStyle.toString()))
				.collect(Collectors.toList());
	}

	private CompletableFuture<List<Vertex>> getUnmatchedVertexes(final TimeGraphState networkState) {
		Body<TimeRange> body = new Body<>(new TimeRange(networkState.getStart(), networkState.getEnd()));
		return this.graphService.getUnmatchedVertexes(this.hostTraceServer,
				this.experimentUuid, body, Optional.of(Direction.EFFECT));
	}

	private List<TcpEventKey> getTcpEventKeys(Map<Vertex, TcpEventKey> traceServerIndexes,
			List<Vertex> unmatchedVertexes) {
		return unmatchedVertexes.stream()
				.filter(vertex -> traceServerIndexes.containsKey(vertex))
				.map(vertex -> traceServerIndexes.get(vertex))
				.collect(Collectors.toList());

	}

	private Multimap<TraceServer, Vertex> getMatchedVertexes(List<TraceServer> cluster,
			Map<TraceServer, Map<Vertex, TcpEventKey>> indexes, TcpEventKey tcpEventKey) {
		Multimap<TraceServer, Vertex> matchedVertexes = ArrayListMultimap.create();

		cluster.forEach(traceServer -> {
			for (Entry<Vertex, TcpEventKey> indexEntry : indexes.get(traceServer).entrySet()) {
				if (indexEntry.getValue().equals(tcpEventKey)) {
					matchedVertexes.put(traceServer, indexEntry.getKey());
				}
			}
		});

		return matchedVertexes;
	}

	private Multimap<TraceServer, Worker> findTraceServerWorkers(List<TraceServer> cluster,
			Multimap<TraceServer, Vertex> matchedVertexes) {
		Multimap<TraceServer, Worker> traceServerWorkers = ArrayListMultimap.create();

		cluster.forEach(traceServer -> {
			List<Worker> workers = matchedVertexes.get(traceServer).stream()
					.map(vertex -> this.graphService.getWorker(traceServer, this.experimentUuid, vertex.getWorkerId()))
					.map(CompletableFuture::join)
					.distinct() // Remove duplicate Worker Id for the same TraceServer
					.collect(Collectors.toList());
			traceServerWorkers.get(traceServer).addAll(workers);
		});

		return traceServerWorkers;
	}

	private GenericResponse<TimeGraphModel> getStates(TraceServer traceServer, HostThread hostThread,
			QueryInterval queryInterval) {
		final String threadStatusDataProviderOutputId = "org.eclipse.tracecompass.internal.analysis.os.linux.core.threadstatus.ThreadStatusDataProvider";
		final String actionId = "FollowThreadAction";
		Map<String, Object> inputParameters = new HashMap<>();
		inputParameters.put("hostThread", hostThread);
		this.timeGraphService.applyActionTooltip(traceServer, this.experimentUuid, threadStatusDataProviderOutputId,
				actionId, new Body<Map<String, Object>>(inputParameters)).join();
		final String criticalPathDataProviderOutputId = "org.eclipse.tracecompass.analysis.graph.core.dataprovider.CriticalPathDataProvider";
		return this.timeGraphService.getStates(traceServer, experimentUuid, criticalPathDataProviderOutputId,
				new Body<GetTimeGraphStatesRequestDto>(
						new GetTimeGraphStatesRequestDto(queryInterval, new ArrayList<>())))
				.join();
	}
}
