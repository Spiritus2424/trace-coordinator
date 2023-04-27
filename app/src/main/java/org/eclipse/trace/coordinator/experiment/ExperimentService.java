package org.eclipse.trace.coordinator.experiment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.experiment.Experiment;
import org.eclipse.tsp.java.client.api.experiment.dto.CreateExperimentRequestDto;
import org.eclipse.tsp.java.client.api.experiment.dto.UpdateExperimentRequestDto;
import org.eclipse.tsp.java.client.shared.query.Body;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ExperimentService {

	public CompletableFuture<List<Experiment>> getExperiments(final TraceServer traceServer) {
		return traceServer.getTspClient().getExperimentApiAsync().getExperiments(Optional.empty())
				.thenApply(response -> response.getResponseModel());
	}

	public CompletableFuture<Experiment> getExperiment(final TraceServer traceServer, final UUID experimentUuid) {
		return traceServer.getTspClient().getExperimentApiAsync().getExperiment(experimentUuid)
				.thenApply(response -> response.getResponseModel());
	}

	public CompletableFuture<Experiment> createExperiment(final TraceServer traceServer,
			final Body<CreateExperimentRequestDto> body) {
		return traceServer.getTspClient().getExperimentApiAsync().createExperiment(body)
				.thenApply(response -> response.getResponseModel());
	}

	public CompletableFuture<Experiment> updateExperiment(final TraceServer traceServer, final UUID experimentUuid,
			final Body<UpdateExperimentRequestDto> body) {
		return traceServer.getTspClient().getExperimentApiAsync().updateExperiment(experimentUuid, body)
				.thenApply(response -> response.getResponseModel());
	}

	public CompletableFuture<Experiment> deleteExperiment(final TraceServer traceServer, final UUID experimentUuid) {
		return traceServer.getTspClient().getExperimentApiAsync().deleteExperiment(experimentUuid)
				.thenApply(response -> response.getResponseModel());
	}
}
