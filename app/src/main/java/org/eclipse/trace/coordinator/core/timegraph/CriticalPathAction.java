package org.eclipse.trace.coordinator.core.timegraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.eclipse.trace.coordinator.core.action.IAction;
import org.eclipse.trace.coordinator.core.graph.GraphService;
import org.eclipse.trace.coordinator.core.style.CriticalPathStyle;
import org.eclipse.trace.coordinator.core.traceserver.TraceServer;
import org.eclipse.trace.coordinator.core.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.api.graph.TcpEventKey;
import org.eclipse.tsp.java.client.api.graph.Vertex;
import org.eclipse.tsp.java.client.api.graph.Worker;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphModel;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphState;
import org.eclipse.tsp.java.client.shared.query.Body;
import org.eclipse.tsp.java.client.shared.query.TimeRange;

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
		Map<TraceServer, Map<Vertex, TcpEventKey>> indexes = this.fetchVertexIndexes(experimentUuid);

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

		/*
		 * Find unmatched vertex mapped the network state for the host TraceServer
		 */
		Multimap<TimeGraphState, Vertex> unmatchedVertexesForEachStates = ArrayListMultimap.create();
		List<CompletableFuture<Void>> unmatchedVertexesFutures = new ArrayList<>();
		for (TimeGraphState timeGraphState : networkStates) {
			unmatchedVertexesFutures.add(
					this.getUnmatchedVertexes(timeGraphState).thenAccept(
							unmatchedVertexes -> unmatchedVertexesForEachStates.putAll(timeGraphState,
									unmatchedVertexes)));
		}
		CompletableFuture
				.allOf(unmatchedVertexesFutures.toArray(new CompletableFuture[unmatchedVertexesFutures.size()])).join();

		/*
		 * Find the TcpEventKey for the unmatchedVertex for each NetworkState
		 */
		Multimap<TimeGraphState, TcpEventKey> tcpEventKeysForEachStates = ArrayListMultimap.create();
		for (TimeGraphState networkState : unmatchedVertexesForEachStates.keySet()) {
			List<TcpEventKey> list = this.getTcpEventKeys(indexes.get(this.hostTraceServer),
					(List<Vertex>) unmatchedVertexesForEachStates.get(networkState));
			tcpEventKeysForEachStates.putAll(networkState, list);
		}

		/*
		 * Match the tcpEventKey with other traceServer using the indexes
		 */
		Map<TimeGraphState, Multimap<TraceServer, Vertex>> matchedVertexesForEachStates = new HashMap<>();
		for (Entry<TimeGraphState, TcpEventKey> tcpEventKeyEntry : tcpEventKeysForEachStates.entries()) {
			Multimap<TraceServer, Vertex> matchedVertexes = matchedVertexesForEachStates
					.computeIfAbsent(tcpEventKeyEntry.getKey(), __ -> ArrayListMultimap.create());
			matchedVertexes.putAll(this.getMatchedVertexes(cluster, indexes, tcpEventKeyEntry));
		}

		/*
		 * Find the Workers on each TraceServer needed to complete the criticalPath
		 */
		Map<TimeGraphState, Multimap<TraceServer, Worker>> traceServerWorkers = this.findTraceServerWorkers(cluster,
				matchedIndexes);

		System.out.println(traceServerWorkers);

	}

	private Map<TraceServer, Map<Vertex, TcpEventKey>> fetchVertexIndexes(UUID experimentUuid) {
		final Map<TraceServer, Map<Vertex, TcpEventKey>> indexes = new ConcurrentHashMap<>();
		this.traceServerManager.getTraceServers().forEach(traceServer -> {
			this.graphService.getVertexIndexes(traceServer, experimentUuid)
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
				experimentUuid, body);
	}

	private List<TcpEventKey> getTcpEventKeys(Map<Vertex, TcpEventKey> traceServerIndexes,
			List<Vertex> unmatchedVertexes) {
		return unmatchedVertexes.stream()
				.filter(vertex -> traceServerIndexes.containsKey(vertex))
				.map(vertex -> traceServerIndexes.get(vertex))
				.collect(Collectors.toList());

	}

	private Multimap<TraceServer, Vertex> getMatchedVertexes(List<TraceServer> cluster,
			Map<TraceServer, Map<Vertex, TcpEventKey>> indexes, Entry<TimeGraphState, TcpEventKey> tcpEventKeyEntry) {
		Multimap<TraceServer, Vertex> matchedVertexes = ArrayListMultimap.create();

		for (TraceServer traceServer : cluster) {
			for (Entry<Vertex, TcpEventKey> indexEntry : indexes.get(traceServer).entrySet()) {
				if (indexEntry.getValue().equals(tcpEventKeyEntry.getValue())) {
					matchedVertexes.put(traceServer, indexEntry.getKey());
				}
			}
		}

		return matchedVertexes;
	}

	private Map<TimeGraphState, Multimap<TraceServer, Worker>> findTraceServerWorkers(List<TraceServer> cluster,
			Map<TimeGraphState, Multimap<TraceServer, Vertex>> matchedIndexes) {
		Map<TimeGraphState, Multimap<TraceServer, Worker>> traceServerWorkers = new HashMap<>();
		for (TimeGraphState networkState : matchedIndexes.keySet()) {
			cluster.forEach(traceServer -> {
				List<Worker> workers = matchedIndexes.get(networkState).get(traceServer).stream()
						.map(vertex -> this.graphService.getWorker(traceServer, experimentUuid, vertex.getWorkerId()))
						.map(CompletableFuture::join)
						.distinct() // Remove duplicate Worker Id for the same TraceServer
						.collect(Collectors.toList());
				traceServerWorkers.get(networkState).get(traceServer).addAll(workers);
			});
		}

		return traceServerWorkers;
	}
}
