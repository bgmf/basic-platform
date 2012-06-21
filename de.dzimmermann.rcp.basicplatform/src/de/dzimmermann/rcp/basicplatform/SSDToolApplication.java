package de.dzimmermann.rcp.basicplatform;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.IFileSystem;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchPlugin;

import de.dzimmermann.rcp.basicplatform.model.TruststoreList;
import de.dzimmermann.rcp.basicplatform.ui.dialog.OpenWorkpaceDialog;
import de.dzimmermann.rcp.basicplatform.ui.dialog.SelectTrustAndKeystorePathDialog;
import de.dzimmermann.rcp.basicplatform.ui.util.Logger;
import de.dzimmermann.rcp.basicplatform.util.BasicPlatformExtensionPointConstants;
import de.dzimmermann.rcp.basicplatform.util.BasicPlatformPluginConstants;
import de.dzimmermann.rcp.basicplatform.util.Messages;
import de.dzimmermann.rcp.basicplatform.util.PFSCoreIconProvider;
import de.dzimmermann.rcp.basicplatform.util.SSDToolUtils;

/**
 * This class controls all aspects of the application's execution
 */
@SuppressWarnings("restriction")
public class SSDToolApplication implements IApplication {

	public static final String SSDTOOL_APPLICATION_ERROR_MESSAGE_TITLE = Messages
			.getString("SSDToolApplication.error.message.title"); //$NON-NLS-1$

	public static final String SSDTOOL_APPLICATION_ERROR_MANDATORY_WS = Messages
			.getString("SSDToolApplication.error.mandatory.ws"); //$NON-NLS-1$
	public static final String SSDTOOL_APPLICATION_ERROR_CANNOT_LOCK_WS = Messages
			.getString("SSDToolApplication.error.cannot.lock.ws"); //$NON-NLS-1$
	public static final String SSDTOOL_APPLICATION_ERROR_CANNOT_SET_WS = Messages
			.getString("SSDToolApplication.error.cannot.set.ws"); //$NON-NLS-1$
	public static final String SSDTOOL_APPLICATION_ERROR_WS_IN_USE = Messages
			.getString("SSDToolApplication.error.ws.inuse"); //$NON-NLS-1$

	public static final String SSDTOOL_APPLICATION_ERROR_TRUSTSTORE = Messages
			.getString("SSDToolApplication.error.truststore"); //$NON-NLS-1$

	// The name of the folder containing metadata information for the workspace.
	protected static final String METADATA_FOLDER = ".metadata"; //$NON-NLS-1$
	protected static final String VERSION_FILENAME = "version.ini"; //$NON-NLS-1$ 
	protected static final String WORKSPACE_VERSION_KEY = "org.eclipse.core.runtime"; //$NON-NLS-1$ 
	protected static final String WORKSPACE_VERSION_VALUE = "1"; //$NON-NLS-1$
	protected static final String PROP_EXIT_CODE = "eclipse.exitcode"; //$NON-NLS-1$

