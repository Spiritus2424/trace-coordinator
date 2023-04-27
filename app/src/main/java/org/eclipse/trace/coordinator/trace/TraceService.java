package org.eclipse.trace.coordinator.trace;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.trace.Trace;
import org.eclipse.tsp.java.client.shared.query.Query;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TraceService {

    public CompletableFuture<Trace> getTrace(TraceServer traceServer, UUID traceUuid) {
        return traceServer.getTspClient().getTraceApiAsync().getTrace(traceUuid).thenApply(response -> {
            Trace trace = null;
            if (response.isOk() && response.getResponseModel() != null) {
                trace = response.getResponseModel();
            }

            return trace;
        });
    }

    public CompletableFuture<List<Trace>> getTraces(TraceServer traceServer) {
        return traceServer.getTspClient().getTraceApiAsync().getTraces(Optional.empty()).thenApply(response -> {
            List<Trace> traces = new ArrayList<>();
            if (response.isOk() && response.getResponseModel() != null) {
                for (Trace trace : response.getResponseModel()) {
                    traces.add(trace);
                }
            }
            return traces;
        });
    }

    public CompletableFuture<List<Trace>> openTrace(TraceServer traceServer, Query query) {
        return traceServer.getTspClient().getTraceApiAsync().openTrace(query).thenApply(response -> {
            List<Trace> traces = new ArrayList<>();
            if (response.isOk() && response.getResponseModel() != null) {
                traces.add(response.getResponseModel());
            }
            return traces;
        });
    }

    public CompletableFuture<Trace> deleteTrace(TraceServer traceServer, UUID traceUuid) {
        return traceServer.getTspClient().getTraceApiAsync().deleteTrace(traceUuid, Optional.empty(), Optional.empty())
                .thenApply(response -> {
                    Trace trace = null;
                    if (response.isOk() && response.getResponseModel() != null) {
                        trace = response.getResponseModel();
                    }
                    return trace;
                });
    }
}
