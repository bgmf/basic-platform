package de.dzimmermann.rcp.basicplatform.ui.util.treewizard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.dzimmermann.rcp.basicplatform.ui.util.Logger;
import de.dzimmermann.rcp.basicplatform.util.InternalAdapter;

/**
 * This class is intended to be used when other default wizard implementations
 * might be not versatile enough.<br>
 * The TreeWizard was created because of the lack of ordinary wizard
 * implementations to provide a tree like page structure. This might not be
 * impossible, but difficult to maintain, so this implementation is intended to
 * solve at least this problem.<br>
 * Additionally this class is not necessarily intended to be used as some kind
 * of dialog only but is as a composite usable everywhere a composite can be
 * used.<br>
 * It also provides {@link FormToolkit} from Eclipse forms to be used within
 * such sides.
 * 
 * @author dzimmermann
 * 
 */
public abstract class TreeWizard extends Composite implements InternalAdapter,
		TreeWizardListener {

	/**
	 * The {@link FormToolkit} used to adapt this page to, when used in an
	 * Eclipse Forms environment.
	 */
	private final FormToolkit formToolkit = new FormToolkit(
			Display.getDefault());

	/**
	 * A {@link Composite} using a {@link StackLayout} to present it's pages.
	 * All {@link TreeWizardPage}s will be printed on it.<br>
	 * Pages are created lazily.
	 */
	private Composite pageStack;

	/**
	 * Root element of the internal representation of the page tree. Using a
	 * couple of internal methods it's relatively easy to navigate over the
	 * tree.
	 */
	private TreeWizardPageElement pageTreeRoot;
	/**
	 * The current top page. This on is the topmost composite on the
	 * {@link #pageStack} and exclusively visible.
	 * 
	 * @see TreeWizardPage
	 */
	private TreeWizardPage currentPage;

	/**
	 * A small composite at the bottom line to print the buttons on. This is for
	 * internal use only and the {@link #createButton(ButtonType)} is the only
	 * method mean to create predefined buttons.
	 * 
	 * @see ButtonType
	 * @see #createButton(ButtonType)
	 * @see #createButtons()
	 */
	private Composite buttonComposite;

	/**
	 * The container of the progress bar.
	 */
	private Composite progressComposite;
	/**
	 * The original size of the progress bar. The idea is to store the size of
	 * the bar, when it is disabled and reset it when it is enabled.<br>
	 * Seems not to be supported by some platforms.
	 * 
	 * @see #setProgressBarVisible(boolean)
	 */
	private Point progressCompositeSize;
	/**
	 * The progress bar used to indicate some action progress done by the wizard
	 * as a background job.
	 */
	private ProgressBar progressBar;

	/**
	 * Internal mapping of a buttons type to it instance.
	 * 
	 * @see ButtonType
	 * @see #createButton(ButtonType)
	 * @see #createButtons()
	 */
	private Map<ButtonType, Button> buttons = new TreeMap<ButtonType, Button>();

	/**
	 * A list with pages that will be notified about focus changes.
	 */
	private List<TreeWizardFocusListener> focusListeners = new ArrayList<TreeWizardFocusListener>();

	public TreeWizard(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(1, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);

		pageStack = new Composite(this, SWT.NONE);
		pageStack.setLayout(new StackLayout());
		pageStack.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));

		progressComposite = new Composite(this, SWT.NONE);
		FormLayout fl_progressComposite = new FormLayout();
		fl_progressComposite.marginTop = 0;
		fl_progressComposite.marginLeft = 10;
		fl_progressComposite.marginRight = 10;
		fl_progressComposite.marginBottom = 0;
		progressComposite.setLayout(fl_progressComposite);
		progressComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		progressBar = new ProgressBar(progressComposite, SWT.NONE);
		FormData fd_progressBar = new FormData();
		fd_progressBar.top = new FormAttachment(0, 0);
		fd_progressBar.left = new FormAttachment(0, 5);
		fd_progressBar.right = new FormAttachment(100, -5);
		fd_progressBar.bottom = new FormAttachment(100, 0);
		progressBar.setLayoutData(fd_progressBar);

		Label separator = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		buttonComposite = new Composite(this, SWT.NONE);
		buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM, true,
				false, 1, 1));
		FormLayout fl_buttonComposite = new FormLayout();
		fl_buttonComposite.marginTop = 10;
		fl_buttonComposite.marginLeft = 10;
		fl_buttonComposite.marginRight = 10;
		fl_buttonComposite.marginBottom = 10;
		buttonComposite.setLayout(fl_buttonComposite);

		// create the buttons first
		createButtons();
		// set the initial enabled state
		initButtons();
		// call this method to create all pages defined by the user
		addPages();
	}

	@Override
	public void dispose() {
		if (buttonComposite != null && !buttonComposite.isDisposed()) {
			for (Control c : buttonComposite.getChildren()) {
				if (c != null && !c.isDisposed())
					c.dispose();
			}
			buttonComposite.dispose();
		}
		if (pageStack != null && !pageStack.isDisposed()) {
			for (Control c : pageStack.getChildren()) {
				if (c != null && !c.isDisposed())
					c.dispose();
			}
			pageStack.dispose();
		}
		try {
			super.dispose();
		} catch (Exception e) {
			Logger.logWarning(String
					.format("Could not dispose TreeWizard because of an caught exception (%s)",
							e.getMessage()));
		}
	}

	/**
	 * Helper method to trigger the adoption of this wizard to Eclipse Forms.
	 * 
	 * @see #formToolkit
	 * @see FormToolkit
	 */
	public void adaptEclipseForms() {
		// XXX RAP currently doesn't support this (1.4M6)
		// formToolkit.paintBordersFor(this);
		formToolkit.adapt(this);
		// formToolkit.paintBordersFor(pageStack);
		formToolkit.adapt(pageStack);
		// formToolkit.paintBordersFor(progressComposite);
		formToolkit.adapt(progressComposite);
		// formToolkit.paintBordersFor(buttonComposite);
		formToolkit.adapt(buttonComposite);
	}

	public void setProgressBarVisible(boolean progressBarVisible) {
		progressComposite.setVisible(progressBarVisible);
		if (!progressBarVisible) {
			progressCompositeSize = progressComposite.getSize();
			progressComposite.setSize(-1, -1);
		} else {
			progressComposite.setSize(progressCompositeSize);
		}
		this.layout(true);
	}

	/*
	 * abstract methods, that need to be implemented for correct behavior
	 */

	/**
	 * Implement this method to trigger automatic creation of the buttons.<br>
	 * Use the {@link #createButton(ButtonType)} method to do so.
	 */
	protected abstract void createButtons();

	/**
	 * Implement this method to trigger automatic creation of the pages.<br>
	 * Use the {@link #addPage(TreeWizardPage, TreeWizardPage)} method to do so.
	 */
	protected abstract void addPages();

	/**
	 * This method is triggered when the OK-button is pressed. The implementor
	 * may define the behavior.
	 */
	protected abstract void okPressed();

	/**
	 * This method is triggered when the Cancel-button is pressed. The
	 * implementor may define the behavior.
	 */
	protected abstract void cancelPressed();

	/**
	 * This method is triggered when the Help-button is pressed. The implementor
	 * may define the behavior.
	 */
	protected abstract void helpPressed();

	/*
	 * pre-implemented methods (partially internal methods only)
	 */

	/**
	 * This method is used to create the predefined buttons on the
	 * {@link #buttonComposite}.
	 * 
	 * @see ButtonType
	 * @see #createButtons()
	 */
	protected Button createButton(ButtonType type) {

		Button button = null;

		if (type == null)
			return null;

		if (buttons.containsKey(type))
			return buttons.get(type);

		button = new Button(buttonComposite, SWT.PUSH);
		switch (type) {
		case OK:
			button.setText("&Finish");
			button.addSelectionListener(new SelectionListener() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					okPressed();
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					okPressed();
				}
			});
			break;
		case CANCEL:
			button.setText("&Cancel");
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					cancelPressed();
				}
			});
			break;
		case NEXT:
			button.setText("&Next >");
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					nextPressed();
				}
			});
			break;
		case BACK:
			button.setText("< &Back");
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					backPressed();
				}
			});
			break;
		case HELP:
			button.setText("&Help");
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					helpPressed();
				}
			});
			break;
		case OVERVIEW:
			button.setText("&Overview");
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					overviewPressed();
				}
			});
			break;
		}

		buttons.put(type, button);

		boolean containsOverview = false;
		for (ButtonType bt : buttons.keySet()) {
			if (ButtonType.OVERVIEW == bt) {
				containsOverview = true;
				break;
			}
		}

		if (containsOverview) {
			FormData fd = new FormData();
			fd.top = new FormAttachment(0, 5);
			fd.right = new FormAttachment(100, -5);
			buttons.get(ButtonType.OVERVIEW).setLayoutData(fd);
		}

		for (ButtonType bt : buttons.keySet()) {

			if (ButtonType.OVERVIEW == bt)
				continue;

			Button current = buttons.get(bt);

			FormData fd = new FormData();
			fd.top = new FormAttachment(buttons.get(ButtonType.OVERVIEW), 5,
					SWT.BOTTOM);

			if (ButtonType.HELP == bt)
				fd.left = new FormAttachment(0, 5);
			else {

				Button cancel = buttons.get(ButtonType.CANCEL);
				Button ok = buttons.get(ButtonType.OK);
				Button next = buttons.get(ButtonType.NEXT);

				// back button
				if (ButtonType.BACK == bt && next != null)
					fd.right = new FormAttachment(next, -5, SWT.LEFT);
				else if (ButtonType.BACK == bt && ok != null)
					fd.right = new FormAttachment(ok, -5, SWT.LEFT);
				else if (ButtonType.BACK == bt && cancel != null)
					fd.right = new FormAttachment(cancel, -5, SWT.LEFT);
				else if (ButtonType.BACK == bt)
					fd.right = new FormAttachment(100, -5);

				// next button
				if (ButtonType.NEXT == bt && ok != null)
					fd.right = new FormAttachment(ok, -5, SWT.LEFT);
				else if (ButtonType.NEXT == bt && cancel != null)
					fd.right = new FormAttachment(cancel, -5, SWT.LEFT);
				else if (ButtonType.NEXT == bt)
					fd.right = new FormAttachment(100, -5);

				// ok button
				if (ButtonType.OK == bt && cancel != null)
					fd.right = new FormAttachment(cancel, -5, SWT.LEFT);
				else if (ButtonType.OK == bt)
					fd.right = new FormAttachment(100, -5);

				if (ButtonType.CANCEL == bt)
					fd.right = new FormAttachment(100, -5);

			}
			current.setLayoutData(fd);
		}

		buttonComposite.layout(true);

		if (buttons.get(ButtonType.OK) != null)
			getShell().setDefaultButton(buttons.get(ButtonType.OK));

		return button;
	}

	/**
	 * If you need to manipulate a button or check if it exists at all, you can
	 * try to fetch it from the wizard. Might be <code>null</code> if the button
	 * was not created.<br>
	 * <br>
	 * Be aware: If you alter the button it might be necessary to refresh the
	 * parent composite (use the {@link #refreshButtons()} method).
	 * 
	 * @param type
	 * @return
	 */
	protected Button getButton(ButtonType type) {
		return buttons.get(type);
	}

	/**
	 * If one ever need to refresh the buttons because of some changes, use this
	 * method to trigger the layout update.
	 */
	public void refreshButtons() {
		buttonComposite.layout(true);
	}

	/**
	 * This helper method is intended to initially set up the buttons after
	 * creation, therefore all buttons must be created within the
	 * {@link #createButtons()} method.
	 */
	private void initButtons() {

		for (ButtonType bt : buttons.keySet())
			buttons.get(bt).setEnabled(false);

		if (buttons.get(ButtonType.CANCEL) != null)
			buttons.get(ButtonType.CANCEL).setEnabled(true);
	}

	/**
	 * The default behavior of the method triggers the creation of the page (in
	 * the means of lazy loading the content and so on) and is triggered by the
	 * OK-button.<br>
	 * Before doing so all prerequisites are checked (Is the page ready? Check
	 * the {@link TreeWizardPage#canProceed} flag of each Page).
	 */
	protected void nextPressed() {

		if (!currentPage.canProceed)
			return;

		final TreeWizardPageElement currentElement = getPageFromStack(
				pageTreeRoot, currentPage.pageId);

		if (currentElement == null)
			return;

		// final Button finish = buttons.get(ButtonType.OK);
		final Button next = buttons.get(ButtonType.NEXT);
		final Button back = buttons.get(ButtonType.BACK);

		// no target id was defined
		if (currentElement.targetId == null
				|| currentElement.targetId.isEmpty()) {

			// if there is exactly one child, use this as the target
			if (currentElement.children.size() == 1) {
				final TreeWizardPageElement child = currentElement.children
						.iterator().next();
				BusyIndicator.showWhile(TreeWizard.this.getDisplay(),
						new Runnable() {
							public void run() {
								currentPage = child.page;
								showPage(currentPage);
								if (next != null) {
									if (child != null
											&& !child.children.isEmpty())
										next.setEnabled(currentPage.canProceed);
									else
										next.setEnabled(false);
								}
								if (back != null) {
									back.setEnabled(true);
								}
								final TreeWizardFocusEvent focusEvent = new TreeWizardFocusEvent(
										currentElement.page, currentPage);
								fireFocusEvent(focusEvent);
							}
						});
			}
			// otherwise we do not know how to react, so do nothing
			else {
				// TODO throw an error
			}
		}
		// a target was defined, so we check all children IDs until we find the
		// correct one
		else {
			for (final TreeWizardPageElement child : currentElement.children) {
				if (child.page.pageId.equals(currentElement.targetId)) {
					BusyIndicator.showWhile(TreeWizard.this.getDisplay(),
							new Runnable() {
								public void run() {
									currentPage = child.page;
									showPage(currentPage);
									if (next != null) {
										if (child != null
												&& !child.children.isEmpty())
											next.setEnabled(currentPage.canProceed);
										else
											next.setEnabled(false);
									}
									if (back != null) {
										back.setEnabled(true);
									}
									final TreeWizardFocusEvent focusEvent = new TreeWizardFocusEvent(
											currentElement.page, currentPage);
									fireFocusEvent(focusEvent);
								}
							});
					break;
				}
			}
		}

		boolean canFinish = pageTreeCanFinish(currentElement);

		if (next != null && canFinish && currentElement != null
				&& currentElement.children.isEmpty())
			next.setEnabled(false);
	}

	/**
	 * Return to the last page in the tree. This method is triggered by the
	 * Back-button.
	 */
	protected void backPressed() {

		TreeWizardPageElement currentElement = getPageFromStack(pageTreeRoot,
				currentPage.pageId);

		if (currentElement == null)
			return;

		Button back = buttons.get(ButtonType.BACK);

		if (currentElement.parent == null) {
			// do something?
		} else {
			currentPage = currentElement.parent.page;
			showPage(currentPage);
			if (back != null) {
				back.setEnabled(currentElement.parent.parent != null);
			}
			final TreeWizardFocusEvent focusEvent = new TreeWizardFocusEvent(
					currentElement.page, currentPage);
			fireFocusEvent(focusEvent);
		}

		Button next = buttons.get(ButtonType.NEXT);
		if (next != null) {
			if (!getPageFromStack(pageTreeRoot, currentPage.pageId).children
					.isEmpty())
				next.setEnabled(currentPage.canProceed);
			else
				next.setEnabled(false);
		}
	}

	/**
	 * TODO: At the moment there is no Overview page, so currently the
	 * implementation does nothing at all!
	 */
	protected void overviewPressed() {

	}

	/**
	 * This method is used to manually trigger button handler (for example the
	 * handler method for the OK button {@link #okPressed()}).<br>
	 * Does nothing if the button has not been created.
	 */
	public void triggerDefaulButtonHandler(ButtonType buttonType) {
		if (buttonType == null) {
			return;
		}
		Button b = buttons.get(buttonType);
		switch (buttonType) {
		case BACK:
			if (b != null) {
				backPressed();
			}
			break;
		case CANCEL:
			if (b != null) {
				cancelPressed();
			}
			break;
		case HELP:
			if (b != null) {
				helpPressed();
			}
			break;
		case NEXT:
			if (b != null) {
				nextPressed();
			}
			break;
		case OK:
			if (b != null) {
				okPressed();
			}
			break;
		case OVERVIEW:
			if (b != null) {
				overviewPressed();
			}
			break;
		}
	}

	/**
	 * On the one hand this method fires an creation event to the called page.
	 * That means, if the page was not visually created before, it will be done
	 * now.<br>
	 * On the other hand the provided page is put on top of the
	 * {@link #pageStack}.
	 * 
	 * @param page
	 *            the page to show
	 */
	private void showPage(TreeWizardPage page) {
		// trigger the creation of the page
		fireEvent(pageTreeRoot, new TreeWizardEvent(page, pageStack));
		// set the top composite of the page stack to this page
		((StackLayout) pageStack.getLayout()).topControl = page
				.getTopComposite();
		pageStack.layout(true);
	}

	/**
	 * This method is used to provide the path in the tree. That is defining the
	 * page that the {@link #nextPressed()} method will try to show.<br>
	 * This methods intention is to set the path dynamically depending on the
	 * users decisions and need to be handled on each page. The easiest way is
	 * to use the pages method {@link TreeWizardPage#setTargetPageID(String)}.
	 * 
	 * @param page
	 *            the page to set the pointer for
	 * @param id
	 *            while the page is the source, the id is the target
	 * @return <code>true</code>, if the update was successful
	 */
	public boolean setTargetPageID(TreeWizardPage page, String id) {

		if (page == null || page.pageId == null || id == null)
			return false;

		TreeWizardPageElement element = getPageFromStack(pageTreeRoot,
				page.pageId);

		if (element == null)
			return false;

		element.targetId = id;

		Button next = buttons.get(ButtonType.NEXT);
		if (next != null) {
			if (!element.children.isEmpty())
				next.setEnabled(currentPage.canProceed);
			else
				next.setEnabled(false);
		}

		Button back = buttons.get(ButtonType.BACK);
		if (back != null) {
			back.setEnabled(element.parent != null
					&& element.parent.parent != null);
		}

		boolean canFinish = pageTreeCanFinish(element);
		Button finish = buttons.get(ButtonType.OK);
		if (finish != null)
			finish.setEnabled(canFinish);

		return true;
	}

	/**
	 * This method is used to add a page into the wizards tree.
	 * 
	 * @param parent
	 *            the parent page, can be <code>null</code>, if the page is the
	 *            root page
	 * @param toAdd
	 *            the page to add to the tree
	 * @return <code>true</code>, if the operation was successful
	 */
	public boolean addPage(TreeWizardPage parent, TreeWizardPage toAdd) {

		if (toAdd == null)
			return false;

		if (parent == toAdd)
			return false;

		if (pageTreeRoot == null)
			pageTreeRoot = new TreeWizardPageElement();

		// TODO handle already existing pages
		// if (pageIdAlreadyInStack(pageTreeRoot, toAdd.pageId))
		// return false;

		// add to root page
		if (parent == null) {

			// there can be only one - first page
			if (pageTreeRoot.children.size() > 0)
				return false;

			TreeWizardPageElement rootChildElement = new TreeWizardPageElement();
			rootChildElement.page = toAdd;
			pageTreeRoot.children.add(rootChildElement);

		}
		// add to an existing page
		else {

			TreeWizardPageElement parentElement = getPageFromStack(
					pageTreeRoot, parent.pageId);

			// parent not found, don't know where to append this page
			if (parentElement == null)
				return false;

			TreeWizardPageElement childElement = new TreeWizardPageElement();
			childElement.parent = parentElement;
			childElement.page = toAdd;
			childElement.targetId = toAdd.pageId;

			parentElement.children.add(childElement);

		}

		// initial page display
		if (currentPage == null) {
			// set the current page
			currentPage = toAdd;
			showPage(currentPage);
		}

		TreeWizardPageElement element = getPageFromStack(pageTreeRoot,
				currentPage.pageId);
		boolean canFinish = pageTreeCanFinish(element);
		Button finish = buttons.get(ButtonType.OK);
		if (finish != null)
			finish.setEnabled(canFinish);

		Button next = buttons.get(ButtonType.NEXT);
		if (next != null) {
			if (!element.children.isEmpty())
				next.setEnabled(currentPage.canProceed);
			else
				next.setEnabled(false);
		}

		return true;
	}

	/**
	 * Public method to check whether or not a page with the provided id exists
	 * within the page tree or not.
	 * 
	 * @param pageId
	 *            the page id to check
	 * @return <code>true</code>, if the page exists within the tree
	 */
	public boolean isPageIdRegistered(String pageId) {

		if (pageId == null || pageId.isEmpty())
			return false;

		if (pageTreeRoot.page == null)
			return false;

		return pageIdAlreadyInStack(pageTreeRoot, pageId);
	}

	/**
	 * Internal method to check whether or not a page exists within the tree.<br>
	 * The method can be used by providing a specific internal page element as
	 * the root to check from.
	 * 
	 * @param root
	 *            the topmost page element to check the id for
	 * @param pageId
	 *            the page id to check for
	 * @return <code>true</code>, if the page exists within the tree
	 */
	private boolean pageIdAlreadyInStack(TreeWizardPageElement root,
			String pageId) {

		for (TreeWizardPageElement element : root.children) {
			if (element.page.pageId.equals(pageId))
				return true;
			else if (pageIdAlreadyInStack(element, pageId))
				return true;
		}

		return false;
	}

	/**
	 * Public method is used to fetch the page for the provided id.
	 * 
	 * @param pageId
	 *            the page id to look for
	 * @return <code>null</code>, if the page could not be found
	 */
	public TreeWizardPage getPage(String pageId) {

		if (pageId == null || pageId.isEmpty())
			return null;

		if (!pageTreeRoot.children.iterator().hasNext())
			return null;

		return getPageFromStack(pageTreeRoot, pageId).page;
	}

	/**
	 * Internal method to fetch a page within the tree.<br>
	 * The method can be used by providing a specific internal page element as
	 * the root to check from.
	 * 
	 * @param root
	 *            the topmost page element to check the id for
	 * @param pageId
	 *            the page id to look for
	 * @return <code>null</code>, if the page could not be found
	 */
	private TreeWizardPageElement getPageFromStack(TreeWizardPageElement root,
			String pageId) {

		for (TreeWizardPageElement element : root.children) {

			// ++ strange bugfix: if not read, the page would not be found!
			String id2test = element.page.pageId;
			// -- bugfix

			if (element.page != null && pageId != null && id2test != null
					&& id2test.equals(pageId))
				return element;
			else {
				TreeWizardPageElement found = getPageFromStack(element, pageId);
				if (found != null)
					return found;
			}
		}

		return null;
	}

	@Override
	protected void checkSubclass() {
	}

	/**
	 * Trigger an event to the all pages in the tree starting from the specified
	 * root element.
	 * 
	 * @param root
	 *            the page element to start with
	 * @param event
	 *            the event to be handled the the appropriate receiver
	 */
	private void fireEvent(TreeWizardPageElement root, TreeWizardEvent event) {

		if (root.page != null)
			root.page.handleEvent(event);

		for (TreeWizardPageElement element : root.children)
			fireEvent(element, event);
	}

	/**
	 * This method is used to add a listener to the stack, which is notified
	 * about focus changes.
	 * 
	 * @param listener
	 *            the listener to add
	 */
	public void addFocusListener(TreeWizardFocusListener listener) {
		if (!focusListeners.contains(listener))
			focusListeners.add(listener);
	}

	/**
	 * This method is used to remove a listener to the stack, which was notified
	 * about focus changes.
	 * 
	 * @param listener
	 *            the listener to remove
	 */
	public void removeFocusListener(TreeWizardFocusListener listener) {
		if (focusListeners.contains(listener))
			focusListeners.add(listener);
	}

	private void fireFocusEvent(TreeWizardFocusEvent event) {
		for (TreeWizardFocusListener listener : focusListeners)
			listener.focusChanged(event);
	}

	/**
	 * This method is currently directly called by the {@link TreeWizardPage}s
	 * <code>canProceed</code> method.
	 */
	@Override
	public void handleEvent(TreeWizardEvent event) {

		TreeWizardPageElement element = getPageFromStack(pageTreeRoot,
				event.targetPage.pageId);

		boolean canFinish = pageTreeCanFinish(element);

		Button finish = buttons.get(ButtonType.OK);
		if (finish != null)
			finish.setEnabled(canFinish);

		Button next = buttons.get(ButtonType.NEXT);
		if (next != null) {
			if (element != null && !element.children.isEmpty())
				next.setEnabled(event.canProceed);
			else
				next.setEnabled(false);

			if (canFinish && element != null && element.children.isEmpty())
				next.setEnabled(false);
		}
	}

	/**
	 * Helper method to check whether or not the current path in the page tree
	 * is complete (every page in this path must provide
	 * <code>canProceed=true</code>)
	 * 
	 * @param element
	 *            the element to check the path for
	 * @return <code>true</code>, if the path in the tree is complete
	 */
	private boolean pageTreeCanFinish(TreeWizardPageElement element) {
		if (element != null
				&& element.page != null
				&& element.page.canProceed
				&& (element.parent == null || pageTreeCanFinish(element.parent))
				&& onePageTreeChildrenCanFinish(element))
			return true;
		else
			return false;

	}

	/**
	 * Another helper method that is used in combination with the
	 * {@link #pageTreeCanFinish(TreeWizardPageElement)} method.
	 * 
	 * @param element
	 *            the page element to check
	 * @return <code>true</code> or <code>false</code>
	 */
	private boolean onePageTreeChildrenCanFinish(TreeWizardPageElement element) {
		if (element.children.isEmpty() && element.page.canProceed)
			return true;
		for (TreeWizardPageElement childElement : element.children) {
			if (childElement.page.canProceed)
				return onePageTreeChildrenCanFinish(childElement);
		}
		return false;
	}

	@Override
	public Object getInternalAdapter(Class<?> adapter) {
		if (TreeWizardPage.class.isAssignableFrom(adapter)) {
			return currentPage;
		} else if (ProgressBar.class.isAssignableFrom(adapter)) {
			return progressBar;
		}
		return null;
	}

	/**
	 * This enumeration contains the supported types of buttons.
	 * 
	 * @author dzimmermann
	 * 
	 */
	public static enum ButtonType {

		// cancel button
		CANCEL,
		// ok button
		OK,
		// button to step forward
		NEXT,
		// button to step backward
		BACK,
		// help button
		HELP,
		// overviewButton
		OVERVIEW;
	}

	/**
	 * Internal class used to wrap around {@link TreeWizardPage}s.
	 * 
	 * @author dzimmermann
	 * 
	 */
	private static class TreeWizardPageElement {

		private TreeWizardPageElement parent;
		private Set<TreeWizardPageElement> children = new HashSet<TreeWizardPageElement>();

		private TreeWizardPage page;
		private String targetId;
	}
}
