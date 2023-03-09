package org.eclipse.trace.coordinator.features;

import java.util.List;

import org.eclipse.trace.coordinator.traceserver.TraceServer;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FeatureService {

    public List<Object> getSupportedTraceTypes(TraceServer traceServer) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    public List<Object> getSupportedOutputTypes(TraceServer traceServer) {
        throw new UnsupportedOperationException("Not Implemented");
    }
}
