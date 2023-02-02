package org.eclipse.trace.coordinator.traceserver.properties;

import java.util.List;

import org.eclipse.trace.coordinator.properties.Properties;
import org.eclipse.trace.coordinator.traceserver.TraceServer;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TraceServerProperties implements Properties<TraceServer> {
    private String host;
    private String port;

    @JsonProperty("traces-path")
    private List<String> tracesPath;

    public TraceServerProperties() {
    }

    public TraceServerProperties(String host, String port, List<String> tracesPath) {
        this.host = host;
        this.port = port;
        this.tracesPath = tracesPath;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
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
        return new TraceServer(this.host, this.port, this.tracesPath);
    }

}
