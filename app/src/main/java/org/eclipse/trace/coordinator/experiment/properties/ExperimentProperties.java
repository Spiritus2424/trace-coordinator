package org.eclipse.trace.coordinator.experiment.properties;

import java.util.List;

import org.eclipse.trace.coordinator.experiment.DistributedExperiment;
import org.eclipse.trace.coordinator.properties.Properties;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExperimentProperties implements Properties<DistributedExperiment> {

    private String name;

    @JsonProperty("traces-uuid")
    private List<String> tracesUuid;

    public ExperimentProperties() {
    }

    public ExperimentProperties(String name, List<String> tracesUuid) {
        this.name = name;
        this.tracesUuid = tracesUuid;
    }

    public List<String> getTracesUuid() {
        return tracesUuid;
    }

    public String getName() {
        return name;
    }

    @Override
    public DistributedExperiment toObject() {
        return new DistributedExperiment(this.name, this.tracesUuid);
    }
}
