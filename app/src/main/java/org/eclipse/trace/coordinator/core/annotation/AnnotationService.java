package org.eclipse.trace.coordinator.core.annotation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.eclipse.trace.coordinator.core.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.annotation.AnnotationCategoriesModel;
import org.eclipse.tsp.java.client.api.annotation.AnnotationModel;
import org.eclipse.tsp.java.client.api.annotation.dto.GetAnnotationsRequestDto;
import org.eclipse.tsp.java.client.core.tspclient.TspClientResponse;
import org.eclipse.tsp.java.client.shared.query.Body;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;
import org.jvnet.hk2.annotations.Service;

import jakarta.inject.Inject;

@Service
public class AnnotationService {

	@Inject
	private AnnotationAnalysis annotationAnalysis;

	public final CompletableFuture<GenericResponse<AnnotationModel>> getAnnotationModel(
			final TraceServer traceServer,
			final UUID experimentUuid,
			final String outputId,
			final Body<GetAnnotationsRequestDto> body) {
		final Body<GetAnnotationsRequestDto> newBody = new Body<>(
				new GetAnnotationsRequestDto(body.getParameters().getRequestedTimerange(), null,
						body.getParameters().getRequestedMarkerSet(),
						body.getParameters().getRequestedMarkerCategories()));
		if (body.getParameters().getRequestedItems() != null) {
			newBody.getParameters().setRequestedItems(List.copyOf(body.getParameters().getRequestedItems()).stream()
					.filter((Long items) -> traceServer.isValidEncodeEntryId(items))
					.map(traceServer::decodeEntryId)
					.collect(Collectors.toList()));
		}

		return traceServer.getTspClient()
				.getAnnotationApiAsync()
				.getAnnotations(experimentUuid, outputId, newBody).thenApply(response -> {
					this.annotationAnalysis.computeAnnotationModel(traceServer,
							response.getResponseModel().getModel().getAnnotations());
					return response.getResponseModel();
				});
	}

	public final CompletableFuture<GenericResponse<AnnotationCategoriesModel>> getAnnotationCategories(
			final TraceServer traceServer,
			final UUID experimentUuid, final String outputId,
			final Optional<String> markerSetId) {

		return traceServer.getTspClient().getAnnotationApiAsync()
				.getAnnotationsCategories(experimentUuid, outputId, markerSetId)
				.thenApply(TspClientResponse::getResponseModel);
	}
}
