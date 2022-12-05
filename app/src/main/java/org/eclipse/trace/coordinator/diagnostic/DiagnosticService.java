package org.eclipse.trace.coordinator.diagnostic;

import org.eclipse.trace.coordinator.configuration.ConfigurationService;
import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.models.health.Health;
import org.eclipse.tsp.java.client.models.health.HealthStatus;
import org.eclipse.tsp.java.client.protocol.restclient.TspClientResponse;
import org.eclipse.tsp.java.client.protocol.tspclient.TspClient;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DiagnosticService {
    private Health status;

    @Inject
    ConfigurationService configurationService;

    DiagnosticService() {
        status = new Health(HealthStatus.UP);
    }

    public Health getStatus() {
        for (TraceServer traceServer : configurationService.getConfiguration().getTraceServers()) {
            TspClient tspClient = new TspClient(traceServer.getUrlWithPort());
            TspClientResponse<Health> response = tspClient.checkHealth();

            if (response.getResponseModel().getStatus() == HealthStatus.DOWN) {
                status.setStatus(HealthStatus.DOWN);
                break;
            }
        }

        return status;
    }
}
