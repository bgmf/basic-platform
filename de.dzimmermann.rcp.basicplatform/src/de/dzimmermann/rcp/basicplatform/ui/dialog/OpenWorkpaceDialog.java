package de.dzimmermann.rcp.basicplatform.ui.dialog;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URL;

import org.eclipse.jface.dialogs.Dialog;
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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import de.dzimmermann.rcp.basicplatform.SSDToolApplication;
import de.dzimmermann.rcp.basicplatform.SSDToolPlugin;
import de.dzimmermann.rcp.basicplatform.model.BasicPlatformSessionModel;
import de.dzimmermann.rcp.basicplatform.model.ServerList;
import de.dzimmermann.rcp.basicplatform.ui.component.ServerSelectionComposite;
import de.dzimmermann.rcp.basicplatform.ui.util.Logger;
import de.dzimmermann.rcp.basicplatform.util.BasicPlatformPluginConstants;
import de.dzimmermann.rcp.basicplatform.util.BasicPlatformPreferenceConstants;
import de.dzimmermann.rcp.basicplatform.util.Messages;
import de.dzimmermann.rcp.basicplatform.util.PFSCoreIconProvider;
import de.dzimmermann.rcp.basicplatform.util.SSDToolUtils;

/**
 * This dialog is created on each application startup. It checks for the last
 * recently opened workspaces
 * 
 * @author dzimmermann
 * @since SSDTool 0.2.0
 * @version 0.2.1
 * 
 * @see SSDToolApplication
 */
