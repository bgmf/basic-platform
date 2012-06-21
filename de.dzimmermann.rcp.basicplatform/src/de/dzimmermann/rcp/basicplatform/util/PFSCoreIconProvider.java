package de.dzimmermann.rcp.basicplatform.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import de.dzimmermann.rcp.basicplatform.SSDToolPlugin;
import de.dzimmermann.rcp.basicplatform.ui.util.Logger;
import de.dzimmermann.rcp.basicplatform.util.PFSCoreResourceType.IconResource;

/**
 * This class can create {@link Image} objects by {@link PFSCoreIcon}s.<br>
 * Alternatively, if a specific icon does not (yet) exists within the
 * {@link PFSCoreIcon} enumartion, you can create an image just by it's name. <br>
 * To do so, use the {@link #getImageByIconName(String)} method, which will try
 * to retrieve a {@link PFSCoreIcon} first, if that failes, it will try to
 * retrieve the image by its name directly from the plugins icon directory.
 * 
 * @author danielz
 * @version 0.3
 * @since PFSRCPCore V0.2.3
 */
public class PFSCoreIconProvider {

	/**
	 * resource jar attribute: resource.properties container
	 */
	private static final String RESOURCE_DEFINITION = "META-INF/resource.properties";
	/**
	 * resource jar attribute: property of the resource type (see
	 * {@link #RESOURCE_DEFINITION})
	 */
	private static final String RESOURCE_TYPE = "resource-type";

	/**
	 * helper attribute: contains a list of {@link URLClassLoader}s from the
	 * core's resource extension point, used to load (external) resources (see
	 * this PFSRCPCore/resource/read-me.txt for further information)
	 */
	private static List<URLClassLoader> urlClassLoaders;

	/**
	 * get a image by the name, starting with the default enabled icon
	 * 
	 * @param iconName
	 *            the name of the icon
	 * @param enabled
	 *            used, to specify whether the enabled or disabled icon should
	 *            be used
	 * @return the {@link Image} identified by the name
	 * @throws MalformedURLException
	 * @throws IllegalArgumentException
	 *             no respective {@link PFSCoreIcon} object exists to the given
	 *             icon path
	 */
	public static Image getImageByIconName(String iconName, boolean enabled) {

		// ### bugix: When there is no icon name, the fetching of an icon will
		// be stoped and null returned immediatelly.
		// On very rare occasion, this is useful, when the icon names are read
		// from a property or something like that
		if (iconName == null || iconName.isEmpty()) {
			return null;
		}
		// ### end of bugfix

		SSDToolUtils.getResourceExtensions();

		Image result = null;

		PFSCoreIcon icon = PFSCoreIcon.getIconTypeByIconPath(iconName, enabled);

		if (icon != null)
			try {
				result = getImageByIconType(icon);
			} catch (IllegalArgumentException e) {
				String message = "Icon " + icon.toString()
						+ " could not be instantiated.\nReason was: "
						+ e.getMessage();
				Logger.logInfo(message);
			} catch (IOException e) {
				String message = "Icon " + icon.toString()
						+ " could not be instantiated.\nReason was: "
						+ e.getMessage();
				Logger.logInfo(message);
			}

		String iconPath = (enabled ? PFSCoreIcon.ENABLED_ICON_PATH
				: PFSCoreIcon.DISABLED_ICON_PATH) + iconName;

		// default: read icon from PFSRCPCore plugin
		try {
			if (result == null) {
				// 1) try to get the image as requested
				URL pluginBasedURL = new URL("platform:/plugin/"
						+ SSDToolPlugin.PLUGIN_ID + "/" + iconPath);
				if (FileLocator.find(pluginBasedURL) != null) {
					ImageDescriptor id = AbstractUIPlugin
							.imageDescriptorFromPlugin(SSDToolPlugin.PLUGIN_ID,
									iconPath);
					result = id.createImage();
				}
				// 2) create a fallback with the enabled icon ...
				pluginBasedURL = new URL("platform:/plugin/"
						+ SSDToolPlugin.PLUGIN_ID + "/"
						+ PFSCoreIcon.ENABLED_ICON_PATH + iconName);
				// ... and try to resolve it
				if (!enabled && result == null
						&& FileLocator.find(pluginBasedURL) != null) {
					// fetch the enabled icon
					result = AbstractUIPlugin.imageDescriptorFromPlugin(
							SSDToolPlugin.PLUGIN_ID, iconPath).createImage();
					// try to create a disabled icon by the local platform
					// rules...
					Image disabled = new Image(result.getDevice(), result,
							SWT.IMAGE_DISABLE);
					result = disabled;
				}
			}
		} catch (MalformedURLException e) {
			Logger.logError(e);
		}

		// fallback: read icon from PFSRCPCore resource jars
		if (result == null) {
			try {
				// 1) try to get the image as requested
				result = getImageResource(enabled, iconName);
				// 2) create a fallback with the enabled icon
				if (!enabled && result == null) {
					result = getImageResource(true, iconName);
					// try to create a disabled icon by the local platform
					// rules...
					if (result != null) {
						Image disabled = new Image(result.getDevice(), result,
								SWT.IMAGE_DISABLE);
						result = disabled;
					}
				}
			} catch (IOException e) {
				Logger.logError(e);
			}
		}

		if (result == null) {
			Logger.logWarning("no icon could be loaded for icon name '"
					+ iconName + "' and status "
					+ (enabled ? "enabled" : "disabled"));
		}

		return result;
	}

