package de.dzimmermann.rcp.bsgtaucha.mgt.ui.component;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.SimpleDateFormat;
import java.util.Collection;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
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
import de.dzimmermann.rcp.basicplatform.ui.util.Logger;
import de.dzimmermann.rcp.basicplatform.util.DirtyEditorSupport;
import de.dzimmermann.rcp.basicplatform.util.ElementUpdateSupport;
import de.dzimmermann.rcp.basicplatform.util.PFSCoreIconProvider;
import de.dzimmermann.rcp.bsgtaucha.mgt.Activator;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.ModelComparator;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.ModelComparator.Type;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.ObjectFactory;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.PersonType;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.RootType;
import de.dzimmermann.rcp.bsgtaucha.mgt.ui.dialog.PersonDialog;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.BSGTauchaConstants;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.DesEncrypter;

public class PersonComposite extends Composite implements
		ElementUpdateSupport<RootType> {

	public static final String ROOTMODEL_CHANGED_MESSAGE = PersonComposite.class
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

	private Text telephoneText;
	private Text mobileText;
	private Text faxText;
	private Text emailText;

	private ITableLabelProvider labelProvider;

	private boolean showPersonData;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public PersonComposite(Composite parent, int style,
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
		// showPersonData = root != null && root.getAdvancedPw() != null
		// && !root.getAdvancedPw().isEmpty() && advPw != null
		// && DesEncrypter.isEqualPassPhrase(root.getAdvancedPw(), advPw);

		setLayout(new FillLayout(SWT.HORIZONTAL));

		ScrolledForm scrolledForm = formToolkit.createScrolledForm(this);
		formToolkit.paintBordersFor(scrolledForm);
		formToolkit.decorateFormHeading(scrolledForm.getForm());
		scrolledForm.setText("Personen Anlegen, Anpassen und Entfernen");
		scrolledForm.getBody().setLayout(new GridLayout(1, false));

		Section overviewSection = formToolkit.createSection(
				scrolledForm.getBody(), Section.TITLE_BAR);
		overviewSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		formToolkit.paintBordersFor(overviewSection);
		overviewSection.setText("Personenübersicht");

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

		final TableViewerColumn nameViewerColumn = new TableViewerColumn(
				overviewTableViewer, SWT.NONE);
		final TableColumn nameColumn = nameViewerColumn.getColumn();
		nameColumn.setWidth(100);
		nameColumn.setText("Name");
		nameColumn.addSelectionListener(new SortAdapter(overviewTableViewer,
				viewerComparator, nameColumn, 0));

		final TableViewerColumn firstnameViewerColumn = new TableViewerColumn(
				overviewTableViewer, SWT.NONE);
		final TableColumn firstnameColumn = firstnameViewerColumn.getColumn();
		firstnameColumn.setWidth(100);
		firstnameColumn.setText("Vorname");
		firstnameColumn.addSelectionListener(new SortAdapter(
				overviewTableViewer, viewerComparator, firstnameColumn, 1));

		if (showPersonData) {
			final TableViewerColumn streetViewerColumn = new TableViewerColumn(
					overviewTableViewer, SWT.NONE);
			final TableColumn streetColumn = streetViewerColumn.getColumn();
			streetColumn.setWidth(100);
			streetColumn.setText("Strasse");
			streetColumn.addSelectionListener(new SortAdapter(
					overviewTableViewer, viewerComparator, streetColumn, 2));

			final TableViewerColumn postalViewerColumn = new TableViewerColumn(
					overviewTableViewer, SWT.NONE);
			final TableColumn postalColumn = postalViewerColumn.getColumn();
			postalColumn.setWidth(100);
			postalColumn.setText("PLZ");
			postalColumn.addSelectionListener(new SortAdapter(
					overviewTableViewer, viewerComparator, postalColumn, 3));

			final TableViewerColumn cityViewerColumn = new TableViewerColumn(
					overviewTableViewer, SWT.NONE);
			final TableColumn cityColumn = cityViewerColumn.getColumn();
			cityColumn.setWidth(100);
			cityColumn.setText("Ort");
			cityColumn.addSelectionListener(new SortAdapter(
					overviewTableViewer, viewerComparator, cityColumn, 4));

			final TableViewerColumn birthdayViewerColumn = new TableViewerColumn(
					overviewTableViewer, SWT.NONE);
			final TableColumn birthdayColumn = birthdayViewerColumn.getColumn();
			birthdayColumn.setWidth(100);
			birthdayColumn.setText("Geburtstag");
			birthdayColumn.addSelectionListener(new SortAdapter(
					overviewTableViewer, viewerComparator, birthdayColumn, 5));
		}

		final Button editButton = formToolkit.createButton(overviewComposite,
				"Editieren", SWT.NONE);
		editButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true,
				false, 1, 1));
		editButton.setEnabled(false);

		final Button addButton = formToolkit.createButton(overviewComposite,
				"Hinzufügen", SWT.NONE);
		addButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true,
				false, 1, 1));

		final Button deleteButton = new Button(overviewComposite, SWT.NONE);
		deleteButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		formToolkit.adapt(deleteButton, true, true);
		deleteButton.setText("Entfernen");
		deleteButton.setEnabled(false);

		// editButton.setEnabled(showPersonData);
		addButton.setEnabled(showPersonData);
		// deleteButton.setEnabled(showPersonData);

		Section detailsSection = formToolkit.createSection(scrolledForm
				.getBody(),
				showPersonData ? (Section.TWISTIE | Section.TITLE_BAR)
						: Section.TITLE_BAR);
		detailsSection.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		formToolkit.paintBordersFor(detailsSection);
		detailsSection.setText("Detailierte personenbezogene Informationen");
		detailsSection.setExpanded(showPersonData);

		Composite composite = formToolkit.createComposite(detailsSection,
				SWT.NONE);
		formToolkit.paintBordersFor(composite);
		detailsSection.setClient(composite);
		composite.setLayout(new GridLayout(2, false));

		Label activeMemberLabel = formToolkit.createLabel(composite,
				"Aktiver Schütze", SWT.NONE);
		activeMemberLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));

		final Label isActiveMemberLabel = formToolkit.createLabel(composite,
				"", SWT.NONE);
		isActiveMemberLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		Label telephoneLabel = formToolkit.createLabel(composite,
				"Telefonnummer", SWT.NONE);
		telephoneLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));

		telephoneText = formToolkit.createText(composite, "", SWT.NONE
				| SWT.BORDER);
		telephoneText.setEditable(false);
		telephoneText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label mobileLabel = formToolkit.createLabel(composite, "Handy",
				SWT.NONE);
		mobileLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));

		mobileText = formToolkit.createText(composite, "New Text", SWT.NONE
				| SWT.BORDER);
		mobileText.setEditable(false);
		mobileText.setText("");
		mobileText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Label faxLabel = formToolkit.createLabel(composite, "Fax-Nummer",
				SWT.NONE);
		faxLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));

		faxText = formToolkit.createText(composite, "New Text", SWT.NONE
				| SWT.BORDER);
		faxText.setEditable(false);
		faxText.setText("");
		faxText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		Label emailLabel = formToolkit.createLabel(composite, "E-Mail",
				SWT.NONE);
		emailLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));

		emailText = formToolkit.createText(composite, "New Text", SWT.NONE
				| SWT.BORDER);
		emailText.setEditable(false);
		emailText.setText("");
		emailText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));

		overviewTableViewer
				.setContentProvider(new DefaultStructuredContentProvider());

		labelProvider = new InternalLabelProvider(overviewTableViewer);
		updateTable();

		overviewTableViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						PersonType p = (PersonType) ((IStructuredSelection) event
								.getSelection()).getFirstElement();
						if (p != null) {
							isActiveMemberLabel
									.setText(p.isActiveMember() != null ? p
											.isActiveMember() ? "ja" : "nein"
											: "nein");
							telephoneText.setText(p.getPhone() != null ? p
									.getPhone() : "");
							mobileText.setText(p.getMobile() != null ? p
									.getMobile() : "");
							faxText.setText(p.getFax() != null ? p.getFax()
									: "");
							emailText.setText(p.getEmail() != null ? p
									.getEmail() : "");
							editButton.setEnabled(showPersonData);
							deleteButton.setEnabled(showPersonData
									&& !p.isRemoved());
						} else {
							isActiveMemberLabel.setText("");
							telephoneText.setText("");
							mobileText.setText("");
							faxText.setText("");
							emailText.setText("");
							editButton.setEnabled(false);
							deleteButton.setEnabled(false);
						}
					}
				});

		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				PersonType person = (PersonType) ((IStructuredSelection) PersonComposite.this.overviewTableViewer
						.getSelection()).getFirstElement();
				if (e.widget == addButton) {
					PersonDialog dialog = new PersonDialog(getShell(), null);
					int result = dialog.open();
					if (PersonDialog.OK == result) {
						PersonType p = dialog.getPerson();
						p.setId("p" + System.currentTimeMillis());
						if (PersonComposite.this.root.getPersons() == null)
							PersonComposite.this.root.setPersons(modelFactory
									.createPersonsType());
						PersonComposite.this.root.getPersons().getPerson()
								.add(p);
						// PersonComposite.this.parentEditor.setDirty();
						fireChangeEvent(new PropertyChangeEvent(this,
								ROOTMODEL_CHANGED_MESSAGE, null,
								PersonComposite.this.root));
						PersonComposite.this.updateTable();
					}
				} else if (e.widget == editButton) {
					if (person == null)
						return;
					PersonDialog dialog = new PersonDialog(getShell(), person);
					if (PersonDialog.OK == dialog.open()) {
						PersonType p = dialog.getPerson();
						person.setName(p.getName());
						person.setFirstname(p.getFirstname());
						person.setStreet(p.getStreet());
						person.setPostalcode(p.getPostalcode());
						person.setCity(p.getCity());
						person.setBirthday(p.getBirthday());
						person.setActiveMember(p.isActiveMember());
						person.setPhone(p.getPhone());
						person.setMobile(p.getMobile());
						person.setFax(p.getFax());
						person.setEmail(p.getEmail());
						// PersonComposite.this.parentEditor.setDirty();
						fireChangeEvent(new PropertyChangeEvent(this,
								ROOTMODEL_CHANGED_MESSAGE, null,
								PersonComposite.this.root));
						PersonComposite.this.updateTable();
					}
				} else if (e.widget == deleteButton) {
					if (person == null)
						return;
					boolean delete = MessageDialog.openConfirm(getShell(),
							"Löschen bestätigen",
							"Sind Sie sich sicher, dass sie die markierte "
									+ "Person löschen wollen?");
					if (delete) {
						// PersonComposite.this.root.getPersons().getPerson()
						// .remove(person);
						person.setRemoved(true);
						// PersonComposite.this.parentEditor.setDirty();
						fireChangeEvent(new PropertyChangeEvent(this,
								ROOTMODEL_CHANGED_MESSAGE, null,
								PersonComposite.this.root));
						PersonComposite.this.updateTable();
						deleteButton.setEnabled(showPersonData
								&& !person.isRemoved());
					}
				}
			}
		};
		addButton.addSelectionListener(selectionAdapter);
		editButton.addSelectionListener(selectionAdapter);
		deleteButton.addSelectionListener(selectionAdapter);

		if (root == null) {
			addButton.setEnabled(false);
			editButton.setEnabled(false);
			deleteButton.setEnabled(false);
		}
	}

	@Override
	protected void checkSubclass() {
	}

	private void updateTable() {
		if (root != null && root.getPersons() != null) {
			overviewTableViewer.setLabelProvider(labelProvider);
			overviewTableViewer.setInput(root.getPersons().getPerson());
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

	private class InternalLabelProvider extends
			DefaultAbstractTableLabelProvider {

		private final SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		private final Image removed;

		public InternalLabelProvider(Viewer viewer) {
			super(viewer);
			removed = PFSCoreIconProvider.getImageByIconName(
					"fugue_cross-white.png", true);
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			if (element instanceof PersonType && columnIndex == 0) {
				if (((PersonType) element).isRemoved())
					return removed;

			}
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof PersonType) {
				PersonType p = (PersonType) element;
				switch (columnIndex) {
				case 0:
					return p.getName() != null ? p.getName() : "";
				case 1:
					return p.getFirstname() != null ? p.getFirstname() : "";
				case 2:
					return p.getStreet() != null ? p.getStreet() : "";
				case 3:
					return p.getPostalcode() != null ? p.getPostalcode() : "";
				case 4:
					return p.getCity() != null ? p.getCity() : "";
				case 5:
					return p.getBirthday() != null ? df.format(p.getBirthday())
							: "";
				}
			}
			return element.toString();
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

			PersonType p1 = (PersonType) e1;
			PersonType p2 = (PersonType) e2;

			int rc = 0;
			switch (propertyIndex) {
			case 0:
				comparator.setType(Type.PERSON_NAME);
				rc = comparator.compare(p1, p2);
				break;
			case 1:
				comparator.setType(Type.PERSON_FIRSTNAME);
				rc = comparator.compare(p1, p2);
				break;
			case 2:
				comparator.setType(Type.PERSON_STREET);
				rc = comparator.compare(p1, p2);
				break;
			case 3:
				comparator.setType(Type.PERSON_PLZ);
				rc = comparator.compare(p1, p2);
				break;
			case 4:
				comparator.setType(Type.PERSON_CITY);
				rc = comparator.compare(p1, p2);
				break;
			case 5:
				Logger.logInfo("SORT: person birthday");
				comparator.setType(Type.PERSON_BIRTHDAY);
				rc = comparator.compare(p1, p2);
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
