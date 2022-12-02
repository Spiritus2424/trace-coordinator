package org.eclipse.trace.coordinator.configuration;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ConfigurationService {
    private Configuration configuration;

    @PostConstruct
    public void loadConfig() {
        String fileName = System.getProperty("TRACE_COORDINATOR_FILE");
        File file = new File(fileName != null ? fileName : ".trace-coordinator.yml");
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try {
            configuration = mapper.readValue(file, Configuration.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
