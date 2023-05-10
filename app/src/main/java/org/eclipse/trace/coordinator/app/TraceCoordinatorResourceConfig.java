package org.eclipse.trace.coordinator.app;

import org.eclipse.trace.coordinator.app.cors.CorsFilter;
import org.eclipse.trace.coordinator.shared.utils.AutoScanFeature;
import org.glassfish.jersey.server.ResourceConfig;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.SeBootstrap.Configuration;

@ApplicationPath("tsp/api")
public class TraceCoordinatorResourceConfig extends ResourceConfig {
	public TraceCoordinatorResourceConfig() {
		packages("org.eclipse.trace.coordinator.api");
		register(AutoScanFeature.class);
		register(CorsFilter.class);
		property(Configuration.PORT, "8080");
	}

}
