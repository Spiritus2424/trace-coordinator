package org.eclipse.trace.coordinator.trace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.trace.coordinator.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.api.trace.Trace;
import org.eclipse.tsp.java.client.shared.query.Query;

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
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TraceController {

    @Inject
    private TraceService traceService;

    @Inject
    private TraceServerManager traceServerManager;

    @PostConstruct
    public void openTraces() {
        for (TraceServer traceServer : this.traceServerManager.getTraceServers()) {
            for (String tracePath : traceServer.getTracesPath()) {
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("uri", tracePath);
                String[] uri = tracePath.split("/");
                parameters.put("name",
                        String.format("%s$%s", traceServer.getHost(), uri[uri.length - 1]));
                this.traceService.openTrace(traceServer, new Query(parameters));
            }
        }
    }

    @GET
    public Response getTraces() {
        List<Trace> traces = this.traceServerManager.getTraceServers().stream()
                .map((TraceServer traceServer) -> this.traceService.getTraces(traceServer))
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        return Response.ok(traces).build();
    }

    @GET
    @Path("{uuid}")
    public Response getTrace(@PathParam("uuid") @NotNull UUID traceUuid) {
        Optional<Trace> trace = this.traceServerManager.getTraceServers().stream()
                .map((TraceServer traceServer) -> this.traceService.getTrace(traceServer, traceUuid))
                .map(CompletableFuture::join)
                .findFirst();
        Response response = null;

        if (trace.isPresent()) {
            response = Response.ok(trace.get()).build();
        } else {
            response = Response.status(Status.NOT_FOUND).entity("No Such Trace").build();
        }

        return response;
    }

    @POST
    public Response openTrace(@NotNull Query query) {
        final String uri = (String) query.getParameters().get("uri");
        List<Trace> traces = this.traceServerManager.getTraceServers().stream()
                .map((TraceServer traceServer) -> {
                    /**
                     * Fix Trace Server: The trace server should put a name by default if it is not
                     * provide
                     */
                    String traceName = (String) query.getParameters().get("name");
                    if (traceName == null) {
                        String[] uriSplit = uri.split("/");
                        traceName = uriSplit[uriSplit.length - 1];
                    }
                    Map<String, Object> parameters = new HashMap<>();
                    parameters.put("uri", uri);
                    parameters.put("name", String.format("%s$%s", traceServer.getHost(), traceName.replace("/", "\\")));
                    return this.traceService.openTrace(traceServer, new Query(parameters));
                })
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        Response response = null;
        if (traces.size() != 0) {
            response = Response.ok(traces).build();
        } else {
            response = Response.status(Status.NOT_FOUND).entity("No Such Trace").build();
        }

        return response;
    }

    @DELETE
    @Path("{uuid}")
    public Response deleteTrace(@PathParam("uuid") @NotNull UUID traceUuid) {
        Optional<Trace> trace = this.traceServerManager.getTraceServers().stream()
                .map((TraceServer traceServer) -> this.traceService.deleteTrace(traceServer, traceUuid))
                .map(CompletableFuture::join)
                .findFirst();

        Response response = null;
        if (trace.isPresent()) {
            response = Response.ok(trace.get()).build();
        } else {
            response = Response.status(Status.NOT_FOUND).entity("No Such Trace").build();
        }

        return response;
    }

}
