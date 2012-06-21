package de.dzimmermann.rcp.basicplatform.ui.dialog;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import de.dzimmermann.rcp.basicplatform.util.SSDToolUtils;

/**
 * The newer version of the OpenSaveDialog.<br>
 * It's not a real dialog but calls the {@link FileDialog} and
 * {@link DirectoryDialog} depending on the given paramters.<br>
 * It is able to provide the last opend directory, it's {@link #open()} method
 * returns the opened file or directory.
 * 
 * @author danielz
 * @since PFSManager V0.5.7, PFSRCPCore V0.2.2
 * @version 0.1
 */
public class OpenSaveDialogV2 {

	/**
	 * the default window title
	 */
	private static final String WINDOW_TITLE = "PFS Manager Dialog";

	/**
	 * the addtion to the {@link #WINDOW_TITLE} attribute, depending on the
	 * {@link #isDirectory} attribute<br>
	 * <ul>
	 * <li>the defalut open addtion</li>
	 * </ul>
	 */
	private static final String STANDARD_TITLE_OPEN = "Open";
	/**
	 * the addtion to the {@link #WINDOW_TITLE} attribute, depending on the
	 * {@link #isDirectory} attribute<br>
	 * <ul>
	 * <li>the defalut save addtion</li>
	 * </ul>
	 */
	private static final String STANDARD_TITLE_SAVE = "Save";

	/**
	 * the open message displayed within the {@link FileDialog} or
	 * {@link DirectoryDialog}
	 */
	private static final String OPEN_MSG = "Select or enter a file to open.";
	/**
	 * the save message displayed within the {@link FileDialog} or
	 * {@link DirectoryDialog}
	 */
	private static final String SAVE_MSG = "Select or enter a file to save.";

	/**
	 * this attribute indicates wheter or not this a dialog used to open sth.<br>
	 * <ul>
	 * <li><code>true</code> = open a file or directory</li>
	 * <li><code>false</code> = save a file or directory</li>
	 * </ul>
	 */
	private final boolean isOpenDialog;
	/**
	 * this attribute indicates whether the dialog is used for files or
	 * directories<br>
	 * <ul>
	 * <li><code>true</code> = is used for directories</li>
	 * <li><code>false</code> = is used for files</li>
	 * </ul>
	 */
	private final boolean isDirectory;
	/**
	 * this attribute indicates if this {@link FileDialog} is used to open more
	 * than one file
	 */
	private final boolean isMultiFileDialog;

	/**
	 * if given, this attributes identifies the last opened directoy by this
	 * string
	 */
	private String lastDirectory;

	/**
	 * this map is used for {@link FileDialog}s, to filter files by extension<br>
	 * the key is the named file type, the value the extension<br>
	 * e.g.: Text-CSV = *.csv
	 */
	private final Map<String, String> fileExtensions;
	/**
	 * the resulting file, but if this is given to the {@link OpenSaveDialogV2},
	 * it will be used instead of the {@link #lastDirectory} attribute
	 */
	private File fileToHandle;

	/**
	 * the names of the multi file selection<br>
	 * (see {@link #isMultiFileDialog} and {@link #fileToHandle})
	 */
	private String[] openedMultiFileNames;

	/**
	 * the parent shell of the {@link OpenSaveDialogV2}, used for the underlying
	 * {@link FileDialog} or {@link DirectoryDialog}
	 */
	private final Shell parentShell;

	/**
	 * this attribute can be used to replace the default override title
	 * {@link #WINDOW_TITLE}={@value #WINDOW_TITLE} with a user-defined one
	 */
	private String overrideWindowTitle;
	/**
	 * this attribute can be used to define a user-defind replacement for the
	 * {@link #STANDARD_TITLE_OPEN}={@value #STANDARD_TITLE_OPEN} attribute
	 */
	private String overrideWindowTitleOpenAddition;
	/**
	 * this attribute can be used to define a user-defind replacement for the
	 * {@link #STANDARD_TITLE_SAVE}={@value #STANDARD_TITLE_SAVE} attribute
	 */
	private String overrideWindowTitleSaveAddition;
	/**
	 * this attribute can be used to replace the default open message
	 * {@link #OPEN_MSG}={@value #OPEN_MSG} attribute
	 */
	private String overrideOpenMessage;
	/**
	 * this attribute can be used to replace the default save message
	 * {@link #SAVE_MSG}={@value #SAVE_MSG} attribute
	 */
	private String overrideSaveMessage;

