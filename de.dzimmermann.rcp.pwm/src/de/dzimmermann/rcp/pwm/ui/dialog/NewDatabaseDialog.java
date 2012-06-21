package de.dzimmermann.rcp.pwm.ui.dialog;

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.IDialogConstants;
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

import de.dzimmermann.rcp.pwm.util.PWMUtils;

public class NewDatabaseDialog extends TitleAreaDialog {

	private static final String WINDOW_TITLE = "PWM - New Database";
	private static final String AREA_TITLE = "Create a new Database";
	private static final String AREA_MESSAGE = "Enter the values needed for a new database.";

	private Text text;

	private final List<IPath> databaseFiles;

	private String newDatabaseName = null;

	public NewDatabaseDialog(Shell parentShell, List<IPath> databaseFiles) {
		super(parentShell);
		this.databaseFiles = databaseFiles;
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
		newFileNameLabel.setText("New File Name:");

		text = new Text(container, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				String t = text.getText();
				if (t.toLowerCase().endsWith(PWMUtils.DEFAULT_EXTENSION_BACKUP))
					t = t.substring(0,
							t.lastIndexOf(PWMUtils.DEFAULT_EXTENSION_BACKUP));
				else if (t.toLowerCase().endsWith(PWMUtils.DEFAULT_EXTENSION))
					t = t.substring(0,
							t.lastIndexOf(PWMUtils.DEFAULT_EXTENSION));
				if (databaseFiles != null) {
					for (IPath path : databaseFiles) {
						String p = path.lastSegment();
						if (p.toLowerCase().endsWith(
								PWMUtils.DEFAULT_EXTENSION_BACKUP))
							p = p.substring(
									0,
									p.lastIndexOf(PWMUtils.DEFAULT_EXTENSION_BACKUP));
						else if (p.toLowerCase().endsWith(
								PWMUtils.DEFAULT_EXTENSION))
							p = p.substring(0,
									p.lastIndexOf(PWMUtils.DEFAULT_EXTENSION));
						if (p.equals(t))
							getButton(IDialogConstants.OK_ID).setEnabled(false);
						else {
							getButton(IDialogConstants.OK_ID).setEnabled(true);
							newDatabaseName = t;
						}
					}
				} else {
					getButton(IDialogConstants.OK_ID).setEnabled(true);
					newDatabaseName = t;
				}
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
		return new Point(400, 200);
	}

	public String getNewDatabaseName() {
		return newDatabaseName;
	}
}
