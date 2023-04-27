package org.eclipse.trace.coordinator.style;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.style.OutputStyleModel;
import org.eclipse.tsp.java.client.shared.query.Query;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StyleService {

    public CompletableFuture<GenericResponse<OutputStyleModel>> getStyles(
            final TraceServer traceServer,
            final UUID experimentUuid,
            final String outputId,
            final Query query) {
        return traceServer.getTspClient().getStyleApiAsync().getStyles(experimentUuid, outputId, query)
                .thenApply(response -> response.getResponseModel());
    }
}
