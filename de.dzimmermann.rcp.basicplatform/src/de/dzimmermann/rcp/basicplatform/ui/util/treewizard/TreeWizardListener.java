package de.dzimmermann.rcp.basicplatform.ui.util.treewizard;

/**
 * This interface is intended to be used to handle {@link TreeWizard} and
 * {@link TreeWizardPage} internal events.
 * 
 * @author dzimmermann
 * 
 */
public interface TreeWizardListener {

	/**
	 * Handles a {@link TreeWizardEvent} that is either fired from the wizard to
	 * the page (on page creation - see {@link TreeWizard#nextPressed()}) or
	 * from page to wizard (on {@link TreeWizardPage#canProceed(boolean)}).
	 * 
	 * @param event
	 *            the {@link TreeWizardEvent} to handle
	 */
	public void handleEvent(TreeWizardEvent event);
}
