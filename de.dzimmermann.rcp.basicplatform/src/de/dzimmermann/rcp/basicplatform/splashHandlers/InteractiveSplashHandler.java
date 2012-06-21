package de.dzimmermann.rcp.basicplatform.splashHandlers;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.splash.AbstractSplashHandler;

import de.dzimmermann.rcp.basicplatform.ui.dialog.OpenWorkpaceDialog;
import de.dzimmermann.rcp.basicplatform.util.SSDToolUtils;

/**
 * This little Splash Handler is created from an example template.<br>
 * It now shows only a dialog where the user can choose a workspace folder. <br>
 * Deprecated, since this doesn't support a cancel behavior to shutdown the
 * complete app.
 * 
 * @author dzimmermann
 * @deprecated
 */
@Deprecated
public class InteractiveSplashHandler extends AbstractSplashHandler {

	public InteractiveSplashHandler() {
	}

	@Override
	public void init(final Shell splash) {
		// Store the shell
		super.init(splash);

		// // Configure the shell layout
		// configureUISplash();

		if (!SSDToolUtils.isShowWorkspaceSelectionDialog()) {
			return;
		}

		OpenWorkpaceDialog dialog = new OpenWorkpaceDialog(
				getSplash());

		if (dialog.open() == TitleAreaDialog.OK) {
			return;
		} else {
			System.exit(0);
			// PlatformUI.getWorkbench().close();
			return;
		}
	}

	@SuppressWarnings("unused")
	private void configureUISplash() {
		// Configure layout
		FillLayout layout = new FillLayout();
		getSplash().setLayout(layout);
		// Force shell to inherit the splash background
		getSplash().setBackgroundMode(SWT.INHERIT_DEFAULT);
	}

	// <extension
	// point="org.eclipse.ui.splashHandlers">
	// <splashHandler
	// class="de.dzimmermann.rcp.basicplatform.splashHandlers.InteractiveSplashHandler"
	// id="de.dzimmermann.rcp.basicplatform.splashHandlers.interactive">
	// </splashHandler>
	// <splashHandlerProductBinding
	// productId="de.dzimmermann.rcp.basicplatform.product"
	// splashId="de.dzimmermann.rcp.basicplatform.splashHandlers.interactive">
	// </splashHandlerProductBinding>
	// </extension>
}
