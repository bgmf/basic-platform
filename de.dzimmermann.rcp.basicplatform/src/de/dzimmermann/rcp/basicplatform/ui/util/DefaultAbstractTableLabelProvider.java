package de.dzimmermann.rcp.basicplatform.ui.util;

import org.eclipse.jface.layout.AbstractColumnLayout;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TableColumn;

import de.dzimmermann.rcp.basicplatform.util.DirtyEditorSupport;
import de.dzimmermann.rcp.basicplatform.util.ElementUpdateSupport;
import de.dzimmermann.rcp.basicplatform.util.PersistingSupport;

/**
 * This class represents a helper for the creation of new {@link TableViewer}s
 * by providing a relativly simple method to create the header, make the tables
 * columns dynamic in range and attach a sorting handler (simple
 * {@link SWT#Selection} value for the header) or add a default editing support.
 * 
 * @author dzimmermann
 * @since PFSRCPCore V0.0.0
 * @version 0.4
 */
public abstract class DefaultAbstractTableLabelProvider extends LabelProvider
		implements ITableLabelProvider {

	/**
	 * this attribute represents the {@link Viewer} on which this
	 * {@link ITableLabelProvider} operates
	 */
	protected Viewer viewer;

	/**
	 * if this attribute is not <code>null</code>, the the dirty-setting may be
	 * used by this label provider
	 */
	protected DirtyEditorSupport parentEditor;
	/**
	 * if this attribute is not <code>null</code>, the label provider can make
	 * use of an {@link ElementUpdateSupport} object, but the implementor needs
	 * to know, what type of object is used
	 */
	protected ElementUpdateSupport<?> elementUpdateSupport;
	/**
	 * if this attribute is not <code>null</code>, the label provider can make
	 * use of an {@link PersistingSupport} object, but the implementor needs to
	 * know, what type of object is used
	 */
	protected PersistingSupport<?> persistingSupport;

	/**
	 * the header of the table
	 */
	protected String[] tableHeaders;

	/**
	 * the header menu, contains eather a show/hide or restore column menu
	 */
	protected Menu headerMenu;
	private MenuItem topItem;
	private Menu headerContentMenu;

	/**
	 * the table body menu - the implementor decides, what it is used for
	 */
	protected Menu tableMenu;

	public DefaultAbstractTableLabelProvider(Viewer viewer) {
		this.viewer = viewer;
		this.headerMenu = new Menu(viewer.getControl());
		this.tableMenu = new Menu(viewer.getControl());
	}

	/*
	 * getter for protected fields
	 */

	public Viewer getViewer() {
		return viewer;
	}

	public String[] getTableHeaders() {
		return tableHeaders;
	}

	public Menu getHeaderMenu() {
		return headerMenu;
	}

	public Menu getTableMenu() {
		return tableMenu;
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

	/*
	 * table header creation
	 */

	/**
	 * initiate the table header, if you table is on a {@link Composite} using
	 * {@link TableColumnLayout}, use this method before initiating the weights
	 * using the {@link #initTableCoulmnWeights(Layout)} (using internally
	 * defined values) or
	 * {@link #initTableCoulmnWeights(Layout, int, int[], int[], boolean[], boolean[])}
	 * method
	 * 
	 * @param tableHeaders
	 */
	public void createTableColumns(String[] tableHeaders) {
		createTableColumns(tableHeaders, SWT.None, null, null);
	}

	public void createTableColumns(String[] tableHeaders,
			boolean showHideColumnsMenu) {
		createTableColumns(tableHeaders, showHideColumnsMenu, SWT.None, null,
				null);
	}

	public void createTableColumns(String[] tableHeaders,
			int swtViewerHandlerEventType,
			Class<? extends DefaultAbstractViewerHandler> viewerHandlerClass) {
		createTableColumns(tableHeaders, swtViewerHandlerEventType,
				viewerHandlerClass, null);
	}

	public void createTableColumns(String[] tableHeaders,
			boolean showHideColumnsMenu, int swtViewerHandlerEventType,
			Class<? extends DefaultAbstractViewerHandler> viewerHandlerClass) {
		createTableColumns(tableHeaders, showHideColumnsMenu,
				swtViewerHandlerEventType, viewerHandlerClass, null);
	}

	public void createTableColumns(String[] tableHeaders,
			Class<? extends DefaultAbstractEditingSupport> editingSupportClass) {
		createTableColumns(tableHeaders, SWT.None, null, editingSupportClass);
	}

	public void createTableColumns(String[] tableHeaders,
			boolean showHideColumnsMenu,
			Class<? extends DefaultAbstractEditingSupport> editingSupportClass) {
		createTableColumns(tableHeaders, false, SWT.None, null,
				editingSupportClass);
	}

	public void createTableColumns(String[] tableHeaders,
			int swtViewerHandlerEventType,
			Class<? extends DefaultAbstractViewerHandler> viewerHandlerClass,
			Class<? extends DefaultAbstractEditingSupport> editingSupportClass) {
		createTableColumns(tableHeaders, false, swtViewerHandlerEventType,
				viewerHandlerClass, editingSupportClass);
	}

	public void createTableColumns(String[] tableHeaders,
			boolean showHideColumnsMenu, int swtViewerHandlerEventType,
			Class<? extends DefaultAbstractViewerHandler> viewerHandlerClass,
			Class<? extends DefaultAbstractEditingSupport> editingSupportClass) {
		if (tableHeaders != null) {
			this.tableHeaders = tableHeaders;
		}

		if (this.tableHeaders != null) {

			for (int index = 0; index < this.tableHeaders.length; index++) {

				int width = columnWidth.length == this.tableHeaders.length ? columnWidth[index]
						: this.tableHeaders[index].length() + 150;
				String text = this.tableHeaders[index];

				final TableViewerColumn tableViewerColumn = new TableViewerColumn(
						(TableViewer) viewer, SWT.NONE);

				tableViewerColumn.getColumn().setWidth(width);
				tableViewerColumn.getColumn().setText(text);
				tableViewerColumn.getColumn().setResizable(true);

				// attach a "viewer handler" (e.g.: to sort the table)
				if (viewerHandlerClass != null) {

					try {
						DefaultAbstractViewerHandler viewerHandler = viewerHandlerClass
								.getConstructor(Viewer.class,
										ViewerColumn.class).newInstance(viewer,
										tableViewerColumn);

						viewerHandler.setColumnIndex(index);

						tableViewerColumn.getColumn().addListener(
								swtViewerHandlerEventType, viewerHandler);

					} catch (Exception e) {
						Logger.logError(e);
						e.printStackTrace();
					}
				}

				// attach the editing support for this column (in common the
				// editing support will decide, which type of editor will be
				// used for each column)
				if (editingSupportClass != null) {

					try {
						DefaultAbstractEditingSupport editingSupport = editingSupportClass
								.getConstructor(ColumnViewer.class,
										String.class, Integer.class)
								.newInstance(viewer, text,
										Integer.valueOf(index));

						editingSupport.parentEditor = parentEditor;
						editingSupport.elementUpdateSupport = elementUpdateSupport;
						editingSupport.persistingSupport = persistingSupport;

						tableViewerColumn.setEditingSupport(editingSupport);

					} catch (Exception e) {
						Logger.logError(e);
						e.printStackTrace();
					}
				}

				tableViewerColumn.getColumn().setMoveable(showHideColumnsMenu);

				// attach the show/hide menu for the columns
				if (showHideColumnsMenu) {

					// the menu to show and hide table columns
					if (topItem == null) {
						topItem = new MenuItem(headerMenu, SWT.CASCADE);
						topItem.setText("show/hide columns");
					}
					if (headerContentMenu == null) {
						headerContentMenu = new Menu(topItem);
						topItem.setMenu(headerContentMenu);
						headerMenu.setDefaultItem(topItem);
					}

					createShowHideMenuItem(headerContentMenu,
							tableViewerColumn.getColumn(), index);

				}
				// attach a default "restore column defaults" menu item
				// TODO RESTORE COLUM BUG: closed/hidden columns do not restore
				else {

					if (topItem == null) {
						topItem = new MenuItem(headerMenu, SWT.PUSH);
						topItem.setText("restore column defaults");

						topItem.addListener(SWT.Selection, new Listener() {

							@Override
							public void handleEvent(Event event) {

								if (viewer instanceof TableViewer) {

									TableViewer tv = (TableViewer) viewer;

									if (columnCount > 0
											&& tv.getTable().getParent()
													.getLayout() instanceof AbstractColumnLayout) {
										initTableCoulmnWeights(tv.getTable()
												.getParent().getLayout());
									} else {
										for (TableColumn tc : tv.getTable()
												.getColumns()) {
											tc.setWidth(tc.getText().length() + 150);
										}
									}

									tv.getTable().update();
									tv.getTable()
											.getParent()
											.layout(new Control[] { tv
													.getTable() });
								}
							}
						});
					}
				}
			}

			if (viewer instanceof TableViewer) {

				((TableViewer) viewer).getTable().addListener(SWT.MenuDetect,
						new Listener() {

							public void handleEvent(Event event) {

								Point pt = Display.getCurrent().map(null,
										((TableViewer) viewer).getTable(),
										new Point(event.x, event.y));

								Rectangle clientArea = ((TableViewer) viewer)
										.getTable().getClientArea();

								boolean header = clientArea.y <= pt.y
										&& pt.y < (clientArea.y + ((TableViewer) viewer)
												.getTable().getHeaderHeight());

								if (header && headerMenu != null) {

									((TableViewer) viewer).getTable().setMenu(
											headerMenu);

								} else if (!header && tableMenu != null) {

									((TableViewer) viewer).getTable().setMenu(
											tableMenu);
								}
							}
						});
			}
		}

		if (viewer instanceof ColumnSavingTableViewer) {
			int[] previousWidths = ((ColumnSavingTableViewer) viewer)
					.restorePreviousWidths(minimumWidth, maximumWidth);
			if (previousWidths != null
					&& previousWidths.length == columnWidth.length) {
				System.arraycopy(previousWidths, 0, columnWidth, 0,
						previousWidths.length);
			}
		}
	}

	/**
	 * Set this value in case the
	 * {@link DefaultAbstractTableLabelProvider#viewer} is a
	 * {@link ColumnSavingTableViewer} to specify the minimum width.<br/>
	 * The default is <code>25</code>.
	 */
	protected int minimumWidth = 25;
	/**
	 * Set this value in case the
	 * {@link DefaultAbstractTableLabelProvider#viewer} is a
	 * {@link ColumnSavingTableViewer} to specify the maximum width.<br/>
	 * The default is <code>500</code>.
	 */
	protected int maximumWidth = 500;

	/*
	 * hide and show table columns
	 */

	private void createShowHideMenuItem(final Menu parent,
			final TableColumn column, final int columnIndex) {

		final MenuItem menuItem = new MenuItem(parent, SWT.CHECK);

		menuItem.setText(column.getText());
		menuItem.setSelection(column.getResizable());

		menuItem.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {

				if (menuItem.getSelection()) {

					if (viewer instanceof TableViewer
							&& ((TableViewer) viewer).getTable().getParent()
									.getLayout() instanceof AbstractColumnLayout
							&& columnCount > 0 && columnIndex < columnCount) {

						column.setWidth(columnWidth[columnIndex]);

						((AbstractColumnLayout) ((TableViewer) viewer)
								.getTable().getParent().getLayout())
								.setColumnData(
										column,
										columnWeight[columnIndex] == 0 ? new ColumnPixelData(
												columnWidth[columnIndex],
												columnResizeable[columnIndex],
												columnAddTrim[columnIndex])
												: new ColumnWeightData(
														columnWeight[columnIndex],
														columnWidth[columnIndex],
														columnResizeable[columnIndex]));

						column.getParent().update();
						column.getParent().getParent()
								.layout(new Control[] { column.getParent() });

					} else {
						column.setWidth(tableHeaders[columnIndex].length() + 150);
					}

					column.setResizable(true);

				} else {

					int sum = 0;

					for (int i = 0; i < ((TableViewer) viewer).getTable()
							.getColumnCount(); i++) {

						TableColumn c = ((TableViewer) viewer).getTable()
								.getColumn(i);
						sum += (c.getWidth());
					}

					column.setWidth(0);
					column.setResizable(false);

					recalculatedColumnWidth();
				}
			}

			private void recalculatedColumnWidth() {

				Rectangle tableBounds = ((TableViewer) viewer).getTable()
						.getBounds();

				int sum = 0;
				TableColumn lastVisibleColumn = null;

				for (int i = 0; i < ((TableViewer) viewer).getTable()
						.getColumnCount(); i++) {

					TableColumn c = ((TableViewer) viewer).getTable()
							.getColumn(i);

					sum += (c.getWidth());

					if (c.getWidth() > 0 && c.getResizable()) {
						lastVisibleColumn = c;
					}
				}

				System.err.println("before: "
						+ lastVisibleColumn
						+ " >> "
						+ (lastVisibleColumn != null ? lastVisibleColumn
								.getWidth() : 0) + "; sum: "
						+ tableBounds.width + " >> " + sum);

				if (viewer instanceof TableViewer
						&& ((TableViewer) viewer).getTable().getParent()
								.getLayout() instanceof AbstractColumnLayout
						&& columnCount > 0 && columnIndex < columnCount) {

					int value = 0;

					if (lastVisibleColumn != null && sum < tableBounds.width) {

						value = tableBounds.width - sum
								+ lastVisibleColumn.getWidth();

						System.err.printf("value=%d (%d-%d+%d)%n", value,
								tableBounds.width, sum,
								lastVisibleColumn.getWidth());

						lastVisibleColumn.setWidth(value);

						((AbstractColumnLayout) ((TableViewer) viewer)
								.getTable().getParent().getLayout())
								.setColumnData(
										lastVisibleColumn,
										columnWeight[columnIndex] == 0 ? new ColumnPixelData(
												value,
												columnResizeable[columnIndex],
												columnAddTrim[columnIndex])
												: new ColumnWeightData(
														columnWeight[columnIndex],
														value,
														columnResizeable[columnIndex]));

						lastVisibleColumn.getParent().update();
						lastVisibleColumn
								.getParent()
								.getParent()
								.layout(new Control[] { lastVisibleColumn
										.getParent() });
					}

				} else {

					if (lastVisibleColumn != null && sum < tableBounds.width) {

						int i = 0;
						while (tableBounds.width > (tableBounds.width - sum + lastVisibleColumn
								.getWidth()) + i) {
							i++;
						}

						lastVisibleColumn.setWidth(tableBounds.width - sum
								+ lastVisibleColumn.getWidth() + i);
					}
				}

				sum = 0;
				lastVisibleColumn = null;
				for (int i = 0; i < ((TableViewer) viewer).getTable()
						.getColumnCount(); i++) {

					TableColumn c = ((TableViewer) viewer).getTable()
							.getColumn(i);

					sum += (c.getWidth());

					if (c.getWidth() > 0 && c.getResizable()) {
						lastVisibleColumn = c;
					}
				}

				System.err.println("after: "
						+ lastVisibleColumn
						+ " >> "
						+ (lastVisibleColumn != null ? lastVisibleColumn
								.getWidth() : 0) + "; sum: "
						+ tableBounds.width + " >> " + sum);
			}
		});
	}

	/*
	 * dynamic table definition
	 */

	protected int columnCount = 0;
	protected int[] columnWidth = {};
	protected int[] columnWeight = {};
	protected boolean[] columnResizeable = {};
	protected boolean[] columnAddTrim = {};

	/**
	 * init the dynamic table with the internal values
	 * 
	 * @param parentLayout
	 */
	public void initTableCoulmnWeights(Layout parentLayout) {
		initTableCoulmnWeights(parentLayout, columnCount, columnWidth,
				columnWeight, columnResizeable, columnAddTrim);
	}

	/**
	 * init the dynamic table with some external provided values
	 * 
	 * @param parentLayout
	 * @param columns
	 * @param columnWidth
	 * @param columnWeight
	 * @param columnResizeable
	 * @param columnAddTrim
	 */
	public void initTableCoulmnWeights(Layout parentLayout, int columns,
			int[] columnWidth, int[] columnWeight, boolean[] columnResizeable,
			boolean[] columnAddTrim) {

		if (parentLayout instanceof AbstractColumnLayout
				&& viewer instanceof TableViewer) {

			for (int i = 0; i < columns; i++) {

				if (((TableViewer) viewer).getTable().getColumn(i)
						.getResizable()
						&& ((TableViewer) viewer).getTable().getColumn(i)
								.getWidth() > 0) {

					((AbstractColumnLayout) parentLayout).setColumnData(
							((TableViewer) viewer).getTable().getColumn(i),
							columnWeight[i] == 0 ? new ColumnPixelData(
									columnWidth[i], columnResizeable[i],
									columnAddTrim[i]) : new ColumnWeightData(
									columnWeight[i], columnWidth[i],
									columnResizeable[i]));
				}
			}
		}
	}
}
