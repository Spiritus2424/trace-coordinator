package org.eclipse.trace.coordinator.trace;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.trace.Trace;
import org.eclipse.tsp.java.client.core.tspclient.TspClientResponse;
import org.eclipse.tsp.java.client.shared.query.Query;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TraceService {

    public Trace getTrace(TraceServer traceServer, UUID traceUuid) {
        TspClientResponse<Trace> response = traceServer.getTspClient().getTraceApi().getTrace(traceUuid);
        Trace trace = null;
        if (response.isOk() && response.getResponseModel() != null) {
            trace = response.getResponseModel();
        }

        return trace;

    }

    public List<Trace> getTraces(TraceServer traceServer) {
        TspClientResponse<Trace[]> response = traceServer.getTspClient().getTraceApi().getTraces(Optional.empty());
        List<Trace> traces = new ArrayList<>();
        if (response.isOk() && response.getResponseModel() != null) {
            for (Trace trace : response.getResponseModel()) {
                traces.add(trace);
            }
        }

        return traces;
    }

    public List<Trace> openTrace(TraceServer traceServer, Query query) {
        TspClientResponse<Trace> response = traceServer.getTspClient().getTraceApi().openTrace(query);

        List<Trace> traces = new ArrayList<>();
        if (response.isOk() && response.getResponseModel() != null) {
            traces.add(response.getResponseModel());
        }

        return traces;
    }

    public Trace deleteTrace(TraceServer traceServer, UUID traceUuid) {
        TspClientResponse<Trace> response = traceServer.getTspClient().getTraceApi().deleteTrace(traceUuid,
                Optional.empty(),
                Optional.empty());

        Trace trace = null;
        if (response.isOk() && response.getResponseModel() != null) {
            trace = response.getResponseModel();
        }

        return trace;
    }
}
