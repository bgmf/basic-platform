package de.dzimmermann.rcp.bsgtaucha.mgt.ui.dialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
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
import org.eclipse.ui.forms.widgets.Section;

import de.dzimmermann.rcp.basicplatform.ui.util.DefaultStructuredContentProvider;
import de.dzimmermann.rcp.basicplatform.ui.util.Logger;
import de.dzimmermann.rcp.basicplatform.ui.util.treewizard.TreeWizard;
import de.dzimmermann.rcp.basicplatform.ui.util.treewizard.TreeWizard.ButtonType;
import de.dzimmermann.rcp.basicplatform.ui.util.treewizard.TreeWizardPage;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.RootType;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.localfile.LocalFileModel;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.BSGTauchaConstants;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.BSGTauchaUtils;

public class OpenExistingFilePage extends TreeWizardPage {

	public static final String ID = OpenExistingFilePage.class.getName();

	private FormToolkit formToolkit = new FormToolkit(Display.getDefault());

	public OpenExistingFilePage(TreeWizard wizard, String pageId,
			String pageName, boolean canProceed) {
		super(wizard, pageId, pageName, canProceed);
		this.formToolkit = (FormToolkit) wizard
				.getInternalAdapter(FormToolkit.class);
	}

	private Composite container;

	private TableViewer openFileTableViewer;
	private Table openFileTable;

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	protected Composite createContent(Composite pageStack) {

		container = new Composite(pageStack, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));

		Composite composite = formToolkit.createComposite(container, SWT.NONE);
		formToolkit.paintBordersFor(container);
		composite.setLayout(new GridLayout(1, false));

		Section openFileSection = formToolkit.createSection(composite,
				Section.TITLE_BAR);
		openFileSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		formToolkit.paintBordersFor(openFileSection);
		openFileSection.setText(getPageName());

		Composite openFileComposite = formToolkit.createComposite(
				openFileSection, SWT.NONE);
		formToolkit.paintBordersFor(openFileComposite);
		openFileSection.setClient(openFileComposite);
		openFileComposite.setLayout(new GridLayout(1, false));

		openFileTableViewer = new TableViewer(openFileComposite, SWT.BORDER
				| SWT.FULL_SELECTION);
		openFileTable = openFileTableViewer.getTable();
		openFileTable.setHeaderVisible(true);
		openFileTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		formToolkit.paintBordersFor(openFileTable);

		TableViewerColumn fileNameViewerColumn = new TableViewerColumn(
				openFileTableViewer, SWT.NONE);
		TableColumn fileNameColumn = fileNameViewerColumn.getColumn();
		fileNameColumn.setWidth(200);
		fileNameColumn.setText("Datei");

		TableViewerColumn lastOpenViewerColumn = new TableViewerColumn(
				openFileTableViewer, SWT.NONE);
		TableColumn lastOpenColumn = lastOpenViewerColumn.getColumn();
		lastOpenColumn.setWidth(200);
		lastOpenColumn.setText("Zuletzt geändert");

		openFileTableViewer
				.setContentProvider(new DefaultStructuredContentProvider());
		openFileTableViewer.setLabelProvider(new ITableLabelProvider() {

			private final SimpleDateFormat df = new SimpleDateFormat(
					"dd.MM.yyyy HH:mm:ss");

			@Override
			public void addListener(ILabelProviderListener listener) {
			}

			@Override
			public void removeListener(ILabelProviderListener listener) {
			}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			@Override
			public void dispose() {
			}

			@Override
			public String getColumnText(Object element, int columnIndex) {
				if (element instanceof LocalFileModel) {
					LocalFileModel m = (LocalFileModel) element;
					switch (columnIndex) {
					case 0:
						return m.getFileName();
					case 1:
						return df.format(new Date(m.getLastModified()));
					}
				}
				return element.toString();
			}

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}
		});
		openFileTableViewer.setInput(localFileModels);
		openFileTableViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						localFile = (LocalFileModel) ((IStructuredSelection) openFileTableViewer
								.getSelection()).getFirstElement();
						if (localFile != null) {
							canProceed(true);
						} else {
							canProceed(false);
						}
					}
				});
		openFileTableViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				if (!canProceed)
					return;
				LoadModelDialog dialog = (LoadModelDialog) wizard
						.getInternalAdapter(LoadModelDialog.class);
				if (dialog != null) {
					Button ok = dialog.getButton(ButtonType.OK);
					if (ok != null && ok.isEnabled()) {
						wizard.triggerDefaulButtonHandler(ButtonType.OK);
						return;
					}
					Button next = dialog.getButton(ButtonType.NEXT);
					if (next != null && next.isEnabled()) {
						wizard.triggerDefaulButtonHandler(ButtonType.NEXT);
						return;
					}
				}
			}
		});

		return container;
	}

	@Override
	protected Composite getTopComposite() {
		return container;
	}

	@Override
	protected boolean checkPageContent() {
		return false;
	}

	private Set<LocalFileModel> localFileModels;
	private LocalFileModel localFile;

	@Override
	public Object getInternalAdapter(Class<?> adapter) {
		if (LocalFileModel.class.isAssignableFrom(adapter)) {
			return localFile;
		}
		return super.getInternalAdapter(adapter);
	}

	@Override
	protected void lazyLoadedPageContent() {
		localFileModels = getModelData();
		if (openFileTable != null && !openFileTable.isDisposed()) {
			openFileTableViewer.setInput(localFileModels);
		}
	}

	private Set<LocalFileModel> getModelData() {

		Set<LocalFileModel> localModelSet = new TreeSet<LocalFileModel>();
		try {
			IFileStore[] children = BSGTauchaUtils.getWorkspaceLocation()
					.childStores(EFS.NONE, null);
			for (IFileStore child : children) {
				if (child.fetchInfo().isDirectory()) {
					continue;
				}
				if (child.fetchInfo().getName()
						.endsWith(BSGTauchaConstants.MODEL_EXTENSION_BACKUP)) {
					continue;
				}
				RootType model = null;
				try {
					model = BSGTauchaUtils.openModel(child.toLocalFile(
							EFS.NONE, null));
				} catch (Exception e) {
					String message = String
							.format("File %s could not be loaded - it was potentially not a correct model file (message: %s)", //$NON-NLS-1$
									child.toURI(), e);
					Logger.logError(message, e);
					System.err.println(message);
				}
				if (model != null) {
					LocalFileModel local = new LocalFileModel(child,
							child.getName(), child.fetchInfo()
									.getLastModified(), model);
					localModelSet.add(local);
				}
			}
		} catch (CoreException e) {
			Logger.logError(e);
			e.printStackTrace();
		}

		return localModelSet;
	}
}
