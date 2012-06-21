package de.dzimmermann.rcp.basicplatform.services;

import java.util.List;

public interface IAvailablePluginService {

	/**
	 * This method decides whether or not the specified plugin is available
	 * within an implicit context.
	 * 
	 * @param pluginId
	 *            the plugin to test
	 * @return <code>true</code>, if the speicied plugin is available
	 */
	boolean isAvailable(String pluginId);

	/**
	 * This method decides whether or not the specified plugin is available
	 * within the specified context.<br/>
	 * The context might be something like the current users name, or something
	 * else.<br/>
	 * <b>Attention:</b> This can be implementation specific!
	 * 
	 * @param context
	 *            context the context for the check
	 * @param pluginId
	 *            the plugin to test
	 * @return <code>true</code>, if the speicied plugin is available
	 */
	boolean isAvailable(Object context, String pluginId);

	/**
	 * This method returns a list of the available plugins considering an
	 * implicit context.
	 * 
	 * @return the list of available plugins
	 */
	List<String> availablePlugins();

	/**
	 * This method returns a list of the available plugins within a specific
	 * context.<br/>
	 * The context might be something like the current users name, or something
	 * else.<br/>
	 * <b>Attention:</b> This can be implementation specific!
	 * 
	 * @param context
	 *            the context for the check
	 * @return the list of available plugins
	 */
	List<String> availablePlugins(Object context);

	/**
	 * Check whether or not the caller is a developer within an implicit
	 * context.
	 * 
	 * @return <code>true</code>, if the caller is a developer
	 */
	boolean isDeveloper();

	/**
	 * Check whether or not the caller is a developer within a specific context.<br/>
	 * The context might be something like the current users name, or something
	 * else.<br/>
	 * <b>Attention:</b> This can be implementation specific!
	 * 
	 * @param context
	 *            the context for the check
	 * @return <code>true</code>, if the caller is a developer
	 */
	boolean isDeveloper(Object context);
}
