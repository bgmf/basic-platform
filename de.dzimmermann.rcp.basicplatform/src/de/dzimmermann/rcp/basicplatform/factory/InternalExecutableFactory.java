package de.dzimmermann.rcp.basicplatform.factory;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;

import de.dzimmermann.rcp.basicplatform.model.Task;
import de.dzimmermann.rcp.basicplatform.model.TaskEditorInput;

/**
 * This is a basic implementation of the {@link ExecutableFactory} interface,
 * which creates all local ressources, needed to be created from within other
 * (calling) plugins
 * 
 * @author danielz
 */
public class InternalExecutableFactory implements ExecutableFactory {

	@SuppressWarnings("unchecked")
	@Override
	public TaskEditorInput createTaskEditorInput(Task task) throws Exception {

		if (task == null)
			throw new IllegalArgumentException("Argument task cannot be null!"); //$NON-NLS-1$

		if (task.getTaskEditorInput() != null
				&& !task.getTaskEditorInput().isEmpty()) {

			Class<? extends TaskEditorInput> clazz = (Class<? extends TaskEditorInput>) Class
					.forName(task.getTaskEditorInput());

			return clazz.getConstructor(Task.class).newInstance(task);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public TitleAreaDialog createTitleAreaDialog(Task task, Shell parentShell)
			throws Exception {

		if (task == null)
			throw new IllegalArgumentException("Argument task cannot be null!"); //$NON-NLS-1$

		if (task.getContextToLoadID() != null && !task.getContextToLoadID().isEmpty()) {

			Class<? extends TitleAreaDialog> clazz = (Class<? extends TitleAreaDialog>) Class
					.forName(task.getContextToLoadID());

			return clazz.getConstructor(Shell.class).newInstance(parentShell);
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Wizard createWizard(Task task) throws Exception {

		if (task == null)
			throw new IllegalArgumentException("Argument task cannot be null!"); //$NON-NLS-1$

		if (task.getContextToLoadID() != null && !task.getContextToLoadID().isEmpty()) {

			Class<? extends Wizard> clazz = (Class<? extends Wizard>) Class
					.forName(task.getContextToLoadID());

			return clazz.getConstructor().newInstance();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public AbstractHandler createHandler(Task task) throws Exception {

		if (task == null)
			throw new IllegalArgumentException("Argument task cannot be null!"); //$NON-NLS-1$

		if (task.getContextToLoadID() != null && !task.getContextToLoadID().isEmpty()) {

			Class<? extends AbstractHandler> clazz = (Class<? extends AbstractHandler>) Class
					.forName(task.getContextToLoadID());

			return clazz.getConstructor().newInstance();
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean canCreateClass(String className) throws Exception {

		try {
			if ((Class<? extends Wizard>) Class.forName(className) != null)
				return true;
		} catch (Exception e) {
			return false;
		}

		return false;
	}
}
