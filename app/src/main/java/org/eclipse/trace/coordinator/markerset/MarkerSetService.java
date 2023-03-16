package org.eclipse.trace.coordinator.markerset;

import java.util.UUID;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.markerset.MarkerSet;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MarkerSetService {

    public GenericResponse<MarkerSet[]> getMarkerSets(TraceServer traceServer, UUID experimentUuid) {
        return traceServer.getTspClient().getMarkerSetApi().getMarkerSets(experimentUuid).getResponseModel();
    }
}
