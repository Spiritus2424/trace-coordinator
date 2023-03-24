package org.eclipse.trace.coordinator.timegraph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.trace.coordinator.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphArrow;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphEntry;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphModel;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphRow;
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
        List<TimeGraphArrow> timeGraphArrows = new ArrayList<>();
        ResponseStatus responseStatus = ResponseStatus.COMPLETED;
        String statusMessage = null;
        for (TraceServer traceServer : this.traceServerManager.getTraceServers()) {
            GenericResponse<TimeGraphArrow[]> genericResponse = this.timeGraphService.getArrows(traceServer,
                    experimentUuid, outputId, query);

            if (responseStatus != ResponseStatus.RUNNING) {
                responseStatus = genericResponse.getStatus();
                statusMessage = genericResponse.getMessage();
            }

            if (genericResponse.getModel() != null) {
                timeGraphArrows.addAll(Arrays.asList(genericResponse.getModel()));
            }
        }
        return Response.ok(new GenericResponse<List<TimeGraphArrow>>(timeGraphArrows, responseStatus, statusMessage))
                .build();
    }

    @POST
    @Path("states")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStates(@PathParam("expUUID") UUID experimentUuid, @PathParam("outputId") String outputId,
            Query query) {
        List<TimeGraphRow> rows = new ArrayList<>();
        ResponseStatus responseStatus = ResponseStatus.COMPLETED;
        String statusMessage = null;

        for (TraceServer traceServer : this.traceServerManager.getTraceServers()) {
            GenericResponse<TimeGraphModel> genericResponse = this.timeGraphService.getStates(traceServer,
                    experimentUuid, outputId, query);

            if (responseStatus != ResponseStatus.RUNNING) {
                responseStatus = genericResponse.getStatus();
                statusMessage = genericResponse.getMessage();
            }

            if (genericResponse.getModel() != null) {
                rows.addAll(genericResponse.getModel().getRows());
            }
        }
        return Response.ok(new GenericResponse<TimeGraphModel>(new TimeGraphModel(rows), responseStatus, statusMessage))
                .build();
    }

    @POST
    @Path("tooltip")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTooltips(@PathParam("expUUID") UUID experimentUuid, @PathParam("outputId") String outputId,
            Query query) {

        Map<String, String> tooltips = new HashMap<>();
        ResponseStatus responseStatus = ResponseStatus.COMPLETED;
        String statusMessage = null;

        for (TraceServer traceServer : this.traceServerManager.getTraceServers()) {
            GenericResponse<Map<String, String>> genericResponse = this.timeGraphService.getTooltips(traceServer,
                    experimentUuid, outputId, query);

            if (responseStatus != ResponseStatus.RUNNING) {
                responseStatus = genericResponse.getStatus();
                statusMessage = genericResponse.getMessage();
            }

            if (genericResponse.getModel() != null) {
                tooltips.putAll(genericResponse.getModel());
            }
        }
        return Response.ok(new GenericResponse<Map<String, String>>(tooltips, responseStatus, statusMessage))
                .build();
    }

    @POST
    @Path("tree")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTree(@PathParam("expUUID") UUID experimentUuid, @PathParam("outputId") String outputId,
            Query query) {

        Set<EntryHeader> headers = new HashSet<>();
        List<TimeGraphEntry> entries = new ArrayList<>();
        ResponseStatus responseStatus = ResponseStatus.COMPLETED;
        String statusMessage = null;

        for (TraceServer traceServer : this.traceServerManager.getTraceServers()) {
            GenericResponse<EntryModel<TimeGraphEntry>> genericResponse = this.timeGraphService.getTree(traceServer,
                    experimentUuid, outputId, query);

            if (responseStatus != ResponseStatus.RUNNING) {
                responseStatus = genericResponse.getStatus();
                statusMessage = genericResponse.getMessage();
            }

            if (genericResponse.getModel() != null) {
                headers.addAll(genericResponse.getModel().getHeaders());
                entries.addAll(genericResponse.getModel().getEntries());
            }
        }

        return Response
                .ok(new GenericResponse<EntryModel<TimeGraphEntry>>(
                        new EntryModel<TimeGraphEntry>(new ArrayList<>(headers), entries), responseStatus,
                        statusMessage))
                .build();
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
