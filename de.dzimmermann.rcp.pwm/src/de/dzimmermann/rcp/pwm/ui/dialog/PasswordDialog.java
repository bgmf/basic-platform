package de.dzimmermann.rcp.pwm.ui.dialog;

import javax.xml.bind.JAXBException;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.dzimmermann.rcp.pwm.model.container.PWMContainerGroup;
import de.dzimmermann.rcp.pwm.model.content.PWMGroup;
import de.dzimmermann.rcp.pwm.util.PWMUtils;

public class PasswordDialog extends TitleAreaDialog {

	private static final String WINDOW_TITLE = "PWM - Enter Password";
	private static final String AREA_TITLE = "Enter Password";
	private static final String AREA_MESSAGE = "Enter the password for the current group.";

	private final PWMContainerGroup group;

	private String password = null;

	public PasswordDialog(Shell parentShell, PWMContainerGroup group) {
		super(parentShell);
		this.group = group;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(WINDOW_TITLE);
	}

	public String getPassword() {
		return password;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		container.setLayout(new GridLayout(2, false));

		final Label label = new Label(container, SWT.NONE);
		label.setText("Password:");
		label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		final Text text = new Text(container, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		text.setEchoChar('*');

		new Label(container, SWT.NONE);

		final Button button = new Button(container, SWT.CHECK);
		button.setText("Show Password");
		button.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));

		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				PWMGroup group = null;
				try {
					group = PWMUtils.loadPWMGroup(PasswordDialog.this.group,
							text.getText());
					password = text.getText();
				} catch (JAXBException e1) {
				}
				getButton(IDialogConstants.OK_ID).setEnabled(group != null);
			}
		});

		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				text.setEchoChar(button.getSelection() ? '\0' : '*');
			}
		});

		setTitle(AREA_TITLE);
		setMessage(AREA_MESSAGE);

		return area;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 225);
	}

}
