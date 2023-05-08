package org.eclipse.trace.coordinator.markerset;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.markerset.MarkerSet;
import org.eclipse.tsp.java.client.core.tspclient.TspClientResponse;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;
import org.jvnet.hk2.annotations.Service;

@Service
public class MarkerSetService {

	public CompletableFuture<GenericResponse<Set<MarkerSet>>> getMarkerSets(final TraceServer traceServer,
			final UUID experimentUuid) {
		return traceServer.getTspClient().getMarkerSetApiAsync().getMarkerSets(experimentUuid)
				.thenApply(TspClientResponse::getResponseModel);

	}
}
