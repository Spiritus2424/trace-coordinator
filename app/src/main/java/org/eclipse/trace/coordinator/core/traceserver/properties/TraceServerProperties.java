package org.eclipse.trace.coordinator.core.traceserver.properties;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.trace.coordinator.core.traceserver.TraceServer;
import org.eclipse.trace.coordinator.shared.properties.Properties;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TraceServerProperties implements Properties<TraceServer> {
	private String host;
	private String port;

	@JsonProperty("traces-path")
	private List<String> tracesPath;

	public TraceServerProperties() {
		this.tracesPath = new ArrayList<>();
	}

	public TraceServerProperties(String host, String port, List<String> tracesPath) {
		this.host = host;
		this.port = port;
		this.tracesPath = (tracesPath == null) ? new ArrayList<>() : tracesPath;

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
		return new TraceServer(this.host, this.port, new ArrayList<>());
	}

}
