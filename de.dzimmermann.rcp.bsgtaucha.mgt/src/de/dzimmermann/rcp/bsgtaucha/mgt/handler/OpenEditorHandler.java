package de.dzimmermann.rcp.bsgtaucha.mgt.handler;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import de.dzimmermann.rcp.basicplatform.handler.BasicPlatformStates;
import de.dzimmermann.rcp.basicplatform.model.BasicPlatformSessionModel;
import de.dzimmermann.rcp.basicplatform.model.Task;
import de.dzimmermann.rcp.basicplatform.model.TaskEditorInput;
import de.dzimmermann.rcp.basicplatform.ui.util.Logger;
import de.dzimmermann.rcp.bsgtaucha.mgt.Activator;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.localfile.LocalFileModel;
import de.dzimmermann.rcp.bsgtaucha.mgt.ui.editor.BandMgtEditor;
import de.dzimmermann.rcp.bsgtaucha.mgt.ui.editor.PersonMgtEditor;
import de.dzimmermann.rcp.bsgtaucha.mgt.ui.editor.TasksMgtEditor;
import de.dzimmermann.rcp.bsgtaucha.mgt.ui.editor.WorkEntryEditor;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.BSGTauchaConstants;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.BSGTauchaUtils;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.DesEncrypter;

public class OpenEditorHandler extends AbstractHandler {

	public static DesEncrypter desEncrypter = null;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();

		Task task = BasicPlatformStates.getTaskState();

		if (BSGTauchaConstants.BAND_TASK_ID.equals(task.getId()))
			openEditor(page, task, BandMgtEditor.ID);
		else if (BSGTauchaConstants.WORK_ENTRIES_TASK_ID.equals(task.getId()))
			openEditor(page, task, WorkEntryEditor.ID);
		else if (BSGTauchaConstants.PERSON_TASK_ID.equals(task.getId()))
			openEditor(page, task, PersonMgtEditor.ID);
		else if (BSGTauchaConstants.WORK_TASK_ID.equals(task.getId()))
			openEditor(page, task, TasksMgtEditor.ID);

		return null;
	}

	private final void openEditor(IWorkbenchPage page, Task task,
			String editorId) throws ExecutionException {

		Map<String, Object> pluginModels = BasicPlatformSessionModel
				.getInstance().getPluginModels();

		LocalFileModel model = (LocalFileModel) pluginModels
				.get(Activator.PLUGIN_ID);
		String defPW = (String) pluginModels.get(Activator.PLUGIN_ID
				+ BSGTauchaConstants.DEFAULT_PASSWORD_SUFFIX);
		// if (defPW != null) {
		desEncrypter = DesEncrypter.getInstance(defPW);
		// }
		if (model != null) {
			try {
				model.setModel(BSGTauchaUtils.openModel(model.getFileStore()
						.toLocalFile(EFS.NONE, null)));
			} catch (CoreException e) {
				Logger.logError(e);
			} catch (FileNotFoundException e) {
				Logger.logError(e);
			} catch (IOException e) {
				Logger.logError(e);
			} catch (JAXBException e) {
				Logger.logError(e);
			}
		}

		TaskEditorInput input = null;
		try {
			// input = SSDToolUtils.getTaskEditorInputByTask(task);
			// if (input == null)
			input = new InternalTaskEditorInput(task, model);

		} catch (Exception e) {
			throw new ExecutionException(
					"Couldn't load the IEditorInput implementation "
							+ task.getTaskEditorInput() + "\n"
							+ e.getClass().getName() + ": "
							+ e.getLocalizedMessage(), e.getCause());
		}
		try {
			page.openEditor(input, editorId, true, IWorkbenchPage.MATCH_ID);
		} catch (PartInitException e) {
			throw new ExecutionException(
					"Error on opening the editor with the id " + task.getId(),
					e);
		}
	}

	private static class InternalTaskEditorInput extends TaskEditorInput {

		private final LocalFileModel model;

		public InternalTaskEditorInput(final Task task,
				final LocalFileModel model) {
			super(task);
			this.model = model;
		}

		@Override
		public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
			if (LocalFileModel.class.isAssignableFrom(adapter)) {
				return model;
			}
			return super.getAdapter(adapter);
		}
	}
}
