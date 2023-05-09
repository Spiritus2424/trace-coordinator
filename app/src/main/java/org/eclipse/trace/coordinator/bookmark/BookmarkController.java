package org.eclipse.trace.coordinator.bookmark;

import java.util.UUID;

import org.eclipse.trace.coordinator.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.api.bookmark.Bookmark;

import jakarta.enterprise.context.ApplicationScoped;
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

@Path("experiments/{expUUID}/bookmarks")
@ApplicationScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BookmarkController {
	@Inject
	private BookmarkService bookmarkService;

	@Inject
	private TraceServerManager traceServerManager;

	@GET
	public Response getBookmarks(@NotNull @PathParam("expUUID") final UUID experimentUuid) {
		return Response.status(Status.NOT_IMPLEMENTED).build();
	}

	@GET
	@Path("{bookmarkId}")
	public Response getBookmark(
			@NotNull @PathParam("expUUID") final UUID experimentUuid,
			@NotNull @PathParam("bookmarkId") final UUID bookmarkUuid) {
		return Response.status(Status.NOT_IMPLEMENTED).build();
	}

	@POST
	public Response createBookmark(
			@NotNull @PathParam("expUUID") final UUID experimentUuid,
			@NotNull @Valid final Bookmark bookmark) {
		return Response.status(Status.NOT_IMPLEMENTED).build();
	}

	@PUT
	@Path("{bookmarkId}")
	public Response updateBookmark(
			@NotNull @PathParam("expUUID") final UUID experimentUuid,
			@NotNull @PathParam("bookmarkId") final UUID bookmarkUuid,
			@NotNull @Valid final Bookmark bookmark) {
		return Response.status(Status.NOT_IMPLEMENTED).build();
	}

	@DELETE
	@Path("{bookmarkId}")
	public Response deleteBookmark(
			@NotNull @PathParam("expUUID") final UUID experimentUuid,
			@NotNull @PathParam("bookmarkId") final UUID bookmarkUuid) {
		return Response.status(Status.NOT_IMPLEMENTED).build();
	}
}
