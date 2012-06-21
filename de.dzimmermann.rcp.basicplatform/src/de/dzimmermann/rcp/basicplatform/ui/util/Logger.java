package de.dzimmermann.rcp.basicplatform.ui.util;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import de.dzimmermann.rcp.basicplatform.SSDToolPlugin;

/**
 * This is a simple logger stub, which uses the eclipse api internal logger to
 * log entries - wether they are critical or just informational.
 * 
 * @author danielz
 */
public class Logger {

	/**
	 * Simply logs a message as an information
	 * 
	 * @param message
	 *            the message to log
	 */
	public static void logInfo(String message) {
		log(IStatus.INFO, IStatus.OK, message, null);
	}

	/**
	 * Simply logs a message as a warning
	 * 
	 * @param message
	 *            the message to log
	 */
	public static void logWarning(String message) {
		log(IStatus.WARNING, IStatus.OK, message, null);
	}

	/**
	 * Logs an exception (critical)
	 * 
	 * @param exception
	 *            the exception to log
	 */
	public static void logError(Throwable exception) {
		logError("Unexpected Exception", exception);
	}

	/**
	 * Logs an exception (critical) with an additional user message
	 * 
	 * @param message
	 *            the message to log
	 * @param exception
	 *            the exception to log
	 */
	public static void logError(String message, Throwable exception) {
		log(IStatus.ERROR, IStatus.OK, message, exception);
	}

	/**
	 * Simple logger. Add the severity of a message to log and alternativly use
	 * a status and, in the case of an exception, the one to log.
	 * 
	 * @param severity
	 *            the severity of the entry
	 * @param code
	 *            the status code of this entry
	 * @param message
	 *            the message to log
	 * @param exception
	 *            the exception to log
	 */
	public static void log(int severity, int code, String message,
			Throwable exception) {
		log(createStatus(severity, code, message, exception));
	}

	/**
	 * Create a {@link Status} object with the content to log. Add the severity
	 * of a message to log and alternativly use a status and, in the case of an
	 * exception, the one to log.
	 * 
	 * @param severity
	 *            the severity of the entry
	 * @param code
	 *            the status code of this entry
	 * @param message
	 *            the message to log
	 * @param exception
	 *            the exception to log
	 * @return
	 */
	public static IStatus createStatus(int severity, int code, String message,
			Throwable exception) {
		return new Status(severity, SSDToolPlugin.PLUGIN_ID, code, message,
				exception);
	}

	/**
	 * Log an entry from a {@link Status} object.
	 * 
	 * @param status
	 *            the {@link Status} object to log
	 */
	public static void log(IStatus status) {
		SSDToolPlugin.getDefault().getLog().log(status);
	}
}
