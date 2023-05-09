package org.eclipse.trace.coordinator.core.traceserver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.trace.coordinator.core.configuration.Configuration;
import org.eclipse.trace.coordinator.core.traceserver.properties.TraceServerProperties;
import org.jvnet.hk2.annotations.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import jakarta.annotation.PostConstruct;

@Service
public class TraceServerManager {

	private List<TraceServer> traceServers;

	public TraceServerManager() {
		this.traceServers = new ArrayList<>();
	}

	@PostConstruct
	public void loadTraceServers() {
		String fileName = System.getProperty("TRACE_COORDINATOR_FILE");
		File file = new File(fileName != null ? fileName : ".trace-coordinator.yml");
		ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

		try {
			List<TraceServerProperties> listOfTraceServerProperties = objectMapper
					.readValue(file, Configuration.class).getTraceServerProperties();
			for (TraceServerProperties traceServerProperties : listOfTraceServerProperties) {
				this.traceServers.add(traceServerProperties.toObject());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<TraceServer> getTraceServers() {
		return traceServers;
	}
}
