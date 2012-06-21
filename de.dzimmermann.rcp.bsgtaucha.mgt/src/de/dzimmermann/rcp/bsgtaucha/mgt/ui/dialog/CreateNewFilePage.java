package de.dzimmermann.rcp.bsgtaucha.mgt.ui.dialog;

import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import de.dzimmermann.rcp.basicplatform.ui.util.Logger;
import de.dzimmermann.rcp.basicplatform.ui.util.treewizard.TreeWizard;
import de.dzimmermann.rcp.basicplatform.ui.util.treewizard.TreeWizardDialog;
import de.dzimmermann.rcp.basicplatform.ui.util.treewizard.TreeWizardDialog.MessageType;
import de.dzimmermann.rcp.basicplatform.ui.util.treewizard.TreeWizardFocusEvent;
import de.dzimmermann.rcp.basicplatform.ui.util.treewizard.TreeWizardFocusListener;
import de.dzimmermann.rcp.basicplatform.ui.util.treewizard.TreeWizardPage;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.ObjectFactory;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.RootType;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.localfile.LocalFileModel;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.BSGTauchaConstants;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.BSGTauchaUtils;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.DesEncrypter;

public class CreateNewFilePage extends TreeWizardPage {

	public static final String ID = CreateNewFilePage.class.getName();

	private FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	private TreeWizardDialog wizardDialog;

	public CreateNewFilePage(TreeWizard wizard, String pageId, String pageName,
			boolean canProceed) {
		super(wizard, pageId, pageName, canProceed);
		this.formToolkit = (FormToolkit) wizard
				.getInternalAdapter(FormToolkit.class);
		wizardDialog = ((TreeWizardDialog) getWizard().getInternalAdapter(
				TreeWizardDialog.class));
	}

	private Composite container;

	private Text fileNameText;
	private Button defaultPasswordButton;
	private Text defaultPasswordText;
	private Button advandedPasswordButton;
	private Text advancedPasswordText;

	private NewFileResult result = new NewFileResult();

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	protected Composite createContent(Composite pageStack) {

		container = new Composite(pageStack, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite composite = formToolkit.createComposite(container, SWT.NONE);
		formToolkit.paintBordersFor(container);
		composite.setLayout(new GridLayout(1, false));

		Section newFileSection = formToolkit.createSection(composite,
				Section.TITLE_BAR);
		newFileSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		formToolkit.paintBordersFor(newFileSection);
		newFileSection.setText(getPageName());

		Composite contentComposite = formToolkit.createComposite(
				newFileSection, SWT.NONE);
		formToolkit.paintBordersFor(contentComposite);
		newFileSection.setClient(contentComposite);
		contentComposite.setLayout(new GridLayout(2, false));

		Label newFileNameLabel = new Label(contentComposite, SWT.NONE);
		newFileNameLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false, 2, 1));
		formToolkit.adapt(newFileNameLabel, true, true);
		newFileNameLabel.setText("Geben Sie den Namen der neuen Datei ein");

