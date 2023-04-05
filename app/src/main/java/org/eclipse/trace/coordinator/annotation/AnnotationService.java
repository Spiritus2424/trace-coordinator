package org.eclipse.trace.coordinator.annotation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

            for (int i = 0; i < requested_items.size(); i++) {
                requested_items.set(i, traceServer.decodeEntryId(requested_items.get(i)));
            }

            query.getParameters().put("requested_items", requested_items);
        }

        GenericResponse<AnnotationModel> genericResponse = traceServer.getTspClient().getAnnotationApi()
                .getAnnotations(experimentUuid, outputId, query)
                .getResponseModel();

        this.annotationAnalysis.computeAnnotationModel(traceServer,
                genericResponse.getModel().getAnnotations());
        return genericResponse;
    }
}
