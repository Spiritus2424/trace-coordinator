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

    public CompletableFuture<GenericResponse<AnnotationModel>> getAnnotationModel(TraceServer traceServer,
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
                .getAnnotations(experimentUuid, outputId, query).thenApply((response) -> {
                    this.annotationAnalysis.computeAnnotationModel(traceServer,
                            response.getResponseModel().getModel().getAnnotations());
                    return response.getResponseModel();
                });
    }

    public CompletableFuture<GenericResponse<AnnotationCategoriesModel>> getAnnotationCategories(
            TraceServer traceServer,
            UUID experimentUuid, String outputId,
            Optional<String> markerSetId) {

        return traceServer.getTspClient().getAnnotationApiAsync()
                .getAnnotationsCategories(experimentUuid, outputId, markerSetId)
                .thenApply(response -> response.getResponseModel());
    }
}
