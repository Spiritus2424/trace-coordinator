package org.eclipse.trace.coordinator.experiment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.trace.coordinator.trace.TraceService;
import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.trace.coordinator.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.models.experiment.Experiment;
import org.eclipse.tsp.java.client.models.query.Query;
import org.eclipse.tsp.java.client.models.trace.Trace;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotNull;
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

@Path("experiments")
@ApplicationScoped
public class ExperimentController {

    @Inject
    ExperimentService experimentService;

    // @Inject
    // DistributedExperimentManager distributedExperimentManager;

    @Inject
    TraceServerManager traceServerManager;

    @Inject
    TraceService traceService;

    @PostConstruct
    public void createExperiments() {
        // for (DistributedExperiment distributedExperiment :
        // distributedExperimentManager.getDistributedExperiments()
        // .values()) {
        // for (TraceServer traceServer : traceServerManager.getTraceServers()) {
        // Map<String, Object> parameters = new HashMap<>();
        // parameters.put("name", traceServer.getHost());
        // parameters.put("traces", distributedExperiment.getTracesUuid());
        // experimentService.createExperiment(traceServer, new Query(parameters));
        // }

        // }

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getExperiments() {
        List<Experiment> experiments = new ArrayList<>();

        for (TraceServer traceServer : this.traceServerManager.getTraceServers()) {
            experiments.addAll(this.experimentService.getExperiments(traceServer));
        }

        return Response.ok(experiments).build();
    }

    @GET
    @Path("{expUUID}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getExperiment(@NotNull @PathParam("expUUID") UUID experimentUuid) {
        Response response = null;

        for (TraceServer traceServer : traceServerManager.getTraceServers()) {
            Experiment experiment = experimentService.getExperiment(traceServer, experimentUuid.toString());
            if (experiment != null) {
                response = Response.ok(experiment).build();
                break;
            } else {
                response = Response.status(Status.NOT_FOUND).entity("No Such Trace").build();
            }
        }

        return response;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @SuppressWarnings("unchecked")
    public Response createExperiment(@NotNull Query query) {
        List<Experiment> experiments = new ArrayList<>();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", query.getParameters().get("name"));
        for (TraceServer traceServer : traceServerManager.getTraceServers()) {
            List<Trace> traces = traceService.getTraces(traceServer);
            List<String> traceServerTracesUuid = new ArrayList<>();
            for (Trace trace : traces) {
                for (String traceUuid : (ArrayList<String>) query.getParameters().get("traces")) {
                    if (trace.getUuid().equals(traceUuid)) {
                        traceServerTracesUuid.add(traceUuid);
                    }
                }
            }

            if (traceServerTracesUuid.size() > 0) {
                parameters.put("traces", traceServerTracesUuid);
                experiments.add(experimentService.createExperiment(traceServer, new Query(parameters)));
            }
        }

        return Response.ok(experiments).build();
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
        List<Experiment> experiments = new ArrayList<>();
        for (TraceServer traceServer : traceServerManager.getTraceServers()) {
            experiments.add(experimentService.deleteExperiment(traceServer, experimentUuid.toString()));
        }

        Response response = null;
        if (experiments.size() != 0) {
            response = Response.ok(experiments).build();
        } else {
            response = Response.status(Status.NOT_FOUND).entity("No Such Experiment").build();

        }

        return response;
    }
}
