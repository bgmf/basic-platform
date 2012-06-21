package de.dzimmermann.rcp.basicplatform.util;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import de.dzimmermann.rcp.basicplatform.SSDToolPlugin;
import de.dzimmermann.rcp.basicplatform.factory.ExecutableFactory;
import de.dzimmermann.rcp.basicplatform.model.ServerList;
import de.dzimmermann.rcp.basicplatform.model.Task;
import de.dzimmermann.rcp.basicplatform.model.TaskEditorInput;
import de.dzimmermann.rcp.basicplatform.model.TaskGroupRestriction;
import de.dzimmermann.rcp.basicplatform.model.TaskImplementationType;
import de.dzimmermann.rcp.basicplatform.model.TaskModel;
import de.dzimmermann.rcp.basicplatform.model.TaskSortingType;
import de.dzimmermann.rcp.basicplatform.model.TruststoreList;
import de.dzimmermann.rcp.basicplatform.services.IAvailablePluginService;
import de.dzimmermann.rcp.basicplatform.services.IGroupService;
import de.dzimmermann.rcp.basicplatform.ui.util.Logger;
import de.dzimmermann.rcp.basicplatform.ui.util.searchhelper.SearchData;
import de.dzimmermann.rcp.basicplatform.ui.util.searchhelper.SearchHelperFactory;
import de.dzimmermann.rcp.basicplatform.ui.util.searchhelper.SearchModel;
import de.dzimmermann.rcp.basicplatform.ui.util.searchhelper.SearchOption;
import de.dzimmermann.rcp.basicplatform.ui.view.FormsPluginNavigatorView;

/**
 * This is a utility class for some common sets of work to be done throughout
 * the SSDTool.
 * 
 * @author danielz
 * @since SSDTool V0.1.0 (original version taken from now closed project
 *        PFSManager 0.5.8)
 * @version 1.9
 */
public class SSDToolUtils {

	// ---------------
	// Dialog Settings
	// ---------------

	/**
	 * this method retrieves the last opened directory of this Plugin by its
	 * dilaog setting<br>
	 * <br>
	 * this is a plug-in dependent workbench setting <br>
	 * <br>
	 * see {@link BasicPlatformPluginConstants#OPEN_SAVE_LAST_DIR_KEY}
	 * 
	 * @return the last directory
	 */
	public static String getDialogSettingsLastOpendDirectory() {
		return SSDToolPlugin.getDefault().getDialogSettings()
				.get(BasicPlatformPluginConstants.OPEN_SAVE_LAST_DIR_KEY);
	}

	/**
	 * this method stored the given string to the dialog settings as the last
	 * opened directory.<br>
	 * <br>
	 * this is a plug-in dependent workbench setting
	 * 
	 * @param lastDir
	 *            the last directory to be stored
	 */
	public static void setDialogSettingsLastOpendDirectory(String lastDir,
			boolean isDirectory) {

		String storedLastDir = getDialogSettingsLastOpendDirectory();

		String newLastDir = !isDirectory ? new File(lastDir).getParent()
				: lastDir;

		if (storedLastDir == null || storedLastDir.isEmpty()
				|| !storedLastDir.equals(newLastDir))
			SSDToolPlugin
					.getDefault()
					.getDialogSettings()
					.put(BasicPlatformPluginConstants.OPEN_SAVE_LAST_DIR_KEY,
							newLastDir);
	}

	/**
	 * Retrieve {@link IDialogSettings} by the plugins settings, if the
	 * <code>create</code> parameter is <code>true</code>, the settings will be
	 * created, if there are not existing at the moment.
	 * 
	 * @param sectionName
	 *            the section to return, if it's present within the plugins
	 *            dialog settings
	 * @param create
	 *            <code>true</code> if a non-existent section should be created
	 * @return the section or <code>null</code>
	 */
	public static IDialogSettings getDialogSettingsBySectionName(
			String sectionName, boolean create) {

		IDialogSettings settings = SSDToolPlugin.getDefault()
				.getDialogSettings();
		if (settings != null) {
			IDialogSettings selectWorksSettings = settings
					.getSection(sectionName);
			if (selectWorksSettings != null) {
				return selectWorksSettings;
			} else if (create) {
				return settings.addNewSection(sectionName);
			} else {
				return null;
			}
		}

		return null;
	}

	// -----------
	// Preferences
	// -----------

	// 1) recent workspace

	/**
	 * retrieve the current status for the workspace selection dialog from the
	 * users preferences
	 * 
	 * @return <code>true</code>, if the dialog should be shown, otherwise
	 *         <code>false</code>
	 */
	public static boolean isShowWorkspaceSelectionDialog() {

		IScopeContext scope = ConfigurationScope.INSTANCE;
		Preferences prefs = scope.getNode(SSDToolPlugin.PLUGIN_ID);

		return prefs.getBoolean(
				BasicPlatformPreferenceConstants.RECENT_WORKSPACES_SHOW, true);
	}

	/**
	 * set the value within the users preferences to the given one
	 * 
	 * @param showWSDialog
	 *            the new boolean
	 */
	public static void setIsShowWorkspaceSelectionDialog(boolean showWSDialog) {

		final boolean old = isShowWorkspaceSelectionDialog();

		IScopeContext scope = ConfigurationScope.INSTANCE;
		Preferences prefs = scope.getNode(SSDToolPlugin.PLUGIN_ID);

		prefs.putBoolean(
				BasicPlatformPreferenceConstants.RECENT_WORKSPACES_SHOW,
				showWSDialog);

		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			Logger.logError(e);
		}

