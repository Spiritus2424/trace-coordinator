package org.eclipse.trace.coordinator.core.virtualtable;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.eclipse.trace.coordinator.core.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.table.ColumnHeaderEntry;
import org.eclipse.tsp.java.client.api.table.TableModel;
import org.eclipse.tsp.java.client.api.table.dto.GetTableLinesRequestDto;
import org.eclipse.tsp.java.client.core.tspclient.TspClientResponse;
import org.eclipse.tsp.java.client.shared.query.Body;
import org.eclipse.tsp.java.client.shared.query.Query;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;
import org.jvnet.hk2.annotations.Service;

@Service
public class VirtualTableService {

	public CompletableFuture<GenericResponse<List<ColumnHeaderEntry>>> getColumns(
			final TraceServer traceServer,
			final UUID experimentUuid,
			final String outputId,
			final Query query) {
		return traceServer.getTspClient().getTableApiAsync().getTableColumns(experimentUuid, outputId, query)
				.thenApply(TspClientResponse::getResponseModel);
	}

	public CompletableFuture<GenericResponse<TableModel>> getLines(
			final TraceServer traceServer,
			final UUID experimentUuid,
			final String outputId,
			final Body<GetTableLinesRequestDto> body) {
		return traceServer.getTspClient().getTableApiAsync().getTableLines(experimentUuid, outputId, body)
				.thenApply(TspClientResponse::getResponseModel);
	}
}
