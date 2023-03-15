package org.eclipse.trace.coordinator.style;

import java.util.UUID;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.style.OutputStyleModel;
import org.eclipse.tsp.java.client.shared.query.Query;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StyleService {

    public GenericResponse<OutputStyleModel> getStyles(TraceServer traceServer, UUID experimentUuid, String outputId,
            Query query) {
        return traceServer.getTspClient().getStyleApi().getStyles(experimentUuid, outputId, query).getResponseModel();
    }
}
