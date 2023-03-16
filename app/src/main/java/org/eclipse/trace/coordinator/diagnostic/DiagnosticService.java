package org.eclipse.trace.coordinator.diagnostic;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.health.Health;
import org.eclipse.tsp.java.client.api.health.HealthStatus;
import org.eclipse.tsp.java.client.core.tspclient.TspClientResponse;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DiagnosticService {

    public Health getStatus(TraceServer traceServer) {
        TspClientResponse<Health> response = traceServer.getTspClient().getHealthApi().checkHealth();
        Health health = new Health(HealthStatus.UP);
        if (response.getResponseModel().getStatus() == HealthStatus.DOWN) {
            health.setStatus(HealthStatus.DOWN);
        }

        return health;
    }
}
