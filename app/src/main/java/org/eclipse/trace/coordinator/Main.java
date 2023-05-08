package org.eclipse.trace.coordinator;

import jakarta.ws.rs.SeBootstrap;
import jakarta.ws.rs.SeBootstrap.Configuration;
import jakarta.ws.rs.SeBootstrap.Instance;

public class Main {
	public static void main(String[] args) throws InterruptedException {

		Configuration config = SeBootstrap.Configuration.builder()
				.rootPath("tsp/api")
				.port(8080)
				.build();

		Instance instance = SeBootstrap.start(new TraceCoordinatorApplication(), config).toCompletableFuture().join();

		System.out.println(instance.configuration().baseUri().toString());
		Thread.currentThread().join();
	}
}
