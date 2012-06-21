package de.dzimmermann.rcp.basicplatform;

import java.util.ArrayList;

import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import de.dzimmermann.rcp.basicplatform.util.Messages;
import de.dzimmermann.rcp.basicplatform.util.UninterruptableJob;
import de.dzimmermann.rcp.basicplatform.util.UninterruptableUIJob;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

	private static final String PERSPECTIVE_ID = "de.dzimmermann.rcp.basicplatform.ui.reportConfigurationPerspective"; //$NON-NLS-1$

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		return new ApplicationWorkbenchWindowAdvisor(configurer);
	}

	@Override
	public IAdaptable getDefaultPageInput() {
		// IWorkspace workspace = ResourcesPlugin.getWorkspace();
		// return workspace.getRoot();
		return SSDToolPlugin.getDefault().getSSDToolModel().getRootTask();
	}

	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		super.initialize(configurer);
		configurer.setSaveAndRestore(true);
	}

	@Override
	public String getInitialWindowPerspectiveId() {
		return PERSPECTIVE_ID;
	}

	@Override
	public boolean preShutdown() {

		// In case of still running jobs, the application asks, if the user is
		// sure to quit the PCP while there might still be important work to be
		// done
		List<Job> uninterruptableJobs = new ArrayList<Job>();
		Job[] jobs = Job.getJobManager().find(null);
		StringBuilder sb = new StringBuilder();
		if (jobs != null && jobs.length > 0) {

			for (int i = 0; i < jobs.length; i++) {

				if (!(jobs[i] instanceof UninterruptableJob)
						&& !(jobs[i] instanceof UninterruptableUIJob)) {
					continue;
				}
				uninterruptableJobs.add(jobs[i]);

				sb.append(jobs[i].getName());
				if (!jobs[i].isUser()) {
					sb.append(Messages
							.getString("ApplicationWorkbenchAdvisor.jobs.background")); //$NON-NLS-1$
				}
				if ((i + 1) < jobs.length) {
					sb.append("\n"); //$NON-NLS-1$
				}
			}
		}

		if (!uninterruptableJobs.isEmpty()) {
			boolean exit = MessageDialog
					.openQuestion(
							PlatformUI.getWorkbench()
									.getActiveWorkbenchWindow().getShell(),
							Messages.getString("ApplicationWorkbenchAdvisor.MessageDialog.Text"), //$NON-NLS-1$
							String.format(
									Messages.getString("ApplicationWorkbenchAdvisor.MessageDialog.confirm.exit.text") //$NON-NLS-1$
									, jobs.length, sb.toString()));
			if (exit) {
				return super.preShutdown();
			} else {
				return false;
			}
		}

		return super.preShutdown();
	}

	// @Override
	// public synchronized AbstractStatusHandler getWorkbenchErrorHandler() {
	// return new AbstractStatusHandler() {
	// @Override
	// public void handle(StatusAdapter statusAdapter, int style) {
	// // if (statusAdapter != null && statusAdapter.getStatus() !=
	// // null) {
	// // if (statusAdapter.getStatus().getException() != null
	// // && PlatformUI.getWorkbench() != null) {
	// // try {
	// // System.err.println(statusAdapter.getStatus()
	// // .getException().getMessage());
	// // } catch (Exception e) {
	// // Logger
	// //
	// .logWarning("Error on writing message to the console.\nThe message was: "
	// // + e.getMessage());
	// // }
	// // }
	// // try {
	// // Logger.log(statusAdapter.getStatus());
	// // } catch (Exception e) {
	// // String originalMessage = statusAdapter.getStatus()
	// // .getMessage();
	// // Throwable originalException = statusAdapter.getStatus()
	// // .getException();
	// // Logger
	// // .logError(
	// //
	// "An error occured while logging a message.\nThe original message was: "
	// // + originalMessage != null ? originalMessage
	// // : originalException != null ? e
	// // .getMessage()
	// // : "UNKNOWN\nMessage during logging was: "
	// // + e
	// // .getMessage(),
	// // e);
	// // }
	// // }
	// }
	// };
	// }
}
