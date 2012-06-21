package de.dzimmermann.rcp.bsgtaucha.mgt.ui.dialog;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.dzimmermann.rcp.basicplatform.ui.util.treewizard.TreeWizard;
import de.dzimmermann.rcp.basicplatform.ui.util.treewizard.TreeWizardDialog;
import de.dzimmermann.rcp.basicplatform.util.InternalAdapter;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.localfile.LocalFileModel;
import de.dzimmermann.rcp.bsgtaucha.mgt.ui.dialog.SelectOpenTypePage.OpenType;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.DesEncrypter;

public class LoadModelDialog extends TreeWizardDialog {

	public LoadModelDialog(Shell parent, int style) {
		super(parent, style);
	}

	@Override
	public String getWindowTitle() {
		return "Lade Daten";
	}

	@Override
	public String getFormTitle() {
		return "Lade eine Datei mit Aufgabendaten";
	}

	@Override
	public Point getInitialSize() {
		return new Point(600, 400);
	}

	@Override
	public Rectangle getInitialBounds() {
		return null;
	}

	@Override
	public TreeWizard getInternalTreeWizard(Composite parent) {
		return new InternalTreeWizard(parent, SWT.NONE);
	}

	private LocalFileModel localFile;
	private String defaultPassword;
	private String advancedPassword;

	public LocalFileModel getLocalFile() {
		return localFile;
	}

	public String getDefaultPassword() {
		return defaultPassword;
	}

	public String getAdvancedPassword() {
		return advancedPassword;
	}

	private final class InternalTreeWizard extends TreeWizard implements
			InternalAdapter {

		public InternalTreeWizard(Composite parent, int style) {
			super(parent, style);
		}

		@Override
		protected void createButtons() {
			Button button = createButton(ButtonType.OK);
			button.setText("Fertigstellen");
			button.setImage(getOkImg());
			button = createButton(ButtonType.CANCEL);
			button.setText("Abbrechen");
			button.setImage(getCancelImg());
			button = createButton(ButtonType.NEXT);
			button.setText("Weiter");
			button.setImage(getForwardImg());
			button = createButton(ButtonType.BACK);
			button.setText("Zurück");
			button.setImage(getBackImg());
			refreshButtons();
		}

		private SelectOpenTypePage root;
		private OpenExistingFilePage openFile;
		private CreateNewFilePage newFile;
		private OpenExistingFilePage openNewFile;

		@Override
		protected void addPages() {

			root = new SelectOpenTypePage(this, SelectOpenTypePage.ID,
					"Typ wählen", false);
			openFile = new OpenExistingFilePage(this, OpenExistingFilePage.ID,
					"Öffne bestehende Datei", false);
			newFile = new CreateNewFilePage(this, CreateNewFilePage.ID,
					"Neue Datei anlegen", false);
			openNewFile = new OpenExistingFilePage(this,
					OpenExistingFilePage.ID + ".2", "Öffne bestehende Datei",
					false);

			// TODO add wizard pages
			addPage(null, root);
			addPage(root, openFile);
			addPage(root, newFile);
			addPage(newFile, openNewFile);

			newFile.setTargetPageID(openNewFile.getPageId());
		}

		@Override
		protected void okPressed() {

			OpenType openType = (OpenType) root
					.getInternalAdapter(OpenType.class);

			if (openType == null)
				returnWithDefaults();

			defaultPassword = null;
			advancedPassword = null;

			switch (openType) {
			case OPEN_EXISTING:

				LocalFileModel localFileModel = (LocalFileModel) openFile
						.getInternalAdapter(LocalFileModel.class);

				if (localFileModel == null)
					return;
				if (localFileModel.getModel() == null)
					return;

				localFile = localFileModel;

				boolean hasDefPW = (localFileModel.getModel().getDefaultPw() != null && !localFileModel
						.getModel().getDefaultPw().isEmpty());
				boolean hasAdvPW = (localFileModel.getModel().getAdvancedPw() != null && !localFileModel
						.getModel().getAdvancedPw().isEmpty());

				if (!hasDefPW && !hasAdvPW) {
					defaultPassword = null;
					advancedPassword = null;
					break; // end of case OPEN_EXISTING
				}
				if (!hasDefPW)
					defaultPassword = null;
				if (!hasAdvPW)
					advancedPassword = null;

				PasswordDialog dialog = new PasswordDialog(getShell(),
						hasDefPW, hasAdvPW);

				int result = dialog.open();
				if (PasswordDialog.OK == result) {
					String defPw = dialog.getDefaultPassword();
					String advPw = dialog.getAdvancedPassword();
					if (localFileModel.getModel().getDefaultPw() != null
							&& DesEncrypter.isEqualPassPhrase(localFileModel
									.getModel().getDefaultPw(), defPw)) {
						localFile = localFileModel;
						defaultPassword = defPw;
					} else {
						MessageDialog.openError(getShell(),
								"Falsches Passwort",
								"Das Passwort für die Verschlüsselung der "
										+ "Daten stimmt nicht mit dem der "
										+ "zu ladenden Datei überein!");
						return;
					}
					if (localFileModel.getModel().getAdvancedPw() != null
							&& DesEncrypter.isEqualPassPhrase(localFileModel
									.getModel().getAdvancedPw(), advPw)) {
						localFile = localFileModel;
						advancedPassword = advPw;
					} else {
						MessageDialog.openWarning(getShell(),
								"Falsches Passwort",
								"Das Passwort für die Freigabe personenbezogener "
										+ "Daten stimmt nicht mit dem der zu "
										+ "ladenden Datei überein!");
						advancedPassword = null;
						break;
					}
				} else {
					return;
				}

				break; // end of case case OPEN_EXISTING

			case CREATE_NEW:
				break;
			}

			close(ResultType.OK);
		}

		private void returnWithDefaults() {
			close();
		}

		@Override
		protected void cancelPressed() {
			close(ResultType.CANCEL);
		}

		@Override
		protected void helpPressed() {
		}

		@Override
		public Object getInternalAdapter(Class<?> adapter) {
			if (TreeWizardDialog.class.isAssignableFrom(adapter))
				return LoadModelDialog.this;
			if (FormToolkit.class.isAssignableFrom(adapter))
				return LoadModelDialog.this.getFormToolkit();
			if (LoadModelDialog.class.isAssignableFrom(adapter))
				return LoadModelDialog.this;
			return super.getInternalAdapter(adapter);
		}
	}
}
