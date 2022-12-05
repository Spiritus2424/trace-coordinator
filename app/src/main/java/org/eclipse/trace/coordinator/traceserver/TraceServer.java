package org.eclipse.trace.coordinator.traceserver;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TraceServer {
    private String url;
    private String port;

    @JsonProperty("traces-path")
    private List<String> tracesPath;

    public TraceServer() {
    }

    public TraceServer(String url, String port, List<String> tracesPath) {
        this.url = url;
        this.port = port;
        this.tracesPath = tracesPath;

    }

    public String getUrlWithPort() {
        return String.format("%s:%s", this.url, this.port);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public List<String> getTracesPath() {
        return tracesPath;
    }

}
