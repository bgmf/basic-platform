package de.dzimmermann.rcp.basicplatform.ui.component;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.dzimmermann.rcp.basicplatform.SSDToolPlugin;
import de.dzimmermann.rcp.basicplatform.util.Messages;
import de.dzimmermann.rcp.basicplatform.util.SSDToolUtils;

public class ServerSelectionComposite extends Composite {

	public static final String IS_VALID_SERVER_SELECTED = ServerSelectionComposite.class
			.getName() + ".IS_VALID_SERVER_SELECTED";

	private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	private Combo serverSelectionCombo;
	private Button allowAlternativeButton;
	private Text alternativeServerText;

	private String currentRemoteServer = "";
	private String alternativeServer = "";
	private boolean useAlternativeServer = false;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public ServerSelectionComposite(Composite parent, int style) {

		super(parent, style);

		setLayout(new GridLayout(2, false));

		// create remote server selection part
		// complete with initializing, adding listeners, ...

		// create widgets
		Label serverSelectionLabel = new Label(this, SWT.NONE);
		serverSelectionLabel
				.setText(Messages
						.getString("OpenSSDToolWorkpaceDialog.severSelectionLabel.Text")); //$NON-NLS-1$
		GridData gd_serverSelectionLabel = new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1);
		serverSelectionLabel.setLayoutData(gd_serverSelectionLabel);

		serverSelectionCombo = new Combo(this, SWT.READ_ONLY);
		GridData gd_serverSelectionCombo = new GridData(SWT.LEFT, SWT.CENTER,
				true, false, 1, 1);
		gd_serverSelectionCombo.widthHint = 500;
		serverSelectionCombo.setLayoutData(gd_serverSelectionCombo);
		serverSelectionCombo
				.setToolTipText(Messages
						.getString("OpenSSDToolWorkpaceDialog.serverSelectionCombo.ToolTipText")); //$NON-NLS-1$

		serverSelectionCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				currentRemoteServer = serverSelectionCombo.getItems()[serverSelectionCombo
						.getSelectionIndex()];
				boolean valid = currentRemoteServer != null
						&& !currentRemoteServer.isEmpty();
				pcs.firePropertyChange(IS_VALID_SERVER_SELECTED, null,
						Boolean.valueOf(valid));
			}
		});

		// get latest server
		String recentRemote = SSDToolUtils.getRecentRemoteServer();

		// fill the combo with all available servers
		if (SSDToolPlugin.getDefault().getSSDToolModel().getServerList() != null) {

			String[] remoteServerNames = new String[SSDToolPlugin.getDefault()
					.getSSDToolModel().getServerList().keySet().size()];

			int i = 0;
			for (String name : SSDToolPlugin.getDefault().getSSDToolModel()
					.getServerList().keySet()) {
				remoteServerNames[i] = name;
				i++;
			}

			Arrays.sort(remoteServerNames);
			serverSelectionCombo.setItems(remoteServerNames);

			int index = -1;

			if (recentRemote != null) {
				for (int j = 0; j < remoteServerNames.length; j++) {
					String available = remoteServerNames[j];
					if (available.equals(recentRemote)) {
						index = j;
						break;
					}
				}
			} else {
				if (remoteServerNames.length > 0) {
					index = 0;
				}
			}

			serverSelectionCombo.select(index);
		}

		serverSelectionCombo.setEnabled(!SSDToolUtils
				.isUseAlternativeServerHost());
		useAlternativeServer = SSDToolUtils.isUseAlternativeServerHost();

		// alternative remote server setup
		if (SSDToolPlugin.getDefault().isDeveloperMode()) {
			allowAlternativeButton = new Button(this, SWT.CHECK);
			allowAlternativeButton.setLayoutData(new GridData(SWT.FILL,
					SWT.CENTER, false, false, 1, 1));
			allowAlternativeButton
					.setText(Messages
							.getString("OpenSSDToolWorkpaceDialog.allowAlternativeButton.Text")); //$NON-NLS-1$
			allowAlternativeButton
					.setToolTipText(Messages
							.getString("OpenSSDToolWorkpaceDialog.allowAlternativeButton.ToolTipText")); //$NON-NLS-1$

			alternativeServerText = new Text(this, SWT.BORDER);
			alternativeServerText.setLayoutData(new GridData(SWT.FILL,
					SWT.FILL, true, false, 1, 1));
			alternativeServerText
					.setMessage(Messages
							.getString("OpenSSDToolWorkpaceDialog.alternativeServerText.Message")); //$NON-NLS-1$

			allowAlternativeButton.setSelection(useAlternativeServer);

			allowAlternativeButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					alternativeServerText.setEnabled(allowAlternativeButton
							.getSelection());
					if (!allowAlternativeButton.getSelection()) {
						useAlternativeServer = false;
						serverSelectionCombo.setEnabled(true);
						currentRemoteServer = serverSelectionCombo.getItems()[serverSelectionCombo
								.getSelectionIndex()];
						boolean valid = currentRemoteServer != null
								&& !currentRemoteServer.isEmpty();
						pcs.firePropertyChange(IS_VALID_SERVER_SELECTED, null,
								Boolean.valueOf(valid));
					} else {
						useAlternativeServer = true;
						serverSelectionCombo.setEnabled(false);
						alternativeServer = alternativeServerText.getText();
						boolean valid = alternativeServer != null
								&& !alternativeServer.isEmpty();
						pcs.firePropertyChange(IS_VALID_SERVER_SELECTED, null,
								Boolean.valueOf(valid));
					}
				}
			});

			alternativeServerText.addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					alternativeServer = alternativeServerText.getText();
					boolean valid = alternativeServer != null
							&& !alternativeServer.isEmpty();
					pcs.firePropertyChange(IS_VALID_SERVER_SELECTED, null,
							Boolean.valueOf(valid));
				}
			});

			alternativeServerText.setEnabled(allowAlternativeButton
					.getSelection());
		}

		if (useAlternativeServer) {
			alternativeServerText.setText(SSDToolUtils
					.getAlternativeServerHost());
			alternativeServer = SSDToolUtils.getAlternativeServerHost();
			boolean valid = alternativeServer != null
					&& !alternativeServer.isEmpty();
			pcs.firePropertyChange(IS_VALID_SERVER_SELECTED, null,
					Boolean.valueOf(valid));
		} else {
			currentRemoteServer = SSDToolUtils.getRecentRemoteServer();
			boolean valid = currentRemoteServer != null
					&& !currentRemoteServer.isEmpty();
			pcs.firePropertyChange(IS_VALID_SERVER_SELECTED, null,
					Boolean.valueOf(valid));
		}

		// set tab list
		List<Control> controlList = new ArrayList<Control>();
		// remote server
		{
			if (serverSelectionCombo != null) {
				controlList.add(serverSelectionCombo);
			}
			if (allowAlternativeButton != null) {
				controlList.add(allowAlternativeButton);
			}
			if (alternativeServerText != null) {
				controlList.add(alternativeServerText);
			}
		}
		this.setTabList(controlList.toArray(new Control[0]));
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public String getCurrentRemoteServer() {
		return currentRemoteServer;
	}

	public String getAlternativeServer() {
		return alternativeServer;
	}

	public boolean isUseAlternativeServer() {
		return useAlternativeServer;
	}

	public void checkServerSelectionValidityAndFireEvent() {
		if (!useAlternativeServer) {
			boolean valid = currentRemoteServer != null
					&& !currentRemoteServer.isEmpty();
			pcs.firePropertyChange(IS_VALID_SERVER_SELECTED, null,
					Boolean.valueOf(valid));
		} else {
			boolean valid = alternativeServer != null
					&& !alternativeServer.isEmpty();
			pcs.firePropertyChange(IS_VALID_SERVER_SELECTED, null,
					Boolean.valueOf(valid));
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}
}