		SSDToolPlugin
				.getDefault()
				.firePropertyChange(
						BasicPlatformPreferenceConstants.RECENT_WORKSPACES_SHOW_CHANGED,
						old, showWSDialog);
	}

	/**
	 * get all recent workspaces, separated by the value of
	 * {@link BasicPlatformPreferenceConstants#RECENT_WORKSPACES_DELIMETER} ('
	 * {@value BasicPlatformPreferenceConstants#RECENT_WORKSPACES_DELIMETER}')
	 * 
	 * @return all recently opened workspaces as one huge string
	 */
	public static String getRecentWorkspaces() {

		IScopeContext scope = ConfigurationScope.INSTANCE;
		Preferences prefs = scope.getNode(SSDToolPlugin.PLUGIN_ID);

		return prefs.get(BasicPlatformPreferenceConstants.RECENT_WORKSPACES,
				null);
	}

	/**
	 * retrieves the current workspace, this is the top (that means the first)
	 * element of the recently opened workspaces list (see
	 * {@link #getRecentWorkspaces()})
	 * 
	 * @return the currently active workspace
	 */
	public static String getCurrentWorkspace() {

		String recentWorkspaces = getRecentWorkspaces();

		if (recentWorkspaces != null) {

			return recentWorkspaces
					.split(BasicPlatformPreferenceConstants.RECENT_WORKSPACES_DELIMETER)[0];
		}

		return null;
	}

	/**
	 * Tries to get the current workspace from the preferences (see
	 * {@link #getCurrentWorkspace()}) and creates the directory, if it s not
	 * present at all.<br>
	 * 
	 * @return returns <code>null</code>, if the current workspace could not be
	 *         retrieved from the preferences
	 */
	public static File getCurrentWorkspaceFile() {

		String currentWorkspace = getCurrentWorkspace();

		if (currentWorkspace != null) {

			File workspace = new File(currentWorkspace);

			if (!workspace.isDirectory()) {
				workspace.mkdirs();
			}

			return workspace;
		}

		return null;
	}

	/**
	 * This method is used to completely overwrite the recent workspaces, so use
	 * with caution!<br>
	 * The default use case is to reset the recent workspaces from within the
	 * SSDTool's preferences page.
	 * 
	 * @param recentWorkspaces
	 *            the string which is intended to replace the current recently
	 *            opened workspaces
	 */
	public static void setRecentWorkspaces(String recentWorkspaces) {

		if (recentWorkspaces == null) {
			return;
		}

		final String old = getRecentWorkspaces();

		IScopeContext scope = ConfigurationScope.INSTANCE;
		Preferences prefs = scope.getNode(SSDToolPlugin.PLUGIN_ID);

		prefs.put(BasicPlatformPreferenceConstants.RECENT_WORKSPACES,
				recentWorkspaces);

		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			Logger.logError(e);
		}

		SSDToolPlugin.getDefault().firePropertyChange(
				BasicPlatformPreferenceConstants.RECENT_WORKSPACES_CHANGED,
				old, recentWorkspaces);
	}

	/**
	 * add a new workspace to the list of workspaces, that means prepend it to
	 * the workspaces list or, if it is already contained within this list, move
	 * the entry to the top<br>
	 * test for the max amount of values, too
	 * 
	 * @param ws
	 *            the workspace to add
	 */
	public static void addRecentWorkspace(String ws) {

		if (ws == null) {
			return;
		}

		IScopeContext scope = ConfigurationScope.INSTANCE;
		Preferences prefs = scope.getNode(SSDToolPlugin.PLUGIN_ID);

		String recentWorkspaces = getRecentWorkspaces();

		if (recentWorkspaces != null) {

			String[] splittedRecentWS = recentWorkspaces
					.split(BasicPlatformPreferenceConstants.RECENT_WORKSPACES_DELIMETER);

			boolean containsWS = false;
			int index = -1;

			for (int i = 0; i < splittedRecentWS.length; i++) {
				if (ws.equals(splittedRecentWS[i])) {
					containsWS = true;
					index = i;
					break;
				}
			}

			int maxAmount = -1;

			Set<String> keys = new HashSet<String>();
			try {
				Collections.addAll(keys, prefs.keys());
			} catch (BackingStoreException e) {
				Logger.logError(e);
			}
			if (keys.contains(BasicPlatformPreferenceConstants.RECENT_WORKSPACES_MAX_AMOUNT)) {
				maxAmount = prefs
						.getInt(BasicPlatformPreferenceConstants.RECENT_WORKSPACES_MAX_AMOUNT,
								BasicPlatformPreferenceConstants.RECENT_WORKSPACES_MAX_AMOUNT_DEFAULT);
			} else {
				maxAmount = BasicPlatformPreferenceConstants.RECENT_WORKSPACES_MAX_AMOUNT_DEFAULT;
				prefs.putInt(
						BasicPlatformPreferenceConstants.RECENT_WORKSPACES_MAX_AMOUNT,
						BasicPlatformPreferenceConstants.RECENT_WORKSPACES_MAX_AMOUNT_DEFAULT);
			}

			String newRecent = null;

			if (!containsWS) {
				if (maxAmount != -1) {
					if (splittedRecentWS.length + 1 > maxAmount) {

						newRecent = ws;
						for (int i = 0; i < splittedRecentWS.length - 1; i++) {
							newRecent += BasicPlatformPreferenceConstants.RECENT_WORKSPACES_DELIMETER
									+ splittedRecentWS[i];
						}

					} else {
						newRecent = ws
								+ BasicPlatformPreferenceConstants.RECENT_WORKSPACES_DELIMETER
								+ recentWorkspaces;
					}
				} else {
					newRecent = ws
							+ BasicPlatformPreferenceConstants.RECENT_WORKSPACES_DELIMETER
							+ recentWorkspaces;
				}

			} else {
				newRecent = splittedRecentWS[index];
				for (int i = 0; i < splittedRecentWS.length; i++) {
					if (i != index) {
						newRecent += BasicPlatformPreferenceConstants.RECENT_WORKSPACES_DELIMETER
								+ splittedRecentWS[i];
					}
				}
			}

			if (newRecent != null && !newRecent.equals(recentWorkspaces)) {
				setRecentWorkspaces(newRecent);
			}

		} else {
			setRecentWorkspaces(ws);
		}

		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			Logger.logError(e);
		}
	}

	// 2) remote server

	/**
	 * get the recent server name
	 * 
	 * @return the last selected remote server name as a string
	 */
	public static String getRecentRemoteServer() {

		IScopeContext scope = ConfigurationScope.INSTANCE;
		Preferences prefs = scope.getNode(SSDToolPlugin.PLUGIN_ID);

		return prefs.get(
				BasicPlatformPreferenceConstants.RECENT_REMOTE_SERVER_NAME,
				null);
	}

	/**
	 * This method is used to completely overwrite the recently selected remote
	 * server, so use with caution!<br>
	 * 
	 * @param recentRemoteServer
	 *            the string which is intended to replace the current recently
	 *            selected remote server by its name
	 */
	public static void setRecentRemoteServer(String recentRemoteServer) {

		if (recentRemoteServer == null) {
			return;
		}

		final String old = getRecentRemoteServer();

		IScopeContext scope = ConfigurationScope.INSTANCE;
		Preferences prefs = scope.getNode(SSDToolPlugin.PLUGIN_ID);

		prefs.put(BasicPlatformPreferenceConstants.RECENT_REMOTE_SERVER_NAME,
				recentRemoteServer);

		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			Logger.logError(e);
		}

		SSDToolPlugin
				.getDefault()
				.firePropertyChange(
						BasicPlatformPreferenceConstants.RECENT_REMOTE_SERVER_NAME_CHANGED,
						old, recentRemoteServer);
	}

	public static String getRecentRemoteServerUrl() {
		return SSDToolPlugin.getDefault().getSSDToolModel().getServerList()
				.get(getRecentRemoteServer());
	}

	public static boolean isUseAlternativeServerHost() {

		boolean useAlternativeServerHost = false;

		IScopeContext scope = ConfigurationScope.INSTANCE;
		Preferences prefs = scope.getNode(SSDToolPlugin.PLUGIN_ID);

		useAlternativeServerHost = prefs
				.getBoolean(
						BasicPlatformPreferenceConstants.RECENT_REMOTE_SERVER_USE_ALTERNATIVE,
						false);

		return useAlternativeServerHost;
	}

	public static void setUseAlternativeServerHost(
			boolean useAlternativeServerHost) {

		final boolean oldUseAlternativeServerHost = isUseAlternativeServerHost();

		IScopeContext scope = ConfigurationScope.INSTANCE;
		Preferences prefs = scope.getNode(SSDToolPlugin.PLUGIN_ID);

		prefs.putBoolean(
				BasicPlatformPreferenceConstants.RECENT_REMOTE_SERVER_USE_ALTERNATIVE,
				useAlternativeServerHost);

		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			Logger.logError(e);
		}

		SSDToolPlugin
				.getDefault()
				.firePropertyChange(
						BasicPlatformPreferenceConstants.RECENT_REMOTE_SERVER_USE_ALTERNATIVE_CHANGED,
						oldUseAlternativeServerHost, useAlternativeServerHost);
	}

	public static String getAlternativeServerHost() {

		IScopeContext scope = ConfigurationScope.INSTANCE;
		Preferences prefs = scope.getNode(SSDToolPlugin.PLUGIN_ID);

		return prefs
				.get(BasicPlatformPreferenceConstants.RECENT_REMOTE_SERVER_ALTERNATIVE,
						null);
	}

	public static void setAlternativeServerHost(String alternativeServerHost) {

		if (alternativeServerHost == null) {
			return;
		}

		final String old = getAlternativeServerHost();

		IScopeContext scope = ConfigurationScope.INSTANCE;
		Preferences prefs = scope.getNode(SSDToolPlugin.PLUGIN_ID);

		prefs.put(
				BasicPlatformPreferenceConstants.RECENT_REMOTE_SERVER_ALTERNATIVE,
				alternativeServerHost);

		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			Logger.logError(e);
		}

		SSDToolPlugin
				.getDefault()
				.firePropertyChange(
						BasicPlatformPreferenceConstants.RECENT_REMOTE_SERVER_ALTERNATIVE_CHANGED,
						old, alternativeServerHost);
	}

	// 3) developer mode preference

	/**
	 * This method retrieves the current development state from the preferences.
	 * 
	 * @return the current development state
	 */
	public static boolean isDeveloperMode() {

		boolean developerMode = false;

		IScopeContext scope = ConfigurationScope.INSTANCE;
		Preferences prefs = scope.getNode(SSDToolPlugin.PLUGIN_ID);

		developerMode = prefs.getBoolean(
				BasicPlatformPreferenceConstants.DEVELOPER_MODE, false);

		return developerMode;
	}

	/**
	 * This method set the developer mode. Attention: This should be used with
	 * caution and is not intended to be presented in any case to a final
	 * customer build!
	 * 
	 * @param developerMode
	 *            the new developer mode
	 */
	public static void setDeveloperMode(boolean developerMode) {

		final boolean oldDevMode = isDeveloperMode();

		IScopeContext scope = ConfigurationScope.INSTANCE;
		Preferences prefs = scope.getNode(SSDToolPlugin.PLUGIN_ID);

		prefs.putBoolean(BasicPlatformPreferenceConstants.DEVELOPER_MODE,
				developerMode);

		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			Logger.logError(e);
		}

		SSDToolPlugin.getDefault().firePropertyChange(
				BasicPlatformPreferenceConstants.DEVELOPER_MODE_CHANGED,
				oldDevMode, developerMode);
	}

	// 4) Trust- and Keystore preferences

	/**
	 * Tries to retrieve the current path to the keystore, can be
	 * <code>null</code>, if the keystore ist not set.
	 * 
	 * @return the path to the keystore in the file system, or <code>null</code>
	 *         , if the path is not set yet
	 */
	public static String getCurrentKeyStorePath() {

		String result = null;

		IScopeContext scope = ConfigurationScope.INSTANCE;
		Preferences prefs = scope.getNode(SSDToolPlugin.PLUGIN_ID);

		result = prefs.get(BasicPlatformPreferenceConstants.KEYSTORE_LOACTION,
				null);

		return result;
	}

	/**
	 * Sets the current path to the keystore.
	 * 
	 * @param path
	 *            the full path to the keystore file
	 */
	public static void setCurrentKeyStorePath(String path) {

		if (path == null) {
			return;
		}

		final String oldPath = getCurrentKeyStorePath();

		IScopeContext scope = ConfigurationScope.INSTANCE;
		Preferences prefs = scope.getNode(SSDToolPlugin.PLUGIN_ID);

		prefs.put(BasicPlatformPreferenceConstants.KEYSTORE_LOACTION, path);

		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			Logger.logError(e);
		}

		SSDToolPlugin.getDefault().firePropertyChange(
				BasicPlatformPreferenceConstants.KEYSTORE_LOACTION_CHANGED,
				oldPath, path);
	}

	/**
	 * Tries to retrieve the persisted password for the current keystore. The
	 * default will be "password" if it is not set yet.
	 * 
	 * @return the currently persisted password or "password", if it is not set
	 */
	public static String getCurrentKeyStorePassword() {

		String result = null;

		IScopeContext scope = ConfigurationScope.INSTANCE;
		Preferences prefs = scope.getNode(SSDToolPlugin.PLUGIN_ID);

		result = prefs.get(BasicPlatformPreferenceConstants.KEYSTORE_LOACTION,
				"password"); //$NON-NLS-1$

		return result;
	}

	/**
	 * Sets the current password to the keystore.
	 * 
	 * @param path
	 *            the password to the keystore
	 */
	public static void setCurrentKeyStorePassword(String password) {

		if (password == null) {
			return;
		}

		final String oldPassword = getCurrentKeyStorePassword();

		IScopeContext scope = ConfigurationScope.INSTANCE;
		Preferences prefs = scope.getNode(SSDToolPlugin.PLUGIN_ID);

		prefs.put(BasicPlatformPreferenceConstants.KEYSTORE_LOACTION_PASSWORD,
				password);

		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			Logger.logError(e);
		}

		SSDToolPlugin
				.getDefault()
				.firePropertyChange(
						BasicPlatformPreferenceConstants.KEYSTORE_LOACTION_PASSWORD_CHANGED,
						oldPassword, password);
	}

	/**
	 * Tries to retrieve the current path to the truststore, can be
	 * <code>null</code>, if the truststore ist not set.
	 * 
	 * @return the path to the truststore in the file system, or
	 *         <code>null</code>, if the path is not set yet
	 */
	public static String getCurrentTrustStorePath() {

		String result = null;

		IScopeContext scope = ConfigurationScope.INSTANCE;
		Preferences prefs = scope.getNode(SSDToolPlugin.PLUGIN_ID);

		result = prefs.get(
				BasicPlatformPreferenceConstants.TRUSTSTORE_LOACTION, null);

		return result;
	}

	/**
	 * Sets the current path to the truststore.
	 * 
	 * @param path
	 *            the full path to the truststore file
	 */
	public static void setCurrentTrustStorePath(String path) {

		if (path == null) {
			return;
		}

		final String oldPath = getCurrentTrustStorePath();

		IScopeContext scope = ConfigurationScope.INSTANCE;
		Preferences prefs = scope.getNode(SSDToolPlugin.PLUGIN_ID);

		prefs.put(BasicPlatformPreferenceConstants.TRUSTSTORE_LOACTION, path);

		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			Logger.logError(e);
		}

		SSDToolPlugin.getDefault().firePropertyChange(
				BasicPlatformPreferenceConstants.TRUSTSTORE_LOACTION_CHANGED,
				oldPath, path);
	}

	/**
	 * Tries to retrieve the persisted password for the current truststore. The
	 * default will be "password" if it is not set yet.
	 * 
	 * @return the currently persisted password or "password", if it is not set
	 */
	public static String getCurrentTrustStorePassword() {

		String result = null;

		IScopeContext scope = ConfigurationScope.INSTANCE;
		Preferences prefs = scope.getNode(SSDToolPlugin.PLUGIN_ID);

		result = prefs.get(
				BasicPlatformPreferenceConstants.TRUSTSTORE_LOACTION,
				"password"); //$NON-NLS-1$

		return result;
	}

	/**
	 * Sets the current path to the truststore.
	 * 
	 * @param path
	 *            the full path to the truststore file
	 */
	public static void setCurrentTrustStorePassword(String password) {

		if (password == null) {
			return;
		}

		final String oldPassword = getCurrentTrustStorePassword();

		IScopeContext scope = ConfigurationScope.INSTANCE;
		Preferences prefs = scope.getNode(SSDToolPlugin.PLUGIN_ID);

		prefs.put(
				BasicPlatformPreferenceConstants.TRUSTSTORE_LOACTION_PASSWORD,
				password);

		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			Logger.logError(e);
		}

		SSDToolPlugin
				.getDefault()
				.firePropertyChange(
						BasicPlatformPreferenceConstants.TRUSTSTORE_LOACTION_PASSWORD_CHANGED,
						oldPassword, password);
	}

	// 5) user name and password

	/**
	 * This method tries to read the last authentication settings from the
	 * preferences and returns a {@link InternalAuthenticator} object, to be
	 * used for the authentication of a user.
	 * 
	 * @return the {@link InternalAuthenticator} as retrieved from the
	 *         preferences
	 */
	public static InternalAuthenticator getAuthentication() {

		InternalAuthenticator result = null;

		IScopeContext scope = ConfigurationScope.INSTANCE;
		Preferences prefs = scope.getNode(SSDToolPlugin.PLUGIN_ID);

		// retrieve current user and passwd
		String userName = prefs.get(
				BasicPlatformPreferenceConstants.SERVER_AUTH_USER, ""); //$NON-NLS-1$
		String password = prefs.get(
				BasicPlatformPreferenceConstants.SERVER_AUTH_PASSWD, ""); //$NON-NLS-1$

		// check wheter it is allowed to store password...
		boolean storePassword = getStoreAuthenticationPassword();
		// ... if not and the passwd is set, clear it
		// (this is done out of security!)
		if (!storePassword && password != null && !password.isEmpty()) {
			prefs.put(BasicPlatformPreferenceConstants.SERVER_AUTH_PASSWD, ""); //$NON-NLS-1$
			try {
				prefs.flush();
			} catch (BackingStoreException e) {
				Logger.logError(e);
			}
			SSDToolPlugin
					.getDefault()
					.firePropertyChange(
							BasicPlatformPreferenceConstants.SERVER_AUTH_PASSWD_CHANGED,
							password, ""); //$NON-NLS-1$
			password = ""; //$NON-NLS-1$
		}

		result = new InternalAuthenticator(userName, password);

		return result;
	}

	/**
	 * This method is used to store the current {@link InternalAuthenticator},
	 * the password is stored, if the flag is <code>true</code>.
	 * 
	 * @param authenticator
	 *            the {@link InternalAuthenticator} with the user name and the
	 *            password
	 */
	public static void storeAuthentication(InternalAuthenticator authenticator) {

		IScopeContext scope = ConfigurationScope.INSTANCE;
		Preferences prefs = scope.getNode(SSDToolPlugin.PLUGIN_ID);

		InternalAuthenticator currentAuthenticator = getAuthentication();

		// check whether it is allowed to store password...
		boolean storePassword = getStoreAuthenticationPassword();
		// ... if not and the passwd is set, clear it
		// (this is done out of security!)
		if (!storePassword && currentAuthenticator.getPassword() != null
				&& !currentAuthenticator.getPassword().isEmpty()) {
			prefs.put(BasicPlatformPreferenceConstants.SERVER_AUTH_PASSWD, ""); //$NON-NLS-1$
			try {
				prefs.flush();
			} catch (BackingStoreException e) {
				Logger.logError(e);
			}
			SSDToolPlugin
					.getDefault()
					.firePropertyChange(
							BasicPlatformPreferenceConstants.SERVER_AUTH_PASSWD_CHANGED,
							currentAuthenticator.getPassword(), ""); //$NON-NLS-1$
			currentAuthenticator.setPassword(""); //$NON-NLS-1$
			authenticator.setPassword(""); //$NON-NLS-1$
		}
		// ... it is allowed, check for equality and store it, if they are
		// different
		if (storePassword
				&& !authenticator.getPassword().equals(
						currentAuthenticator.getPassword())) {
			prefs.put(BasicPlatformPreferenceConstants.SERVER_AUTH_PASSWD,
					authenticator.getPassword());
			try {
				prefs.flush();
			} catch (BackingStoreException e) {
				Logger.logError(e);
			}
			SSDToolPlugin
					.getDefault()
					.firePropertyChange(
							BasicPlatformPreferenceConstants.SERVER_AUTH_PASSWD_CHANGED,
							currentAuthenticator.getPassword(),
							authenticator.getPassword());
		}

		// check for equality of the user name
		// if they are different, store them
		if (!authenticator.getUserName().equals(
				currentAuthenticator.getUserName())) {
			prefs.put(BasicPlatformPreferenceConstants.SERVER_AUTH_USER,
					authenticator.getUserName());
			try {
				prefs.flush();
			} catch (BackingStoreException e) {
				Logger.logError(e);
			}
			SSDToolPlugin.getDefault().firePropertyChange(
					BasicPlatformPreferenceConstants.SERVER_AUTH_USER_CHANGED,
					currentAuthenticator.getUserName(),
					authenticator.getUserName());
		}
	}

	/**
	 * Tries to retrieve, whether or not the password should be stored.<br>
	 * See {@link #setStoreAuthenticationPassword(boolean)} for the potential
	 * risks of this measure.
	 * 
	 * @return <code>true</code> if the password should be stored, otherwise
	 *         <code>false</code>
	 */
	public static boolean getStoreAuthenticationPassword() {

		boolean result = false;

		IScopeContext scope = ConfigurationScope.INSTANCE;
		Preferences prefs = scope.getNode(SSDToolPlugin.PLUGIN_ID);

		result = prefs.getBoolean(
				BasicPlatformPreferenceConstants.SERVER_AUTH_PASSWD_STORE,
				false);

		return result;
	}

	/**
	 * This method allows to decide, whether the password will be stored or not.<br>
	 * ATTENTION: This is no absolute security measure for the password, since
	 * with the knowledge where to look, the preference can easily be overridden
	 * and the password would be stored the next time.
	 * 
	 * @param storePassword
	 *            <code>true</code> if the password should be stored, otherwise
	 *            <code>false</code>
	 */
	public static void setStoreAuthenticationPassword(boolean storePassword) {

		boolean oldStorePassword = getStoreAuthenticationPassword();

		IScopeContext scope = ConfigurationScope.INSTANCE;
		Preferences prefs = scope.getNode(SSDToolPlugin.PLUGIN_ID);

		prefs.putBoolean(
				BasicPlatformPreferenceConstants.SERVER_AUTH_PASSWD_STORE,
				storePassword);

		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			Logger.logError(e);
		}

		SSDToolPlugin
				.getDefault()
				.firePropertyChange(
						BasicPlatformPreferenceConstants.SERVER_AUTH_PASSWD_STORE_CHANGED,
						oldStorePassword, storePassword);
	}

	// 6) Retrieve preferences from other plugins (Eclipse, UI, Workbench, ...)

	public static boolean isShowTraditionalStyleTabs() {

		boolean result = true;

		IScopeContext scope = InstanceScope.INSTANCE;
		Preferences prefs = scope
				.getNode(BasicPlatformPreferenceConstants.ORG_ECLIPSE_UI_PLUGIN_ID);

		prefs.get(
				BasicPlatformPreferenceConstants.ORG_ECLIPSE_UI_PREF_SHOW_TRADITIONAL_STYLE_TABS,
				"false"); //$NON-NLS-1$
		result = prefs
				.getBoolean(
						BasicPlatformPreferenceConstants.ORG_ECLIPSE_UI_PREF_SHOW_TRADITIONAL_STYLE_TABS,
						true);

		return result;
	}

	public static boolean isOpenOnSingleClick() {

		boolean result = false;

		IScopeContext scope = InstanceScope.INSTANCE;
		Preferences prefs = scope
				.getNode(BasicPlatformPreferenceConstants.ORG_ECLIPSE_UI_WORKBENCH_PLUGIN_ID);

		prefs.get(
				BasicPlatformPreferenceConstants.ORG_ECLIPSE_UI_WORKBENCH_PREF_OPEN_ON_SINGLE_CLICK,
				"false"); //$NON-NLS-1$
		result = prefs
				.getBoolean(
						BasicPlatformPreferenceConstants.ORG_ECLIPSE_UI_WORKBENCH_PREF_OPEN_ON_SINGLE_CLICK,
						false);

		return result;
	}

	public static boolean isSelectOnHover() {

		boolean result = false;

		IScopeContext scope = InstanceScope.INSTANCE;
		Preferences prefs = scope
				.getNode(BasicPlatformPreferenceConstants.ORG_ECLIPSE_UI_WORKBENCH_PLUGIN_ID);

		prefs.get(
				BasicPlatformPreferenceConstants.ORG_ECLIPSE_UI_WORKBENCH_PREF_SELECT_ON_HOVER,
				"false"); //$NON-NLS-1$
		result = prefs
				.getBoolean(
						BasicPlatformPreferenceConstants.ORG_ECLIPSE_UI_WORKBENCH_PREF_SELECT_ON_HOVER,
						false);

		return result;
	}

	public static boolean isOpenAfterDelay() {

		boolean result = false;

		IScopeContext scope = InstanceScope.INSTANCE;
		Preferences prefs = scope
				.getNode(BasicPlatformPreferenceConstants.ORG_ECLIPSE_UI_WORKBENCH_PLUGIN_ID);

		prefs.get(
				BasicPlatformPreferenceConstants.ORG_ECLIPSE_UI_WORKBENCH_PREF_OPEN_AFTER_DELAY,
				"false"); //$NON-NLS-1$
		result = prefs
				.getBoolean(
						BasicPlatformPreferenceConstants.ORG_ECLIPSE_UI_WORKBENCH_PREF_OPEN_AFTER_DELAY,
						false);

		return result;
	}

	// ---------------
	// Extension Point
	// ---------------

	// 1) Plug-Ins

	/**
	 * This method reads receives the {@link TaskModel} and creates it from the
	 * extenison point, since the schema was improved to represent it.<br>
	 * This solution exists since SSDTool Version 0.2.0 and
	 * {@link SSDToolPlugin} version 0.3
	 * 
	 * @param configurationElement
	 *            the configuration element from which the {@link TaskModel} may
	 *            be builded
	 * @return the {@link TaskModel} or <code>null</code>, if there is no such
	 *         extension
	 */
	public static TaskModel getExtensionPointTaskModel(
			IConfigurationElement configurationElement) {

		String configurationElementName = null;

		try {
			configurationElementName = configurationElement.getName();
		} catch (Exception e) {
			Logger.logError("Could not retrieve the name of a SSDTool plugin", //$NON-NLS-1$
					e);
		}

		if (configurationElementName != null
				&& configurationElementName
						.equals(BasicPlatformExtensionPointConstants.SSDTOOL_EP_BUNDLE_DEFINITION_ELEMENT)) {

			String contributor = null;

			try {
				contributor = configurationElement.getContributor().getName();
				if (contributor != null) {
					SSDToolPlugin.getDefault().getContributors()
							.add(contributor);
				}
			} catch (Exception e) {
				Logger.logError(
						"Error on reading contributor of a SSDTool plugin", e); //$NON-NLS-1$
			}

			Task pluginRootTask = null;

			try {

				pluginRootTask = getPluginRootTask(contributor,
						configurationElement);

				return new TaskModel(pluginRootTask);

			} catch (Exception e) {
				Logger.logError("Could not load " //$NON-NLS-1$
						+ (contributor != null ? "" : "a ") + "SSDTool plugin" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						+ (contributor != null ? " " + contributor : ""), e); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		return null;
	}

	/**
	 * This method simply retrieves the one and only root task and builds the
	 * Task model from it.
	 * 
	 * @param contributor
	 *            the contributing plugin
	 * @param pfsBundleDefinitionElement
	 *            the element with the (only) task is it (see
	 *            {@link #SSDTOOL_EP_BUNDLE_DEFINITION_ELEMENT}=
	 *            {@value #SSDTOOL_EP_BUNDLE_DEFINITION_ELEMENT})
	 * @return the contributing plugins {@link Task} model
	 * @throws Exception
	 *             Exceptions are thrown on any error, which is most vital to
	 *             the model (wrong named attributes, wrong content and so on)
	 */
	public static Task getPluginRootTask(String contributor,
			IConfigurationElement pfsBundleDefinitionElement) throws Exception {

		// There need to be exactly one task - the root task
		return getTask(
				contributor,
				pfsBundleDefinitionElement
						.getChildren(BasicPlatformExtensionPointConstants.SSDTOOL_EP_TASK_ELEMENT)[0]);
	}

	/**
	 * This method creates the current task and tries to create all children by
	 * a recursive call as well.
	 * 
	 * @param contributor
	 *            the contributing plugin
	 * @param pfsBundleTaskElement
	 *            the current taskElement
	 * @return the new Task, created by this method
	 * @throws Exception
	 */
	public static Task getTask(String contributor,
			IConfigurationElement pfsBundleTaskElement) throws Exception {

		String id = null;
		String parentTaskID = null;
		String contextToLoadID = null;
		String name = null;
		String description = null;
		boolean enabled = true;
		TaskImplementationType type = null;
		TaskEditorInput input = null;
		TaskSortingType sorting = TaskSortingType.ASCENDING;

		// first round: get basic attributes
		for (String attributeName : pfsBundleTaskElement.getAttributeNames()) {
			if (attributeName
					.equals(BasicPlatformExtensionPointConstants.SSDTOOL_EP_TASK_ID)) {
				id = pfsBundleTaskElement
						.getAttribute(BasicPlatformExtensionPointConstants.SSDTOOL_EP_TASK_ID);
			} else if (attributeName
					.equals(BasicPlatformExtensionPointConstants.SSDTOOL_EP_TASK_PARENT_TASK_ID)) {
				parentTaskID = pfsBundleTaskElement
						.getAttribute(BasicPlatformExtensionPointConstants.SSDTOOL_EP_TASK_PARENT_TASK_ID);
			} else if (attributeName
					.equals(BasicPlatformExtensionPointConstants.SSDTOOL_EP_TASK_TARGET_CLASS_ID)) {
				contextToLoadID = pfsBundleTaskElement
						.getAttribute(BasicPlatformExtensionPointConstants.SSDTOOL_EP_TASK_TARGET_CLASS_ID);
			} else if (attributeName
					.equals(BasicPlatformExtensionPointConstants.SSDTOOL_EP_TASK_NAME)) {
				name = pfsBundleTaskElement
						.getAttribute(BasicPlatformExtensionPointConstants.SSDTOOL_EP_TASK_NAME);
			} else if (attributeName
					.equals(BasicPlatformExtensionPointConstants.SSDTOOL_EP_TASK_DESCRIPTION)) {
				description = pfsBundleTaskElement
						.getAttribute(BasicPlatformExtensionPointConstants.SSDTOOL_EP_TASK_DESCRIPTION);
			} else if (attributeName
					.equals(BasicPlatformExtensionPointConstants.SSDTOOL_EP_TASK_ENABLED)) {
				try {
					enabled = Boolean
							.parseBoolean(pfsBundleTaskElement
									.getAttribute(BasicPlatformExtensionPointConstants.SSDTOOL_EP_TASK_ENABLED));
				} catch (Exception e) {
					Logger.logWarning(e.getMessage());
				}
			} else if (attributeName
					.equals(BasicPlatformExtensionPointConstants.SSDTOOL_EP_TASK_TYPE)) {
				type = TaskImplementationType
						.getTypeByName(pfsBundleTaskElement
								.getAttribute(BasicPlatformExtensionPointConstants.SSDTOOL_EP_TASK_TYPE));
			} else if (attributeName
					.equals(BasicPlatformExtensionPointConstants.SSDTOOL_EP_TASK_TARGET_EDITOR_INPUT)) {
				Object testInput = pfsBundleTaskElement
						.createExecutableExtension(BasicPlatformExtensionPointConstants.SSDTOOL_EP_TASK_TARGET_EDITOR_INPUT);
				if (testInput instanceof TaskEditorInput) {
					input = (TaskEditorInput) testInput;
				}
			} else if (attributeName
					.equals(BasicPlatformExtensionPointConstants.SSDTOOL_EP_TASK_SORT_CHILDREN)) {
				TaskSortingType tmp = TaskSortingType
						.getType(pfsBundleTaskElement
								.getAttribute(BasicPlatformExtensionPointConstants.SSDTOOL_EP_TASK_SORT_CHILDREN));
				if (tmp != null)
					sorting = tmp;
			}
		}

		// then: create the task
		Task task = new Task(contextToLoadID, name, description, enabled, type,
				((input == null) ? null : input.getClass().getName()));

		// set the plugin id of this task
		task.setTaskPluginID(contributor);

		// set the id and the parent task id, if provided
		task.setId(id);
		task.setParentTaskID(parentTaskID);

		// set the sorting
		task.setSortChildren(sorting);

		// second round: get the Image
		for (String attributeName : pfsBundleTaskElement.getAttributeNames()) {
			if (attributeName
					.equals(BasicPlatformExtensionPointConstants.SSDTOOL_EP_TASK_PFSCOREICON_NAME)) {
				try {
					task.setImageString(pfsBundleTaskElement
							.getAttribute(attributeName));
					PFSCoreIcon icon = PFSCoreIcon.getIconTypeByIconPath(
							pfsBundleTaskElement.getAttribute(attributeName),
							enabled);
					if (icon != null) {
						task.setImage(PFSCoreIconProvider
								.getImageByIconType(icon));
					} else {
						Image image = PFSCoreIconProvider.getImageByIconName(
								pfsBundleTaskElement
										.getAttribute(attributeName), enabled);
						if (image != null) {
							task.setImage(image);
						}
					}
				} catch (Exception e) {
					Logger.logError(e);
				}
			}
			if (task.getImage() == null
					&& attributeName
							.equals(BasicPlatformExtensionPointConstants.SSDTOOL_EP_TASK_IMAGE_RESOURCE)) {
				try {
					URL url = new URL(
							BasicPlatformPluginConstants.PLUGIN_BASE_URL_STRING
									+ contributor
									+ "/" //$NON-NLS-1$
									+ pfsBundleTaskElement
											.getAttribute(attributeName));
					if (FileLocator.find(url) != null) {
						Image icon = AbstractUIPlugin
								.imageDescriptorFromPlugin(
										contributor,
										pfsBundleTaskElement
												.getAttribute(attributeName))
								.createImage();
						task.setImage(icon);
					} else {
						throw new IllegalArgumentException(
								"Can't load the image from plugin" //$NON-NLS-1$
										+ contributor
										+ "(" //$NON-NLS-1$
										+ pfsBundleTaskElement
												.getAttribute(attributeName)
										+ ")!"); //$NON-NLS-1$
					}
				} catch (Exception e) {
					Logger.logWarning(e.getMessage());
				}
			}
			if (task.getImage() == null
					&& attributeName
							.equals(BasicPlatformExtensionPointConstants.SSDTOOL_EP_TASK_IMAGE_CLASS)) {
				try {
					Logger.logInfo(pfsBundleTaskElement
							.getAttribute(attributeName)
							+ ": Not implemented yet."); //$NON-NLS-1$
				} catch (Exception e) {
					Logger.logWarning(e.getMessage());
				}
			}

			// additional check: get the help context id
			if (attributeName
					.equals(BasicPlatformExtensionPointConstants.SSDTOOL_EP_TASK_HELP_CONTEXT_ID)) {
				String helpContextId = pfsBundleTaskElement
						.getAttribute(attributeName);
				if (helpContextId != null) {
					task.setHelpContextId(helpContextId);
				}
			}
		}

		// final round: create all children
		for (IConfigurationElement childTaskElement : pfsBundleTaskElement
				.getChildren(BasicPlatformExtensionPointConstants.SSDTOOL_EP_TASK_ELEMENT)) {
			Task childTask = null;
			try {
				childTask = getTask(contributor, childTaskElement);
			} catch (Exception e) {
				Logger.logError(
						"Cannot create a Task of the contributing plugin " //$NON-NLS-1$
								+ contributor, e);
			}
			if (childTask != null) {
				task.addChild(childTask);
			}
		}

		for (IConfigurationElement groupRestrictionElement : pfsBundleTaskElement
				.getChildren(BasicPlatformExtensionPointConstants.SSDTOOL_EP_GROUP_RESTRICTION_ELEMENT)) {
			TaskGroupRestriction groupRestriction = null;
			try {
				String groupName = groupRestrictionElement
						.getAttribute(BasicPlatformExtensionPointConstants.SSDTOOL_EP_GROUP_RESTRICTION_NAME);
				String inverseString = groupRestrictionElement
						.getAttribute(BasicPlatformExtensionPointConstants.SSDTOOL_EP_GROUP_RESTRICTION_INVERSE);
				boolean inverse = false;
				if (inverseString == null || inverseString.isEmpty())
					inverse = false;
				else
					inverse = Boolean.parseBoolean(inverseString);
				groupRestriction = new TaskGroupRestriction(groupName, inverse);
			} catch (Exception e) {
				Logger.logError(
						"Cannot create a TaskGroupRestriction of the contributing plugin " //$NON-NLS-1$
								+ contributor, e);
			}
			if (groupRestriction != null)
				task.addGroupRestrictions(groupRestriction);
		}

		return task;
	}

	/**
	 * This method is used to load a {@link TaskEditorInput} class by its name
	 * using the factory.<br>
	 * That implicates, that the classes need to be unique throughout all
	 * plugins.
	 * 
	 * @param task
	 *            the {@link Task} defines the plug-ins classpath from where the
	 *            {@link TaskEditorInput} should be loaded from
	 * @return an instantiated object of the given class
	 * @throws CoreException
	 */
	public static TaskEditorInput getTaskEditorInputByTask(Task task)
			throws CoreException {

		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(
						BasicPlatformExtensionPointConstants.PLUGIN_EP_ID);

		for (IConfigurationElement e : config) {

			if (task.getTaskPluginID() != null
					&& !task.getTaskPluginID().isEmpty()
					&& !e.getContributor().getName()
							.equals(task.getTaskPluginID())) {
				continue;
			}

			if (e.getName()
					.equals(BasicPlatformExtensionPointConstants.SSDTOOL_EP_BUNDLE_DEFINITION_ELEMENT)) {

				SSDToolPlugin.getDefault().getContributors()
						.add(e.getContributor().getName());

				Object o = e
						.createExecutableExtension(BasicPlatformExtensionPointConstants.SSDTOOL_EP_BUNDLE_DEFINITION_EXEC_FACTORY);

				if (o instanceof ExecutableFactory) {

					try {

						if (((ExecutableFactory) o).canCreateClass(task
								.getTaskEditorInput())) {

							TaskEditorInput input = ((ExecutableFactory) o)
									.createTaskEditorInput(task);

							if (input != null) {
								return input;
							}
						}
					} catch (Exception e1) {
						String message = "Class '" + task.getContextToLoadID() //$NON-NLS-1$
								+ "' not found in bundle '" //$NON-NLS-1$
								+ e.getContributor().getName() + "'"; //$NON-NLS-1$
						Logger.logWarning(message);
					}
				}
			}
		}

		return null;
	}

	/**
	 * This method is used to load a {@link TitleAreaDialog} class by its name
	 * using the factory.<br>
	 * That implicates, that the classes need to be unique throughout all
	 * plugins.
	 * 
	 * @param task
	 *            the {@link Task} defines the plug-ins classpath from where the
	 *            {@link TitleAreaDialog} should be loaded from
	 * @param parentShell
	 *            the shell in which the {@link TitleAreaDialog} should be
	 *            loaded in (to be modal)
	 * @return an instantiated object of the given class
	 * @throws CoreException
	 */
	public static TitleAreaDialog getTitleAreaDialogByTask(Task task,
			Shell parentShell) throws CoreException {

		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(
						BasicPlatformExtensionPointConstants.PLUGIN_EP_ID);

		for (IConfigurationElement e : config) {

			if (task.getTaskPluginID() != null
					&& !task.getTaskPluginID().isEmpty()
					&& !e.getContributor().getName()
							.equals(task.getTaskPluginID())) {
				continue;
			}

			if (e.getName()
					.equals(BasicPlatformExtensionPointConstants.SSDTOOL_EP_BUNDLE_DEFINITION_ELEMENT)) {

				SSDToolPlugin.getDefault().getContributors()
						.add(e.getContributor().getName());

				Object o = e
						.createExecutableExtension(BasicPlatformExtensionPointConstants.SSDTOOL_EP_BUNDLE_DEFINITION_EXEC_FACTORY);

				if (o instanceof ExecutableFactory) {

					try {

						if (((ExecutableFactory) o).canCreateClass(task
								.getContextToLoadID())) {

							TitleAreaDialog dialog = ((ExecutableFactory) o)
									.createTitleAreaDialog(task, parentShell);

							if (dialog != null) {
								return dialog;
							}
						}
					} catch (Exception e1) {
						String message = "Class '" + task.getContextToLoadID() //$NON-NLS-1$
								+ "' not found in bundle '" //$NON-NLS-1$
								+ e.getContributor().getName() + "'"; //$NON-NLS-1$
						Logger.logWarning(message);
					}
				}
			}
		}

		return null;
	}

	/**
	 * This method is used to load a {@link Wizard} class by its name using the
	 * factory.<br>
	 * That implicates, that the classes need to be unique throughout all
	 * plugins.
	 * 
	 * @param task
	 *            the {@link Task} defines the plug-ins classpath from where the
	 *            {@link Wizard} should be loaded from
	 * @return an instantiated object of the given class
	 * @throws CoreException
	 */
	public static Wizard getWizardByTask(Task task) throws CoreException {

		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(
						BasicPlatformExtensionPointConstants.PLUGIN_EP_ID);

		for (IConfigurationElement e : config) {

			if (task.getTaskPluginID() != null
					&& !task.getTaskPluginID().isEmpty()
					&& !e.getContributor().getName()
							.equals(task.getTaskPluginID())) {
				continue;
			}

			if (e.getName()
					.equals(BasicPlatformExtensionPointConstants.SSDTOOL_EP_BUNDLE_DEFINITION_ELEMENT)) {

				SSDToolPlugin.getDefault().getContributors()
						.add(e.getContributor().getName());

				Object o = e
						.createExecutableExtension(BasicPlatformExtensionPointConstants.SSDTOOL_EP_BUNDLE_DEFINITION_EXEC_FACTORY);

				if (o instanceof ExecutableFactory) {

					try {

						if (((ExecutableFactory) o).canCreateClass(task
								.getContextToLoadID())) {

							Wizard wizard = ((ExecutableFactory) o)
									.createWizard(task);

							if (wizard != null) {
								return wizard;
							}
						}
					} catch (Exception e1) {
						String message = "Class '" + task.getContextToLoadID() //$NON-NLS-1$
								+ "' not found in bundle '" //$NON-NLS-1$
								+ e.getContributor().getName() + "'"; //$NON-NLS-1$
						Logger.logWarning(message);
					}
				}
			}
		}

		return null;
	}

	/**
	 * This method is used to load a {@link TitleAreaDialog} class by its name
	 * using the factory.<br>
	 * That implicates, that the classes need to be unique throughout all
	 * plugins.
	 * 
	 * @param task
	 *            the {@link Task} defines the plug-ins classpath from where the
	 *            {@link TitleAreaDialog} should be loaded from
	 * @param parentShell
	 *            the shell in which the {@link TitleAreaDialog} should be
	 *            loaded in (to be modal)
	 * @return an instantiated object of the given class
	 * @throws CoreException
	 */
	public static AbstractHandler getHandlerByTask(Task task)
			throws CoreException {

		IConfigurationElement[] config = Platform.getExtensionRegistry()
				.getConfigurationElementsFor(
						BasicPlatformExtensionPointConstants.PLUGIN_EP_ID);

		for (IConfigurationElement e : config) {

			if (task.getTaskPluginID() != null
					&& !task.getTaskPluginID().isEmpty()
					&& !e.getContributor().getName()
							.equals(task.getTaskPluginID())) {
				continue;
			}

			if (e.getName()
					.equals(BasicPlatformExtensionPointConstants.SSDTOOL_EP_BUNDLE_DEFINITION_ELEMENT)) {

				SSDToolPlugin.getDefault().getContributors()
						.add(e.getContributor().getName());

				Object o = e
						.createExecutableExtension(BasicPlatformExtensionPointConstants.SSDTOOL_EP_BUNDLE_DEFINITION_EXEC_FACTORY);

				if (o instanceof ExecutableFactory) {

					try {

						if (((ExecutableFactory) o).canCreateClass(task
								.getContextToLoadID())) {

							AbstractHandler handler = ((ExecutableFactory) o)
									.createHandler(task);

							if (handler != null) {
								return handler;
							}
						}
					} catch (Exception e1) {
						String message = "Class '" + task.getContextToLoadID() //$NON-NLS-1$
								+ "' not found in bundle '" //$NON-NLS-1$
								+ e.getContributor().getName() + "'"; //$NON-NLS-1$
						Logger.logWarning(message);
					}
				}
			}
		}

		return null;
	}

	// 2) Server List

	/**
	 * This method is used to retrieve the internal path to the truststore
	 * (internal means, inside the providing plugin!).
	 * 
	 * @param configurationElement
	 * @since 0.6
	 */
	public static TruststoreList getTruststorePath(
			IConfigurationElement configurationElement) {

		TruststoreList truststores = null;

		String configurationElementName = null;

		try {
			configurationElementName = configurationElement.getName();
		} catch (Exception e) {
			Logger.logError(
					"Could not retrieve the name of a PCP Server List plugin", //$NON-NLS-1$
					e);
		}

		if (configurationElementName != null
				&& configurationElementName
						.equals(BasicPlatformExtensionPointConstants.SSDTOOL_SERVER_EP_TRUSTORE_ELEMENT)) {

			String contributor = null;

			try {
				contributor = configurationElement.getContributor().getName();
				if (contributor != null) {
					SSDToolPlugin.getDefault().getContributors()
							.add(contributor);
				}
			} catch (Exception e) {
				Logger.logError(
						"Error on reading contributor of a PCP Server List plugin", //$NON-NLS-1$
						e);
			}

			try {

				truststores = new TruststoreList();
				truststores
						.addTrustStore(
								contributor,
								configurationElement
										.getAttribute(BasicPlatformExtensionPointConstants.SSDTOOL_SERVER_EP_PATH_ATTRIBUTE),
								configurationElement
										.getAttribute(BasicPlatformExtensionPointConstants.SSDTOOL_SERVER_EP_OPTIONAL_PASSWORD_ATTRIBUTE));

			} catch (InvalidRegistryObjectException e) {
				Logger.logError("Could not load " //$NON-NLS-1$
						+ (contributor != null ? "" : "a ") //$NON-NLS-1$ //$NON-NLS-2$
						+ "PCP Server List plugin (TRUSTSTORE)" //$NON-NLS-1$
						+ (contributor != null ? " " + contributor : ""), e); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		return truststores;
	}

	/**
	 * This method reads extension point for {@link ServerList}s creates them.<br>
	 * This solution exists since PCP version 0.3.0 and {@link SSDToolPlugin}
	 * version 0.6
	 * 
	 * @param configurationElement
	 *            the configuration element from which the {@link ServerList}
	 *            may be builded
	 * @return the {@link ServerList} or <code>null</code>, if there is no such
	 *         extension
	 */
	public static ServerList getExtensionPointServerList(
			IConfigurationElement configurationElement) {

		ServerList result = null;

		String configurationElementName = null;

		try {
			configurationElementName = configurationElement.getName();
		} catch (Exception e) {
			Logger.logError(
					"Could not retrieve the name of a PCP Server List plugin", //$NON-NLS-1$
					e);
		}

		if (configurationElementName != null
				&& configurationElementName
						.equals(BasicPlatformExtensionPointConstants.SSDTOOL_SERVER_EP_PCP_SERVER_ELEMENT)) {

			String contributor = null;

			try {
				contributor = configurationElement.getContributor().getName();
				if (contributor != null) {
					SSDToolPlugin.getDefault().getContributors()
							.add(contributor);
				}
			} catch (Exception e) {
				Logger.logError(
						"Error on reading contributor of a PCP Server List plugin", //$NON-NLS-1$
						e);
			}

			ServerList list = new ServerList();

			try {

				String name = configurationElement
						.getAttribute(BasicPlatformExtensionPointConstants.SSDTOOL_SERVER_EP_NAME_ATTRIBUTE);
				String urlString = configurationElement
						.getAttribute(BasicPlatformExtensionPointConstants.SSDTOOL_SERVER_EP_URL_ATTRIBUTE);

				list.addServer(name, urlString);

				return list;

			} catch (InvalidRegistryObjectException e) {
				Logger.logError("Could not load " //$NON-NLS-1$
						+ (contributor != null ? "" : "a ") //$NON-NLS-1$ //$NON-NLS-2$
						+ "PCP Server List plugin" //$NON-NLS-1$
						+ (contributor != null ? " " + contributor : ""), e); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}

		return result;
	}

	// ---------------
	// other utilities
	// ---------------

	/**
	 * returns all open editors, identified by their unique ID
	 * 
	 * @return the set of ids of open editors
	 */
	public static Set<String> getOpenEditorIDs() {

		Set<String> results = new HashSet<String>();

		for (IEditorReference ref : PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage()
				.getEditorReferences()) {
			results.add(ref.getId());
		}

		return results;
	}

	/**
	 * Used to close an editor, if the check of {@link #getOpenEditorIDs()}
	 * tells, there is such an open editor (otherwise it returns
	 * <code>true</code>(to indicate that the method was successful after all)).
	 * If there is such an editor, the platform tries to close it and returns,
	 * whether or not it was successful.
	 * 
	 * @param editorId
	 *            the id of the editor to close
	 * @param input
	 *            if the input is <code>null</code>, the editor will be checked
	 *            by the ID only: if it is not <code>null</code> and if it
	 *            matches an editor with the same input, the editor will not(!)
	 *            be closed.
	 * @return <code>true</code>, if the editor was present and could be closed,
	 *         otherwise <code>false</code>
	 */
	public static boolean closeEditor(String editorId, IEditorInput input) {
		if (getOpenEditorIDs().contains(editorId)) {
			for (IEditorReference ref : PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage()
					.getEditorReferences()) {
				if (ref.getId().equals(editorId)) {
					IEditorInput refInput = null;
					try {
						refInput = ref.getEditorInput();
					} catch (PartInitException e) {
					}
					if (input != null && refInput != null
							&& refInput.equals(input)) {
						continue;
					}
					if (ref.isDirty()) {
						return ref.getPage().closeEditors(
								new IEditorReference[] { ref }, true);
					} else {
						return ref.getPage().closeEditors(
								new IEditorReference[] { ref }, false);
					}
				}
			}
		}

		return true;
	}

	/**
	 * Tries to open the editor with the specified id, passing the given input
	 * (an {@link IEditorInput} implementation) down to it.
	 * 
	 * @param input
	 *            the input to load (might be <code>null</code>)
	 * @param editorId
	 *            the id of the editor to open
	 * @return the {@link IEditorPart} object, if the platform could open it
	 * @throws PartInitException
	 */
	public static IEditorPart openEditor(IEditorInput input, String editorId)
			throws PartInitException {

		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();

		return page.openEditor(input, editorId);
	}

	/**
	 * returns all open editors, identified by their unique ID<br>
	 * excludes are the current navigators
	 * 
	 * @return the set of ids of open views (excluding the navigators)
	 */
	public static Set<String> getOpenViewIDs() {

		Set<String> results = new HashSet<String>();

		for (IViewReference ref : PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getViewReferences()) {

			if (FormsPluginNavigatorView.ID.equals(ref.getId()))
				results.add(ref.getId());
		}

		return results;
	}

	/*
	 * EXTENSION POINT
	 */

	//
	// resource
	//

	/**
	 * returns all {@link URLClassLoader}s for resource extensions
	 */
	public static List<URLClassLoader> getResourceExtensions() {

		List<URLClassLoader> urlClassLoaders = new ArrayList<URLClassLoader>();

		IConfigurationElement[] config = Platform
				.getExtensionRegistry()
				.getConfigurationElementsFor(
						BasicPlatformExtensionPointConstants.PFS_RCP_CORE_EXTENSION_POINT);
		if (config == null || config.length == 0) {
			Logger.logWarning("Could not load other PCP plugins - "
					+ "unable to retrieve the plugins or no plugins available.");
		} else {

			for (IConfigurationElement element : config) {
				if (BasicPlatformExtensionPointConstants.PFS_RCP_CORE_EP_ELEMENT_RESOURCE
						.equals(element.getName())
						&& element
								.getAttribute(BasicPlatformExtensionPointConstants.PFS_RCP_CORE_EP_ATTRIBUTE_RESOURCE_PROVIDER) != null) {
					String resourceProviderImpl = element
							.getAttribute(BasicPlatformExtensionPointConstants.PFS_RCP_CORE_EP_ATTRIBUTE_RESOURCE_PROVIDER);
					PFSCoreResourceProvider provider = null;
					if (resourceProviderImpl != null) {
						try {
							provider = (PFSCoreResourceProvider) element
									.createExecutableExtension(BasicPlatformExtensionPointConstants.PFS_RCP_CORE_EP_ATTRIBUTE_RESOURCE_PROVIDER);
							for (IConfigurationElement resourceEntry : element
									.getChildren()) {
								if (resourceEntry
										.getAttribute(BasicPlatformExtensionPointConstants.PFS_RCP_CORE_EP_ATTRIBUTE_RESOURCE_PATH) != null) {
									String resource = resourceEntry
											.getAttribute(BasicPlatformExtensionPointConstants.PFS_RCP_CORE_EP_ATTRIBUTE_RESOURCE_PATH);
									try {
										URLClassLoader cl = provider
												.getResource(resource);
										if (cl != null) {
											urlClassLoaders.add(cl);
										}
									} catch (Exception e) {
										Logger.logError(e);
									}
								}
							}
						} catch (CoreException e) {
							System.err.println(e.getMessage());
							Logger.logError(e);
						}
					}
				}
			}
		}

		if (!urlClassLoaders.isEmpty()) {
			return urlClassLoaders;
		} else {
			return null;
		}
	}

	//
	// helper search
	//

	public static SearchModel getSearchModel() {

		SearchModel model = null;

		IConfigurationElement[] config = Platform
				.getExtensionRegistry()
				.getConfigurationElementsFor(
						BasicPlatformExtensionPointConstants.PFS_RCP_CORE_EP_HELPER_SEARCH);
		if (config == null || config.length == 0) {
			Logger.logWarning("Could not load other PCP plugins - "
					+ "unable to retrieve the plugins or no plugins available.");
		} else {

			model = new SearchModel();

			for (IConfigurationElement element : config) {

				String pluginId = element.getContributor().getName();

				SearchData data = null;

				String targetId = null;
				String message = null;
				SearchHelperFactory factory = null;

				if (BasicPlatformExtensionPointConstants.PFS_RCP_CORE_EP_HELPER_SEARCH_ROOT
						.equals(element.getName())
						&& element
								.getAttribute(BasicPlatformExtensionPointConstants.PFS_RCP_CORE_EP_HELPER_SEARCH_ROOT_ATTR_TID) != null
						&& element
								.getAttribute(BasicPlatformExtensionPointConstants.PFS_RCP_CORE_EP_HELPER_SEARCH_ROOT_ATTR_MT) != null
						&& element
								.getAttribute(BasicPlatformExtensionPointConstants.PFS_RCP_CORE_EP_HELPER_SEARCH_ROOT_ATTR_TF) != null) {

					targetId = element
							.getAttribute(BasicPlatformExtensionPointConstants.PFS_RCP_CORE_EP_HELPER_SEARCH_ROOT_ATTR_TID);
					message = element
							.getAttribute(BasicPlatformExtensionPointConstants.PFS_RCP_CORE_EP_HELPER_SEARCH_ROOT_ATTR_MT);
					try {
						factory = (SearchHelperFactory) element
								.createExecutableExtension(BasicPlatformExtensionPointConstants.PFS_RCP_CORE_EP_HELPER_SEARCH_ROOT_ATTR_TF);
					} catch (CoreException e) {
						e.printStackTrace();
						Logger.logError(e);
						continue;
					}

					if (targetId != null && message != null && factory != null) {
						data = new SearchData(targetId, message, factory);
					}

					if (data != null) {
						if (element
								.getAttribute(BasicPlatformExtensionPointConstants.PFS_RCP_CORE_EP_HELPER_SEARCH_ROOT_ATTR_HICON) != null) {
							data.setHintSearch(Boolean.parseBoolean(element
									.getAttribute(BasicPlatformExtensionPointConstants.PFS_RCP_CORE_EP_HELPER_SEARCH_ROOT_ATTR_HICON)));
						}
						if (element
								.getAttribute(BasicPlatformExtensionPointConstants.PFS_RCP_CORE_EP_HELPER_SEARCH_ROOT_ATTR_HCANCEL) != null) {
							data.setHintCancel(Boolean.parseBoolean(element
									.getAttribute(BasicPlatformExtensionPointConstants.PFS_RCP_CORE_EP_HELPER_SEARCH_ROOT_ATTR_HCANCEL)));
						}

						// there have to be exactly on "options" element!
						for (IConfigurationElement option : element
								.getChildren(BasicPlatformExtensionPointConstants.PFS_RCP_CORE_EP_HELPER_SEARCH_ELEMENT_OPTIONS)[0]
								.getChildren(BasicPlatformExtensionPointConstants.PFS_RCP_CORE_EP_HELPER_SEARCH_ELEMENT_OPTION)) {

							SearchOption searchOption = null;

							if (option
									.getAttribute(BasicPlatformExtensionPointConstants.PFS_RCP_CORE_EP_HELPER_SEARCH_OPTION_ATTR_NAME) != null
									&& option
											.getAttribute(BasicPlatformExtensionPointConstants.PFS_RCP_CORE_EP_HELPER_SEARCH_OPTION_ATTR_LABEL) != null) {
								searchOption = new SearchOption(
										option.getAttribute(BasicPlatformExtensionPointConstants.PFS_RCP_CORE_EP_HELPER_SEARCH_OPTION_ATTR_NAME),
										option.getAttribute(BasicPlatformExtensionPointConstants.PFS_RCP_CORE_EP_HELPER_SEARCH_OPTION_ATTR_LABEL));
								data.getOptions().add(searchOption);
							}
						}

						model.getTargetSearch().put(targetId, data);
						model.getPluginSearch().put(pluginId, data);
					}
				}
			}
		}

		return model;
	}

	public static IAvailablePluginService getAvailablePluginService() {
		Bundle bundle = Platform.getBundle(SSDToolPlugin.PLUGIN_ID);
		ServiceReference<?> reference = bundle.getBundleContext()
				.getServiceReference(IAvailablePluginService.class.getName());
		IAvailablePluginService service = (IAvailablePluginService) bundle
				.getBundleContext().getService(reference);
		return service;
	}

	public static IGroupService getGroupService() {
		Bundle bundle = Platform.getBundle(SSDToolPlugin.PLUGIN_ID);
		ServiceReference<?> reference = bundle.getBundleContext()
				.getServiceReference(IGroupService.class.getName());
		IGroupService service = (IGroupService) bundle.getBundleContext()
				.getService(reference);
		return service;
	}
}
