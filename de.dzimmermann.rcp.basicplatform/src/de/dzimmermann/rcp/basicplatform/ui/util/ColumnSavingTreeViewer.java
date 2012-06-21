package de.dzimmermann.rcp.basicplatform.ui.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class ColumnSavingTreeViewer extends TreeViewer {

	private final AbstractUIPlugin plugin;
	private final String tableId;

	private int[] previousWidths;

	public ColumnSavingTreeViewer(String tableId, AbstractUIPlugin plugin,
			Composite parent) {
		super(parent);

		this.plugin = plugin;
		this.tableId = tableId;
		init();
	}

	public ColumnSavingTreeViewer(String tableId, AbstractUIPlugin plugin,
			Composite parent, int style) {
		super(parent, style);

		this.plugin = plugin;
		this.tableId = tableId;
		init();
	}

	public ColumnSavingTreeViewer(String tableId, AbstractUIPlugin plugin,
			Tree tree) {
		super(tree);

		this.plugin = plugin;
		this.tableId = tableId;
		init();
	}

	private void init() {
		IDialogSettings settings = plugin.getDialogSettings();

		IDialogSettings sectionTmp = settings.getSection(tableId);
		if (sectionTmp == null) {
			sectionTmp = settings.addNewSection(tableId);
		}

		final IDialogSettings section = sectionTmp;

		this.previousWidths = restoreWidths(section);

		getTree().addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				saveWidths(section);
			}
		});
	}

	/**
	 * Restores the previous widths if the column count is the same as the
	 * column count of the current table.
	 * 
	 * @return the restored column widths or {@code null} if the column counts
	 *         differed
	 */
	public int[] restorePreviousWidths() {
		Tree tree = getTree();
		TreeColumn[] columns = tree.getColumns();

		if (previousWidths.length != columns.length) {
			return null;
		}

		for (int i = 0; i < columns.length; i++) {
			TreeColumn col = columns[i];
			int width = previousWidths[i];

			col.setWidth(width);
		}

		return previousWidths.clone();
	}

	/**
	 * Sets the table columns to the specified minimum or maximum width, if they
	 * are out of these boundaries.<br>
	 * Attention: Values of zero ore below for either of the width types are
	 * ignored, nothing will be done in this case!
	 * 
	 * @param min
	 *            the lowest width for a table column
	 * @param max
	 *            the highest width for a table column
	 * 
	 * @return the restored column widths or {@code null} if the column counts
	 *         differed
	 */
	public int[] restoreMinMaxWidth(int min, int max) {
		Tree tree = getTree();
		TreeColumn[] columns = tree.getColumns();

		if (previousWidths.length != columns.length) {
			return null;
		}

		for (int i = 0; i < columns.length; i++) {
			TreeColumn col = columns[i];
			if (min > 0 && col.getWidth() < min) {
				col.setWidth(min);
				if (previousWidths != null) {
					previousWidths[i] = min;
				}
			} else if (max > 0 && col.getWidth() > max) {
				col.setWidth(max);
				if (previousWidths != null) {
					previousWidths[i] = max;
				}
			}
		}

		return previousWidths.clone();
	}

	/**
	 * Restores the previous widths if the column count is the same as the
	 * column count of the current table.<br>
	 * Additionally this method sets the table columns to the specified minimum
	 * or maximum width, if they are out of these boundaries.<br>
	 * Attention: Values of zero ore below for either of the width types are
	 * ignored, nothing will be done in this case!<br>
	 * <br>
	 * This method is a pure combination of {@link #restorePreviousWidths()} and
	 * {@link #restoreMinMaxWidth(int, int)} (in this order).
	 * 
	 * @param min
	 *            the lowest width for a table column
	 * @param max
	 *            the highest width for a table column
	 * 
	 * @return the restored column widths or {@code null} if the column counts
	 *         differed
	 */
	public int[] restorePreviousWidths(int min, int max) {
		if (restorePreviousWidths() == null)
			return null;
		return restoreMinMaxWidth(min, max);
	}

	private int[] restoreWidths(IDialogSettings section) {
		List<Integer> widthsList = new ArrayList<Integer>();

		int currentCol = 0;
		while (true) {
			try {
				int width = section.getInt("col" + currentCol++);
				widthsList.add(width);
			} catch (NumberFormatException e) {
				break;
			}

		}

		int widthArray[] = new int[widthsList.size()];
		for (int i = 0; i < widthsList.size(); i++) {
			widthArray[i] = widthsList.get(i).intValue();
		}
		return widthArray;
	}

	private void saveWidths(IDialogSettings section) {
		final Tree tree = getTree();
		TreeColumn[] columns = tree.getColumns();
		for (int i = 0; i < columns.length; i++) {
			section.put("col" + i, columns[i].getWidth());
		}
	}

}
