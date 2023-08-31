package org.eclipse.trace.coordinator.core.configuration;

import java.io.File;
import java.io.IOException;

import org.jvnet.hk2.annotations.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

@Service
public class ConfigurationProvider {

	private Configuration configuration;

	public Configuration getConfiguration() {

		if (this.configuration == null) {
			this.configuration = this.loadingFromConfigurationFile();
		}

		return configuration;
	}

	private Configuration loadingFromConfigurationFile() {
		String fileName = System.getProperty("TRACE_COORDINATOR_FILE");
		File file = new File(fileName != null ? fileName : ".trace-coordinator.yml");
		ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
		Configuration configuration = null;
		try {
			configuration = objectMapper.readValue(file, Configuration.class);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return configuration;
	}
}
