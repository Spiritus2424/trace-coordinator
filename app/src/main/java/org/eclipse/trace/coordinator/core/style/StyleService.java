package org.eclipse.trace.coordinator.core.style;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.eclipse.trace.coordinator.core.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.style.OutputStyleModel;
import org.eclipse.tsp.java.client.core.tspclient.TspClientResponse;
import org.eclipse.tsp.java.client.shared.query.Query;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;
import org.jvnet.hk2.annotations.Service;

@Service
public class StyleService {

	public CompletableFuture<GenericResponse<OutputStyleModel>> getStyles(
			final TraceServer traceServer,
			final UUID experimentUuid,
			final String outputId,
			final Query query) {
		return traceServer.getTspClient().getStyleApiAsync().getStyles(experimentUuid, outputId, query)
				.thenApply(TspClientResponse::getResponseModel);
	}
}
