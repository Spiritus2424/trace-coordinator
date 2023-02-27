package org.eclipse.trace.coordinator.markerset;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.trace.coordinator.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.models.markerset.MarkerSet;
import org.eclipse.tsp.java.client.models.response.GenericResponse;
import org.eclipse.tsp.java.client.models.response.ResponseStatus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * MarkerSetController
 */
@Path("experiments/{expUUID}/outputs/markerSets")
@ApplicationScoped
public class MarkerSetController {

    @Inject
    private TraceServerManager traceServerManager;

    @Inject
    private MarkerSetService markerSetService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getMarkerSets(@PathParam("expUUID") UUID experimentUuid) {
        Set<MarkerSet> markerSets = new HashSet<>();
        ResponseStatus responseStatus = ResponseStatus.COMPLETED;
        String statusMessage = null;
        for (TraceServer traceServer : traceServerManager.getTraceServers()) {

            GenericResponse<MarkerSet[]> genericResponse = this.markerSetService
                    .getMarkerSets(traceServer, experimentUuid);
            if (responseStatus != ResponseStatus.RUNNING) {
                responseStatus = genericResponse.getStatus();
                statusMessage = genericResponse.getMessage();
            }

            if (genericResponse.getModel() != null) {
                markerSets.addAll(Arrays.asList(genericResponse.getModel()));

            }
        }

        return Response
                .ok(new GenericResponse<MarkerSet[]>(markerSets.toArray(new MarkerSet[markerSets.size()]),
                        responseStatus, statusMessage))
                .build();
    }

}