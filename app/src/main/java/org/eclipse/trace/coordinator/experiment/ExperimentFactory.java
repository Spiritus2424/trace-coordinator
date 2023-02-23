package org.eclipse.trace.coordinator.experiment;

import java.util.List;

import org.eclipse.tsp.java.client.models.experiment.Experiment;
import org.eclipse.tsp.java.client.models.indexing.IndexingStatus;

public class ExperimentFactory {

    public static Experiment createExperiment(List<Experiment> experiments) {
        Experiment distributedExperiment = experiments.get(0);
        experiments.stream().skip(1).forEach((Experiment experiment) -> {
            if (experiment.getIndexingStatus() == IndexingStatus.COMPLETED) {
                distributedExperiment.setStart(distributedExperiment.getEnd().min(experiment.getEnd()));
                distributedExperiment.setEnd(distributedExperiment.getEnd().max(experiment.getEnd()));
                distributedExperiment.setNbEvents(distributedExperiment.getNbEvents() + experiment.getNbEvents());
                distributedExperiment.getTraces().addAll(experiment.getTraces());
            }
        });
        return distributedExperiment;
    }
}
