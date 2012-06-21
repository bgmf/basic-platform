package de.dzimmermann.rcp.basicplatform.ui.dialog;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

import de.dzimmermann.rcp.basicplatform.SSDToolPlugin;
import de.dzimmermann.rcp.basicplatform.model.BasicPlatformSessionModel;
import de.dzimmermann.rcp.basicplatform.ui.component.ServerSelectionComposite;
import de.dzimmermann.rcp.basicplatform.ui.util.Logger;
import de.dzimmermann.rcp.basicplatform.util.BasicPlatformPluginConstants;
import de.dzimmermann.rcp.basicplatform.util.Messages;
import de.dzimmermann.rcp.basicplatform.util.PFSCoreIcon;
import de.dzimmermann.rcp.basicplatform.util.PFSCoreIconProvider;
import de.dzimmermann.rcp.basicplatform.util.SSDToolUtils;

public class SelectRemoteServerDialog extends TitleAreaDialog implements
		PropertyChangeListener {

	public static final String WINDOW_TITLE = Messages
			.getString("SelectRemoteServerDialog.window.title"); //$NON-NLS-1$
	private static final String AREA_TITLE = Messages
			.getString("SelectRemoteServerDialog.area.title"); //$NON-NLS-1$

	private static final String OK_MESSAGE = Messages
			.getString("SelectRemoteServerDialog.ok.message"); //$NON-NLS-1$

	private static final String ERROR_MESSAGE_DEFAULT = Messages
			.getString("SelectRemoteServerDialog.error.message.default"); //$NON-NLS-1$
	private static final String ERROR_MESSAGE_DEV_MODE = Messages
			.getString("SelectRemoteServerDialog.error.message.devmode"); //$NON-NLS-1$

	private boolean centerOnMonitor = false;

	private Image wizardFolder;

	private ServerSelectionComposite serverSelectionComposite;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public SelectRemoteServerDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(WINDOW_TITLE);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(450, 249);
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
				BasicPlatformPluginConstants.DIALOG_SETTINGS_SECTION_SELECTRS,
				true);
		if (settings != null) {
			return settings;
		}
		return super.getDialogBoundsSettings();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		container.setLayout(new GridLayout(2, false));

		createIcons();

		container.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				if (wizardFolder != null && !wizardFolder.isDisposed()) {
					wizardFolder.dispose();
					wizardFolder = null;
				}
			}
		});

		// initial set up
		setTitle(AREA_TITLE);
		setMessage(OK_MESSAGE);

		if (wizardFolder != null) {
			setTitleImage(wizardFolder);
		}

		serverSelectionComposite = new ServerSelectionComposite(container,
				SWT.NONE);
		serverSelectionComposite.addPropertyChangeListener(this);
		serverSelectionComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP,
				true, false, 1, 1));

		return area;
	}

	private void createIcons() {

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
	protected void createButtonsForButtonBar(Composite parent) {

		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);

		if (serverSelectionComposite != null
				&& !serverSelectionComposite.isDisposed()) {
			serverSelectionComposite.checkServerSelectionValidityAndFireEvent();
		}
	}

	@Override
	protected void okPressed() {

		boolean error = false;
		boolean devMode = SSDToolPlugin.getDefault().isDeveloperMode();

		String currentRemoteServer = serverSelectionComposite
				.getCurrentRemoteServer();

		if (currentRemoteServer != null) {

			BasicPlatformSessionModel.getInstance().setRemoteServerName(
					currentRemoteServer);
			BasicPlatformSessionModel.getInstance().setRemoteServerUrl(
					SSDToolPlugin.getDefault().getSSDToolModel()
							.getServerList().get(currentRemoteServer));

			String recentRemote = SSDToolUtils.getRecentRemoteServer();

			if (recentRemote == null
					|| !recentRemote.equals(currentRemoteServer)) {

				if (devMode
						|| (SSDToolUtils.getOpenEditorIDs().isEmpty() && SSDToolUtils
								.getOpenViewIDs().isEmpty())) {
					SSDToolUtils.setRecentRemoteServer(currentRemoteServer);
				} else {
					MessageDialog
							.openError(
									getShell(),
									Messages.getString("SelectRemoteServerDialog.MessageDialog.Error.Title"), //$NON-NLS-1$
									Messages.getString("SelectRemoteServerDialog.MessageDialog.Error.Text")); //$NON-NLS-1$
				}
			}

			if (devMode
					|| (SSDToolUtils.getOpenEditorIDs().isEmpty() && SSDToolUtils
							.getOpenViewIDs().isEmpty())) {
				// set, if the alternative server should be used instead
				if (serverSelectionComposite.isUseAlternativeServer()) {
					SSDToolUtils.setUseAlternativeServerHost(true);
					SSDToolUtils
							.setAlternativeServerHost(serverSelectionComposite
									.getAlternativeServer());
					BasicPlatformSessionModel.getInstance().setRemoteServerAlternative(
							true);
					BasicPlatformSessionModel.getInstance().setRemoteServerName(
							serverSelectionComposite.getAlternativeServer());
					BasicPlatformSessionModel.getInstance().setRemoteServerUrl(
							serverSelectionComposite.getAlternativeServer());
				} else {
					SSDToolUtils.setUseAlternativeServerHost(false);
					SSDToolUtils.setAlternativeServerHost(""); //$NON-NLS-1$
					BasicPlatformSessionModel.getInstance().setRemoteServerAlternative(
							false);
				}
			} else {
				MessageDialog
						.openError(
								getShell(),
								Messages.getString("SelectRemoteServerDialog.MessageDialog2.Error.Title"), //$NON-NLS-1$
								Messages.getString("SelectRemoteServerDialog.MessageDialog2.Error.Text")); //$NON-NLS-1$
			}

		} else {
			error = true;
		}

		if (error) {
			setMessage(null);
			setErrorMessage(ERROR_MESSAGE_DEFAULT);
		} else if (!error
				&& !(devMode || (SSDToolUtils.getOpenEditorIDs().isEmpty() && SSDToolUtils
						.getOpenViewIDs().isEmpty()))) {
			setMessage(null);
			setErrorMessage(ERROR_MESSAGE_DEV_MODE);
		} else {
			setErrorMessage(null);
			setMessage(OK_MESSAGE);

			super.okPressed();
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		if (evt.getPropertyName().equals(
				ServerSelectionComposite.IS_VALID_SERVER_SELECTED)) {
			getButton(IDialogConstants.OK_ID).setEnabled(
					(Boolean) evt.getNewValue());
		}
	}
}
