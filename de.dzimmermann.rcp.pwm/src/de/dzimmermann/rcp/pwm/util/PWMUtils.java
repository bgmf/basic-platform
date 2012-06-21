package de.dzimmermann.rcp.pwm.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.IFileSystem;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import de.dzimmermann.rcp.basicplatform.ui.util.Logger;
import de.dzimmermann.rcp.basicplatform.util.SSDToolUtils;
import de.dzimmermann.rcp.pwm.model.container.PWMContainer;
import de.dzimmermann.rcp.pwm.model.container.PWMContainerGroup;
import de.dzimmermann.rcp.pwm.model.container.PWMContainerGroupContent;
import de.dzimmermann.rcp.pwm.model.content.PWMGroup;
import de.dzimmermann.rcp.pwm.ui.dialog.ManagePWMDBsDialog;

public class PWMUtils {

	/*
	 * path constants
	 */

	// directory for the default file
	public static final String DEFAULT_PATH = System.getProperty("user.home")
			+ "/.pwm";

	// default file extension (it could be .xml.gz, but I chose .pwm)
	public static final String DEFAULT_EXTENSION = ".pwm"; //$NON-NLS-1$
	// backup extension to be appended to the original file
	public static final String DEFAULT_EXTENSION_BACKUP = ".bak"; //$NON-NLS-1$

	// default file name
	public static final String DEFAULT_FILE_NAME = "default-password-groups"
			+ DEFAULT_EXTENSION;

	/*
	 * workspace path
	 */

	private static final String DEFAULT_WORKSPACE_PATH = "pwm";

	/*
	 * task id constants
	 */

	public static final String PWM_TASK_ROOT = "de.dzimmermann.rcp.pwm.root";
	public static final String PWM_TASK_OPENDB = "de.dzimmermann.rcp.pwm.btOpenDB";

	/*
	 * dialog settings keys
	 */

	public static final String PWM_DS_MANAGE_DB = ManagePWMDBsDialog.class
			.getName();

	/*
	 * other constants
	 */
	public static final IFileSystem FILE_SYSTEM = EFS.getLocalFileSystem();

	/*
	 * convenient methods
	 */

	private static IPath getDefaultPath() {
		return new Path(DEFAULT_PATH + "/" + DEFAULT_FILE_NAME);
	}

	private static IPath getDefaultWorkspacePath() {
		return new Path(SSDToolUtils.getCurrentWorkspace() + "/"
				+ DEFAULT_WORKSPACE_PATH);
	}

	public static void createBackupFile(IFileStore file2backup, int efsOptions)
			throws CoreException {
		IFileStore backup = file2backup.getParent().getChild(
				file2backup.fetchInfo().getName() + DEFAULT_EXTENSION_BACKUP);
		file2backup.copy(backup, efsOptions, null);
	}

	/*
	 * PWM container methods
	 */

	public static PWMContainer loadPWMContainer() throws Exception {
		return loadPWMContainer((String) null);
	}

	public static PWMContainer loadPWMContainer(String path)
			throws IOException, CoreException, JAXBException {
		return loadPWMContainer(new Path(path));
	}

	public static PWMContainer loadPWMContainer(IPath path) throws IOException,
			CoreException, JAXBException {

		PWMContainer container = null;

		InputStream is = new GZIPInputStream(FILE_SYSTEM.getStore(
				path == null ? getDefaultPath() : path).openInputStream(
				EFS.NONE, null));

		JAXBContext ctxt = JAXBContext.newInstance(PWMContainer.class
				.getPackage().getName());
		Unmarshaller u = ctxt.createUnmarshaller();

		Object o = null;
		try {
			o = u.unmarshal(is);
		} catch (Exception e) {
			Logger.logError(e);
			System.err.println(e);
		}
		if (o != null) {
			container = (PWMContainer) o;
		}

		is.close();

		return container;
	}

	public static void savePWMContainer(PWMContainer container)
			throws JAXBException, IOException, CoreException {
		savePWMContainer(null, container);
	}

	public static void savePWMContainer(IPath path, PWMContainer container)
			throws JAXBException, IOException, CoreException {

		JAXBContext ctxt = JAXBContext.newInstance(PWMContainer.class
				.getPackage().getName());
		Marshaller marshaller = ctxt.createMarshaller();
		marshaller.setProperty("jaxb.formatted.output", Boolean.TRUE); //$NON-NLS-1$

		if (path == null)
			path = getDefaultPath();

		IFileStore fs = FILE_SYSTEM.getStore(path);

		if (fs.fetchInfo().exists()) {
			try {
				createBackupFile(fs, EFS.OVERWRITE);
			} catch (CoreException e) {
				String message = String
						.format("Error on creating a backup for file %s.%nThe message was: %s", //$NON-NLS-1$
								fs.fetchInfo().getName(), e.getMessage());
				System.err.printf(message);
			}
		}

		OutputStream os = new GZIPOutputStream(fs.openOutputStream(
				EFS.OVERWRITE, null));

		marshaller.marshal(container.createJAXBElement(container), os);

		os.flush();
		os.close();
	}

	/*
	 * PWM groups methods
	 */

