package org.eclipse.trace.coordinator.experiment;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DistributedExperimentManager {

    private Map<UUID, DistributedExperiment> distributedExperiments;

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
