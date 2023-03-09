package org.eclipse.trace.coordinator.style;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.trace.coordinator.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.models.query.Query;
import org.eclipse.tsp.java.client.models.response.GenericResponse;
import org.eclipse.tsp.java.client.models.response.ResponseStatus;
import org.eclipse.tsp.java.client.models.style.OutputElementStyle;
import org.eclipse.tsp.java.client.models.style.OutputStyleModel;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * StyleController
 */
@Path("experiments/{expUUID}/outputs/{outputId}/style")
@ApplicationScoped
public class StyleController {

    @Inject
    StyleService styleService;

    @Inject
    TraceServerManager traceServerManager;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStyles(@PathParam("expUUID") UUID experimentUuid, @PathParam("outputId") String outputId,
            Query query) {
        Map<String, OutputElementStyle> styles = new HashMap<>();
        ResponseStatus responseStatus = ResponseStatus.COMPLETED;
        String statusMessage = null;
        for (TraceServer traceServer : traceServerManager.getTraceServers()) {
            GenericResponse<OutputStyleModel> genericResponse = this.styleService.getStyles(traceServer, experimentUuid,
                    outputId, query);

            if (responseStatus != ResponseStatus.RUNNING) {
                responseStatus = genericResponse.getStatus();
                statusMessage = genericResponse.getMessage();
            }

            if (genericResponse.getModel() != null) {
                styles.putAll(genericResponse.getModel().getStyles());
            }
        }
        return Response
                .ok(new GenericResponse<OutputStyleModel>(new OutputStyleModel(styles), responseStatus, statusMessage))
                .build();
    }
}