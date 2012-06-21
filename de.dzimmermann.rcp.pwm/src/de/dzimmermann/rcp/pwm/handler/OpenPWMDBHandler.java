package de.dzimmermann.rcp.pwm.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import de.dzimmermann.rcp.basicplatform.handler.BasicPlatformStates;
import de.dzimmermann.rcp.basicplatform.model.Task;
import de.dzimmermann.rcp.basicplatform.util.PFSCoreIconProvider;
import de.dzimmermann.rcp.pwm.ui.dialog.ManagePWMDBsDialog;
import de.dzimmermann.rcp.pwm.ui.editor.PWMEditor;
import de.dzimmermann.rcp.pwm.util.PWMUtils;

public class OpenPWMDBHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IWorkbenchPage page = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();

		Task task = BasicPlatformStates.getTaskState();
		// Map<String, Object> models = BasicPlatformSessionModel.getInstance()
		// .getPluginModels();

		if (PWMUtils.PWM_TASK_OPENDB.equals(task.getId())) {

			Shell shell = page.getActivePart().getSite().getShell();
			ManagePWMDBsDialog dialog = new ManagePWMDBsDialog(shell);

			if (TitleAreaDialog.OK == dialog.open()) {
				IPath path = dialog.getSelectedDatabaseFile();
				InternalTaskEditorInput input = new InternalTaskEditorInput(
						path);
				try {
					page.openEditor(input, PWMEditor.ID, true,
							IWorkbenchPage.MATCH_ID);
				} catch (PartInitException e) {
					throw new ExecutionException(
							"Error on opening the Password manager for database file "
									+ path.toOSString(), e);
				}
			}
		}

		return null;
	}

	private static class InternalTaskEditorInput implements IEditorInput,
			IAdaptable {

		private final IPath path;
		private final String dbName;

		public InternalTaskEditorInput(final IPath path) {

			this.path = path;

			String p = path.lastSegment();
			if (p.toLowerCase().endsWith(PWMUtils.DEFAULT_EXTENSION_BACKUP))
				p = p.substring(0,
						p.lastIndexOf(PWMUtils.DEFAULT_EXTENSION_BACKUP));
			else if (p.toLowerCase().endsWith(PWMUtils.DEFAULT_EXTENSION))
				p = p.substring(0, p.lastIndexOf(PWMUtils.DEFAULT_EXTENSION));
			dbName = p;
		}

		@Override
		public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
			if (IPath.class.isAssignableFrom(adapter)) {
				return path;
			}
			return null;
		}

		@Override
		public boolean exists() {
			return true;
		}

		@Override
		public ImageDescriptor getImageDescriptor() {

			ImageDescriptor desc = null;

			Image img = PFSCoreIconProvider.getImageByIconName(
					"fugue_database--pencil.png", true);
			if (img != null)
				desc = ImageDescriptor.createFromImage(img);

			return desc;
		}

		@Override
		public String getName() {
			return String.format("PWM: %s", dbName);
		}

		@Override
		public IPersistableElement getPersistable() {
			return null;
		}

		@Override
		public String getToolTipText() {
			return String.format(
					"Manage your passwords within the database %s.\n"
							+ "Allows you to create groups and sub groups and "
							+ "add as many passwords to it, as you like.",
					dbName);
		}
	}
}
