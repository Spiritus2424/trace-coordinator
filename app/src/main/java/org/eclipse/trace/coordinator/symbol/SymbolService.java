package org.eclipse.trace.coordinator.symbol;

import org.eclipse.trace.coordinator.traceserver.TraceServer;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SymbolService {

    public void createSymbolProvider(TraceServer traceServer, String hostId, String pid, String url) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    public void getSymbolProvider(TraceServer traceServer, String hostId, String pid) {
        throw new UnsupportedOperationException("Not Implemented");
    }
}
