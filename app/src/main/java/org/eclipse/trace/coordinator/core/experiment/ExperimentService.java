package org.eclipse.trace.coordinator.core.experiment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.trace.coordinator.core.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.experiment.Experiment;
import org.eclipse.tsp.java.client.api.experiment.dto.CreateExperimentRequestDto;
import org.eclipse.tsp.java.client.api.experiment.dto.UpdateExperimentRequestDto;
import org.eclipse.tsp.java.client.core.tspclient.TspClientResponse;
import org.eclipse.tsp.java.client.shared.query.Body;
import org.eclipse.tsp.java.client.shared.tracecompass.TraceCompassLog;
import org.eclipse.tsp.java.client.shared.tracecompass.TraceCompassLogUtils.FlowScopeLog;
import org.eclipse.tsp.java.client.shared.tracecompass.TraceCompassLogUtils.FlowScopeLogBuilder;
import org.jvnet.hk2.annotations.Service;

@Service
public class ExperimentService {

	private final @NonNull Logger logger;

	public ExperimentService() {
		this.logger = TraceCompassLog.getLogger(ExperimentService.class);
	}

	public CompletableFuture<List<Experiment>> getExperiments(final TraceServer traceServer) {
		try (FlowScopeLog flowScopeLog = new FlowScopeLogBuilder(this.logger, Level.FINE,
				"ExperimentService#getExperiments").setCategory(traceServer.getHost()).build()) {
			return traceServer.getTspClient().getExperimentApiAsync().getExperiments(Optional.empty())
					.thenApply(TspClientResponse::getResponseModel);
		}
	}

	public CompletableFuture<Experiment> getExperiment(final TraceServer traceServer, final UUID experimentUuid) {
		try (FlowScopeLog flowScopeLog = new FlowScopeLogBuilder(this.logger, Level.FINE,
				"ExperimentService#getExperiment").setCategory(traceServer.getHost()).build()) {
			return traceServer.getTspClient().getExperimentApiAsync().getExperiment(experimentUuid)
					.thenApply(TspClientResponse::getResponseModel);
		}

	}

	public CompletableFuture<Experiment> createExperiment(final TraceServer traceServer,
			final Body<CreateExperimentRequestDto> body) {
		try (FlowScopeLog flowScopeLog = new FlowScopeLogBuilder(this.logger, Level.FINE,
				"ExperimentService#createExperiment").setCategory(traceServer.getHost()).build()) {
			return traceServer.getTspClient().getExperimentApiAsync().createExperiment(body)
					.thenApply(TspClientResponse::getResponseModel);
		}
	}

	public CompletableFuture<Experiment> updateExperiment(final TraceServer traceServer, final UUID experimentUuid,
			final Body<UpdateExperimentRequestDto> body) {
		return traceServer.getTspClient().getExperimentApiAsync().updateExperiment(experimentUuid, body)
				.thenApply(TspClientResponse::getResponseModel);
	}

	public CompletableFuture<Experiment> deleteExperiment(final TraceServer traceServer, final UUID experimentUuid) {
		return traceServer.getTspClient().getExperimentApiAsync().deleteExperiment(experimentUuid)
				.thenApply(TspClientResponse::getResponseModel);
	}
}
