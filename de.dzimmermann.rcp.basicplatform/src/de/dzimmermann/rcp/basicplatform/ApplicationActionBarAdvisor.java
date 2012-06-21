package de.dzimmermann.rcp.basicplatform;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

import de.dzimmermann.rcp.basicplatform.action.RemoteServerContributionItem;
import de.dzimmermann.rcp.basicplatform.action.RemoteServerSelectionAction;
import de.dzimmermann.rcp.basicplatform.util.BasicPlatformPluginConstants;
import de.dzimmermann.rcp.basicplatform.util.Messages;

/**
 * The extended default class...
 * 
 * @author dzimmermann
 * @version 0.2
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

	public static final String REMOTE_SERVER_NAME_ITEM_ID = "de.dzimmermann.rcp.basicplatform.remote_system.name"; //$NON-NLS-1$

	private IWorkbenchAction exitAction;
	private IWorkbenchAction saveAction;
	private IWorkbenchAction saveAsAction;
	private IWorkbenchAction saveAllAction;

	private IWorkbenchAction preferencesAction;

	private IWorkbenchAction aboutAction;
	private IWorkbenchAction showHelpAction;
	private IWorkbenchAction searchHelpAction;
	private IWorkbenchAction dynamicHelpAction;

	private IWorkbenchAction introAction;

	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

	@Override
	protected void makeActions(IWorkbenchWindow window) {

		// file menu actions
		saveAction = ActionFactory.SAVE.create(window);
		register(saveAction);

		saveAsAction = ActionFactory.SAVE_AS.create(window);
		register(saveAsAction);

		saveAllAction = ActionFactory.SAVE_ALL.create(window);
		register(saveAllAction);

		exitAction = ActionFactory.QUIT.create(window);
		register(exitAction);

		// window menu actions
		preferencesAction = ActionFactory.PREFERENCES.create(window);
		register(preferencesAction);

		// help menu actions
		aboutAction = ActionFactory.ABOUT.create(window);
		register(aboutAction);

		showHelpAction = ActionFactory.HELP_CONTENTS.create(window);
		register(showHelpAction);

		searchHelpAction = ActionFactory.HELP_SEARCH.create(window);
		register(searchHelpAction);

		dynamicHelpAction = ActionFactory.DYNAMIC_HELP.create(window);
		register(dynamicHelpAction);

		// introAction = ActionFactory.INTRO.create(window);
		// register(introAction);
		introAction = null;
	}

	@Override
	protected void fillMenuBar(IMenuManager menuBar) {

		MenuManager fileMenu = new MenuManager(
				Messages.getString("ApplicationActionBarAdvisor.MenuManager.File"), //$NON-NLS-1$
				IWorkbenchActionConstants.M_FILE);
		MenuManager windowMenu = new MenuManager(
				Messages.getString("ApplicationActionBarAdvisor.MenuManager.Window"), //$NON-NLS-1$
				IWorkbenchActionConstants.M_WINDOW);
		MenuManager helpMenu = new MenuManager(
				Messages.getString("ApplicationActionBarAdvisor.MenuManager.Help"), //$NON-NLS-1$
				IWorkbenchActionConstants.M_HELP);

		menuBar.add(fileMenu);
		menuBar.add(windowMenu);
		menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		menuBar.add(helpMenu);

		// file
		fileMenu.add(saveAction);
		fileMenu.add(saveAsAction);
		fileMenu.add(saveAllAction);
		fileMenu.add(new Separator());
		fileMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		fileMenu.add(new Separator());
		fileMenu.add(exitAction);

		// window
		windowMenu.add(preferencesAction);
		windowMenu.add(new Separator());
		windowMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));

		// help
		if (introAction != null) {
			helpMenu.add(introAction);
		}
		helpMenu.add(new Separator());
		helpMenu.add(new GroupMarker(IWorkbenchActionConstants.HELP_START));
		helpMenu.add(showHelpAction);
		helpMenu.add(searchHelpAction);
		helpMenu.add(dynamicHelpAction);
		helpMenu.add(new GroupMarker(IWorkbenchActionConstants.HELP_END));
		helpMenu.add(new Separator());
		helpMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
		helpMenu.add(new Separator());
		helpMenu.add(aboutAction);
	}

	@Override
	protected void fillCoolBar(ICoolBarManager coolBar) {

		// super.fillCoolBar(coolBar);

		// IToolBarManager defaultPlatformToolbar = new ToolBarManager(SWT.FLAT
		// | SWT.RIGHT);
		// coolBar.add(new ToolBarContributionItem(defaultPlatformToolbar,
		// "platform.toolbar"));
		// defaultPlatformToolbar.add(saveAction);

		IToolBarManager ssdToolContentsToolbar = new ToolBarManager(SWT.FLAT
				| SWT.RIGHT);
		coolBar.add(new ToolBarContributionItem(ssdToolContentsToolbar,
				BasicPlatformPluginConstants.TOOLBAR_CONTENT));

		IToolBarManager ssdToolAddtionToolbar = new ToolBarManager(SWT.FLAT
				| SWT.RIGHT);
		coolBar.add(new ToolBarContributionItem(ssdToolAddtionToolbar,
				BasicPlatformPluginConstants.TOOLBAR_ADDITIONS));
	}

	@Override
	protected void fillStatusLine(IStatusLineManager statusLine) {
		super.fillStatusLine(statusLine);

		if (SSDToolPlugin.getDefault().isDeveloperMode()) {
			ActionContributionItem remoteServerActionItem = new ActionContributionItem(
					new RemoteServerSelectionAction(null));
			remoteServerActionItem
					.setMode(ActionContributionItem.MODE_FORCE_TEXT);
			statusLine.add(remoteServerActionItem);
		} else {
			ContributionItem infoItem = new RemoteServerContributionItem();
			statusLine.add(infoItem);
		}
	}
}
