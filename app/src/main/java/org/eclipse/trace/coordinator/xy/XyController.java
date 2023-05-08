package org.eclipse.trace.coordinator.xy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.trace.coordinator.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.api.xy.XyModel;
import org.eclipse.tsp.java.client.api.xy.dto.GetXyModelRequestDto;
import org.eclipse.tsp.java.client.api.xy.dto.GetXyTreeRequestDto;
import org.eclipse.tsp.java.client.shared.entry.Entry;
import org.eclipse.tsp.java.client.shared.entry.EntryHeader;
import org.eclipse.tsp.java.client.shared.entry.EntryModel;
import org.eclipse.tsp.java.client.shared.query.Body;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;
import org.eclipse.tsp.java.client.shared.response.ResponseStatus;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class XyController {

	@Inject
	private XyService xyService;

	@Inject
	private TraceServerManager traceServerManager;

	@POST
	@Path("xy")
	public Response getXy(
			@NotNull @PathParam("expUUID") final UUID experimentUuid,
			@NotNull @PathParam("outputId") final String outputId,
			@NotNull @Valid final Body<GetXyModelRequestDto> body) {
		final GenericResponse<XyModel> genericResponseMerged = this.traceServerManager.getTraceServers()
				.stream()
				.map((TraceServer traceServer) -> this.xyService.getXy(traceServer, experimentUuid, outputId, body))
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
							accumulator.getModel().getSeries().addAll(genericResponse.getModel().getSeries());
						}
					}

					return accumulator;
				});

		return Response.ok(genericResponseMerged).build();
	}

	@POST
	@Path("tree")
	public Response getTree(
			@NotNull @PathParam("expUUID") final UUID experimentUuid,
			@NotNull @PathParam("outputId") final String outputId,
			@NotNull @Valid final Body<GetXyTreeRequestDto> body) {
		final Set<EntryHeader> headers = new HashSet<>();
		final GenericResponse<EntryModel<Entry>> genericResponseMerged = this.traceServerManager.getTraceServers()
				.stream()
				.map((TraceServer traceServer) -> this.xyService.getTree(traceServer, experimentUuid, outputId, body))
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
	public Response getTooltips() {
		return Response.status(Status.NOT_IMPLEMENTED).build();
	}
}
