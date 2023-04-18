package org.eclipse.trace.coordinator.annotation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.annotation.AnnotationCategoriesModel;
import org.eclipse.tsp.java.client.api.annotation.AnnotationModel;
import org.eclipse.tsp.java.client.shared.query.Query;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AnnotationService {

    @Inject
    private AnnotationAnalysis annotationAnalysis;

    public GenericResponse<AnnotationCategoriesModel> getAnnotationCategories(TraceServer traceServer,
            UUID experimentUuid, String outputId,
            Optional<String> markerSetId) {

        return traceServer.getTspClient().getAnnotationApi()
                .getAnnotationsCategories(experimentUuid, outputId, markerSetId)
                .getResponseModel();
    }

    public GenericResponse<AnnotationModel> getAnnotationModel(TraceServer traceServer, UUID experimentUuid,
            String outputId, Query query) {

        if (query.getParameters().containsKey("requested_items")) {
            List<Integer> requested_items = (List<Integer>) query.getParameters().get("requested_items");

            requested_items.parallelStream().map((Integer encodeEntryId) -> {
                return traceServer.decodeEntryId(encodeEntryId);
            });

            query.getParameters().put("requested_items", requested_items);
        }

        GenericResponse<AnnotationModel> genericResponse = traceServer.getTspClient().getAnnotationApi()
                .getAnnotations(experimentUuid, outputId, query)
                .getResponseModel();

        this.annotationAnalysis.computeAnnotationModel(traceServer,
                genericResponse.getModel().getAnnotations());
        return genericResponse;
    }

    public CompletableFuture<GenericResponse<AnnotationModel>> getAnnotationModelAsync(TraceServer traceServer,
            UUID experimentUuid, String outputId, Query query) {

        if (query.getParameters().containsKey("requested_items")) {
            List<Integer> requested_items = (List<Integer>) query.getParameters().get("requested_items");

            requested_items.parallelStream().map((Integer encodeEntryId) -> {
                return traceServer.decodeEntryId(encodeEntryId);
            });

            query.getParameters().put("requested_items", requested_items);
        }

        return traceServer.getTspClient()
                .getAnnotationApiAsync()
                .getAnnotations(experimentUuid, outputId, query).thenApply((result) -> {
                    this.annotationAnalysis.computeAnnotationModel(traceServer,
                            result.getResponseModel().getModel().getAnnotations());
                    return result.getResponseModel();
                });
    }

    public CompletableFuture<GenericResponse<AnnotationCategoriesModel>> getAnnotationCategoriesAsync(
            TraceServer traceServer,
            UUID experimentUuid, String outputId,
            Optional<String> markerSetId) {

        return traceServer.getTspClient().getAnnotationApiAsync()
                .getAnnotationsCategories(experimentUuid, outputId, markerSetId)
                .thenApply(result -> result.getResponseModel());
    }
}
