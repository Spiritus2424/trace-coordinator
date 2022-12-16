package org.eclipse.trace.coordinator.traceserver.properties;

import java.util.List;

import org.eclipse.trace.coordinator.properties.Properties;
import org.eclipse.trace.coordinator.traceserver.TraceServer;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TraceServerProperties implements Properties<TraceServer> {
    private String url;
    private String port;

    @JsonProperty("traces-path")
    private List<String> tracesPath;

    public TraceServerProperties() {
    }

    public TraceServerProperties(String url, String port, List<String> tracesPath) {
        this.url = url;
        this.port = port;
        this.tracesPath = tracesPath;
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

    public TraceServer toObject() {
        return new TraceServer(this.url, this.port, this.tracesPath);
    }

}
