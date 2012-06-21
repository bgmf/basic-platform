package de.dzimmermann.rcp.bsgtaucha.mgt.ui.dialog;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import de.dzimmermann.rcp.basicplatform.ui.util.DefaultStructuredContentProvider;
import de.dzimmermann.rcp.basicplatform.ui.util.treewizard.TreeWizard;
import de.dzimmermann.rcp.basicplatform.ui.util.treewizard.TreeWizardPage;

public class SelectOpenTypePage extends TreeWizardPage {

	public static final String ID = SelectOpenTypePage.class.getName();

	private FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	public SelectOpenTypePage(TreeWizard wizard, String pageId,
			String pageName, boolean canProceed) {
		super(wizard, pageId, pageName, canProceed);
		this.formToolkit = (FormToolkit) wizard
				.getInternalAdapter(FormToolkit.class);
	}

	private OpenType openType;

	private Composite composite;

	// private final FormToolkit formToolkit_1 = new FormToolkit(
	// Display.getDefault());

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	protected Composite createContent(Composite pageStack) {

		composite = new Composite(pageStack, SWT.NONE);
		composite.setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite container = formToolkit.createComposite(composite, SWT.NONE);
		formToolkit.paintBordersFor(container);
		container.setLayout(new GridLayout(1, false));

		Section selectTypeSection = formToolkit.createSection(container,
				Section.TITLE_BAR);
		selectTypeSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		formToolkit.paintBordersFor(selectTypeSection);
		selectTypeSection.setText(getPageName());

		Composite typeComposite = formToolkit.createComposite(
				selectTypeSection, SWT.NONE);
		formToolkit.paintBordersFor(typeComposite);
		selectTypeSection.setClient(typeComposite);
		typeComposite.setLayout(new GridLayout(2, false));

		ListViewer listViewer = new ListViewer(typeComposite, SWT.BORDER
				| SWT.V_SCROLL);
		List list = listViewer.getList();
		list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		Label descrLabel = formToolkit.createLabel(typeComposite,
				"Beschreibung:", SWT.WRAP);
		descrLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false,
				1, 1));

		final Label descrContentLabel = formToolkit.createLabel(typeComposite,
				"", SWT.WRAP);
		descrContentLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true,
				false, 1, 1));

		listViewer.setContentProvider(new DefaultStructuredContentProvider());
		listViewer.setLabelProvider(new LabelProvider());
		listViewer.setInput(OpenType.values());

		listViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				openType = (OpenType) ((IStructuredSelection) event
						.getSelection()).getFirstElement();
				if (openType != null) {
					descrContentLabel.setText(openType.description);
					descrContentLabel.getParent().layout(true, true);
					canProceed(true);
				} else {
					descrContentLabel.setText("");
					descrContentLabel.getParent().layout(true, true);
					canProceed(false);
				}
			}
		});
		listViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				if (canProceed) {
					// TODO fix strange behaviour
					// the open existing file page can not set the canProceed
					// flag anymore - the reason might be, that sometimes the
					// parents (a.k.a. this page's) canProceed flag is not set
					// properly
					wizard.triggerDefaulButtonHandler(TreeWizard.ButtonType.NEXT);
				}
			}
		});

		return composite;
	}

	@Override
	protected Composite getTopComposite() {
		return composite;
	}

	@Override
	protected boolean checkPageContent() {
		canProceed(true);
		if (openType == null) {
			super.canProceed(false);
			return false;
		}
		return true;
	}

	@Override
	public void canProceed(boolean canProceed) {
		if (canProceed) {
			if (openType == null) {
				super.canProceed(false);
				return;
			}
			switch (openType) {
			case OPEN_EXISTING:
				setTargetPageID(OpenExistingFilePage.ID);
				break;
			case CREATE_NEW:
				setTargetPageID(CreateNewFilePage.ID);
				break;
			}
		}
		super.canProceed(canProceed);
	}

	@Override
	public Object getInternalAdapter(Class<?> adapter) {
		if (OpenType.class.isAssignableFrom(adapter)) {
			return openType;
		}
		return super.getInternalAdapter(adapter);
	}

	public enum OpenType {

		//
		OPEN_EXISTING(
				"Eine bestehende Datei öffnen",
				"Auf der nächsten Seite können Sie eine bestehende Datei auswählen, die Sie bearbeiten möchten."),

		//
		CREATE_NEW(
				"Eine neue Datei erstellen",
				"Auf der nächsten Seite können Sie eine neue Datei erstellen, die Passwörter festlegen und zum bearbeiten öffnen.");

		private final String name;
		private final String description;

		@Override
		public String toString() {
			return name;
		}

		private OpenType(final String name, final String description) {
			this.name = name;
			this.description = description;
		}
	}
}
