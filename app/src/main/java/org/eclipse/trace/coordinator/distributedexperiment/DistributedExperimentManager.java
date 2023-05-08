package org.eclipse.trace.coordinator.distributedexperiment;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.trace.coordinator.configuration.Configuration;
import org.eclipse.trace.coordinator.distributedexperiment.properties.DistributedExperimentProperties;
import org.jvnet.hk2.annotations.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import jakarta.annotation.PostConstruct;

@Service
public class DistributedExperimentManager {

	private Map<UUID, DistributedExperiment> distributedExperiments;

	@PostConstruct
	public void loadDistributedExperiment() {
		String fileName = System.getProperty("TRACE_COORDINATOR_FILE");
		File file = new File(fileName != null ? fileName : ".trace-coordinator.yml");
		ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

		try {
			List<DistributedExperimentProperties> listOfDistributedExperimentProperties = objectMapper
					.readValue(file, Configuration.class).getDistributedExperimentProperties();
			for (DistributedExperimentProperties experimentProperties : listOfDistributedExperimentProperties) {
				DistributedExperiment distributedExperiment = experimentProperties.toObject();
				this.distributedExperiments.put(UUID.fromString(distributedExperiment.getName()),
						distributedExperiment);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public DistributedExperimentManager() {
		this.distributedExperiments = new HashMap<>();
	}

	public Map<UUID, DistributedExperiment> getDistributedExperiments() {
		return distributedExperiments;
	}

	public boolean hasDistributedExperiment(UUID uuid) {
		return this.distributedExperiments.containsKey(uuid);
	}

	public DistributedExperiment getDistributedExperiment(UUID uuid) {
		return this.distributedExperiments.get(uuid);
	}

	public DistributedExperiment addDistributedExperiment(DistributedExperiment distributedExperiment) {
		return this.distributedExperiments.put(UUID.fromString(distributedExperiment.getName()), distributedExperiment);
	}

	public DistributedExperiment removeDistributedExperiment(UUID uuid) {
		return this.distributedExperiments.remove(uuid);
	}

}
