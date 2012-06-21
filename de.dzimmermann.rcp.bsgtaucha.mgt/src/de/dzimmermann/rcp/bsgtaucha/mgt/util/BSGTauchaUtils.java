package de.dzimmermann.rcp.bsgtaucha.mgt.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.IFileSystem;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.osgi.service.datalocation.Location;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import de.dzimmermann.rcp.basicplatform.ui.util.Logger;
import de.dzimmermann.rcp.bsgtaucha.mgt.Activator;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.ObjectFactory;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.RootType;

public class BSGTauchaUtils {

	/**
	 * this method retrieves the last opened directory of this Plugin by its
	 * dialog setting<br>
	 * <br>
	 * this is a plug-in dependent workbench setting <br>
	 * <br>
	 * see {@link SSDConstants#LAST_OPEN_DIR}
	 * 
	 * @return the last directory
	 */
	public static String getDialogSettingsLastOpendDirectory() {
		return Activator.getDefault().getDialogSettings()
				.get(BSGTauchaConstants.LAST_OPEN_DIR);
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
			Activator.getDefault().getDialogSettings()
					.put(BSGTauchaConstants.LAST_OPEN_DIR, newLastDir);
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

		IDialogSettings settings = Activator.getDefault().getDialogSettings();
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

	public static IFileStore getWorkspaceLocation() {

		IFileStore workspaceFolder = null;

		Location workspace = Platform.getInstanceLocation();

		URL wsUrl = workspace.getURL();
		if (wsUrl != null) {
			IFileSystem fileSystem = EFS.getLocalFileSystem();
			try {
				// workspaceFolder =
				// fileSystem.getStore(wsUrl.toURI()).getChild(
				workspaceFolder = fileSystem
						.getStore(new Path(wsUrl.getFile())).getChild(
								BSGTauchaConstants.MODEL_DIR);
				if (!workspaceFolder.fetchInfo().isDirectory()) {
					workspaceFolder.mkdir(EFS.NONE, null);
				}
			} catch (Exception e) {
				Logger.logError(e);
				e.printStackTrace();
			}
		}

		return workspaceFolder;
	}

	public static String getLastModelPath() {
		InstanceScope scope = new InstanceScope();
		Preferences prefs = scope.getNode(Activator.PLUGIN_ID);
		return prefs.get(BSGTauchaConstants.LAST_MODEL_PREFERENCE, null);
	}

	public static void setLastModelPath(String newLastSSDModelPath) {
		// final String old = getLastSSDModelPath();
		InstanceScope scope = new InstanceScope();
		Preferences prefs = scope.getNode(Activator.PLUGIN_ID);
		prefs.put(BSGTauchaConstants.LAST_MODEL_PREFERENCE, newLastSSDModelPath);
		try {
			prefs.flush();
		} catch (BackingStoreException e) {
			Logger.logError(e);
		}
	}

	public static RootType openModel(File file) throws FileNotFoundException,
			IOException, JAXBException {

		RootType model = null;

		InputStream is = new GZIPInputStream(new FileInputStream(file));

		// JAXBContext ctxt = JAXBContext.newInstance(RootType.class);
		JAXBContext ctxt = JAXBContext.newInstance(RootType.class.getPackage()
				.getName());
		Unmarshaller u = ctxt.createUnmarshaller();

		// model = (RootType) u.unmarshal(is);
		@SuppressWarnings("unchecked")
		JAXBElement<RootType> data = (JAXBElement<RootType>) u.unmarshal(is);
		model = data.getValue();

		is.close();

		return model;
	}

	public static void saveModel(RootType model, File file)
			throws JAXBException, FileNotFoundException, IOException {

		JAXBContext ctxt = JAXBContext.newInstance(RootType.class.getPackage()
				.getName());
		Marshaller marshaller = ctxt.createMarshaller();
		marshaller.setProperty("jaxb.formatted.output", Boolean.TRUE); //$NON-NLS-1$

		if (file.isFile()) {
			IFileSystem fileSystem = EFS.getLocalFileSystem();
			try {
				createBackupFile(
						fileSystem.getStore(new Path(file.getAbsolutePath())),
						EFS.OVERWRITE);
			} catch (CoreException e) {
				String message = String
						.format("Error on creating a backup for file %s.%nThe message was: %s", //$NON-NLS-1$
								file.getAbsolutePath(), e.getMessage());
				Logger.logError(message, e);
				System.err.printf(message);
			}
		}

		OutputStream os = new GZIPOutputStream(new FileOutputStream(file));

		// marshaller.marshal(model, os);
		marshaller.marshal(new ObjectFactory().createRoot(model), os);

		os.flush();
		os.close();
	}

	public static void createBackupFile(IFileStore file2backup, int efsOptions)
			throws CoreException {
		IFileStore backup = file2backup.getParent().getChild(
				file2backup.fetchInfo().getName()
						+ BSGTauchaConstants.MODEL_EXTENSION_BACKUP);
		file2backup.copy(backup, efsOptions, null);
	}

	public static void saveModel(Class<?> classToSave, Object model, File file)
			throws JAXBException, IOException {

		JAXBContext ctxt = JAXBContext.newInstance(classToSave);
		Marshaller marshaller = ctxt.createMarshaller();
		marshaller.setProperty("jaxb.formatted.output", Boolean.TRUE);

		OutputStream os = new FileOutputStream(file);

		marshaller.marshal(model, os);

		os.flush();
		os.close();
	}
}
