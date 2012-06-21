package de.dzimmermann.rcp.bsgtaucha.mgt.ui.dialog;

import java.util.Calendar;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.dzimmermann.rcp.basicplatform.ui.util.DefaultStructuredContentProvider;
import de.dzimmermann.rcp.basicplatform.util.PFSCoreIconProvider;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.BandActionType;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.BandTypes;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.EntryType;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.ObjectFactory;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.PersonType;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.RootType;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.WorkType;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.OpenDocumentCreator;

public class WorkEntryDialog extends Dialog {

	private final FormToolkit formToolkit = new FormToolkit(
			Display.getDefault());

	private final ObjectFactory factory = new ObjectFactory();
	private RootType root;
	private EntryType entry;

	private boolean bandActionOnly;

	private boolean editMode;

	private WorkType currentWorkType;
	private PersonType currentPersonType;

	private Form parentForm;

	private DateTime dateTime;
	private Text timeText;

	private ComboViewer workComboViewer;
	private ComboViewer personComboViewer;

	private Text commentText;

	private Label bandTypeLabel;
	private ComboViewer bandTypeComboViewer;
	private Combo bandTypeCombo;
	private Label amountLabel;
	private Text amountText;
	private Link trackLink;
	private Text trackText;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public WorkEntryDialog(Shell parentShell, RootType root, EntryType entry,
			boolean editMode, boolean bandActionOnly) {
		super(parentShell);
		this.root = root;
		if (entry == null) {
			entry = new ObjectFactory().createEntryType();
		} else {
			EntryType tmp = entry.getClone();
			entry = tmp;
		}
		this.editMode = editMode;
		this.entry = entry;
		this.bandActionOnly = bandActionOnly;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		getShell().setText("Geleistete Arbeit anlegen oder anpassen");

		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		parentForm = formToolkit.createForm(container);
		formToolkit.paintBordersFor(parentForm);
		formToolkit.decorateFormHeading(parentForm);
		parentForm.setText("Geleistete Arbeit anlegen oder anpassen");

		IToolBarManager toolbar = parentForm.getToolBarManager();
		Action reuseSelectionAction = new InternalReuseEntryAction();
		toolbar.add(reuseSelectionAction);
		toolbar.update(true);

		parentForm.getBody().setLayout(new GridLayout(1, false));

		Composite composite = formToolkit.createComposite(parentForm.getBody(),
				SWT.NONE);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));
		formToolkit.paintBordersFor(composite);
		composite.setLayout(new GridLayout(4, false));

		Label dateLabel = new Label(composite, SWT.NONE);
		dateLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false,
				1, 1));
		formToolkit.adapt(dateLabel, true, true);
		dateLabel.setText("Datum");

		dateTime = new DateTime(composite, SWT.BORDER | SWT.CALENDAR);
		formToolkit.adapt(dateTime);
		formToolkit.paintBordersFor(dateTime);

		Label timeLabel = new Label(composite, SWT.NONE);
		timeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false,
				1, 1));
		formToolkit.adapt(timeLabel, true, true);
		timeLabel.setText("Dauer (in h)");

		timeText = new Text(composite, SWT.NONE);
		timeText.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1,
				1));
		formToolkit.adapt(timeText, true, true);

		Label workLabel = new Label(composite, SWT.NONE);
		workLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		formToolkit.adapt(workLabel, true, true);
		workLabel.setText("Tätigkeit");

		workComboViewer = new ComboViewer(composite, SWT.NONE);
		Combo workCombo = workComboViewer.getCombo();
		workCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		formToolkit.paintBordersFor(workCombo);

		Label personLabel = new Label(composite, SWT.NONE);
		personLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		formToolkit.adapt(personLabel, true, true);
		personLabel.setText("Person");

		personComboViewer = new ComboViewer(composite, SWT.NONE);
		Combo personCombo = personComboViewer.getCombo();
		personCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		formToolkit.paintBordersFor(personCombo);

		Label commentLabel = new Label(composite, SWT.NONE);
		commentLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false,
				false, 1, 1));
		formToolkit.adapt(commentLabel, true, true);
		commentLabel.setText("Bemerkungen");

		commentText = new Text(composite, SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_commentText = new GridData(SWT.FILL, SWT.CENTER, true,
				false, 3, 1);
		gd_commentText.heightHint = 50;
		commentText.setLayoutData(gd_commentText);
		formToolkit.adapt(commentText, true, true);

		Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label = new GridData(SWT.FILL, SWT.CENTER, true, false, 4,
				1);
		gd_label.heightHint = 10;
		label.setLayoutData(gd_label);
		formToolkit.adapt(label, true, true);

		bandTypeLabel = new Label(composite, SWT.NONE);
		bandTypeLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		formToolkit.adapt(bandTypeLabel, true, true);
		bandTypeLabel.setText("Band Typ");

		bandTypeComboViewer = new ComboViewer(composite, SWT.NONE);
		bandTypeCombo = bandTypeComboViewer.getCombo();
		bandTypeCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		formToolkit.paintBordersFor(bandTypeCombo);

		amountLabel = new Label(composite, SWT.NONE);
		amountLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		formToolkit.adapt(amountLabel, true, true);
		amountLabel.setText("Anzahl");

		amountText = new Text(composite, SWT.NONE);
		amountText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		formToolkit.adapt(amountText, true, true);

		new Label(composite, SWT.NONE);
		new Label(composite, SWT.NONE);

		trackLink = new Link(composite, SWT.NONE);
		trackLink.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		formToolkit.adapt(trackLink, true, true);
		trackLink.setText("<a>Bahn(en)</a>");

		trackText = new Text(composite, SWT.NONE);
		trackText.setEditable(false);
		trackText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		formToolkit.adapt(trackText, true, true);

		bandTypeLabel.setEnabled(false);
		bandTypeCombo.setEnabled(false);
		amountLabel.setEnabled(false);
		amountText.setEnabled(false);
		trackLink.setEnabled(false);
		trackText.setEnabled(false);

		DefaultStructuredContentProvider provider = new DefaultStructuredContentProvider();
		workComboViewer.setContentProvider(provider);
		personComboViewer.setContentProvider(provider);
		bandTypeComboViewer.setContentProvider(provider);

		InternalLabelProvider labelProvider = new InternalLabelProvider();
		workComboViewer.setLabelProvider(labelProvider);
		personComboViewer.setLabelProvider(labelProvider);
		bandTypeComboViewer.setLabelProvider(labelProvider);

		if (root.getWorks() != null && !root.getWorks().getWork().isEmpty()) {
			if (bandActionOnly) {
				workComboViewer.addFilter(new IntenalViewerFilter());
			}
			workComboViewer.setInput(root.getWorks().getWork());
		} else {
			workLabel.setForeground(Display.getCurrent().getSystemColor(
					SWT.COLOR_RED));
		}
		if (root.getPersons() != null
				&& !root.getPersons().getPerson().isEmpty()) {
			personComboViewer.setInput(root.getPersons().getPerson());
		} else {
			personLabel.setForeground(Display.getCurrent().getSystemColor(
					SWT.COLOR_RED));
		}

		bandTypeComboViewer.setInput(BandTypes.values());

		if (editMode) {
			if (entry.getDate() != null) {
				Calendar c = Calendar.getInstance();
				c.setTime(entry.getDate());
				dateTime.setDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
						c.get(Calendar.DATE));
			}
			if (entry.getTime() != null) {
				timeText.setText("" + entry.getTime());
			}
			if (entry.getComment() != null) {
				commentText.setText(entry.getComment());
			}
			if (entry.getPersonId() != null) {
				PersonType pt = null;
				if (root.getPersons() != null) {
					for (PersonType p : root.getPersons().getPerson()) {
						if (p.getId().equals(entry.getPersonId())) {
							pt = p;
							break;
						}
					}

				}
				if (pt != null) {
					StructuredSelection s = new StructuredSelection(pt);
					personComboViewer.setSelection(s);
					currentPersonType = pt;
				}
				personCombo.setEnabled(false);
			}
			if (entry.getWorkId() != null) {
				WorkType wt = null;
				if (root.getWorks() != null) {
					for (WorkType w : root.getWorks().getWork()) {
						if (w.getId().equals(entry.getWorkId())) {
							wt = w;
							break;
						}
					}

				}
				if (wt != null) {
					StructuredSelection s = new StructuredSelection(wt);
					workComboViewer.setSelection(s);
					currentWorkType = wt;
				}
				workCombo.setEnabled(false);
			}
			if (entry.getBandAction() != null) {
				if (entry.getBandAction().getBandType() != null) {
					StructuredSelection s = new StructuredSelection(entry
							.getBandAction().getBandType());
					bandTypeComboViewer.setSelection(s);
				}
				bandTypeLabel.setEnabled(true);
				bandTypeCombo.setEnabled(true);
				if (entry.getBandAction().getAmount() != null) {
					amountText.setText("" + entry.getBandAction().getAmount());
				}
				amountLabel.setEnabled(true);
				amountText.setEnabled(true);
				trackText.setText(getTrackString(entry.getBandAction()));
				trackLink.setEnabled(true);
				trackText.setEnabled(true);
			}
		}

		ModifyListener modifyListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				if (e.widget == timeText) {
					entry.setTime(!timeText.getText().isEmpty() ? Float
							.valueOf(timeText.getText()) : 0);
				} else if (e.widget == commentText) {
					entry.setComment(!commentText.getText().isEmpty() ? commentText
							.getText() : null);
				} else if (e.widget == amountText) {
					if (entry.getBandAction() == null)
						entry.setBandAction(factory.createBandActionType());
					if (!amountText.getText().isEmpty()) {
						entry.getBandAction().setAmount(
								Integer.parseInt(amountText.getText()));
					} else {
						entry.getBandAction().setAmount(null);
					}
				}
			}
		};
		timeText.addModifyListener(modifyListener);
		commentText.addModifyListener(modifyListener);
		amountText.addModifyListener(modifyListener);

		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.widget == dateTime) {
					Calendar c = Calendar.getInstance();
					c.set(dateTime.getYear(), dateTime.getMonth(),
							dateTime.getDay());
					entry.setDate(c.getTime());
				} else if (e.widget == trackLink) {
					InternalTrackSelectionDialog dialog = new InternalTrackSelectionDialog(
							getShell(), entry.getBandAction());
					if (InternalTrackSelectionDialog.OK == dialog.open()) {
						BandActionType ba = dialog.getBandAction();
						if (entry.getBandAction() == null)
							entry.setBandAction(ba);
						else {
							entry.getBandAction().getTrack().clear();
							entry.getBandAction().getTrack()
									.addAll(ba.getTrack());
						}
						trackText
								.setText(getTrackString(entry.getBandAction()));
					}
				}
			}
		};
		dateTime.addSelectionListener(selectionAdapter);
		trackLink.addSelectionListener(selectionAdapter);

		ISelectionChangedListener seleChangedListener = new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				if (event.getSource() == workComboViewer) {
					WorkType wt = (WorkType) ((IStructuredSelection) workComboViewer
							.getSelection()).getFirstElement();
					if (wt != null) {
						currentWorkType = wt;
						entry.setWorkId(wt.getId());
						if (wt.isAffectBand() != null && wt.isAffectBand()) {
							bandTypeLabel.setEnabled(true);
							bandTypeCombo.setEnabled(true);
							amountLabel.setEnabled(true);
							amountText.setEnabled(true);
							trackLink.setEnabled(true);
							trackText.setEnabled(true);
						} else {
							bandTypeLabel.setEnabled(false);
							bandTypeCombo.setEnabled(false);
							amountLabel.setEnabled(false);
							amountText.setEnabled(false);
							trackLink.setEnabled(false);
							trackText.setEnabled(false);
						}
					} else {
						currentWorkType = null;
					}
				} else if (event.getSource() == personComboViewer) {
					PersonType pt = (PersonType) ((IStructuredSelection) personComboViewer
							.getSelection()).getFirstElement();
					if (pt != null) {
						currentPersonType = pt;
						entry.setPersonId(pt.getId());
					} else {
						currentPersonType = null;
					}
				} else if (event.getSource() == bandTypeComboViewer) {
					BandTypes bt = (BandTypes) ((IStructuredSelection) bandTypeComboViewer
							.getSelection()).getFirstElement();
					if (entry.getBandAction() == null)
						entry.setBandAction(factory.createBandActionType());
					if (bt != null) {
						entry.getBandAction().setBandType(bt);
					} else {
						entry.getBandAction().setBandType(null);
					}
				}
			}
		};
		workComboViewer.addSelectionChangedListener(seleChangedListener);
		personComboViewer.addSelectionChangedListener(seleChangedListener);
		bandTypeComboViewer.addSelectionChangedListener(seleChangedListener);

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
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
	protected void okPressed() {

		if (currentWorkType == null) {
			parentForm.setMessage("Wählen Sie eine Tätigkeit!",
					IMessageProvider.ERROR);
			return;
		} else {
			if (currentWorkType.isAffectBand() == null
					|| !currentWorkType.isAffectBand()) {
				entry.setBandAction(null);
			} else {
				if (entry.getBandAction() == null) {
					parentForm.setMessage("Unbekanntes Problem bei Bändern!",
							IMessageProvider.ERROR);
					return;
				}
				if (entry.getBandAction().getBandType() == null) {
					parentForm.setMessage("Kein Bandtyp ausgewählt!",
							IMessageProvider.ERROR);
					return;
				}
				if (entry.getBandAction().getAmount() == null
						|| entry.getBandAction().getAmount() == 0) {
					boolean confirm = MessageDialog
							.openQuestion(
									getShell(),
									"Anzahl bestätigen",
									"Sie wollen bei einer Tätigkeit, die ein oder mehrere Bänder betreffen kann, eine Anzahl von '0' angeben...\n"
											+ "Sind Sie sich sicher?");
					if (confirm)
						entry.getBandAction().setAmount(0);
					else
						return;
				}
			}
		}

		if (currentPersonType == null) {
			parentForm.setMessage("Wählen Sie eine Person!",
					IMessageProvider.ERROR);
			return;
		}

		if (entry.getDate() == null) {
			Calendar c = Calendar.getInstance();
			c.set(dateTime.getYear(), dateTime.getMonth(), dateTime.getDay());
			entry.setDate(c.getTime());
		}
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		c.add(Calendar.DATE, +1);
		if (entry.getDate() == null || c.getTime().before(entry.getDate())) {
			parentForm.setMessage("Kein Datum, oder Datum in der Zukunft!",
					IMessageProvider.ERROR);
			return;
		}

		if (entry.getTime() == null || entry.getTime() == 0) {
			boolean confirm = MessageDialog.openQuestion(getShell(),
					"Dauer bestätigen",
					"Sie wollen bei einer Tätigkeit, eine Dauer von '0' Stunden angeben...\n"
							+ "Sind Sie sich sicher?");
			if (confirm)
				entry.setTime(new Float(0));
			else
				return;
		}

		super.okPressed();
	}

	public EntryType getEntry() {
		return entry;
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(600, 500);
	}

	private class InternalReuseEntryAction extends Action {

		public InternalReuseEntryAction() {
			super("Vorauswahl übernehmen", IAction.AS_PUSH_BUTTON);
			setImageDescriptor(ImageDescriptor
					.createFromImage(PFSCoreIconProvider.getImageByIconName(
							"fugue_arrow-split-270.png", true)));
		}

		@Override
		public void run() {
			if (entry != null) {
				if (entry.getDate() != null) {
					Calendar c = Calendar.getInstance();
					c.setTime(entry.getDate());
					dateTime.setDate(c.get(Calendar.YEAR),
							c.get(Calendar.MONTH), c.get(Calendar.DATE));
				}
				if (entry.getTime() != null) {
					timeText.setText("" + entry.getTime());
				}
				if (entry.getComment() != null) {
					commentText.setText(entry.getComment());
				}
				if (entry.getPersonId() != null) {
					PersonType pt = null;
					if (root.getPersons() != null) {
						for (PersonType p : root.getPersons().getPerson()) {
							if (p.getId().equals(entry.getPersonId())) {
								pt = p;
								break;
							}
						}

					}
					if (pt != null) {
						StructuredSelection s = new StructuredSelection(pt);
						personComboViewer.setSelection(s);
					}
				}
				if (entry.getWorkId() != null) {
					WorkType wt = null;
					if (root.getWorks() != null) {
						for (WorkType w : root.getWorks().getWork()) {
							if (w.getId().equals(entry.getWorkId())) {
								wt = w;
								break;
							}
						}

					}
					if (wt != null) {
						StructuredSelection s = new StructuredSelection(wt);
						workComboViewer.setSelection(s);
					}
				}
				if (entry.getBandAction() != null) {
					if (entry.getBandAction().getBandType() != null) {
						StructuredSelection s = new StructuredSelection(entry
								.getBandAction().getBandType());
						bandTypeComboViewer.setSelection(s);
					}
					bandTypeLabel.setEnabled(true);
					bandTypeCombo.setEnabled(true);
					if (entry.getBandAction().getAmount() != null) {
						amountText.setText(""
								+ entry.getBandAction().getAmount());
					}
					amountLabel.setEnabled(true);
					amountText.setEnabled(true);
					trackText.setText(getTrackString(entry.getBandAction()));
					trackLink.setEnabled(true);
					trackText.setEnabled(true);
				}
			}
		}
	}

	private class IntenalViewerFilter extends ViewerFilter {

		@Override
		public boolean select(Viewer viewer, Object parentElement,
				Object element) {
			if (element instanceof WorkType) {
				if (bandActionOnly
						&& (((WorkType) element).isAffectBand() == null || !((WorkType) element)
								.isAffectBand()))
					return false;
			}
			return true;
		}
	}

	private class InternalLabelProvider implements ILabelProvider,
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

	private class InternalTrackSelectionDialog extends Dialog {

		private final FormToolkit formToolkit = new FormToolkit(
				Display.getDefault());

		private BandActionType bandAction;

		protected InternalTrackSelectionDialog(Shell parentShell,
				BandActionType bandAction) {
			super(parentShell);
			if (bandAction == null)
				this.bandAction = factory.createBandActionType();
			else {
				BandActionType tmp = bandAction.getClone();
				if (!WorkEntryDialog.this.editMode)
					tmp.getTrack().clear();
				this.bandAction = tmp;
			}
		}

		public BandActionType getBandAction() {
			return bandAction;
		}

		private static final String TRACK_KEY = "track";

		@Override
		protected Control createDialogArea(Composite parent) {

			getShell().setText("Betroffene Bahn(en) bearbeiten");

			Composite container = (Composite) super.createDialogArea(parent);
			container.setLayout(new FillLayout(SWT.HORIZONTAL));

			parentForm = formToolkit.createForm(container);
			formToolkit.paintBordersFor(parentForm);
			formToolkit.decorateFormHeading(parentForm);
			parentForm.setText("Betroffene Bahn(en) bearbeiten");

			parentForm.getBody().setLayout(new GridLayout(1, true));

			for (Integer i = 1; i <= 6; i++) {

				final Button trackButton = formToolkit.createButton(
						parentForm.getBody(), "Bahn 1", SWT.CHECK);
				trackButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER,
						true, false));
				trackButton.setText("Bahn " + i.toString());

				if (bandAction.getTrack().contains(i))
					trackButton.setSelection(true);

				trackButton.setData(TRACK_KEY, i);

				trackButton.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						if (((Button) e.widget).getSelection()) {
							bandAction.getTrack().add(
									(Integer) e.widget.getData(TRACK_KEY));
						} else {
							bandAction.getTrack().remove(
									(Integer) e.widget.getData(TRACK_KEY));
						}
					}
				});

				if (i == 2 || i == 4) {
					Label seperator = formToolkit.createLabel(
							parentForm.getBody(), "", SWT.SEPARATOR
									| SWT.HORIZONTAL);
					seperator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
							true, false));
				}
			}
			return container;
		}

		@Override
		protected void createButtonsForButtonBar(Composite parent) {
			createButton(parent, IDialogConstants.OK_ID,
					IDialogConstants.OK_LABEL, true);
			createButton(parent, IDialogConstants.CANCEL_ID,
					IDialogConstants.CANCEL_LABEL, false);
		}

		@Override
		protected Point getInitialSize() {
			return new Point(320, 333);
		}
	}
}
