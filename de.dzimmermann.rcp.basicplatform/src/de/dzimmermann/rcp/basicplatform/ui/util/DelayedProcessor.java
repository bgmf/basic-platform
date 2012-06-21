package de.dzimmermann.rcp.basicplatform.ui.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.progress.UIJob;

/**
 * Performs some actions on a collection of objects after a specified delay of
 * time. If the user of this class puts another object in the collection (via
 * the add method), the delay begins again. It can be used e.g. for validation
 * of data after a certain amount of time. Thus a lot of changes can be
 * collected that would otherwise clog the tubes of various threads.
 * <p>
 * Since this class derives from {@link UIJob} it is possible to make changes to
 * the UI.
 * 
 * @author werner
 * @since SSDTool-recordProcessing-Plugin 0.1.10
 */
public abstract class DelayedProcessor<T> extends UIJob {

	private final int delay;

	private final Set<T> objects = new HashSet<T>();

	/**
	 * The same as {@code new DelayedProcessor(jobName, 500)}.
	 * 
	 * @param jobName
	 */
	public DelayedProcessor(String jobName) {
		this(jobName, 500);
	}

	public DelayedProcessor(String jobName, int delay) {
		super(jobName);
		this.delay = delay;
	}

	/**
	 * The action that should be performed after the timeout successfully ran
	 * out.
	 * 
	 * @param objects
	 */
	public abstract void execute(IProgressMonitor monitor, Collection<T> objects);

	/**
	 * Executes the action the user wants and clears the list of objects.
	 */
	@Override
	public final IStatus runInUIThread(IProgressMonitor monitor) {
		synchronized (this) {
			execute(monitor, objects);
			objects.clear();
		}
		return Status.OK_STATUS;
	}

	/**
	 * Adds another object and potentially delays the execution of this action
	 * further. In case the provided object was already part of the used
	 * collection the delay won't get reset.
	 * 
	 * @param fr
	 */
	public synchronized void add(T fr) {
		if (objects.add(fr)) {
			schedule(delay);
		}
	}
}
