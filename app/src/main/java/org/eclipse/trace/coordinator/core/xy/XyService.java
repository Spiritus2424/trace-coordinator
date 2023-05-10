package org.eclipse.trace.coordinator.core.xy;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.eclipse.trace.coordinator.core.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.xy.XyModel;
import org.eclipse.tsp.java.client.api.xy.dto.GetXyModelRequestDto;
import org.eclipse.tsp.java.client.api.xy.dto.GetXyTreeRequestDto;
import org.eclipse.tsp.java.client.core.tspclient.TspClientResponse;
import org.eclipse.tsp.java.client.shared.entry.Entry;
import org.eclipse.tsp.java.client.shared.entry.EntryModel;
import org.eclipse.tsp.java.client.shared.query.Body;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;
import org.jvnet.hk2.annotations.Service;

import jakarta.inject.Inject;

@Service
public class XyService {

	@Inject
	private XyAnalysis xyAnalysis;

	public CompletableFuture<GenericResponse<XyModel>> getXy(
			final TraceServer traceServer,
			final UUID experimentUuid,
			final String outputId,
			final Body<GetXyModelRequestDto> body) {

		final List<Integer> traceServerRequestedItems = List.copyOf(body.getParameters().getRequestedItems()).stream()
				.filter(traceServer::isValidEncodeEntryId)
				.map(traceServer::decodeEntryId)
				.collect(Collectors.toList());

		final Body<GetXyModelRequestDto> newBody = new Body<>(
				new GetXyModelRequestDto(
						body.getParameters().getRequestedTimerange(),
						traceServerRequestedItems));

		return (!traceServerRequestedItems.isEmpty())
				? traceServer.getTspClient().getXyApiAsync().getXy(experimentUuid, outputId, newBody)
						.thenApply(response -> {
							if (response.getResponseModel().getModel() != null) {
								this.xyAnalysis.computeXy(traceServer, response
										.getResponseModel().getModel().getSeries());
							}
							return response.getResponseModel();
						})
				: null;
	}

	public CompletableFuture<GenericResponse<EntryModel<Entry>>> getTree(
			final TraceServer traceServer,
			final UUID experimentUuid,
			final String outputId,
			final Body<GetXyTreeRequestDto> body) {
		return traceServer.getTspClient().getXyApiAsync().getXyTree(experimentUuid, outputId, body)
				.thenApply(response -> {
					if (response.getResponseModel().getModel() != null) {
						this.xyAnalysis.computeTree(traceServer,
								response.getResponseModel().getModel().getEntries());
					}
					return response.getResponseModel();
				});
	}

	public CompletableFuture<GenericResponse<Map<String, String>>> getTooltips(
			final TraceServer traceServer,
			final UUID experimentUuid,
			final String outputId,
			final int xValue,
			final Optional<Integer> yValue,
			final Optional<String> seriesId) {
		return traceServer.getTspClient().getXyApiAsync()
				.getXyTooltips(experimentUuid, outputId, xValue, yValue, seriesId)
				.thenApply(TspClientResponse::getResponseModel);
	}
}
