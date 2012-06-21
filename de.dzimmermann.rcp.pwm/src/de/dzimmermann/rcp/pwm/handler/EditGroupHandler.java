package de.dzimmermann.rcp.pwm.handler;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import de.dzimmermann.rcp.basicplatform.util.DirtyEditorSupport;
import de.dzimmermann.rcp.basicplatform.util.ElementUpdateSupport;
import de.dzimmermann.rcp.pwm.model.container.PWMContainer;
import de.dzimmermann.rcp.pwm.model.container.PWMContainerGroup;
import de.dzimmermann.rcp.pwm.model.content.PWMGroup;
import de.dzimmermann.rcp.pwm.ui.dialog.EditGroupDialog;
import de.dzimmermann.rcp.pwm.ui.editor.PWMEditor;
import de.dzimmermann.rcp.pwm.util.PWMUtils;

public class EditGroupHandler extends AbstractHandler implements IHandler {

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

				if (editor instanceof ElementUpdateSupport)
					((ElementUpdateSupport) editor)
							.updateElement(StructuredSelection.EMPTY);

				EditGroupDialog dialog = new EditGroupDialog(editor.getSite()
						.getShell(), PWMUtils.findParentPWMObject(container,
						group), group);
				if (dialog.open() == TitleAreaDialog.CANCEL)
					return null;

				try {
					PWMGroup content = PWMUtils.loadPWMGroup(group,
							dialog.getCurrentPassword());
					String name = dialog.getGroupName();
					if (name != null) {
						group.setName(name);
						content.setName(name);
					}
					PWMUtils.savePWMGroup(
							group,
							content,
							dialog.getGroupPassword() != null ? dialog
									.getGroupPassword() : dialog
									.getCurrentPassword());
				} catch (JAXBException e) {
					throw new ExecutionException(e.getMessage(), e);
				} catch (IOException e) {
					throw new ExecutionException(e.getMessage(), e);
				} catch (Exception e) {
					throw new ExecutionException(e.getMessage(), e);
				}

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
