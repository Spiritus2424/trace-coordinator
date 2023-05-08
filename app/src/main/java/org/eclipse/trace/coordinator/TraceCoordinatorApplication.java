package org.eclipse.trace.coordinator;

import org.glassfish.jersey.server.ResourceConfig;

import jakarta.ws.rs.ApplicationPath;

@ApplicationPath("tsp/api")
public class TraceCoordinatorApplication extends ResourceConfig {
	public TraceCoordinatorApplication() {
		packages("org.eclipse.trace.coordinator");
		register(AutoScanFeature.class);
	}

}
