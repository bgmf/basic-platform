package de.dzimmermann.rcp.basicplatform.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import de.dzimmermann.rcp.basicplatform.util.ImplementationLoader;

/**
 * This method is intended for future use.<br>
 * The idea is to provide an API (partially via Fragments) that allows us to
 * have a model with a correct scope:
 * <ol>
 * <li><b><i>Application scope</i></b>: A singleton pattern for
 * {@link PCPApplicationMode#RCP} environments, that means Desktop environments
 * with a single running instance that is strictly separated from other
 * instances and</li>
 * <li><b><i>Session scope</i></b>: A pseudo singleton pattern for separate
 * session that occurs in {@link PCPApplicationMode#RAP} environments (in WWW
 * environments) where a single running application instance is shared by many
 * users, that would otherwise share and work on the same data as well. That is
 * not necessarily wanted.</li>
 * <li></li>
 * </ol>
 * 
 * @author dzimmermann
 */
public class BasicPlatformSessionModel {

	/**
	 * An instance of a provider (implementations via Fragments) that is used to
	 * create the singleton/session.
	 */
	private static final BasicPlatformSingletonProvider PCP_SESSION_MODEL_PROVIDER;
	static {
		PCP_SESSION_MODEL_PROVIDER = (BasicPlatformSingletonProvider) ImplementationLoader
				.newInstance(BasicPlatformSessionModel.class);
	}

	/**
	 * Use this method to create and/or retrieve the correct model for the
	 * current scope.
	 * 
	 * @return the model appropriate for the current scope
	 */
	public static BasicPlatformSessionModel getInstance() {
		return (BasicPlatformSessionModel) PCP_SESSION_MODEL_PROVIDER
				.getInstanceInternal();
	}

	/**
	 * This value should be set via the Framents (the
	 * {@link BasicPlatformSingletonProvider} implementations). Used to know in
	 * which scope (see {@link PCPApplicationMode}) we are running, if needed
	 * somewhere in the platform.
	 */
	protected PCPApplicationMode applicationMode;

	/**
	 * The current workspace. Needed for an {@link PCPApplicationMode#RCP} scope
	 */
	private String currentWorkspace;

	/**
	 * The current users name. Useful for both scopes.
	 */
	private String userName;
	/**
	 * The current users password. Useful for both scopes.
	 */
	private String password;

	/**
	 * The name of the targeted PFS server. Useful for both scopes.
	 */
	private String remoteServerName;
	/**
	 * The URL of the targeted PFS server. Useful for both scopes.
	 */
	private String remoteServerUrl;
	/**
	 * A value that decides whether we are pointing to a alternative server or
	 * not (developer mode only).
	 */
	private boolean remoteServerAlternative;

	/**
	 * A list of plugins, that are visible for the current user.
	 */
	private Set<String> visiblePlugins;
	/**
	 * Each plugin can have an own model, to prevent the plugins from handling
	 * sessions (Session Scope; see {@link PCPApplicationMode#RAP}) the model
	 * can be handled/retrieved/managed through the platforms main model.
	 */
	private Map<String, Object> pluginModels;

	/**
	 * Default constructor.<br>
	 * Only this one is allowed!!!
	 */
	BasicPlatformSessionModel() {
		visiblePlugins = new TreeSet<String>();
		pluginModels = new HashMap<String, Object>();
	}

	/**
	 * If you need to know the current application mode, you have to use this
	 * method. The value should be set via the Framgent implementations.
	 * 
	 * @return the current application mode
	 * @see PCPApplicationMode
	 */
	public PCPApplicationMode getApplicationMode() {
		return applicationMode;
	}

	/**
	 * In case of a single Application Scope (see {@link PCPApplicationMode#RCP}
	 * ) there should be a workspace.<br>
	 * Use this method to retrieve it.
	 * 
	 * @return the path of the workspace in the local file system
	 */
	public String getCurrentWorkspace() {
		return currentWorkspace;
	}

	/**
	 * In case of a single Application Scope (see {@link PCPApplicationMode#RCP}
	 * ) there should be a workspace.<br>
	 * Use this method to set it.
	 * 
	 * @param currentWorkspace
	 *            the path of the workspace in the local file system
	 */
	public void setCurrentWorkspace(String currentWorkspace) {
		this.currentWorkspace = currentWorkspace;
	}

	/**
	 * Retrieve the current users name.<br>
	 * Can be used by all application modes.
	 * 
	 * @return the current user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * The current users name.<br>
	 * Can be used by all application modes
	 * 
	 * @param userName
	 *            the current user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Retrieve the current users name.<br>
	 * Can be used by all application modes.
	 * 
	 * @return the current user name
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * The current users password.<br>
	 * Can be used by all application modes
	 * 
	 * @param password
	 *            the current users passoword
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Web Service calls are done via a remote server. It can be addressed via
	 * it's name, as specified within the plugin.xml.<br>
	 * Use this method to retrieve it.
	 * 
	 * @return the name of the remote server
	 */
	public String getRemoteServerName() {
		return remoteServerName;
	}

	/**
	 * Web Service calls are done via a remote server. It can be addressed via
	 * it's name, as specified within the plugin.xml.
	 * 
	 * @param remoteServerName
	 *            the name of the remote server
	 */
	public void setRemoteServerName(String remoteServerName) {
		this.remoteServerName = remoteServerName;
	}

	/**
	 * Web Service calls are done via a remote server. It can be addressed via
	 * it's URL, as specified within the plugin.xml.<br>
	 * Use this method to retrieve it.
	 * 
	 * @return the URL of the remote server
	 */
	public String getRemoteServerUrl() {
		return remoteServerUrl;
	}

	/**
	 * Web Service calls are done via a remote server. It can be addressed via
	 * it's URL, as specified within the plugin.xml.
	 * 
	 * @param remoteServerUrl
	 *            the URL of the remote server
	 */
	public void setRemoteServerUrl(String remoteServerUrl) {
		this.remoteServerUrl = remoteServerUrl;
	}

	/**
	 * In developer mode (currently only available for Application Scope,
	 * {@link PCPApplicationMode#RCP}) it is possible to use an other than the
	 * remote servers as defined within the plugin.xml. This value is just an
	 * hint, whether this is used or not.
	 * 
	 * @return <code>true</code>, if the alternative remote server is used
	 *         (remote server name equals the URL)
	 */
	public boolean isRemoteServerAlternative() {
		return remoteServerAlternative;
	}

	/**
	 * 
	 * @param remoteServerAlternative
	 */
	public void setRemoteServerAlternative(boolean remoteServerAlternative) {
		this.remoteServerAlternative = remoteServerAlternative;
	}

	/**
	 * Retrieve the list of visible plugins.
	 * 
	 * @return a set with unique plugin ids, that are visible for the user
	 */
	public Set<String> getVisiblePlugins() {
		return visiblePlugins;
	}

	/**
	 * Set a list of available plugin ids for the current session.
	 * 
	 * @param visiblePlugins
	 *            the list of visible plugin ids
	 */
	public void setVisiblePlugins(Set<String> visiblePlugins) {
		this.visiblePlugins = visiblePlugins;
	}

	/**
	 * Retrieve the plugin id to plugin model mapping.
	 * 
	 * @return key=plugin-id, value=plugin-model
	 */
	public Map<String, Object> getPluginModels() {
		return pluginModels;
	}
}
