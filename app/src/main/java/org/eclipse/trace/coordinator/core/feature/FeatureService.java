package org.eclipse.trace.coordinator.core.feature;

import java.util.List;

import org.eclipse.trace.coordinator.core.traceserver.TraceServer;
import org.jvnet.hk2.annotations.Service;

@Service
public class FeatureService {

	public List<Object> getSupportedTraceTypes(final TraceServer traceServer) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	public List<Object> getSupportedOutputTypes(final TraceServer traceServer) {
		throw new UnsupportedOperationException("Not Implemented");
	}
}
