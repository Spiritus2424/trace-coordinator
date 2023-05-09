package org.eclipse.trace.coordinator.core.symbol;

import org.eclipse.trace.coordinator.core.traceserver.TraceServer;
import org.jvnet.hk2.annotations.Service;

@Service
public class SymbolService {

	public void createSymbolProvider(final TraceServer traceServer, final String hostId, final String pid,
			final String url) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	public void getSymbolProvider(final TraceServer traceServer, final String hostId, final String pid) {
		throw new UnsupportedOperationException("Not Implemented");
	}
}
