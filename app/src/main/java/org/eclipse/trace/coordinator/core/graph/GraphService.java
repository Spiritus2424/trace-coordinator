package org.eclipse.trace.coordinator.core.graph;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.eclipse.trace.coordinator.core.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.graph.TcpEventKey;
import org.eclipse.tsp.java.client.api.graph.Vertex;
import org.eclipse.tsp.java.client.api.graph.Worker;
import org.eclipse.tsp.java.client.core.tspclient.TspClientResponse;
import org.eclipse.tsp.java.client.shared.query.Body;
import org.eclipse.tsp.java.client.shared.query.TimeRange;
import org.jvnet.hk2.annotations.Service;

@Service
public class GraphService {

	public CompletableFuture<Worker> getWorker(final TraceServer traceServer, final UUID experimentUuid,
			Integer workerId) {
		return traceServer.getTspClient().getGraphApiAsync().fetchWorker(experimentUuid, workerId)
				.thenApply(TspClientResponse::getResponseModel);
	}

	public CompletableFuture<List<Vertex>> getUnmatchedVertexes(final TraceServer traceServer,
			final UUID experimentUuid, Body<TimeRange> body) {
		return traceServer.getTspClient().getGraphApiAsync().fetchUnmatchedVertex(experimentUuid, body)
				.thenApply(TspClientResponse::getResponseModel);
	}

	public CompletableFuture<Map<Vertex, TcpEventKey>> getVertexIndexes(final TraceServer traceServer,
			final UUID experimentUuid) {
		return traceServer.getTspClient().getGraphApiAsync().fetchVertexIndexes(experimentUuid)
				.thenApply(TspClientResponse::getResponseModel);
	}
}