	/**
	 * The default contructor to build the appropriate dialog.
	 * 
	 * @param parentShell
	 *            the parent shell of the dialog to open (see
	 *            {@link #parentShell})
	 * @param isOpenDialog
	 *            decide, whether this dialog is used to open or save a file or
	 *            directory (see {@link #isDirectory})
	 * @param overrideWindowTitle
	 *            override the default dialog title, when != <code>null</code>
	 * @param overrideWindowTitleOpenAddition
	 *            overrides the default open additon to the window title when !=
	 *            <code>null</code>
	 * @param overrideWindowTitleSaveAddition
	 *            overrides the default save additon to the window title when !=
	 *            <code>null</code>
	 * @param overrideOpenMessage
	 *            overrides the default open message of the dialog when !=
	 *            <code>null</code>
	 * @param overrideSaveMessage
	 *            overrides the default save message of the dialog when !=
	 *            <code>null</code>
	 * @param lastDirectory
	 *            the last opend directory, can be used to set a starting point
	 *            to the dialog (see {@link #lastDirectory})
	 * @param fileToHandle
	 *            the last opend {@link File}, can be used to set a starting
	 *            point to the dialog (see {@link #fileToHandle})
	 * @param isDirectory
	 *            decide, whether this dialog shoul handle directories or files
	 *            (see {@link #isDirectory})
	 * @param isMultiFileDialog
	 *            decide, whether a {@link FileDialog} should be used to open
	 *            multiple files or not (see {@link #isMultiFileDialog})
	 * @param fileExtensions
	 *            a {@link Map} of file extension (see {@link #fileExtensions})
	 */
	public OpenSaveDialogV2(Shell parentShell, boolean isOpenDialog,
			String overrideWindowTitle, String overrideWindowTitleOpenAddition,
			String overrideWindowTitleSaveAddition, String overrideOpenMessage,
			String overrideSaveMessage, String lastDirectory,
			File fileToHandle, boolean isDirectory, boolean isMultiFileDialog,
			Map<String, String> fileExtensions) {

		this.parentShell = parentShell;

		this.isOpenDialog = isOpenDialog;

		this.overrideWindowTitle = overrideWindowTitle == null ? WINDOW_TITLE
				: overrideWindowTitle;
		this.overrideWindowTitleOpenAddition = overrideWindowTitleOpenAddition == null ? STANDARD_TITLE_OPEN
				: overrideWindowTitleOpenAddition;
		this.overrideWindowTitleSaveAddition = overrideWindowTitleSaveAddition == null ? STANDARD_TITLE_SAVE
				: overrideWindowTitleSaveAddition;
		this.overrideOpenMessage = overrideOpenMessage == null ? OPEN_MSG
				: overrideOpenMessage;
		this.overrideSaveMessage = overrideSaveMessage == null ? SAVE_MSG
				: overrideSaveMessage;

		this.lastDirectory = lastDirectory;
		this.fileToHandle = fileToHandle;
		this.isDirectory = isDirectory;
		this.isMultiFileDialog = isMultiFileDialog;

		if (fileExtensions == null || fileExtensions.isEmpty()) {

			Map<String, String> newFileExtensions = new HashMap<String, String>();
			newFileExtensions.put("all files", "*");

			this.fileExtensions = newFileExtensions;

		} else
			this.fileExtensions = fileExtensions;
	}

	/**
	 * The default contructor to build the appropriate dialog.
	 * 
	 * @param parentShell
	 *            the parent shell of the dialog to open (see
	 *            {@link #parentShell})
	 * @param isOpenDialog
	 *            decide, whether this dialog is used to open or save a file or
	 *            directory (see {@link #isDirectory})
	 * @param lastDirectory
	 *            the last opend directory, can be used to set a starting point
	 *            to the dialog (see {@link #lastDirectory})
	 * @param fileToHandle
	 *            the last opend {@link File}, can be used to set a starting
	 *            point to the dialog (see {@link #fileToHandle})
	 * @param isDirectory
	 *            decide, whether this dialog shoul handle directories or files
	 *            (see {@link #isDirectory})
	 * @param isMultiFileDialog
	 *            decide, whether a {@link FileDialog} should be used to open
	 *            multiple files or not (see {@link #isMultiFileDialog})
	 * @param fileExtensions
	 *            a {@link Map} of file extension (see {@link #fileExtensions})
	 */
	public OpenSaveDialogV2(Shell parentShell, boolean isOpenDialog,
			String lastDirectory, File fileToHandle, boolean isDirectory,
			boolean isMultiFileDialog, Map<String, String> fileExtensions) {

		this(parentShell, isOpenDialog, null, null, null, null, null,
				lastDirectory, fileToHandle, isDirectory, isMultiFileDialog,
				fileExtensions);
	}

