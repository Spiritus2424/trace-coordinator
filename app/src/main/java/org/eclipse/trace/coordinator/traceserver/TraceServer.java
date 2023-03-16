package org.eclipse.trace.coordinator.traceserver;

import java.net.URI;
import java.util.List;

import org.eclipse.tsp.java.client.core.tspclient.TspClient;

public class TraceServer {
    private URI uri;
    private String port;
    private List<String> tracesPath;
    private TspClient tspClient;

    public TraceServer(String host, String port, List<String> tracesPath) {
        this.uri = URI.create(String.format("%s:%s", host, port));
        this.port = port;
        this.tracesPath = tracesPath;

        this.tspClient = new TspClient(this.uri.toString());

    }

    public URI getUri() {
        return this.uri;
    }

    public String getHost() {
        return this.uri.getHost();
    }

    public String getPort() {
        return this.port;
    }

    public List<String> getTracesPath() {
        return this.tracesPath;
    }

    public TspClient getTspClient() {
        return tspClient;
    }

}
