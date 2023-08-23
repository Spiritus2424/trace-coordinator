package org.eclipse.trace.coordinator.core.timegraph;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.eclipse.trace.coordinator.core.style.CriticalPathStyle;
import org.eclipse.trace.coordinator.core.traceserver.TraceServer;
import org.eclipse.trace.coordinator.core.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphModel;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphRow;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphState;
import org.eclipse.tsp.java.client.api.timegraph.dto.GetTimeGraphStatesRequestDto;
import org.eclipse.tsp.java.client.shared.query.Body;
import org.eclipse.tsp.java.client.shared.query.QueryInterval;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;

import jakarta.inject.Inject;

public class CriticalPathAction {
	@Inject
	TimeGraphService timeGraphService;
	@Inject
	TraceServerManager traceServerManager;

	public void compute(TraceServer hostTraceServer, UUID experimentUuid, TimeGraphModel timeGraphModel) {
		List<TraceServer> cluster = this.traceServerManager.getTraceServers().stream()
				.filter((TraceServer traceServer) -> traceServer.getId() != hostTraceServer.getId())
				.collect(Collectors.toList());
		List<TimeGraphState> networkStates = this.findStyleState(timeGraphModel, CriticalPathStyle.NETWORK);

		for (TimeGraphState timeGraphState : networkStates) {
			QueryInterval queryInterval = new QueryInterval(timeGraphState.getStart(), timeGraphState.getEnd(), null);
			List<TimeGraphRow> timeGraphRows = this.collectMissingStates(cluster, experimentUuid, queryInterval);

		}

		List<TimeGraphRow> timeGraphRows = networkStates.stream()
				.map(timeGraphState -> new QueryInterval(timeGraphState.getStart(), timeGraphState.getEnd(), null))
				.map(queryInterval -> this.collectMissingStates(cluster, experimentUuid, queryInterval))
				.flatMap(List::stream)
				.collect(Collectors.toList());

	}

	private List<TimeGraphState> findStyleState(TimeGraphModel timeGraphModel, CriticalPathStyle criticalPathStyle) {
		return timeGraphModel.getRows().stream()
				.map(timeGraphRow -> timeGraphRow.getStates())
				.flatMap(List::stream)
				.filter(timeGraphState -> timeGraphState.getStyle().getParentKey().equals(criticalPathStyle.toString()))
				.collect(Collectors.toList());
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

	private List<TimeGraphRow> collectMissingStates(List<TraceServer> cluster, UUID experimentUuid,
			QueryInterval queryInterval) {
		String outputId = "org.eclipse.tracecompass.internal.analysis.os.linux.core.threadstatus.ThreadStatusDataProvider";
		return cluster.stream()
				.map(traceServer -> this.timeGraphService.getStates(traceServer, experimentUuid, outputId,
						new Body<>(new GetTimeGraphStatesRequestDto(queryInterval, null))))
				.map(CompletableFuture::join)
				.map(GenericResponse::getModel)
				.filter(Objects::nonNull)
				.map(TimeGraphModel::getRows)
				.flatMap(List::stream)
				.collect(Collectors.toList());
	}

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
