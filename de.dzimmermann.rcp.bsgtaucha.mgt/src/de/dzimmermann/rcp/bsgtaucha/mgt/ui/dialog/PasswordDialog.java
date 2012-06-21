package de.dzimmermann.rcp.bsgtaucha.mgt.ui.dialog;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class PasswordDialog extends Dialog {

	private final FormToolkit formToolkit = new FormToolkit(
			Display.getDefault());

	private final boolean hasDefaultPassword;
	private final boolean hasAdvancedPassword;

	private Text defaultPWText;
	private Text advancedPWText;

	private String defaultPassword;
	private String advancedPassword;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public PasswordDialog(Shell parentShell, boolean hasDefaultPassword,
			boolean hasAdvancedPassword) {
		super(parentShell);
		this.hasDefaultPassword = hasDefaultPassword;
		this.hasAdvancedPassword = hasAdvancedPassword;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		getShell().setText("Passwort eingeben");

		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		Form pwForm = formToolkit.createForm(container);
		formToolkit.paintBordersFor(pwForm);
		formToolkit.decorateFormHeading(pwForm);
		pwForm.setText("Passwortfreigabe nötig!");
		pwForm.getBody().setLayout(new GridLayout(1, false));

		Label defaultPWLabel = formToolkit.createLabel(pwForm.getBody(),
				"Passwort für die Verschlüsselung", SWT.NONE);
		defaultPWLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		defaultPWText = formToolkit.createText(pwForm.getBody(), "New Text",
				SWT.BORDER | SWT.PASSWORD);
		defaultPWText.setText("");
		defaultPWText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label label = new Label(pwForm.getBody(), SWT.SEPARATOR
				| SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));
		formToolkit.adapt(label, true, true);

		Label advancedPWLabel = formToolkit.createLabel(pwForm.getBody(),
				"Passwort für die Sichtbarkeit der Persondendaten", SWT.NONE);

		advancedPWText = formToolkit.createText(pwForm.getBody(), "New Text",
				SWT.BORDER | SWT.PASSWORD);
		advancedPWText.setText("");
		advancedPWText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		defaultPWText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				defaultPassword = defaultPWText.getText();
			}
		});
		advancedPWText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				advancedPassword = advancedPWText.getText();
			}
		});

		if (!hasDefaultPassword) {
			defaultPWLabel.setEnabled(false);
			defaultPWText.setEnabled(false);
		}
		if (!hasAdvancedPassword) {
			advancedPWLabel.setEnabled(false);
			advancedPWText.setEnabled(false);
		}

		return container;
	}

	public String getDefaultPassword() {
		return defaultPassword;
	}

	public String getAdvancedPassword() {
		return advancedPassword;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 250);
	}
}
