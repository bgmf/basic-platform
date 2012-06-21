package de.dzimmermann.rcp.basicplatform.ui.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * This is a very basic implementation of the {@link IStructuredContentProvider}
 * interface. It creates the content for most of the lists, combos and tables by
 * providing the content of simple arrays, {@link Set}s, {@link List}s,
 * {@link Map}s and {@link Collection}s at all.
 * 
 * @author dzimmermann
 * @since PFSRCPCore 0.0.0
 * @version 1.0
 */
public class DefaultStructuredContentProvider implements
		IStructuredContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {

		if (inputElement instanceof Object[]) {
			return (Object[]) inputElement;
		}

		if (inputElement instanceof Set<?>) {
			return ((Set<?>) inputElement).toArray();
		}

		if (inputElement instanceof List<?>) {
			return ((List<?>) inputElement).toArray();
		}

		if (inputElement instanceof Map<?, ?>) {
			return ((Map<?, ?>) inputElement).keySet().toArray();
		}

		if (inputElement instanceof Collection<?>) {
			return ((Collection<?>) inputElement).toArray();
		}

		return null;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
}
