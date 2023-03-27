package org.eclipse.trace.coordinator.virtualtable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.trace.coordinator.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.api.table.ColumnHeaderEntry;
import org.eclipse.tsp.java.client.api.table.Line;
import org.eclipse.tsp.java.client.api.table.TableModel;
import org.eclipse.tsp.java.client.shared.query.Query;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;
import org.eclipse.tsp.java.client.shared.response.ResponseStatus;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("experiments/{expUUID}/outputs/table/{outputId}")
@ApplicationScoped
public class VirtualTableController {

    @Inject
    private VirtualTableService virtualTableService;

    @Inject
    private TraceServerManager traceServerManager;

    @POST
    @Path("columns")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getColumns(@PathParam("expUUID") @NotNull UUID experimentUuid,
            @PathParam("outputId") @NotNull String outputId, @NotNull Query query) {

        List<ColumnHeaderEntry> columnHeaderEntries = new ArrayList<>();
        ResponseStatus responseStatus = ResponseStatus.COMPLETED;
        String statusMessage = null;
        for (TraceServer traceServer : this.traceServerManager.getTraceServers()) {
            GenericResponse<ColumnHeaderEntry[]> genericResponse = this.virtualTableService.getColumns(traceServer,
                    experimentUuid, outputId,
                    query);

            if (responseStatus != ResponseStatus.RUNNING) {
                responseStatus = genericResponse.getStatus();
                statusMessage = genericResponse.getMessage();
            }

            if (genericResponse.getModel() != null) {
                columnHeaderEntries.addAll(Arrays.asList(genericResponse.getModel()));
            }

        }

        return Response
                .ok(new GenericResponse<List<ColumnHeaderEntry>>(columnHeaderEntries, responseStatus, statusMessage))
                .build();
    }

    @POST
    @Path("lines")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getLines(@PathParam("expUUID") @NotNull UUID experimentUuid,
            @PathParam("outputId") @NotNull String outputId, @NotNull Query query) {
        TableModel tableModel = new TableModel();
        Set<Integer> columnsIds = new HashSet<>();
        ResponseStatus responseStatus = ResponseStatus.COMPLETED;
        String statusMessage = null;
        for (TraceServer traceServer : this.traceServerManager.getTraceServers()) {
            GenericResponse<TableModel> genericResponse = this.virtualTableService.getLines(traceServer,
                    experimentUuid, outputId,
                    query);

            if (responseStatus != ResponseStatus.RUNNING) {
                responseStatus = genericResponse.getStatus();
                statusMessage = genericResponse.getMessage();
            }

            if (genericResponse.getModel() != null) {
                columnsIds.addAll(genericResponse.getModel().getColumnIds());
                tableModel.getLines().addAll(genericResponse.getModel().getLines());
                tableModel.setLowIndex(Math.min(tableModel.getLowIndex(), genericResponse.getModel().getLowIndex()));
                tableModel.setSize(tableModel.getSize() + genericResponse.getModel().getSize());
            }
        }
        tableModel.setColumnIds(new ArrayList<>(columnsIds));
        int lineIndex = 0;
        for (Line line : tableModel.getLines()) {
            line.setIndex(lineIndex++);
        }

        return Response
                .ok(new GenericResponse<TableModel>(tableModel, responseStatus, statusMessage))
                .build();

    }
}
