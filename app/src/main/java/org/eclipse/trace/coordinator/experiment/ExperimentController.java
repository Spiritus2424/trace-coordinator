package org.eclipse.trace.coordinator.experiment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.trace.coordinator.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.models.experiment.Experiment;
import org.eclipse.tsp.java.client.models.query.Query;

import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@ApplicationPath("/experiments")
public class ExperimentController {

    @Inject
    ExperimentService experimentService;

    @Inject
    DistributedExperimentManager distributedExperimentManager;

    @Inject
    TraceServerManager traceServerManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getExperiments() {
        List<Experiment> experiments = new ArrayList<>();
        for (DistributedExperiment distributedExperiment : this.distributedExperimentManager.getDistributedExperiments()
                .values()) {
            experiments.add(distributedExperiment.toExperiment());
        }

        return Response.ok(experiments).build();
    }

    @GET
    @Path("{expUUID}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getExperiment(@NotNull @PathParam("expUUID") UUID experimentUuid) {
        Response response = null;

        if (this.distributedExperimentManager.hasDistributedExperiment(experimentUuid)) {
            response = Response
                    .ok(this.distributedExperimentManager.getDistributedExperiment(experimentUuid).toExperiment())
                    .build();
        } else {
            response = Response.status(Status.NOT_FOUND).entity("No Such Experiment").build();
        }

        return response;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createExperiment(@NotNull Query query) {
        Response response = null;

        DistributedExperiment distributedExperiment = null;
        if (query.getParameters().containsKey("name") && query.getParameters().containsKey("traces")) {
            distributedExperiment = new DistributedExperiment(query.getParameters().get("name").toString());

            for (TraceServer traceServer : this.traceServerManager.getTraceServers()) {
                query.getParameters().put("name", traceServer.getUrlWithPort().concat(distributedExperiment.getName()));
                Experiment experiment = this.experimentService.createExperiment(traceServer, query);
                if (experiment != null) {
                    distributedExperiment.addExperiment(experiment);
                }
            }
            this.distributedExperimentManager.addDistributedExperiment(distributedExperiment);
            response = Response.ok(distributedExperiment.toExperiment()).build();
        } else {
            response = Response.status(Status.BAD_REQUEST).entity("Missing name and traces body parameter").build();
        }

        return response;
    }

    @PUT
    @Path("{expUUID")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateExperiment(@NotNull @PathParam("expUUID") UUID experimentUuid,
            @QueryParam("name") String experimentName, @NotNull Query query) {
        return Response.status(Status.NOT_IMPLEMENTED).build();
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{expUUID}")
    public Response deleteExperiment(@NotNull @PathParam("expUUID") UUID experimentUuid) {
        Response response = null;

        if (this.distributedExperimentManager.hasDistributedExperiment(experimentUuid)) {
            DistributedExperiment distributedExperiment = this.distributedExperimentManager
                    .removeDistributedExperiment(experimentUuid);

            for (TraceServer traceServer : this.traceServerManager.getTraceServers()) {
                for (Experiment experiment : distributedExperiment.getExperiments()) {
                    this.experimentService.deleteExperiment(traceServer, experiment.getUuid());
                }
            }

        } else {
            response = Response.status(Status.NOT_FOUND).entity("No Such Experiment").build();
        }

        return response;
    }
}
