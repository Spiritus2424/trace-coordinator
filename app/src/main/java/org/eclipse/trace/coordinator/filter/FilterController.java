package org.eclipse.trace.coordinator.filter;

import org.eclipse.trace.coordinator.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.api.filter.Filter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("filters")
@ApplicationScoped
public class FilterController {

    @Inject
    private FilterService filterService;

    @Inject
    private TraceServerManager traceServerManager;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilters() {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }

    @GET
    @Path("{filterId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFilter(@NotNull @PathParam("filterId") final String filterId) {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createFilter(@NotNull @Valid final Filter Filter) {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }

    @PUT
    @Path("{filterId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateFilter(
            @NotNull @PathParam("filterId") final String filterId,
            @NotNull @Valid final Filter filter) {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }

    @DELETE
    @Path("{filterId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFilter(@NotNull @PathParam("filterId") final String filterId) {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }

}
