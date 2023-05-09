package org.eclipse.trace.coordinator.api.virtualtable;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.eclipse.trace.coordinator.core.traceserver.TraceServer;
import org.eclipse.trace.coordinator.core.traceserver.TraceServerManager;
import org.eclipse.trace.coordinator.core.virtualtable.VirtualTableService;
import org.eclipse.tsp.java.client.api.table.ColumnHeaderEntry;
import org.eclipse.tsp.java.client.api.table.TableModel;
import org.eclipse.tsp.java.client.api.table.dto.GetTableLinesRequestDto;
import org.eclipse.tsp.java.client.shared.query.Body;
import org.eclipse.tsp.java.client.shared.query.Query;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;
import org.eclipse.tsp.java.client.shared.response.ResponseStatus;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("experiments/{expUUID}/outputs/table/{outputId}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VirtualTableController {

	@Inject
	private VirtualTableService virtualTableService;

	@Inject
	private TraceServerManager traceServerManager;

	@POST
	@Path("columns")
	public Response getColumns(@PathParam("expUUID") @NotNull UUID experimentUuid,
			@PathParam("outputId") @NotNull String outputId, @NotNull Query query) {
		GenericResponse<List<ColumnHeaderEntry>> genericResponseMerged = this.traceServerManager.getTraceServers()
				.stream()
				.map((TraceServer traceServer) -> this.virtualTableService.getColumns(traceServer, experimentUuid,
						outputId, query))
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
	@Path("lines")
	public Response getLines(
			@NotNull @PathParam("expUUID") final UUID experimentUuid,
			@NotNull @PathParam("outputId") final String outputId,
			@NotNull @Valid final Body<GetTableLinesRequestDto> body) {
		final GenericResponse<TableModel> genericResponseMerged = this.traceServerManager.getTraceServers()
				.stream()
				.map((TraceServer traceServer) -> this.virtualTableService.getLines(traceServer, experimentUuid,
						outputId, body))
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
							accumulator.getModel().getColumnIds().addAll(genericResponse.getModel().getColumnIds());
							accumulator.getModel().getLines().addAll(genericResponse.getModel().getLines());
							accumulator.getModel().setLowIndex(Math.min(accumulator.getModel().getLowIndex(),
									genericResponse.getModel().getLowIndex()));
							accumulator.getModel()
									.setSize(accumulator.getModel().getSize() + genericResponse.getModel().getSize());
						}
					}

					return accumulator;
				});

		return Response.ok(genericResponseMerged).build();
	}
}
