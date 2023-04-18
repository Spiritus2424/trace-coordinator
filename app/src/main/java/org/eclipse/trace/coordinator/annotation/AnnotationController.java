package org.eclipse.trace.coordinator.annotation;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.trace.coordinator.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.api.annotation.AnnotationCategoriesModel;
import org.eclipse.tsp.java.client.api.annotation.AnnotationModel;
import org.eclipse.tsp.java.client.shared.query.Query;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;
import org.eclipse.tsp.java.client.shared.response.ResponseStatus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("experiments/{expUUID}/outputs/{outputId}/annotations")
@ApplicationScoped
public class AnnotationController {

    @Inject
    private TraceServerManager traceServerManager;

    @Inject
    private AnnotationService annotationService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAnnotationCategories(@PathParam("expUUID") UUID experimentUuid,
            @PathParam("outputId") String outputId,
            @QueryParam("markerSetId") String markerSetId) {

        GenericResponse<AnnotationCategoriesModel> genericResponseMerged = this.traceServerManager
                .getTraceServers()
                .stream()
                .map((TraceServer traceServer) -> this.annotationService.getAnnotationCategories(traceServer,
                        experimentUuid, outputId, markerSetId != null ? Optional.of(markerSetId) : Optional.empty()))
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList())
                .stream()
                .reduce(null, (accumulator, genericResponse) -> {
                    if (accumulator == null) {
                        accumulator = genericResponse;
                    } else {
                        if (accumulator.getStatus() != ResponseStatus.RUNNING) {
                            accumulator.setStatus(genericResponse.getStatus());
                            accumulator.setMessage(genericResponse.getMessage());
                        }
                        if (genericResponse.getModel() != null) {
                            accumulator.getModel().getAnnotationCategories()
                                    .addAll(genericResponse.getModel().getAnnotationCategories());
                        }
                    }

                    return accumulator;
                });

        return Response.ok(genericResponseMerged).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAnnotation(@PathParam("expUUID") UUID experimentUuid,
            @PathParam("outputId") String outputId, @NotNull Query query) {
        GenericResponse<AnnotationModel> genericResponseMerged = this.traceServerManager
                .getTraceServers()
                .stream()
                .map((TraceServer traceServer) -> this.annotationService.getAnnotationModel(traceServer,
                        experimentUuid, outputId, query))
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList())
                .stream()
                .reduce(null, (accumulator, genericResponse) -> {
                    if (accumulator == null) {
                        accumulator = genericResponse;
                    } else {
                        if (accumulator.getStatus() != ResponseStatus.RUNNING) {
                            accumulator.setStatus(genericResponse.getStatus());
                            accumulator.setMessage(genericResponse.getMessage());
                        }
                        if (genericResponse.getModel() != null) {
                            accumulator.getModel().getAnnotations().putAll(genericResponse.getModel().getAnnotations());
                        }
                    }

                    return accumulator;
                });

        return Response.ok(genericResponseMerged).build();
    }
}
