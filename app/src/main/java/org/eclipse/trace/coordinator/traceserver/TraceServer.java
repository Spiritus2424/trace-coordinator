package org.eclipse.trace.coordinator.traceserver;

import java.util.List;

import org.eclipse.tsp.java.client.protocol.tspclient.TspClient;

public class TraceServer {
    private String url;
    private String port;
    private List<String> tracesPath;
    private TspClient tspClient;

    public TraceServer(String url, String port, List<String> tracesPath) {
        this.url = url;
        this.port = port;
        this.tracesPath = tracesPath;
        this.tspClient = new TspClient(getUrlWithPort());
    }

    public String getUrlWithPort() {
        return String.format("%s:%s", this.url, this.port);
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPort() {
        return this.port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public List<String> getTracesPath() {
        return this.tracesPath;
    }

    public TspClient getTspClient() {
        return tspClient;
    }

}
