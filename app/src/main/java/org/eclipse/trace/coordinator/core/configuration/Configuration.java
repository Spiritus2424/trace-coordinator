package org.eclipse.trace.coordinator.core.configuration;

import java.util.List;

import org.eclipse.trace.coordinator.core.experiment.properties.ExperimentProperties;
import org.eclipse.trace.coordinator.core.traceserver.properties.TraceServerProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Configuration {

	@JsonProperty("trace-servers")
	@NonNull
	private List<TraceServerProperties> traceServerProperties;

	@JsonProperty("experiments")
	private List<ExperimentProperties> experimentProperties;

}
