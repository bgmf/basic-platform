package de.dzimmermann.rcp.basicplatform;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.Authenticator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.dzimmermann.rcp.basicplatform.model.BasicPlatformToolModel;
import de.dzimmermann.rcp.basicplatform.model.ServerList;
import de.dzimmermann.rcp.basicplatform.model.Task;
import de.dzimmermann.rcp.basicplatform.model.TaskModel;
import de.dzimmermann.rcp.basicplatform.model.TruststoreList;
import de.dzimmermann.rcp.basicplatform.ui.util.Console;
import de.dzimmermann.rcp.basicplatform.ui.util.Logger;
import de.dzimmermann.rcp.basicplatform.ui.util.searchhelper.SearchModel;
import de.dzimmermann.rcp.basicplatform.util.BasicPlatformExtensionPointConstants;
import de.dzimmermann.rcp.basicplatform.util.BasicPlatformPluginConstants;
import de.dzimmermann.rcp.basicplatform.util.InternalAuthenticator;
import de.dzimmermann.rcp.basicplatform.util.SSDToolImageRegistryLookup;
import de.dzimmermann.rcp.basicplatform.util.SSDToolUtils;

/**
 * The activator class controls the SSDTool plug-in life cycle
 * 
 * @author danielz
 * @since SSDTool V0.1.0
 * @version 0.9
 */
public class SSDToolPlugin extends AbstractUIPlugin {

	/**
	 * The SSDTool plug-in ID
	 */
	public static final String PLUGIN_ID = "de.dzimmermann.rcp.basicplatform"; //$NON-NLS-1$

	/**
	 * The shared instance
	 */
	private static SSDToolPlugin plugin;

	/**
	 * This {@link PropertyChangeSupport} is used to fire events from within the
	 * PCP.<br>
	 * To be aware of {@link PageChangedEvent}s fired by the PCP, attach via the
	 * methods {@link #addListener(PropertyChangeListener)} and
	 * {@link #removeListener(PropertyChangeListener)}.<br>
	 * Use the {@link #firePropertyChange(PropertyChangeEvent)} and
	 * {@link #firePropertyChange(String, Object, Object)} methods to trigger an
	 * event and publish it to all registered listeners
	 */
	private final PropertyChangeSupport pcs;

	/**
	 * the default console where syserr and sysout will be bound to
	 */
	private Console console;

	/**
	 * the local navigator {@link Task} tree model
	 */
	private BasicPlatformToolModel ssdToolModel;

	/**
	 * 
	 */
	private SearchModel searchModel;

	/**
	 * a list of contributing plug-ins (those, using the extension point
	 * {@value #PLUGIN_EP_ID})
	 */
	private Set<String> contributors;

	/**
	 * indicates that the current installation is used in developer mode, this
	 * overrides some security measures, the user should not be permitted to
	 * override this setting
	 */
	private boolean developerMode = false;

	/**
	 * This is the {@link Authenticator} implementation used to enter user name
	 * and password for server who might request this.
	 */
	private InternalAuthenticator authenticator;

