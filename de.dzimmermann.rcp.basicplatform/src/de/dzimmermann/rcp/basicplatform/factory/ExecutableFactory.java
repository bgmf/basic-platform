package de.dzimmermann.rcp.basicplatform.factory;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;

import de.dzimmermann.rcp.basicplatform.model.Task;
import de.dzimmermann.rcp.basicplatform.model.TaskEditorInput;

/**
 * This interface describes the basic functionality a factory for executable
 * classes need to provide.
 * 
 * @author danielz
 * @since PFSManager Version 0.5.0, PFSRCPCore V0.2.2
 * @version 1.0
 */
public interface ExecutableFactory {

	/**
	 * This method creates a concrete {@link Object} of a
	 * {@link TaskEditorInput}.
	 * 
	 * @param task
	 *            the {@link Task} wich defines the {@link Class} (by its name)
	 *            via the {@link Task#getTaskEditorInput()}
	 * @return an {@link Object} of an {@link TaskEditorInput} implementation,
	 *         will can be defined by a {@link Task} (the
	 *         {@link Task#getTaskEditorInput()}
	 * @throws Exception
	 *             in eny error case an {@link Exception} can be thrown
	 */
	public TaskEditorInput createTaskEditorInput(Task task) throws Exception;

	/**
	 * This method creates a concrete {@link Object} of a
	 * {@link TitleAreaDialog}.<br>
	 * Be aware, that only the default constructor is permitted.
	 * 
	 * @param task
	 *            the {@link Task} which defines the {@link Class} (by its name)
	 *            via the {@link Task#getContextToLoadID()}
	 * @param parentShell
	 *            the {@link Shell} for which this {@link TitleAreaDialog} will
	 *            be created
	 * @return an {@link Object} of an {@link TitleAreaDialog} implementation,
	 *         will can be defined by a {@link Task} (the {@link Task#getContextToLoadID()}
	 * @throws Exception
	 *             in any error case an {@link Exception} can be thrown
	 */
	public TitleAreaDialog createTitleAreaDialog(Task task, Shell parentShell)
			throws Exception;

	/**
	 * This method creates a concrete {@link Object} of a {@link Wizard}.<br>
	 * Be aware, that only a zero argument constructor is permitted.
	 * 
	 * @param task
	 *            the {@link Task} which defines the {@link Class} (by its name)
	 *            via the {@link Task#getContextToLoadID()}
	 * @return an {@link Object} of an {@link Wizard} implementation, as defined
	 *         by a {@link Task} (the {@link Task#getContextToLoadID()}
	 * @throws Exception
	 *             in any error case an {@link Exception} can be thrown
	 */
	public Wizard createWizard(Task task) throws Exception;

	/**
	 * This method tries to create an {@link Object} of an
	 * {@link AbstractHandler}, as defined by the specified task.<br>
	 * Be aware, that only a zero argument constructor is permitted.<br>
	 * There is no <code>ExecutionEvent</code> passed down to the handler (it's
	 * <code>null</code>!)
	 * 
	 * @param task
	 *            the {@link Task} which defines the {@link Class} (by its name)
	 *            via the {@link Task#getContextToLoadID()}
	 * @return an {@link Object} of an {@link AbstractHandler} implementation,
	 *         as defined by a {@link Task} (the {@link Task#getContextToLoadID()}
	 * @throws Exception
	 *             in any error case an {@link Exception} can be thrown
	 */
	public AbstractHandler createHandler(Task task) throws Exception;

	/**
	 * This method decides whether an implementation of the
	 * {@link ExecutableFactory} can create a {@link Class}.
	 * 
	 * @param className
	 *            the class name to test
	 * @return returns true, if the classloader in the implementations context
	 *         knows the class, else false
	 * @throws Exception
	 *             in any error case an {@link Exception} can be thrown
	 */
	public boolean canCreateClass(String className) throws Exception;
}
