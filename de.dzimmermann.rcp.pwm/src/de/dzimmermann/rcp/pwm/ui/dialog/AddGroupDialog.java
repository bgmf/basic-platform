package de.dzimmermann.rcp.pwm.ui.dialog;

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

import de.dzimmermann.rcp.pwm.model.container.PWMContainer;
import de.dzimmermann.rcp.pwm.model.container.PWMContainerGroup;

public class AddGroupDialog extends TitleAreaDialog {

	private static final String WINDOW_TITLE = "PWM - Add Group";
	private static final String AREA_TITLE = "Add a new Group";
	private static final String AREA_MESSAGE = "Specify a name and a password for a new group.";

	private Text nameText;

	private Object container;

	private String groupName = null;
	private String password = null;
	private Text passwordText;

	public AddGroupDialog(Shell parentShell, Object container) {
		super(parentShell);
		this.container = container;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(WINDOW_TITLE);
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		container.setLayout(new GridLayout(2, false));

		Label newFileNameLabel = new Label(container, SWT.NONE);
		newFileNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		newFileNameLabel.setText("Group Name:");

		nameText = new Text(container, SWT.BORDER);
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		Label passwordLabel = new Label(container, SWT.NONE);
		passwordLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		passwordLabel.setText("Password:");

		passwordText = new Text(container, SWT.BORDER);
		passwordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		new Label(container, SWT.NONE);

		final Button showPWButton = new Button(container, SWT.CHECK);
		showPWButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		showPWButton.setText("Show Password");

		nameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				String t = nameText.getText();
				boolean inUse = false;
				if (AddGroupDialog.this.container == null) {
				} else if (AddGroupDialog.this.container instanceof PWMContainer) {
					for (PWMContainerGroup g : ((PWMContainer) AddGroupDialog.this.container)
							.getGroups()) {
						if (g.getName().equals(t)) {
							inUse = true;
							break;
						}
					}
				} else if (AddGroupDialog.this.container instanceof PWMContainerGroup) {
					for (PWMContainerGroup g : ((PWMContainerGroup) AddGroupDialog.this.container)
							.getGroups()) {
						if (g.getName().equals(t)) {
							inUse = true;
							break;
						}
					}
				}
				if (!inUse && t != null && !t.isEmpty()) {
					getButton(IDialogConstants.OK_ID).setEnabled(true);
					groupName = t;
				} else {
					getButton(IDialogConstants.OK_ID).setEnabled(false);
					groupName = null;
				}
			}
		});
		passwordText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				password = passwordText.getText();
			}
		});

		passwordText.setEchoChar('*');
		showPWButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				passwordText.setEchoChar(showPWButton.getSelection() ? '\0'
						: '*');
			}
		});

		setTitle(AREA_TITLE);
		setMessage(AREA_MESSAGE);

		return container;
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

		getButton(IDialogConstants.OK_ID).setEnabled(false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(392, 250);
	}

	public String getGroupName() {
		return groupName;
	}

	public String getPassword() {
		return password;
	}
}
