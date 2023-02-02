package org.eclipse.trace.coordinator.configuration;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.trace.coordinator.experiment.properties.ExperimentProperties;
import org.eclipse.trace.coordinator.traceserver.properties.TraceServerProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Configuration {

    @JsonProperty("trace-servers")
    private List<TraceServerProperties> traceServerProperties;

    @JsonProperty("experiments")
    private List<ExperimentProperties> experimentProperties;

    public Configuration() {
        this.traceServerProperties = new ArrayList<>();
        this.experimentProperties = new ArrayList<>();
    }

    public List<TraceServerProperties> getTraceServerProperties() {
        return traceServerProperties;
    }

    public List<ExperimentProperties> getExperimentProperties() {
        return experimentProperties;
    }

}
