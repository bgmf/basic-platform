package de.dzimmermann.rcp.basicplatform.util;

import java.net.URL;

import org.osgi.framework.Bundle;

import de.dzimmermann.rcp.basicplatform.ui.dialog.OpenWorkpaceDialog;
import de.dzimmermann.rcp.basicplatform.ui.dialog.SelectRemoteServerDialog;
import de.dzimmermann.rcp.basicplatform.ui.dialog.SelectTrustAndKeystorePathDialog;
import de.dzimmermann.rcp.basicplatform.ui.dialog.UserAuthenticationDialog;
import de.dzimmermann.rcp.basicplatform.ui.view.FormsPluginNavigatorView;

/**
 * This abstract class contains all platform plugin specific constants.
 * 
 * @author dzimmermann
 * @since 0.2.0
 * @version 0.3
 */
public abstract class BasicPlatformPluginConstants {

	/**
	 * the default console name for the PCP
	 */
	public static final String CONSOLE_NAME = Messages
			.getString("SSDToolPluginConstants.console.name"); //$NON-NLS-1$

	/**
	 * the id of the navigator view
	 */
	public static final String NAVIGATOR_ID = FormsPluginNavigatorView.ID;

	/*
	 * the key in the dialog settings used to retieve the last opened dir
	 */

	/**
	 * the id of the last opened directory key for the dialog settings within
	 * the PCP plugin properties
	 */
	public static final String OPEN_SAVE_LAST_DIR_KEY = "basic-platform.open-save.last-dir"; //$NON-NLS-1$

	/*
	 * toolbar ids
	 */

	/**
	 * this is the key of the default PCP content within the toolbar
	 */
	public static final String TOOLBAR_DEFAULT = "basic-platform.toolbar.default"; //$NON-NLS-1$

	/**
	 * this is the key of the default PCP content within the toolbar
	 */
	public static final String TOOLBAR_CONTENT = "basic-platform.toolbar.content"; //$NON-NLS-1$

	/**
	 * this is the PCP addtions group within the toolbar, add your plugins
	 * content here - use seperators, if needed
	 */
	public static final String TOOLBAR_ADDITIONS = "basic-platform.toolbar.additions"; //$NON-NLS-1$

	/*
	 * different stuff
	 */

	/**
	 * This is a very basic {@link URL} prefix for the OSGi-{@link Bundle}
	 * reference.
	 */
	public static final String PLUGIN_BASE_URL_STRING = "platform:/plugin/"; //$NON-NLS-1$

	/*
	 * DIALOG SETTINGS SECTION NAMES
	 */

	/**
	 * the section descriptor for the dialog settings of the PCP
	 */
	public static final String DIALOG_SETTINGS_SECTION = "BasicPlatformDialogSettings"; //$NON-NLS-1$

	/**
	 * constant for the select open workspace dialog settings
	 */
	public static final String DIALOG_SETTINGS_SECTION_OPENWS = OpenWorkpaceDialog.class
			.getName();

	/**
	 * constant for the select remote server dialog settings
	 */
	public static final String DIALOG_SETTINGS_SECTION_SELECTRS = SelectRemoteServerDialog.class
			.getName();

	/**
	 * constant for the select select trust- ans keystore path dialog settings
	 */
	public static final String DIALOG_SETTINGS_SECTION_TKSTOREPATH = SelectTrustAndKeystorePathDialog.class
			.getName();

	/**
	 * constant for the dialog to enter a users authentication date (user name
	 * and password)
	 */
	public static final String DIALOG_SETTINGS_SECTION_USERAUTH = UserAuthenticationDialog.class
			.getName();
}
