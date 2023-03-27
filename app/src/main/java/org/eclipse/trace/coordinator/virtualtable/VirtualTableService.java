package org.eclipse.trace.coordinator.virtualtable;

import java.util.UUID;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.table.ColumnHeaderEntry;
import org.eclipse.tsp.java.client.api.table.TableModel;
import org.eclipse.tsp.java.client.shared.query.Query;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotNull;

@ApplicationScoped
public class VirtualTableService {

    public GenericResponse<ColumnHeaderEntry[]> getColumns(@NotNull TraceServer traceServer,
            @NotNull UUID experimentUuid,
            @NotNull String outputId, Query query) {
        return traceServer.getTspClient().getTableApi().getTableColumns(experimentUuid, outputId, query)
                .getResponseModel();

    }

    public GenericResponse<TableModel> getLines(@NotNull TraceServer traceServer, @NotNull UUID experimentUuid,
            @NotNull String outputId, Query query) {
        return traceServer.getTspClient().getTableApi().getTableLines(experimentUuid, outputId, query)
                .getResponseModel();

    }
}
