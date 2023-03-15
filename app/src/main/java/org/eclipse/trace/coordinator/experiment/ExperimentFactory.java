package org.eclipse.trace.coordinator.experiment;

import java.util.List;

import org.eclipse.tsp.java.client.api.experiment.Experiment;
import org.eclipse.tsp.java.client.shared.indexing.IndexingStatus;

public class ExperimentFactory {

    public static Experiment createExperiment(List<Experiment> experiments) {
        Experiment distributedExperiment = experiments.get(0);
        experiments.stream().skip(1).forEach((Experiment experiment) -> {
            distributedExperiment.setStart(distributedExperiment.getStart().min(experiment.getStart()));
            distributedExperiment.setEnd(distributedExperiment.getEnd().max(experiment.getEnd()));
            distributedExperiment.setNbEvents(distributedExperiment.getNbEvents() + experiment.getNbEvents());
            distributedExperiment.getTraces().addAll(experiment.getTraces());
            if (distributedExperiment.getIndexingStatus() != IndexingStatus.RUNNING
                    && experiment.getIndexingStatus() == IndexingStatus.RUNNING) {
                distributedExperiment.setIndexingStatus(IndexingStatus.RUNNING);
            }

        });

        return distributedExperiment;
    }
}
