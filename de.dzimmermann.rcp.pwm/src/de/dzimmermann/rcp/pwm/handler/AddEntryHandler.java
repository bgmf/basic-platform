package de.dzimmermann.rcp.pwm.handler;

import java.io.IOException;
import java.util.Calendar;

import javax.xml.bind.JAXBException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import de.dzimmermann.rcp.basicplatform.util.DirtyEditorSupport;
import de.dzimmermann.rcp.basicplatform.util.ElementUpdateSupport;
import de.dzimmermann.rcp.pwm.model.container.PWMContainerGroup;
import de.dzimmermann.rcp.pwm.model.content.PWMGroup;
import de.dzimmermann.rcp.pwm.model.content.PWMGroupEntry;
import de.dzimmermann.rcp.pwm.ui.dialog.AddEditEntryDialog;
import de.dzimmermann.rcp.pwm.ui.editor.PWMEditor;
import de.dzimmermann.rcp.pwm.util.PWMUtils;

public class AddEntryHandler extends AbstractHandler implements IHandler {

	protected boolean edit = false;

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

			entry = !edit ? new PWMGroupEntry()
					: (entry == null ? new PWMGroupEntry() : entry);

			AddEditEntryDialog dialog = new AddEditEntryDialog(editor.getSite()
					.getShell(), entry);

			if (dialog.open() == TitleAreaDialog.CANCEL)
				return null;

			PWMGroupEntry result = dialog.getResult();

			boolean modified = false;

			if (result.getName() != null
					&& !result.getName().equals(entry.getName())) {
				entry.setName(result.getName());
				modified = true;
			}
			if (result.getUsername() != null
					&& !result.getUsername().equals(entry.getUsername())) {
				entry.setUsername(result.getUsername());
				modified = true;
			}
			if (result.getPassword() != null
					&& !result.getPassword().equals(entry.getPassword())) {
				entry.setPassword(result.getPassword());
				modified = true;
			}
			if (result.getUrl() != null
					&& !result.getUrl().equals(entry.getUrl())) {
				entry.setUrl(result.getUrl());
				modified = true;
			}
			if (result.getDescription() != null
					&& !result.getDescription().equals(entry.getDescription())) {
				entry.setDescription(result.getDescription());
				modified = true;
			}
			if (result.getDateExpiration() != null
					&& !result.getDateExpiration().equals(
							entry.getDateExpiration())) {
				entry.setDateExpiration(result.getDateExpiration());
				modified = true;
			}

			Calendar c = Calendar.getInstance();

			if (entry.getId() == null || entry.getId().isEmpty()) {
				entry.setId(PWMUtils.getPWMID(entry.getName(), c));
				modified = true;
			}
			if (entry.getDateAdded() == null) {
				entry.setDateAdded(c);
				modified = true;
			}
			if (modified)
				entry.setDateModified(c);

			if (!edit) {
				content.getEntries().add(entry);
				modified = true;
			}

			if (modified) {
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
