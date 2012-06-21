package de.dzimmermann.rcp.basicplatform.ui.preference;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IMessage;

import de.dzimmermann.rcp.basicplatform.SSDToolPlugin;
import de.dzimmermann.rcp.basicplatform.ui.dialog.OpenWorkpaceDialog;
import de.dzimmermann.rcp.basicplatform.util.BasicPlatformPreferenceConstants;
import de.dzimmermann.rcp.basicplatform.util.Messages;
import de.dzimmermann.rcp.basicplatform.util.PFSCoreIconProvider;
import de.dzimmermann.rcp.basicplatform.util.SSDToolUtils;

/**
 * This is the entry page for SSDTool plugins. Therefore, they need to use this
 * preference page's ID as parent.
 * 
 * @author danielz
 * @version 0.1
 * @since SSDTool V0.1.0
 */
public class InternalPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage, Listener {

	/**
	 * The ID of this main SSDTool preference page
	 */
	public static final String ID = InternalPreferencePage.class.getName();

	private static final String OK_MESSAGE = Messages
			.getString("SSDToolPreferencePage.ok.messsage"); //$NON-NLS-1$

	private Image logoSplash;
	private Image folder;

	// workspace setting
	private FormData formLabelFD;
	private Label lblUnfoldTheSub;
	private Button btnOpenWorkspaceSelection;
	private Text text;
	private Button showWSSelectionDialogCheckButton;

	private String recentWSBeforeNewSelection;

	public InternalPreferencePage() {
		loadIcons();
	}

	public InternalPreferencePage(String title) {
		super(title);
		loadIcons();
	}

	/**
	 * @wbp.parser.constructor
	 */
	public InternalPreferencePage(String title, ImageDescriptor image) {
		super(title, image);
		loadIcons();
	}

	private void loadIcons() {
		// try {
		// logoSplash = PFSCoreIconProvider
		// .getImageByIconType(PFSCoreIcon.PFSCORE_ICON_SPLASH);
		// } catch (IllegalArgumentException e) {
		// System.err.println(e.getMessage());
		// Logger.logError(e);
		// } catch (IOException e) {
		// System.err.println(e.getMessage());
		// Logger.logError(e);
		// }
		folder = PFSCoreIconProvider.getImageByIconName("fldr_obj.gif", true); //$NON-NLS-1$
	}

	@Override
	public void dispose() {
		if (folder != null && !folder.isDisposed()) {
			folder.dispose();
		}
		super.dispose();
	}

