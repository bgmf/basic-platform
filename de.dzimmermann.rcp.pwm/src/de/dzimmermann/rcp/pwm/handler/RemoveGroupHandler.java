package de.dzimmermann.rcp.pwm.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import de.dzimmermann.rcp.basicplatform.util.DirtyEditorSupport;
import de.dzimmermann.rcp.basicplatform.util.ElementUpdateSupport;
import de.dzimmermann.rcp.pwm.model.container.PWMContainer;
import de.dzimmermann.rcp.pwm.model.container.PWMContainerGroup;
import de.dzimmermann.rcp.pwm.ui.editor.PWMEditor;
import de.dzimmermann.rcp.pwm.util.PWMUtils;

public class RemoveGroupHandler extends AbstractHandler implements IHandler {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		IEditorPart editor = page.getActiveEditor();

		if (PWMEditor.ID.equals(editor.getEditorSite().getId())) {

			PWMContainer container = (PWMContainer) editor
					.getAdapter(PWMContainer.class);
			PWMContainerGroup group = (PWMContainerGroup) editor
					.getAdapter(PWMContainerGroup.class);

			if (group != null) {

				boolean delete = MessageDialog.openConfirm(editor.getSite()
						.getShell(), "PWM - Remove Group", String.format(
						"Are you sure, that you want to remove the group '%s'",
						group.getName()));
				if (!delete)
					return null;

				Object parent = PWMUtils.findParentPWMObject(container, group);
				if (parent == null)
					return null;
				else if (parent instanceof PWMContainer)
					container.getGroups().remove(group);
				else if (parent instanceof PWMContainerGroup)
					((PWMContainerGroup) parent).getGroups().remove(group);

				if (editor instanceof ElementUpdateSupport)
					((ElementUpdateSupport) editor).updateElement(container);

				if (editor instanceof DirtyEditorSupport)
					((DirtyEditorSupport) editor).setDirty();

			} else {
				MessageDialog.openInformation(editor.getSite().getShell(),
						"PWM - Operation not available",
						"Please select a group to proceed!");
			}
		}

		return null;
	}
}
