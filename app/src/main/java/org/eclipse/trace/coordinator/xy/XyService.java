package org.eclipse.trace.coordinator.xy;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.models.entry.Entry;
import org.eclipse.tsp.java.client.models.entry.EntryModel;
import org.eclipse.tsp.java.client.models.query.Query;
import org.eclipse.tsp.java.client.models.response.GenericResponse;
import org.eclipse.tsp.java.client.models.xy.XYModel;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class XyService {

    public GenericResponse<XYModel> getXy(TraceServer traceServer, UUID experimentUuid, String outputId, Query query) {
        return traceServer.getTspClient().getXY(experimentUuid, outputId, query).getResponseModel();
    }

    public GenericResponse<EntryModel<Entry>> getTree(TraceServer traceServer, UUID experimentUuid, String outputId,
            Query query) {
        return traceServer.getTspClient().getXYTree(experimentUuid, outputId, query).getResponseModel();
    }

    public GenericResponse<Map<String, String>> getTooltip(TraceServer traceServer, UUID experimentUuid,
            String outputId, int xValue, Optional<Integer> yValue, Optional<String> seriesId) {
        return traceServer.getTspClient().getXYToolTip(experimentUuid, outputId, xValue, yValue, seriesId)
                .getResponseModel();
    }
}
