package org.eclipse.trace.coordinator.xy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.trace.coordinator.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.models.entry.Entry;
import org.eclipse.tsp.java.client.models.entry.EntryHeader;
import org.eclipse.tsp.java.client.models.entry.EntryModel;
import org.eclipse.tsp.java.client.models.query.Query;
import org.eclipse.tsp.java.client.models.response.GenericResponse;
import org.eclipse.tsp.java.client.models.response.ResponseStatus;
import org.eclipse.tsp.java.client.models.xy.XYModel;
import org.eclipse.tsp.java.client.models.xy.XYSerie;

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
        List<XYSerie> series = new ArrayList<>();
        String title = null;
        ResponseStatus responseStatus = ResponseStatus.COMPLETED;
        String statusMessage = null;
        for (TraceServer traceServer : this.traceServerManager.getTraceServers()) {
            GenericResponse<XYModel> genericResponse = this.xyService.getXy(traceServer, experimentUuid, outputId,
                    query);

            if (responseStatus != ResponseStatus.RUNNING) {
                responseStatus = genericResponse.getStatus();
                statusMessage = genericResponse.getMessage();
            }

            if (genericResponse.getModel() != null) {
                series.addAll(genericResponse.getModel().getSeries());
            }

            if (title == null) {
                title = genericResponse.getModel().getTitle();
            }
        }

        return Response.ok(new GenericResponse<XYModel>(new XYModel(title, series), responseStatus, statusMessage))
                .build();
    }

    @POST
    @Path("tree")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTree(@PathParam("expUUID") UUID experimentUuid, @PathParam("outputId") String outputId,
            Query query) {
        Set<EntryHeader> headers = new HashSet<>();
        List<Entry> entries = new ArrayList<>();
        ResponseStatus responseStatus = ResponseStatus.COMPLETED;
        String statusMessage = null;

        for (TraceServer traceServer : this.traceServerManager.getTraceServers()) {
            GenericResponse<EntryModel<Entry>> genericResponse = this.xyService.getTree(traceServer, experimentUuid,
                    outputId,
                    query);

            if (responseStatus != ResponseStatus.RUNNING) {
                responseStatus = genericResponse.getStatus();
                statusMessage = genericResponse.getMessage();
            }

            if (genericResponse.getModel() != null) {
                headers.addAll(genericResponse.getModel().getHeaders());
                entries.addAll(genericResponse.getModel().getEntries());
            }

        }
        int counter = 0;
        for (Entry entry : entries) {
            entry.setId(counter++);
        }

        return Response
                .ok(new GenericResponse<EntryModel<Entry>>(new EntryModel<Entry>(new ArrayList<>(headers), entries),
                        responseStatus,
                        statusMessage))
                .build();
    }

    @GET
    @Path("tooltip")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTooltip() {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }
}
