package org.eclipse.trace.coordinator.trace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.trace.coordinator.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.models.query.Query;
import org.eclipse.tsp.java.client.models.trace.Trace;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
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

    @Inject
    private TraceServerManager traceServerManager;

    @PostConstruct
    public void openTraces() {
        for (TraceServer traceServer : traceServerManager.getTraceServers()) {
            for (String tracePath : traceServer.getTracesPath()) {
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("uri", tracePath);
                String[] uri = tracePath.split("/");
                parameters.put("name",
                        String.format("%s$%s", traceServer.getHost(), uri[uri.length - 1]));
                traceService.openTrace(traceServer, new Query(parameters));
            }
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getTraces() {
        List<Trace> traces = new ArrayList<>();
        for (TraceServer traceServer : this.traceServerManager.getTraceServers()) {
            traces.addAll(this.traceService.getTraces(traceServer));
        }

        return Response.ok(traces).build();
    }

    @GET
    @Path("{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getTrace(@PathParam("uuid") @NotNull UUID traceUuid) {
        Response response = null;

        for (TraceServer traceServer : traceServerManager.getTraceServers()) {
            Trace trace = traceService.getTrace(traceServer, traceUuid);
            if (trace != null) {
                response = Response.ok(trace).build();
                break;
            } else {
                response = Response.status(Status.NOT_FOUND).entity("No Such Trace").build();
            }
        }

        return response;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response openTrace(@NotNull Query query) {
        Response response = null;
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("uri", query.getParameters().get("uri"));
        String traceName = (String) query.getParameters().get("name");

        if (traceName == null) {
            System.out.println(query.getParameters().get("name"));
            String[] uri = ((String) query.getParameters().get("uri")).split("/");
            traceName = uri[uri.length - 1];
        }

        List<Trace> traces = new ArrayList<>();
        for (TraceServer traceServer : traceServerManager.getTraceServers()) {
            /**
             * Fix Trace Server: The trace server should put a name by default if it is not
             * provide
             */
            parameters.put("name", String.format("%s$%s", traceServer.getHost(), traceName.replace("/", "\\")));
            System.out.println(parameters.get("name"));
            traces.addAll(traceService.openTrace(traceServer, new Query(parameters)));
        }

        if (traces.size() != 0) {
            response = Response.ok(traces).build();
        } else {
            response = Response.status(Status.NOT_FOUND).entity("No Such Trace").build();
        }

        return response;
    }

    @DELETE
    @Path("{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteTrace(@PathParam("uuid") @NotNull UUID traceUuid) {
        Response response = null;
        for (TraceServer traceServer : traceServerManager.getTraceServers()) {
            Trace trace = traceService.deleteTrace(traceServer, traceUuid);
            if (trace != null) {
                response = Response.ok(trace).build();
                break;
            } else {
                response = Response.status(Status.NOT_FOUND).entity("No Such Trace").build();
            }
        }

        return response;
    }

}
