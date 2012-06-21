package de.dzimmermann.rcp.basicplatform.util;

/**
 * This abstract class holds all constants for platforms specific preferences. <br>
 * <br>
 * versions:
 * <ul>
 * <li>0.1<br>
 * - initial version, created workspace selection preferences</li>
 * </li>0.2<br>
 * - introduced remote servers</li>
 * <li>0.3<br>
 * - added the developer mode</li>
 * <li>0.4<br>
 * - added trust- and keystore preferences</li>
 * <li>0.5<br>
 * - added user and password preferences for the remote server</li>
 * </ul>
 * 
 * @author dzimmermann
 * @since 0.2.0
 * @version 0.5
 */
public abstract class BasicPlatformPreferenceConstants {

	// /////////////////// //
	// workspace selection //
	// /////////////////// //

	/**
	 * The default delimeter for the workspaces.<br>
	 * My intention is not to allow changes...
	 */
	public static final String RECENT_WORKSPACES_DELIMETER = ";"; //$NON-NLS-1$

	/**
	 * the preference name for the max amount of recent workspaces
	 */
	public static final String RECENT_WORKSPACES_MAX_AMOUNT = "RECENT_WORKSPACES_MAX_AMOUNT"; //$NON-NLS-1$
	/**
	 * property name to be used if the max amount of recent workspaces changed
	 */
	public static final String RECENT_WORKSPACES_MAX_AMOUNT_CHANGED = RECENT_WORKSPACES_MAX_AMOUNT
			+ "-changed"; //$NON-NLS-1$
	/**
	 * default value for the max amount of recent workspaces, this will be set
	 * as the initial value, when the application is started for the first time
	 */
	public static final int RECENT_WORKSPACES_MAX_AMOUNT_DEFAULT = 5;

	/**
	 * preference name for the latest workspaces
	 */
	public static final String RECENT_WORKSPACES = "RECENT_WORKSPACES"; //$NON-NLS-1$
	/**
	 * property name to be used if the value the latest workspaces changed
	 */
	public static final String RECENT_WORKSPACES_CHANGED = RECENT_WORKSPACES
			+ "-changed"; //$NON-NLS-1$

	/**
	 * preference name indicating whether or not to show the workspace selection
	 * dialog
	 */
	public static final String RECENT_WORKSPACES_SHOW = "RECENT_WORKSPACES_SHOW"; //$NON-NLS-1$
	/**
	 * property name to be used if the vaue of show or hide the workspace
	 * selection dialog has been changed
	 */
	public static final String RECENT_WORKSPACES_SHOW_CHANGED = RECENT_WORKSPACES_SHOW
			+ "-changed"; //$NON-NLS-1$

	// /////////////////////// //
	// remote server selection //
	// /////////////////////// //

	/**
	 * preference name for the latest remote server
	 */
	public static final String RECENT_REMOTE_SERVER_NAME = "RECENT_REMOTE_SERVER_NAME"; //$NON-NLS-1$
	/**
	 * property name to be used if the latest remote server has been changed
	 */
	public static final String RECENT_REMOTE_SERVER_NAME_CHANGED = RECENT_REMOTE_SERVER_NAME
			+ "-changed"; //$NON-NLS-1$
	/**
	 * preference to allow the selection of an alternative remote server host
	 */
	public static final String RECENT_REMOTE_SERVER_USE_ALTERNATIVE = "RECENT_REMOTE_SERVER_USE_ALTERNATIVE"; //$NON-NLS-1$
	/**
	 * property name to be used if the state of the alternative remote server
	 * host usage has been changed
	 */
	public static final String RECENT_REMOTE_SERVER_USE_ALTERNATIVE_CHANGED = RECENT_REMOTE_SERVER_USE_ALTERNATIVE
			+ "-changed"; //$NON-NLS-1$
	/**
	 * preference to specify the host of an alternative remote server
	 */
	public static final String RECENT_REMOTE_SERVER_ALTERNATIVE = "RECENT_REMOTE_SERVER_ALTERNATIVE"; //$NON-NLS-1$
	/**
	 * property name to be used if the alternative remote server host changed
	 */
	public static final String RECENT_REMOTE_SERVER_ALTERNATIVE_CHANGED = RECENT_REMOTE_SERVER_ALTERNATIVE
			+ "-changed"; //$NON-NLS-1$

	// ////////////// //
	// developer mode //
	// ////////////// //

	/**
	 * preference name for the developer mode
	 */
	public static final String DEVELOPER_MODE = "DEVELOPER_MODE"; //$NON-NLS-1$
	/**
	 * property name to be used if the developer mode has been changed
	 */
	public static final String DEVELOPER_MODE_CHANGED = DEVELOPER_MODE
			+ "-changed"; //$NON-NLS-1$

