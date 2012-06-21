package de.dzimmermann.rcp.basicplatform.ui.util;

import java.io.PrintStream;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;


/**
 * This is a simple Console view helper to print especially development
 * information to it.
 * 
 * @author danielz
 */
public class Console {

	/**
	 * the {@link MessageConsole} on which all messages will appear
	 */
	private MessageConsole systemConsole;

	/**
	 * the {@link MessageConsoleStream} with a default color (black)
	 */
	private MessageConsoleStream defaultMessageOutputStream;
	/**
	 * a default {@link PrintStream}, which will be mapped to the
	 * {@link MessageConsoleStream}
	 */
	private PrintStream defaultMessagePrintStream;

	/**
	 * the {@link MessageConsoleStream} with a warning color (blue)
	 */
	private MessageConsoleStream warningMessageOutputStream;
	/**
	 * a warning {@link PrintStream}, which will be mapped to the
	 * {@link MessageConsoleStream}
	 */
	private PrintStream warningMessagePrintStream;

	/**
	 * the {@link MessageConsoleStream} with an error color (red)
	 */
	private MessageConsoleStream errorMessageOutputStream;
	/**
	 * a error {@link PrintStream}, which will be mapped to the
	 * {@link MessageConsoleStream}
	 */
	private PrintStream errorMessagePrintStream;

	/**
	 * creates a new console with an internal name (to find the correct console)
	 * 
	 * @param systemConsoleName
	 *            the unique name of this console
	 */
	public Console(String systemConsoleName) {
		initiateSystemConsole(systemConsoleName);
	}

	/**
	 * hook syserr and sysout to default and error {@link MessageConsoleStream}s
	 */
	public void hookSystemOutputs() {
		System.setOut(defaultMessagePrintStream);
		System.setErr(errorMessagePrintStream);
	}

	/**
	 * this method can be used to identify and activate a specific console by
	 * using the console as a link to the {@link ConsolePlugin}
	 * 
	 * @return the current {@link MessageConsole}
	 */
	public MessageConsole getSystemConsole() {
		return systemConsole;
	}

	/**
	 * returnes the {@link MessageConsoleStream} which is used for default
	 * messages
	 * 
	 * @return the default {@link MessageConsoleStream}
	 */
	public MessageConsoleStream getDefaultMessageOutputStream() {
		return defaultMessageOutputStream;
	}

	/**
	 * returnes the {@link PrintStream} assoziated with default messages
	 * 
	 * @return the default {@link PrintStream}
	 */
	public PrintStream getDefaultMessagePrintStream() {
		return defaultMessagePrintStream;
	}

	/**
	 * returnes the {@link MessageConsoleStream} which is used for error
	 * messages
	 * 
	 * @return the error {@link MessageConsoleStream}
	 */
	public MessageConsoleStream getErrorMessageOutputStream() {
		return errorMessageOutputStream;
	}

	/**
	 * returnes the {@link PrintStream} assoziated with error messages
	 * 
	 * @return the error {@link PrintStream}
	 */
	public PrintStream getErrorMessagePrintStream() {
		return errorMessagePrintStream;
	}

	/**
	 * returnes the {@link MessageConsoleStream} which is used for warning
	 * messages
	 * 
	 * @return the warning {@link MessageConsoleStream}
	 */
	public MessageConsoleStream getWarningMessageOutputStream() {
		return warningMessageOutputStream;
	}

	/**
	 * returnes the {@link PrintStream} assoziated with warning messages
	 * 
	 * @return the warning {@link PrintStream}
	 */
	public PrintStream getWarningMessagePrintStream() {
		return warningMessagePrintStream;
	}

	/**
	 * This method initiates the system console (searches via the parameter for
	 * it) <br>
	 * Use the warningStream directly, if you want to add on. <br>
	 * System.out is hooked to defaultMessage <br>
	 * System.err is hooked to errorMessage
	 * 
	 * @param systemConsoleName
	 *            the name of the console to initialize
	 */
	public void initiateSystemConsole(String systemConsoleName) {

		systemConsole = findSystemConsole(systemConsoleName);

		defaultMessageOutputStream = systemConsole.newMessageStream();
		defaultMessagePrintStream = new PrintStream(defaultMessageOutputStream);

		warningMessageOutputStream = systemConsole.newMessageStream();
		warningMessageOutputStream.setColor(new Color(Display.getCurrent(), 0,
				0, 255));
		warningMessagePrintStream = new PrintStream(warningMessageOutputStream);

		errorMessageOutputStream = systemConsole.newMessageStream();
		errorMessageOutputStream.setColor(new Color(Display.getCurrent(), 255,
				0, 0));
		errorMessagePrintStream = new PrintStream(errorMessageOutputStream);
	}

	/**
	 * if the system console is not displayed, this method will force its
	 * display
	 */
	public void showSystemConsole() {
		try {
			IWorkbenchPage page = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			String id = IConsoleConstants.ID_CONSOLE_VIEW;
			IConsoleView view = (IConsoleView) page.showView(id);
			view.display(systemConsole);
		} catch (PartInitException e) {
			System.err.println(e.getMessage());
			Logger.logError(e);
		}
	}

	/**
	 * this method realy initiates the system console, specified by the given
	 * name, if it isn't allready initalized. <br>
	 * an initialized console still might be invisibly, then the
	 * showSystemConsole method should be used.
	 * 
	 * @param systemConsoleName
	 *            name which is used to identify ore create the console
	 * @return the (new or found) MessageConsole
	 */
	private MessageConsole findSystemConsole(String systemConsoleName) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();

		IConsoleManager conMan = plugin.getConsoleManager();

		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++)
			if (systemConsoleName.equals(existing[i].getName()))
				return (MessageConsole) existing[i];

		// no console found, so create a new one
		MessageConsole systemConsole = new MessageConsole(systemConsoleName,
				null);
		conMan.addConsoles(new IConsole[] { systemConsole });

		return systemConsole;
	}
}
