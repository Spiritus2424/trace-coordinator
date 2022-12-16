package org.eclipse.trace.coordinator.experiment;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.models.experiment.Experiment;
import org.eclipse.tsp.java.client.models.query.Query;
import org.eclipse.tsp.java.client.protocol.restclient.TspClientResponse;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExperimentService {

    public List<Experiment> getExperiments(TraceServer traceServer) {
        TspClientResponse<Experiment[]> response = traceServer.getTspClient().getExperiments(Optional.empty());

        List<Experiment> experiments = new ArrayList<>();
        if (response.isOk() && response.getResponseModel() != null) {
            for (Experiment experiment : response.getResponseModel()) {
                experiments.add(experiment);
            }
        }

        return experiments;
    }

    public Experiment getExperiment(TraceServer traceServer, String experimentUuid) {
        TspClientResponse<Experiment> response = traceServer.getTspClient().getExperiment(experimentUuid);

        Experiment experiment = null;
        if (response.isOk() && response.getResponseModel() != null) {
            experiment = response.getResponseModel();
        }

        return experiment;
    }

    public Experiment createExperiment(TraceServer traceServer, Query query) {
        TspClientResponse<Experiment> response = traceServer.getTspClient().createExperiment(query);

        Experiment experiment = null;
        if (response.isOk() && response.getResponseModel() != null) {
            experiment = response.getResponseModel();
        }

        return experiment;
    }

    public Experiment updateExperiment(TraceServer traceServer, String experimentUuid, Query query) {
        TspClientResponse<Experiment> response = traceServer.getTspClient().updateExperiment(experimentUuid, query);

        Experiment experiment = null;
        if (response.isOk() && response.getResponseModel() != null) {
            experiment = response.getResponseModel();
        }

        return experiment;

    }

    public Experiment deleteExperiment(TraceServer traceServer, String experimentUuid) {
        TspClientResponse<Experiment> response = traceServer.getTspClient().deleteExperiment(experimentUuid);

        Experiment experiment = null;
        if (response.isOk() && response.getResponseModel() != null) {
            experiment = response.getResponseModel();
        }

        return experiment;
    }
}
