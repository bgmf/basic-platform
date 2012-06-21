package de.dzimmermann.rcp.basicplatform.ui.dialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.util.Geometry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IMessage;

import de.dzimmermann.rcp.basicplatform.util.BasicPlatformPluginConstants;
import de.dzimmermann.rcp.basicplatform.util.InternalAuthenticator;
import de.dzimmermann.rcp.basicplatform.util.Messages;
import de.dzimmermann.rcp.basicplatform.util.SSDToolUtils;

/**
 * This dialog is used to specify a user name and a password for the
 * authentication at a web server.
 * 
 * @author dzimmermann
 * @since SSDTool (PCP) v0.4.3
 * @version 0.1
 */
public class UserAuthenticationDialog extends TitleAreaDialog {

	public static final String WINDOW_TITLE = Messages
			.getString("UserAuthenticationDialog.window.title"); //$NON-NLS-1$
	private static final String AREA_TITLE = Messages
			.getString("UserAuthenticationDialog.area.title"); //$NON-NLS-1$

	private static final String OK_MESSAGE = Messages
			.getString("UserAuthenticationDialog.ok.message"); //$NON-NLS-1$
	private static final String ERROR_MESSAGE_DEFAULT = Messages
			.getString("UserAuthenticationDialog.error.message.default"); //$NON-NLS-1$
	private static final String WARNING_MESSAGE = Messages
			.getString("UserAuthenticationDialog.warning.message"); //$NON-NLS-1$

	private boolean centerOnMonitor = false;

	private Label userNameLabel;
	private Text userNameText;
	private Label passwordLabel;
	private Text passwordText;
	private Button storePasswordCheckButton;

	private InternalListener listener;

	private InternalAuthenticator currentAuthenticator;

	private String userName;
	private String password;
	private boolean storePassword;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public UserAuthenticationDialog(Shell parentShell,
			InternalAuthenticator currentAuthenticator) {
		super(parentShell);
		// setShellStyle(SWT.RESIZE);
		this.currentAuthenticator = currentAuthenticator;
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 255);
	}

	@Override
	protected int getDialogBoundsStrategy() {
		// explicit usage of only DIALOG_PERSISTLOCATION
		// add DIALOG_PERSISTSIZE, if this dialog become resizable
		return DIALOG_PERSISTLOCATION;
	}

	@Override
	protected Point getInitialLocation(Point initialSize) {

		if (!centerOnMonitor) {
			return super.getInitialLocation(initialSize);
		}

		Monitor monitor = getShell().getMonitor();
		Rectangle monitorBounds = monitor.getClientArea();
		Point centerPoint = Geometry.centerPoint(monitorBounds);

		return new Point(centerPoint.x - (initialSize.x / 2), Math.max(
				monitorBounds.y, Math.min(centerPoint.y
						- (initialSize.y * 2 / 3), monitorBounds.y
						+ monitorBounds.height - initialSize.y)));
	}

	@Override
	protected IDialogSettings getDialogBoundsSettings() {
		// If we were explicitly instructed to center on the monitor, then
		// do not provide any settings for retrieving a different location or,
		// worse, saving the centered location.
		if (centerOnMonitor) {
			return null;
		}

		IDialogSettings settings = SSDToolUtils.getDialogSettingsBySectionName(
				BasicPlatformPluginConstants.DIALOG_SETTINGS_SECTION_USERAUTH, true);
		if (settings != null) {
			return settings;
		}
		return super.getDialogBoundsSettings();
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(WINDOW_TITLE);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		final Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		container.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				if (listener != null) {
					if (!userNameText.isDisposed()) {
						userNameText.removeListener(SWT.Modify, listener);
					}
					if (!passwordText.isDisposed()) {
						passwordText.removeListener(SWT.Modify, listener);
					}
					if (!storePasswordCheckButton.isDisposed()) {
						storePasswordCheckButton.removeListener(SWT.Selection,
								listener);
					}
				}
				container.removeDisposeListener(this);
			}
		});

		userNameLabel = new Label(container, SWT.NONE);
		userNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		userNameLabel.setText(Messages
				.getString("UserAuthenticationDialog.userNameLabel.Text")); //$NON-NLS-1$

		userNameText = new Text(container, SWT.BORDER);
		userNameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		passwordLabel = new Label(container, SWT.NONE);
		passwordLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		passwordLabel.setText(Messages
				.getString("UserAuthenticationDialog.passwordLabelText")); //$NON-NLS-1$

		passwordText = new Text(container, SWT.BORDER | SWT.PASSWORD);
		passwordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		new Label(container, SWT.NONE);

		storePasswordCheckButton = new Button(container, SWT.CHECK);
		storePasswordCheckButton.setLayoutData(new GridData(SWT.RIGHT,
				SWT.CENTER, false, false, 1, 1));
		storePasswordCheckButton
				.setText(Messages
						.getString("UserAuthenticationDialog.storePasswordCheckButton.Text")); //$NON-NLS-1$

		listener = new InternalListener();

		storePasswordCheckButton.addListener(SWT.Selection, listener);
		userNameText.addListener(SWT.Modify, listener);
		passwordText.addListener(SWT.Modify, listener);

		// initial set up
		setTitle(AREA_TITLE);

		if (currentAuthenticator != null) {
			userName = currentAuthenticator.getUserName();
			userNameText.setText(userName);
			password = currentAuthenticator.getPassword();
			passwordText.setText(password);
			storePassword = SSDToolUtils.getStoreAuthenticationPassword();
			storePasswordCheckButton.setSelection(storePassword);

			printMessage();
		}

		return area;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected void okPressed() {
		super.okPressed();
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	public boolean isStorePassword() {
		return storePassword;
	}

	private class InternalListener implements Listener {

		@Override
		public void handleEvent(Event event) {
			if (event.widget == storePasswordCheckButton) {
				storePassword = storePasswordCheckButton.getSelection();
				printMessage();
			} else if (event.widget == userNameText) {
				userName = userNameText.getText();
				printMessage();
			} else if (event.widget == passwordText) {
				password = passwordText.getText();
				printMessage();
			}
		}
	}

	private void printMessage() {
		if (userNameText.getText().isEmpty()) {
			setMessage(ERROR_MESSAGE_DEFAULT, IMessage.ERROR);
		} else {
			if (storePassword) {
				setMessage(WARNING_MESSAGE, IMessage.WARNING);
			} else {
				setMessage(OK_MESSAGE, IMessage.NONE);
			}
		}
	}
}