public class OpenWorkpaceDialog extends TitleAreaDialog implements
		Listener, PropertyChangeListener {

	/**
	 * XXX copy from {@link Dialog}
	 */
	@SuppressWarnings("unused")
	private static final String DIALOG_ORIGIN_X = "DIALOG_X_ORIGIN"; //$NON-NLS-1$
	/**
	 * XXX copy from {@link Dialog}
	 */
	@SuppressWarnings("unused")
	private static final String DIALOG_ORIGIN_Y = "DIALOG_Y_ORIGIN"; //$NON-NLS-1$
	/**
	 * XXX copy from {@link Dialog}
	 */
	private static final String DIALOG_WIDTH = "DIALOG_WIDTH"; //$NON-NLS-1$
	/**
	 * XXX copy from {@link Dialog}
	 */
	private static final String DIALOG_HEIGHT = "DIALOG_HEIGHT"; //$NON-NLS-1$
	/**
	 * XXX copy from {@link Dialog}
	 */
	@SuppressWarnings("unused")
	private static final String DIALOG_FONT_DATA = "DIALOG_FONT_NAME"; //$NON-NLS-1$

	public static final String WINDOW_TITLE = Messages
			.getString("OpenSSDToolWorkpaceDialog.window.title"); //$NON-NLS-1$
	private static final String AREA_TITLE = Messages
			.getString("OpenSSDToolWorkpaceDialog.area.title"); //$NON-NLS-1$

	private static final String OK_MESSAGE = Messages
			.getString("OpenSSDToolWorkpaceDialog.ok.message"); //$NON-NLS-1$

	private static final String ERROR_MESSAGE_DEFAULT = Messages
			.getString("OpenSSDToolWorkpaceDialog.error.message.default"); //$NON-NLS-1$

	private static final String ERROR_MESSAGE_SPECIFIED_FOLDER_INVALID = Messages
			.getString("OpenSSDToolWorkpaceDialog.error.message.specified.folder.invalid"); //$NON-NLS-1$

	private final FormToolkit formToolkit = new FormToolkit(
			Display.getDefault());

	private Image folder;
	private Image wizardFolder;

	private Combo workspaceSelectionCombo;
	private Button browseButton;
	private Button showOnStartupCkeckbox;

	// additional stuffs widgets
	private ServerSelectionComposite serverSelectionComposite;

	// recent workspaces
	private boolean showWSDialogOnStartup = true;
	private String currentWS = null;

	// other
	private boolean centerOnMonitor = false;
	private boolean invalidDataWSParameter = false;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 * @wbp.parser.constructor
	 */
	public OpenWorkpaceDialog(Shell parentShell) {
		this(parentShell, false);
	}

	public OpenWorkpaceDialog(Shell parentShell,
			boolean invalidDataWSParameter) {
		super(parentShell);
		this.invalidDataWSParameter = invalidDataWSParameter;
	}

	@Override
	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(newShellStyle | SWT.RESIZE);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(WINDOW_TITLE);
	}

	@Override
	protected Point getInitialSize() {

		IDialogSettings settings = getDialogBoundsSettings();

		int x = 640;
		int y = 375;

		if (settings != null) {
			if (settings.get(DIALOG_WIDTH) != null) {
				x = settings.getInt(DIALOG_WIDTH);
			}
			if (settings.get(DIALOG_HEIGHT) != null) {
				y = settings.getInt(DIALOG_HEIGHT);
			}
		}

		return new Point(x, y);
	}

	@Override
	protected int getDialogBoundsStrategy() {
		return super.getDialogBoundsStrategy();
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
		if (centerOnMonitor) {
			return null;
		}

		IDialogSettings settings = SSDToolUtils
				.getDialogSettingsBySectionName(
						BasicPlatformPluginConstants.DIALOG_SETTINGS_SECTION_OPENWS,
						true);
		if (settings != null) {
			return settings;
		}
		return super.getDialogBoundsSettings();
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite area = (Composite) super.createDialogArea(parent);
		Composite defaultContainer = new Composite(area, SWT.NONE);
		defaultContainer.setLayoutData(new GridData(GridData.FILL_BOTH));

		createIcons();
		defaultContainer.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				if (wizardFolder != null && !wizardFolder.isDisposed()) {
					wizardFolder.dispose();
					wizardFolder = null;
				}
				if (folder != null && !folder.isDisposed()) {
					folder.dispose();
					folder = null;
				}
			}
		});

		defaultContainer.setLayout(new FillLayout(SWT.HORIZONTAL));

		final Composite container = formToolkit.createComposite(
				defaultContainer, SWT.NONE);
		formToolkit.paintBordersFor(container);
		container.setLayout(new GridLayout(2, false));

		Section workspaceSection = formToolkit.createSection(container,
				Section.TITLE_BAR);
		workspaceSection.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
				false, 2, 1));
		formToolkit.paintBordersFor(workspaceSection);
		workspaceSection.setText(Messages
				.getString("OpenSSDToolWorkpaceDialog.WorkspaceLabel.text"));

		Composite workspaceComposite = formToolkit.createComposite(
				workspaceSection, SWT.NONE);
		formToolkit.paintBordersFor(workspaceComposite);
		workspaceSection.setClient(workspaceComposite);
		workspaceComposite.setLayout(new GridLayout(2, false));

		workspaceSelectionCombo = new Combo(workspaceComposite, SWT.BORDER);
		workspaceSelectionCombo
				.setToolTipText(Messages
						.getString("OpenSSDToolWorkpaceDialog.WorkspaceSelectionCombo.Text")); //$NON-NLS-1$
		workspaceSelectionCombo.setLayoutData(new GridData(SWT.FILL,
				SWT.CENTER, true, false, 1, 1));

		workspaceSelectionCombo.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				if (wizardFolder != null && !wizardFolder.isDisposed()) {
					wizardFolder.dispose();
					wizardFolder = null;
				}
				if (folder != null && !folder.isDisposed()) {
					folder.dispose();
					folder = null;
				}
			}
		});

		browseButton = new Button(workspaceComposite, SWT.NONE);
		browseButton
				.setToolTipText(Messages
						.getString("OpenSSDToolWorkpaceDialog.browseButton.ToolTipText")); //$NON-NLS-1$
		browseButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		browseButton.setText(Messages
				.getString("OpenSSDToolWorkpaceDialog.browseButton.Text")); //$NON-NLS-1$

		if (folder != null) {
			browseButton.setImage(folder);
		}

		Section additionalSetUpSection = formToolkit.createSection(container,
				Section.TITLE_BAR);
		additionalSetUpSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true, 2, 1));
		formToolkit.paintBordersFor(additionalSetUpSection);
		additionalSetUpSection
				.setText(Messages
						.getString("OpenSSDToolWorkpaceDialog.grpAdditionalSetUp.Text")); //$NON-NLS-1$

		Composite grpAdditionalSetUp = formToolkit.createComposite(
				additionalSetUpSection, SWT.NONE);
		formToolkit.paintBordersFor(grpAdditionalSetUp);
		additionalSetUpSection.setClient(grpAdditionalSetUp);

		grpAdditionalSetUp.setLayout(new GridLayout(1, false));

		// if (SSDToolPlugin.getDefault().isDeveloperMode()) {
		{
			serverSelectionComposite = new ServerSelectionComposite(
					grpAdditionalSetUp, SWT.NONE);
			formToolkit.adapt(serverSelectionComposite);
			serverSelectionComposite.addPropertyChangeListener(this);
			serverSelectionComposite.setLayoutData(new GridData(SWT.FILL,
					SWT.TOP, true, false, 1, 1));
		}

		if (grpAdditionalSetUp.getChildren() == null
				|| grpAdditionalSetUp.getChildren().length == 0) {
			Label emptyLabel = new Label(grpAdditionalSetUp, SWT.WRAP);
			emptyLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
					false, 2, 1));
			emptyLabel.setText(Messages
					.getString("OpenSSDToolWorkpaceDialog.emptyLabel.Text")); //$NON-NLS-1$
		}

		showOnStartupCkeckbox = formToolkit
				.createButton(
						container,
						Messages.getString("OpenSSDToolWorkpaceDialog.showOnStartuoCheckbox.Text") //$NON-NLS-1$
						, SWT.CHECK);
		showOnStartupCkeckbox
				.setToolTipText(Messages
						.getString("OpenSSDToolWorkpaceDialog.showOnStartuoCheckbox.ToolTipText")); //$NON-NLS-1$
		showOnStartupCkeckbox.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 2, 1));

		// initial set up
		setTitle(AREA_TITLE);
		if (!invalidDataWSParameter) {
			setMessage(OK_MESSAGE);
		} else {
			setMessage(null);
			setErrorMessage(ERROR_MESSAGE_SPECIFIED_FOLDER_INVALID);
		}
		if (wizardFolder != null) {
			setTitleImage(wizardFolder);
		}

		showWSDialogOnStartup = SSDToolUtils.isShowWorkspaceSelectionDialog();
		String recentWS = SSDToolUtils.getRecentWorkspaces();

		showOnStartupCkeckbox.setSelection(!showWSDialogOnStartup);

		if (recentWS != null) {
			String[] splittedRecentWS = recentWS
					.split(BasicPlatformPreferenceConstants.RECENT_WORKSPACES_DELIMETER);
			workspaceSelectionCombo.setItems(splittedRecentWS);
			workspaceSelectionCombo.setText(splittedRecentWS[0]);
			currentWS = splittedRecentWS[0];
		}

		workspaceSelectionCombo.addListener(SWT.Modify, this);
		workspaceSelectionCombo.addListener(SWT.Selection, this);
		browseButton.addListener(SWT.Selection, this);
		showOnStartupCkeckbox.addListener(SWT.Selection, this);

		return area;
	}

	@SuppressWarnings("deprecation")
	public URL getCurrentWSAsURL() {

		try {
			return new File(currentWS).toURL();
			// return new File(currentWS).toURI().toURL();
		} catch (Exception e) {
			Logger.logError(
					"Should never appear: Malformed URL in open workspace dialog on conversion >> " //$NON-NLS-1$
							+ currentWS, e);
			return null;
		}
	}

	public String getCurrentWS() {
		return currentWS;
	}

	private void createIcons() {
		folder = PFSCoreIconProvider.getImageByIconName("fldr_obj.gif", true); //$NON-NLS-1$
		wizardFolder = PFSCoreIconProvider.getImageByIconName(
				"newfolder_wiz.png", true); //$NON-NLS-1$
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

		// ###
		// workspace
		// ###

		// check validity...
		if (!checkPathValidity(currentWS)) {
			return;
		}
		// set the workspace preferences
		SSDToolUtils.setIsShowWorkspaceSelectionDialog(showWSDialogOnStartup);
		SSDToolUtils.addRecentWorkspace(currentWS);
		BasicPlatformSessionModel.getInstance().setCurrentWorkspace(currentWS);

		// ###
		// remote server
		// ###

		// check if there are any servers...
		if (SSDToolPlugin.getDefault().getSSDToolModel().getServerList() != null) {
			// check developer mode
			// if set, allow change of the server by using the values of this
			// dialog
			if (SSDToolPlugin.getDefault().isDeveloperMode()) {
				// set remote server
				String recentRemote = SSDToolUtils.getRecentRemoteServer();
				String currentRemoteServer = serverSelectionComposite
						.getCurrentRemoteServer();
				if (currentRemoteServer != null) {
					if (recentRemote == null
							|| !recentRemote.equals(currentRemoteServer)) {
						SSDToolUtils.setRecentRemoteServer(currentRemoteServer);
					}
				}
				BasicPlatformSessionModel.getInstance().setRemoteServerName(
						currentRemoteServer);
				BasicPlatformSessionModel.getInstance().setRemoteServerUrl(
						SSDToolPlugin.getDefault().getSSDToolModel()
								.getServerList().get(currentRemoteServer));

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
			}
			// if developer mode is not set, and there is no recent server set,
			// choose the first from the list (might be tricky if there more
			// than one plugin extending the list...)
			else {
				BasicPlatformSessionModel.getInstance().setRemoteServerAlternative(false);
				String recentRemote = SSDToolUtils.getRecentRemoteServer();
				String currentRemoteServer = serverSelectionComposite
						.getCurrentRemoteServer();
				if (currentRemoteServer != null) {
					if (recentRemote == null
							|| !recentRemote.equals(currentRemoteServer)) {
						recentRemote = currentRemoteServer;
					}
				}
				if (recentRemote == null) {
					ServerList serverList = SSDToolPlugin.getDefault()
							.getSSDToolModel().getServerList();
					String next = serverList.keySet().iterator().next();
					SSDToolUtils.setRecentRemoteServer(next);
					SSDToolUtils.setAlternativeServerHost("");
					BasicPlatformSessionModel.getInstance().setRemoteServerName(next);
					BasicPlatformSessionModel.getInstance().setRemoteServerUrl(
							SSDToolPlugin.getDefault().getSSDToolModel()
									.getServerList().get(next));
				} else {
					SSDToolUtils.setRecentRemoteServer(recentRemote);
					SSDToolUtils.setAlternativeServerHost("");
					BasicPlatformSessionModel.getInstance().setRemoteServerName(
							recentRemote);
					BasicPlatformSessionModel.getInstance().setRemoteServerUrl(
							SSDToolPlugin.getDefault().getSSDToolModel()
									.getServerList().get(recentRemote));
				}
			}
		}

		super.okPressed();
	}

	private boolean checkPathValidity(String path) {

		// bugfix: initial load of a workspace causes wrong answer because
		// current workspace is (of course) still unset
		if (!path.equals(currentWS)) {
			currentWS = path;
		}

		if (currentWS == null || currentWS.isEmpty()) {

			setErrorMessage(ERROR_MESSAGE_DEFAULT);
			getButton(IDialogConstants.OK_ID).setEnabled(false);

			return false;
		}

		File file = new File(path);

		if (file.isDirectory()) {

			setErrorMessage(null);
			setMessage(OK_MESSAGE);
			getButton(IDialogConstants.OK_ID).setEnabled(true);

			return true;

		} else {

			setErrorMessage(ERROR_MESSAGE_DEFAULT);
			getButton(IDialogConstants.OK_ID).setEnabled(false);

			return false;
		}
	}

	@Override
	public void handleEvent(Event event) {

		if (event.widget == workspaceSelectionCombo) {

			checkPathValidity(workspaceSelectionCombo.getText());

		} else if (event.widget == browseButton) {

			String lastDir = SSDToolUtils.getDialogSettingsLastOpendDirectory();
			OpenSaveDialogV2 dialog = new OpenSaveDialogV2(
					getShell(),
					true,
					Messages.getString("OpenSSDToolWorkpaceDialog.OpenSaveDialog.Title"), //$NON-NLS-1$
					Messages.getString("OpenSSDToolWorkpaceDialog.OpenSaveDialog.Text"), //$NON-NLS-1$
					null,
					Messages.getString("OpenSSDToolWorkpaceDialog.OpenSaveDialog.ToolTipText"), //$NON-NLS-1$
					null, lastDir, null, true, false, null);

			String result = null;
			if ((result = dialog.open()) != null) {
				SSDToolUtils.setDialogSettingsLastOpendDirectory(result, true);
				workspaceSelectionCombo.setText(result);
			}

		} else if (event.widget == showOnStartupCkeckbox) {
			showWSDialogOnStartup = !showOnStartupCkeckbox.getSelection();
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
