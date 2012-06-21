package de.dzimmermann.rcp.basicplatform.util;

import de.dzimmermann.rcp.basicplatform.model.Task;

public class InternalPluginHandlerTrigger {

	private Task task;

	public InternalPluginHandlerTrigger(Task task) {
		this.task = task;
	}

	public Task getTask() {
		return task;
	}
}
