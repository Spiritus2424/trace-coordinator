package org.eclipse.trace.coordinator.symbol;

import org.eclipse.trace.coordinator.traceserver.TraceServerManager;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("symbols")
@ApplicationScoped
public class SymbolController {

    @Inject
    private SymbolService symbolService;

    @Inject
    private TraceServerManager traceServerManager;

    @GET
    @Path("{hostId}/{PID}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSymbolProvider(
            @NotNull @PathParam("hostId") final String hostId,
            @NotNull @PathParam("PID") final String pid) {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }

    @POST
    @Path("{hostId}/{PID}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSymbolProvider(
            @NotNull @PathParam("hostId") final String hostId,
            @NotNull @PathParam("PID") final String pid,
            final String url) {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }
}
