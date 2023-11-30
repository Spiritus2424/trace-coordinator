package org.eclipse.trace.coordinator.core.timegraph;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.eclipse.trace.coordinator.core.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphArrow;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphEntry;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphModel;
import org.eclipse.tsp.java.client.api.timegraph.dto.GetTimeGraphArrowsRequestDto;
import org.eclipse.tsp.java.client.api.timegraph.dto.GetTimeGraphStatesRequestDto;
import org.eclipse.tsp.java.client.api.timegraph.dto.GetTimeGraphTooltipsRequestDto;
import org.eclipse.tsp.java.client.api.timegraph.dto.GetTimeGraphTreeRequestDto;
import org.eclipse.tsp.java.client.core.action.ActionDescriptor;
import org.eclipse.tsp.java.client.core.tspclient.TspClientResponse;
import org.eclipse.tsp.java.client.shared.entry.EntryModel;
import org.eclipse.tsp.java.client.shared.query.Body;
import org.eclipse.tsp.java.client.shared.query.Query;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;
import org.jvnet.hk2.annotations.Service;

import jakarta.inject.Inject;
import jakarta.ws.rs.ClientErrorException;

@Service
public class TimeGraphService {
	@Inject
	private TimeGraphAnalysis timeGraphAnalysis;

	public CompletableFuture<GenericResponse<List<TimeGraphArrow>>> getArrows(
			final TraceServer traceServer,
			final UUID experimentUuid,
			final String outputId,
			final Body<GetTimeGraphArrowsRequestDto> body) {
		return traceServer.getTspClient().getTimeGraphApiAsync()
				.getTimeGraphArrows(experimentUuid, outputId, body)
				.thenApply(response -> {
					if (response.getResponseModel() != null
							&& response.getResponseModel().getModel() != null) {
						this.timeGraphAnalysis.computeArrows(traceServer, response.getResponseModel().getModel());
					}
					return response.getResponseModel();
				});
	}

	public CompletableFuture<GenericResponse<TimeGraphModel>> getStates(
			final TraceServer traceServer,
			final UUID experimentUuid,
			final String outputId,
			final Body<GetTimeGraphStatesRequestDto> body) {

		Body<GetTimeGraphStatesRequestDto> newBody = body.getParameters().getRequestedItems() == null
				|| body.getParameters().getRequestedItems().isEmpty() ? body
						: new Body<>(new GetTimeGraphStatesRequestDto(
								body.getParameters().getRequestedTimerange(),
								body.getParameters().getRequestedItems().stream()
										.filter(traceServer::isValidEncodeEntryId)
										.map(traceServer::decodeEntryId)
										.collect(Collectors.toList())));

		return (body.getParameters().getRequestedItems() == null || body.getParameters().getRequestedItems().isEmpty()
				|| !newBody.getParameters().getRequestedItems().isEmpty())
						? traceServer.getTspClient().getTimeGraphApiAsync()
								.getTimeGraphStates(experimentUuid, outputId, newBody)
								.thenApply(response -> {
									if (response.getResponseModel() != null
											&& response.getResponseModel().getModel() != null) {
										this.timeGraphAnalysis.computeStates(traceServer,
												response.getResponseModel().getModel().getRows());
									}
									return response.getResponseModel();
								})
						: null;

	}

	public CompletableFuture<GenericResponse<Map<String, String>>> getTooltips(
			final TraceServer traceServer,
			final UUID experimentUuid,
			final String outputId,
			final Body<GetTimeGraphTooltipsRequestDto> body) {
		final Body<GetTimeGraphTooltipsRequestDto> newBody = new Body<>(
				new GetTimeGraphTooltipsRequestDto(
						body.getParameters().getRequestedElement(),
						body.getParameters().getRequestedTimes(),
						body.getParameters().getRequestedItems()
								.stream()
								.filter(traceServer::isValidEncodeEntryId)
								.map(traceServer::decodeEntryId)
								.collect(Collectors.toList())));

		return (!newBody.getParameters().getRequestedItems().isEmpty())
				? traceServer.getTspClient().getTimeGraphApiAsync()
						.getTimeGraphTooltips(experimentUuid, outputId, newBody)
						.thenApply(TspClientResponse::getResponseModel)
				: null;
	}

	public CompletableFuture<GenericResponse<EntryModel<TimeGraphEntry>>> getTree(
			final TraceServer traceServer,
			final UUID experimentUuid,
			final String outputId,
			final Body<GetTimeGraphTreeRequestDto> body) {
		return traceServer.getTspClient().getTimeGraphApiAsync().getTimeGraphTree(experimentUuid, outputId, body)
				.thenApply(response -> {
					if (response.getResponseModel() != null && response.getResponseModel().getModel() != null) {
						this.timeGraphAnalysis.computeTrees(traceServer,
								response.getResponseModel().getModel().getEntries());
					}
					return response.getResponseModel();
				});
	}

	public CompletableFuture<GenericResponse<List<ActionDescriptor>>> getActionTooltips(
			final TraceServer traceServer,
			final UUID experimentUuid,
			final String outputId,
			final Body<GetTimeGraphTooltipsRequestDto> body) {
		final Body<GetTimeGraphTooltipsRequestDto> newBody = new Body<>(
				new GetTimeGraphTooltipsRequestDto(
						body.getParameters().getRequestedElement(),
						body.getParameters().getRequestedTimes(),
						body.getParameters().getRequestedItems()
								.stream()
								.filter(traceServer::isValidEncodeEntryId)
								.map(traceServer::decodeEntryId)
								.collect(Collectors.toList())));

		return (!newBody.getParameters().getRequestedItems().isEmpty())
				? traceServer.getTspClient().getTimeGraphApiAsync()
						.getTimeGraphActionTooltips(experimentUuid, outputId, newBody)
						.thenApply(TspClientResponse::getResponseModel)
				: null;
	}

	public CompletableFuture<Void> applyActionTooltip(
			final TraceServer traceServer,
			final UUID experimentUuid,
			final String outputId,
			final String actionId,
			final Body<Map<String, Object>> body) {
		return traceServer.getTspClient().getTimeGraphApiAsync()
				.applyTimeGraphActionTooltip(experimentUuid, outputId, actionId, body)
				.thenApply(response -> {
					if (!response.isOk()) {
						throw new ClientErrorException(response.getStatusMessage(),
								response.getStatusCode().getStatusCode());
					}
					return response.getResponseModel();
				});
	}

	public GenericResponse<TimeGraphModel> getNavigations(
			final TraceServer traceServer,
			final UUID experimentUuid,
			final String outputId,
			final Query query) {
		throw new UnsupportedOperationException("Not Implemented");
	}
}
