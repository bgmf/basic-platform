package de.dzimmermann.rcp.bsgtaucha.mgt.handler;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;

import de.dzimmermann.rcp.basicplatform.handler.BasicPlatformStates;
import de.dzimmermann.rcp.basicplatform.model.BasicPlatformSessionModel;
import de.dzimmermann.rcp.basicplatform.model.Task;
import de.dzimmermann.rcp.basicplatform.ui.util.treewizard.TreeWizardDialog.ResultType;
import de.dzimmermann.rcp.basicplatform.util.ElementUpdateSupport;
import de.dzimmermann.rcp.bsgtaucha.mgt.Activator;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.localfile.LocalFileModel;
import de.dzimmermann.rcp.bsgtaucha.mgt.ui.dialog.LoadModelDialog;
import de.dzimmermann.rcp.bsgtaucha.mgt.ui.editor.BandMgtEditor;
import de.dzimmermann.rcp.bsgtaucha.mgt.ui.editor.PersonMgtEditor;
import de.dzimmermann.rcp.bsgtaucha.mgt.ui.editor.TasksMgtEditor;
import de.dzimmermann.rcp.bsgtaucha.mgt.ui.editor.WorkEntryEditor;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.BSGTauchaConstants;

public class OpenFileHandler extends AbstractHandler {

	@SuppressWarnings("unchecked")
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();

		Task task = BasicPlatformStates.getTaskState();
		Map<String, Object> models = BasicPlatformSessionModel.getInstance()
				.getPluginModels();

		if (BSGTauchaConstants.OPEN_FILE.equals(task.getId())) {

			Shell shell = page.getActivePart().getSite().getShell();
			LoadModelDialog dialog = new LoadModelDialog(shell,
					SWT.APPLICATION_MODAL);
			ResultType result = dialog.open();

			if (ResultType.OK == result) {

				LocalFileModel model = dialog.getLocalFile();

				models.put(Activator.PLUGIN_ID, model);
				models.put(Activator.PLUGIN_ID
						+ BSGTauchaConstants.DEFAULT_PASSWORD_SUFFIX,
						dialog.getDefaultPassword());
				models.put(Activator.PLUGIN_ID
						+ BSGTauchaConstants.ADVANCED_PASSWORD_SUFFIX,
						dialog.getAdvancedPassword());

				for (IEditorReference editor : page.getEditorReferences()) {
					if (editors2update.contains(editor.getId())) {
						IEditorPart e = editor.getEditor(false);
						if (e != null && e instanceof ElementUpdateSupport) {
							((ElementUpdateSupport<LocalFileModel>) e)
									.updateElement(model);
						}
					}
				}
			}
		}

		return null;
	}

	private final static Set<String> editors2update = new HashSet<String>();
	static {
		editors2update.add(PersonMgtEditor.ID);
		editors2update.add(TasksMgtEditor.ID);
		editors2update.add(BandMgtEditor.ID);
		editors2update.add(WorkEntryEditor.ID);
	}
}
