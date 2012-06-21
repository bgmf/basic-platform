package de.dzimmermann.rcp.basicplatform.ui.util;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * This {@link ViewerFilter} just inverts another {@code ViewerFilter}. It is
 * useful for building tables with mutually exclusive content.
 * 
 * @author werner
 * 
 */
public class InvertingFilter extends ViewerFilter {

	private final ViewerFilter filterToInvert;

	/**
	 * Creates a new {@link ViewerFilter} that inverts the provided filter.
	 * 
	 * @param filterToInvert
	 *            the filter to invert
	 */
	public InvertingFilter(ViewerFilter filterToInvert) {
		this.filterToInvert = filterToInvert;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		return !filterToInvert.select(viewer, parentElement, element);
	}

}
