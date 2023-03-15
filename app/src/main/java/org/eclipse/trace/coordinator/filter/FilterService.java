package org.eclipse.trace.coordinator.filter;

import java.util.List;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.filter.Filter;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FilterService {

    public List<Filter> getFilters(TraceServer traceServer) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    public Filter getFilter(TraceServer traceServer, String filterId) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    public Filter createFilter(TraceServer traceServer, Filter filter) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    public Filter updateFilter(TraceServer traceServer, String filterId, Filter filter) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    public Filter deleteFilter(TraceServer traceServer, String filterId) {
        throw new UnsupportedOperationException("Not Implemented");
    }
}
