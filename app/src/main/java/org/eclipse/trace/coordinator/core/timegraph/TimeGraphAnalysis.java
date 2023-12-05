package org.eclipse.trace.coordinator.core.timegraph;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.trace.coordinator.core.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphArrow;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphEntry;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphRow;
import org.eclipse.tsp.java.client.shared.tracecompass.TraceCompassLog;
import org.eclipse.tsp.java.client.shared.tracecompass.TraceCompassLogUtils.FlowScopeLog;
import org.eclipse.tsp.java.client.shared.tracecompass.TraceCompassLogUtils.FlowScopeLogBuilder;
import org.jvnet.hk2.annotations.Service;

@Service
public class TimeGraphAnalysis {

	private final @NonNull Logger logger;

	public TimeGraphAnalysis() {
		this.logger = TraceCompassLog.getLogger(TimeGraphAnalysis.class);
	}

	public void computeArrows(final TraceServer traceServer, final List<TimeGraphArrow> arrows) {
		try (FlowScopeLog flowScopeLog = new FlowScopeLogBuilder(this.logger, Level.FINE,
				"TimeGraphAnalysis#computeArrows").build()) {
			arrows.parallelStream().forEach(timeGraphArrow -> {
				timeGraphArrow.setSourceId(traceServer.encodeEntryId(timeGraphArrow.getSourceId()));
				timeGraphArrow.setTargetId(traceServer.encodeEntryId(timeGraphArrow.getTargetId()));
			});
		}
	}

	public void computeStates(final TraceServer traceServer, final List<TimeGraphRow> rows) {
		try (FlowScopeLog flowScopeLog = new FlowScopeLogBuilder(this.logger, Level.FINE,
				"TimeGraphAnalysis#computeStates").build()) {
			rows.parallelStream().forEach(row -> {
				row.setEntryId(traceServer.encodeEntryId(row.getEntryId()));
			});
		}
	}

	public void computeTrees(final TraceServer traceServer, final List<TimeGraphEntry> entries) {
		try (FlowScopeLog flowScopeLog = new FlowScopeLogBuilder(this.logger, Level.FINE,
				"TimeGraphAnalysis#computeTrees").build()) {
			entries.parallelStream().forEach(entry -> {
				entry.setId(traceServer.encodeEntryId(entry.getId()));
				if (entry.getParentId() != -1) {
					entry.setParentId(traceServer.encodeEntryId(entry.getParentId()));
				}
			});
		}
	}

}
