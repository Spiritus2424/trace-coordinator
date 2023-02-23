package org.eclipse.trace.coordinator.configuration;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.trace.coordinator.distributedexperiment.properties.DistributedExperimentProperties;
import org.eclipse.trace.coordinator.traceserver.properties.TraceServerProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Configuration {

    @JsonProperty("trace-servers")
    private List<TraceServerProperties> traceServerProperties;

    @JsonProperty("experiments")
    private List<DistributedExperimentProperties> distributedExperimentProperties;

    public Configuration() {
        this.traceServerProperties = new ArrayList<>();
        this.distributedExperimentProperties = new ArrayList<>();
    }

    public List<TraceServerProperties> getTraceServerProperties() {
        return traceServerProperties;
    }

    public List<DistributedExperimentProperties> getDistributedExperimentProperties() {
        return distributedExperimentProperties;
    }

}
