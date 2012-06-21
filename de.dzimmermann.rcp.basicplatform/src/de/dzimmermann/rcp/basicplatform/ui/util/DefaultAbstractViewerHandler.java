package de.dzimmermann.rcp.basicplatform.ui.util;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.swt.widgets.Listener;

public abstract class DefaultAbstractViewerHandler implements Listener {

	protected Viewer viewer;
	protected ViewerColumn column;

	protected int columnIndex;

	public DefaultAbstractViewerHandler(Viewer viewer, ViewerColumn column) {
		this.viewer = viewer;
		this.column = column;
	}

	protected Viewer getViewer() {
		return viewer;
	}

	protected ViewerColumn getColumn() {
		return column;
	}

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	public int getColumnIndex() {
		return columnIndex;
	}
}
