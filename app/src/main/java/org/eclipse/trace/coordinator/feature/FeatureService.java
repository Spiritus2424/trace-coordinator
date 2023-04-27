package org.eclipse.trace.coordinator.feature;

import java.util.List;

import org.eclipse.trace.coordinator.traceserver.TraceServer;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FeatureService {

    public List<Object> getSupportedTraceTypes(final TraceServer traceServer) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    public List<Object> getSupportedOutputTypes(final TraceServer traceServer) {
        throw new UnsupportedOperationException("Not Implemented");
    }
}
