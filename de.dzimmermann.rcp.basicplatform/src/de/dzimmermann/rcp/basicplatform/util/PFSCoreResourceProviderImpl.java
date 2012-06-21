package de.dzimmermann.rcp.basicplatform.util;

import java.net.URL;
import java.net.URLClassLoader;

import org.eclipse.core.runtime.FileLocator;

import de.dzimmermann.rcp.basicplatform.SSDToolPlugin;

/**
 * The local implementation for the
 * 
 * @author dzimmermann
 * 
 */
public class PFSCoreResourceProviderImpl implements PFSCoreResourceProvider {

	public PFSCoreResourceProviderImpl() {
	}

	@Override
	public URLClassLoader getResource(String resource) throws Exception {
		URL url = new URL(String.format("%s%s/%s",
				BasicPlatformPluginConstants.PLUGIN_BASE_URL_STRING,
				SSDToolPlugin.PLUGIN_ID, resource));
		if (FileLocator.find(url) != null) {
			URLClassLoader loader = new URLClassLoader(new URL[] { getClass()
					.getClassLoader().getResource(resource) }, getClass()
					.getClassLoader());
			return loader;
		}
		return null;
	}
}
