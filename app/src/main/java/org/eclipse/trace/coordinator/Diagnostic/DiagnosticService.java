package org.eclipse.trace.coordinator.Diagnostic;

import org.eclipse.tsp.java.client.models.health.Health;
import org.eclipse.tsp.java.client.models.health.HealthStatus;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DiagnosticService {
    private Health status;

    DiagnosticService() {
        status = new Health(HealthStatus.UP);
    }

    public Health getStatus() {
        return status;
    }
}
