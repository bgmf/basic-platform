package de.dzimmermann.rcp.basicplatform.ui.dialog;

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.util.Geometry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
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

import de.dzimmermann.rcp.basicplatform.ui.util.Logger;
import de.dzimmermann.rcp.basicplatform.util.BasicPlatformPluginConstants;
import de.dzimmermann.rcp.basicplatform.util.Messages;
import de.dzimmermann.rcp.basicplatform.util.PFSCoreIcon;
import de.dzimmermann.rcp.basicplatform.util.PFSCoreIconProvider;
import de.dzimmermann.rcp.basicplatform.util.SSDToolUtils;

public class SelectTrustAndKeystorePathDialog extends TitleAreaDialog {

	public static final String WINDOW_TITLE = Messages.getString("SelectTrustAndKeystorePathDialog.windowtitle"); //$NON-NLS-1$
	private static final String AREA_TITLE = Messages.getString("SelectTrustAndKeystorePathDialog.areatitle"); //$NON-NLS-1$
	private static final String OK_MESSAGE = Messages.getString("SelectTrustAndKeystorePathDialog.ok.message"); //$NON-NLS-1$
	private static final String ERROR_MESSAGE = Messages.getString("SelectTrustAndKeystorePathDialog.error.message"); //$NON-NLS-1$

	private Image folder;
	private Image wizardFolder;

	private String keyPath = null;
	private String trustPath = null;
	private Label selectPathLabel;
	private Text text;
	private Button browseButton;

	// other
	private boolean centerOnMonitor = false;

	private boolean stillError = true;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public SelectTrustAndKeystorePathDialog(Shell parentShell) {
		super(parentShell);

		keyPath = SSDToolUtils.getCurrentKeyStorePath();
		trustPath = SSDToolUtils.getCurrentTrustStorePath();

		try {
			PFSCoreIcon icon = PFSCoreIcon.PFSCORE_ICON_FOLDER;
			folder = PFSCoreIconProvider.getImageByIconType(icon);
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
			Logger.logError(e);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			Logger.logError(e);
		}

		try {
			PFSCoreIcon icon = PFSCoreIcon.PFSCORE_ICON_WIZARD_FOLDER;
			wizardFolder = PFSCoreIconProvider.getImageByIconType(icon);
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
			Logger.logError(e);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			Logger.logError(e);
		}
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(WINDOW_TITLE);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(600, 225);
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

		IDialogSettings settings = SSDToolUtils
				.getDialogSettingsBySectionName(
						BasicPlatformPluginConstants.DIALOG_SETTINGS_SECTION_TKSTOREPATH,
						true);
		if (settings != null) {
			return settings;
		}
		return super.getDialogBoundsSettings();
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
				if (folder != null) {
					folder.dispose();
					folder = null;
				}
				if (wizardFolder != null) {
					wizardFolder.dispose();
					wizardFolder = null;
				}
			}
		});

		selectPathLabel = new Label(container, SWT.NONE);
		selectPathLabel.setText(Messages.getString("SelectTrustAndKeystorePathDialog.selectPathLabel")); //$NON-NLS-1$
		new Label(container, SWT.NONE);

		text = new Text(container, SWT.BORDER);
		text
				.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
						1, 1));
		text.addListener(SWT.Modify, new Listener() {
			@Override
			public void handleEvent(Event event) {
				File f = new File(text.getText());
				if (!f.isFile()) {
					stillError = true;
					setErrorMessage(ERROR_MESSAGE);
				} else {
					stillError = false;
					setErrorMessage(null);
					setMessage(OK_MESSAGE);
					keyPath = text.getText();
					trustPath = text.getText();
				}
			}
		});
		text.setText(keyPath == null && trustPath == null ? "" //$NON-NLS-1$
				: keyPath == null || trustPath == null ? "" : !keyPath //$NON-NLS-1$
						.equals(trustPath) ? "" : keyPath); //$NON-NLS-1$

		browseButton = new Button(container, SWT.NONE);
		browseButton.setText(Messages.getString("SelectTrustAndKeystorePathDialog.browseButton")); //$NON-NLS-1$
		browseButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				String lastDir = SSDToolUtils
						.getDialogSettingsLastOpendDirectory();
				OpenSaveDialogV2 dialog = new OpenSaveDialogV2(getShell(),
						true, Messages.getString("SelectTrustAndKeystorePathDialog.OpenSaveDialog.title"), Messages.getString("SelectTrustAndKeystorePathDialog.OpenSaveDialog.text"), null, //$NON-NLS-1$ //$NON-NLS-2$
						Messages.getString("SelectTrustAndKeystorePathDialog.OpenSaveDialog.desc"), //$NON-NLS-1$
						null, lastDir, null, false, false, null);
				String result = null;
				if ((result = dialog.open()) != null) {
					SSDToolUtils.setDialogSettingsLastOpendDirectory(result,
							false);
					text.setText(result);
				}
			}
		});

		if (folder != null) {
			browseButton.setImage(folder);
		}

		// initial set up
		setTitle(AREA_TITLE);
		if (trustPath == null || keyPath == null) {
			setErrorMessage(ERROR_MESSAGE);
		} else {
			File trustFile = new File(trustPath);
			File keyFile = new File(keyPath);
			if (!trustFile.isFile() || !keyFile.isFile()) {
				stillError = true;
				setErrorMessage(ERROR_MESSAGE);
			} else {
				stillError = false;
				setErrorMessage(null);
				setMessage(OK_MESSAGE);
			}
		}

		if (wizardFolder != null) {
			setTitleImage(wizardFolder);
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
		if (!stillError) {
			if (keyPath != null
					&& !keyPath.equals(SSDToolUtils.getCurrentKeyStorePath())) {
				SSDToolUtils.setCurrentKeyStorePath(keyPath);
			}
			if (trustPath != null
					&& !trustPath.equals(SSDToolUtils
							.getCurrentTrustStorePath())) {
				SSDToolUtils.setCurrentTrustStorePath(trustPath);
			}
			super.okPressed();
		}
	}
}
