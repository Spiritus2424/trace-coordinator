package org.eclipse.trace.coordinator.distributedexperiment;

import java.util.List;

public class DistributedExperiment {
    private String name;
    private List<String> tracesPath;

    public DistributedExperiment(String name, List<String> tracesPath) {
        this.name = name;
        this.tracesPath = tracesPath;
    }

    public String getName() {
        return name;
    }

    public List<String> getTracesPath() {
        return tracesPath;
    }
}
