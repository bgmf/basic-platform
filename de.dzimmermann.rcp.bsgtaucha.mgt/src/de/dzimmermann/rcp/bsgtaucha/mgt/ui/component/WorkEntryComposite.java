package de.dzimmermann.rcp.bsgtaucha.mgt.ui.component;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
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

public class WorkEntryComposite extends Composite implements
		ElementUpdateSupport<RootType> {

	public static final String STATUSLINE_MESSAGE = WorkEntryComposite.class
			.getName() + ".statusline-message";
	public static final String ROOTMODEL_CHANGED_MESSAGE = WorkEntryComposite.class
			.getName() + ".root-model.changed";

	private static final String STATUSLINE_FORMAT = "%s: %.2fh / %s: %.2fh";

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
	private Button editButton;
	private Button addButton;
	private Button deleteButton;
	private Text commentText;

	private Button searchButton;
	private Button clearButton;
	private ComboViewer personComboViewer;
	private Combo personCombo;
	private ComboViewer workComboViewer;
	private Combo workCombo;
	private Button fromButton;
	private DateTime fromDateTime;
	private Label toLabel;
	private DateTime toDateTime;

	private boolean showPersonData;

	private boolean clearFilter = true;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public WorkEntryComposite(Composite parent, int style,
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

		ScrolledForm parentForm = formToolkit.createScrolledForm(this);
		formToolkit.paintBordersFor(parentForm);
		formToolkit.decorateFormHeading(parentForm.getForm());
		parentForm.setText("Geleistete Arbeiten verwalten");
		parentForm.getBody().setLayout(new GridLayout(1, false));

		Section overviewSection = formToolkit.createSection(
				parentForm.getBody(), Section.TITLE_BAR);
		overviewSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		formToolkit.paintBordersFor(overviewSection);
		overviewSection.setText("Übersicht");

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

		final InternalViewerComparator viewerComparator = new InternalViewerComparator();
		overviewTableViewer.setComparator(viewerComparator);

		final TableViewerColumn dateViewerColumn = new TableViewerColumn(
				overviewTableViewer, SWT.NONE);
		final TableColumn dateColumn = dateViewerColumn.getColumn();
		dateColumn.setWidth(100);
		dateColumn.setText("Datum");
		dateColumn.addSelectionListener(new SortAdapter(overviewTableViewer,
				viewerComparator, dateColumn, 0));

		final TableViewerColumn timeViewerColumn = new TableViewerColumn(
				overviewTableViewer, SWT.NONE);
		final TableColumn timeColumn = timeViewerColumn.getColumn();
		timeColumn.setWidth(55);
		timeColumn.setText("Zeit");

		final TableViewerColumn personViewerColumn = new TableViewerColumn(
				overviewTableViewer, SWT.NONE);
		final TableColumn personColumn = personViewerColumn.getColumn();
		personColumn.setWidth(250);
		personColumn.setText("Person");
		personColumn.addSelectionListener(new SortAdapter(overviewTableViewer,
				viewerComparator, personColumn, 2));

		final TableViewerColumn workViewerColumn = new TableViewerColumn(
				overviewTableViewer, SWT.NONE);
		final TableColumn workColumn = workViewerColumn.getColumn();
		workColumn.setWidth(250);
		workColumn.setText("Tätigkeit");
		workColumn.addSelectionListener(new SortAdapter(overviewTableViewer,
				viewerComparator, workColumn, 3));

		final TableViewerColumn typeViewerColumn = new TableViewerColumn(
				overviewTableViewer, SWT.NONE);
		final TableColumn typeColumn = typeViewerColumn.getColumn();
		typeColumn.setWidth(75);
		typeColumn.setText("Bandtyp");
		typeColumn.addSelectionListener(new SortAdapter(overviewTableViewer,
				viewerComparator, typeColumn, 4));

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

		editButton = formToolkit.createButton(overviewComposite, "Editieren",
				SWT.NONE);
		editButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 1, 1));
		editButton.setEnabled(false);

		addButton = formToolkit.createButton(overviewComposite, "Hinzufügen",
				SWT.NONE);
		addButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true,
				false, 1, 1));
		addButton.setEnabled(enableAdd);

		deleteButton = new Button(overviewComposite, SWT.NONE);
		deleteButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		formToolkit.adapt(deleteButton, true, true);
		deleteButton.setText("Entfernen");
		deleteButton.setEnabled(false);

		Label comentLabel = new Label(overviewComposite, SWT.NONE);
		comentLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false,
				false, 1, 1));
		formToolkit.adapt(comentLabel, true, true);
		comentLabel.setText("Bemerkungen");

		commentText = new Text(overviewComposite, SWT.WRAP | SWT.H_SCROLL
				| SWT.CANCEL | SWT.MULTI | SWT.BORDER);
		commentText.setEditable(false);
		GridData gd_commentText = new GridData(SWT.FILL, SWT.CENTER, true,
				false, 2, 1);
		gd_commentText.heightHint = 50;
		commentText.setLayoutData(gd_commentText);
		formToolkit.adapt(commentText, true, true);

		Section detailsSection = formToolkit.createSection(
				parentForm.getBody(), Section.TWISTIE | Section.TITLE_BAR);
		detailsSection.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		formToolkit.paintBordersFor(detailsSection);
		detailsSection.setText("Suchen");
		detailsSection.setExpanded(true);

		Composite composite = formToolkit.createComposite(detailsSection,
				SWT.NONE);
		formToolkit.paintBordersFor(composite);
		detailsSection.setClient(composite);
		composite.setLayout(new GridLayout(4, false));

		searchButton = formToolkit.createButton(composite, "Suchen!", SWT.NONE);
		searchButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		searchButton.setEnabled(enableAdd);

		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		clearButton = formToolkit.createButton(composite, "Zurücksetzen",
				SWT.NONE);
		clearButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		clearButton.setEnabled(enableAdd);

		Label personNameLabel = formToolkit.createLabel(composite, "Name",
				SWT.NONE);
		personNameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));

		personComboViewer = new ComboViewer(composite, SWT.NONE);
		personCombo = personComboViewer.getCombo();
		personCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 3, 1));
		formToolkit.paintBordersFor(personCombo);

		formToolkit.createLabel(composite, "Tätigkeit", SWT.NONE);

		workComboViewer = new ComboViewer(composite, SWT.NONE);
		workCombo = workComboViewer.getCombo();
		workCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				3, 1));
		formToolkit.paintBordersFor(workCombo);

		fromButton = formToolkit.createButton(composite, "von", SWT.CHECK);
		fromButton.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false,
				1, 1));
		// Label fromLabel = formToolkit.createLabel(composite, "von",
		// SWT.NONE);
		// fromLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false,
		// 1, 1));

		fromDateTime = new DateTime(composite, SWT.BORDER | SWT.DROP_DOWN
				| SWT.CALENDAR);
		formToolkit.adapt(fromDateTime);
		formToolkit.paintBordersFor(fromDateTime);

		toLabel = formToolkit.createLabel(composite, "bis", SWT.NONE);
		toLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false, 1,
				1));

		toDateTime = new DateTime(composite, SWT.BORDER | SWT.DROP_DOWN
				| SWT.CALENDAR);
		formToolkit.adapt(toDateTime);
		formToolkit.paintBordersFor(toDateTime);

		fromButton.setSelection(false);
		fromDateTime.setEnabled(fromButton.getSelection());
		toLabel.setEnabled(fromButton.getSelection());
		toDateTime.setEnabled(fromButton.getSelection());

		DefaultStructuredContentProvider provider = new DefaultStructuredContentProvider();

		labelProvider = new InternalLabelProvider(overviewTableViewer, root);

		InternalComboLabelProvider comboLabelProvider = new InternalComboLabelProvider();

		overviewTableViewer.setContentProvider(provider);
		personComboViewer.setContentProvider(provider);
		workComboViewer.setContentProvider(provider);

		overviewTableViewer.setLabelProvider(labelProvider);
		personComboViewer.setLabelProvider(comboLabelProvider);
		workComboViewer.setLabelProvider(comboLabelProvider);

		final InternalViewerFilter filter = new InternalViewerFilter();
		overviewTableViewer.addFilter(filter);

		updateTable();

		overviewTableViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						EntryType et = (EntryType) ((IStructuredSelection) event
								.getSelection()).getFirstElement();
						if (et != null) {
							commentText.setText(et.getComment() == null ? ""
									: et.getComment());
							editButton.setEnabled(true);
							deleteButton.setEnabled(true);
							fireChangeEvent(new PropertyChangeEvent(
									WorkEntryComposite.this,
									WorkEntryComposite.STATUSLINE_MESSAGE,
									null, getStatusLineContent(et)));
						} else {
							commentText.setText("");
							editButton.setEnabled(false);
							deleteButton.setEnabled(false);
							fireChangeEvent(new PropertyChangeEvent(
									WorkEntryComposite.this,
									WorkEntryComposite.STATUSLINE_MESSAGE,
									null, getStatusLineContent(et)));
						}
					}

					private String getStatusLineContent(EntryType et) {
						String result = "";
						if (et == null)
							return result;
						List<EntryType> visibleEntries = new ArrayList<EntryType>();
						if (WorkEntryComposite.this.root.getWorks() != null) {
							for (EntryType e : WorkEntryComposite.this.root
									.getWorks().getEntry()) {
								if (filter.select(overviewTableViewer, null, e))
									visibleEntries.add(e);
							}
						}
						double pHours = 0;
						double wHours = 0;
						for (EntryType tmp : visibleEntries) {
							if (tmp.getPersonId().equals(et.getPersonId())) {
								pHours += tmp.getTime();
							}
							if (tmp.getWorkId().equals(et.getWorkId())) {
								wHours += tmp.getTime();
							}
						}
						result = String.format(STATUSLINE_FORMAT,
								getPersonStringByID(et.getPersonId()), pHours,
								getWorkStringByID(et.getWorkId()), wHours);
						return result;
					}

					private String getPersonStringByID(String pid) {
						if (WorkEntryComposite.this.root.getPersons() == null)
							return "";
						for (PersonType pt : WorkEntryComposite.this.root
								.getPersons().getPerson()) {
							if (pt.getId().equals(pid))
								return (pt.getFirstname() != null ? pt
										.getFirstname() : "")
										+ " "
										+ (pt.getName() != null ? pt.getName()
												: "");
						}
						return "";
					}

					private String getWorkStringByID(String wid) {
						if (WorkEntryComposite.this.root.getWorks() == null)
							return "";
						for (WorkType wt : WorkEntryComposite.this.root
								.getWorks().getWork()) {
							if (wt.getId().equals(wid))
								return wt.getName() != null ? wt.getName() : "";
						}
						return "";
					}
				});

		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.widget == addButton) {
					EntryType et = (EntryType) ((IStructuredSelection) overviewTableViewer
							.getSelection()).getFirstElement();
					WorkEntryDialog dialog = new WorkEntryDialog(getShell(),
							WorkEntryComposite.this.root, et, false, false);
					if (WorkEntryDialog.OK == dialog.open()) {
						EntryType entry = dialog.getEntry();
						if (WorkEntryComposite.this.root.getWorks() == null)
							WorkEntryComposite.this.root
									.setWorks(WorkEntryComposite.this.modelFactory
											.createWorksType());
						WorkEntryComposite.this.root.getWorks().getEntry()
								.add(entry);
						// WorkEntryComposite.this.parentEditor.setDirty();
						fireChangeEvent(new PropertyChangeEvent(this,
								ROOTMODEL_CHANGED_MESSAGE, null,
								WorkEntryComposite.this.root));
						WorkEntryComposite.this.updateTable();
					}
				} else if (e.widget == editButton) {
					EntryType et = (EntryType) ((IStructuredSelection) overviewTableViewer
							.getSelection()).getFirstElement();
					WorkEntryDialog dialog = new WorkEntryDialog(getShell(),
							WorkEntryComposite.this.root, et, true, false);
					if (WorkEntryDialog.OK == dialog.open()) {
						EntryType entry = dialog.getEntry();
						et.setDate(entry.getDate());
						et.setPersonId(entry.getPersonId());
						et.setWorkId(entry.getWorkId());
						et.setTime(entry.getTime());
						et.setComment(entry.getComment());
						et.setBandAction(entry.getBandAction());
						// WorkEntryComposite.this.parentEditor.setDirty();
						fireChangeEvent(new PropertyChangeEvent(this,
								ROOTMODEL_CHANGED_MESSAGE, null,
								WorkEntryComposite.this.root));
						WorkEntryComposite.this.updateTable();
					}
				} else if (e.widget == deleteButton) {
					EntryType et = (EntryType) ((IStructuredSelection) overviewTableViewer
							.getSelection()).getFirstElement();
					if (et != null) {
						boolean confirm = MessageDialog
								.openConfirm(getShell(), "Löschen bestätigen",
										"Sind Sie sich sicher, dass sie den markierten Eintrag löschen wollen?");
						if (confirm) {
							WorkEntryComposite.this.root.getWorks().getEntry()
									.remove(et);
							// WorkEntryComposite.this.parentEditor.setDirty();
							fireChangeEvent(new PropertyChangeEvent(this,
									ROOTMODEL_CHANGED_MESSAGE, null,
									WorkEntryComposite.this.root));
							WorkEntryComposite.this.updateTable();
						}
					}
				} else if (e.widget == clearButton) {
					workComboViewer.setSelection(StructuredSelection.EMPTY);
					personComboViewer.setSelection(StructuredSelection.EMPTY);
					Calendar c = Calendar.getInstance();
					c.set(Calendar.HOUR_OF_DAY, 0);
					c.set(Calendar.MINUTE, 0);
					c.set(Calendar.SECOND, 0);
					c.set(Calendar.MILLISECOND, 0);
					fromDateTime.setDate(c.get(Calendar.YEAR),
							c.get(Calendar.MONTH), c.get(Calendar.DATE));
					toDateTime.setDate(c.get(Calendar.YEAR),
							c.get(Calendar.MONTH), c.get(Calendar.DATE));
					fromButton.setSelection(false);
					fromDateTime.setEnabled(fromButton.getSelection());
					toLabel.setEnabled(fromButton.getSelection());
					toDateTime.setEnabled(fromButton.getSelection());
					clearFilter = true;
					updateTable();
				} else if (e.widget == searchButton) {
					clearFilter = false;
					updateTable();
				} else if (e.widget == fromButton) {
					fromDateTime.setEnabled(fromButton.getSelection());
					toLabel.setEnabled(fromButton.getSelection());
					toDateTime.setEnabled(fromButton.getSelection());
				}
			}
		};
		addButton.addSelectionListener(selectionAdapter);
		editButton.addSelectionListener(selectionAdapter);
		deleteButton.addSelectionListener(selectionAdapter);
		clearButton.addSelectionListener(selectionAdapter);
		searchButton.addSelectionListener(selectionAdapter);
		fromButton.addSelectionListener(selectionAdapter);
	}

	@Override
	protected void checkSubclass() {
	}

	private void updateTable() {
		if (root != null && root.getWorks() != null) {
			labelProvider.setRoot(root);
			overviewTableViewer.setLabelProvider(labelProvider);
			overviewTableViewer.setInput(root.getWorks().getEntry());
		}
		if (root != null && root.getPersons() != null) {
			personComboViewer.setInput(root.getPersons().getPerson());
		}
		if (root != null && root.getWorks() != null) {
			workComboViewer.setInput(root.getWorks().getWork());
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

			if (clearFilter)
				return true;

			WorkType wt = (WorkType) ((IStructuredSelection) workComboViewer
					.getSelection()).getFirstElement();
			PersonType pt = (PersonType) ((IStructuredSelection) personComboViewer
					.getSelection()).getFirstElement();

			Calendar c = Calendar.getInstance();
			c.set(fromDateTime.getYear(), fromDateTime.getMonth(),
					fromDateTime.getDay());
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);

			Date from = c.getTime();

			c.set(toDateTime.getYear(), toDateTime.getMonth(),
					toDateTime.getDay());

			Date to = c.getTime();

			EntryType et = (EntryType) element;

			if (wt != null && !wt.getId().equals(et.getWorkId()))
				return false;
			if (pt != null && !pt.getId().equals(et.getPersonId()))
				return false;
			if (fromButton.getSelection() && et.getDate().before(from)
					|| et.getDate().after(to))
				return false;

			return true;
		}
	}

	private class InternalLabelProvider extends
			DefaultAbstractTableLabelProvider implements IColorProvider {

		private final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		private final Image removedBoth;
		private final Image removedPerson;
		private final Image removedWork;
		private RootType root;

		public InternalLabelProvider(Viewer viewer, RootType root) {
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
			EntryType et = (EntryType) element;
			if (et != null) {
				switch (columnIndex) {
				case 0:
					if (et.getDate() != null)
						return df.format(et.getDate());
					break;
				case 1:
					return "" + et.getTime();
				case 2:
					return getPersonStringByID(et.getPersonId());
				case 3:
					return getWorkStringByID(et.getWorkId());
				case 4:
					if (et.getBandAction() != null)
						return OpenDocumentCreator.getTypeValue(et
								.getBandAction().getBandType());
					break;
				case 5:
					if (et.getBandAction() != null)
						return "" + et.getBandAction().getAmount();
					break;
				case 6:
					return getTrackString(et.getBandAction());
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
	}

	private class InternalComboLabelProvider implements ILabelProvider,
			IColorProvider {

		@Override
		public void addListener(ILabelProviderListener listener) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
		}

		@Override
		public Color getForeground(Object element) {
			if (element instanceof WorkType) {
				if (((WorkType) element).isAffectBand())
					return Display.getCurrent().getSystemColor(
							SWT.COLOR_DARK_GREEN);
			}
			return null;
		}

		@Override
		public Color getBackground(Object element) {
			return null;
		}

		@Override
		public Image getImage(Object element) {
			return null;
		}

		@Override
		public String getText(Object element) {
			if (element instanceof WorkType) {
				return ((WorkType) element).getName();
			} else if (element instanceof PersonType) {
				PersonType pt = (PersonType) element;
				return (pt.getFirstname() != null ? pt.getFirstname() : "")
						+ " " + (pt.getName() != null ? pt.getName() : "");
			} else if (element instanceof BandTypes) {
				return OpenDocumentCreator.getTypeValue((BandTypes) element);
			}
			return "";
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
			case 2:
				comparator.setType(Type.PERSON);
				rc = comparator.compare(w1, w2);
				break;
			case 3:
				comparator.setType(Type.WORK);
				rc = comparator.compare(w1, w2);
				break;
			case 4:
				comparator.setType(Type.BAND_TYPE);
				rc = comparator.compare(w1, w2);
				break;
			case 1:
			case 5:
			case 6:
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
