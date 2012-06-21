package de.dzimmermann.rcp.basicplatform.model;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.ViewPart;

public enum TaskImplementationType {

	/**
	 * default type - used to simply place a plugin tree element to
	 * separate/group specific tasks
	 */
	NONE("none"),
	/**
	 * used to indicate, that the desired object to open is an editor (see
	 * {@link EditorPart})
	 */
	EDITOR("editor"),
	/**
	 * the target, that should be opened is another view (see {@link ViewPart})
	 */
	VIEW("view"),
	/**
	 * dialog indicator - tries to open a specific {@link TitleAreaDialog}
	 * within the plugin
	 */
	DIALOG("dialog"),
	/**
	 * wizard indicator - tries to open a {@link Wizard} from within the plugin
	 */
	WIZARD("wizard"),
	/**
	 * sometimes it's not that easy to specify, whether an editor, dialog or
	 * something else should be opened - than an {@link AbstractHandler}
	 * implementation might be useful
	 */
	HANDLER("handler");

	private String name;

	private TaskImplementationType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static TaskImplementationType getTypeByName(String name) {

		for (TaskImplementationType type : values()) {
			if (type.getName().equals(name)) {
				return type;
			}
		}

		return null;
	}
}
