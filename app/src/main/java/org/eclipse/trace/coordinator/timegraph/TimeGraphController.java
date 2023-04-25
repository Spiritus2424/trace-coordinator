package org.eclipse.trace.coordinator.timegraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.trace.coordinator.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphArrow;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphEntry;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphModel;
import org.eclipse.tsp.java.client.shared.entry.EntryHeader;
import org.eclipse.tsp.java.client.shared.entry.EntryModel;
import org.eclipse.tsp.java.client.shared.query.Query;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;
import org.eclipse.tsp.java.client.shared.response.ResponseStatus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("experiments/{expUUID}/outputs/timeGraph/{outputId}")
@ApplicationScoped
public class TimeGraphController {

    @Inject
    private TimeGraphService timeGraphService;

    @Inject
    private TraceServerManager traceServerManager;

    @POST
    @Path("arrows")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getArrows(@PathParam("expUUID") UUID experimentUuid, @PathParam("outputId") String outputId,
            Query query) {
        GenericResponse<List<TimeGraphArrow>> genericResponseMerged = this.traceServerManager.getTraceServers()
                .stream()
                .map((TraceServer traceServer) -> this.timeGraphService.getArrows(traceServer, experimentUuid, outputId,
                        query))
                .map(CompletableFuture::join)
                .reduce(null, (accumulator, genericResponse) -> {
                    if (accumulator == null) {
                        accumulator = genericResponse;
                    } else {
                        if (accumulator.getStatus() != ResponseStatus.RUNNING) {
                            accumulator.setStatus(genericResponse.getStatus());
                            accumulator.setMessage(genericResponse.getMessage());
                        }
                        if (genericResponse.getModel() != null) {
                            accumulator.getModel().addAll(genericResponse.getModel());
                        }
                    }

                    return accumulator;
                });

        return Response.ok(genericResponseMerged).build();
    }

    @POST
    @Path("states")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStates(@PathParam("expUUID") UUID experimentUuid, @PathParam("outputId") String outputId,
            Query query) {
        GenericResponse<TimeGraphModel> genericResponseMerged = this.traceServerManager.getTraceServers()
                .stream()
                .map((TraceServer traceServer) -> this.timeGraphService.getStates(traceServer, experimentUuid, outputId,
                        query))
                .map(CompletableFuture::join)
                .reduce(null, (accumulator, genericResponse) -> {
                    if (accumulator == null) {
                        accumulator = genericResponse;
                    } else {
                        if (accumulator.getStatus() != ResponseStatus.RUNNING) {
                            accumulator.setStatus(genericResponse.getStatus());
                            accumulator.setMessage(genericResponse.getMessage());
                        }
                        if (genericResponse.getModel() != null) {
                            accumulator.getModel().getRows().addAll(genericResponse.getModel().getRows());
                        }
                    }

                    return accumulator;
                });

        return Response.ok(genericResponseMerged).build();
    }

    @POST
    @Path("tooltip")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTooltips(@PathParam("expUUID") UUID experimentUuid, @PathParam("outputId") String outputId,
            Query query) {
        GenericResponse<Map<String, String>> genericResponseMerged = this.traceServerManager.getTraceServers()
                .stream()
                .map((TraceServer traceServer) -> this.timeGraphService.getTooltips(traceServer, experimentUuid,
                        outputId,
                        query))
                .filter(Objects::nonNull)
                .map(CompletableFuture::join)
                .reduce(null, (accumulator, genericResponse) -> {
                    if (accumulator == null) {
                        accumulator = genericResponse;
                    } else {
                        if (accumulator.getStatus() != ResponseStatus.RUNNING) {
                            accumulator.setStatus(genericResponse.getStatus());
                            accumulator.setMessage(genericResponse.getMessage());
                        }
                        if (genericResponse.getModel() != null) {
                            accumulator.getModel().putAll(genericResponse.getModel());
                        }
                    }

                    return accumulator;
                });

        return Response.ok(genericResponseMerged).build();
    }

    @POST
    @Path("tree")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTree(@PathParam("expUUID") UUID experimentUuid, @PathParam("outputId") String outputId,
            Query query) {

        Set<EntryHeader> headers = new HashSet<>();
        GenericResponse<EntryModel<TimeGraphEntry>> genericResponseMerged = this.traceServerManager.getTraceServers()
                .stream()
                .map((TraceServer traceServer) -> this.timeGraphService.getTree(traceServer, experimentUuid, outputId,
                        query))
                .map(CompletableFuture::join)
                .reduce(null, (accumulator, genericResponse) -> {
                    if (accumulator == null) {
                        accumulator = genericResponse;
                    } else {
                        if (accumulator.getStatus() != ResponseStatus.RUNNING) {
                            accumulator.setStatus(genericResponse.getStatus());
                            accumulator.setMessage(genericResponse.getMessage());
                        }
                        if (genericResponse.getModel() != null) {
                            accumulator.getModel().getEntries().addAll(genericResponse.getModel().getEntries());
                            headers.addAll(genericResponse.getModel().getHeaders());
                        }
                    }
                    return accumulator;
                });

        genericResponseMerged.getModel().setHeaders(new ArrayList<>(headers));
        return Response.ok(genericResponseMerged).build();
    }

    @POST
    @Path("navigate/states")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNavigations(@PathParam("expUUID") UUID experimentUuid, @PathParam("outputId") String outputId,
            Query query) {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }
}
