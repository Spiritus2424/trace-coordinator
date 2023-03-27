package org.eclipse.trace.coordinator.datatree;

import java.util.UUID;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.shared.query.Query;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class DataTreeService {
    public Response getTree(TraceServer traceServer, UUID experimentUuid, String outputId, Query query) {
        throw new UnsupportedOperationException("Not Implemented");
    }
}