	public static PWMGroup loadPWMGroup(PWMContainerGroup containerGroup,
			String password) throws JAXBException {

		if (password == null)
			password = "";

		PWMGroup pwmGroup = null;
		if (!getPWMContentHash(containerGroup,
				containerGroup.getContent().getValue()).equals(
				containerGroup.getContent().getHash()))
			return null;

		EncryptionDecryptionUtils des = EncryptionDecryptionUtils
				.getInstance(password);
		String decryptedData = des.decrypt(containerGroup.getContent()
				.getValue());
		if (decryptedData == null)
			return null;

		StringReader reader = new StringReader(decryptedData);

		JAXBContext ctxt = JAXBContext.newInstance(PWMGroup.class.getPackage()
				.getName());
		Unmarshaller u = ctxt.createUnmarshaller();

		pwmGroup = (PWMGroup) u.unmarshal(reader);

		reader.close();

		return pwmGroup;
	}

	public static void savePWMGroup(PWMContainerGroup containerGroup,
			PWMGroup pwmGroup, String password) throws JAXBException,
			IOException, Exception {

		if (password == null)
			password = "";

		if (containerGroup.getId() == null
				|| !containerGroup.getId().equals(pwmGroup.getId()))
			containerGroup.setId(pwmGroup.getId());

		if (containerGroup.getName() == null
				|| !containerGroup.getName().equals(pwmGroup.getName()))
			containerGroup.setName(pwmGroup.getName());

		JAXBContext ctxt = JAXBContext.newInstance(PWMGroup.class.getPackage()
				.getName());
		Marshaller marshaller = ctxt.createMarshaller();
		marshaller.setProperty("jaxb.formatted.output", Boolean.TRUE); //$NON-NLS-1$

		StringWriter writer = new StringWriter();

		marshaller.marshal(pwmGroup.createJAXBElement(pwmGroup), writer);

		writer.flush();
		writer.close();

		EncryptionDecryptionUtils des = EncryptionDecryptionUtils
				.getInstance(password);
		String encryptedData = des.encrypt(writer.toString());
		if (encryptedData == null)
			throw new Exception("Encryption of the storage data failed!");

		if (containerGroup.getContent() == null)
			containerGroup.setContent(new PWMContainerGroupContent());
		containerGroup.getContent().setValue(encryptedData);

		String hash = getPWMContentHash(containerGroup, encryptedData);
		containerGroup.getContent().setHash(hash);
	}

	/*
	 * PWM helper methods
	 */

	public static String getPWMContentHash(PWMContainerGroup containerGroup,
			String encryptedData) {
		return EncryptionDecryptionUtils.getDigest(containerGroup.getId() + "|"
				+ containerGroup.getName() + "|" + encryptedData);
	}

	public static String getPWMID(String name, Calendar cal) {
		return EncryptionDecryptionUtils
				.getDigest(name + cal.getTimeInMillis());
	}

	public static Object findParentPWMObject(Object parent,
			PWMContainerGroup group) {
		if (parent instanceof PWMContainer) {
			for (PWMContainerGroup cg : ((PWMContainer) parent).getGroups()) {
				if (cg.getId().equals(group.getId()))
					return parent;
			}
			PWMContainerGroup cgParent = null;
			for (PWMContainerGroup cg : ((PWMContainer) parent).getGroups()) {
				Object o = findParentPWMObject(cg, group);
				if (o != null && o instanceof PWMContainerGroup) {
					cgParent = (PWMContainerGroup) o;
					break;
				}
			}
			return cgParent;
		} else {
			PWMContainerGroup cgParent = null;
			for (PWMContainerGroup cg : ((PWMContainerGroup) parent)
					.getGroups()) {
				if (cg.getId().equals(group.getId()))
					return parent;
				Object o = findParentPWMObject(cg, group);
				if (o != null && o instanceof PWMContainerGroup) {
					cgParent = (PWMContainerGroup) o;
					break;
				}
			}
			return cgParent;
		}
	}

	/*
	 * manage database files within the workspace
	 */
	public static List<IPath> getDatabasesFromWorkspace() throws CoreException {

		List<IPath> pathList = null;

		IFileStore workspace = FILE_SYSTEM.getStore(getDefaultWorkspacePath());
		if (!workspace.fetchInfo().isDirectory())
			workspace.mkdir(EFS.NONE, null);

		for (IFileStore child : workspace.childStores(EFS.NONE, null)) {
			if (!child.getName().endsWith(DEFAULT_EXTENSION))
				continue;
			if (pathList == null)
				pathList = new ArrayList<IPath>();
			pathList.add(new Path(child.toLocalFile(EFS.NONE, null)
					.getAbsolutePath()));
		}

		return pathList;
	}

	public static IPath addDatabaseToWorkspace(String name)
			throws JAXBException, IOException, CoreException {

		if (name.toLowerCase().endsWith(DEFAULT_EXTENSION_BACKUP))
			name = name
					.substring(0, name.lastIndexOf(DEFAULT_EXTENSION_BACKUP));
		else if (name.toLowerCase().endsWith(DEFAULT_EXTENSION))
			name = name.substring(0, name.lastIndexOf(DEFAULT_EXTENSION));

		name = name.concat(DEFAULT_EXTENSION);

		IPath wsPath = getDefaultWorkspacePath();

		IFileStore workspace = FILE_SYSTEM.getStore(wsPath);
		if (!workspace.fetchInfo().isDirectory())
			workspace.mkdir(EFS.NONE, null);

		IPath path = wsPath.append(name);
		savePWMContainer(path, new PWMContainer());

		return path;
	}

	public static void removeDatabaseFromWorkspace(IPath path)
			throws CoreException {
		IFileStore fs = FILE_SYSTEM.getStore(path);
		if (!fs.fetchInfo().exists())
			return;
		createBackupFile(fs, EFS.OVERWRITE);
		fs.delete(EFS.NONE, null);
	}
}
