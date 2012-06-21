package de.dzimmermann.rcp.basicplatform.util;

import java.util.Collection;

import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.Viewer;

import de.dzimmermann.rcp.basicplatform.ui.util.DefaultAbstractEditingSupport;

/**
 * This interface is intended to be used to update elements in UIs. It occures
 * mor than once to me, that this is a sometimes missing feature especially when
 * it comes to some {@link Viewer}s {@link EditingSupport}.
 * <p>
 * 
 * Maybe it would be wise to update the {@link DefaultAbstractEditingSupport}
 * and {@link MainPFSTableLabelProvider} to recognize this class and provide it
 * to an implementor.
 * 
 * @author dzimmermann
 * @since PFSRCPCore 0.4.0
 * @version 0.1
 * 
 * @param <T>
 *            This parameter indicates the values, that need to be updated.
 */
public interface ElementUpdateSupport<T> {

	/**
	 * Use this method, if there is only one element, that needs to be updated.
	 * 
	 * @param elementToUpdate
	 *            the element, that has changed or simply needs an update
	 */
	public void updateElement(T elementToUpdate);

	/**
	 * Use this method if you have more than one element (see {@link Collection}
	 * ) that need an update.
	 * 
	 * @param elementsToUpdate
	 *            the {@link Collection} of elements that needs to be updated.
	 */
	public void updateElements(Collection<T> elementsToUpdate);
}
