package org.eclipse.trace.coordinator.xy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.trace.coordinator.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.api.xy.XyModel;
import org.eclipse.tsp.java.client.shared.entry.Entry;
import org.eclipse.tsp.java.client.shared.entry.EntryHeader;
import org.eclipse.tsp.java.client.shared.entry.EntryModel;
import org.eclipse.tsp.java.client.shared.query.Query;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;
import org.eclipse.tsp.java.client.shared.response.ResponseStatus;

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

@Path("experiments/{expUUID}/outputs/XY/{outputId}")
@ApplicationScoped
public class XyController {

    @Inject
    XyService xyService;

    @Inject
    TraceServerManager traceServerManager;

    @POST
    @Path("xy")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getXy(@PathParam("expUUID") UUID experimentUuid, @PathParam("outputId") String outputId,
            Query query) {
        GenericResponse<XyModel> genericResponseMerged = this.traceServerManager.getTraceServers()
                .stream()
                .map((TraceServer traceServer) -> this.xyService.getXy(traceServer, experimentUuid, outputId, query))
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
                            accumulator.getModel().getSeries().addAll(genericResponse.getModel().getSeries());
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
        GenericResponse<EntryModel<Entry>> genericResponseMerged = this.traceServerManager.getTraceServers()
                .stream()
                .map((TraceServer traceServer) -> this.xyService.getTree(traceServer, experimentUuid, outputId, query))
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
                            headers.addAll(genericResponse.getModel().getHeaders());
                            accumulator.getModel().getEntries().addAll(genericResponse.getModel().getEntries());
                        }
                    }

                    return accumulator;
                });

        genericResponseMerged.getModel().setHeaders(new ArrayList<>(headers));
        return Response.ok(genericResponseMerged).build();
    }

    @GET
    @Path("tooltip")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTooltip() {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }
}
