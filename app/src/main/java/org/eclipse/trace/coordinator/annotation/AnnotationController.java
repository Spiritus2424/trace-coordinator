package org.eclipse.trace.coordinator.annotation;

import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("experiments/{expUUID}/outputs/{outputId}/annotations")
@ApplicationScoped
public class AnnotationController {

    @Inject
    private AnnotationService annotationService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAnnotationCategories(UUID experimentUuid, UUID outputId) {

        return Response.ok().build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAnnotation(UUID experimentUuid, UUID outputId) {

        return Response.ok().build();
    }
}