	/**
	 * This method opens a dialog approprioate to your selection.
	 * 
	 * @return returns the absolute path of the first selected file, if you had
	 *         set the {@link #isMultiFileDialog} flag, you need to read all
	 *         files by using the {@link #getMultiFilesToHandle()} method
	 */
	public String open() {

		String result = null;

		if (lastDirectory == null || lastDirectory.isEmpty()) {

			lastDirectory = SSDToolUtils.getDialogSettingsLastOpendDirectory();

			if (lastDirectory == null || lastDirectory.isEmpty())
				lastDirectory = System.getProperty("user.home", ".");
		}

		String selection = null;

		if (isDirectory) {

			DirectoryDialog directoryDialog;

			if (isOpenDialog) {

				directoryDialog = new DirectoryDialog(parentShell, SWT.OPEN);

				directoryDialog.setText(overrideWindowTitle + ": "
						+ overrideWindowTitleOpenAddition);
				directoryDialog.setMessage(overrideOpenMessage);

				if (fileToHandle != null && fileToHandle.isFile()) {
					directoryDialog.setFilterPath(fileToHandle.getParent());
				} else if (fileToHandle != null && fileToHandle.isDirectory()) {
					directoryDialog.setFilterPath(fileToHandle
							.getAbsolutePath());
				} else {
					directoryDialog.setFilterPath(lastDirectory);
				}

			} else {

				directoryDialog = new DirectoryDialog(parentShell, SWT.SAVE);

				directoryDialog.setText(overrideWindowTitle + ": "
						+ overrideWindowTitleSaveAddition);
				directoryDialog.setMessage(overrideSaveMessage);

				if (fileToHandle != null && fileToHandle.isFile()) {
					directoryDialog.setFilterPath(fileToHandle.getParent());
				} else if (fileToHandle != null && fileToHandle.isDirectory()) {
					directoryDialog.setFilterPath(fileToHandle
							.getAbsolutePath());
				} else {
					directoryDialog.setFilterPath(lastDirectory);
				}
			}

			selection = directoryDialog.open();

		} else {

			FileDialog fileDialog;

			String[] filterNames = new String[fileExtensions.size()];
			String[] filterExtensions = new String[fileExtensions.size()];

			int index = 0;
			for (String name : fileExtensions.keySet()) {

				filterNames[index] = name;
				filterExtensions[index] = fileExtensions.get(name);

				index++;
			}

			if (isOpenDialog) {

				fileDialog = new FileDialog(parentShell, SWT.OPEN
						| (isMultiFileDialog ? SWT.MULTI : SWT.NONE));

				fileDialog.setText(overrideWindowTitle + ": "
						+ overrideWindowTitleOpenAddition);

				if (fileToHandle != null && fileToHandle.isFile()) {
					fileDialog.setFilterPath(fileToHandle.getParent());
				} else if (fileToHandle != null && fileToHandle.isDirectory()) {
					fileDialog.setFilterPath(fileToHandle.getAbsolutePath());
				} else {
					fileDialog.setFilterPath(lastDirectory);
				}

				fileDialog.setFilterExtensions(filterExtensions);
				fileDialog.setFilterNames(filterNames);

				if (fileToHandle != null && fileToHandle.isFile()) {
					fileDialog.setFileName(fileToHandle.getName());
				}

			} else {

				fileDialog = new FileDialog(parentShell, SWT.SAVE);

				fileDialog.setText(overrideWindowTitle + ": "
						+ overrideWindowTitleSaveAddition);

				if (fileToHandle != null && fileToHandle.isFile()) {
					fileDialog.setFilterPath(fileToHandle.getParent());
				} else if (fileToHandle != null && fileToHandle.isDirectory()) {
					fileDialog.setFilterPath(fileToHandle.getAbsolutePath());
				} else {
					fileDialog.setFilterPath(lastDirectory);
				}

				fileDialog.setFilterExtensions(filterExtensions);
				fileDialog.setFilterNames(filterNames);

				fileDialog.setOverwrite(true);
			}

			selection = fileDialog.open();

			if (isOpenDialog && isMultiFileDialog)
				openedMultiFileNames = fileDialog.getFileNames();
		}

		if (selection != null) {

			SSDToolUtils.setDialogSettingsLastOpendDirectory(selection,
					isDirectory);

			fileToHandle = new File(selection);
			result = selection;
		}

		return result;
	}

	/**
	 * return the resulting selected file or directory as a {@link File} object
	 * 
	 * @return the resulting {@link File} object, basing on the String, returned
	 *         by the {@link #open()} method
	 */
	public File getFileToHandle() {
		return fileToHandle;
	}

	/**
	 * resturns the selection for multi file {@link FileDialog}s as an array of
	 * {@link File}s
	 * 
	 * @return the result may be a single {@link File}, if the {@link File} was
	 *         used as a single file dialog, but it can also return an array of
	 *         {@link File}s, when the dialog was set to multi file handling.<br>
	 *         all other cases will return <code>null</code>
	 */
	public File[] getMultiFilesToHandle() {

		File[] result = null;

		if (isOpenDialog && !isDirectory) {

			if (isMultiFileDialog) {

				if (fileToHandle != null && !fileToHandle.isDirectory()
						&& openedMultiFileNames != null
						&& openedMultiFileNames.length > 0) {

					File parentFile = fileToHandle.getParentFile();

					result = new File[openedMultiFileNames.length];

					for (int i = 0; i < openedMultiFileNames.length; i++)
						result[i] = new File(parentFile,
								openedMultiFileNames[i]);
				}

			} else {

				if (fileToHandle != null) {
					result = new File[1];
					result[0] = fileToHandle;
				}
			}
		}

		return result;
	}
}
