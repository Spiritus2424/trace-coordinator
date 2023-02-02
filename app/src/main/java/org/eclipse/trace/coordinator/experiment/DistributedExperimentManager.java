package org.eclipse.trace.coordinator.experiment;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.trace.coordinator.configuration.Configuration;
import org.eclipse.trace.coordinator.experiment.properties.ExperimentProperties;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DistributedExperimentManager {

    private Map<UUID, DistributedExperiment> distributedExperiments;

    @PostConstruct
    public void loadDistributedExperiment() {
        String fileName = System.getProperty("TRACE_COORDINATOR_FILE");
        File file = new File(fileName != null ? fileName : ".trace-coordinator.yml");
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

        try {
            List<ExperimentProperties> listOfExperimentProperties = objectMapper
                    .readValue(file, Configuration.class).getExperimentProperties();
            for (ExperimentProperties experimentProperties : listOfExperimentProperties) {
                DistributedExperiment distributedExperiment = experimentProperties.toObject();
                this.distributedExperiments.put(distributedExperiment.getUuid(), distributedExperiment);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DistributedExperimentManager() {
        this.distributedExperiments = new HashMap<>();
    }

    public Map<UUID, DistributedExperiment> getDistributedExperiments() {
        return distributedExperiments;
    }

    public boolean hasDistributedExperiment(UUID uuid) {
        return this.distributedExperiments.containsKey(uuid);
    }

    public DistributedExperiment getDistributedExperiment(UUID uuid) {
        return this.distributedExperiments.get(uuid);
    }

    public DistributedExperiment addDistributedExperiment(DistributedExperiment distributedExperiment) {
        return this.distributedExperiments.put(distributedExperiment.getUuid(), distributedExperiment);
    }

    public DistributedExperiment removeDistributedExperiment(UUID uuid) {
        return this.distributedExperiments.remove(uuid);
    }

}
