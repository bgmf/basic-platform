package de.dzimmermann.rcp.basicplatform.ui.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import de.dzimmermann.rcp.basicplatform.SSDToolPlugin;
import de.dzimmermann.rcp.basicplatform.ui.util.Logger;
import de.dzimmermann.rcp.basicplatform.util.BasicPlatformPreferenceConstants;

/**
 * This class theoretically should initalize the preferences with some default
 * settings.
 * 
 * @author dzimmermann
 * @version 0.3
 * @since SSDTool v0.2.0
 */
public class InternalPreferenceInitializer extends AbstractPreferenceInitializer {

	public InternalPreferenceInitializer() {
	}

	@Override
	public void initializeDefaultPreferences() {

		ConfigurationScope scope = new ConfigurationScope();
		// Logger.logInfo(scope.getLocation().toOSString());
		Preferences prefs = scope.getNode(SSDToolPlugin.PLUGIN_ID);
		// Logger.logInfo(prefs.absolutePath());

		prefs
				.putInt(
						BasicPlatformPreferenceConstants.RECENT_WORKSPACES_MAX_AMOUNT,
						5);

		prefs
				.putBoolean(
						BasicPlatformPreferenceConstants.RECENT_WORKSPACES_SHOW,
						true);

		prefs.put(BasicPlatformPreferenceConstants.KEYSTORE_LOACTION_PASSWORD,
				"password"); //$NON-NLS-1$

		prefs.put(BasicPlatformPreferenceConstants.TRUSTSTORE_LOACTION_PASSWORD,
				"password"); //$NON-NLS-1$

		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			Logger.logError(e);
		}
	}
}
