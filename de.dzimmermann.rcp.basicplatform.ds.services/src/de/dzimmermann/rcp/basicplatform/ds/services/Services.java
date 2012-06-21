package de.dzimmermann.rcp.basicplatform.ds.services;

import java.util.List;

import org.osgi.service.component.ComponentContext;

import de.dzimmermann.rcp.basicplatform.services.IAvailablePluginService;
import de.dzimmermann.rcp.basicplatform.services.IGroupService;

public class Services implements IAvailablePluginService, IGroupService {

	private static final String PLUGIN_ID = "de.dzimmermann.rcp.basicplatform.ds.services";

	public Services() {
	}

	/*
	 * The following methods are the implementation of the
	 * IAvailablePluginService interface
	 */

	@Override
	public boolean isAvailable(String pluginId) {
		return false;
	}

	@Override
	public boolean isAvailable(Object context, String pluginId) {
		throw new UnsupportedOperationException(
				"The method IAvailablePluginService#isAvailable(Object,String) is not supported by this Service implementation");
	}

	@Override
	public List<String> availablePlugins() {
		return null;
	}

	@Override
	public List<String> availablePlugins(Object context) {
		throw new UnsupportedOperationException(
				"The method IAvailablePluginService#availablePlugins(Object) is not supported by this Service implementation");
	}

	@Override
	public boolean isDeveloper() {
		return false;
	}

	@Override
	public boolean isDeveloper(Object context) {
		throw new UnsupportedOperationException(
				"The method IAvailablePluginService#isDeveloper(Object) is not supported by this Service implementation");
	}

	/*
	 * The following methods are the implementation of the IGroupService
	 * interface
	 */

	@Override
	public List<String> getAssignedGroups() {
		return null;
	}

	@Override
	public List<String> getAssignedGroups(Object context) {
		throw new UnsupportedOperationException(
				"The method IGroupService#getAssignedGroups(Object) is not supported by this Service implementation");
	}

	protected void activate(ComponentContext componentContext) {
	}

	protected void deactivate(ComponentContext componentContext) {
	}
}
