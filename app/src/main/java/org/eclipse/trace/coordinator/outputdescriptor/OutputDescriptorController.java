package org.eclipse.trace.coordinator.outputdescriptor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.trace.coordinator.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.models.outputdescriptor.OutputDescriptor;

import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@ApplicationPath("/experiments/{expUUID}/outputs")
public class OutputDescriptorController {

    @Inject
    OutputDescriptorService outputDescriptorService;

    @Inject
    TraceServerManager traceServerManager;

    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getOutputDescriptors(@NotNull @PathParam("expUUID") UUID experimentUuid) {
        Set<OutputDescriptor> outputDescriptors = new HashSet<>();

        for (TraceServer traceServer : this.traceServerManager.getTraceServers()) {
            outputDescriptors
                    .addAll(outputDescriptorService.getOutputDescriptors(traceServer, experimentUuid.toString()));
        }

        return Response.ok(outputDescriptors.toArray()).build();
    }

    @GET
    @Path("{outputId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getOutputDescriptor(@NotNull @PathParam("expUUID") UUID experimentUuid,
            @NotNull @PathParam("outputId") UUID outputId) {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }
}
