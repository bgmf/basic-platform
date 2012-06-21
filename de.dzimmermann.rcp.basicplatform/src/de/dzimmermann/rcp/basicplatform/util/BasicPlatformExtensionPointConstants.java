package de.dzimmermann.rcp.basicplatform.util;

import de.dzimmermann.rcp.basicplatform.model.TaskModel;

/**
 * This abstract class contains all extension point related constants.<br>
 * <br>
 * versions:
 * <ul>
 * <li>&lt;1.0<br>
 * - old plugin model using references to {@link TaskModel} implementation per
 * ech Plug-In</li>
 * </li>1.0<br>
 * - initial version for the PCP (v0.1.0), added updated plugin model using a
 * extension point description of each plugin<br>
 * - introduced forms for adding plugins to the welcome page</li>
 * <li>1.1<br>
 * - added remote server model</li>
 * <li>1.2<br>
 * - added trustsore to remote server model</li>
 * <li>1.3<br>
 * added new constants (extended the task model and the extension point a
 * little)</li>
 * </ul>
 * 
 * @author dzimmermann
 * @since 0.1.0
 * @version 1.3
 */
public abstract class BasicPlatformExtensionPointConstants {

	// //////////////////////////////// //
	// plugin extenstion point defaults //
	// //////////////////////////////// //

	/**
	 * the plugins extension point id
	 */
	public static final String PLUGIN_EP_ID = "de.dzimmermann.rcp.basicplatform.plugin"; //$NON-NLS-1$

	// new way to describe the task model
	public static final String SSDTOOL_EP_BUNDLE_DEFINITION_ELEMENT = "bundle-definition"; //$NON-NLS-1$
	public static final String SSDTOOL_EP_BUNDLE_DEFINITION_EXEC_FACTORY = "exec-factory"; //$NON-NLS-1$

	public static final String SSDTOOL_EP_TASK_ELEMENT = "bundle-task"; //$NON-NLS-1$
	public static final String SSDTOOL_EP_TASK_ID = "id"; //$NON-NLS-1$
	public static final String SSDTOOL_EP_TASK_PARENT_TASK_ID = "parent-task-id"; //$NON-NLS-1$
	public static final String SSDTOOL_EP_TASK_TARGET_CLASS_ID = "target-class-id"; //$NON-NLS-1$
	public static final String SSDTOOL_EP_TASK_TARGET_EDITOR_INPUT = "target-editor-input"; //$NON-NLS-1$
	public static final String SSDTOOL_EP_TASK_NAME = "name"; //$NON-NLS-1$
	public static final String SSDTOOL_EP_TASK_TYPE = "type"; //$NON-NLS-1$
	public static final String SSDTOOL_EP_TASK_DESCRIPTION = "description"; //$NON-NLS-1$
	public static final String SSDTOOL_EP_TASK_ENABLED = "enabled"; //$NON-NLS-1$
	public static final String SSDTOOL_EP_TASK_PFSCOREICON_NAME = "internal-icon-name"; //$NON-NLS-1$
	public static final String SSDTOOL_EP_TASK_IMAGE_RESOURCE = "image-resource"; //$NON-NLS-1$
	public static final String SSDTOOL_EP_TASK_IMAGE_CLASS = "image-class"; //$NON-NLS-1$
	public static final String SSDTOOL_EP_TASK_HELP_CONTEXT_ID = "help-context-id"; //$NON-NLS-1$
	public static final String SSDTOOL_EP_TASK_SORT_CHILDREN = "sort-children"; //$NON-NLS-1$

	public static final String SSDTOOL_EP_GROUP_RESTRICTION_ELEMENT = "group-restriction"; //$NON-NLS-1$
	public static final String SSDTOOL_EP_GROUP_RESTRICTION_NAME = "group-name"; //$NON-NLS-1$
	public static final String SSDTOOL_EP_GROUP_RESTRICTION_INVERSE = "inverse"; //$NON-NLS-1$

