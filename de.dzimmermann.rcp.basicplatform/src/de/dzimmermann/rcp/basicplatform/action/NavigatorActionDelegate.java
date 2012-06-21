package de.dzimmermann.rcp.basicplatform.action;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

import de.dzimmermann.rcp.basicplatform.ui.util.Logger;
import de.dzimmermann.rcp.basicplatform.util.BasicPlatformPluginConstants;

/**
 * This {@link AbstractHandler} extension opens the plug-in navigator if it was
 * closed.
 * 
 * @author dzimmermann
 * @since SSDTool 0.0.0
 * @version 1.0
 */
public class NavigatorActionDelegate extends AbstractHandler
		implements IWorkbenchWindowActionDelegate {

	@SuppressWarnings("unused")
	private IWorkbenchWindow window;

	@Override
	public void run(IAction action) {
		try {
			IWorkbenchPage page = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			page.showView(BasicPlatformPluginConstants.NAVIGATOR_ID);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			Logger.logError(e);
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public void init(IWorkbenchWindow window) {
		this.window = window;
	}

	@Override
	public Object execute(ExecutionEvent event)
			throws org.eclipse.core.commands.ExecutionException {
		run(null);
		return null;
	}
}
