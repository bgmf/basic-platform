package de.dzimmermann.rcp.basicplatform.model;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class TaskEditorInput implements IEditorInput {

	protected Task task;

	public TaskEditorInput() {
	}

	public TaskEditorInput(Task task) {
		this.task = task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	@Override
	public boolean exists() {
		return task != null ? true : false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return task.getImage() != null ? ImageDescriptor.createFromImage(task
				.getImage()) : null;
	}

	@Override
	public String getName() {
		return task != null ? task.getName() : "";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {

		if (task != null && task.getDescription() != null)
			return task.getDescription();
		else
			return "no tooltip available...";
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {

		if (Task.class.isAssignableFrom(adapter) && task != null) {
			return task;
		}

		return null;
	}
}
