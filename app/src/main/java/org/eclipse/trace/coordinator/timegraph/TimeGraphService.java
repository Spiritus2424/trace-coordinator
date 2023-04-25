package org.eclipse.trace.coordinator.timegraph;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphArrow;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphEntry;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphModel;
import org.eclipse.tsp.java.client.shared.entry.EntryModel;
import org.eclipse.tsp.java.client.shared.query.Query;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TimeGraphService {
	@Inject
	private TimeGraphAnalysis timeGraphAnalysis;

	public CompletableFuture<GenericResponse<List<TimeGraphArrow>>> getArrows(TraceServer traceServer,
			UUID experimentUuid,
			String outputId,
			Query query) {
		return traceServer.getTspClient().getTimeGraphApiAsync()
				.getTimeGraphArrows(experimentUuid, outputId, query).thenApply(response -> {
					List<TimeGraphArrow> timeGraphArrows = Arrays.asList(response.getResponseModel().getModel());
					this.timeGraphAnalysis.computeArrows(traceServer, timeGraphArrows);
					return new GenericResponse<List<TimeGraphArrow>>(timeGraphArrows,
							response.getResponseModel().getStatus(),
							response.getResponseModel().getMessage());
				});
	}

	public CompletableFuture<GenericResponse<TimeGraphModel>> getStates(TraceServer traceServer, UUID experimentUuid,
			String outputId,
			Query query) {
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("requested_timerange", query.getParameters().get("requested_timerange"));
		if (query.getParameters().containsKey("requested_items")) {
			List<Integer> requested_items = List.copyOf((List<Integer>) query.getParameters().get("requested_items"));

			parameters.put("requested_items", requested_items.stream()
					.filter((Integer encodeEntryId) -> traceServer.isValidEncodeEntryId(encodeEntryId))
					.map(encodeEntryId -> traceServer.decodeEntryId(encodeEntryId)).collect(Collectors.toList()));
		}

		return traceServer.getTspClient().getTimeGraphApiAsync()
				.getTimeGraphStates(experimentUuid, outputId, new Query(parameters)).thenApply(response -> {
					this.timeGraphAnalysis.computeStates(traceServer, response.getResponseModel().getModel().getRows());
					return response.getResponseModel();
				});
	}

	@SuppressWarnings("unchecked")
	public CompletableFuture<GenericResponse<Map<String, String>>> getTooltips(TraceServer traceServer,
			UUID experimentUuid,
			String outputId,
			Query query) {
		List<Integer> requested_items = List.copyOf((List<Integer>) query.getParameters().get("requested_items"));
		requested_items = requested_items.stream()
				.filter((Integer encodeEntryId) -> traceServer.isValidEncodeEntryId(encodeEntryId))
				.map(encodeEntryId -> traceServer.decodeEntryId(encodeEntryId)).collect(Collectors.toList());
		if (requested_items.isEmpty()) {
			return null;
		}

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("requested_times", query.getParameters().get("requested_times"));
		parameters.put("requested_element", query.getParameters().get("requested_element"));
		parameters.put("requested_items", requested_items);

		return traceServer.getTspClient().getTimeGraphApiAsync()
				.getTimeGraphTooltip(experimentUuid, outputId, new Query(parameters))
				.thenApply(response -> response.getResponseModel());
	}

	public CompletableFuture<GenericResponse<EntryModel<TimeGraphEntry>>> getTree(TraceServer traceServer,
			UUID experimentUuid,
			String outputId,
			Query query) {
		return traceServer.getTspClient()
				.getTimeGraphApiAsync()
				.getTimeGraphTree(experimentUuid, outputId, query).thenApply(response -> {
					this.timeGraphAnalysis.computeTrees(traceServer,
							response.getResponseModel().getModel().getEntries());

					return response.getResponseModel();
				});
	}

	public GenericResponse<TimeGraphModel> getNavigations(TraceServer traceServer, UUID experimentUuid,
			String outputId,
			Query query) {
		throw new UnsupportedOperationException("Not Implemented");
	}
}
