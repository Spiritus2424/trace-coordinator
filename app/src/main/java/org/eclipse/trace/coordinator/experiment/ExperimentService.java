package org.eclipse.trace.coordinator.experiment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.experiment.Experiment;
import org.eclipse.tsp.java.client.core.tspclient.TspClientResponse;
import org.eclipse.tsp.java.client.shared.query.Query;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExperimentService {

    public List<Experiment> getExperiments(TraceServer traceServer) {
        TspClientResponse<Experiment[]> response = traceServer.getTspClient().getExperimentApi()
                .getExperiments(Optional.empty());

        List<Experiment> experiments = new ArrayList<>();
        if (response.isOk() && response.getResponseModel() != null) {
            for (Experiment experiment : response.getResponseModel()) {
                experiments.add(experiment);
            }
        }

        return experiments;
    }

    public Experiment getExperiment(TraceServer traceServer, UUID experimentUuid) {
        TspClientResponse<Experiment> response = traceServer.getTspClient().getExperimentApi()
                .getExperiment(experimentUuid);

        Experiment experiment = null;
        if (response.isOk() && response.getResponseModel() != null) {
            experiment = response.getResponseModel();
        }

        return experiment;
    }

    public Experiment createExperiment(TraceServer traceServer, Query query) {
        TspClientResponse<Experiment> response = traceServer.getTspClient().getExperimentApi().createExperiment(query);

        Experiment experiment = null;
        if (response.isOk() && response.getResponseModel() != null) {
            experiment = response.getResponseModel();
        }

        return experiment;
    }

    public Experiment updateExperiment(TraceServer traceServer, UUID experimentUuid, Query query) {
        TspClientResponse<Experiment> response = traceServer.getTspClient().getExperimentApi()
                .updateExperiment(experimentUuid, query);

        Experiment experiment = null;
        if (response.isOk() && response.getResponseModel() != null) {
            experiment = response.getResponseModel();
        }

        return experiment;

    }

    public Experiment deleteExperiment(TraceServer traceServer, UUID experimentUuid) {
        TspClientResponse<Experiment> response = traceServer.getTspClient().getExperimentApi()
                .deleteExperiment(experimentUuid);

        Experiment experiment = null;
        if (response.isOk() && response.getResponseModel() != null) {
            experiment = response.getResponseModel();
        }

        return experiment;
    }
}
