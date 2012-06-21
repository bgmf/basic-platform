package de.dzimmermann.rcp.bsgtaucha.mgt.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import de.dzimmermann.rcp.basicplatform.ui.util.Logger;
import de.dzimmermann.rcp.bsgtaucha.mgt.ui.dialog.ODFExportTreeWizardDialog;
import de.dzimmermann.rcp.bsgtaucha.mgt.ui.dialog.ODFExportTreeWizardDialog.ResultType;

public class ODFExportHandler extends AbstractHandler implements
		IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		Shell activeWorkbenchWindowShell = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getShell();

		ODFExportTreeWizardDialog wizard = new ODFExportTreeWizardDialog(
				activeWorkbenchWindowShell, SWT.DIALOG_TRIM
						| SWT.APPLICATION_MODAL | SWT.MAX | SWT.RESIZE);

		ResultType result = wizard.open();

		if (ResultType.DEFAULT == result) {
			// how to react on this? this should not be possible at all!
			String message = ODFExportTreeWizardDialog.class.getName()
					+ ": result state was \"" //$NON-NLS-1$
					+ ResultType.DEFAULT + "\"! That was not anticipated!"; //$NON-NLS-1$
			Logger.logWarning(message);
		} else if (ResultType.OK == result) {
			// TODO implement additional behavior in the ok-case if needed
		} else if (ResultType.CANCEL == result) {
			// TODO implement additional behavior in the cancel-case if needed
		}

		return null;
	}
}
