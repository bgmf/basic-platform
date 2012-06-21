package de.dzimmermann.rcp.basicplatform.ui.util;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.dzimmermann.rcp.basicplatform.model.Task;
import de.dzimmermann.rcp.basicplatform.model.TaskGroupRestriction;
import de.dzimmermann.rcp.basicplatform.model.TaskModel;
import de.dzimmermann.rcp.basicplatform.services.IAvailablePluginService;
import de.dzimmermann.rcp.basicplatform.services.IGroupService;
import de.dzimmermann.rcp.basicplatform.util.SSDToolUtils;

public class TaskTreeContentProvider implements ITreeContentProvider {

	private boolean isDeveloper = false;
	private List<String> groups = null;

	public TaskTreeContentProvider() {
		try {
			IAvailablePluginService service = SSDToolUtils
					.getAvailablePluginService();
			if (service != null)
				isDeveloper = service.isDeveloper();
		} catch (Exception e) {
			Logger.logError(e);
		}
		try {
			IGroupService service = SSDToolUtils.getGroupService();
			if (service != null)
				groups = service.getAssignedGroups();
		} catch (Exception e) {
			Logger.logError(e);
		}
	}

	@Override
	public Object[] getChildren(Object parentElement) {

		List<Task> parentChildren = ((Task) parentElement).getChildren();
		List<Task> newTasks = new LinkedList<Task>();
		if (parentChildren != null)
			for (int i = 0; i < parentChildren.size(); i++) {
				Task t = parentChildren.get(i);
				boolean block = false;
				if (t.getGroupRestrictions() != null
						&& !t.getGroupRestrictions().isEmpty()
						&& groups != null) {
					for (TaskGroupRestriction tgr : t.getGroupRestrictions()) {
						block = tgr.isInverse() ? groups
								.contains(tgr.getName()) : !groups.contains(tgr
								.getName());
						if (block)
							break;
					}
				}
				if (t.isEnabled())
					t.setEnabled(!block || isDeveloper);
				if (t.isEnabled())
					newTasks.add(t);
			}

		return newTasks.toArray(new Task[0]);
	}

	@Override
	public Object getParent(Object element) {
		return ((Task) element).getParent();
	}

	@Override
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof Task)
			return getChildren(inputElement);
		if (inputElement instanceof TaskModel)
			return getChildren(((TaskModel) inputElement).getPluginRoot());
		return null;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
}
