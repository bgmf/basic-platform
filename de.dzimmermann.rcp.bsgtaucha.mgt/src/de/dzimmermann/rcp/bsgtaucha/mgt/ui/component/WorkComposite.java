package de.dzimmermann.rcp.bsgtaucha.mgt.ui.component;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import de.dzimmermann.rcp.basicplatform.model.BasicPlatformSessionModel;
import de.dzimmermann.rcp.basicplatform.ui.util.DefaultStructuredContentProvider;
import de.dzimmermann.rcp.basicplatform.util.DirtyEditorSupport;
import de.dzimmermann.rcp.basicplatform.util.ElementUpdateSupport;
import de.dzimmermann.rcp.basicplatform.util.PFSCoreIconProvider;
import de.dzimmermann.rcp.bsgtaucha.mgt.Activator;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.ModelComparator;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.ModelComparator.Type;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.ObjectFactory;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.RootType;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.WorkType;
import de.dzimmermann.rcp.bsgtaucha.mgt.ui.dialog.WorkDialog;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.BSGTauchaConstants;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.DesEncrypter;

public class WorkComposite extends Composite implements
		ElementUpdateSupport<RootType> {

	public static final String ROOTMODEL_CHANGED_MESSAGE = WorkComposite.class
			.getName() + ".root-model.changed";

	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	public void addListener(PropertyChangeListener listener) {
		pcs.addPropertyChangeListener(listener);
	}

	public void removeListener(PropertyChangeListener listener) {
		pcs.removePropertyChangeListener(listener);
	}

	private void fireChangeEvent(PropertyChangeEvent event) {
		if (pcs.getPropertyChangeListeners() == null
				|| pcs.getPropertyChangeListeners().length == 0)
			return;
		for (PropertyChangeListener l : pcs.getPropertyChangeListeners()) {
			l.propertyChange(event);
		}
	}

	private final FormToolkit formToolkit = new FormToolkit(
			Display.getDefault());

	// private final DirtyEditorSupport parentEditor;

	private ObjectFactory modelFactory = new ObjectFactory();
	private RootType root;

	private TableViewer overviewTableViewer;
	private Table overviewTable;
	private InternalLabelProvider labelProvider;

	private boolean showPersonData;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public WorkComposite(Composite parent, int style,
			DirtyEditorSupport parentEditor, RootType root) {

		super(parent, style);
		// this.parentEditor = parentEditor;
		this.root = root;

		String advPw = (String) BasicPlatformSessionModel
				.getInstance()
				.getPluginModels()
				.get(Activator.PLUGIN_ID
						+ BSGTauchaConstants.ADVANCED_PASSWORD_SUFFIX);
		showPersonData = true;
		if (root != null)
			if (root.getAdvancedPw() != null && !root.getAdvancedPw().isEmpty())
				if (!DesEncrypter
						.isEqualPassPhrase(root.getAdvancedPw(), advPw))
					showPersonData = false;

		setLayout(new FillLayout(SWT.HORIZONTAL));

		boolean enableAdd = true;
		if (root == null)
			enableAdd = false;

		ScrolledForm scrolledForm = formToolkit.createScrolledForm(this);
		formToolkit.paintBordersFor(scrolledForm);
		formToolkit.decorateFormHeading(scrolledForm.getForm());
		scrolledForm
				.setText("Arbeitsdefinitionen Anlegen, Ändern und Entfernen");
		scrolledForm.getBody().setLayout(new GridLayout(1, false));

		Section overviewSection = formToolkit.createSection(
				scrolledForm.getBody(), Section.TITLE_BAR);
		overviewSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		formToolkit.paintBordersFor(overviewSection);
		overviewSection.setText("Übersicht über die verschiedenen Arbeiten");

		Composite overviewComposite = formToolkit.createComposite(
				overviewSection, SWT.NONE);
		formToolkit.paintBordersFor(overviewComposite);
		overviewSection.setClient(overviewComposite);
		overviewComposite.setLayout(new GridLayout(3, false));

		overviewTableViewer = new TableViewer(overviewComposite,
				SWT.FULL_SELECTION);
		overviewTable = overviewTableViewer.getTable();
		overviewTable.setHeaderVisible(true);
		overviewTable.setLinesVisible(true);
		overviewTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 3, 1));
		formToolkit.paintBordersFor(overviewTable);

		final InternalViewerComparator comparator = new InternalViewerComparator();
		overviewTableViewer.setComparator(comparator);

		final TableViewerColumn bandViewerColumn = new TableViewerColumn(
				overviewTableViewer, SWT.NONE);
		final TableColumn bandColumn = bandViewerColumn.getColumn();
		bandColumn.setWidth(55);
		bandColumn.setText("Band?");
		bandColumn.addSelectionListener(new SortAdapter(overviewTableViewer,
				comparator, bandColumn, 0));

		final TableViewerColumn nameViewerColumn = new TableViewerColumn(
				overviewTableViewer, SWT.NONE);
		final TableColumn typeColumn = nameViewerColumn.getColumn();
		typeColumn.setWidth(200);
		typeColumn.setText("Typ");
		typeColumn.addSelectionListener(new SortAdapter(overviewTableViewer,
				comparator, typeColumn, 1));

		final TableViewerColumn descViewerColumn = new TableViewerColumn(
				overviewTableViewer, SWT.NONE);
		final TableColumn descColumn = descViewerColumn.getColumn();
		descColumn.setWidth(300);
		descColumn.setText("Kurzbeschreibung");

		final Button editButton = formToolkit.createButton(overviewComposite,
				"Editieren", SWT.NONE);
		editButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 1, 1));
		editButton.setEnabled(false);

		final Button addButton = formToolkit.createButton(overviewComposite,
				"Hinzufügen", SWT.NONE);
		addButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true,
				false, 1, 1));
		addButton.setEnabled(enableAdd);

		final Button deleteButton = new Button(overviewComposite, SWT.NONE);
		deleteButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		formToolkit.adapt(deleteButton, true, true);
		deleteButton.setText("Entfernen");
		deleteButton.setEnabled(false);

		DefaultStructuredContentProvider provider = new DefaultStructuredContentProvider();
		labelProvider = new InternalLabelProvider();

		overviewTableViewer.setContentProvider(provider);
		ColumnViewerToolTipSupport.enableFor(overviewTableViewer,
				ToolTip.NO_RECREATE);

		updateTable();

		overviewTableViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						WorkType wt = (WorkType) ((IStructuredSelection) event
								.getSelection()).getFirstElement();
						if (wt != null) {
							editButton.setEnabled(true);
							deleteButton.setEnabled(showPersonData
									&& !wt.isRemoved());
						} else {
							editButton.setEnabled(false);
							deleteButton.setEnabled(false);
						}
					}
				});

		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				WorkType work = (WorkType) ((IStructuredSelection) WorkComposite.this.overviewTableViewer
						.getSelection()).getFirstElement();
				if (e.widget == editButton) {
					if (work == null)
						return;
					WorkDialog dialog = new WorkDialog(getShell(), work);
					if (WorkDialog.OK == dialog.open()) {
						WorkType w = dialog.getwork();
						work.setAffectBand(w.isAffectBand());
						work.setName(w.getName());
						work.setDescription(w.getDescription());
						// WorkComposite.this.parentEditor.setDirty();
						fireChangeEvent(new PropertyChangeEvent(this,
								ROOTMODEL_CHANGED_MESSAGE, null,
								WorkComposite.this.root));
						WorkComposite.this.updateTable();
					}
				} else if (e.widget == addButton) {
					WorkDialog dialog = new WorkDialog(getShell(), null);
					if (WorkDialog.OK == dialog.open()) {
						WorkType w = dialog.getwork();
						w.setId("w" + System.currentTimeMillis());
						if (WorkComposite.this.root.getWorks() == null)
							WorkComposite.this.root.setWorks(modelFactory
									.createWorksType());
						WorkComposite.this.root.getWorks().getWork().add(w);
						// WorkComposite.this.parentEditor.setDirty();
						fireChangeEvent(new PropertyChangeEvent(this,
								ROOTMODEL_CHANGED_MESSAGE, null,
								WorkComposite.this.root));
						WorkComposite.this.updateTable();
					}
				} else if (e.widget == deleteButton) {
					if (work == null)
						return;
					boolean delete = MessageDialog.openConfirm(getShell(),
							"Löschen bestätigen",
							"Sind Sie sich sicher, dass sie die markierte "
									+ "Tätigkeit löschen wollen?");
					if (delete) {
						// WorkComposite.this.root.getWorks().getWork()
						// .remove(work);
						work.setRemoved(true);
						// WorkComposite.this.parentEditor.setDirty();
						fireChangeEvent(new PropertyChangeEvent(this,
								ROOTMODEL_CHANGED_MESSAGE, null,
								WorkComposite.this.root));
						WorkComposite.this.updateTable();
						deleteButton.setEnabled(showPersonData
								&& !work.isRemoved());
					}
				}
			}
		};
		editButton.addSelectionListener(selectionAdapter);
		addButton.addSelectionListener(selectionAdapter);
		deleteButton.addSelectionListener(selectionAdapter);
	}

	@Override
	protected void checkSubclass() {
	}

	private void updateTable() {
		if (root != null && root.getWorks() != null) {
			overviewTableViewer.setLabelProvider(labelProvider);
			overviewTableViewer.setInput(root.getWorks().getWork());
		}
	}

	@Override
	public void updateElement(RootType elementToUpdate) {

		if (elementToUpdate == null)
			return;

		this.root = elementToUpdate;

		String advPw = (String) BasicPlatformSessionModel
				.getInstance()
				.getPluginModels()
				.get(Activator.PLUGIN_ID
						+ BSGTauchaConstants.ADVANCED_PASSWORD_SUFFIX);
		showPersonData = true;
		if (root != null)
			if (root.getAdvancedPw() != null && !root.getAdvancedPw().isEmpty())
				if (!DesEncrypter
						.isEqualPassPhrase(root.getAdvancedPw(), advPw))
					showPersonData = false;

		updateTable();
	}

	@Override
	public void updateElements(Collection<RootType> elementsToUpdate) {
		if (elementsToUpdate == null || elementsToUpdate.isEmpty())
			return;
		updateElement(elementsToUpdate.iterator().next());
	}

	private static class InternalLabelProvider extends CellLabelProvider
			implements ITableLabelProvider, IColorProvider, IFontProvider {

		private final Image removed;

		public InternalLabelProvider() {
			removed = PFSCoreIconProvider.getImageByIconName(
					"fugue_cross-circle-frame.png", true);
		}

		@Override
		public Font getFont(Object element) {
			return null;
		}

		@Override
		public Color getForeground(Object element) {
			WorkType wt = (WorkType) element;
			if (wt.isAffectBand() != null && wt.isAffectBand())
				return Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
			return null;
		}

		@Override
		public Color getBackground(Object element) {
			return null;
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			currentColumn = columnIndex;
			if (element instanceof WorkType && columnIndex == 0) {
				if (((WorkType) element).isRemoved())
					return removed;

			}
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			currentColumn = columnIndex;
			WorkType wt = (WorkType) element;
			switch (columnIndex) {
			case 0:
				return wt.isAffectBand() != null && wt.isAffectBand() ? "ja"
						: "nein";
			case 1:
				return wt.getName();
			case 2:
				return wt.getDescription();
			}
			return null;
		}

		// tooltip for the current cell

		@Override
		public int getToolTipDisplayDelayTime(Object object) {
			return 2;
		}

		@Override
		public int getToolTipTimeDisplayed(Object object) {
			return 0;
		}

		@Override
		public Point getToolTipShift(Object object) {
			return super.getToolTipShift(object);
		}

		@Override
		public int getToolTipStyle(Object object) {
			return SWT.SHADOW_NONE;
		}

		@Override
		public String getToolTipText(Object element) {
			String description = ((WorkType) element).getDescription();
			return description != null && !description.isEmpty() ? description
					: ((WorkType) element).getName();
		}

		@Override
		public Image getToolTipImage(Object object) {
			return null;
		}

		@Override
		public Font getToolTipFont(Object object) {
			return super.getToolTipFont(object);
		}

		@Override
		public Color getToolTipBackgroundColor(Object object) {
			return super.getToolTipBackgroundColor(object);
		}

		@Override
		public Color getToolTipForegroundColor(Object object) {
			return super.getToolTipForegroundColor(object);
		}

		private int currentColumn;

		@Override
		public void update(ViewerCell cell) {
			cell.setBackground(getBackground(cell.getElement()));
			cell.setFont(getFont(cell.getElement()));
			cell.setForeground(getForeground(cell.getElement()));
			cell.setImage(getColumnImage(cell.getElement(), currentColumn));
			// XXX it is unused and creates errors in RAP
			// cell.setStyleRanges(null);
			cell.setText(getColumnText(cell.getElement(), currentColumn));
		}
	}

	private class InternalViewerComparator extends ViewerComparator {

		private static final int DESCENDING = 1;

		private int propertyIndex;
		private int direction = DESCENDING;

		private ModelComparator comparator = new ModelComparator(root,
				Type.DEFAULT);

		public InternalViewerComparator() {
			this.propertyIndex = 0;
			direction = DESCENDING;
		}

		public int getDirection() {
			return direction == 1 ? SWT.DOWN : SWT.UP;
		}

		public void setColumn(int column) {
			if (column == this.propertyIndex) {
				// Same column as last sort; toggle the direction
				direction = 1 - direction;
			} else {
				// New column; do an ascending sort
				this.propertyIndex = column;
				direction = DESCENDING;
			}
		}

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {

			WorkType w1 = (WorkType) e1;
			WorkType w2 = (WorkType) e2;

			int rc = 0;
			switch (propertyIndex) {
			case 0:
				comparator.setType(Type.WORK_AFFECT_BAND);
				rc = comparator.compare(w1, w2);
				break;
			case 1:
				comparator.setType(Type.WORK_NAME);
				rc = comparator.compare(w1, w2);
				break;
			default:
				rc = 0;
			}
			// If descending order, flip the direction
			if (direction == DESCENDING) {
				rc = -rc;
			}
			return rc;
		}
	}

	private class SortAdapter extends SelectionAdapter {

		private TableViewer viewer;
		private InternalViewerComparator comparator;

		private TableColumn column;
		private int columnIndex;

		public SortAdapter(TableViewer viewer,
				InternalViewerComparator comparator, TableColumn column,
				int columnIndex) {
			this.viewer = viewer;
			this.comparator = comparator;
			this.column = column;
			this.columnIndex = columnIndex;
		}

		public void widgetSelected(SelectionEvent e) {
			comparator.setColumn(columnIndex);
			int dir = comparator.getDirection();
			viewer.getTable().setSortDirection(dir);
			viewer.getTable().setSortColumn(column);
			viewer.refresh();
		};
	}
}
