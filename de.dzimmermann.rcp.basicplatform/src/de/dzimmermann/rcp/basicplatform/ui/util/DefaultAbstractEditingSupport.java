package de.dzimmermann.rcp.basicplatform.ui.util;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;

import de.dzimmermann.rcp.basicplatform.util.DirtyEditorSupport;
import de.dzimmermann.rcp.basicplatform.util.ElementUpdateSupport;
import de.dzimmermann.rcp.basicplatform.util.PersistingSupport;

public abstract class DefaultAbstractEditingSupport extends EditingSupport {

	protected String columnName;
	protected int columnIndex;

	protected CellEditor editor;

	protected DirtyEditorSupport parentEditor;
	protected ElementUpdateSupport<?> elementUpdateSupport;
	protected PersistingSupport<?> persistingSupport;

	public DefaultAbstractEditingSupport(ColumnViewer viewer,
			String columnName, Integer columnIndex) {
		super(viewer);

		this.columnName = columnName;
		this.columnIndex = columnIndex;
	}

	public void setColumnEditor(CellEditor editor) {
		this.editor = editor;
	}

	public CellEditor getColumnEditor() {
		return editor;
	}

	public String getColumnName() {
		return columnName;
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public void setParentEditor(DirtyEditorSupport parentEditor) {
		this.parentEditor = parentEditor;
	}

	public DirtyEditorSupport getParentEditor() {
		return parentEditor;
	}

	public void setElementUpdateSupport(
			ElementUpdateSupport<?> elementUpdateSupport) {
		this.elementUpdateSupport = elementUpdateSupport;
	}

	public ElementUpdateSupport<?> getElementUpdateSupport() {
		return elementUpdateSupport;
	}

	public void setPersistingSupport(PersistingSupport<?> persistingSupport) {
		this.persistingSupport = persistingSupport;
	}

	public PersistingSupport<?> getPersistingSupport() {
		return persistingSupport;
	}
}
