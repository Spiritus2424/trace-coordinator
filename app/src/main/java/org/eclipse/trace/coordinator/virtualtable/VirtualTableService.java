package org.eclipse.trace.coordinator.virtualtable;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.table.ColumnHeaderEntry;
import org.eclipse.tsp.java.client.api.table.TableModel;
import org.eclipse.tsp.java.client.shared.query.Query;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.constraints.NotNull;

@ApplicationScoped
public class VirtualTableService {

    public CompletableFuture<GenericResponse<List<ColumnHeaderEntry>>> getColumns(@NotNull TraceServer traceServer,
            @NotNull UUID experimentUuid,
            @NotNull String outputId, Query query) {
        return traceServer.getTspClient().getTableApiAsync()
                .getTableColumns(experimentUuid, outputId, query)
                .thenApply(response -> {

                    return new GenericResponse<List<ColumnHeaderEntry>>(
                            Arrays.asList(response.getResponseModel().getModel()),
                            response.getResponseModel().getStatus(), response.getResponseModel().getMessage());
                });
    }

    public CompletableFuture<GenericResponse<TableModel>> getLines(@NotNull TraceServer traceServer,
            @NotNull UUID experimentUuid,
            @NotNull String outputId, Query query) {
        return traceServer.getTspClient().getTableApiAsync().getTableLines(experimentUuid, outputId, query)
                .thenApply(response -> response.getResponseModel());
    }
}
