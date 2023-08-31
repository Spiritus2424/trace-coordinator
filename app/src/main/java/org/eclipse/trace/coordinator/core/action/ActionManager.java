package org.eclipse.trace.coordinator.core.action;

import java.util.UUID;

import org.jvnet.hk2.annotations.Service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import lombok.Getter;

@Service
public class ActionManager {

	@Getter
	private Multimap<UUID, IAction<? extends Object>> actionApplied;

	public ActionManager() {
		this.actionApplied = ArrayListMultimap.create();
	}

}
