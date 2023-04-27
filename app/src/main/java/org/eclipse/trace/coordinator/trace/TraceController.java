package org.eclipse.trace.coordinator.trace;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.trace.coordinator.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.api.trace.Trace;
import org.eclipse.tsp.java.client.api.trace.dto.OpenTraceRequestDto;
import org.eclipse.tsp.java.client.shared.query.Body;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("traces")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TraceController {

	@Inject
	private TraceService traceService;

	@Inject
	private TraceServerManager traceServerManager;

	@PostConstruct
	public void openTraces() {
		for (TraceServer traceServer : this.traceServerManager.getTraceServers()) {
			for (String tracePath : traceServer.getTracesPath()) {
				String[] uri = tracePath.split("/");
				OpenTraceRequestDto openTraceRequestDto = new OpenTraceRequestDto(tracePath);
				openTraceRequestDto.setName(String.format("%s$%s", traceServer.getHost(), uri[uri.length - 1]));
				this.traceService.openTrace(traceServer, new Body<OpenTraceRequestDto>(openTraceRequestDto));
			}
		}
	}

	@GET
	public Response getTraces() {
		final List<Trace> traces = this.traceServerManager.getTraceServers().stream()
				.map((TraceServer traceServer) -> this.traceService.getTraces(traceServer))
				.map(CompletableFuture::join)
				.flatMap(List::stream)
				.collect(Collectors.toList());

		return Response.ok(traces).build();
	}

	@GET
	@Path("{uuid}")
	public Response getTrace(@NotNull @PathParam("uuid") final UUID traceUuid) {
		final Optional<Trace> trace = this.traceServerManager.getTraceServers().stream()
				.map((TraceServer traceServer) -> this.traceService.getTrace(traceServer, traceUuid))
				.map(CompletableFuture::join)
				.findFirst();
		Response response = null;

		if (trace.isPresent()) {
			response = Response.ok(trace.get()).build();
		} else {
			response = Response.status(Status.NOT_FOUND).entity("No Such Trace").build();
		}

		return response;
	}

	@POST
	public Response openTrace(@NotNull @Valid final Body<OpenTraceRequestDto> body) {
		final List<Trace> traces = this.traceServerManager.getTraceServers().stream()
				.map((TraceServer traceServer) -> {
					/**
					 * Fix Trace Server: The trace server should put a name by default if it is not
					 * provide
					 */
					String traceName = body.getParameters().getName();
					if (traceName == null) {
						final String[] uriSplit = body.getParameters().getUri().split("/");
						traceName = uriSplit[uriSplit.length - 1];
					}

					OpenTraceRequestDto openTraceRequestDto = new OpenTraceRequestDto(body.getParameters().getUri(),
							String.format("%s$%s", traceServer.getHost(), traceName.replace("/", "\\")), null);
					return this.traceService.openTrace(traceServer, new Body<OpenTraceRequestDto>(openTraceRequestDto));
				})
				.map(CompletableFuture::join)
				.flatMap(List::stream)
				.collect(Collectors.toList());

		Response response = null;
		if (traces.size() != 0) {
			response = Response.ok(traces).build();
		} else {
			response = Response.status(Status.NOT_FOUND).entity("No Such Trace").build();
		}

		return response;
	}

	@DELETE
	@Path("{uuid}")
	public Response deleteTrace(@NotNull @PathParam("uuid") final UUID traceUuid) {
		final Optional<Trace> trace = this.traceServerManager.getTraceServers().stream()
				.map((TraceServer traceServer) -> this.traceService.deleteTrace(traceServer, traceUuid))
				.map(CompletableFuture::join)
				.findFirst();

		Response response = null;
		if (trace.isPresent()) {
			response = Response.ok(trace.get()).build();
		} else {
			response = Response.status(Status.NOT_FOUND).entity("No Such Trace").build();
		}

		return response;
	}

}
