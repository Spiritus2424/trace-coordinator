package org.eclipse.trace.coordinator.outputdescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.outputdescriptor.OutputDescriptor;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OutputDescriptorService {

    public CompletableFuture<List<OutputDescriptor>> getOutputDescriptors(TraceServer traceServer,
            UUID experimentUuid) {
        return traceServer.getTspClient().getOutputDescriptorApiAsync()
                .experimentOutputs(experimentUuid, Optional.empty()).thenApply(response -> {
                    List<OutputDescriptor> outputDescriptors = new ArrayList<>();
                    if (response.isOk() && response.getResponseModel() != null) {
                        for (OutputDescriptor outputDescriptor : response.getResponseModel()) {
                            outputDescriptors.add(outputDescriptor);
                        }
                    }
                    return outputDescriptors;
                });

    }

}
