package org.eclipse.trace.coordinator.annotation;

import java.util.Optional;
import java.util.UUID;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.annotation.AnnotationCategoriesModel;
import org.eclipse.tsp.java.client.api.annotation.AnnotationModel;
import org.eclipse.tsp.java.client.shared.query.Query;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AnnotationService {

    public GenericResponse<AnnotationCategoriesModel> getAnnotationCategories(TraceServer traceServer,
            UUID experimentUuid, String outputId,
            Optional<String> markerSetId) {
        return traceServer.getTspClient().getAnnotationApi()
                .getAnnotationsCategories(experimentUuid, outputId, markerSetId)
                .getResponseModel();
    }

    public GenericResponse<AnnotationModel> getAnnotationModel(TraceServer traceServer, UUID experimentUuid,
            String outputId, Query query) {
        return traceServer.getTspClient().getAnnotationApi().getAnnotations(experimentUuid, outputId, query)
                .getResponseModel();
    }
}
