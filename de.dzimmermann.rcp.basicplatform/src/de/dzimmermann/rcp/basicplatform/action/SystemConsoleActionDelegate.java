package de.dzimmermann.rcp.basicplatform.action;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import de.dzimmermann.rcp.basicplatform.SSDToolPlugin;

/**
 * This {@link AbstractHandler} extension opens the SSDTool-Console.
 * 
 * @author dzimmermann
 * @since SSDTool 0.0.0
 * @version 1.0
 */
public class SystemConsoleActionDelegate extends AbstractHandler
		implements IWorkbenchWindowActionDelegate {

	@SuppressWarnings("unused")
	private IWorkbenchWindow window;

	@Override
	public void run(IAction action) {
		if (SSDToolPlugin.getDefault().getConsole() != null)
			SSDToolPlugin.getDefault().getConsole().showSystemConsole();
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
	public Object execute(ExecutionEvent event) throws ExecutionException {
		run(null);
		return null;
	}
}
