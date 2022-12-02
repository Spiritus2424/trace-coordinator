package org.eclipse.trace.coordinator.configuration;

import jakarta.inject.Inject;
import jakarta.ws.rs.ApplicationPath;

@ApplicationPath("")
public class ConfigurationController {

    @Inject
    ConfigurationService configurationService;

}
