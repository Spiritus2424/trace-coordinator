package org.eclipse.trace.coordinator.distributedexperiment;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DistributedExperiment {

    @JsonProperty("traces-uuid")
    private List<String> tracesUuid;

    public DistributedExperiment() {
    }

    public DistributedExperiment(List<String> tracesUuid) {
        this.tracesUuid = tracesUuid;
    }

    public List<String> getTracesUuid() {
        return tracesUuid;
    }
}
