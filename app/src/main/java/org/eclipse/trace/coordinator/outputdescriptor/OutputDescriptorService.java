package org.eclipse.trace.coordinator.outputdescriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.models.outputdescriptor.OutputDescriptor;
import org.eclipse.tsp.java.client.protocol.restclient.TspClientResponse;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OutputDescriptorService {

    public List<OutputDescriptor> getOutputDescriptors(TraceServer traceServer, String experimentUuid) {
        TspClientResponse<OutputDescriptor[]> response = traceServer.getTspClient().experimentOutputs(experimentUuid,
                Optional.empty());

        List<OutputDescriptor> outputDescriptors = new ArrayList<>();
        if (response.isOk() && response.getResponseModel() != null) {
            for (OutputDescriptor outputDescriptor : response.getResponseModel()) {
                outputDescriptors.add(outputDescriptor);
            }
        }

        return outputDescriptors;
    }

}
