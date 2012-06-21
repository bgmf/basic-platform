package de.dzimmermann.rcp.pwm.ui.dialog;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

import de.dzimmermann.rcp.basicplatform.ui.util.DefaultAbstractTableLabelProvider;
import de.dzimmermann.rcp.basicplatform.ui.util.DefaultStructuredContentProvider;
import de.dzimmermann.rcp.basicplatform.ui.util.Logger;
import de.dzimmermann.rcp.basicplatform.util.PFSCoreIconProvider;
import de.dzimmermann.rcp.basicplatform.util.SSDToolUtils;
import de.dzimmermann.rcp.pwm.util.PWMUtils;

public class ManagePWMDBsDialog extends TitleAreaDialog {

	private static final String WINDOW_TITLE = "PWM - Manage Databases";
	private static final String AREA_TITLE = "Manage Databases";
	private static final String AREA_MESSAGE = "Choose either a database to load or create new ones. "
			+ "You can delete them as well, but be aware, that you loose all data!";

	private final Image newImage;
	private final Image deleteImage;

	private TableViewer tableViewer;
	private Table table;

	private LabelProvider labelProvider;

	private List<IPath> databaseFiles;
	private IPath selectedDatabaseFile;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public ManagePWMDBsDialog(Shell parentShell) {
		super(parentShell);

		newImage = PFSCoreIconProvider.getImageByIconName("newfile_wiz.gif",//$NON-NLS-1$
				true);
		deleteImage = PFSCoreIconProvider.getImageByIconName(
				"newfile_delete_wiz.png",//$NON-NLS-1$
				true);
	}

