package org.eclipse.trace.coordinator.core.action;

public interface IAction<T> {

	public void compute(T data);
}
