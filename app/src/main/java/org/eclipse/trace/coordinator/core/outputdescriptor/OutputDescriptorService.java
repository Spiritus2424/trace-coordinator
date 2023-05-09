package org.eclipse.trace.coordinator.core.outputdescriptor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.eclipse.trace.coordinator.core.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.outputdescriptor.OutputDescriptor;
import org.eclipse.tsp.java.client.core.tspclient.TspClientResponse;
import org.jvnet.hk2.annotations.Service;

@Service
public class OutputDescriptorService {

	public CompletableFuture<List<OutputDescriptor>> getOutputDescriptors(final TraceServer traceServer,
			final UUID experimentUuid) {
		return traceServer.getTspClient().getOutputDescriptorApiAsync()
				.experimentOutputs(experimentUuid, Optional.empty()).thenApply(TspClientResponse::getResponseModel);

	}

}
