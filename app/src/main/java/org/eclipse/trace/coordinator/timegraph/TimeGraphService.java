package org.eclipse.trace.coordinator.timegraph;

import java.util.Map;
import java.util.UUID;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.models.entry.EntryModel;
import org.eclipse.tsp.java.client.models.query.Query;
import org.eclipse.tsp.java.client.models.response.GenericResponse;
import org.eclipse.tsp.java.client.models.timegraph.TimeGraphArrow;
import org.eclipse.tsp.java.client.models.timegraph.TimeGraphEntry;
import org.eclipse.tsp.java.client.models.timegraph.TimeGraphModel;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TimeGraphService {

    public GenericResponse<TimeGraphArrow[]> getArrows(TraceServer traceServer, UUID experimentUuid, String outputId,
            Query query) {
        return traceServer.getTspClient().getTimeGraphArrows(experimentUuid, outputId, query).getResponseModel();
    }

    public GenericResponse<TimeGraphModel> getStates(TraceServer traceServer, UUID experimentUuid, String outputId,
            Query query) {
        return traceServer.getTspClient().getTimeGraphStates(experimentUuid, outputId, query).getResponseModel();
    }

    public GenericResponse<Map<String, String>> getTooltips(TraceServer traceServer, UUID experimentUuid,
            String outputId,
            Query query) {
        return traceServer.getTspClient().getTimeGraphTooltip(experimentUuid, outputId, query).getResponseModel();
    }

    public GenericResponse<EntryModel<TimeGraphEntry>> getTree(TraceServer traceServer, UUID experimentUuid,
            String outputId,
            Query query) {
        return traceServer.getTspClient().getTimeGraphTree(experimentUuid, outputId, query).getResponseModel();
    }

    public GenericResponse<TimeGraphModel> getNavigations(TraceServer traceServer, UUID experimentUuid,
            String outputId,
            Query query) {
        throw new UnsupportedOperationException("Not Implemented");
    }
}
