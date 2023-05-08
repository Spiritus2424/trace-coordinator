package org.eclipse.trace.coordinator.annotation;

import java.util.List;
import java.util.Map;

import org.eclipse.trace.coordinator.traceserver.TraceServer;
import org.eclipse.tsp.java.client.api.annotation.Annotation;
import org.jvnet.hk2.annotations.Service;

@Service
public class AnnotationAnalysis {

	public void computeAnnotationModel(final TraceServer traceServer,
			final Map<String, List<Annotation>> annotationModel) {
		for (List<Annotation> annotations : annotationModel.values()) {
			for (Annotation annotation : annotations) {
				annotation.setEntryId(traceServer.encodeEntryId(annotation.getEntryId()));
			}
		}
	}
}
