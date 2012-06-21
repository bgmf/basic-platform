package de.dzimmermann.rcp.bsgtaucha.mgt.ui.dialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.dzimmermann.rcp.basicplatform.model.BasicPlatformSessionModel;
import de.dzimmermann.rcp.basicplatform.ui.util.Logger;
import de.dzimmermann.rcp.basicplatform.ui.util.treewizard.TreeWizard;
import de.dzimmermann.rcp.basicplatform.ui.util.treewizard.TreeWizardPage;
import de.dzimmermann.rcp.basicplatform.util.PFSCoreIconProvider;
import de.dzimmermann.rcp.bsgtaucha.mgt.Activator;
import de.dzimmermann.rcp.bsgtaucha.mgt.handler.OpenEditorHandler;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.localfile.LocalFileModel;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.BSGTauchaConstants;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.BSGTauchaUtils;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.DesEncrypter;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.ODFFile;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.ODFFile.ContentEntry;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.ODFFile.Parameter;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.OpenDocumentCreator;

public class ODFExportTreeWizardDialog extends Dialog {

	/**
	 * This enumeration contains the different types of possible results of this
	 * shell.
	 * 
	 * @author dzimmermann
	 * 
	 */
	public static enum ResultType {
		/**
		 * Default result type and default message type (no image will be
		 * printed).
		 */
		DEFAULT,
		/**
		 * Result type for the OK button.
		 */
		OK,
		/**
		 * Result type for the CANCEL button.
		 */
		CANCEL;
	}

	/**
	 * This enumeration is used to wrap around {@link IMessageProvider}
	 * constants.
	 * 
	 * @author dzimmermann
	 * 
	 */
	public static enum MessageType {
		/**
		 * Default message type: Display no icon.
		 */
		NONE(IMessageProvider.NONE),
		/**
		 * Message type for informations. A little info image will be displayed.
		 */
		INFORMATION(IMessageProvider.INFORMATION),
		/**
		 * Message type for warnings. A little warning image will be displayed.
		 */
		WARNING(IMessageProvider.WARNING),
		/**
		 * Message type for errors. A little error image will be displayed.
		 */
		ERROR(IMessageProvider.ERROR);

		private final int iMessageProviderType;

		private MessageType(int iMessageProviderType) {
			this.iMessageProviderType = iMessageProviderType;
		}

		public int getType() {
			return iMessageProviderType;
		}
	}

	private static final String WINDOW_TITLE = "Datenexport"; //$NON-NLS-1$

	private final FormToolkit formToolkit = new FormToolkit(
			Display.getDefault());

	private final Image okImg;
	private final Image cancelImg;

	private final Image backImg;
	private final Image forwardImg;

	// private final Image infoImg;
	// private final Image warningImg;
	// private final Image errorImg;

	protected ResultType result = ResultType.DEFAULT;
	protected Shell shell;

	private Form form;