	// //////////////////////////////////// //
	// server list extension point defaults //
	// //////////////////////////////////// //

	public static final String SSDTOOL_SERVER_EP_ID = "de.dzimmermann.rcp.basicplatform.serverlist"; //$NON-NLS-1$

	public static final String SSDTOOL_SERVER_EP_TRUSTORE_ELEMENT = "truststore"; //$NON-NLS-1$
	public static final String SSDTOOL_SERVER_EP_PATH_ATTRIBUTE = "path"; //$NON-NLS-1$
	public static final String SSDTOOL_SERVER_EP_OPTIONAL_PASSWORD_ATTRIBUTE = "password"; //$NON-NLS-1$
	// a little bit out of the line, but this is the path within the instance
	// location a.k.a. the workspace, in which the truststore will be put in
	public static final String SSDTOOL_SERVER_TRUSTORE_INSTANCELOC_PATH = ".truststore"; //$NON-NLS-1$

	public static final String SSDTOOL_SERVER_EP_PCP_SERVER_ELEMENT = "server"; //$NON-NLS-1$
	public static final String SSDTOOL_SERVER_EP_NAME_ATTRIBUTE = "name"; //$NON-NLS-1$
	public static final String SSDTOOL_SERVER_EP_URL_ATTRIBUTE = "url"; //$NON-NLS-1$

	// ////////////////// //
	// resource extension //
	// ////////////////// //

	public static final String PFS_RCP_CORE_EXTENSION_POINT = "de.dzimmermann.rcp.basicplatform.resource"; //$NON-NLS-1$

	public static final String PFS_RCP_CORE_EP_ELEMENT_RESOURCE = "resource"; //$NON-NLS-1$
	public static final String PFS_RCP_CORE_EP_ELEMENT_RESOURCE_ENTRY = "resource-entry"; //$NON-NLS-1$
	public static final String PFS_RCP_CORE_EP_ATTRIBUTE_RESOURCE_PROVIDER = "resource-provider"; //$NON-NLS-1$
	public static final String PFS_RCP_CORE_EP_ATTRIBUTE_RESOURCE_PATH = "path"; //$NON-NLS-1$

	// ///////////// //
	// search helper //
	// ///////////// //

	public static final String PFS_RCP_CORE_EP_HELPER_SEARCH = "de.dzimmermann.rcp.basicplatform.helper.search"; //$NON-NLS-1$

	public static final String PFS_RCP_CORE_EP_HELPER_SEARCH_ROOT = "search"; //$NON-NLS-1$
	public static final String PFS_RCP_CORE_EP_HELPER_SEARCH_ROOT_ATTR_TID = "target-id"; //$NON-NLS-1$
	public static final String PFS_RCP_CORE_EP_HELPER_SEARCH_ROOT_ATTR_MT = "search-message"; //$NON-NLS-1$
	public static final String PFS_RCP_CORE_EP_HELPER_SEARCH_ROOT_ATTR_TF = "target-factory"; //$NON-NLS-1$
	public static final String PFS_RCP_CORE_EP_HELPER_SEARCH_ROOT_ATTR_HICON = "hint-icon"; //$NON-NLS-1$
	public static final String PFS_RCP_CORE_EP_HELPER_SEARCH_ROOT_ATTR_HCANCEL = "hint-cancel"; //$NON-NLS-1$
	public static final String PFS_RCP_CORE_EP_HELPER_SEARCH_ELEMENT_OPTIONS = "options"; //$NON-NLS-1$
	public static final String PFS_RCP_CORE_EP_HELPER_SEARCH_ELEMENT_OPTION = "option"; //$NON-NLS-1$
	public static final String PFS_RCP_CORE_EP_HELPER_SEARCH_OPTION_ATTR_NAME = "name"; //$NON-NLS-1$
	public static final String PFS_RCP_CORE_EP_HELPER_SEARCH_OPTION_ATTR_LABEL = "label"; //$NON-NLS-1$
}