	/**
	 * The constructor
	 */
	public SSDToolPlugin() {

		pcs = new PropertyChangeSupport(this);

		console = new Console(BasicPlatformPluginConstants.CONSOLE_NAME);
		console.hookSystemOutputs();

		ssdToolModel = new BasicPlatformToolModel();

		contributors = new HashSet<String>();

		authenticator = SSDToolUtils.getAuthentication();
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry imageRegistry) {
		SSDToolImageRegistryLookup.fillSSDToolImageRegistry(imageRegistry);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext context) throws Exception {

		super.start(context);
		plugin = this;

		// read the extension points of this plugin...
		readExtensionPointData();

		// set the current authenticator to be used for authenticate at another
		// web server
		Authenticator.setDefault(authenticator);

		// check developer mode
		developerMode = SSDToolUtils.isDeveloperMode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void stop(BundleContext context) throws Exception {

		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static SSDToolPlugin getDefault() {
		return plugin;
	}

	public Console getConsole() {
		return console;
	}

	public BasicPlatformToolModel getSSDToolModel() {
		return ssdToolModel;
	}

	public Set<String> getContributors() {
		return contributors;
	}

	public boolean isDeveloperMode() {
		return developerMode;
	}

	public SearchModel getSearchModel() {
		return searchModel;
	}

	public InternalAuthenticator getAuthenticator() {
		if (authenticator == null) {
			authenticator = SSDToolUtils.getAuthentication();
		}
		Authenticator.setDefault(authenticator);
		return authenticator;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	/**
	 * this method will try to build the navigation tree by adding the items to
	 * the local SSDTool plugin tree structure
	 */
	@SuppressWarnings("deprecation")
	private void readExtensionPointData() {

		/*
		 * register plugins into the pcp's task tree
		 */

		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(
						BasicPlatformExtensionPointConstants.PLUGIN_EP_ID);

		if (config == null || config.length == 0) {
			Logger.logWarning("Could not load other PCP plugins - " //$NON-NLS-1$
					+ "unable to retrieve the plugins or no plugins available."); //$NON-NLS-1$
		} else {

			List<TaskModel> modelsToIntegrate = new ArrayList<TaskModel>();

			for (IConfigurationElement configurationElement : config) {

				TaskModel pluginTaskModel = SSDToolUtils
						.getExtensionPointTaskModel(configurationElement);

				if (pluginTaskModel != null) {
					if (pluginTaskModel.getPluginRoot().getParentTaskID() == null
							|| pluginTaskModel.getPluginRoot()
									.getParentTaskID().isEmpty()) {
						ssdToolModel.addRootTaskChild(pluginTaskModel
								.getPluginRoot());
					} else {
						modelsToIntegrate.add(pluginTaskModel);
					}
				}
			}

			if (!modelsToIntegrate.isEmpty()) {
				findAndIntegrateTaskModels(modelsToIntegrate);
			}
		}

		/*
		 * register all servers
		 */

		config = Platform.getExtensionRegistry().getConfigurationElementsFor(
				BasicPlatformExtensionPointConstants.SSDTOOL_SERVER_EP_ID);

		if (config == null || config.length == 0) {
			Logger.logWarning("Could not load Servers into the internal list - " //$NON-NLS-1$
					+ "unable to retrieve the hosting plugins or no plugins available."); //$NON-NLS-1$
		} else {

			for (IConfigurationElement configurationElement : config) {

				TruststoreList truststore = SSDToolUtils
						.getTruststorePath(configurationElement);

				if (truststore != null) {
					for (String plugin : truststore.keySet()) {
						ssdToolModel.addTruststore(plugin,
								truststore.get(plugin));
					}
				}

				ServerList result = SSDToolUtils
						.getExtensionPointServerList(configurationElement);

				if (result != null) {
					for (String name : result.keySet()) {
						ssdToolModel.addServerToList(name, result.get(name));
					}
				}
			}
		}
	}

	private void findAndIntegrateTaskModels(List<TaskModel> taskModels) {

		for (TaskModel tm : taskModels) {
			if (!checkAndIntegrate(ssdToolModel.getRootTask(),
					tm.getPluginRoot())) {
				String pluginId = tm.getPluginRoot().getTaskPluginID();
				Logger.logWarning(pluginId != null ? String
						.format("Cannot integrate Plugin %s. A Plugin with a root task id %s was not found!", //$NON-NLS-1$
								pluginId, tm.getPluginRoot().getParentTaskID())
						: String.format(
								"Could not integrate a Plugin with a parent tast %s. A Plugin with a root task id %s was not found!", //$NON-NLS-1$
								tm.getPluginRoot().getClass().getName(), tm
										.getPluginRoot().getParentTaskID()));
			}
		}
	}

	private boolean checkAndIntegrate(Task toCheck, Task toIntegrate) {

		if (toCheck.getId() != null && toIntegrate.getParentTaskID() != null
				&& toCheck.getId().equals(toIntegrate.getParentTaskID())) {
			toCheck.addChild(toIntegrate);
			return true;
		} else {
			for (Task t : toCheck.getChildren()) {
				if (checkAndIntegrate(t, toIntegrate)) {
					return true;
				}
			}
		}
		return false;
	}

	//
	// property change support
	//

	/**
	 * adds an interessed {@link PropertyChangeListener} for
	 * {@link PropertyChangeEvent}s fired by the PCP
	 */
	public void addListener(PropertyChangeListener pcl) {
		this.pcs.addPropertyChangeListener(pcl);
	}

	/**
	 * removes the specified listener, if allready registerd (see
	 * {@link #addListener(PropertyChangeListener)})
	 * 
	 * @param pcl
	 */
	public void removeListener(PropertyChangeListener pcl) {
		this.pcs.removePropertyChangeListener(pcl);
	}

	public void firePropertyChange(PropertyChangeEvent evt) {
		pcs.firePropertyChange(evt);
	}

	public void firePropertyChange(String propertyName, Object oldValue,
			Object newValue) {
		pcs.firePropertyChange(propertyName, oldValue, newValue);
	}
}