	/**
	 * get a image by the {@link PFSCoreIcon} object given to this method
	 * 
	 * @param iconType
	 * @return
	 * @throws IOException
	 *             can happen during the process to locate the icon
	 * @throws IllegalArgumentException
	 *             no {@link PFSCoreIcon} was given or the icon could not be
	 *             found in the registry
	 */
	public static Image getImageByIconType(PFSCoreIcon iconType)
			throws IOException, IllegalArgumentException {

		if (iconType == null)
			throw new IllegalArgumentException(
					"Can't load an image when the icon type is NULL!");

		String internalSSDToolIconLocation = null;

		URL pluginBasedURL = new URL("platform:/plugin/"
				+ SSDToolPlugin.PLUGIN_ID + "/" + PFSCoreIcon.ENABLED_ICON_PATH
				+ iconType.iconName);

		if (iconType.enabled && FileLocator.find(pluginBasedURL) != null) {
			internalSSDToolIconLocation = PFSCoreIcon.ENABLED_ICON_PATH
					+ iconType.iconName;
		} else if (!iconType.enabled
				&& FileLocator.find(pluginBasedURL) != null) {
			internalSSDToolIconLocation = PFSCoreIcon.DISABLED_ICON_PATH
					+ iconType.iconName;
		} else {
			throw new IllegalArgumentException("Can't load the image "
					+ iconType.iconName + "!");
		}

		ImageDescriptor descriptor = AbstractUIPlugin
				.imageDescriptorFromPlugin(SSDToolPlugin.PLUGIN_ID,
						internalSSDToolIconLocation);
		if (descriptor == null) {
			Logger.logWarning(String.format("Can not find icon %s!",
					internalSSDToolIconLocation));
			return null;
		} else {
			try {
				return descriptor.createImage();
			} catch (Exception e) {
				Logger.logWarning(String.format(
						"Can not find icon %s! (Exception message was \"%s\")",
						internalSSDToolIconLocation, e.getMessage()));
				return null;
			}
		}
	}

	/**
	 * This method is used to check image resource bundled within this plugin
	 * 
	 * @param enabled
	 * @param iconName
	 * @return
	 * @throws Exception
	 */
	private static Image getImageResource(boolean enabled, String iconName)
			throws IOException {

		// this is the readout from the extension point
		if (urlClassLoaders == null) {
			urlClassLoaders = SSDToolUtils.getResourceExtensions();
		}
		if (urlClassLoaders != null) {
			for (URLClassLoader cl : urlClassLoaders) {
				Image i = createImageFromURLClassLoader(cl, enabled, iconName);
				if (i != null) {
					return i;
				}
			}
		}

		// this can only be successful, if the PFSRCPCore is not bundled!
		// try {
		// File[] resourceJars = getResourceJars();
		// if (resourceJars != null) {
		// for (File resourceJarFile : resourceJars) {
		// URLClassLoader cl = getResourceJarURLClassLoader(resourceJarFile);
		// return createImageFromURLClassLoader(cl, enabled, iconName);
		// }
		// }
		// } catch (Exception e) {
		// Logger
		// .logWarning(String
		// .format(
		// "Exception during reading jars via File-Objects%n"
		// + "%s"
		// +
		// "%n\tCan only occur if the resulting image was not found via PFSRCPCore resource extension point.",
		// e.getLocalizedMessage()));
		// }

		return null;
	}

