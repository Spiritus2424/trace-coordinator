package org.eclipse.trace.coordinator.core.experiment;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.tsp.java.client.api.experiment.Experiment;
import org.eclipse.tsp.java.client.shared.indexing.IndexingStatus;
import org.eclipse.tsp.java.client.shared.tracecompass.TraceCompassLog;
import org.eclipse.tsp.java.client.shared.tracecompass.TraceCompassLogUtils.FlowScopeLog;
import org.eclipse.tsp.java.client.shared.tracecompass.TraceCompassLogUtils.FlowScopeLogBuilder;

public class ExperimentFactory {

	private static final @NonNull Logger logger = TraceCompassLog.getLogger(ExperimentFactory.class);

	private ExperimentFactory() {
	}

	public static Experiment createExperiment(List<Experiment> experiments) {
		try (FlowScopeLog flowScopeLog = new FlowScopeLogBuilder(logger, Level.FINE,
				"ExperimentFactory#createExperiment").build()) {
			return experiments.stream()
					.reduce(null, (distributedExperiment, experiment) -> {
						if (distributedExperiment == null) {
							distributedExperiment = experiment;
						} else {
							distributedExperiment
									.setStart(Math.min(distributedExperiment.getStart(), experiment.getStart()));
							distributedExperiment.setEnd(Math.max(distributedExperiment.getEnd(), experiment.getEnd()));
							distributedExperiment
									.setNbEvents(distributedExperiment.getNbEvents() + experiment.getNbEvents());
							distributedExperiment.getTraces().addAll(experiment.getTraces());
							if (distributedExperiment.getIndexingStatus() != IndexingStatus.RUNNING
									&& experiment.getIndexingStatus() == IndexingStatus.RUNNING) {
								distributedExperiment.setIndexingStatus(IndexingStatus.RUNNING);
							}
						}

						return distributedExperiment;
					});
		}
	}
}
