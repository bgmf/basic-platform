package de.dzimmermann.rcp.bsgtaucha.mgt.ui.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import de.dzimmermann.rcp.basicplatform.ui.util.treewizard.TreeWizard;
import de.dzimmermann.rcp.basicplatform.ui.util.treewizard.TreeWizardPage;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.BSGTauchaUtils;

public class ODFExportTWPTarget extends TreeWizardPage {

	public static final String ID = ODFExportTWPTarget.class.getName();
	public static final String ID_SIMPLE_PERSON = ID + ".simple_person";
	public static final String ID_SIMPLE_BANDACTIONS = ID
			+ ".simple_bandactions";

	private final FormToolkit formToolkit = new FormToolkit(
			Display.getDefault());

	public ODFExportTWPTarget(TreeWizard wizard, String pageId,
			String pageName, boolean canProceed) {
		super(wizard, pageId, pageName, canProceed);
	}

	private Composite composite;
	private Text targetText;

	private String targetPath;

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
		contentComposite.setLayout(new GridLayout(3, false));

		Label targetLabel = formToolkit.createLabel(contentComposite, "Ziel:",
				SWT.NONE);
		targetLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));

		targetText = formToolkit.createText(contentComposite, "New Text",
				SWT.NONE | SWT.BORDER);
		targetText.setText("");
		targetText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		Button browseButton = formToolkit.createButton(contentComposite,
				"Durchsuchen...", SWT.NONE);
		browseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(
						ODFExportTWPTarget.this.composite.getShell(), SWT.SAVE);
				dialog.setText("Wählen Sie ein Ziel.\nHinweis: Die Dateiendung wird automatisch hinzugefügt.");
				String oldPath = BSGTauchaUtils
						.getDialogSettingsLastOpendDirectory();
				if (oldPath != null) {
					dialog.setFileName(oldPath);
					dialog.setFilterPath(oldPath);
				}
				String path = dialog.open();
				if (path != null && !path.isEmpty()) {
					BSGTauchaUtils.setDialogSettingsLastOpendDirectory(path,
							false);
					ODFExportTWPTarget.this.targetPath = path;
					targetText.setText(path);
					canProceed(true);
				} else {
					ODFExportTWPTarget.this.targetPath = null;
					targetText.setText("");
					canProceed(false);
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
		return false;
	}

	@Override
	public Object getInternalAdapter(Class<?> adapter) {
		if (String.class.isAssignableFrom(adapter)) {
			return targetPath;
		}
		return super.getInternalAdapter(adapter);
	}
}
