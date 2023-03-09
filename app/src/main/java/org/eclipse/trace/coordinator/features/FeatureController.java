package org.eclipse.trace.coordinator.features;

import org.eclipse.trace.coordinator.traceserver.TraceServerManager;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("about")
@ApplicationScoped
public class FeatureController {

    @Inject
    private FeatureService featureService;

    @Inject
    private TraceServerManager traceServerManager;

    @GET
    @Path("traceTypes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSupportedTracesTypes() {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }

    @GET
    @Path("outputTypes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSupportedOutputTypes() {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }
}