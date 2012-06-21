package de.dzimmermann.rcp.basicplatform.ui.util.treewizard;

/**
 * The {@link TreeWizard} fires messages about the focus of the pages.<br>
 * On each page flip, an all registered listeners will be notified about the
 * change of the focus.
 * 
 * @author dzimmermann
 */
public interface TreeWizardFocusListener {

	/**
	 * This method is called each time when the a page is changed within the
	 * {@link TreeWizard}.
	 */
	public void focusChanged(TreeWizardFocusEvent event);
}
