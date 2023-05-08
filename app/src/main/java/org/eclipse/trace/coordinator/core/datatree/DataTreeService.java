package org.eclipse.trace.coordinator.core.datatree;

import java.util.UUID;

import org.eclipse.trace.coordinator.core.traceserver.TraceServer;
import org.eclipse.tsp.java.client.shared.query.Query;
import org.jvnet.hk2.annotations.Service;

import jakarta.ws.rs.core.Response;

@Service
public class DataTreeService {
	public Response getTree(final TraceServer traceServer, final UUID experimentUuid, final String outputId,
			final Query query) {
		throw new UnsupportedOperationException("Not Implemented");
	}
}
