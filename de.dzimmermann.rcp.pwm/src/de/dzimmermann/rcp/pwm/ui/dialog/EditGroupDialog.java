package de.dzimmermann.rcp.pwm.ui.dialog;

import javax.xml.bind.JAXBException;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.dzimmermann.rcp.pwm.model.container.PWMContainer;
import de.dzimmermann.rcp.pwm.model.container.PWMContainerGroup;
import de.dzimmermann.rcp.pwm.model.content.PWMGroup;
import de.dzimmermann.rcp.pwm.util.PWMUtils;

public class EditGroupDialog extends TitleAreaDialog {

	private static final String WINDOW_TITLE = "PWM - Edit Group";
	private static final String AREA_TITLE = "Edit a Group";
	private static final String AREA_MESSAGE = "Enter the password to enable modifications to the group.";
	private static final String AREA_MESSAGE_PW = "The new passwords are not equal!";

	private final Object container;
	private final PWMContainerGroup containerGroup;

	private Text enterPasswordText;
	private Label nameLabel;
	private Text nameText;
	private Label passwordLabel;
	private Text passwordText;
	private Label repeatPasswordLabel;
	private Text repeatPasswordText;

	private String currentPassword = "";
	private String groupName = null;
	private String groupPassword = null;

	private boolean nameReady = true;
	private boolean pwReady = true;

	public EditGroupDialog(Shell parentShell, Object container,
			PWMContainerGroup containerGroup) {
		super(parentShell);
		this.container = container;
		this.containerGroup = containerGroup;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(WINDOW_TITLE);
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public String getGroupName() {
		return groupName;
	}

	public String getGroupPassword() {
		return groupPassword;
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		container.setLayout(new GridLayout(2, false));

		Label enterPasswordLabel = new Label(container, SWT.NONE);
		enterPasswordLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		enterPasswordLabel.setText("Enter Password:");

		enterPasswordText = new Text(container, SWT.BORDER);
		enterPasswordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		enterPasswordText.setEchoChar('*');

		final Label sepLabel = new Label(container, SWT.SEPARATOR
				| SWT.HORIZONTAL | SWT.CENTER);
		sepLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				2, 1));

		nameLabel = new Label(container, SWT.NONE);
		nameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		nameLabel.setText("Name:");

		nameText = new Text(container, SWT.BORDER);
		nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		nameText.setText(containerGroup.getName());

		passwordLabel = new Label(container, SWT.NONE);
		passwordLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		passwordLabel.setText("Password:");

		passwordText = new Text(container, SWT.BORDER);
		passwordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		passwordText.setEchoChar('*');
		passwordText.setText("******");

		repeatPasswordLabel = new Label(container, SWT.NONE);
		repeatPasswordLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		repeatPasswordLabel.setText("Repeat Password:");

		repeatPasswordText = new Text(container, SWT.BORDER);
		repeatPasswordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		repeatPasswordText.setEchoChar('*');
		repeatPasswordText.setText("******");

		enterPasswordText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				PWMGroup group = null;
				try {
					group = PWMUtils.loadPWMGroup(containerGroup,
							enterPasswordText.getText());
					currentPassword = enterPasswordText.getText();
				} catch (JAXBException e1) {
				}
				enableWidgets(group != null);
			}
		});

		nameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				String t = nameText.getText();
				boolean inUse = false;
				if (EditGroupDialog.this.container == null) {
				} else if (EditGroupDialog.this.container instanceof PWMContainer) {
					for (PWMContainerGroup g : ((PWMContainer) EditGroupDialog.this.container)
							.getGroups()) {
						if (g.getName().equals(t)) {
							inUse = true;
							break;
						}
					}
				} else if (EditGroupDialog.this.container instanceof PWMContainerGroup) {
					for (PWMContainerGroup g : ((PWMContainerGroup) EditGroupDialog.this.container)
							.getGroups()) {
						if (g.getName().equals(t)) {
							inUse = true;
							break;
						}
					}
				}
				if (!inUse && t != null && !t.isEmpty()) {
					nameReady = true;
					groupName = t;
				} else {
					nameReady = false;
					groupName = null;
				}
				getButton(IDialogConstants.OK_ID).setEnabled(
						pwReady && nameReady);
			}
		});

		ModifyListener ml = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				String one = passwordText.getText();
				String two = repeatPasswordText.getText();
				if (!one.equals(two)) {
					setMessage(AREA_MESSAGE_PW, IMessageProvider.ERROR);
					groupPassword = null;
					pwReady = false;
				} else {
					setMessage(AREA_MESSAGE, IMessageProvider.NONE);
					groupPassword = one;
					pwReady = true;
				}
				getButton(IDialogConstants.OK_ID).setEnabled(
						pwReady && nameReady);
			}
		};
		passwordText.addModifyListener(ml);
		repeatPasswordText.addModifyListener(ml);

		enableWidgets(false);

		setTitle(AREA_TITLE);
		setMessage(AREA_MESSAGE);

		return area;
	}

	private void enableWidgets(boolean enabled) {
		nameLabel.setEnabled(enabled);
		nameText.setEnabled(enabled);
		passwordLabel.setEnabled(enabled);
		passwordText.setEnabled(enabled);
		repeatPasswordLabel.setEnabled(enabled);
		repeatPasswordText.setEnabled(enabled);
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
		getButton(IDialogConstants.OK_ID).setEnabled(pwReady && nameReady);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}
}
