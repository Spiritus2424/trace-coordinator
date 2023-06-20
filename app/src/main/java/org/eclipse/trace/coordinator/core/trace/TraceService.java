package org.eclipse.trace.coordinator.core.trace;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.eclipse.trace.coordinator.core.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.trace.Trace;
import org.eclipse.tsp.java.client.api.trace.dto.OpenTraceRequestDto;
import org.eclipse.tsp.java.client.core.tspclient.TspClientResponse;
import org.eclipse.tsp.java.client.shared.query.Body;
import org.jvnet.hk2.annotations.Service;

@Service
public class TraceService {

	public CompletableFuture<Trace> getTrace(final TraceServer traceServer, final UUID traceUuid) {
		return traceServer.getTspClient().getTraceApiAsync().getTrace(traceUuid)
				.thenApply(response -> response.getResponseModel());
	}

	public CompletableFuture<List<Trace>> getTraces(final TraceServer traceServer) {
		return traceServer.getTspClient().getTraceApiAsync().getTraces(Optional.empty())
				.thenApply(response -> response.getResponseModel());
	}

	public CompletableFuture<List<Trace>> openTrace(final TraceServer traceServer,
			final Body<OpenTraceRequestDto> body) {
		return traceServer.getTspClient().getTraceApiAsync().openTrace(body).thenApply(response -> {
			List<Trace> traces = new ArrayList<>();
			if (response.isOk() && response.getResponseModel() != null) {
				traces.add(response.getResponseModel());
			}
			return traces;
		});
	}

	public CompletableFuture<List<Trace>> openTraces(final TraceServer traceServer,
			final Body<OpenTraceRequestDto> body) {
		return traceServer.getTspClient().getTraceApiAsync().openTraces(body)
				.thenApply(TspClientResponse::getResponseModel);
	}

	public CompletableFuture<Trace> deleteTrace(final TraceServer traceServer, final UUID traceUuid) {
		return traceServer.getTspClient().getTraceApiAsync().deleteTrace(traceUuid, Optional.empty(), Optional.empty())
				.thenApply(TspClientResponse::getResponseModel);
	}
}
