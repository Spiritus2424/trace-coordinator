package org.eclipse.trace.coordinator.core.traceserver;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.trace.coordinator.core.configuration.ConfigurationProvider;
import org.eclipse.trace.coordinator.core.traceserver.properties.TraceServerProperties;
import org.jvnet.hk2.annotations.Service;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

@Service
public class TraceServerManager {

	@Inject
	private ConfigurationProvider configurationProvider;

	private List<TraceServer> traceServers;

	public TraceServerManager() {
		this.traceServers = new ArrayList<>();
	}

	@PostConstruct
	private void loadTraceServers() {
		for (TraceServerProperties traceServerProperties : this.configurationProvider.getConfiguration()
				.getTraceServerProperties()) {
			this.traceServers.add(traceServerProperties.toObject());
		}

		for (TraceServer traceServer : this.traceServers) {
			// Logger.getLogger(TraceServerManager.class.getName()).log(Level.INFO, "Trace
			// Server: {0}",
			// traceServer.getUri().toString());
		}

		if (traceServers.isEmpty()) {
			// Logger.getLogger(TraceServerManager.class.getName()).log(Level.INFO, "No
			// trace server found");
		}
	}

	public List<TraceServer> getTraceServers() {
		return traceServers;
	}
}
