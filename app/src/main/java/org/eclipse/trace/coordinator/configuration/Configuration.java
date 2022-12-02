package org.eclipse.trace.coordinator.configuration;

import java.util.List;

import org.eclipse.trace.coordinator.distributedexperiment.DistributedExperiment;
import org.eclipse.trace.coordinator.traceserver.TraceServer;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Configuration {

    @JsonProperty("trace-servers")
    private List<TraceServer> traceServers;

    @JsonProperty("experiments")
    private List<DistributedExperiment> distributedExperiments;

    public Configuration() {
    }

    public List<TraceServer> getTraceServers() {
        return traceServers;
    }

    public void setTraceServers(List<TraceServer> traceServers) {
        this.traceServers = traceServers;
    }

    public List<DistributedExperiment> getDistributedExperiments() {
        return distributedExperiments;
    }

    public void setDistributedExperiments(List<DistributedExperiment> distributedExperiments) {
        this.distributedExperiments = distributedExperiments;
    }
}