	private TreeWizard treeWizard;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public ODFExportTreeWizardDialog(Shell parent, int style) {

		super(parent, style);

		setText(WINDOW_TITLE);

		okImg = PFSCoreIconProvider.getImageByIconName("fugue_tick.png", true); //$NON-NLS-1$
		cancelImg = PFSCoreIconProvider.getImageByIconName("fugue_cross.png", //$NON-NLS-1$
				true);
		backImg = PFSCoreIconProvider.getImageByIconName("fugue_arrow-180.png", //$NON-NLS-1$
				true);
		forwardImg = PFSCoreIconProvider.getImageByIconName(
				"fugue_arrow.png", true); //$NON-NLS-1$
		// infoImg = PFSCoreIconProvider.getImageByIconName("info_obj.gif", true); //$NON-NLS-1$
		// warningImg = PFSCoreIconProvider.getImageByIconName("showwarn_tsk.gif", //$NON-NLS-1$
		// true);
		// errorImg = PFSCoreIconProvider
		// .getImageByIconName("error_obj.gif", true); //$NON-NLS-1$
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public ResultType open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	private static final String FORM_TITLE = "Datenexport"; //$NON-NLS-1$

	public static final String DEFAULT_MESSAGE = "Wählen Sie einen Dokumententyp und ein Ziel"; //$NON-NLS-1$
	public static final String INFO_MESSAGE = ""; //$NON-NLS-1$
	public static final String WARNING_MESSAGE = ""; //$NON-NLS-1$
	public static final String ERROR_MESSAGE = ""; //$NON-NLS-1$

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(720, 560);
		shell.setText(getText());

		shell.setLayout(new FillLayout());

		Composite container = new Composite(shell, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		formToolkit.adapt(container);
		// formToolkit.paintBordersFor(container);

		form = formToolkit.createForm(container);
		formToolkit.decorateFormHeading(form);
		// formToolkit.paintBordersFor(form);
		form.setText(FORM_TITLE);
		form.getBody().setLayout(new FillLayout());

		treeWizard = new InternalTreeWizard(form.getBody(), SWT.NONE);
		treeWizard.setProgressBarVisible(false);
		treeWizard.adaptEclipseForms();

		setMessage(MessageType.NONE, DEFAULT_MESSAGE);

	}

	/**
	 * This method is used to display a message within the message area of the
	 * {@link Shell}s {@link Form}.
	 * 
	 * @param type
	 *            The type of the message.
	 *            <p>
	 *            See
	 *            <ul>
	 *            <li>{@link #DEFAULT}</li>
	 *            <li>{@link #INFO}</li>
	 *            <li>{@link #WARNING}</li>
	 *            <li>{@link #ERROR}</li>
	 *            </ul>
	 *            </p>
	 * @param message
	 *            The message that should be displayed.
	 */
	public void setMessage(MessageType type, String message) {
		form.setMessage(message, type.getType());
	}

	private class InternalTreeWizard extends TreeWizard {

		// root
		private TreeWizardPage pageSelectDocument;

		// simple person document
		private TreeWizardPage pageSelectDestinationSimplePerson;

		// default
		private TreeWizardPage pageSelectSetUp;
		private TreeWizardPage pageSelectDestination;

		public InternalTreeWizard(Composite parent, int style) {
			super(parent, style);
		}

		@Override
		protected void createButtons() {
			Button button = createButton(ButtonType.OK);
			button.setText("Fertigstellen"); //$NON-NLS-1$
			button.setImage(okImg);
			button = createButton(ButtonType.CANCEL);
			button.setText("Abbrechen"); //$NON-NLS-1$
			button.setImage(cancelImg);
			button = createButton(ButtonType.NEXT);
			button.setText("Weiter"); //$NON-NLS-1$
			button.setImage(forwardImg);
			button = createButton(ButtonType.BACK);
			button.setText("Zurück"); //$NON-NLS-1$
			button.setImage(backImg);
			refreshButtons();
		}

		@Override
		protected void okPressed() {

			ODFFile odfFile = (ODFFile) pageSelectDocument
					.getInternalAdapter(ODFFile.class);

			if (odfFile == null) {
				MessageDialog
						.openWarning(shell, "Export Fehlgeschlagen!",
								"Es konnte nicht festgestellt werden, was Sie exportieren wollen.");
				returnWithDefaults();
			}

			Map<String, Object> pluginModels = BasicPlatformSessionModel
					.getInstance().getPluginModels();
			LocalFileModel model = (LocalFileModel) pluginModels
					.get(Activator.PLUGIN_ID);
			String defPW = (String) pluginModels.get(Activator.PLUGIN_ID
					+ BSGTauchaConstants.DEFAULT_PASSWORD_SUFFIX);
			// if (defPW != null) {
			OpenEditorHandler.desEncrypter = DesEncrypter.getInstance(defPW);
			// }
			if (model != null) {
				try {
					model.setModel(BSGTauchaUtils.openModel(model
							.getFileStore().toLocalFile(EFS.NONE, null)));
				} catch (CoreException e) {
					Logger.logError(e);
				} catch (FileNotFoundException e) {
					Logger.logError(e);
				} catch (IOException e) {
					Logger.logError(e);
				} catch (JAXBException e) {
					Logger.logError(e);
				}
			}

			switch (odfFile) {
			case PERSON_SIMPLE:
				if (!handleSimplePerson(model)) {
					MessageDialog
							.openWarning(shell, "Export Fehlgeschlagen!",
									"Der Export ist Fehlgeschlagen. Im Log finden Sie möglicherweise die Ursache.");
					returnWithDefaults();
				}
				break;

			case BAND_ACTIONS_SIMPLE:
				if (!handleSimpleBandActions(model)) {
					MessageDialog
							.openWarning(shell, "Export Fehlgeschlagen!",
									"Der Export ist Fehlgeschlagen. Im Log finden Sie möglicherweise die Ursache.");
					returnWithDefaults();
				}
				break;
			case BAND_ACTIONS_COMPLEX:
				if (!handleComplexBandActions(model)) {
					MessageDialog
							.openWarning(shell, "Export Fehlgeschlagen!",
									"Der Export ist Fehlgeschlagen. Im Log finden Sie möglicherweise die Ursache.");
					returnWithDefaults();
				}
				break;
			case ENTRIES_SIMPLE:
				if (!handleSimpleEntries(model)) {
					MessageDialog
							.openWarning(shell, "Export Fehlgeschlagen!",
									"Der Export ist Fehlgeschlagen. Im Log finden Sie möglicherweise die Ursache.");
					returnWithDefaults();
				}
				break;
			}

			result = ResultType.OK;
			ODFExportTreeWizardDialog.this.shell.dispose();
		}

		private void returnWithDefaults() {
			result = ResultType.DEFAULT;
			ODFExportTreeWizardDialog.this.shell.dispose();
		}

		private boolean exportData(File exportModel, File targetFile,
				ODFFile odfFile) {

			FileOutputStream fos = null;
			try {
				Map<String, InputStream> contentEntries = new HashMap<String, InputStream>();
				for (ContentEntry ce : odfFile.getContentEntries()) {
					contentEntries.put(ce.getEntryName(), ce.getXslt());
				}
				Map<String, String> parameters = new HashMap<String, String>();
				for (Parameter p : odfFile.getParameters()) {
					parameters.put(p.getParameter(), p.getValue());
				}
				fos = new FileOutputStream(targetFile);
				if (odfFile.isComplex()) {
					OpenDocumentCreator.createODFFile(odfFile.getTemplate(),
							targetFile, contentEntries, new FileInputStream(
									exportModel), parameters);
				} else {
					OpenDocumentCreator.writeXmlDocument(
							odfFile.getContentEntries()[0].getXslt(),
							exportModel, new FileOutputStream(targetFile),
							parameters, "windows-1252");
				}
			} catch (Exception e) {
				Logger.logError(e);
				return false;
			} finally {
				if (exportModel != null) {
					// exportModel.delete();
				}
				if (fos != null) {
					try {
						fos.flush();
						fos.close();
					} catch (IOException e) {
					}
				}
			}
			return true;
		}

		private boolean handleSimplePerson(LocalFileModel model) {
			ODFFile odfFile = ODFFile.PERSON_SIMPLE;
			String target = (String) pageSelectDestinationSimplePerson
					.getInternalAdapter(String.class);
			if (target == null) {
				String msg = "Keine Datei angegeben für " + odfFile.getName();
				Logger.logWarning(msg);
				return false;
			}
			File targetFile = new File(target);
			File exportModel = null;
			try {
				exportModel = OpenDocumentCreator.extractModelDataIntoFile(
						model.getModel(), odfFile);
			} catch (Exception e) {
				Logger.logError(e);
				return false;
			}
			return exportData(exportModel, targetFile, odfFile);
		}

		private boolean handleComplexBandActions(LocalFileModel model) {
			ODFFile odfFile = ODFFile.BAND_ACTIONS_COMPLEX;
			String target = (String) pageSelectDestination
					.getInternalAdapter(String.class);
			if (target == null) {
				String msg = "Keine Datei angegeben für " + odfFile.getName();
				Logger.logWarning(msg);
				return false;
			}
			File targetFile = new File(target);
			File exportModel = null;
			try {
				exportModel = OpenDocumentCreator.extractModelDataIntoFile(
						model.getModel(), odfFile);
			} catch (Exception e) {
				Logger.logError(e);
				return false;
			}
			return exportData(exportModel, targetFile, odfFile);
		}

		private boolean handleSimpleBandActions(LocalFileModel model) {
			ODFFile odfFile = ODFFile.BAND_ACTIONS_SIMPLE;
			String target = (String) pageSelectDestination
					.getInternalAdapter(String.class);
			if (target == null) {
				String msg = "Keine Datei angegeben für " + odfFile.getName();
				Logger.logWarning(msg);
				return false;
			}
			File targetFile = new File(target);
			File exportModel = null;
			try {
				exportModel = OpenDocumentCreator.extractModelDataIntoFile(
						model.getModel(), odfFile);
			} catch (Exception e) {
				Logger.logError(e);
				return false;
			}
			return exportData(exportModel, targetFile, odfFile);
		}

		private boolean handleSimpleEntries(LocalFileModel model) {
			ODFFile odfFile = ODFFile.ENTRIES_SIMPLE;
			String target = (String) pageSelectDestination
					.getInternalAdapter(String.class);
			if (target == null) {
				String msg = "Keine Datei angegeben für " + odfFile.getName();
				Logger.logWarning(msg);
				return false;
			}
			File targetFile = new File(target);
			File exportModel = null;
			try {
				exportModel = OpenDocumentCreator.extractModelDataIntoFile(
						model.getModel(), odfFile);
			} catch (Exception e) {
				Logger.logError(e);
				return false;
			}
			return exportData(exportModel, targetFile, odfFile);
		}

		@Override
		protected void cancelPressed() {
			result = ResultType.CANCEL;
			ODFExportTreeWizardDialog.this.shell.dispose();
		}

		@Override
		protected void helpPressed() {
			// no help available ATM
		}

		@Override
		protected void addPages() {

			pageSelectDocument = new ODFExportTWPSelectType(this,
					ODFExportTWPSelectType.ID, "Dokumententyp wählen", false);

			pageSelectDestinationSimplePerson = new ODFExportTWPTarget(this,
					ODFExportTWPTarget.ID_SIMPLE_PERSON,
					"Wählen Sie ein Ziel zum Speichern", false);

			pageSelectSetUp = new ODFExportTWPSetUp(this, ODFExportTWPSetUp.ID,
					"Stellen Sie den Export ein", false);
			pageSelectDestination = new ODFExportTWPTarget(this,
					ODFExportTWPTarget.ID, "Wählen Sie ein Ziel zum Speichern",
					false);

			addPage(null, pageSelectDocument);

			addPage(pageSelectDocument, pageSelectDestinationSimplePerson);

			// addPage(pageSelectDocument, pageSelectSetUp);
			// addPage(pageSelectSetUp, pageSelectDestination);
			addPage(pageSelectDocument, pageSelectDestination);
		}

		@Override
		public Object getInternalAdapter(Class<?> adapter) {
			if (ODFExportTreeWizardDialog.class.isAssignableFrom(adapter)) {
				return ODFExportTreeWizardDialog.this;
			}
			return super.getInternalAdapter(adapter);
		}
	}
}
