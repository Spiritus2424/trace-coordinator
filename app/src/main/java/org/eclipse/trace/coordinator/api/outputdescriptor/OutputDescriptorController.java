package org.eclipse.trace.coordinator.api.outputdescriptor;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.eclipse.trace.coordinator.core.outputdescriptor.OutputDescriptorService;
import org.eclipse.trace.coordinator.core.traceserver.TraceServer;
import org.eclipse.trace.coordinator.core.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.api.outputdescriptor.OutputDescriptor;

import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/experiments/{expUUID}/outputs")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class OutputDescriptorController {

	@Inject
	private OutputDescriptorService outputDescriptorService;

	@Inject
	private TraceServerManager traceServerManager;

	@GET
	public Response getOutputDescriptors(@NotNull @PathParam("expUUID") final UUID experimentUuid) {
		final Set<OutputDescriptor> outputDescriptors = this.traceServerManager.getTraceServers()
				.stream()
				.map((TraceServer traceServer) -> this.outputDescriptorService.getOutputDescriptors(traceServer,
						experimentUuid))
				.map(CompletableFuture::join)
				.flatMap(List::stream)
				.collect(Collectors.toSet());

		return Response.ok(outputDescriptors.toArray()).build();
	}

	@GET
	@Path("{outputId}")
	public Response getOutputDescriptor(
			@NotNull @PathParam("expUUID") final UUID experimentUuid,
			@NotNull @PathParam("outputId") final String outputId) {
		return Response.status(Status.NOT_IMPLEMENTED).build();
	}
}
