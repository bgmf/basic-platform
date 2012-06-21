package de.dzimmermann.rcp.basicplatform.ui.util;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * This abstract class adds methods to set or get the current search value (the
 * content to search for) and forces a client to implement the search method and
 * the method to set a search option.
 * 
 * @author dzimmermann
 * @since PFSRCPCore 0.5.1
 * @version 0.2
 */
public abstract class AbstractSearchViewerFilter extends ViewerFilter {

	/**
	 * The parameter to search for.
	 */
	protected String searchValue;

	/**
	 * This method uses a string which is specifying the name of a search option
	 * - may come in handy, when used with enums or other constants...
	 * 
	 * @param searchOptionName
	 *            the name of the option, which is used to filter the conten for
	 */
	public abstract void setSearchOption(String searchOptionName);

	/**
	 * Use this method to retrieve the last option name.
	 * 
	 * @return the current option name
	 */
	public abstract String getSearchOption();

	/**
	 * This method performs the actual search. The default implementation of the
	 * {@link #select(Viewer, Object, Object)} method calls this method to
	 * decide wheter a element passes the search or not.
	 * 
	 * @param viewer
	 *            the viewer on which the filter will be performed
	 * @param parentElement
	 *            the parent element of the search
	 * @param the
	 *            element in question
	 * @return <code>true</code>, if the elememt passes the search,
	 *         <code>false</code> otherwise
	 */
	protected abstract boolean passesSearch(Viewer viewer,
			Object parentElement, Object element);

	/**
	 * This method is intended to refresh a viewer.
	 * 
	 * @since 0.2
	 */
	public abstract void refresh();

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public String getSearchValue() {
		return searchValue;
	}

	/**
	 * The default implementation used only the
	 * {@link #passesSearch(Viewer, Object, Object)} method, to decide whether
	 * or not an element passes.<br>
	 * Clients, which need more filter options, should call {@code
	 * super.select(viewer, parentElement, element)} first, before applying
	 * different operations to not interfere with the search.
	 */
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		return passesSearch(viewer, parentElement, element);
	}
}
