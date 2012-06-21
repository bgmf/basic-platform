package de.dzimmermann.rcp.pwm.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import de.dzimmermann.rcp.basicplatform.util.PFSCoreIconProvider;

public class MainComposite extends Composite {

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public MainComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));

		createToolbar();

		SashForm sashForm = new SashForm(this, SWT.NONE);
		sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));

		Composite groupContainer = new Composite(sashForm, SWT.NONE);

		Composite contentContainer = new Composite(sashForm, SWT.NONE);
		contentContainer.setLayout(new FillLayout(SWT.HORIZONTAL));

		SashForm contentSashForm = new SashForm(contentContainer, SWT.VERTICAL);

		Composite contentTableComposite = new Composite(contentSashForm,
				SWT.NONE);

		Composite contentOverviewComposite = new Composite(contentSashForm,
				SWT.NONE);
		contentSashForm.setWeights(new int[] { 2, 1 });

		sashForm.setWeights(new int[] { 1, 2 });
	}

	@Override
	protected void checkSubclass() {
	}

	private void createToolbar() {

		ToolBar toolBar = new ToolBar(this, SWT.FLAT | SWT.RIGHT);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));

		ToolItem newDbItem = new ToolItem(toolBar, SWT.NONE);
		newDbItem.setImage(PFSCoreIconProvider.getImageByIconName("fugue_database--plus.png", true));
		newDbItem.setToolTipText("New Database");

		ToolItem openDbItem = new ToolItem(toolBar, SWT.NONE);
		openDbItem.setImage(PFSCoreIconProvider.getImageByIconName("fugue_database-import.png", true));
		openDbItem.setToolTipText("Open Database");

		ToolItem saveDbItem = new ToolItem(toolBar, SWT.NONE);
		saveDbItem.setImage(PFSCoreIconProvider.getImageByIconName("fugue_database--minus.png", true));
		saveDbItem.setToolTipText("Save Database");

		ToolItem sepItem1 = new ToolItem(toolBar, SWT.SEPARATOR);

		ToolItem groupItem = new ToolItem(toolBar, SWT.DROP_DOWN);
		groupItem.setImage(PFSCoreIconProvider.getImageByIconName("fugue_folders-stack.png", true));
		groupItem.setToolTipText("Group Management");

		ToolItem sepItem2 = new ToolItem(toolBar, SWT.SEPARATOR);

		ToolItem entryItem = new ToolItem(toolBar, SWT.DROP_DOWN);
		entryItem.setImage(PFSCoreIconProvider.getImageByIconName("fugue_key.png", true));
		entryItem.setToolTipText("Entry Management (per Group)");
	}
}
