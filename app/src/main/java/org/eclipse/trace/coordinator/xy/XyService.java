package org.eclipse.trace.coordinator.xy;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.xy.XyModel;
import org.eclipse.tsp.java.client.shared.entry.Entry;
import org.eclipse.tsp.java.client.shared.entry.EntryModel;
import org.eclipse.tsp.java.client.shared.query.Query;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class XyService {

    public GenericResponse<XyModel> getXy(TraceServer traceServer, UUID experimentUuid, String outputId, Query query) {
        return traceServer.getTspClient().getXyApi().getXy(experimentUuid, outputId, query).getResponseModel();
    }

    public GenericResponse<EntryModel<Entry>> getTree(TraceServer traceServer, UUID experimentUuid, String outputId,
            Query query) {
        return traceServer.getTspClient().getXyApi().getXyTree(experimentUuid, outputId, query).getResponseModel();
    }

    public GenericResponse<Map<String, String>> getTooltip(TraceServer traceServer, UUID experimentUuid,
            String outputId, int xValue, Optional<Integer> yValue, Optional<String> seriesId) {
        return traceServer.getTspClient().getXyApi().getXyTooltip(experimentUuid, outputId, xValue, yValue, seriesId)
                .getResponseModel();
    }
}
