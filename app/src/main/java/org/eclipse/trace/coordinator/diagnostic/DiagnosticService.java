package org.eclipse.trace.coordinator.diagnostic;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.models.health.Health;
import org.eclipse.tsp.java.client.models.health.HealthStatus;
import org.eclipse.tsp.java.client.protocol.restclient.TspClientResponse;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DiagnosticService {

    public Health getStatus(TraceServer traceServer) {
        TspClientResponse<Health> response = traceServer.getTspClient().checkHealth();
        Health health = new Health(HealthStatus.UP);
        if (response.getResponseModel().getStatus() == HealthStatus.DOWN) {
            health.setStatus(HealthStatus.DOWN);
        }

        return health;
    }
}
