package de.dzimmermann.rcp.basicplatform.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.Image;

public class Task extends PlatformObject {

	/**
	 * The Id of the plugin, the task is belonging to.<br>
	 * Used to identify the calling plugin and to trigger the execution there
	 * (dialogs, ...). The fallback might be to still test all plugins, until
	 * the context is matching.
	 */
	private String taskPluginID;

	/**
	 * The id of this task.<br>
	 * <br>
	 * <b>This attribute is not required, but needed if you want to specify a
	 * {@link #parentTaskID}.</b>
	 */
	private String id;
	/**
	 * If used, this indicates a parent task under which this task (and all it's
	 * children!) will be located.<br>
	 * This is only applicable for a plugins root task, for all of it's
	 * children, this attribute will be ignored.<br>
	 * <br>
	 * <b>The specified ID does not indicate a tasks parent in the plugin
	 * tree.</b><br>
	 * For example if you enter such an ID to a task that is not the plugins
	 * root task, the specified ID might point somewhere else but it's real
	 * parent. But nevertheless, it will in any case be ignored.
	 */
	private String parentTaskID;

	/**
	 * can either be the id of the editor (as used within the application
	 * environment) or the implementing class name for a {@link TitleAreaDialog}
	 * or a {@link Wizard}
	 */
	private String contextToLoadID;
	/**
	 * the name of this Task (will be displayed within the navigation view)
	 */
	private String name;
	/**
	 * the description will be used as a tooltip in the case of an editor
	 */
	private String description;

	/**
	 * disabled Task will not be displayed (can be used when specific task will
	 * be activated from within the application environment)
	 */
	private boolean enabled;

	/**
	 * defines the type of this Task (see {@link TaskImplementationType}
	 */
	private TaskImplementationType type;

	/**
	 * the parent of this Task, if it is the root it can be <code>null</code>
	 */
	private Task parent;
	/**
	 * the children of this Task
	 */
	private List<Task> children;

	/**
	 * the name of an {@link TaskEditorInput} implementation
	 */
	private String taskEditorInputImpl;

	/**
	 * this is the relative path of a image, that is situated in the
	 * {@link Plugin} which checks the {@link Task} and tries to display the
	 * image.
	 */
	private String imageString;

	/**
	 * an image provided by the plugin that provides a specific {@link Image}
	 */
	private Image image;

	/**
	 * This is an optional {@code String} an will appear only at the root of the
	 * task tree, created for each plugin, depending of whether or not the
	 * plugins supports the usage of the help context id of the Eclipse API .
	 */
	private String helpContextId;

	/**
	 * This is an optional value (which is by default set to
	 * {@link TaskSortingType#ASCENDING}) that determines, how to sort the
	 * current tasks children.<br/>
	 * This may come in handy, if you had a special order of tasks in mind, that
	 * would otherwise be destroyed by the automatic ordering<br/>
	 * <br/>
	 * Be aware, that also the childrens children will be affected.
	 */
	private TaskSortingType sortChildren = TaskSortingType.ASCENDING;

	/**
	 * The groups, that the current task is restricted for.<br/>
	 * <br/>
	 * That means either via whitelisting (<code>inverse==false</code>) or
	 * blacklisting (<code>inverse==true</code>)
	 */
	private List<TaskGroupRestriction> groupRestrictions = null;

	public Task(String contextToLoadID, String name, String description,
			boolean enabled, TaskImplementationType taskImplementationType,
			String taskEditorInputImpl) {

		this.contextToLoadID = contextToLoadID;
		this.name = name;

		this.description = description;

		this.enabled = enabled;
		this.type = taskImplementationType;

		this.taskEditorInputImpl = taskEditorInputImpl;

		children = new LinkedList<Task>();
	}

	public Task(String contextToLoadID, String name, String description,
			boolean enabled, TaskImplementationType taskImplementationType,
			String taskEditorInputImpl, Task parent, List<Task> children) {

		this(contextToLoadID, name, description, enabled,
				taskImplementationType, taskEditorInputImpl);

		this.parent = parent;
		this.children = children;

		if (parent != null && !parent.isEnabled()) {
			this.enabled = false;
		}

		if (children == null) {
			this.children = new LinkedList<Task>();
		}
	}

	public void setTaskPluginID(String taskPluginID) {
		this.taskPluginID = taskPluginID;
	}

	public String getTaskPluginID() {
		return taskPluginID;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setParentTaskID(String parentTaskID) {
		this.parentTaskID = parentTaskID;
	}

	public String getParentTaskID() {
		return parentTaskID;
	}

	public String getContextToLoadID() {
		return contextToLoadID;
	}

	public void setContextToLoadID(String contextToLoadID) {
		this.contextToLoadID = contextToLoadID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public TaskImplementationType getTaskImplementationType() {
		return type;
	}

	public void setTaskImplementationType(TaskImplementationType type) {
		this.type = type;
	}

	public Task getParent() {
		return parent;
	}

	public void setParent(Task parent) {
		this.parent = parent;
	}

	public List<Task> getChildren() {
		return children;
	}

	public void setChildren(List<Task> children) {
		this.children = children;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setTaskEditorInputImpl(String taskEditorInputImpl) {
		this.taskEditorInputImpl = taskEditorInputImpl;
	}

	public String getTaskEditorInput() {
		return taskEditorInputImpl;
	}

	public void setImageString(String imageString) {
		this.imageString = imageString;
	}

	public String getImageString() {
		return imageString;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public Image getImage() {
		return image;
	}

	public void setHelpContextId(String helpContextId) {
		this.helpContextId = helpContextId;
	}

	public String getHelpContextId() {
		return helpContextId;
	}

	public void setSortChildren(TaskSortingType sortChildren) {
		this.sortChildren = sortChildren;
	}

	public TaskSortingType getSortChildren() {
		return sortChildren;
	}

	public void addChild(Task child) {

		if (this.children == null) {
			this.children = new LinkedList<Task>();
		}

		child.setParent(this);

		this.children.add(child);
	}

	public List<TaskGroupRestriction> getGroupRestrictions() {
		return groupRestrictions;
	}

	public void setGroupRestrictions(List<TaskGroupRestriction> groupRestriction) {
		this.groupRestrictions = groupRestriction;
	}

	public void addGroupRestrictions(TaskGroupRestriction groupRestriction) {
		if (this.groupRestrictions == null)
			this.groupRestrictions = new ArrayList<TaskGroupRestriction>();
		if (this.groupRestrictions.isEmpty()
				|| !this.groupRestrictions.contains(groupRestriction))
			this.groupRestrictions.add(groupRestriction);
	}

	@Override
	public String toString() {

		return String
				.format("[Task %30s - id=%50s, enable=%5b, editor=%6s, description=%s]",
						name, contextToLoadID, enabled, type.toString(),
						description);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + (type == null ? 0 : type.hashCode());
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime * result
				+ ((contextToLoadID == null) ? 0 : contextToLoadID.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Task other = (Task) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (type != other.type)
			return false;
		if (enabled != other.enabled)
			return false;
		if (contextToLoadID == null) {
			if (other.contextToLoadID != null)
				return false;
		} else if (!contextToLoadID.equals(other.contextToLoadID))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
