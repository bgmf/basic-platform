package de.dzimmermann.rcp.basicplatform.ui.view.action;

import java.util.HashMap;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;

import de.dzimmermann.rcp.basicplatform.model.Task;
import de.dzimmermann.rcp.basicplatform.model.TaskEditorInput;
import de.dzimmermann.rcp.basicplatform.model.TaskImplementationType;
import de.dzimmermann.rcp.basicplatform.ui.util.Logger;
import de.dzimmermann.rcp.basicplatform.util.InternalPluginHandlerTrigger;
import de.dzimmermann.rcp.basicplatform.util.Messages;
import de.dzimmermann.rcp.basicplatform.util.SSDToolUtils;

/**
 * This action handles the default usage of the {@link CommonNavigator} derivate
 * {@link CNFNavigatorView}.<br>
 * This means: It opens editors, views, dialogs and wizards.<br>
 * Additionally it opens or collapses a {@link Task} with
 * {@link TaskImplementationType#NONE}.
 * 
 * @author dzimmermann
 * @since SSDTool 0.0.0
 * @version 1.2
 */
public class OpenTaskRelatedWidgetCNFAction extends Action {

	private final static String ACTION_TEXT = Messages.getString("OpenTaskRelatedWidgetCNFAction.action.text"); //$NON-NLS-1$
	private final static String ACTION_TOOLTIP_TEXT = Messages.getString("OpenTaskRelatedWidgetCNFAction.action.tooltiptext"); //$NON-NLS-1$

	private IWorkbenchPage page;
	private ISelectionProvider provider;
	private ISelection selection;
	private StructuredViewer viewer;

	private Task task;

	public OpenTaskRelatedWidgetCNFAction(IWorkbenchPage workbenchPage,
			ISelectionProvider selectionProvider,
			StructuredViewer commonNavigatorViewer) {
		setText(ACTION_TEXT);
		setToolTipText(ACTION_TOOLTIP_TEXT);
		page = workbenchPage;
		provider = selectionProvider;
		viewer = commonNavigatorViewer;
	}

	public OpenTaskRelatedWidgetCNFAction(IWorkbenchPage workbenchPage,
			ISelection selection, StructuredViewer commonNavigatorViewer) {
		setText(ACTION_TEXT);
		setToolTipText(ACTION_TOOLTIP_TEXT);
		page = workbenchPage;
		this.selection = selection;
		viewer = commonNavigatorViewer;
	}

	@Override
	public boolean isEnabled() {
		ISelection selection = null;

		if (this.selection != null) {
			selection = this.selection;
		} else if (provider != null) {
			selection = provider.getSelection();
		}

		if (selection != null && !selection.isEmpty()) {
			IStructuredSelection sSelection = (IStructuredSelection) selection;
			if (sSelection.size() == 1
					&& sSelection.getFirstElement() instanceof Task) {
				task = ((Task) sSelection.getFirstElement());
				return true;
			}
		}
		return false;
	}

	public boolean hasChildren(Task t) {
		if (t.getChildren() != null && !t.getChildren().isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void run() {

		try {
			if (isEnabled()) {

				if (hasChildren(task)) {

					boolean isExpandable = false;
					boolean expanded = false;

					if (viewer instanceof TreeViewer) {
						isExpandable = ((TreeViewer) viewer).isExpandable(task);
						expanded = ((TreeViewer) viewer).getExpandedState(task);
					}

					if (isExpandable) {
						if (viewer instanceof TreeViewer) {
							((TreeViewer) viewer).setExpandedState(task,
									!expanded);
						}
					}
				}

				if (task.getContextToLoadID() == null)
					return;

				int returnCode = IStatus.OK;

				switch (task.getTaskImplementationType()) {
				case NONE:
					break;
				case EDITOR:
					openEditor(page, task);
					break;
				case VIEW:
					openView(page, task);
					break;
				case DIALOG:
					returnCode = openDialog(task, page.getWorkbenchWindow()
							.getShell());
					if (TitleAreaDialog.OK == returnCode) {
						// do something
					} else if (TitleAreaDialog.CANCEL == returnCode) {
						// do something
					}
					break;
				case WIZARD:
					returnCode = openWizard(task, page.getWorkbenchWindow()
							.getShell());

					if (IStatus.OK == returnCode) {
						// do something
					} else if (IStatus.INFO == returnCode) {
						// do something
					} else if (IStatus.WARNING == returnCode) {
						// do something
					} else if (IStatus.ERROR == returnCode) {
						throw new PartInitException(
								"An error occured on loading an PFSManager widget!\n" //$NON-NLS-1$
										+ task.toString());
					}
					break;
				case HANDLER:
					Object result = openHandler(task);
					if (result != null) {
						// do something
					}
					break;
				}
			}
		} catch (PartInitException e) {
			e.printStackTrace();
			Logger.logError(e);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.logError(e);
		}
	}

	private void openView(IWorkbenchPage page, Task task)
			throws PartInitException {

		page.showView(task.getContextToLoadID());
	}

	private void openEditor(IWorkbenchPage page, Task task)
			throws PartInitException {

		TaskEditorInput input = null;

		try {

			input = SSDToolUtils.getTaskEditorInputByTask(task);

			if (input == null) {
				input = new TaskEditorInput(task);
			}

		} catch (Exception e) {
			throw new PartInitException(
					"Couldn't load the IEditorInput implementation " //$NON-NLS-1$
							+ task.getTaskEditorInput() + "\n" //$NON-NLS-1$
							+ e.getClass().getName() + ": " //$NON-NLS-1$
							+ e.getLocalizedMessage(), e.getCause());
		}

		page.openEditor(input, task.getContextToLoadID(), true, IWorkbenchPage.MATCH_ID);
	}

	private int openDialog(Task task, Shell parentShell) throws Exception {

		TitleAreaDialog tad = SSDToolUtils.getTitleAreaDialogByTask(task,
				parentShell);

		if (tad == null) {
			throw new PartInitException("Cannot open dialog (" + task.getContextToLoadID() //$NON-NLS-1$
					+ ")"); //$NON-NLS-1$
		}

		return tad.open();
	}

	private int openWizard(Task task, Shell parentShell) throws Exception {

		final Wizard wiz = SSDToolUtils.getWizardByTask(task);

		if (wiz == null) {
			throw new PartInitException("Cannot open wizard (" + task.getContextToLoadID() //$NON-NLS-1$
					+ ")"); //$NON-NLS-1$
		}

		WizardDialog wizDialog = new WizardDialog(parentShell, wiz);

		return wizDialog.open();
	}

	private Object openHandler(Task task) throws Exception {

		final AbstractHandler handler = SSDToolUtils.getHandlerByTask(task);

		if (handler == null) {
			throw new PartInitException("Cannot open handler (" + task.getContextToLoadID() //$NON-NLS-1$
					+ ")"); //$NON-NLS-1$
		}

		ExecutionEvent event = new ExecutionEvent(null,
				new HashMap<String, String>(),
				new InternalPluginHandlerTrigger(task), this);
		return handler.execute(event);
	}
}
