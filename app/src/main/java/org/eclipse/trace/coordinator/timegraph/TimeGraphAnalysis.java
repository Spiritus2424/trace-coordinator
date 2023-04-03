package org.eclipse.trace.coordinator.timegraph;

import java.util.List;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphArrow;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphEntry;
import org.eclipse.tsp.java.client.api.timegraph.TimeGraphRow;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TimeGraphAnalysis {

    public void computeArrows(TraceServer traceServer, List<TimeGraphArrow> arrows) {
        for (TimeGraphArrow timeGraphArrow : arrows) {
            timeGraphArrow.setSourceId(traceServer.encodeEntryId(timeGraphArrow.getSourceId()));
            timeGraphArrow.setTargetId(traceServer.encodeEntryId(timeGraphArrow.getTargetId()));
        }
    }

    public void computeStates(TraceServer traceServer, List<TimeGraphRow> rows) {
        for (TimeGraphRow row : rows) {
            row.setEntryId(traceServer.encodeEntryId(row.getEntryId()));
        }
    }

    public void computeTrees(TraceServer traceServer, List<TimeGraphEntry> entries) {
        for (TimeGraphEntry entry : entries) {
            entry.setId(traceServer.encodeEntryId(entry.getId()));
            if (entry.getParentId() != -1) {
                entry.setParentId(traceServer.encodeEntryId(entry.getParentId()));
            }
        }
    }

}
