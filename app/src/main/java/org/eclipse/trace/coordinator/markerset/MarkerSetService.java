package org.eclipse.trace.coordinator.markerset;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.markerset.MarkerSet;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MarkerSetService {

    public CompletableFuture<GenericResponse<MarkerSet[]>> getMarkerSets(TraceServer traceServer, UUID experimentUuid) {
        return traceServer.getTspClient().getMarkerSetApiAsync().getMarkerSets(experimentUuid)
                .thenApply(response -> response.getResponseModel());

    }
}
