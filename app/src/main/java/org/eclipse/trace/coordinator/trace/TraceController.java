package org.eclipse.trace.coordinator.trace;

import java.util.UUID;

import org.eclipse.tsp.java.client.models.query.Query;
import org.eclipse.tsp.java.client.models.trace.Trace;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("traces")
@ApplicationScoped
public class TraceController {

    @Inject
    TraceService traceService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTraces() {
        return Response.ok(traceService.getTraces()).build();

    }

    @GET
    @Path("{traceUuid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTrace(@PathParam("traceUuid") UUID traceUuid) {
        Trace trace = traceService.getTrace(traceUuid.toString());
        Response response;
        if (trace == null) {
            response = Response.status(Status.NOT_FOUND).entity("No Such Trace").build();
        } else {
            response = Response.ok(trace).build();
        }

        return response;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response openTrace(Query query) {

        Trace trace = traceService.openTrace(query);
        Response response;
        if (trace == null) {
            response = Response.status(Status.NOT_FOUND).entity("No Such Trace").build();
        } else {
            response = Response.ok(trace).build();
        }

        return response;
    }

}