		fileNameText = new Text(contentComposite, SWT.NONE | SWT.BORDER);
		fileNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 2, 1));
		formToolkit.adapt(fileNameText, true, true);

		Composite compositeSeparator1 = formToolkit
				.createCompositeSeparator(contentComposite);
		GridData gd_compositeSeparator1 = new GridData(SWT.FILL, SWT.BOTTOM,
				false, true, 2, 1);
		gd_compositeSeparator1.heightHint = 10;
		compositeSeparator1.setLayoutData(gd_compositeSeparator1);
		formToolkit.paintBordersFor(compositeSeparator1);

		Label optionsLabel = formToolkit.createLabel(contentComposite,
				"weitere Optionen", SWT.CENTER);
		optionsLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 2, 1));

		Composite compositeSeparator2 = formToolkit
				.createCompositeSeparator(contentComposite);
		GridData gd_compositeSeparator2 = new GridData(SWT.FILL, SWT.CENTER,
				false, false, 2, 1);
		gd_compositeSeparator2.heightHint = 10;
		compositeSeparator2.setLayoutData(gd_compositeSeparator2);
		formToolkit.paintBordersFor(compositeSeparator2);

		defaultPasswordButton = new Button(contentComposite, SWT.CHECK);
		formToolkit.adapt(defaultPasswordButton, true, true);
		defaultPasswordButton.setText("Inhalt verschlüsseln");

		defaultPasswordText = new Text(contentComposite, SWT.PASSWORD
				| SWT.BORDER);
		defaultPasswordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		formToolkit.adapt(defaultPasswordText, true, true);

		advandedPasswordButton = new Button(contentComposite, SWT.CHECK);
		formToolkit.adapt(advandedPasswordButton, true, true);
		advandedPasswordButton.setText("Personendaten freigeben");

		advancedPasswordText = new Text(contentComposite, SWT.PASSWORD
				| SWT.BORDER);
		advancedPasswordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		formToolkit.adapt(advancedPasswordText, true, true);

		ModifyListener textListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (e.widget == fileNameText) {
					result.fileName = fileNameText.getText();
				} else if (e.widget == defaultPasswordText) {
					if (defaultPasswordText.getText().isEmpty())
						result.defaultPassword = null;
					else
						result.defaultPassword = defaultPasswordText.getText();
				} else if (e.widget == advancedPasswordText) {
					if (advancedPasswordText.getText().isEmpty())
						result.advancedPassword = null;
					else
						result.advancedPassword = advancedPasswordText
								.getText();
				}
				canProceed(checkPageContent());
			}
		};
		fileNameText.addModifyListener(textListener);
		defaultPasswordText.addModifyListener(textListener);
		advancedPasswordText.addModifyListener(textListener);

		SelectionAdapter buttonListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.widget == defaultPasswordButton) {
					defaultPasswordText.setEnabled(defaultPasswordButton
							.getSelection());
					result.defaultPassword = defaultPasswordButton
							.getSelection() ? !defaultPasswordText.getText()
							.isEmpty() ? defaultPasswordText.getText() : null
							: null;
				} else if (e.widget == advandedPasswordButton) {
					advancedPasswordText.setEnabled(advandedPasswordButton
							.getSelection());
					result.advancedPassword = advandedPasswordButton
							.getSelection() ? !advancedPasswordText.getText()
							.isEmpty() ? advancedPasswordText.getText() : null
							: null;
				}
				canProceed(checkPageContent());
			}
		};
		defaultPasswordButton.addSelectionListener(buttonListener);
		advandedPasswordButton.addSelectionListener(buttonListener);

		defaultPasswordText.setEnabled(defaultPasswordButton.getSelection());
		advancedPasswordText.setEnabled(advandedPasswordButton.getSelection());

		TreeWizardFocusListener focusListener = new TreeWizardFocusListener() {
			@Override
			public void focusChanged(TreeWizardFocusEvent event) {
				if ((OpenExistingFilePage.ID + ".2").equals(event
						.getFocusedTreeWizardPage().getPageId())
						&& ID.equals(event.getLastTreeWizardPage().getPageId())) {
					IFileStore workspace = BSGTauchaUtils
							.getWorkspaceLocation();
					String appendix = !CreateNewFilePage.this.result.fileName
							.toLowerCase().endsWith(
									BSGTauchaConstants.MODEL_EXTENSION) ? BSGTauchaConstants.MODEL_EXTENSION
							: "";
					IFileStore child = workspace
							.getChild(CreateNewFilePage.this.result.fileName
									+ appendix);
					try {
						RootType root = new ObjectFactory().createRootType();
						if (result.defaultPassword != null) {
							root.setDefaultPw(DesEncrypter
									.getDigest(result.defaultPassword));
						}
						if (result.advancedPassword != null) {
							root.setAdvancedPw(DesEncrypter
									.getDigest(result.advancedPassword));
						}
						BSGTauchaUtils.saveModel(root,
								child.toLocalFile(EFS.NONE, null));
						((OpenExistingFilePage) event
								.getFocusedTreeWizardPage())
								.lazyLoadedPageContent();
					} catch (Exception e1) {
						Logger.logError(e1);
						if (wizardDialog != null) {
							wizardDialog.setMessage(MessageType.ERROR,
									ERROR_COULD_NOT_CREATE_FILE);
						}
					}
				}
			}
		};
		wizard.addFocusListener(focusListener);

		return container;
	}

	@Override
	protected Composite getTopComposite() {
		return container;
	}

	private static final String ERROR_COULD_NOT_CREATE_FILE = "Die Datei konnte nicht erstellt werden!";

	private static final String ERROR_NO_FILE_NAME = "Bitte geben Sie eine Namen für die Datei an!";
	private static final String ERROR_FILE_NAME_PRESENT = "Eine Datei mit dem Namen existiert bereits!";
	private static final String WARNING_NO_DEFAULT_PASSWORD = "Die Daten werden nicht verschlüsselt gespeichert!";

	@Override
	protected boolean checkPageContent() {

		if (wizardDialog == null)
			return false;

		if (result.fileName == null || result.fileName.isEmpty()) {
			wizardDialog.setMessage(MessageType.ERROR, ERROR_NO_FILE_NAME);
			return false;
		}

		Set<LocalFileModel> currentData = getModelData();
		for (LocalFileModel lfm : currentData) {
			if (result.fileName.equals(lfm.getFileName())
					|| (result.fileName + BSGTauchaConstants.MODEL_EXTENSION)
							.equals(lfm.getFileName())) {
				wizardDialog.setMessage(MessageType.ERROR,
						ERROR_FILE_NAME_PRESENT);
				return false;
			}
		}

		if (result.defaultPassword == null) {
			wizardDialog.setMessage(MessageType.WARNING,
					WARNING_NO_DEFAULT_PASSWORD);
		}

		return true;
	}

	private Set<LocalFileModel> getModelData() {

		Set<LocalFileModel> localModelSet = new TreeSet<LocalFileModel>();
		try {
			IFileStore[] children = BSGTauchaUtils.getWorkspaceLocation()
					.childStores(EFS.NONE, null);
			for (IFileStore child : children) {
				if (child.fetchInfo().isDirectory()) {
					continue;
				}
				if (!child.fetchInfo().getName()
						.endsWith(BSGTauchaConstants.MODEL_EXTENSION)) {
					continue;
				}
				RootType model = null;
				try {
					model = BSGTauchaUtils.openModel(child.toLocalFile(
							EFS.NONE, null));
				} catch (Exception e) {
					String message = String
							.format("File %s could not be loaded - it was potentially not a correct model file (message: %s)", //$NON-NLS-1$
									child.toURI(), e);
					Logger.logError(message, e);
					System.err.println(message);
				}
				if (model != null) {
					LocalFileModel local = new LocalFileModel(child,
							child.getName(), child.fetchInfo()
									.getLastModified(), model);
					localModelSet.add(local);
				}
			}
		} catch (CoreException e) {
			Logger.logError(e);
			e.printStackTrace();
		}

		return localModelSet;
	}

	public NewFileResult getResult() {
		return result;
	}

	protected class NewFileResult {

		protected String fileName;
		protected String defaultPassword;
		protected String advancedPassword;
	}
}
