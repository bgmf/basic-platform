package de.dzimmermann.rcp.pwm.handler;

import java.io.IOException;

import javax.xml.bind.JAXBException;

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
import de.dzimmermann.rcp.pwm.model.container.PWMContainerGroup;
import de.dzimmermann.rcp.pwm.model.content.PWMGroup;
import de.dzimmermann.rcp.pwm.model.content.PWMGroupEntry;
import de.dzimmermann.rcp.pwm.ui.editor.PWMEditor;
import de.dzimmermann.rcp.pwm.util.PWMUtils;

public class RemoveEntryHandler extends AbstractHandler implements IHandler {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		IEditorPart editor = page.getActiveEditor();

		if (PWMEditor.ID.equals(editor.getEditorSite().getId())) {

			PWMContainerGroup group = (PWMContainerGroup) editor
					.getAdapter(PWMContainerGroup.class);
			PWMGroup content = (PWMGroup) editor.getAdapter(PWMGroup.class);
			PWMGroupEntry entry = (PWMGroupEntry) editor
					.getAdapter(PWMGroupEntry.class);

			if (entry != null) {

				boolean delete = MessageDialog.openConfirm(editor.getSite()
						.getShell(), "PWM - Remove Entry", String.format(
						"Are you sure, that you want to remove the entry '%s'",
						entry.getName()));
				if (!delete)
					return null;

				content.getEntries().remove(entry);

				try {

					PWMUtils.savePWMGroup(group, content,
							(String) editor.getAdapter(String.class));

					if (editor instanceof ElementUpdateSupport)
						((ElementUpdateSupport) editor).updateElement(content);
					if (editor instanceof DirtyEditorSupport)
						((DirtyEditorSupport) editor).setDirty();

				} catch (JAXBException e) {
					throw new ExecutionException(e.getMessage(), e);
				} catch (IOException e) {
					throw new ExecutionException(e.getMessage(), e);
				} catch (Exception e) {
					throw new ExecutionException(e.getMessage(), e);
				}
			}
		}

		return null;
	}
}