	@Override
	protected Control createContents(Composite parent) {

		Composite content = new Composite(parent, SWT.NONE);
		content.setLayout(new FormLayout());
		{
			Label splashLabel = new Label(content, SWT.WRAP | SWT.HORIZONTAL);
			{
				formLabelFD = new FormData();
				formLabelFD.left = new FormAttachment(0, 10);
				formLabelFD.right = new FormAttachment(100, -10);
				splashLabel.setLayoutData(formLabelFD);
			}
			splashLabel.setAlignment(SWT.CENTER);
			{
				lblUnfoldTheSub = new Label(content, SWT.NONE);
				lblUnfoldTheSub.setAlignment(SWT.CENTER);
				{
					FormData fd_lblUnfoldTheSub = new FormData();
					fd_lblUnfoldTheSub.right = new FormAttachment(splashLabel,
							0, SWT.RIGHT);
					fd_lblUnfoldTheSub.top = new FormAttachment(splashLabel, 6);
					fd_lblUnfoldTheSub.left = new FormAttachment(0);
					lblUnfoldTheSub.setLayoutData(fd_lblUnfoldTheSub);
				}
				lblUnfoldTheSub.setText(Messages
						.getString("SSDToolPreferencePage.unfoldsub.text")); //$NON-NLS-1$
			}

			Label label = new Label(content, SWT.SEPARATOR | SWT.HORIZONTAL);
			FormData fd_label = new FormData();
			fd_label.right = new FormAttachment(splashLabel, 0, SWT.RIGHT);
			fd_label.bottom = new FormAttachment(lblUnfoldTheSub, 23,
					SWT.BOTTOM);
			fd_label.top = new FormAttachment(lblUnfoldTheSub, 6);
			fd_label.left = new FormAttachment(0, 10);
			label.setLayoutData(fd_label);

			Group wsSelectionGroup = new Group(content, SWT.NONE);
			wsSelectionGroup.setText(Messages
					.getString("SSDToolPreferencePage.ws.selectiongroup.text")); //$NON-NLS-1$
			wsSelectionGroup.setLayout(new FormLayout());
			FormData fd_wsSelectionGroup = new FormData();
			fd_wsSelectionGroup.height = 70;
			fd_wsSelectionGroup.left = new FormAttachment(splashLabel, 0,
					SWT.LEFT);
			fd_wsSelectionGroup.right = new FormAttachment(splashLabel, 0,
					SWT.RIGHT);
			fd_wsSelectionGroup.top = new FormAttachment(label, 6);
			wsSelectionGroup.setLayoutData(fd_wsSelectionGroup);

			Label currentWSLabel = new Label(wsSelectionGroup, SWT.NONE);
			FormData fd_currentWSLabel = new FormData();
			fd_currentWSLabel.top = new FormAttachment(0, 9);
			fd_currentWSLabel.left = new FormAttachment(0, 10);
			currentWSLabel.setLayoutData(fd_currentWSLabel);
			currentWSLabel.setText(Messages
					.getString("SSDToolPreferencePage.currentWSlabel.text")); //$NON-NLS-1$

			text = new Text(wsSelectionGroup, SWT.BORDER);
			FormData fd_text = new FormData();
			fd_text.top = new FormAttachment(0, 5);
			fd_text.left = new FormAttachment(currentWSLabel, 7);
			text.setLayoutData(fd_text);

			text.setEditable(false);

			btnOpenWorkspaceSelection = new Button(wsSelectionGroup, SWT.NONE);
			fd_text.right = new FormAttachment(btnOpenWorkspaceSelection, -6);
			FormData fd_btnOpenWorkspaceSelection = new FormData();
			fd_btnOpenWorkspaceSelection.top = new FormAttachment(0, 5);
			fd_btnOpenWorkspaceSelection.right = new FormAttachment(100, -10);
			btnOpenWorkspaceSelection
					.setLayoutData(fd_btnOpenWorkspaceSelection);
			btnOpenWorkspaceSelection
					.setText(Messages
							.getString("SSDToolPreferencePage.open.WorkspaceSelection.text")); //$NON-NLS-1$

			if (folder != null) {
				btnOpenWorkspaceSelection.setImage(folder);
			}

			showWSSelectionDialogCheckButton = new Button(wsSelectionGroup,
					SWT.CHECK);
			FormData fd_showWSSelectionDialogCheckButton = new FormData();
			fd_showWSSelectionDialogCheckButton.top = new FormAttachment(text,
					6);
			fd_showWSSelectionDialogCheckButton.right = new FormAttachment(
					btnOpenWorkspaceSelection, 0, SWT.RIGHT);
			showWSSelectionDialogCheckButton
					.setLayoutData(fd_showWSSelectionDialogCheckButton);
			showWSSelectionDialogCheckButton
					.setText(Messages
							.getString("SSDToolPreferencePage.showWS.SelectionDialog.checkbutton.text")); //$NON-NLS-1$

			if (logoSplash != null)
				splashLabel.setImage(logoSplash);
			else {
				splashLabel.setFont(new Font(splashLabel.getDisplay(),
						new FontData("Times New Roman", 14, SWT.BOLD))); //$NON-NLS-1$
				splashLabel.setText("Basic Platform"); //$NON-NLS-1$
			}

			hookListener();
			reset();
		}

		content.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				if (logoSplash != null && !logoSplash.isDisposed()) {
					logoSplash.dispose();
					logoSplash = null;
				}
			}
		});

		return content;
	}

	private void hookListener() {
		btnOpenWorkspaceSelection.addListener(SWT.Selection, this);
	}

	@Override
	public void init(IWorkbench workbench) {
		setMessage(OK_MESSAGE, IMessage.NONE);
		recentWSBeforeNewSelection = SSDToolUtils.getRecentWorkspaces();
	}

	@Override
	public boolean performOk() {
		SSDToolUtils
				.setIsShowWorkspaceSelectionDialog(!showWSSelectionDialogCheckButton
						.getSelection());
		checkForRestart();
		return super.performOk();
	}

	@Override
	public boolean performCancel() {
		SSDToolUtils.setRecentWorkspaces(recentWSBeforeNewSelection);
		return super.performCancel();
	}

	@Override
	protected void performApply() {
		SSDToolUtils
				.setIsShowWorkspaceSelectionDialog(!showWSSelectionDialogCheckButton
						.getSelection());
		reset();
		checkForRestart();
		super.performApply();
	}

	@Override
	protected void performDefaults() {
		SSDToolUtils.setRecentWorkspaces(recentWSBeforeNewSelection);
		reset();
		super.performDefaults();
	}

	private void reset() {
		text.setText(SSDToolUtils.getCurrentWorkspace());
		showWSSelectionDialogCheckButton.setSelection(!SSDToolUtils
				.isShowWorkspaceSelectionDialog());
	}

	@Override
	public void handleEvent(Event e) {

		if (SWT.Selection == e.type) {
			if (e.widget == btnOpenWorkspaceSelection) {
				OpenWorkpaceDialog dialog = new OpenWorkpaceDialog(
						this.getShell());
				if (TitleAreaDialog.OK == dialog.open()) {
					reset();
				}
			}
		}
	}

	private void checkForRestart() {

		if (!recentWSBeforeNewSelection
				.split(BasicPlatformPreferenceConstants.RECENT_WORKSPACES_DELIMETER)[0]
				.equals(SSDToolUtils.getCurrentWorkspace())) {
			String[] buttonLabels = {
					Messages.getString("SSDToolPreferencePage.buttonlabel1"), Messages.getString("SSDToolPreferencePage.buttonlabel2"), Messages.getString("SSDToolPreferencePage.buttonlabel3") }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			MessageDialog dialog = new MessageDialog(
					getShell(),
					Messages.getString("SSDToolPreferencePage.Messagedialog.title"), //$NON-NLS-1$
					null,
					Messages.getString("SSDToolPreferencePage.Messagedialog.text"), //$NON-NLS-1$
					MessageDialog.CONFIRM, buttonLabels, 0);
			int returnValue = dialog.open();
			if (returnValue == 0) {
				if (!PlatformUI.getWorkbench().restart()) {
					ErrorDialog
							.openError(
									getShell(),
									"Error on Restart!", //$NON-NLS-1$
									"Couldn't restart the Workbench due to an unexpected problem!\nRestart the application manually now!", //$NON-NLS-1$
									new Status(IStatus.ERROR,
											SSDToolPlugin.PLUGIN_ID,
											"Unexpected result of an attempt to restart the Workbench")); //$NON-NLS-1$
				}
			} else if (returnValue == 1) {
				if (!PlatformUI.getWorkbench().close()) {
					ErrorDialog
							.openError(
									getShell(),
									"Error on Close!", //$NON-NLS-1$
									"Couldn't close the Workbench due to an unexpected problem!\nRestart the application manually now!", //$NON-NLS-1$
									new Status(IStatus.ERROR,
											SSDToolPlugin.PLUGIN_ID,
											"Unexpected result of an attempt to close the Workbench")); //$NON-NLS-1$
				}
			} else {
				MessageDialog
						.openWarning(
								getShell(),
								Messages.getString("SSDToolPreferencePage.MessageDialog.title"), //$NON-NLS-1$
								String.format(
										Messages.getString("SSDToolPreferencePage.MessageDialog.text"), //$NON-NLS-1$
										recentWSBeforeNewSelection
												.split(BasicPlatformPreferenceConstants.RECENT_WORKSPACES_DELIMETER)[0],
										SSDToolUtils.getCurrentWorkspace()));
			}
		}
	}
}
