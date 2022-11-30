package org.eclipse.trace.coordinator.Diagnostic;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DiagnosticService {
    private String status;

    DiagnosticService() {
        status = "UP";
    }

    public String getStatus() {
        return status;
    }
}
