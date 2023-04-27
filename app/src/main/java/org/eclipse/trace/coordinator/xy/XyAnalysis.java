package org.eclipse.trace.coordinator.xy;

import java.util.List;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.xy.XySerie;
import org.eclipse.tsp.java.client.shared.entry.Entry;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class XyAnalysis {

	public void computeXy(TraceServer traceServer, List<XySerie> xySeries) {
		for (XySerie xySerie : xySeries) {
			xySerie.setSerieId(traceServer.encodeEntryId(xySerie.getSerieId()));
		}
	}

	public void computeTree(TraceServer traceServer, List<Entry> entries) {
		for (Entry entry : entries) {
			entry.setId(traceServer.encodeEntryId(entry.getId()));
			if (entry.getParentId() != -1) {
				entry.setParentId(traceServer.encodeEntryId(entry.getParentId()));
			}
		}
	}
}
