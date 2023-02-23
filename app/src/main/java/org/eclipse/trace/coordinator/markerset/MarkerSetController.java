package org.eclipse.trace.coordinator.markerset;

import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * MarkerSetController
 */
@Path("experiments/{expUUID}/outputs/markerSets")
@ApplicationScoped
public class MarkerSetController {

    @Inject
    private MarkerSetService markerSetService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getMarkerSets(UUID experimentUuid) {

        return Response.ok().build();
    }

}