	/**
	 * Tries to create an image from the given {@link URLClassLoader}, using the
	 * icon name and the enabled flag to identify it.
	 * 
	 * @param cl
	 *            the {@link URLClassLoader} where the image might be situated
	 *            in
	 * @param enabled
	 *            the enabled state for the image, if there is no such image,
	 *            <code>null</code> might be returned
	 * @param iconName
	 *            the icon name, if there is no such image, <code>null</code>
	 *            might be returned
	 * @return the {@link Image} or <code>null</code>, if the specified image
	 *         could not be found
	 * @throws IOException
	 *             an IOException can only occur, if the resource.properties
	 *             file was not found (see {@link #RESOURCE_DEFINITION}
	 *             -attribute)
	 */
	private static Image createImageFromURLClassLoader(URLClassLoader cl,
			boolean enabled, String iconName) throws IOException {

		if (cl != null) {

			InputStream in = cl.getResourceAsStream(RESOURCE_DEFINITION);

			if (in != null) {

				Properties p = new Properties();
				p.load(in);

				PFSCoreResourceType resourceType = PFSCoreResourceType
						.getResourceTypeByString(p.getProperty(RESOURCE_TYPE));
				if (resourceType != null
						&& PFSCoreResourceType.ICON == resourceType) {

					IconResource iconResource = PFSCoreResourceType.IconResource
							.getIconResourceByBoolean(enabled);
					if (iconResource != null) {

						URL iconResourceURL = cl
								.getResource(p.getProperty(iconResource
										.getType()) + iconName);
						if (iconResourceURL != null) {
							return ImageDescriptor.createFromURL(
									iconResourceURL).createImage();
						}
					}
				}
			}
		}

		return null;
	}

	/**
	 * private method to return an array of resource jars to check/load
	 * 
	 * @return the arrays of the '.jar' files found in diretory 'resources'
	 * @throws URISyntaxException
	 *             on malformed URI, when the resources directory is checked
	 * @throws IOException
	 *             when the {@link FileLocator} tries to resolve the plugin
	 *             internal jar file as a external resource
	 */
	public static File[] getResourceJars() {
		try {
			URL pluginBasedURL = new URL(String.format(
					"platform:/plugin/%s/resources", SSDToolPlugin.PLUGIN_ID));
			URL resourceLocURL = FileLocator.resolve(pluginBasedURL);
			if (resourceLocURL != null) {
				File resourceDir = new File(resourceLocURL.toURI());
				if (resourceDir.isDirectory()) {
					File[] resourceFiles = resourceDir
							.listFiles(new FileFilter() {
								@Override
								public boolean accept(File pathname) {
									// accept only jar files
									if (pathname.getName().endsWith(".jar")) {
										return true;
									}
									return false;
								}
							});
					return resourceFiles;
				}
			}
		} catch (URISyntaxException e) {
			Logger.logWarning(e.getMessage());
		} catch (MalformedURLException e) {
			Logger.logWarning(e.getMessage());
		} catch (IOException e) {
			Logger.logWarning(e.getMessage());
		}
		return null;
	}

	/**
	 * private method to create a {@link URLClassLoader} from a given resource
	 * jar file
	 * 
	 * @param resourceJarFile
	 *            the jar file to create the {@link URLClassLoader} from
	 * @return the {@link URLClassLoader}, no exeption was thrown
	 * @throws MalformedURLException
	 *             occures, when the URI/URL from the given file was malformed -
	 *             should never happen!
	 */
	@SuppressWarnings("unused")
	private static URLClassLoader getResourceJarURLClassLoader(
			File resourceJarFile) throws MalformedURLException {
		return new URLClassLoader(new URL[] { resourceJarFile.toURI().toURL() });
	}
}
