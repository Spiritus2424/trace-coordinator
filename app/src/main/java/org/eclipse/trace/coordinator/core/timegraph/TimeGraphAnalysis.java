package org.eclipse.trace.coordinator.core.timegraph;

import java.util.List;

import org.eclipse.trace.coordinator.core.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphArrow;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphEntry;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphRow;
import org.jvnet.hk2.annotations.Service;

@Service
public class TimeGraphAnalysis {

	public void computeArrows(final TraceServer traceServer, final List<TimeGraphArrow> arrows) {
		arrows.parallelStream().forEach(timeGraphArrow -> {
			timeGraphArrow.setSourceId(traceServer.encodeEntryId(timeGraphArrow.getSourceId()));
			timeGraphArrow.setTargetId(traceServer.encodeEntryId(timeGraphArrow.getTargetId()));
		});
	}

	public void computeStates(final TraceServer traceServer, final List<TimeGraphRow> rows) {
		rows.parallelStream().forEach(row -> {
			row.setEntryId(traceServer.encodeEntryId(row.getEntryId()));
		});
	}

	public void computeTrees(final TraceServer traceServer, final List<TimeGraphEntry> entries) {
		entries.parallelStream().forEach(entry -> {
			entry.setId(traceServer.encodeEntryId(entry.getId()));
			if (entry.getParentId() != -1) {
				entry.setParentId(traceServer.encodeEntryId(entry.getParentId()));
			}
		});
	}

}
