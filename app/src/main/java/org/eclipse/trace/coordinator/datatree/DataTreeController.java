package org.eclipse.trace.coordinator.datatree;

import java.util.UUID;

import org.eclipse.trace.coordinator.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.shared.query.Query;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("experiments/{expUUID}/outputs/data/{outputId}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DataTreeController {

	@Inject
	private DataTreeService dataTreeService;

	@Inject
	private TraceServerManager traceServerManager;

	@POST
	@Path("tree")
	public Response getTree(
			@PathParam("expUUID") final UUID experimentUuid,
			@PathParam("outputId") final String outputId,
			@Valid final Query query) {
		return Response.status(Status.NOT_IMPLEMENTED).build();
	}

}