	// java properties to set
	protected static final String JAVA_TRUSTSTORE_PROPERTY = "javax.net.ssl.trustStore"; //$NON-NLS-1$
	protected static final String JAVA_TRUSTSTORE_PASSWD_PROPERTY = "javax.net.ssl.trustStorePassword"; //$NON-NLS-1$
	protected static final String JAVA_KEYSTORE_PROPERTY = "javax.net.ssl.keyStore"; //$NON-NLS-1$
	protected static final String JAVA_KEYSTORE_PASSWD_PROPERTY = "javax.net.ssl.keyStorePassword"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.
	 * IApplicationContext)
	 */
	@SuppressWarnings({ "deprecation" })
	public Object start(IApplicationContext context) throws Exception {

		Display.DEBUG = true;
		Display display = PlatformUI.createDisplay();
		try {

			// look and see if there's a splash shell we can parent off of
			Shell shell = WorkbenchPlugin.getSplashShell(display);
			if (shell != null) {
				// should should set the icon and message for this shell to be
				// the same as the chooser dialog - this will be the guy that
				// lives in the task bar and without these calls you'd have the
				// default icon with no message.
				shell.setText(OpenWorkpaceDialog.WINDOW_TITLE);

				Image img = PFSCoreIconProvider.getImageByIconName(
						"fugue_application-terminal.png", true); //$NON-NLS-1$
				if (img == null) {
					ImageDescriptor imgDescr = SSDToolPlugin
							.getImageDescriptor("icons/ipqlogo_16.gif"); //$NON-NLS-1$
					if (imgDescr != null)
						img = imgDescr.createImage();
				}
				if (img != null)
					shell.setImage(img);
				else
					shell.setImages(Dialog.getDefaultImages());
			}

			// check if the key- and trustsore prefs are set correctly or force
			// setting them, if the user cancels, then the application ends
			// boolean check = checkTrustAndKeyStoreLocation(shell);
			// if (!check) {
			// WorkbenchPlugin.unsetSplashShell(display);
			// Platform.endSplash();
			// return IApplication.EXIT_OK;
			// }

			// try to open the current workspace or select one using the dialog
			boolean check = checkInstanceLocation(shell);
			if (!check) {
				WorkbenchPlugin.unsetSplashShell(display);
				Platform.endSplash();
				return IApplication.EXIT_OK;
			}

			// try read or set the truststore using the truststore part of the
			// remote server extension point
			// currently not in use, will always return true
			try {
				check = checkAndCopyTruststore();
				if (!check) {
					printErrorMessage(shell,
							SSDTOOL_APPLICATION_ERROR_TRUSTSTORE
									+ "\nCheck was false"); //$NON-NLS-1$
				}
			} catch (Exception e) {
				printErrorMessage(shell, SSDTOOL_APPLICATION_ERROR_TRUSTSTORE
						+ "\nException was\n" + e.getMessage(), e); //$NON-NLS-1$
			}

			int returnCode = PlatformUI.createAndRunWorkbench(display,
					new ApplicationWorkbenchAdvisor());
			if (returnCode == PlatformUI.RETURN_RESTART) {
				return IApplication.EXIT_RESTART;
			}

			return IApplication.EXIT_OK;

		} finally {

			if (display != null) {
				display.dispose();
			}

			Location instanceLoc = Platform.getInstanceLocation();
			if (instanceLoc != null && instanceLoc.isLocked()) {
				instanceLoc.release();
			}
		}
	}

	/**
	 * In opposite to the method {@link #checkAndCopyTruststore()} this method
	 * will only pop up a dedicated dialog, when no trust- or keystore are
	 * present in the preferences, or if those are pointing to non-existing
	 * files.
	 * 
	 * @param shell
	 *            the shell for the dialog
	 * @return <code>true</code> if the preferences are set to a valid value,
	 *         otherwise <code>false</code>
	 */
	// XXX currently not in use
	@SuppressWarnings("unused")
	private boolean checkTrustAndKeyStoreLocation(Shell shell) {

		String keyStorePath = SSDToolUtils.getCurrentKeyStorePath();
		String trustStorePath = SSDToolUtils.getCurrentTrustStorePath();

		boolean force = forceTrustAndKeyStore(keyStorePath, trustStorePath);

		while (keyStorePath == null || trustStorePath == null || force) {

			SelectTrustAndKeystorePathDialog dialog = new SelectTrustAndKeystorePathDialog(
					shell);
			try {
				if (TitleAreaDialog.CANCEL == dialog.open()) {
					return false;
				}
			} catch (Exception e) {
				Logger.logError(e);
			}

			keyStorePath = SSDToolUtils.getCurrentKeyStorePath();
			trustStorePath = SSDToolUtils.getCurrentTrustStorePath();
			force = forceTrustAndKeyStore(keyStorePath, trustStorePath);
		}

		System.setProperty(JAVA_KEYSTORE_PROPERTY, keyStorePath);
		System.setProperty(JAVA_TRUSTSTORE_PROPERTY, trustStorePath);

		return true;
	}

	/**
	 * This helper methods checks the both pathes and decides, whether or not
	 * the selection of those pathes should be forced or not
	 * 
	 * @param keyStorePath
	 *            the path of the keystore
	 * @param trustStorePath
	 *            the path of the truststore
	 * @return <code>true</code>, if the selection should be forces, otherwise
	 *         <code>false</code>
	 */
	private boolean forceTrustAndKeyStore(String keyStorePath,
			String trustStorePath) {

		boolean force = false;

		if (keyStorePath != null) {
			File f = new File(keyStorePath);
			if (!f.isFile()) {
				force = true;
			}
		}
		if (trustStorePath != null) {
			File f = new File(trustStorePath);
			if (!f.isFile()) {
				force = true;
			}
		}

		return force;
	}

	/**
	 * <b>THIS IS A COPY OF THE IDEApplication SAME METHOD, CHANGED TO FIT MY
	 * NEEDS!</b><br>
	 * Return true if a valid workspace path has been set and false otherwise.
	 * Prompt for and set the path if possible and required. <br>
	 * I decided not to make the whole
	 * eclipse-check-workspace-version-validity-check-thing
	 * 
	 * @param shell
	 *            the shell for the dialog
	 * @author dzimmermann
	 * @return <code>true</code> if a valid instance location has been set and
	 *         <code>false</code> otherwise
	 */
	@SuppressWarnings("deprecation")
	private boolean checkInstanceLocation(Shell shell) {

		Location instanceLoc = Platform.getInstanceLocation();

		// -data @none was specified but an ide requires workspace
		if (instanceLoc == null) {
			printErrorMessage(shell, SSDTOOL_APPLICATION_ERROR_MANDATORY_WS);
			return false;
		}

		// -data "/valid/path", workspace already set
		if (instanceLoc.isSet()) {
			// make sure the meta data version is compatible (or the user has
			// chosen to overwrite it).
			if (!checkPathValidity(instanceLoc.getURL())) {
				return false;
			}

			// at this point its valid, so try to lock it and update the
			// metadata version information if successful
			try {
				if (instanceLoc.lock()) {
					// writeWorkspaceVersion();
					return true;
				}

				// we failed to create the directory.
				// Two possibilities:
				// 1. directory is already in use
				// 2. directory could not be created
				URI uri = null;
				try {
					uri = instanceLoc.getURL().toURI();
				} catch (URISyntaxException e) {
					Logger.logError(e);
				}
				// File workspaceDirectory = uri != null ? new File(uri)
				// : new File(instanceLoc.getURL().getFile());
				IFileSystem fileSystem = EFS.getLocalFileSystem();
				IFileStore wsStore = fileSystem.getStore(uri);
				if (wsStore.fetchInfo().exists()) {
					// if (workspaceDirectory.exists()) {
					printErrorMessage(shell,
							SSDTOOL_APPLICATION_ERROR_CANNOT_LOCK_WS);
				} else {
					printErrorMessage(shell,
							SSDTOOL_APPLICATION_ERROR_CANNOT_SET_WS);
				}
			} catch (IOException e) {
				Logger.logError("Could not obtain lock for workspace location", //$NON-NLS-1$
						e);
				printErrorMessage(shell, e.getMessage());
			}
			return false;
		}

		// -data @noDefault or -data not specified, prompt and set

		boolean force = false;

		while (true) {

			URL workspaceUrl = null;

			if (force || SSDToolUtils.isShowWorkspaceSelectionDialog()) {

				OpenWorkpaceDialog dialog = new OpenWorkpaceDialog(shell);

				if (dialog.open() == TitleAreaDialog.CANCEL) {
					return false;
				}

				// workspaceUrl = dialog.getCurrentWSAsURL();

				try {
					File f = new File(dialog.getCurrentWS());
					workspaceUrl = f.toURL();
				} catch (MalformedURLException e) {
					printErrorMessage(shell, dialog.getCurrentWS(), e);
					Logger.logError(e);
					e.printStackTrace();
				}

			} else {
				try {
					workspaceUrl = new File(SSDToolUtils.getCurrentWorkspace())
							.toURL();
					// SSDToolUtils.getCurrentWorkspaceFile().toURI().toURL();
				} catch (MalformedURLException e) {
					printErrorMessage(shell,
							SSDToolUtils.getCurrentWorkspace(), e);
					Logger.logError(e);
					e.printStackTrace();
				}
			}

			if (workspaceUrl == null) {
				return false;
			}

			// if there is an error with the first selection, then force the
			// dialog to open to give the user a chance to correct
			force = true;

			try {
				// the operation will fail if the url is not a valid
				// instance data area, so other checking is unneeded
				if (instanceLoc.set(workspaceUrl, true)) {
					// writeWorkspaceVersion();
					return true;
				}
			} catch (Exception e) {
				Logger.logError(SSDTOOL_APPLICATION_ERROR_CANNOT_SET_WS, e);
				printErrorMessage(shell,
						SSDTOOL_APPLICATION_ERROR_CANNOT_SET_WS);
				return false;
			}

			// by this point it has been determined that the workspace is
			// already in use -- force the user to choose again
			printErrorMessage(shell, SSDTOOL_APPLICATION_ERROR_WS_IN_USE);
		}
	}

	/**
	 * no metadata check here...
	 * 
	 * @param path
	 *            the url to check
	 * @return <code>true</code>, is the path is valid, else <code>false</code>
	 */
	private boolean checkPathValidity(URL path) {

		URI uri = null;
		try {
			uri = path.toURI();
		} catch (URISyntaxException e) {
			Logger.logError(e);
		}

		// File f = uri != null ? new File(uri) : new File(path.getFile());
		IFileSystem fileSystem = EFS.getLocalFileSystem();
		IFileStore store = fileSystem.getStore(uri);

		if (!store.fetchInfo().isDirectory()) {
			return false;
		}

		return true;
	}

	/**
	 * default helper method to print an error
	 * 
	 * @param parentShell
	 *            the parent shell on where the message should pop up.
	 * @param message
	 *            the message to display
	 */
	private void printErrorMessage(Shell parentShell, String message,
			Exception... exception) {
		MessageDialog.openError(parentShell,
				SSDTOOL_APPLICATION_ERROR_MESSAGE_TITLE, message);
		if (exception == null || exception.length == 0) {
			Logger.logError(new Exception(message));
		} else {
			for (Exception e : exception) {
				Logger.logError(e);
			}
		}
	}

	/**
	 * TODO Fix the truststore checking and setting
	 * 
	 * @return
	 * @throws URISyntaxException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws CoreException
	 */
	private boolean checkAndCopyTruststore() throws URISyntaxException,
			MalformedURLException, IOException, CoreException {

		Location instanceLoc = Platform.getInstanceLocation();
		if (instanceLoc != null) {
			// File instanceLocFile = new File(instanceLoc.getURL().toURI());
			File instanceLocFile = new File(instanceLoc.getURL().getFile());
			if (!instanceLocFile.isDirectory()) {
				return false;
			}
			File truststoreLocFile = new File(
					instanceLocFile,
					BasicPlatformExtensionPointConstants.SSDTOOL_SERVER_TRUSTORE_INSTANCELOC_PATH);
			if (!truststoreLocFile.exists()) {
				if (!truststoreLocFile.mkdirs()) {
					return false;
				}
			}
			if (truststoreLocFile.isDirectory()) {
				TruststoreList truststores = SSDToolPlugin.getDefault()
						.getSSDToolModel().getTruststoreList();
				for (String plugin : truststores.keySet()) {
					File truststore = new File(new File(truststoreLocFile,
							plugin), new File(truststores.get(plugin)
							.getInternalPath()).getName());
					if (truststore.isFile()) {
						System.setProperty(JAVA_TRUSTSTORE_PROPERTY,
								truststore.getAbsolutePath());
						if (truststores.get(plugin).getOptionalPassword() != null) {
							System.setProperty(JAVA_TRUSTSTORE_PASSWD_PROPERTY,
									truststores.get(plugin)
											.getOptionalPassword());
						}
						return true;
					}
					URL url = FileLocator
							.find(new URL(
									BasicPlatformPluginConstants.PLUGIN_BASE_URL_STRING
											+ plugin
											+ (!truststores.get(plugin)
													.getInternalPath()
													.startsWith("/") ? "/" //$NON-NLS-1$ //$NON-NLS-2$
													+ truststores.get(plugin)
															.getInternalPath()
													: truststores.get(plugin)
															.getInternalPath())));
					if (url != null) {

						truststore.getParentFile().mkdirs();

						InputStream in = FileLocator.openStream(Platform
								.getBundle(plugin),
								new Path(truststores.get(plugin)
										.getInternalPath()), false);
						FileOutputStream out = new FileOutputStream(truststore,
								false);

						byte[] buffer = new byte[1024];

						int read = 0;
						while ((read = in.read(buffer)) != -1) {
							out.write(buffer, 0, read);
						}

						in.close();
						out.close();

						if (truststore.isFile()) {
							System.setProperty(JAVA_TRUSTSTORE_PROPERTY,
									truststore.getAbsolutePath());
							if (truststores.get(plugin).getOptionalPassword() != null) {
								System.setProperty(
										JAVA_TRUSTSTORE_PASSWD_PROPERTY,
										truststores.get(plugin)
												.getOptionalPassword());
							}
							return true;
						} else {
							return false;
						}
					} else {
						return false;
					}
				}
			} else {
				return false;
			}
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		try {
			Location instanceLoc = Platform.getInstanceLocation();
			if (instanceLoc != null && instanceLoc.isLocked()) {
				instanceLoc.release();
			}
		} catch (IOException e) {
			Logger.logError(Messages.getString("SSDToolApplication.10"), e); //$NON-NLS-1$
		}

		final IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null)
			return;
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}
}
