package org.eclipse.trace.coordinator.Diagnostic;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("")
@ApplicationScoped
public class DiagnosticController {

    @Inject
    DiagnosticService diagnosticService;

    @GET
    @Path("health")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getHealthStatus() {

        return Response.ok(diagnosticService.getStatus()).build();
    }
}
