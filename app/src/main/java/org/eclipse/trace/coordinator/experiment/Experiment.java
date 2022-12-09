package org.eclipse.trace.coordinator.experiment;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Experiment {

    @JsonProperty("traces-uuid")
    private List<String> tracesUuid;

    public Experiment() {
    }

    public Experiment(List<String> tracesUuid) {
        this.tracesUuid = tracesUuid;
    }

    public List<String> getTracesUuid() {
        return tracesUuid;
    }
}
