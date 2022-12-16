package org.eclipse.trace.coordinator.traceserver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.trace.coordinator.traceserver.properties.TraceServerProperties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TraceServerManager {

    private List<TraceServer> traceServers;

    public TraceServerManager() {
        this.traceServers = new ArrayList<>();
    }

    @PostConstruct
    @SuppressWarnings("unchecked")
    public void loadTraceServer() {
        String fileName = System.getProperty("TRACE_COORDINATOR_FILE");
        File file = new File(fileName != null ? fileName : ".trace-coordinator.yml");
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

        try {
            List<TraceServerProperties> listOfTraceServerProperties = (List<TraceServerProperties>) objectMapper
                    .readValue(file, Map.class)
                    .get("trace-servers");
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
