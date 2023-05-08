package org.eclipse.trace.coordinator.api.annotation;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.eclipse.trace.coordinator.core.annotation.AnnotationService;
import org.eclipse.trace.coordinator.core.traceserver.TraceServer;
import org.eclipse.trace.coordinator.core.traceserver.TraceServerManager;
import org.eclipse.tsp.java.client.api.annotation.AnnotationCategoriesModel;
import org.eclipse.tsp.java.client.api.annotation.AnnotationModel;
import org.eclipse.tsp.java.client.api.annotation.dto.GetAnnotationsRequestDto;
import org.eclipse.tsp.java.client.shared.query.Body;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;
import org.eclipse.tsp.java.client.shared.response.ResponseStatus;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("experiments/{expUUID}/outputs/{outputId}/annotations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AnnotationController {

	@Inject
	private TraceServerManager traceServerManager;

	@Inject
	private AnnotationService annotationService;

	@GET
	public Response getAnnotationCategories(
			@NotNull @PathParam("expUUID") final UUID experimentUuid,
			@NotNull @PathParam("outputId") final String outputId,
			@QueryParam("markerSetId") final String markerSetId) {

		final GenericResponse<AnnotationCategoriesModel> genericResponseMerged = this.traceServerManager
				.getTraceServers()
				.stream()
				.map((TraceServer traceServer) -> this.annotationService.getAnnotationCategories(traceServer,
						experimentUuid, outputId, markerSetId != null ? Optional.of(markerSetId) : Optional.empty()))
				.map(CompletableFuture::join)
				.reduce(null, (accumulator, genericResponse) -> {
					if (accumulator == null) {
						accumulator = genericResponse;
					} else {
						if (accumulator.getStatus() != ResponseStatus.RUNNING) {
							accumulator.setStatus(genericResponse.getStatus());
							accumulator.setMessage(genericResponse.getMessage());
						}
						if (genericResponse.getModel() != null) {
							accumulator.getModel().getAnnotationCategories()
									.addAll(genericResponse.getModel().getAnnotationCategories());
						}
					}

					return accumulator;
				});

		return Response.ok(genericResponseMerged).build();
	}

	@POST
	public Response getAnnotation(
			@NotNull @PathParam("expUUID") final UUID experimentUuid,
			@NotNull @PathParam("outputId") final String outputId,
			@NotNull @Valid final Body<GetAnnotationsRequestDto> body) {
		final GenericResponse<AnnotationModel> genericResponseMerged = this.traceServerManager
				.getTraceServers()
				.stream()
				.map(traceServer -> this.annotationService.getAnnotationModel(traceServer, experimentUuid, outputId,
						body))
				.map(CompletableFuture::join)
				.reduce(null, (accumulator, genericResponse) -> {
					if (accumulator == null) {
						accumulator = genericResponse;
					} else {
						if (accumulator.getStatus() != ResponseStatus.RUNNING) {
							accumulator.setStatus(genericResponse.getStatus());
							accumulator.setMessage(genericResponse.getMessage());
						}
						if (genericResponse.getModel() != null) {
							accumulator.getModel().getAnnotations().putAll(genericResponse.getModel().getAnnotations());
						}
					}

					return accumulator;
				});

		return Response.ok(genericResponseMerged).build();
	}
}
