package org.eclipse.trace.coordinator.xy;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.xy.XyModel;
import org.eclipse.tsp.java.client.shared.entry.Entry;
import org.eclipse.tsp.java.client.shared.entry.EntryModel;
import org.eclipse.tsp.java.client.shared.query.Query;
import org.eclipse.tsp.java.client.shared.response.GenericResponse;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class XyService {

    @Inject
    private XyAnalysis xyAnalysis;

    public CompletableFuture<GenericResponse<XyModel>> getXy(TraceServer traceServer, UUID experimentUuid,
            String outputId, Query query) {
        if (query.getParameters().containsKey("requested_items")) {
            List<Integer> requested_items = (List<Integer>) query.getParameters().get("requested_items");

            for (int i = 0; i < requested_items.size(); i++) {
                requested_items.set(i, traceServer.decodeEntryId(requested_items.get(i)));
            }

            query.getParameters().put("requested_items", requested_items);
        }

        return traceServer.getTspClient().getXyApiAsync().getXy(experimentUuid, outputId, query).thenApply(response -> {
            this.xyAnalysis.computeXy(traceServer, response.getResponseModel().getModel().getSeries());
            return response.getResponseModel();
        });
    }

    public CompletableFuture<GenericResponse<EntryModel<Entry>>> getTree(TraceServer traceServer, UUID experimentUuid,
            String outputId,
            Query query) {
        return traceServer.getTspClient().getXyApiAsync().getXyTree(experimentUuid, outputId, query)
                .thenApply(response -> {
                    this.xyAnalysis.computeTree(traceServer, response.getResponseModel().getModel().getEntries());
                    return response.getResponseModel();
                });
    }

    public GenericResponse<Map<String, String>> getTooltip(TraceServer traceServer, UUID experimentUuid,
            String outputId, int xValue, Optional<Integer> yValue, Optional<String> seriesId) {
        return traceServer.getTspClient().getXyApi().getXyTooltip(experimentUuid, outputId, xValue, yValue, seriesId)
                .getResponseModel();
    }
}
