package de.dzimmermann.rcp.pwm.ui.dialog;

import java.util.Calendar;

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
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.dzimmermann.rcp.pwm.model.content.PWMGroupEntry;

public class AddEditEntryDialog extends TitleAreaDialog {

	private static final String WINDOW_TITLE = "PWM - %s Entry";
	private static final String AREA_TITLE = "%s a Entry";
	private static final String AREA_MESSAGE = "Specify the fields of an Password Entry.";

	private final PWMGroupEntry entry;

	private PWMGroupEntry result = new PWMGroupEntry();

	private Text titleText;
	private Text usernameText;
	private Text passwordText;
	private Text urlText;
	private Text descriptionText;

	private DateTime expirationDate;
	private DateTime expirationTime;

	public AddEditEntryDialog(Shell parentShell, PWMGroupEntry entry) {
		super(parentShell);
		this.entry = entry;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(String.format(WINDOW_TITLE, entry != null ? "Add"
				: "Edit"));
	}

	public PWMGroupEntry getEntry() {
		return entry;
	}

	public PWMGroupEntry getResult() {
		return result;
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		container.setLayout(new GridLayout(4, false));

		Label titleLabel = new Label(container, SWT.NONE);
		titleLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false));
		titleLabel.setText("Title:");

		titleText = new Text(container, SWT.BORDER);
		titleText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				3, 1));

		titleText.setText(entry != null && entry.getName() != null ? entry
				.getName() : "");

		Label usernameLabel = new Label(container, SWT.NONE);
		usernameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false));
		usernameLabel.setText("Username:");

		usernameText = new Text(container, SWT.BORDER);
		usernameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));

		usernameText
				.setText(entry != null && entry.getUsername() != null ? entry
						.getUsername() : "");

		Label passwordLabel = new Label(container, SWT.NONE);
		passwordLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false));
		passwordLabel.setText("Password:");

		passwordText = new Text(container, SWT.BORDER);
		passwordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));

		passwordText.setEchoChar('*');
		passwordText
				.setText(entry != null && entry.getPassword() != null ? entry
						.getPassword() : "");

		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		final Button showPWButton = new Button(container, SWT.CHECK);
		showPWButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		showPWButton.setText("Show Password");

		Label urlLabel = new Label(container, SWT.NONE);
		urlLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		urlLabel.setText("URL:");

		urlText = new Text(container, SWT.BORDER);
		urlText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				3, 1));

		urlText.setText(entry != null && entry.getUrl() != null ? entry
				.getUrl() : "");

		Label sepLabel = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		sepLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				4, 1));

		Label descriptionLabel = new Label(container, SWT.NONE);
		descriptionLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false,
				false, 1, 1));
		descriptionLabel.setText("Description:");

		descriptionText = new Text(container, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		descriptionText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 3, 1));

		descriptionText
				.setText(entry != null && entry.getDescription() != null ? entry
						.getDescription() : "");

		final Button expirationButton = new Button(container, SWT.CHECK);
		expirationButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		expirationButton.setText("Expires:");

		expirationDate = new DateTime(container, SWT.BORDER | SWT.DROP_DOWN);
		expirationDate.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));

		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		expirationTime = new DateTime(container, SWT.BORDER | SWT.DROP_DOWN
				| SWT.TIME | SWT.SHORT);
		expirationTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));

		if (entry != null && entry.getDateExpiration() != null) {
			expirationDate.setEnabled(true);
			expirationTime.setEnabled(true);
			expirationDate
					.setYear(entry.getDateExpiration().get(Calendar.YEAR));
			expirationDate.setMonth(entry.getDateExpiration().get(
					Calendar.MONTH));
			expirationDate.setDay(entry.getDateExpiration().get(
					Calendar.DAY_OF_MONTH));
			expirationTime.setHours(entry.getDateExpiration().get(
					Calendar.HOUR_OF_DAY));
			expirationTime.setMinutes(entry.getDateExpiration().get(
					Calendar.MINUTE));
		} else {
			expirationDate.setEnabled(false);
			expirationTime.setEnabled(false);
		}

		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);

		ModifyListener ml = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (e.widget == titleText) {
					result.setName(titleText.getText().isEmpty() ? null
							: titleText.getText());
				} else if (e.widget == usernameText) {
					result.setUsername(usernameText.getText().isEmpty() ? null
							: usernameText.getText());
				} else if (e.widget == passwordText) {
					result.setPassword(passwordText.getText().isEmpty() ? null
							: passwordText.getText());
				} else if (e.widget == urlText) {
					result.setUrl(urlText.getText().isEmpty() ? null : urlText
							.getText());
				} else if (e.widget == descriptionText) {
					result.setDescription(descriptionText.getText().isEmpty() ? null
							: descriptionText.getText());
				}
			}
		};
		titleText.addModifyListener(ml);
		usernameText.addModifyListener(ml);
		passwordText.addModifyListener(ml);
		urlText.addModifyListener(ml);
		descriptionText.addModifyListener(ml);

		SelectionAdapter sa = new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.widget == showPWButton) {
					passwordText.setEchoChar(showPWButton.getSelection() ? '\0'
							: '*');
					// if (showPWButton.getSelection())
					// passwordText
					// .setText(result.getPassword() != null ? entry
					// .getPassword() : result.getPassword());
					// else
					// passwordText.setText("******");
				} else if (e.widget == expirationButton) {
					if (expirationButton.getSelection()) {
						expirationDate.setEnabled(true);
						expirationTime.setEnabled(true);
						result.setDateExpiration(getExpiration());
					} else {
						expirationDate.setEnabled(false);
						expirationTime.setEnabled(false);
						result.setDateExpiration(null);
					}
				}
			}
		};
		showPWButton.addSelectionListener(sa);
		expirationButton.addSelectionListener(sa);

		DateTimeListener dtl = new DateTimeListener();
		expirationDate.addListener(SWT.Selection, dtl);
		expirationDate.addListener(SWT.Modify, dtl);
		expirationTime.addListener(SWT.Selection, dtl);
		expirationTime.addListener(SWT.Modify, dtl);

		setTitle(String.format(AREA_TITLE, entry != null ? "Add" : "Edit"));
		setMessage(String.format(AREA_MESSAGE, entry != null ? "Add" : "Edit"));

		return area;
	}

	private Calendar getExpiration() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, expirationDate.getYear());
		c.set(Calendar.MONTH, expirationDate.getMonth());
		c.set(Calendar.DAY_OF_MONTH, expirationDate.getDay());
		c.set(Calendar.HOUR_OF_DAY, expirationTime.getHours());
		c.set(Calendar.MINUTE, expirationTime.getMinutes());
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 450);
	}

	private class DateTimeListener implements Listener {
		@Override
		public void handleEvent(Event event) {
			result.setDateExpiration(getExpiration());
		}
	}
}
