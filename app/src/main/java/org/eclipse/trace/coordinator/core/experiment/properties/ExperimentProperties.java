package org.eclipse.trace.coordinator.core.experiment.properties;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.trace.coordinator.shared.properties.Properties;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ExperimentProperties implements Properties<Void> {

	@NonNull
	private String name;

	@JsonProperty("traces-path")
	@NonNull
	private List<String> tracesPath;

	public ExperimentProperties() {
		this.tracesPath = new ArrayList<>();
	}

	@Override
	public Void toObject() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'toObject'");
	}

}
