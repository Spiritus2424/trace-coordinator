package org.eclipse.trace.coordinator.api.filter;

import org.eclipse.trace.coordinator.core.filter.FilterService;
import org.eclipse.trace.coordinator.core.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.api.filter.Filter;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("filters")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FilterController {

	@Inject
	private FilterService filterService;

	@Inject
	private TraceServerManager traceServerManager;

	@GET
	public Response getFilters() {
		return Response.status(Status.NOT_IMPLEMENTED).build();
	}

	@GET
	@Path("{filterId}")
	public Response getFilter(@NotNull @PathParam("filterId") final String filterId) {
		return Response.status(Status.NOT_IMPLEMENTED).build();
	}

	@POST
	public Response createFilter(@NotNull @Valid final Filter filter) {
		return Response.status(Status.NOT_IMPLEMENTED).build();
	}

	@PUT
	@Path("{filterId}")
	public Response updateFilter(
			@NotNull @PathParam("filterId") final String filterId,
			@NotNull @Valid final Filter filter) {
		return Response.status(Status.NOT_IMPLEMENTED).build();
	}

	@DELETE
	@Path("{filterId}")
	public Response deleteFilter(@NotNull @PathParam("filterId") final String filterId) {
		return Response.status(Status.NOT_IMPLEMENTED).build();
	}

}
