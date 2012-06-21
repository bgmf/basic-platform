package de.dzimmermann.rcp.pwm.handler;

import java.io.IOException;
import java.util.Calendar;

import javax.xml.bind.JAXBException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import de.dzimmermann.rcp.basicplatform.util.DirtyEditorSupport;
import de.dzimmermann.rcp.basicplatform.util.ElementUpdateSupport;
import de.dzimmermann.rcp.pwm.model.container.PWMContainer;
import de.dzimmermann.rcp.pwm.model.container.PWMContainerGroup;
import de.dzimmermann.rcp.pwm.model.content.PWMGroup;
import de.dzimmermann.rcp.pwm.ui.dialog.AddGroupDialog;
import de.dzimmermann.rcp.pwm.ui.editor.PWMEditor;
import de.dzimmermann.rcp.pwm.util.PWMUtils;

public class AddGroupHandler extends AbstractHandler implements IHandler {

	private boolean useGroup = false;

	public AddGroupHandler(boolean useGroup) {
		this.useGroup = useGroup;
	}

	public AddGroupHandler() {
		this(false);
	}

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

			if (!useGroup && container != null || useGroup && group != null) {

				String[] groupdata = getNewGroupName(page, useGroup ? group
						: container);
				if (groupdata == null || groupdata[0] == null) // no name
					return null;

				String id = PWMUtils.getPWMID(groupdata[0],
						Calendar.getInstance());

				PWMContainerGroup newGroup = new PWMContainerGroup();
				newGroup.setId(id);
				newGroup.setName(groupdata[0]);

				PWMGroup newContent = new PWMGroup();
				newContent.setId(id);
				newContent.setName(groupdata[0]);

				try {
					PWMUtils.savePWMGroup(newGroup, newContent, groupdata[1]);
				} catch (JAXBException e) {
					throw new ExecutionException(e.getMessage(), e);
				} catch (IOException e) {
					throw new ExecutionException(e.getMessage(), e);
				} catch (Exception e) {
					throw new ExecutionException(e.getMessage(), e);
				}

				if (!useGroup)
					container.getGroups().add(newGroup);
				else
					group.getGroups().add(newGroup);

				if (editor instanceof ElementUpdateSupport)
					((ElementUpdateSupport) editor).updateElement(container);

				if (editor instanceof DirtyEditorSupport)
					((DirtyEditorSupport) editor).setDirty();
			} else {
				if (useGroup)
					MessageDialog.openInformation(editor.getSite().getShell(),
							"PWM - Operation not available",
							"Please select a parent group to proceed!");
			}
		}

		return null;
	}

	private String[] getNewGroupName(IWorkbenchPage page, Object container) {
		AddGroupDialog dialog = new AddGroupDialog(page.getActiveEditor()
				.getEditorSite().getShell(), container);
		if (Dialog.OK == dialog.open()) {
			return new String[] { dialog.getGroupName(), dialog.getPassword() };
		}
		return null;
	}

	public static class AddSubGroupHandler extends AddGroupHandler {
		public AddSubGroupHandler() {
			super(true);
		}
	}
}
