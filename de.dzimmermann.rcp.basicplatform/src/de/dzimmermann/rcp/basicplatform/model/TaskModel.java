package de.dzimmermann.rcp.basicplatform.model;

import org.eclipse.core.runtime.PlatformObject;

public class TaskModel extends PlatformObject {

	// this root element will not be displayed in the used
	// TaskTreeContentProvider & Label Provider
	protected Task pluginRoot;

	public TaskModel() {
		pluginRoot = null;
	}

	public TaskModel(Task pluginRoot) {
		this.pluginRoot = pluginRoot;
	}

	public Task getPluginRoot() {

		if (pluginRoot == null)
			return null;

		return pluginRoot;
	}

	public void setPluginRoot(Task pluginRoot) {
		this.pluginRoot = pluginRoot;
	}

	public boolean switchTask(String taskNameToSwitch, Task root,
			boolean switchTo) {

		if (root.getName().equals(taskNameToSwitch)) {

			root.setEnabled(switchTo);
			return true;

		} else {
			if (root.getChildren().size() > 0) {

				for (Task child : root.getChildren()) {

					if (switchTask(taskNameToSwitch, child, switchTo))
						break;
				}
			} else
				return false;
		}

		return false;
	}

	public Task getTaskByName(String taskName, Task root) {

		if (root == null)
			return null;

		if (root.getName().equals(taskName))
			return root;
		else {
			if (root.getChildren().size() > 0) {
				for (Task child : root.getChildren()) {

					Task t = getTaskByName(taskName, child);

					if (t != null && t.getName().equals(taskName))
						return t;
				}
			}
		}

		return null;
	}
}
