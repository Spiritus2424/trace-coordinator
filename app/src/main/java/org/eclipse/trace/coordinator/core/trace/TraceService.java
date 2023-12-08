package org.eclipse.trace.coordinator.core.trace;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.trace.coordinator.core.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.trace.Trace;
import org.eclipse.tsp.java.client.api.trace.dto.OpenTraceRequestDto;
import org.eclipse.tsp.java.client.core.tspclient.TspClientResponse;
import org.eclipse.tsp.java.client.shared.query.Body;
import org.eclipse.tsp.java.client.shared.tracecompass.TraceCompassLog;
import org.eclipse.tsp.java.client.shared.tracecompass.TraceCompassLogUtils.FlowScopeLog;
import org.eclipse.tsp.java.client.shared.tracecompass.TraceCompassLogUtils.FlowScopeLogBuilder;
import org.jvnet.hk2.annotations.Service;

@Service
public class TraceService {

	private final @NonNull Logger logger;

	public TraceService() {
		this.logger = TraceCompassLog.getLogger(TraceService.class);
	}

	public CompletableFuture<Trace> getTrace(final TraceServer traceServer, final UUID traceUuid) {
		try (FlowScopeLog flowScopeLog = new FlowScopeLogBuilder(this.logger, Level.FINE,
				"TraceService#getTrace").setCategory(traceServer.getHost()).build()) {
			return traceServer.getTspClient().getTraceApiAsync().getTrace(traceUuid)
					.thenApply(response -> response.getResponseModel());
		}
	}

	public CompletableFuture<List<Trace>> getTraces(final TraceServer traceServer) {
		try (FlowScopeLog flowScopeLog = new FlowScopeLogBuilder(this.logger, Level.FINE,
				"TraceService#getTraces").setCategory(traceServer.getHost()).build()) {
			return traceServer.getTspClient().getTraceApiAsync().getTraces(Optional.empty())
					.thenApply(response -> response.getResponseModel());
		}
	}

	public CompletableFuture<List<Trace>> openTrace(final TraceServer traceServer,
			final Body<OpenTraceRequestDto> body) {
		try (FlowScopeLog flowScopeLog = new FlowScopeLogBuilder(this.logger, Level.FINE,
				"TraceService#openTrace").setCategory(traceServer.getHost()).build()) {
			return traceServer.getTspClient().getTraceApiAsync().openTrace(body).thenApply(response -> {
				List<Trace> traces = new ArrayList<>();
				if (response.isOk() && response.getResponseModel() != null) {
					traces.add(response.getResponseModel());
				}
				return traces;
			});
		}
	}

	public CompletableFuture<List<Trace>> openTraces(final TraceServer traceServer,
			final Body<OpenTraceRequestDto> body) {
		try (FlowScopeLog flowScopeLog = new FlowScopeLogBuilder(this.logger, Level.FINE,
				"TraceService#openTraces").setCategory(traceServer.getHost()).build()) {
			return traceServer.getTspClient().getTraceApiAsync().openTraces(body)
					.thenApply(TspClientResponse::getResponseModel);
		}
	}

	public CompletableFuture<Trace> deleteTrace(final TraceServer traceServer, final UUID traceUuid) {
		return traceServer.getTspClient().getTraceApiAsync().deleteTrace(traceUuid, Optional.empty(), Optional.empty())
				.thenApply(TspClientResponse::getResponseModel);
	}
}
