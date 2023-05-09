package org.eclipse.trace.coordinator.core.diagnostic;

import java.util.concurrent.CompletableFuture;

import org.eclipse.trace.coordinator.core.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.health.Health;
import org.eclipse.tsp.java.client.core.tspclient.TspClientResponse;
import org.jvnet.hk2.annotations.Service;

@Service
public class DiagnosticService {

	public CompletableFuture<Health> getStatus(final TraceServer traceServer) {
		return traceServer.getTspClient().getHealthApiAsync().checkHealth()
				.thenApply(TspClientResponse::getResponseModel);
	}
}
