package org.eclipse.trace.coordinator.trace;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.models.query.Query;
import org.eclipse.tsp.java.client.models.trace.Trace;
import org.eclipse.tsp.java.client.protocol.restclient.TspClientResponse;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TraceService {

    public Trace getTrace(TraceServer traceServer, String traceUuid) {
        TspClientResponse<Trace> response = traceServer.getTspClient().getTrace(traceUuid);
        Trace trace = null;
        if (response.isOk() && response.getResponseModel() != null) {
            trace = response.getResponseModel();
        }

        return trace;

    }

    public List<Trace> getTraces(TraceServer traceServer) {
        TspClientResponse<Trace[]> response = traceServer.getTspClient().getTraces(Optional.empty());
        List<Trace> traces = new ArrayList<>();
        if (response.isOk() && response.getResponseModel() != null) {
            for (Trace trace : response.getResponseModel()) {
                traces.add(trace);
            }
        }

        return traces;
    }

    public Trace openTrace(TraceServer traceServer, Query query) {
        TspClientResponse<Trace> response = traceServer.getTspClient().openTrace(query);

        Trace trace = null;
        if (response.isOk() && response.getResponseModel() != null) {
            trace = response.getResponseModel();
        }

        return trace;
    }

    public Trace deleteTrace(TraceServer traceServer, String traceUuid) {
        TspClientResponse<Trace> response = traceServer.getTspClient().deleteTrace(traceUuid, Optional.empty(),
                Optional.empty());

        Trace trace = null;
        if (response.isOk() && response.getResponseModel() != null) {
            trace = response.getResponseModel();
        }

        return trace;
    }
}
