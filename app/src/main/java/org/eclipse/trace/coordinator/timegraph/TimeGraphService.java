package org.eclipse.trace.coordinator.timegraph;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

	public GenericResponse<TimeGraphArrow[]> getArrows(TraceServer traceServer, UUID experimentUuid,
			String outputId,
			Query query) {

		GenericResponse<TimeGraphArrow[]> genericResponse = traceServer.getTspClient().getTimeGraphApi()
				.getTimeGraphArrows(experimentUuid, outputId, query)
				.getResponseModel();

		this.timeGraphAnalysis.computeArrows(traceServer, Arrays.asList(genericResponse.getModel()));
		return genericResponse;
	}

	public GenericResponse<TimeGraphModel> getStates(TraceServer traceServer, UUID experimentUuid, String outputId,
			Query query) {

		if (query.getParameters().containsKey("requested_items")) {
			List<Integer> requested_items = (List<Integer>) query.getParameters().get("requested_items");

			for (int i = 0; i < requested_items.size(); i++) {
				requested_items.set(i, traceServer.decodeEntryId(requested_items.get(i)));
			}

			query.getParameters().put("requested_items", requested_items);
		}

		GenericResponse<TimeGraphModel> genericResponse = traceServer.getTspClient().getTimeGraphApi()
				.getTimeGraphStates(experimentUuid, outputId, query)
				.getResponseModel();
		this.timeGraphAnalysis.computeStates(traceServer, genericResponse.getModel().getRows());
		return genericResponse;
	}

	public GenericResponse<Map<String, String>> getTooltips(TraceServer traceServer, UUID experimentUuid,
			String outputId,
			Query query) {

		if (query.getParameters().containsKey("requested_items")) {
			List<Integer> requested_items = (List<Integer>) query.getParameters().get("requested_items");

			for (int i = 0; i < requested_items.size(); i++) {
				requested_items.set(i, traceServer.decodeEntryId(requested_items.get(i)));
			}

			query.getParameters().put("requested_items", requested_items);
		}

		return traceServer.getTspClient().getTimeGraphApi().getTimeGraphTooltip(experimentUuid, outputId, query)
				.getResponseModel();
	}

	public GenericResponse<EntryModel<TimeGraphEntry>> getTree(TraceServer traceServer, UUID experimentUuid,
			String outputId,
			Query query) {
		GenericResponse<EntryModel<TimeGraphEntry>> genericResponse = traceServer.getTspClient()
				.getTimeGraphApi()
				.getTimeGraphTree(experimentUuid, outputId, query)
				.getResponseModel();
		this.timeGraphAnalysis.computeTrees(traceServer, genericResponse.getModel().getEntries());

		return genericResponse;
	}

	public GenericResponse<TimeGraphModel> getNavigations(TraceServer traceServer, UUID experimentUuid,
			String outputId,
			Query query) {
		throw new UnsupportedOperationException("Not Implemented");
	}
}
