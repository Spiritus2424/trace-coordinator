package org.eclipse.trace.coordinator.experiment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.experiment.Experiment;
import org.eclipse.tsp.java.client.shared.query.Query;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExperimentService {

    public CompletableFuture<List<Experiment>> getExperiments(TraceServer traceServer) {
        return traceServer.getTspClient().getExperimentApiAsync().getExperiments(Optional.empty())
                .thenApply(response -> {
                    List<Experiment> experiments = new ArrayList<>();
                    if (response.isOk() && response.getResponseModel() != null) {
                        for (Experiment experiment : response.getResponseModel()) {
                            experiments.add(experiment);
                        }
                    }
                    return experiments;
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

    public CompletableFuture<Experiment> createExperiment(TraceServer traceServer, Query query) {
        return traceServer.getTspClient().getExperimentApiAsync().createExperiment(query).thenApply(response -> {
            Experiment experiment = null;
            if (response.isOk() && response.getResponseModel() != null) {
                experiment = response.getResponseModel();
            }
            return experiment;
        });
    }

    public CompletableFuture<Experiment> updateExperiment(TraceServer traceServer, UUID experimentUuid, Query query) {
        return traceServer.getTspClient().getExperimentApiAsync().updateExperiment(experimentUuid, query)
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
