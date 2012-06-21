package de.dzimmermann.rcp.basicplatform.util;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.UIJob;

import de.dzimmermann.rcp.basicplatform.model.BasicPlatformSessionModel;
import de.dzimmermann.rcp.basicplatform.ui.dialog.UserAuthenticationDialog;
import de.dzimmermann.rcp.basicplatform.ui.util.Logger;

/**
 * This class represents a specific authenticator (for username and password),
 * which is called when there is no current
 * 
 * @author dzimmermann
 * @since SSDTool (PCP) v0.4.3
 * @version 0.2
 */
public class InternalAuthenticator extends Authenticator {

	/**
	 * this boolean is used only for the dialog calls, to identify whether
	 * cancel was pressed or not
	 */
	private boolean cancel = false;

	/**
	 * this value represents the current user name
	 */
	private String userName;
	/**
	 * this value represents the current password
	 */
	private String password;

	/**
	 * Default Constructor for the purpose to create a new {@link Authenticator}
	 * , used for copyies and so on
	 */
	public InternalAuthenticator() {
	}

	public InternalAuthenticator(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public PasswordAuthentication requestAuthentication() {
		return getPasswordAuthentication();
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {

		userName = BasicPlatformSessionModel.getInstance().getUserName();
		password = BasicPlatformSessionModel.getInstance().getPassword();

		if (userName == null || userName.isEmpty() || password == null) {

			// retrieve current data from the preferences
			final InternalAuthenticator current = SSDToolUtils
					.getAuthentication();

			// set them into this object
			userName = current.getUserName();
			password = current.getPassword();

			// reset cancel
			cancel = false;

			// XXX The following code is only a simple approach and need to be
			// fixed

			// it seams, that we are currently not in an UI-Thread...
			if (PlatformUI.getWorkbench().getActiveWorkbenchWindow() == null) {

				// encapsulate the call for the dialog within a UIJob ...
				UIJob uiJob = new UIJob(
						Messages.getString("PCPAuthenticator.UIJob.authenticate")) { //$NON-NLS-1$
					@Override
					public IStatus runInUIThread(IProgressMonitor monitor) {
						cancel = !callDialog(current);
						return Status.OK_STATUS;
					}
				};

				// ... schedule it ...
				uiJob.schedule();

				// ... and wait until it's finished
				try {
					uiJob.join();
				} catch (InterruptedException e1) {
					Logger.logError(e1);
					System.err.println(e1.getMessage());
				}
			}
			// ... otherwise, we should be in an UI-Thread, and there should be
			// no
			// other complication
			else {
				// simply call for the dialog
				cancel = !callDialog(current);
			}

			// cancel was pressed - return
			// (it is most likely, that an exception will be thrown on this
			// action)
			if (cancel) {
				return null;
			}
		}

		BasicPlatformSessionModel.getInstance().setUserName(userName);
		BasicPlatformSessionModel.getInstance().setPassword(password);

		// cancel was not pressed, return the password authentication object
		return new PasswordAuthentication(userName,
				password != null ? password.toCharArray() : "".toCharArray()); //$NON-NLS-1$
	}

	/**
	 * This method is used to call the user interaction dialog for the
	 * authentication. When finished, it stores the (new) data to the
	 * preferences.
	 * 
	 * @param current
	 *            the current values used for the authentication as specified by
	 *            the preferences
	 * @return <code>true</code>, if the process was successfull;
	 *         <code>false</code>, if cancel was pressed
	 */
	private boolean callDialog(InternalAuthenticator current) {

		Shell shell = null;
		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		if (page != null)
			shell = page.getActivePart().getSite().getShell();
		if (shell == null)
			shell = Display.getDefault().getActiveShell();

		// create instance of the authentication dialog
		UserAuthenticationDialog dialog = new UserAuthenticationDialog(shell,
				current);

		// open it and test the return code
		if (TitleAreaDialog.OK == dialog.open()) {

			// set the username and password as specified by the dialog
			userName = dialog.getUserName();
			password = dialog.getPassword();

			// store the new preferences
			SSDToolUtils.setStoreAuthenticationPassword(dialog
					.isStorePassword());
			SSDToolUtils.storeAuthentication(InternalAuthenticator.this);

			return true;
		} else {
			return false;
		}
	}
}
