package org.eclipse.trace.coordinator.core.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.tsp.java.client.core.action.ActionDescriptor;
import org.jvnet.hk2.annotations.Service;

import lombok.Getter;

@Service
public class ActionManager {

	@Getter
	private Map<String, List<ActionDescriptor>> actionApplied;

	public ActionManager() {
		this.actionApplied = new HashMap<>();
	}

}
