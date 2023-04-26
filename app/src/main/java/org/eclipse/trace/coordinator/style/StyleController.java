package org.eclipse.trace.coordinator.style;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.trace.coordinator.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.api.style.OutputStyleModel;
import org.eclipse.tsp.java.client.shared.query.Query;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;
import org.eclipse.tsp.java.client.shared.response.ResponseStatus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * StyleController
 */
@Path("experiments/{expUUID}/outputs/{outputId}/style")
@ApplicationScoped
public class StyleController {

    @Inject
    StyleService styleService;

    @Inject
    TraceServerManager traceServerManager;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStyles(
            @NotNull @PathParam("expUUID") final UUID experimentUuid,
            @NotNull @PathParam("outputId") final String outputId,
            @NotNull final Query query) {
        final GenericResponse<OutputStyleModel> genericResponseMerged = this.traceServerManager.getTraceServers()
                .stream()
                .map((TraceServer traceServer) -> this.styleService.getStyles(traceServer, experimentUuid, outputId,
                        query))
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
                            accumulator.getModel().getStyles().putAll(genericResponse.getModel().getStyles());
                        }
                    }

                    return accumulator;
                });

        return Response.ok(genericResponseMerged).build();
    }
}