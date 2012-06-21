package de.dzimmermann.rcp.basicplatform.ui.util.treewizard;

import org.eclipse.swt.widgets.Composite;

/**
 * This class is used for internal events that are fired from wizard to page or
 * vice versa. The use ranges from page creation to pages present there state.
 * 
 * @author dzimmermann
 * 
 */
public class TreeWizardEvent {

	/**
	 * The page the event is pointed to or triggered by.
	 */
	protected TreeWizardPage targetPage;

	/**
	 * In the case of page creation events, this is the parent composite on
	 * which the pages will create their top composite.
	 */
	protected Composite pageStack;

	/**
	 * The state of the page. Only applicable in the case that the page was
	 * triggering the event.
	 */
	protected boolean canProceed;

	public TreeWizardEvent() {
	}

	/**
	 * This constructor is used to the create and/or show event.
	 * 
	 * @param targetPage
	 *            the page to show or create
	 * @param pageStack
	 *            the parent composite
	 */
	public TreeWizardEvent(TreeWizardPage targetPage, Composite pageStack) {
		this.targetPage = targetPage;
		this.pageStack = pageStack;
	}

	@Override
	public String toString() {
		return String
				.format("%s [target-page-id=%s, pass-down-page-stack=%b, page-can-proceed=%b]",
						TreeWizardEvent.class.getName(),
						targetPage != null ? targetPage.pageId : "null",
						pageStack != null, canProceed);
	}
}
