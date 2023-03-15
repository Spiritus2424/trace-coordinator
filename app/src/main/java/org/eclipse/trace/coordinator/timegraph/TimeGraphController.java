package org.eclipse.trace.coordinator.timegraph;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("experiments/{expUUID}/outputs/timeGraph/{outputId}")
@ApplicationScoped
public class TimeGraphController {

    @POST
    @Path("arrows")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArrows() {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }

    @POST
    @Path("states")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStates() {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }

    @POST
    @Path("tooltip")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTooltips() {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }

    @POST
    @Path("tree")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTree() {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }

    @POST
    @Path("navigate/states")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNavigations() {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }
}
