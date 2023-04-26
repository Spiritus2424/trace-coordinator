package org.eclipse.trace.coordinator.experiment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.experiment.Experiment;
import org.eclipse.tsp.java.client.api.experiment.dto.CreateExperimentRequestDto;
import org.eclipse.tsp.java.client.api.experiment.dto.UpdateExperimentRequestDto;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExperimentService {

    public CompletableFuture<List<Experiment>> getExperiments(TraceServer traceServer) {
        return traceServer.getTspClient().getExperimentApiAsync().getExperiments(Optional.empty())
                .thenApply(response -> {
                    return response.getResponseModel();
                });
    }

    public CompletableFuture<Experiment> getExperiment(TraceServer traceServer, UUID experimentUuid) {
        return traceServer.getTspClient().getExperimentApiAsync().getExperiment(experimentUuid).thenApply(response -> {
            Experiment experiment = null;
            if (response.isOk() && response.getResponseModel() != null) {
                experiment = response.getResponseModel();
            }
            return experiment;
        });
    }

    public CompletableFuture<Experiment> createExperiment(TraceServer traceServer, CreateExperimentRequestDto body) {
        return traceServer.getTspClient().getExperimentApiAsync().createExperiment(body).thenApply(response -> {
            Experiment experiment = null;
            if (response.isOk() && response.getResponseModel() != null) {
                experiment = response.getResponseModel();
            }
            return experiment;
        });
    }

    public CompletableFuture<Experiment> updateExperiment(TraceServer traceServer, UUID experimentUuid,
            UpdateExperimentRequestDto body) {
        return traceServer.getTspClient().getExperimentApiAsync().updateExperiment(experimentUuid, body)
                .thenApply(response -> {
                    Experiment experiment = null;
                    if (response.isOk() && response.getResponseModel() != null) {
                        experiment = response.getResponseModel();
                    }

                    return experiment;
                });
    }

    public CompletableFuture<Experiment> deleteExperiment(TraceServer traceServer, UUID experimentUuid) {
        return traceServer.getTspClient().getExperimentApiAsync().deleteExperiment(experimentUuid)
                .thenApply(response -> {
                    Experiment experiment = null;
                    if (response.isOk() && response.getResponseModel() != null) {
                        experiment = response.getResponseModel();
                    }

                    return experiment;
                });
    }
}
