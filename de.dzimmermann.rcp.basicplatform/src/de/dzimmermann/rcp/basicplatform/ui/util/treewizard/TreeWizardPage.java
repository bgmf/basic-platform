package de.dzimmermann.rcp.basicplatform.ui.util.treewizard;

import org.eclipse.swt.widgets.Composite;

import de.dzimmermann.rcp.basicplatform.util.InternalAdapter;

/**
 * This abstract class is the base of all pages of the {@link TreeWizard}.<br>
 * Implementation provide the means for the wizard to check, whether a page is
 * complete or not. If a branch - from root to leaf - is complete (see
 * {@link #canProceed}), the wizard enables the finish button.
 * 
 * @author dzimmermann
 * 
 */
public abstract class TreeWizardPage implements TreeWizardListener,
		InternalAdapter {

	/**
	 * The ID of this page. This value is <i>required</i>!
	 */
	protected final String pageId;
	/**
	 * The name of this page. This value is <i>optional</i>.
	 */
	protected String pageName;
	/**
	 * Additionally to the name, the description might come in handy for
	 * overview tooltips etc. to describe the task of a page.
	 */
	protected String desription;

	/**
	 * The parent {@link TreeWizard} on which this page is printed on. This
	 * value is <i>required</i>!
	 */
	protected final TreeWizard wizard;

	/**
	 * The switch of this value should <b>always</b> been done via the
	 * {@link #canProceed(boolean)} method. This value is used by the
	 * {@link TreeWizard} to check, whether one can proceed to the next page or
	 * finish the wizard at all.
	 */
	protected boolean canProceed = true;

	/**
	 * An indicator, whether or not this page has already been created. Used for
	 * lazy loading.
	 */
	private boolean isCreated = false;

	/**
	 * A local variable indicating whether or not the page should trigger the
	 * {@link #lazyLoadedPageContent()} method each time it comes in focus.<br>
	 * The default is <code>false</code>.
	 */
	private boolean forceReload = false;

	public TreeWizardPage(TreeWizard wizard, String pageId, String pageName,
			boolean canProceed) {
		this(wizard, pageId, pageName);
		this.canProceed = canProceed;
	}

	public TreeWizardPage(TreeWizard wizard, String pageId, boolean canProceed) {
		this(wizard, pageId);
		this.canProceed = canProceed;
	}

	public TreeWizardPage(TreeWizard wizard, String pageId, String pageName) {
		this(wizard, pageId);
		this.pageName = pageName;
	}

	public TreeWizardPage(TreeWizard wizard, String pageId) {
		this.wizard = wizard;
		this.pageId = pageId;
	}

	/**
	 * Use this method, if you need to know a pages ID.<br>
	 * This value is <i>required</i>!
	 * 
	 * @return the ID of this page
	 */
	public String getPageId() {
		return pageId;
	}

	/**
	 * Use this method, if you need to know a pages name.<br>
	 * This value is <i>optional</i>.
	 * 
	 * @return the name of this page
	 */
	public String getPageName() {
		return pageName;
	}

	/**
	 * Since the description might not necessarily be needed by an overview, the
	 * default will be null.<br>
	 * Use this method if there is a need for an implementation of a page.
	 * 
	 * @param desription
	 */
	public void setDesription(String desription) {
		this.desription = desription;
	}

	/**
	 * Use this method, if you need to know a pages description.<br>
	 * This value is <i>optional</i>.
	 * 
	 * @return the description of this page
	 */
	public String getDesription() {
		return desription;
	}

	/**
	 * If you ever need access to the underlying parent {@link TreeWizard} of a
	 * page, use this method get it.<br>
	 * This value is <i>required</i>!
	 * 
	 * @return the parent {@link TreeWizard} on which this page is printed on
	 */
	public TreeWizard getWizard() {
		return wizard;
	}

	/**
	 * External parts of a wizard might be interested in the can proceed stage
	 * of each page. This method enables the user to check whether or not this
	 * page is able to proceed.
	 * 
	 * @return <code>true</code>, if this page is completed, otherwise
	 *         <code>false</code>
	 */
	public boolean isCanProceed() {
		return canProceed;
	}

	/**
	 * Internally a variable indicates, whether this page has been created (on
	 * showing it for the first time) or not.<br>
	 * If you ever need to know, whether or not this has already been done, use
	 * this method to check for it.
	 * 
	 * @return <code>true</code>, if the page was shown at least one time,
	 *         <code>false</code> otherwise
	 */
	public boolean isCreated() {
		return isCreated;
	}

	/**
	 * This method is used to set the current state of whether or not the page
	 * should trigger the {@link #lazyLoadedPageContent()} method each time.<br>
	 * If not set, the default value will be <code>false</code>, so the method
	 * will only called once, when the page is shown for the first time.
	 * lazyLoadedPageContent
	 * 
	 * @param forceReload
	 *            if set to <code>true</code>, the page will trigger the
	 *            <code>lazyLoadedPageContent()</code> method each time the page
	 *            will be shown
	 */
	public void setForceReload(boolean forceReload) {
		this.forceReload = forceReload;
	}

	/**
	 * This method is used to retrieve the current state of whether or not the
	 * page should trigger the {@link #lazyLoadedPageContent()} method each
	 * time.
	 * 
	 * @return since it is of a boolean value, it can either be
	 *         <code>true</code> or <code>false</code>, while the latter is the
	 *         default value
	 */
	public boolean isForceReload() {
		return forceReload;
	}

	/**
	 * This method is called once, before this page will be created for the
	 * first time. This is useful, if there is some content to be loaded during
	 * runtime, but not before the page is created the first time.<br>
	 * On the other hand it might come in handy if you want to reload data
	 * later.
	 */
	protected void lazyLoadedPageContent() {
		// Default implementation does nothing. Overwrite, if needed!
	}

	/**
	 * Implementations have to use this method to create the pages content.<br>
	 * <br>
	 * If your page need to lazy load some data once, you can either do it here
	 * or overwrite the {@link #lazyLoadedPageContent()} method which is called
	 * once before the page content will be created.<br>
	 * <br>
	 * Be aware: After the first load, the content is stable. You need to remove
	 * the page and reprint it, if you need to change something during runtime.
	 * That means, that this method will be called only once in the wizards
	 * lifecycle.
	 * 
	 * @param pageStack
	 *            the {@link Composite} on which this page will be displayed on
	 * @return the top {@link Composite} of this page (see
	 *         {@link #getTopComposite()})
	 */
	protected abstract Composite createContent(Composite pageStack);

	/**
	 * This should point to the implementation dependent top {@link Composite}
	 * of the active page. Otherwise the wizard is not able to show the page.
	 * 
	 * @return the top {@link Composite} of this page
	 */
	protected abstract Composite getTopComposite();

	/**
	 * This method is of an optional use: It will be triggered, when the wizard
	 * proceeds to the active page only. In implementations this can used to
	 * trigger message updates, that are specific for the active page.
	 * 
	 * @return can return a boolean, but this is implementation dependent, the
	 *         wizard does nothing with the result.
	 */
	protected abstract boolean checkPageContent();

	/**
	 * Trigger the update of this page. using only {@link #canProceed} would not
	 * trigger the wizard to check the tree, if it is possible to proceed of
	 * even finish. That means, implementation <b>have to use</b> this method.
	 * 
	 * @param canProceed
	 *            <code>true</code>, if this page is complete
	 */
	public void canProceed(boolean canProceed) {

		// set the local value (of course)
		this.canProceed = canProceed;

		// create an event...
		TreeWizardEvent event = new TreeWizardEvent();
		event.targetPage = this;
		event.canProceed = canProceed;
		// ... and trigger it at the wizard side
		wizard.handleEvent(event);
	}

	/**
	 * Using this method implementation can trigger the tree to be set up for
	 * the correct path in the page tree.
	 * 
	 * @param id
	 *            the id this page should point to
	 * @return <code>true</code>, if the operation was successful
	 */
	public boolean setTargetPageID(String id) {
		return wizard.setTargetPageID(this, id);
	}

	/**
	 * This method, provided because this page is a natural listener to the
	 * wizard, handles the events, the wizard fires on opportunities like
	 * switching to a (new/not yet shown) page.
	 */
	@Override
	public void handleEvent(TreeWizardEvent event) {

		// do only something on this page, if it is the targeted one
		if (event.targetPage.pageId == this.pageId) {

			// create page, if this has not happened yet
			if (!isCreated) {
				// call the lazy load method
				lazyLoadedPageContent();
				// create the page
				createContent(event.pageStack);
				isCreated = true;
			}
			// call the lazy load method repeatedly each time, the page comes in
			// focus of the wizard (when it becomes the topmost page in the
			// stack of the wizards pages)
			else if (isCreated && forceReload) {
				lazyLoadedPageContent();
			}

			// +++ bugfix
			// Without this call, the finish button would be available on wrong
			// pages, if another branch is complete.
			// The problem is, that the values are not checked, so this is a
			// forced check with the current canProceed value.
			canProceed(canProceed);
			// --- bugfix

			// By adding this method here, only the active page will have the
			// possibility to check it's content and react in each way it
			// prefers.
			// An alternative would be the back and forward methods of the
			// wizard itself, but this is quite not as flexible as this.
			// XXX The result of the checkPageContent call is currently unused!
			@SuppressWarnings("unused")
			boolean checkPageContentResult = checkPageContent();
		}
	}

	@Override
	public Object getInternalAdapter(Class<?> adapter) {
		if (TreeWizardPage.class.isAssignableFrom(adapter)) {
			return pageId;
		}
		if (String.class.isAssignableFrom(adapter)) {
			return pageName;
		}
		if (TreeWizard.class.isAssignableFrom(adapter)) {
			return wizard;
		}
		if (boolean.class.isAssignableFrom(adapter)) {
			return canProceed;
		}
		return null;
	}
}
