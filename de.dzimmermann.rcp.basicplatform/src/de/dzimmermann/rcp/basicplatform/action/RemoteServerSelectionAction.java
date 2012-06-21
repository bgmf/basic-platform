package de.dzimmermann.rcp.basicplatform.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;

import de.dzimmermann.rcp.basicplatform.SSDToolPlugin;
import de.dzimmermann.rcp.basicplatform.model.BasicPlatformSessionModel;
import de.dzimmermann.rcp.basicplatform.ui.dialog.SelectRemoteServerDialog;
import de.dzimmermann.rcp.basicplatform.ui.view.FormsPluginNavigatorView;
import de.dzimmermann.rcp.basicplatform.util.Messages;
import de.dzimmermann.rcp.basicplatform.util.SSDToolImageRegistryLookup;

public class RemoteServerSelectionAction extends Action {

	public static final String REMOTE_SERVER_ACTION_ITEM_ID = "de.dzimmermann.rcp.basicplatform.remote_system.action"; //$NON-NLS-1$

	private Image editEnabledIcon = SSDToolPlugin.getDefault()
			.getImageRegistry()
			.get(SSDToolImageRegistryLookup.EDIT_ENABLED.getKey());
	private Image editDisabledIcon = SSDToolPlugin.getDefault()
			.getImageRegistry()
			.get(SSDToolImageRegistryLookup.EDIT_DISABLED.getKey());

	private Shell target;

	public RemoteServerSelectionAction() {

		// super(SSDToolUtils.getRecentRemoteServer() == null
		// || SSDToolUtils.getRecentRemoteServer().isEmpty() ? Messages
		// .getString("RemoteServerSelectionAction.util.noremoteServer") //$NON-NLS-1$
		// : SSDToolUtils.getRecentRemoteServer(),
		// IAction.AS_DROP_DOWN_MENU);
		super(
				BasicPlatformSessionModel.getInstance().getRemoteServerName() == null
						|| BasicPlatformSessionModel.getInstance()
								.getRemoteServerName().isEmpty() ? Messages
						.getString("RemoteServerSelectionAction.util.noremoteServer") //$NON-NLS-1$
						: BasicPlatformSessionModel.getInstance()
								.getRemoteServerName(),
				IAction.AS_DROP_DOWN_MENU);

		setId(REMOTE_SERVER_ACTION_ITEM_ID);

		// String remoteServer = SSDToolUtils.getRecentRemoteServer();
		// String alternativeServerHost =
		// SSDToolUtils.getAlternativeServerHost();
		String remoteServer = BasicPlatformSessionModel.getInstance()
				.getRemoteServerName();
		String alternativeServerHost = BasicPlatformSessionModel.getInstance()
				.getRemoteServerUrl();

		// remoteServer = SSDToolUtils.isUseAlternativeServerHost() ?
		// alternativeServerHost
		// : (remoteServer != null && !remoteServer.isEmpty() ? remoteServer
		// : Messages.getString("RemoteServerSelectionAction.remoteServer.noremote")); //$NON-NLS-1$
		remoteServer = BasicPlatformSessionModel.getInstance()
				.isRemoteServerAlternative() ? alternativeServerHost
				: (remoteServer != null && !remoteServer.isEmpty() ? remoteServer
						: Messages
								.getString("RemoteServerSelectionAction.remoteServer.noremote")); //$NON-NLS-1$

		setText(remoteServer);
		setToolTipText(Messages
				.getString("RemoteServerSelectionAction.ToolTipText") //$NON-NLS-1$
				+ remoteServer);
		setEnabled(true);
	}

	public RemoteServerSelectionAction(Shell target) {
		this();
		this.target = target;
	}

	@Override
	public void run() {

		if (target == null) {
			target = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().getActivePart().getSite().getShell();
		}

		SelectRemoteServerDialog dialog = new SelectRemoteServerDialog(target);

		if (dialog.open() == TitleAreaDialog.OK) {

			IViewReference[] viewReferences = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage()
					.getViewReferences();

			setEnabled(true);

			// String remoteServer = SSDToolUtils.getRecentRemoteServer();
			// String alternativeServerHost = SSDToolUtils
			// .getAlternativeServerHost();
			String remoteServer = BasicPlatformSessionModel.getInstance()
					.getRemoteServerName();
			String alternativeServerHost = BasicPlatformSessionModel
					.getInstance().getRemoteServerUrl();

			// remoteServer = SSDToolUtils.isUseAlternativeServerHost() ?
			// alternativeServerHost
			// : (remoteServer != null && !remoteServer.isEmpty() ? remoteServer
			// : Messages.getString("RemoteServerSelectionAction.remoteserver.util.noremote")); //$NON-NLS-1$
			remoteServer = BasicPlatformSessionModel.getInstance()
					.isRemoteServerAlternative() ? alternativeServerHost
					: (remoteServer != null && !remoteServer.isEmpty() ? remoteServer
							: Messages
									.getString("RemoteServerSelectionAction.remoteserver.util.noremote")); //$NON-NLS-1$

			setText(remoteServer);
			setToolTipText(Messages
					.getString("RemoteServerSelectionAction.ToolTipText2") //$NON-NLS-1$
					+ remoteServer);

			for (IViewReference ivr : viewReferences) {

				if (ivr.getId().equals(FormsPluginNavigatorView.ID)) {
					IStatusLineManager statusLineMgr = ivr.getView(true)
							.getViewSite().getActionBars()
							.getStatusLineManager();

					ActionContributionItem actionItem = (ActionContributionItem) statusLineMgr
							.find(REMOTE_SERVER_ACTION_ITEM_ID);
					actionItem.update(IAction.IMAGE);
					actionItem.update(IAction.TEXT);
					actionItem.update(IAction.TOOL_TIP_TEXT);

					if (actionItem.getWidget() instanceof Button) {
						Button b = (Button) actionItem.getWidget();
						b.update();
						b.getParent().layout(true, true);
					}
				}
			}
		}
	}

	@Override
	public void runWithEvent(Event event) {
		if (SWT.Selection == event.type) {
			run();
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		if (enabled)
			setImageDescriptor(ImageDescriptor.createFromImage(editEnabledIcon));
		else
			setImageDescriptor(ImageDescriptor
					.createFromImage(editDisabledIcon));
		super.setEnabled(enabled);
	}
}