	// //////////////////// //
	// trust- and key-store //
	// //////////////////// //

	/**
	 * preference name for the location of the truststore
	 */
	public static final String TRUSTSTORE_LOACTION = "TRUSTSTORE_LOACTION"; //$NON-NLS-1$
	/**
	 * property name to be used if the location of the truststore has been
	 * changed
	 */
	public static final String TRUSTSTORE_LOACTION_CHANGED = TRUSTSTORE_LOACTION
			+ "-changed"; //$NON-NLS-1$

	/**
	 * preference name for the password of the truststore
	 */
	public static final String TRUSTSTORE_LOACTION_PASSWORD = TRUSTSTORE_LOACTION
			+ "_PASSWORD"; //$NON-NLS-1$
	/**
	 * property name to be used if the password of the truststore has been
	 * changed
	 */
	public static final String TRUSTSTORE_LOACTION_PASSWORD_CHANGED = TRUSTSTORE_LOACTION_PASSWORD
			+ "-changed"; //$NON-NLS-1$

	/**
	 * preference name for the location of the keystore
	 */
	public static final String KEYSTORE_LOACTION = "KEYSTORE_LOACTION"; //$NON-NLS-1$
	/**
	 * property name to be used if the location of the keystore has been changed
	 */
	public static final String KEYSTORE_LOACTION_CHANGED = KEYSTORE_LOACTION
			+ "-changed"; //$NON-NLS-1$

	/**
	 * preference name for the password of the keystore
	 */
	public static final String KEYSTORE_LOACTION_PASSWORD = "KEYSTORE_LOACTION_PASSWORD"; //$NON-NLS-1$
	/**
	 * property name to be used if the password of the keystore has been changed
	 */
	public static final String KEYSTORE_LOACTION_PASSWORD_CHANGED = KEYSTORE_LOACTION_PASSWORD
			+ "-changed"; //$NON-NLS-1$

	// ///////////////// //
	// user and password //
	// ///////////////// //

	/**
	 * preference name for the user
	 */
	public static final String SERVER_AUTH_USER = "SERVER_AUTH_USER"; //$NON-NLS-1$
	/**
	 * property name to be used if the user changed
	 */
	public static final String SERVER_AUTH_USER_CHANGED = SERVER_AUTH_USER
			+ "-changed"; //$NON-NLS-1$

	/**
	 * preference name for the password
	 */
	public static final String SERVER_AUTH_PASSWD = "SERVER_AUTH_PASSWD"; //$NON-NLS-1$
	/**
	 * property name to be used if the password changed
	 */
	public static final String SERVER_AUTH_PASSWD_CHANGED = SERVER_AUTH_PASSWD
			+ "-changed"; //$NON-NLS-1$

	/**
	 * preference name used to specify whether the password will be stored or not
	 */
	public static final String SERVER_AUTH_PASSWD_STORE = "SERVER_AUTH_PASSWD_STORE"; //$NON-NLS-1$
	/**
	 * property name to be used if the password storage setting changed
	 */
	public static final String SERVER_AUTH_PASSWD_STORE_CHANGED = SERVER_AUTH_PASSWD_STORE
			+ "-changed"; //$NON-NLS-1$

	// //////////////////////////////////////////////////////////// //
	// Preferences from other plugins (Eclipse, UI, Workbench, ...) //
	// //////////////////////////////////////////////////////////// //

	public static final String ORG_ECLIPSE_UI_PLUGIN_ID = "org.eclipse.ui"; //$NON-NLS-1$
	public static final String ORG_ECLIPSE_UI_PREF_SHOW_TRADITIONAL_STYLE_TABS = "SHOW_TRADITIONAL_STYLE_TABS"; //$NON-NLS-1$

	public static final String ORG_ECLIPSE_UI_WORKBENCH_PLUGIN_ID = "org.eclipse.ui.workbench"; //$NON-NLS-1$
	public static final String ORG_ECLIPSE_UI_WORKBENCH_PREF_SELECT_ON_HOVER = "SELECT_ON_HOVER"; //$NON-NLS-1$
	public static final String ORG_ECLIPSE_UI_WORKBENCH_PREF_OPEN_ON_SINGLE_CLICK = "OPEN_ON_SINGLE_CLICK"; //$NON-NLS-1$
	public static final String ORG_ECLIPSE_UI_WORKBENCH_PREF_OPEN_AFTER_DELAY = "OPEN_AFTER_DELAY"; //$NON-NLS-1$
}
