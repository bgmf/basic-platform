package de.dzimmermann.rcp.bsgtaucha.mgt.ui.component;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.SimpleDateFormat;
import java.util.Collection;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
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
import de.dzimmermann.rcp.basicplatform.ui.util.DefaultAbstractTableLabelProvider;
import de.dzimmermann.rcp.basicplatform.ui.util.DefaultStructuredContentProvider;
import de.dzimmermann.rcp.basicplatform.util.DirtyEditorSupport;
import de.dzimmermann.rcp.basicplatform.util.ElementUpdateSupport;
import de.dzimmermann.rcp.basicplatform.util.PFSCoreIconProvider;
import de.dzimmermann.rcp.bsgtaucha.mgt.Activator;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.BandActionType;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.BandTypes;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.EntryType;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.ModelComparator;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.ModelComparator.Type;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.ObjectFactory;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.PersonType;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.RootType;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.WorkType;
import de.dzimmermann.rcp.bsgtaucha.mgt.ui.dialog.WorkEntryDialog;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.BSGTauchaConstants;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.DesEncrypter;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.OpenDocumentCreator;

public class BandComposite extends Composite implements
		ElementUpdateSupport<RootType> {

	public static final String ROOTMODEL_CHANGED_MESSAGE = BandComposite.class
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
	private InternalLabelProvider overviewLabelProvider;
	private TableViewer currentStateTableViewer;
	private Table currentStateTable;
	private InternalLabelProvider currentStateLabelProvider;

	private boolean showPersonData;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public BandComposite(Composite parent, int style,
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

		boolean enableAdd = true;
		if (root == null)
			enableAdd = false;

		setLayout(new FillLayout(SWT.HORIZONTAL));

		ScrolledForm scrolledForm = formToolkit.createScrolledForm(this);
		formToolkit.paintBordersFor(scrolledForm);
		formToolkit.decorateFormHeading(scrolledForm.getForm());
		scrolledForm
				.setText("Ein- und Ausgänge der Bänder verwalten && aktuelle Bandbestände");
		scrolledForm.getBody().setLayout(new GridLayout(1, false));

		Section overviewSection = formToolkit.createSection(
				scrolledForm.getBody(), Section.TITLE_BAR);
		overviewSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		formToolkit.paintBordersFor(overviewSection);
		overviewSection.setText("Übersicht über die Ein- && Ausgänge");

		Composite overviewComposite = formToolkit.createComposite(
				overviewSection, SWT.NONE);
		formToolkit.paintBordersFor(overviewComposite);
		overviewSection.setClient(overviewComposite);
		overviewComposite.setLayout(new GridLayout(2, false));

		overviewTableViewer = new TableViewer(overviewComposite,
				SWT.FULL_SELECTION);
		overviewTable = overviewTableViewer.getTable();
		overviewTable.setHeaderVisible(true);
		overviewTable.setLinesVisible(true);
		overviewTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 2, 1));
		formToolkit.paintBordersFor(overviewTable);

		final InternalViewerComparator viewerComparator = new InternalViewerComparator();
		overviewTableViewer.setComparator(viewerComparator);

		final TableViewerColumn dateViewerColumn = new TableViewerColumn(
				overviewTableViewer, SWT.NONE);
		final TableColumn dateColumn = dateViewerColumn.getColumn();
		dateColumn.setWidth(100);
		dateColumn.setText("Datum");
		dateColumn.addSelectionListener(new SortAdapter(overviewTableViewer,
				viewerComparator, dateColumn, 0));

		final TableViewerColumn typeViewerColumn = new TableViewerColumn(
				overviewTableViewer, SWT.NONE);
		final TableColumn typeColumn = typeViewerColumn.getColumn();
		typeColumn.setWidth(75);
		typeColumn.setText("Bandtyp");
		typeColumn.addSelectionListener(new SortAdapter(overviewTableViewer,
				viewerComparator, typeColumn, 1));

		final TableViewerColumn countViewerColumn = new TableViewerColumn(
				overviewTableViewer, SWT.NONE);
		final TableColumn countColumn = countViewerColumn.getColumn();
		countColumn.setWidth(55);
		countColumn.setText("Anzahl");

		final TableViewerColumn trackViewerColumn = new TableViewerColumn(
				overviewTableViewer, SWT.NONE);
		final TableColumn trackColumn = trackViewerColumn.getColumn();
		trackColumn.setWidth(100);
		trackColumn.setText("Bahn(en)");

		final TableViewerColumn personViewerColumn = new TableViewerColumn(
				overviewTableViewer, SWT.NONE);
		final TableColumn personColumn = personViewerColumn.getColumn();
		personColumn.setWidth(250);
		personColumn.setText("Person");
		personColumn.addSelectionListener(new SortAdapter(overviewTableViewer,
				viewerComparator, personColumn, 4));

		final TableViewerColumn workViewerColumn = new TableViewerColumn(
				overviewTableViewer, SWT.NONE);
		final TableColumn workColumn = workViewerColumn.getColumn();
		workColumn.setWidth(250);
		workColumn.setText("Tätigkeit");
		workColumn.addSelectionListener(new SortAdapter(overviewTableViewer,
				viewerComparator, workColumn, 5));

		final Button addButton = formToolkit.createButton(overviewComposite,
				"Eingang / Ausgang", SWT.NONE);
		addButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true,
				false, 1, 1));
		addButton
				.setToolTipText("Sollten Sie bestehende Ein- oder Ausgänge modifizieren wollen, nutzen Sie die Maske der Übersicht über 'Geleisteten Arbeiten'.");
		addButton.setEnabled(enableAdd);

		final Button deleteButton = new Button(overviewComposite, SWT.NONE);
		deleteButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		formToolkit.adapt(deleteButton, true, true);
		deleteButton.setText("Entfernen");
		deleteButton
				.setToolTipText("Gegenwärtig ist das Entfernen von diesen Einträgen über diese Eingabemaske nicht erlaubt.\n"
						+ "Verwenden Sie gegebenenfalls die Maske der Übersicht über 'Geleisteten Arbeiten'.");
		deleteButton.setEnabled(false);

		Section currentStateSection = formToolkit.createSection(
				scrolledForm.getBody(), Section.TWISTIE | Section.TITLE_BAR);
		currentStateSection.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM,
				true, false, 1, 1));
		formToolkit.paintBordersFor(currentStateSection);
		currentStateSection.setText("gegenwärtige Bestände");
		currentStateSection.setExpanded(true);

		Composite currentStateComposite = formToolkit.createComposite(
				currentStateSection, SWT.NONE);
		formToolkit.paintBordersFor(currentStateComposite);
		currentStateSection.setClient(currentStateComposite);
		currentStateComposite.setLayout(new GridLayout(1, false));

		currentStateTableViewer = new TableViewer(currentStateComposite,
				SWT.FULL_SELECTION);
		currentStateTable = currentStateTableViewer.getTable();
		GridData gd_currentStateTable = new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1);
		gd_currentStateTable.minimumHeight = 75;
		currentStateTable.setLayoutData(gd_currentStateTable);
		formToolkit.paintBordersFor(currentStateTable);

		final TableViewerColumn typeViewerColumn2 = new TableViewerColumn(
				currentStateTableViewer, SWT.NONE);
		final TableColumn typeColumn2 = typeViewerColumn2.getColumn();
		typeColumn2.setWidth(75);
		typeColumn2.setText("Typ");

		final TableViewerColumn countViewerColumn2 = new TableViewerColumn(
				currentStateTableViewer, SWT.NONE);
		final TableColumn countColumn2 = countViewerColumn2.getColumn();
		countColumn2.setWidth(55);
		countColumn2.setText("Anzahl");

		DefaultStructuredContentProvider provider = new DefaultStructuredContentProvider();

		overviewTableViewer.setContentProvider(provider);
		currentStateTableViewer.setContentProvider(provider);

		InternalViewerFilter filter = new InternalViewerFilter();
		overviewTableViewer.addFilter(filter);

		overviewLabelProvider = new InternalLabelProvider(overviewTableViewer,
				root);
		currentStateLabelProvider = new InternalLabelProvider(
				currentStateTableViewer, root);

		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.widget == addButton) {
					EntryType et = (EntryType) ((IStructuredSelection) overviewTableViewer
							.getSelection()).getFirstElement();
					WorkEntryDialog dialog = new WorkEntryDialog(getShell(),
							BandComposite.this.root, et, false, true);
					if (WorkEntryDialog.OK == dialog.open()) {
						EntryType entry = dialog.getEntry();
						if (BandComposite.this.root.getWorks() == null)
							BandComposite.this.root
									.setWorks(BandComposite.this.modelFactory
											.createWorksType());
						BandComposite.this.root.getWorks().getEntry()
								.add(entry);
						// BandComposite.this.parentEditor.setDirty();
						fireChangeEvent(new PropertyChangeEvent(this,
								ROOTMODEL_CHANGED_MESSAGE, null,
								BandComposite.this.root));
						BandComposite.this.updateTable();
					}
				}
				// else if (e.widget == editButton) {
				// EntryType et = (EntryType) ((IStructuredSelection)
				// overviewTableViewer
				// .getSelection()).getFirstElement();
				// WorkEntryDialog dialog = new WorkEntryDialog(getShell(),
				// BandComposite.this.root, et, true);
				// if (WorkEntryDialog.OK == dialog.open()) {
				// EntryType entry = dialog.getEntry();
				// // TODO update content
				// // BandComposite.this.parentEditor.setDirty();
				// fireChangeEvent(new PropertyChangeEvent(this,
				// ROOTMODEL_CHANGED_MESSAGE, null, BandComposite.this.root));
				// BandComposite.this.updateTable();
				// }
				// }
				// else if (e.widget == deleteButton) {
				// EntryType et = (EntryType) ((IStructuredSelection)
				// overviewTableViewer
				// .getSelection()).getFirstElement();
				// if (et != null) {
				// boolean confirm = MessageDialog
				// .openConfirm(getShell(), "Löschen bestätigen",
				// "Sind Sie sich sicher, dass sie den markierten Eintrag löschen wollen?");
				// if (confirm) {
				// BandComposite.this.root.getWorks().getEntry()
				// .remove(et);
				// // BandComposite.this.parentEditor.setDirty();
				// fireChangeEvent(new PropertyChangeEvent(this,
				// ROOTMODEL_CHANGED_MESSAGE, null, BandComposite.this.root));
				// BandComposite.this.updateTable();
				// }
				// }
				// }
			}
		};

		addButton.addSelectionListener(selectionAdapter);

		updateTable();
	}

	@Override
	protected void checkSubclass() {
	}

	private void updateTable() {
		if (root != null && root.getWorks() != null) {
			overviewLabelProvider.setRoot(root);
			overviewTableViewer.setLabelProvider(overviewLabelProvider);
			overviewTableViewer.setInput(root.getWorks().getEntry());
			currentStateTableViewer.setLabelProvider(currentStateLabelProvider);
			currentStateTableViewer.setInput(BandTypes.values());
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

	private class InternalViewerFilter extends ViewerFilter {

		@Override
		public boolean select(Viewer viewer, Object parentElement,
				Object element) {
			if (element instanceof EntryType) {
				if (((EntryType) element).getBandAction() != null)
					return true;
			} else if (element instanceof BandTypes) {
				return true;
			}
			return false;
		}

	}

	private class InternalLabelProvider extends
			DefaultAbstractTableLabelProvider implements IColorProvider {

		private final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		private final Image removedBoth;
		private final Image removedPerson;
		private final Image removedWork;
		private RootType root;

		public InternalLabelProvider(final TableViewer viewer,
				final RootType root) {
			super(viewer);
			removedBoth = PFSCoreIconProvider.getImageByIconName(
					"fugue_cross.png", true);
			removedPerson = PFSCoreIconProvider.getImageByIconName(
					"fugue_cross-white.png", true);
			removedWork = PFSCoreIconProvider.getImageByIconName(
					"fugue_cross-circle-frame.png", true);
			this.root = root;
		}

		public void setRoot(RootType root) {
			this.root = root;
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			if (element instanceof EntryType && columnIndex == 0) {
				EntryType et = (EntryType) element;
				boolean rp = isPersonRemoved(et.getPersonId());
				boolean rw = isWorkRemoved(et.getWorkId());
				if (rp && rw)
					return removedBoth;
				else if (rp)
					return removedPerson;
				else if (rw)
					return removedWork;
			}
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof EntryType) {
				EntryType et = (EntryType) element;
				switch (columnIndex) {
				case 0:
					return et.getDate() != null ? df.format(et.getDate()) : "";
				case 1:
					return et.getBandAction() != null
							&& et.getBandAction().getBandType() != null ? OpenDocumentCreator
							.getTypeValue(et.getBandAction().getBandType())
							: "";
				case 2:
					return et.getBandAction() != null
							&& et.getBandAction().getBandType() != null ? et
							.getBandAction().getAmount().toString() : "0";
				case 3:
					return getTrackString(et.getBandAction());
				case 4:
					return getPersonStringByID(et.getPersonId());
				case 5:
					return getWorkStringByID(et.getWorkId());
				}
			} else if (element instanceof BandTypes) {
				BandTypes bt = (BandTypes) element;
				switch (columnIndex) {
				case 0:
					return OpenDocumentCreator.getTypeValue(bt);
				case 1:
					return getAmoutByType(bt).toString();
				}
			}
			return "";
		}

		private String getPersonStringByID(String pid) {
			if (root.getPersons() == null)
				return "";
			for (PersonType pt : root.getPersons().getPerson()) {
				if (pt.getId().equals(pid))
					return (pt.getFirstname() != null ? pt.getFirstname() : "")
							+ " " + (pt.getName() != null ? pt.getName() : "");
			}
			return "";
		}

		private boolean isPersonRemoved(String pid) {
			if (root.getPersons() == null)
				return false;
			for (PersonType pt : root.getPersons().getPerson()) {
				if (pt.getId().equals(pid))
					if (pt.isRemoved())
						return true;
					else
						return false;
			}
			return false;
		}

		private String getWorkStringByID(String wid) {
			if (root.getWorks() == null)
				return "";
			for (WorkType wt : root.getWorks().getWork()) {
				if (wt.getId().equals(wid))
					return wt.getName() != null ? wt.getName() : "";
			}
			return "";
		}

		private boolean isWorkRemoved(String wid) {
			if (root.getWorks() == null)
				return false;
			for (WorkType wt : root.getWorks().getWork()) {
				if (wt.getId().equals(wid))
					if (wt.isRemoved())
						return true;
					else
						return false;
			}
			return false;
		}

		private Integer getAmoutByType(BandTypes bt) {
			Integer i = 0;
			if (root.getWorks() == null)
				return i;
			for (EntryType et : root.getWorks().getEntry()) {
				if (et.getBandAction() == null)
					continue;
				if (!bt.equals(et.getBandAction().getBandType()))
					continue;
				i += et.getBandAction().getAmount();
			}
			return i;
		}

		private String getTrackString(BandActionType ba) {
			StringBuilder sb = new StringBuilder();
			if (ba != null && !ba.getTrack().isEmpty()) {
				int i = 0;
				for (Integer value : ba.getTrack()) {
					sb.append(value);
					if ((i + 1) < ba.getTrack().size())
						sb.append(", ");
					i++;
				}
			}
			return sb.toString();
		}

		@Override
		public Color getBackground(Object element) {
			return null;
		}

		@Override
		public Color getForeground(Object element) {
			if (element instanceof EntryType) {
				if (((EntryType) element).getBandAction() != null) {
					if (((EntryType) element).getBandAction().getAmount() > 0)
						return Display.getCurrent().getSystemColor(
								SWT.COLOR_DARK_GREEN);
					else
						return Display.getCurrent().getSystemColor(
								SWT.COLOR_DARK_RED);
				}
			}
			return null;
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

			EntryType w1 = (EntryType) e1;
			EntryType w2 = (EntryType) e2;

			int rc = 0;
			switch (propertyIndex) {
			case 0:
				comparator.setType(Type.DATE);
				rc = comparator.compare(w1, w2);
				break;
			case 1:
				comparator.setType(Type.BAND_TYPE);
				rc = comparator.compare(w1, w2);
				break;
			case 4:
				comparator.setType(Type.PERSON);
				rc = comparator.compare(w1, w2);
				break;
			case 5:
				comparator.setType(Type.WORK);
				rc = comparator.compare(w1, w2);
				break;
			case 2:
			case 3:
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
