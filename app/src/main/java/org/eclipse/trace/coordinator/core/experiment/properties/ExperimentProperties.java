package org.eclipse.trace.coordinator.core.experiment.properties;

import java.util.List;

import org.eclipse.trace.coordinator.shared.properties.Properties;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class ExperimentProperties implements Properties<Void> {

	@NonNull
	private String name;

	@JsonProperty("traces-path")
	@NonNull
	private List<String> tracesPath;

	@Override
	public Void toObject() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'toObject'");
	}

}
