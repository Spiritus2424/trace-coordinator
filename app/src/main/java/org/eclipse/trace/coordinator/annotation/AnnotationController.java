package org.eclipse.trace.coordinator.annotation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.trace.coordinator.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.models.annotation.AnnotationCategoriesModel;
import org.eclipse.tsp.java.client.models.query.Query;
import org.eclipse.tsp.java.client.models.response.GenericResponse;
import org.eclipse.tsp.java.client.models.response.ResponseStatus;

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
    TraceServerManager traceServerManager;

    @Inject
    private AnnotationService annotationService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAnnotationCategories(@PathParam("expUUID") UUID experimentUuid,
            @PathParam("outputId") String outputId,
            @QueryParam("markerSetId") String markerSetId) {
        Set<String> annotationCategories = new HashSet<>();
        ResponseStatus responseStatus = ResponseStatus.COMPLETED;
        String statusMessage = null;
        for (TraceServer traceServer : traceServerManager.getTraceServers()) {
            GenericResponse<AnnotationCategoriesModel> genericResponse = this.annotationService
                    .getAnnotationCategories(traceServer, experimentUuid, outputId,
                            markerSetId != null ? Optional.of(markerSetId) : Optional.empty());
            if (responseStatus != ResponseStatus.RUNNING) {
                responseStatus = genericResponse.getStatus();
                statusMessage = genericResponse.getMessage();
            }
            if (genericResponse.getModel() != null) {
                annotationCategories.addAll(genericResponse.getModel().getAnnotationCategories());
            }
        }

        return Response.ok(new GenericResponse<AnnotationCategoriesModel>(
                new AnnotationCategoriesModel(new ArrayList<>(annotationCategories)), responseStatus, statusMessage))
                .build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAnnotation(@PathParam("expUUID") UUID experimentUuid,
            @PathParam("outputId") String outputId, @NotNull Query query) {

        return Response.ok().build();
    }
}
