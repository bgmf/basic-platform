package de.dzimmermann.rcp.basicplatform.action;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.StatusLineLayoutData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import de.dzimmermann.rcp.basicplatform.model.BasicPlatformSessionModel;
import de.dzimmermann.rcp.basicplatform.util.Messages;
import de.dzimmermann.rcp.basicplatform.util.SWTResourceManager;

public class RemoteServerContributionItem extends ContributionItem {

	public static final String REMOTE_SERVER_LABEL_ITEM_ID = "de.dzimmermann.rcp.basicplatform.remote_system.label"; //$NON-NLS-1$

	public RemoteServerContributionItem() {
		setId(REMOTE_SERVER_LABEL_ITEM_ID);
	}

	@Override
	public void fill(Composite parent) {

		Composite c = new Composite(parent, SWT.NONE);
		StatusLineLayoutData slld_c = new StatusLineLayoutData();
		c.setLayoutData(slld_c);
		c.setLayout(new GridLayout(1, true));

		Label serverLabel = new Label(c, SWT.NONE);
		serverLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true,
				true));

		String remoteServer = BasicPlatformSessionModel.getInstance()
				.getRemoteServerName();
		String alternativeServerHost = BasicPlatformSessionModel.getInstance()
				.getRemoteServerUrl();

		remoteServer = BasicPlatformSessionModel.getInstance()
				.isRemoteServerAlternative() ? alternativeServerHost
				: (remoteServer != null && !remoteServer.isEmpty() ? remoteServer
						: Messages
								.getString("RemoteServerSelectionAction.remoteServer.noremote")); //$NON-NLS-1$

		serverLabel.setForeground(SWTResourceManager.getColor(0, 120, 178));
		serverLabel.setText(remoteServer);
	}
}
