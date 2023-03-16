package org.eclipse.trace.coordinator.bookmark;

import java.util.UUID;

import org.eclipse.trace.coordinator.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.api.bookmark.Bookmark;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
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
public class BookmarkController {
    @Inject
    private BookmarkService bookmarkService;

    @Inject
    private TraceServerManager traceServerManager;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookmarks(@PathParam("expUUID") UUID experimentUuid) {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }

    @GET
    @Path("{bookmarkId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookmark(@PathParam("expUUID") UUID experimentUuid, @PathParam("bookmarkId") UUID bookmarkUuid) {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBookmark(@PathParam("expUUID") UUID experimentUuid, Bookmark bookmark) {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }

    @PUT
    @Path("{bookmarkId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBookmark(@PathParam("expUUID") UUID experimentUuid,
            @PathParam("bookmarkId") UUID bookmarkUuid, Bookmark bookmark) {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }

    @DELETE
    @Path("{bookmarkId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBookmark(@PathParam("expUUID") UUID experimentUuid,
            @PathParam("bookmarkId") UUID bookmarkUuid) {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }
}
