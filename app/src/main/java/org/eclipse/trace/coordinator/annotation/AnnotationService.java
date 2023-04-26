package org.eclipse.trace.coordinator.annotation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.annotation.AnnotationCategoriesModel;
import org.eclipse.tsp.java.client.api.annotation.AnnotationModel;
import org.eclipse.tsp.java.client.api.annotation.dto.GetAnnotationsRequestDto;
import org.eclipse.tsp.java.client.shared.query.Body;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AnnotationService {

	@Inject
	private AnnotationAnalysis annotationAnalysis;

	public CompletableFuture<GenericResponse<AnnotationModel>> getAnnotationModel(
			final TraceServer traceServer,
			final UUID experimentUuid,
			final String outputId,
			final Body<GetAnnotationsRequestDto> body) {
		final Body<GetAnnotationsRequestDto> newBody = new Body<GetAnnotationsRequestDto>(
				new GetAnnotationsRequestDto(body.getParameters().getRequestedTimerange(), null,
						body.getParameters().getRequestedMarkerSet(),
						body.getParameters().getRequestedMarkerCategories()));
		if (body.getParameters().getRequestedItems() != null) {
			newBody.getParameters().setRequestedItems(List.copyOf(body.getParameters().getRequestedItems()).stream()
					.filter((Integer encodeEntryId) -> traceServer.isValidEncodeEntryId(encodeEntryId))
					.map((Integer encodeEntryId) -> {
						return traceServer.decodeEntryId(encodeEntryId);
					})
					.collect(Collectors.toList()));
		}

		return traceServer.getTspClient()
				.getAnnotationApiAsync()
				.getAnnotations(experimentUuid, outputId, newBody).thenApply((response) -> {
					this.annotationAnalysis.computeAnnotationModel(traceServer,
							response.getResponseModel().getModel().getAnnotations());
					return response.getResponseModel();
				});
	}

	public CompletableFuture<GenericResponse<AnnotationCategoriesModel>> getAnnotationCategories(
			final TraceServer traceServer,
			final UUID experimentUuid, String outputId,
			final Optional<String> markerSetId) {

		return traceServer.getTspClient().getAnnotationApiAsync()
				.getAnnotationsCategories(experimentUuid, outputId, markerSetId)
				.thenApply(response -> response.getResponseModel());
	}
}
