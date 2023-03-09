package org.eclipse.trace.coordinator.style;

import java.util.UUID;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.models.query.Query;
import org.eclipse.tsp.java.client.models.response.GenericResponse;
import org.eclipse.tsp.java.client.models.style.OutputStyleModel;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StyleService {

    public GenericResponse<OutputStyleModel> getStyles(TraceServer traceServer, UUID experimentUuid, String outputId,
            Query query) {
        return traceServer.getTspClient().getStyles(experimentUuid, outputId, query).getResponseModel();
    }
}
