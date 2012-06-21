package de.dzimmermann.rcp.bsgtaucha.mgt.ui.dialog;

import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import de.dzimmermann.rcp.basicplatform.model.BasicPlatformSessionModel;
import de.dzimmermann.rcp.basicplatform.ui.util.DefaultStructuredContentProvider;
import de.dzimmermann.rcp.basicplatform.ui.util.treewizard.TreeWizard;
import de.dzimmermann.rcp.basicplatform.ui.util.treewizard.TreeWizardPage;
import de.dzimmermann.rcp.bsgtaucha.mgt.Activator;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.localfile.LocalFileModel;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.BSGTauchaConstants;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.DesEncrypter;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.ODFFile;

public class ODFExportTWPSelectType extends TreeWizardPage {

	public static final String ID = ODFExportTWPSelectType.class.getName();

	private final FormToolkit formToolkit = new FormToolkit(
			Display.getDefault());

	public ODFExportTWPSelectType(TreeWizard wizard, String pageId,
			String pageName, boolean canProceed) {
		super(wizard, pageId, pageName, canProceed);
	}

	private Composite composite;
	private ODFFile odfFile;

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	protected Composite createContent(Composite pageStack) {

		composite = formToolkit.createComposite(pageStack);
		composite.setLayout(new GridLayout(1, true));

		Section section = formToolkit.createSection(composite,
				Section.TITLE_BAR);
		section.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		formToolkit.paintBordersFor(section);
		section.setText(pageName);

		Composite contentComposite = formToolkit.createComposite(section,
				SWT.NONE);
		formToolkit.paintBordersFor(contentComposite);
		section.setClient(contentComposite);
		contentComposite.setLayout(new FillLayout(SWT.HORIZONTAL));

		final ListViewer listViewer = new ListViewer(contentComposite,
				SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE | SWT.FULL_SELECTION);

		listViewer.setContentProvider(new DefaultStructuredContentProvider());
		listViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof ODFFile) {
					return ((ODFFile) element).getName();
				}
				return super.getText(element);
			}
		});

		listViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				ODFFile odfFile = (ODFFile) ((IStructuredSelection) event
						.getSelection()).getFirstElement();
				if (odfFile != null) {
					ODFExportTWPSelectType.this.odfFile = odfFile;
					canProceed(true);
				} else {
					ODFExportTWPSelectType.this.odfFile = null;
					canProceed(false);
				}

			}
		});

		LocalFileModel model = (LocalFileModel) BasicPlatformSessionModel
				.getInstance().getPluginModels().get(Activator.PLUGIN_ID);
		String advPw = (String) BasicPlatformSessionModel
				.getInstance()
				.getPluginModels()
				.get(Activator.PLUGIN_ID
						+ BSGTauchaConstants.ADVANCED_PASSWORD_SUFFIX);
		boolean showPersonData = true;
		if (model != null && model.getModel() != null)
			if (model.getModel().getAdvancedPw() != null
					&& !model.getModel().getAdvancedPw().isEmpty())
				if (!DesEncrypter.isEqualPassPhrase(model.getModel()
						.getAdvancedPw(), advPw))
					showPersonData = false;

		Set<ODFFile> visible = new TreeSet<ODFFile>();
		for (ODFFile f : ODFFile.values()) {
			// XXX hard filter content
			if (ODFFile.BAND_ACTIONS_COMPLEX == f)
				continue;
			if ((f.isNeedAdmin() && showPersonData) || !f.isNeedAdmin())
				visible.add(f);
		}
		listViewer.setInput(visible);
		listViewer.setSelection(visible.isEmpty() ? StructuredSelection.EMPTY
				: new StructuredSelection(visible.iterator().next()));

		return composite;
	}

	@Override
	public Object getInternalAdapter(Class<?> adapter) {
		if (ODFFile.class.isAssignableFrom(adapter)) {
			return odfFile;
		}
		return super.getInternalAdapter(adapter);
	}

	@Override
	protected Composite getTopComposite() {
		return composite;
	}

	@Override
	protected boolean checkPageContent() {
		return false;
	}

	@Override
	public void canProceed(boolean canProceed) {
		switch (odfFile) {
		case PERSON_SIMPLE:
			setTargetPageID(ODFExportTWPTarget.ID_SIMPLE_PERSON);
			break;
		case BAND_ACTIONS_SIMPLE:
		case BAND_ACTIONS_COMPLEX:
			// setTargetPageID(ODFExportTWPSetUp.ID);
			setTargetPageID(ODFExportTWPTarget.ID);
			break;
		case ENTRIES_SIMPLE:
			setTargetPageID(ODFExportTWPTarget.ID);
			break;
		}
		super.canProceed(canProceed);
	}
}
