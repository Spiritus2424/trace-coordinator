package org.eclipse.trace.coordinator.core.timegraph;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
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
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphRow;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphState;
import org.eclipse.tsp.java.client.shared.query.Body;
import org.eclipse.tsp.java.client.shared.query.QueryInterval;
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
		Queue<TimeGraphState> networkStates = this.findStyleState(timeGraphModel, CriticalPathStyle.NETWORK);

		/*
		 * Find unmatched vertex mapped the network state for the host TraceServer
		 */
		TimeGraphState networkState = networkStates.poll();
		Map<TimeGraphState, List<Vertex>> unmatchedVertexesForEachStates = new ConcurrentHashMap<>();
		while (networkState != null) {
			Body<TimeRange> body = new Body<>(new TimeRange(networkState.getStart(), networkState.getEnd()));
			this.graphService.getUnmatchedVertexes(this.hostTraceServer,
					experimentUuid, body).thenAccept(unmatchedVertexes -> {
						unmatchedVertexesForEachStates.put(networkState, unmatchedVertexes);
					});
		}

		/*
		 * Find the TcpEventKey for the unmatchedVertex
		 */
		List<TcpEventKey> tcpEventKeys = unmatchedVertexesForEachStates.values().stream()
				.flatMap(List::stream)
				.filter(vertex -> indexes.get(this.hostTraceServer).containsKey(vertex))
				.map(vertex -> indexes.get(this.hostTraceServer).get(vertex))
				.collect(Collectors.toList());

		/*
		 * Match the tcpEventKey with other traceServer using the indexes
		 */

		Map<TraceServer, Map<Vertex, TcpEventKey>> indexesShallowCopy = new ConcurrentHashMap<>(indexes);
		for (TcpEventKey tcpEventKey : tcpEventKeys) {
			cluster.forEach(traceServer -> {
				indexesShallowCopy.get(traceServer)
						.entrySet()
						.removeIf(entry -> entry.getValue().equals(tcpEventKey));
			});
		}

		/*
		 * Find the Workers on each TraceServer needed to complete the criticalPath
		 */
		Multimap<TraceServer, Worker> traceServerWorkers = ArrayListMultimap.create();
		cluster.forEach(traceServer -> {
			List<Worker> workers = indexesShallowCopy.get(traceServer).keySet().stream()
					.map(vertex -> this.graphService.getWorker(traceServer, experimentUuid, vertex.getWorkerId()))
					.map(CompletableFuture::join)
					.distinct() // Remove duplicate Worker Id for the same TraceServer
					.collect(Collectors.toList());
			traceServerWorkers.putAll(traceServer, workers);
		});

	}

	private Map<TraceServer, Map<Vertex, TcpEventKey>> fetchVertexIndexes(UUID experimentUuid) {
		final Map<TraceServer, Map<Vertex, TcpEventKey>> indexes = new ConcurrentHashMap<>();
		this.traceServerManager.getTraceServers().forEach(traceServer -> {
			this.graphService.getVertexIndexes(traceServer, experimentUuid)
					.thenApply(traceServerIndexes -> indexes.put(traceServer, traceServerIndexes));
		});

		return indexes;
	}

	private Queue<TimeGraphState> findStyleState(TimeGraphModel timeGraphModel, CriticalPathStyle criticalPathStyle) {
		return timeGraphModel.getRows().stream()
				.map(timeGraphRow -> timeGraphRow.getStates())
				.flatMap(List::stream)
				.filter(timeGraphState -> timeGraphState.getStyle().getParentKey().equals(criticalPathStyle.toString()))
				.collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
	}

	// private List<Integer> collectItems(List<TraceServer> cluster, UUID
	// experimentUuid, QueryInterval queryInterval) {
	// String outputId =
	// "org.eclipse.tracecompass.internal.analysis.os.linux.core.threadstatus.ThreadStatusDataProvider";
	// return cluster.stream()
	// .map(traceServer -> this.timeGraphService.getTree(traceServer,
	// experimentUuid, outputId,
	// new Body<>(new GetTimeGraphTreeRequestDto(null, queryInterval))))
	// .map(CompletableFuture::join)
	// .map(GenericResponse::getModel)
	// .filter(Objects::nonNull)
	// .map(EntryModel::getEntries)
	// .flatMap(List::stream)
	// .map(TimeGraphEntry::getId)
	// .collect(Collectors.toList());
	// }

	// private List<TimeGraphRow> collectMissingStates(List<TraceServer> cluster,
	// UUID experimentUuid,
	// QueryInterval queryInterval) {
	// String outputId =
	// "org.eclipse.tracecompass.internal.analysis.os.linux.core.threadstatus.ThreadStatusDataProvider";
	// return cluster.stream()
	// .map(traceServer -> this.timeGraphService.getStates(traceServer,
	// experimentUuid, outputId,
	// new Body<>(new GetTimeGraphStatesRequestDto(queryInterval, null))))
	// .map(CompletableFuture::join)
	// .map(GenericResponse::getModel)
	// .filter(Objects::nonNull)
	// .map(TimeGraphModel::getRows)
	// .flatMap(List::stream)
	// .collect(Collectors.toList());
	// }

	private void findMatchingState(List<TimeGraphRow> timeGraphRows, QueryInterval queryInterval) {
		/**
		 * 1. Trouver le state qui commence en même temps
		 * 2. Voir si le state est WAIT FOR CPU, USERMODE
		 */
	}
	// private void collectMissingStates(List<TraceServer> traceServers, UUID
	// experimentUuid,
	// List<TimeGraphState> timeGraphStates) {
	// List<Integer> requestedItems = new ArrayList<>();
	// timeGraphStates.stream().map(timeGraphState -> {
	// QueryInterval queryInterval = new QueryInterval(timeGraphState.getStart(),
	// timeGraphState.getEnd(), null);
	// traceServers.stream().map(traceServer -> {
	// requestedItems.addAll(this.collectRequestedItems(traceServer,
	// experimentUuid,
	// new Body<>(new GetTimeGraphTreeRequestDto(null, queryInterval))));
	// return null;
	// });

	// return null;
	// });
	// // for (TimeGraphState timeGraphState : timeGraphStates) {
	// // for (TraceServer traceServer : traceServers) {

	// // }
	// // }

	// }
}
