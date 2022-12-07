package org.eclipse.trace.coordinator.trace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import org.eclipse.trace.coordinator.configuration.ConfigurationService;
import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.models.query.Query;
import org.eclipse.tsp.java.client.models.trace.Trace;
import org.eclipse.tsp.java.client.protocol.restclient.TspClientResponse;
import org.eclipse.tsp.java.client.protocol.tspclient.TspClient;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TraceService {

    @Inject
    ConfigurationService configurationService;

    @PostConstruct
    public void openTraces() {

        for (TraceServer traceServer : configurationService.getConfiguration().getTraceServers()) {
            for (String tracesPath : traceServer.getTracesPath()) {
                HashMap<String, Object> parameters = new HashMap<>();
                parameters.put("uri", tracesPath);
                parameters.put("name", traceServer.getUrlWithPort());
                openTrace(new Query(parameters));
            }

        }
    }

    public Trace getTrace(String traceUuid) {
        Trace trace = null;
        for (TraceServer traceServer : configurationService.getConfiguration().getTraceServers()) {
            TspClient tspClient = new TspClient(traceServer.getUrlWithPort());

            TspClientResponse<Trace> response = tspClient.getTrace(traceUuid);
            if (response.isOk() && response.getResponseModel() != null) {
                trace = response.getResponseModel();
                break;
            }

        }

        return trace;
    }

    public Trace[] getTraces() {
        ArrayList<Trace> traces = new ArrayList<>();
        for (TraceServer traceServer : configurationService.getConfiguration().getTraceServers()) {
            TspClient tspClient = new TspClient(traceServer.getUrlWithPort());

            TspClientResponse<Trace[]> response = tspClient.getTraces(Optional.empty());
            if (response.isOk() && response.getResponseModel() != null) {
                for (Trace trace : response.getResponseModel()) {
                    traces.add(trace);
                }
            }
        }

        return traces.toArray(new Trace[traces.size()]);
    }

    public Trace openTrace(Query query) {
        Trace trace = null;
        for (TraceServer traceServer : configurationService.getConfiguration().getTraceServers()) {
            TspClient tspClient = new TspClient(traceServer.getUrlWithPort());
            TspClientResponse<Trace> response = tspClient.openTrace(query);

            if (response.isOk() && response.getResponseModel() != null) {
                trace = response.getResponseModel();
                break;
            }
        }

        return trace;
    }

    public Trace deleteTrace(String traceUuid) {
        Trace trace = null;

        for (TraceServer traceServer : configurationService.getConfiguration().getTraceServers()) {
            TspClient tspClient = new TspClient(traceServer.getUrlWithPort());
            TspClientResponse<Trace> response = tspClient.deleteTrace(traceUuid, Optional.empty(), Optional.empty());

            if (response.isOk() && response.getResponseModel() != null) {
                trace = response.getResponseModel();
                break;
            }
        }

        return trace;
    }
}
