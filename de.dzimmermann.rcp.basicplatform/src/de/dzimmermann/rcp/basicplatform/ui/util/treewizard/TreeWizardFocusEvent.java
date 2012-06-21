package de.dzimmermann.rcp.basicplatform.ui.util.treewizard;

/**
 * This event is triggered by the {@link TreeWizard}, which is notifying all
 * registered {@link TreeWizardFocusListener}s about the focus change.
 * 
 * @author dzimmermann
 */
public final class TreeWizardFocusEvent {

	/**
	 * The page, that has lost the focus.
	 */
	protected final TreeWizardPage lastTreeWizardPage;
	/**
	 * The page, that has gained the focus.
	 */
	protected final TreeWizardPage focusedTreeWizardPage;

	/**
	 * This is the <code>protected</code> constructor that is used by the
	 * {@link TreeWizard} to prepare an event.
	 * 
	 * @param lastTreeWizardPage
	 *            the page, that has lost the focus
	 * @param focusedTreeWizardPage
	 *            the page, that has gained the focus
	 */
	protected TreeWizardFocusEvent(TreeWizardPage lastTreeWizardPage,
			TreeWizardPage focusedTreeWizardPage) {
		this.lastTreeWizardPage = lastTreeWizardPage;
		this.focusedTreeWizardPage = focusedTreeWizardPage;
	}

	/**
	 * See {@link #lastTreeWizardPage}.<br>
	 * A <code>null</code>-check is advised before used.
	 * 
	 * @return the page, that has lost the focus
	 */
	public TreeWizardPage getLastTreeWizardPage() {
		return lastTreeWizardPage;
	}

	/**
	 * See {@link #focusedTreeWizardPage}.<br>
	 * A <code>null</code>-check is advised before used.
	 * 
	 * @return the page, that has gained the focus
	 */
	public TreeWizardPage getFocusedTreeWizardPage() {
		return focusedTreeWizardPage;
	}
}
