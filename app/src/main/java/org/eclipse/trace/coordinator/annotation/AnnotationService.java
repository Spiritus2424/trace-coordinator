package org.eclipse.trace.coordinator.annotation;

import java.util.Optional;
import java.util.UUID;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.models.annotation.AnnotationCategoriesModel;
import org.eclipse.tsp.java.client.models.annotation.AnnotationModel;
import org.eclipse.tsp.java.client.models.query.Query;
import org.eclipse.tsp.java.client.models.response.GenericResponse;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AnnotationService {

    public GenericResponse<AnnotationCategoriesModel> getAnnotationCategories(TraceServer traceServer,
            UUID experimentUuid, String outputId,
            Optional<String> markerSetId) {
        return traceServer.getTspClient().getAnnotationsCategories(experimentUuid, outputId, markerSetId)
                .getResponseModel();
    }

    public GenericResponse<AnnotationModel> getAnnotationModel(TraceServer traceServer, UUID experimentUuid,
            String outputId, Query query) {
        return traceServer.getTspClient().getAnnotations(experimentUuid, outputId, query).getResponseModel();
    }
}