	@Override
	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(newShellStyle | SWT.RESIZE);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(WINDOW_TITLE);
	}

	@Override
	protected IDialogSettings getDialogBoundsSettings() {
		IDialogSettings settings = SSDToolUtils.getDialogSettingsBySectionName(
				PWMUtils.PWM_DS_MANAGE_DB, true);
		if (settings != null)
			return settings;
		return super.getDialogBoundsSettings();
	}

	@Override
	protected int getDialogBoundsStrategy() {
		return super.getDialogBoundsStrategy();
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		setTitle(AREA_TITLE);
		setMessage(AREA_MESSAGE);

		tableViewer = new TableViewer(container, SWT.BORDER
				| SWT.FULL_SELECTION | SWT.SINGLE);
		table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

		TableViewerColumn tvc = new TableViewerColumn(tableViewer, SWT.NONE);
		tvc.getColumn().setText("Name");
		tvc.getColumn().setWidth(200);

		tvc = new TableViewerColumn(tableViewer, SWT.NONE);
		tvc.getColumn().setText("Last Modified");
		tvc.getColumn().setWidth(100);

		Button addButton = new Button(container, SWT.NONE);
		addButton.setImage(newImage);
		addButton.setText("Add");

		Button removeButton = new Button(container, SWT.NONE);
		removeButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		removeButton.setImage(deleteImage);
		removeButton.setText("Remove");

		tableViewer.setContentProvider(new DefaultStructuredContentProvider());
		labelProvider = new InternalLabelProvider(tableViewer);
		tableViewer.setLabelProvider(labelProvider);

		try {
			databaseFiles = PWMUtils.getDatabasesFromWorkspace();
		} catch (CoreException e) {
			e.printStackTrace();
			Logger.logError(e);
		}
		tableViewer.setInput(databaseFiles);

		tableViewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				okPressed();
			}
		});
		tableViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						IStructuredSelection selection = (IStructuredSelection) tableViewer
								.getSelection();
						if (selection != null && !selection.isEmpty())
							getButton(IDialogConstants.OK_ID).setEnabled(true);
						else
							getButton(IDialogConstants.OK_ID).setEnabled(false);
					}
				});

		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				NewDatabaseDialog dialog = new NewDatabaseDialog(getShell(),
						databaseFiles);
				if (Dialog.OK == dialog.open()
						&& dialog.getNewDatabaseName() != null) {
					IPath path = null;
					try {
						path = PWMUtils.addDatabaseToWorkspace(dialog
								.getNewDatabaseName());
						// PWMContainer container = PWMUtils
						// .loadPWMContainer(path);
						// PWMContainerGroup group = new PWMContainerGroup();
						// container.getGroups().add(group);
						// PWMGroup content = new PWMGroup();
						// content.setName("test");
						// content.setDescription("bla");
						// PWMGroupEntry entry = new PWMGroupEntry();
						// entry.setDateAdded(Calendar.getInstance());
						// entry.setDateModified(entry.getDateAdded());
						// entry.setName("entry-name");
						// entry.setId(entry.getName()
						// + entry.getDateAdded().getTimeInMillis());
						// entry.setUsername("entry-user");
						// entry.setPassword("entry-pw");
						// entry.setUrl("www.google.de");
						// entry.setDescription("test-entry");
						// content.getEntries().add(entry);
						// PWMUtils.savePWMGroup(group, content, null);
						// PWMUtils.savePWMContainer(path, container);
					} catch (JAXBException e1) {
						e1.printStackTrace();
						Logger.logError(e1);
					} catch (IOException e1) {
						e1.printStackTrace();
						Logger.logError(e1);
					} catch (CoreException e1) {
						e1.printStackTrace();
						Logger.logError(e1);
					} catch (Exception e1) {
						e1.printStackTrace();
						Logger.logError(e1);
					} finally {
						if (path != null) {
							if (databaseFiles == null)
								databaseFiles = new ArrayList<IPath>();
							databaseFiles.add(path);
						}
						tableViewer.setLabelProvider(labelProvider);
						tableViewer.setInput(databaseFiles);
					}
				}
			}
		});

		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) tableViewer
						.getSelection();
				if (selection != null && !selection.isEmpty()) {
					boolean b = MessageDialog
							.openConfirm(getShell(), "PWM - Confirm Deletion",
									"Are you sure, that you want to remove the selected database file?");
					if (b) {
						try {
							PWMUtils.removeDatabaseFromWorkspace((IPath) selection
									.getFirstElement());
							databaseFiles = PWMUtils
									.getDatabasesFromWorkspace();
						} catch (CoreException e1) {
							e1.printStackTrace();
							Logger.logError(e1);
						} finally {
							tableViewer.setLabelProvider(labelProvider);
							tableViewer.setInput(databaseFiles);
						}
					}
				}
			}
		});

		return area;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);

		getButton(IDialogConstants.OK_ID).setEnabled(false);
	}

	@Override
	protected void okPressed() {

		IStructuredSelection selection = (IStructuredSelection) tableViewer
				.getSelection();
		if (selection != null && !selection.isEmpty()) {
			selectedDatabaseFile = (IPath) selection.getFirstElement();
			super.okPressed();
		}
	}

	public IPath getSelectedDatabaseFile() {
		return selectedDatabaseFile;
	}

	public List<IPath> getDatabaseFiles() {
		return databaseFiles;
	}

	// @Override
	// protected Point getInitialSize() {
	// return new Point(640, 300);
	// }

	private class InternalLabelProvider extends
			DefaultAbstractTableLabelProvider {

		private DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

		public InternalLabelProvider(Viewer viewer) {
			super(viewer);
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			Assert.isTrue(element instanceof IPath);
			IPath path = (IPath) element;
			switch (columnIndex) {
			case 0:
				String p = path.lastSegment();
				if (p.toLowerCase().endsWith(PWMUtils.DEFAULT_EXTENSION_BACKUP))
					p = p.substring(0,
							p.lastIndexOf(PWMUtils.DEFAULT_EXTENSION_BACKUP));
				else if (p.toLowerCase().endsWith(PWMUtils.DEFAULT_EXTENSION))
					p = p.substring(0,
							p.lastIndexOf(PWMUtils.DEFAULT_EXTENSION));
				return p;
			case 1:
				return df.format(new Date(PWMUtils.FILE_SYSTEM.getStore(path)
						.fetchInfo().getLastModified()));
			}
			return "";
		}
	}
}
