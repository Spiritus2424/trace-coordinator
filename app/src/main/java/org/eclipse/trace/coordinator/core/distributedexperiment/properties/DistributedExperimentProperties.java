package org.eclipse.trace.coordinator.core.distributedexperiment.properties;

import java.util.List;

import org.eclipse.trace.coordinator.core.distributedexperiment.DistributedExperiment;
import org.eclipse.trace.coordinator.shared.properties.Properties;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DistributedExperimentProperties implements Properties<DistributedExperiment> {

	private String name;

	@JsonProperty("traces-path")
	private List<String> tracesPath;

	public DistributedExperimentProperties() {
	}

	public DistributedExperimentProperties(String name, List<String> tracesPath) {
		this.name = name;
		this.tracesPath = tracesPath;
	}

	public List<String> getTracesPath() {
		return tracesPath;
	}

	public String getName() {
		return name;
	}

	@Override
	public DistributedExperiment toObject() {
		return new DistributedExperiment(this.getName(), this.getTracesPath());
	}

}
