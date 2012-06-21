package de.dzimmermann.rcp.basicplatform.model;


/**
 * This is only the initial (and empty) Task model, which will be enhanced by
 * the plugins and then displayed by a navigation view
 * 
 * @author dzimmermann
 * @since SSDTool 0.0.0
 * @version 1.0
 */
public class BasicPlatformTaskModel extends TaskModel {

	public BasicPlatformTaskModel() {

		// ---
		// virtual root - this one will NOT be displayed!
		// but all plugins, implementing the local Extension Point, will be
		// attached as children to this Task
		// ---
		this.pluginRoot = new Task(null, "Non-Visible Root", null, true, //$NON-NLS-1$
				TaskImplementationType.NONE, null, null, null);
	}
}
