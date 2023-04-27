package org.eclipse.trace.coordinator.markerset;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.trace.coordinator.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.api.markerset.MarkerSet;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;
import org.eclipse.tsp.java.client.shared.response.ResponseStatus;

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
        GenericResponse<MarkerSet[]> genericResponseMerged = this.traceServerManager.getTraceServers()
                .stream()
                .map((TraceServer traceServer) -> this.markerSetService.getMarkerSets(traceServer, experimentUuid))
                .map(CompletableFuture::join)
                .reduce(null, (accumulator, genericResponse) -> {
                    if (accumulator == null) {
                        accumulator = genericResponse;
                    } else {
                        if (accumulator.getStatus() != ResponseStatus.RUNNING) {
                            accumulator.setStatus(genericResponse.getStatus());
                            accumulator.setMessage(genericResponse.getMessage());
                        }
                        if (genericResponse.getModel() != null) {
                            markerSets.addAll(Arrays.asList(genericResponse.getModel()));
                        }
                    }
                    return accumulator;
                });
        genericResponseMerged.setModel(markerSets.toArray(new MarkerSet[markerSets.size()]));

        return Response.ok(genericResponseMerged).build();
    }

}