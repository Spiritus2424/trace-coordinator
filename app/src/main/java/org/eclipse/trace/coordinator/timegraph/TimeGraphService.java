package org.eclipse.trace.coordinator.timegraph;

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

@ApplicationScoped
public class TimeGraphService {

    public GenericResponse<TimeGraphArrow[]> getArrows(TraceServer traceServer, UUID experimentUuid, String outputId,
            Query query) {
        return traceServer.getTspClient().getTimeGraphApi().getTimeGraphArrows(experimentUuid, outputId, query)
                .getResponseModel();
    }

    public GenericResponse<TimeGraphModel> getStates(TraceServer traceServer, UUID experimentUuid, String outputId,
            Query query) {
        return traceServer.getTspClient().getTimeGraphApi().getTimeGraphStates(experimentUuid, outputId, query)
                .getResponseModel();
    }

    public GenericResponse<Map<String, String>> getTooltips(TraceServer traceServer, UUID experimentUuid,
            String outputId,
            Query query) {
        return traceServer.getTspClient().getTimeGraphApi().getTimeGraphTooltip(experimentUuid, outputId, query)
                .getResponseModel();
    }

    public GenericResponse<EntryModel<TimeGraphEntry>> getTree(TraceServer traceServer, UUID experimentUuid,
            String outputId,
            Query query) {
        return traceServer.getTspClient().getTimeGraphApi().getTimeGraphTree(experimentUuid, outputId, query)
                .getResponseModel();
    }

    public GenericResponse<TimeGraphModel> getNavigations(TraceServer traceServer, UUID experimentUuid,
            String outputId,
            Query query) {
        throw new UnsupportedOperationException("Not Implemented");
    }
}
