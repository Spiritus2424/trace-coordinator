package org.eclipse.trace.coordinator.filter;

import java.util.List;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.filter.Filter;
import org.jvnet.hk2.annotations.Service;

@Service
public class FilterService {

	public List<Filter> getFilters(final TraceServer traceServer) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	public Filter getFilter(final TraceServer traceServer, final String filterId) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	public Filter createFilter(final TraceServer traceServer, final Filter filter) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	public Filter updateFilter(final TraceServer traceServer, final String filterId, final Filter filter) {
		throw new UnsupportedOperationException("Not Implemented");
	}

	public Filter deleteFilter(final TraceServer traceServer, final String filterId) {
		throw new UnsupportedOperationException("Not Implemented");
	}
}
