package org.eclipse.trace.coordinator.core.timegraph;

import java.util.List;

import org.eclipse.trace.coordinator.core.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphArrow;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphEntry;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphRow;

public interface ITimeGraphAction {

	public void computeArrows(final TraceServer traceServer, final List<TimeGraphArrow> arrows);

	public void computeStates(final TraceServer traceServer, final List<TimeGraphRow> rows);

	public void computeTrees(final TraceServer traceServer, final List<TimeGraphEntry> entries);
}
