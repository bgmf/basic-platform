package de.dzimmermann.rcp.bsgtaucha.mgt.ui.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import de.dzimmermann.rcp.basicplatform.ui.util.treewizard.TreeWizard;
import de.dzimmermann.rcp.basicplatform.ui.util.treewizard.TreeWizardPage;

public class ODFExportTWPSetUp extends TreeWizardPage {

	public static final String ID = ODFExportTWPSetUp.class.getName();

	private final FormToolkit formToolkit = new FormToolkit(
			Display.getDefault());

	public ODFExportTWPSetUp(TreeWizard wizard, String pageId, String pageName,
			boolean canProceed) {
		super(wizard, pageId, pageName, canProceed);
	}

	private Composite composite;

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

		return composite;
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
	public Object getInternalAdapter(Class<?> adapter) {
		return super.getInternalAdapter(adapter);
	}
}
