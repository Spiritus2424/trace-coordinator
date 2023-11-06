package org.eclipse.trace.coordinator.core.graph;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.eclipse.trace.coordinator.core.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.graph.CreateCriticalPathDto;
import org.eclipse.tsp.java.client.api.graph.Direction;
import org.eclipse.tsp.java.client.api.graph.GraphDto;
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
			final UUID experimentUuid, Body<TimeRange> body, Optional<Direction> optionalDirection) {
		return traceServer.getTspClient().getGraphApiAsync()
				.fetchUnmatchedVertex(experimentUuid, body, optionalDirection)
				.thenApply(TspClientResponse::getResponseModel);
	}

	public CompletableFuture<Map<Vertex, TcpEventKey>> getVertexIndexes(final TraceServer traceServer,
			final UUID experimentUuid, Optional<Direction> optionalDirection) {
		return traceServer.getTspClient().getGraphApiAsync().fetchVertexIndexes(experimentUuid, optionalDirection)
				.thenApply(TspClientResponse::getResponseModel);
	}

	public CompletableFuture<GraphDto> createCriticalPath(final TraceServer traceServer, final UUID experiementUuid,
			Body<CreateCriticalPathDto> body) {
		return traceServer.getTspClient().getGraphApiAsync().createCriticalPath(experiementUuid,
				body)
				.thenApply(TspClientResponse::getResponseModel);
	}

}