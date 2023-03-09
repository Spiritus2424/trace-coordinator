package org.eclipse.trace.coordinator.bookmark;

import java.util.UUID;

import org.eclipse.tsp.java.client.models.bookmark.Bookmark;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("experiments/{expUUID}/bookmarks")
@ApplicationScoped
public class BookmarkController {

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookmarks(UUID experimentUuid) {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }

    @GET
    @Path("{bookmarkId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookmark(UUID experimentUuid, UUID bookmarkUuid) {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBookmark(UUID experimentUuid, Bookmark bookmark) {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }

    @PUT
    @Path("{bookmarkId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBookmark(UUID experimentUuid, UUID bookmarkUuid, Bookmark bookmark) {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }

    @DELETE
    @Path("{bookmarkId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBookmark(UUID experimentUuid, UUID bookmarkUuid) {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }
}
