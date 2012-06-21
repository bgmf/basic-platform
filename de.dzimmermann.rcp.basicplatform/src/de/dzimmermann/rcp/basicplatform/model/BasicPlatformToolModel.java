package de.dzimmermann.rcp.basicplatform.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;


/**
 * The internal model of the SSDTool.<br>
 * New name of the app is PCP; added the server list; added the truststore list.
 * 
 * @author dzimmermann
 * @version 1.4
 * @since SSDTool 0.0.1 (original version taken from now closed project
 *        PFSManager 0.5.8)
 */
public class BasicPlatformToolModel {

	public static final String MODEL_ID = "de.dzimmermann.rcp.basicplatform.model"; //$NON-NLS-1$

	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	public static final String EVENT_TASK_TREE_CHANGED = "ssdtool.taskTreeChanged"; //$NON-NLS-1$
	public static final String EVENT_SERVER_LIST_CHANGED = "ssdtool.serverListChanged"; //$NON-NLS-1$
	public static final String EVENT_TRUSTSTORE_LIST_CHANGED = "ssdtool.truststoreListChanged"; //$NON-NLS-1$

	private Task rootTask;
	private TruststoreList truststoreList;
	private ServerList serverList;

	public BasicPlatformToolModel() {
		this(null);
	}

	public BasicPlatformToolModel(TaskModel taskModel) {

		if (taskModel != null) {
			rootTask = taskModel.getPluginRoot();
		} else {
			rootTask = new BasicPlatformTaskModel().getPluginRoot();
		}
	}

	public Task getRootTask() {
		return rootTask;
	}

	public void setRootTask(Task rootTask) {

		final Task oldRootTask = this.rootTask;

		this.rootTask = rootTask;

		pcs.firePropertyChange(EVENT_TASK_TREE_CHANGED, oldRootTask, rootTask);
	}

	public void addRootTaskChild(Task child) {

		final List<Task> oldRootTaskChildren = rootTask.getChildren();

		rootTask.addChild(child);

		pcs.firePropertyChange(EVENT_TASK_TREE_CHANGED, oldRootTaskChildren,
				rootTask.getChildren());
	}

	public void setTruststoreList(TruststoreList truststoreList) {

		final TruststoreList oldTruststoreList = this.truststoreList;

		this.truststoreList = truststoreList;

		pcs.firePropertyChange(EVENT_TRUSTSTORE_LIST_CHANGED,
				oldTruststoreList, truststoreList);
	}

	public TruststoreList getTruststoreList() {
		return truststoreList;
	}

	public void addTruststore(String plugin, TrustStore trustStore) {

		final TruststoreList oldTruststoreList = this.truststoreList;

		TruststoreList newTruststoreList = new TruststoreList();
		if (truststoreList != null) {
			newTruststoreList.putAll(truststoreList);
		}

		newTruststoreList.addTrustStore(plugin, trustStore);

		truststoreList = newTruststoreList;

		pcs.firePropertyChange(EVENT_TRUSTSTORE_LIST_CHANGED,
				oldTruststoreList, truststoreList);
	}

	public ServerList getServerList() {
		return serverList;
	}

	public void setServerList(ServerList serverList) {

		final ServerList oldServerList = serverList;

		this.serverList = serverList;

		pcs.firePropertyChange(EVENT_SERVER_LIST_CHANGED, oldServerList,
				serverList);
	}

	public void addServerToList(String name, String url) {

		final ServerList oldServerList = serverList;

		ServerList newServerList = new ServerList();

		if (serverList != null) {
			newServerList.putAll(serverList);
		}

		newServerList.addServer(name, url);

		serverList = newServerList;

		pcs.firePropertyChange(EVENT_SERVER_LIST_CHANGED, oldServerList,
				serverList);
	}

	public void firePropertyChange(String propertyChangeEventName) {
		pcs.firePropertyChange(propertyChangeEventName, 0, 1);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.removePropertyChangeListener(listener);
	}
}
