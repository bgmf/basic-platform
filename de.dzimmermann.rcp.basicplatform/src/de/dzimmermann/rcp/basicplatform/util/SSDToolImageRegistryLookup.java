package de.dzimmermann.rcp.basicplatform.util;

import java.io.IOException;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

import de.dzimmermann.rcp.basicplatform.SSDToolPlugin;
import de.dzimmermann.rcp.basicplatform.ui.util.Logger;

/**
 * This enum is a simple container for different icons which will be loaded
 * througout this plugin.
 * 
 * @author dzimmermann
 * @version 0.1
 * @since PCP V0.3.0
 * @see SSDToolPlugin
 */
public enum SSDToolImageRegistryLookup {

	// workspace icon (folder)
	WS_ENABLED("basic-platform.ws.enabled"), //$NON-NLS-1$
	// add - enabled
	ADD_ENABLED("basic-platform.add.enabled"), //$NON-NLS-1$
	// add - disabled
	ADD_DISABLED("basic-platform.add.disabled"), //$NON-NLS-1$
	// remove - enabled
	REMOVE_ENABLED("basic-platform.remove.enabled"), //$NON-NLS-1$
	// remove - disabled
	REMOVE_DISABLED("basic-platform.remove.disabled"), //$NON-NLS-1$
	// edit - enabled
	EDIT_ENABLED("basic-platform.edit.enabled"), //$NON-NLS-1$
	// edit - disabled
	EDIT_DISABLED("basic-platform.edit.disabled"), //$NON-NLS-1$
	// enable - enabled
	ENABLE_ENABLED("basic-platform.enable.enabled"), //$NON-NLS-1$
	// enable - disabled
	ENABLE_DISABLED("basic-platform.enable.disabled"), //$NON-NLS-1$
	// reset - enabled
	RESET_ENABLED("basic-platform.reset.enabled"), //$NON-NLS-1$
	// reset - disabled
	RESET_DISABLED("basic-platform.reset.disabled"), //$NON-NLS-1$
	// checked checkbox
	CHECKED("basic-platform.checked"), //$NON-NLS-1$
	// unchecked checkbox
	UNCHECKED("basic-platform.unchecked"); //$NON-NLS-1$

	private String key;

	private SSDToolImageRegistryLookup(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public static void fillSSDToolImageRegistry(ImageRegistry imageRegistry) {

		try {
			PFSCoreIcon icon = PFSCoreIcon.PFSCORE_ICON_MISC_ADD;
			icon.setEnabled(true);
			Image image = PFSCoreIconProvider.getImageByIconType(icon);
			if (image != null)
				imageRegistry.put(WS_ENABLED.key, image);
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
			Logger.logError(e);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			Logger.logError(e);
		}

		try {
			PFSCoreIcon icon = PFSCoreIcon.PFSCORE_ICON_MISC_ADD;
			icon.setEnabled(true);
			Image image = PFSCoreIconProvider.getImageByIconType(icon);
			if (image != null)
				imageRegistry.put(ADD_ENABLED.key, image);
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
			Logger.logError(e);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			Logger.logError(e);
		}

		try {
			PFSCoreIcon icon = PFSCoreIcon.PFSCORE_ICON_MISC_ADD;
			icon.setEnabled(false);
			Image image = PFSCoreIconProvider.getImageByIconType(icon);
			if (image != null)
				imageRegistry.put(ADD_DISABLED.key, image);
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
			Logger.logError(e);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			Logger.logError(e);
		}

		try {
			PFSCoreIcon icon = PFSCoreIcon.PFSCORE_ICON_MISC_EDIT_CHEATSHEET;
			icon.setEnabled(true);
			Image image = PFSCoreIconProvider.getImageByIconType(icon);
			if (image != null)
				imageRegistry.put(EDIT_ENABLED.key, image);
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
			Logger.logError(e);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			Logger.logError(e);
		}

		try {
			PFSCoreIcon icon = PFSCoreIcon.PFSCORE_ICON_MISC_EDIT_CHEATSHEET;
			icon.setEnabled(false);
			Image image = PFSCoreIconProvider.getImageByIconType(icon);
			if (image != null)
				imageRegistry.put(EDIT_DISABLED.key, image);
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
			Logger.logError(e);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			Logger.logError(e);
		}

		try {
			PFSCoreIcon icon = PFSCoreIcon.PFSCORE_ICON_EDITOR_DELETE;
			icon.setEnabled(true);
			Image image = PFSCoreIconProvider.getImageByIconType(icon);
			if (image != null)
				imageRegistry.put(REMOVE_ENABLED.key, image);
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
			Logger.logError(e);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			Logger.logError(e);
		}

		try {
			PFSCoreIcon icon = PFSCoreIcon.PFSCORE_ICON_EDITOR_DELETE;
			icon.setEnabled(false);
			Image image = PFSCoreIconProvider.getImageByIconType(icon);
			if (image != null)
				imageRegistry.put(REMOVE_DISABLED.key, image);
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
			Logger.logError(e);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			Logger.logError(e);
		}

		try {
			PFSCoreIcon icon = PFSCoreIcon.PFSCORE_ICON_MISC_START;
			icon.setEnabled(true);
			Image image = PFSCoreIconProvider.getImageByIconType(icon);
			if (image != null)
				imageRegistry.put(ENABLE_ENABLED.key, image);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			Logger.logError(e);
		}

		try {
			PFSCoreIcon icon = PFSCoreIcon.PFSCORE_ICON_MISC_START;
			icon.setEnabled(false);
			Image image = PFSCoreIconProvider.getImageByIconType(icon);
			if (image != null)
				imageRegistry.put(ENABLE_DISABLED.key, image);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			Logger.logError(e);
		}

		try {
			PFSCoreIcon icon = PFSCoreIcon.PFSCORE_ICON_EDITOR_UNDO;
			icon.setEnabled(true);
			Image image = PFSCoreIconProvider.getImageByIconType(icon);
			if (image != null)
				imageRegistry.put(RESET_ENABLED.key, image);
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
			Logger.logError(e);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			Logger.logError(e);
		}

		try {
			PFSCoreIcon icon = PFSCoreIcon.PFSCORE_ICON_EDITOR_UNDO;
			icon.setEnabled(false);
			Image image = PFSCoreIconProvider.getImageByIconType(icon);
			if (image != null)
				imageRegistry.put(RESET_DISABLED.key, image);
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
			Logger.logError(e);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			Logger.logError(e);
		}

		try {
			PFSCoreIcon icon = PFSCoreIcon.PFSCORE_ICON_MISC_CHECKED;
			icon.setEnabled(true);
			Image image = PFSCoreIconProvider.getImageByIconType(icon);
			if (image != null)
				imageRegistry.put(CHECKED.key, image);
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
			Logger.logError(e);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			Logger.logError(e);
		}

		try {
			PFSCoreIcon icon = PFSCoreIcon.PFSCORE_ICON_MISC_UNCHECKED;
			icon.setEnabled(true);
			Image image = PFSCoreIconProvider.getImageByIconType(icon);
			if (image != null)
				imageRegistry.put(UNCHECKED.key, image);
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
			Logger.logError(e);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			Logger.logError(e);
		}
	}
}
