package org.eclipse.trace.coordinator;

import java.io.IOException;
import java.net.URI;

import org.eclipse.trace.coordinator.app.TraceCoordinatorResourceConfig;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

public class Main {

	public static void main(String[] args) throws IOException {
		// Set up the HTTP server
		String protocol = "http";
		String host = System.getenv().containsKey("HOST") ? System.getenv("HOST") : "0.0.0.0";
		String port = System.getenv().containsKey("PORT") ? System.getenv("PORT") : "8080";
		HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(
				URI.create(String.format("%s://%s:%s", protocol, host, port)),
				new TraceCoordinatorResourceConfig());

		httpServer.start();
	}
}
