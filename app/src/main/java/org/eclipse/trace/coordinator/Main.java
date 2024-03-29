package org.eclipse.trace.coordinator;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.trace.coordinator.app.TraceCoordinatorResourceConfig;

import jakarta.ws.rs.SeBootstrap;
import jakarta.ws.rs.SeBootstrap.Instance;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		TraceCoordinatorResourceConfig traceCoordinatorApplication = new TraceCoordinatorResourceConfig();
		Instance instance = SeBootstrap.start(traceCoordinatorApplication).toCompletableFuture().join();

		Logger.getLogger(Main.class.getName()).log(Level.INFO, "Listening on {0}",
				instance.configuration().baseUri().toString());

		Thread.currentThread().join();
	}
}
