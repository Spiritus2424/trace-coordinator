package org.eclipse.trace.coordinator.symbol;

import org.eclipse.trace.coordinator.traceserver.TraceServer;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SymbolService {

    public void createSymbolProvider(final TraceServer traceServer, final String hostId, final String pid,
            final String url) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    public void getSymbolProvider(final TraceServer traceServer, final String hostId, final String pid) {
        throw new UnsupportedOperationException("Not Implemented");
    }
}
