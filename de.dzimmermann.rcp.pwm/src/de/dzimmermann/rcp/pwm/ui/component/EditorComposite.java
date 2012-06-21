package de.dzimmermann.rcp.pwm.ui.component;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.bind.JAXBException;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import de.dzimmermann.rcp.basicplatform.ui.util.DefaultAbstractTableLabelProvider;
import de.dzimmermann.rcp.basicplatform.ui.util.DefaultAbstractTreeTableLabelProvider;
import de.dzimmermann.rcp.basicplatform.ui.util.DefaultStructuredContentProvider;
import de.dzimmermann.rcp.basicplatform.ui.util.Logger;
import de.dzimmermann.rcp.basicplatform.util.InternalAdapter;
import de.dzimmermann.rcp.pwm.factory.MenuCreatorFactory;
import de.dzimmermann.rcp.pwm.model.container.PWMContainer;
import de.dzimmermann.rcp.pwm.model.container.PWMContainerGroup;
import de.dzimmermann.rcp.pwm.model.content.PWMGroup;
import de.dzimmermann.rcp.pwm.model.content.PWMGroupEntry;
import de.dzimmermann.rcp.pwm.ui.dialog.PasswordDialog;
import de.dzimmermann.rcp.pwm.util.PWMStates;
import de.dzimmermann.rcp.pwm.util.PWMUtils;

public class EditorComposite extends Composite implements InternalAdapter {

	private final FormToolkit formToolkit = new FormToolkit(
			Display.getDefault());

	private TreeViewer groupTreeViewer;
	private Tree groupTree;

	private TableViewer contentTableViewer;
	private Table contentTable;

	private ITreeContentProvider treeContentProvider = new InternalTreeContentProvider();
	private IStructuredContentProvider tableContentProvider = new DefaultStructuredContentProvider();

	private ILabelProvider treeLabelProvider = null;
	private ILabelProvider tableLabelProvider = null;

	private InternalSelectionChangedListener selectionChangedListener = new InternalSelectionChangedListener();

	private FormText nameFormText;
	private FormText dateAddedFormText;
	private FormText usernameFormText;
	private FormText dateModFormText;
	private FormText passwordFormText;
	private FormText dateExpFormText;
	private Button urlLink;
	private Text descText;

	private PWMContainer container;

	private PWMContainerGroup currentContainerGroup;

	private PWMGroup currentGroup;
	private String currentGroupPassword;

	private PWMGroupEntry currentEntry;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public EditorComposite(final Composite parent, final int style,
			final PWMContainer container) {

		super(parent, style);
		this.container = container;

		// setLayout(new FillLayout(SWT.HORIZONTAL));
		// setLayout(new GridLayout(2, false));
		setLayout(new FormLayout());

		formToolkit.paintBordersFor(this);

		// SashForm sashForm = new SashForm(this, SWT.NONE);

		// Composite groupComposite = formToolkit.createComposite(sashForm,
		// SWT.NONE);
		Composite groupComposite = formToolkit.createComposite(this, SWT.NONE);
		FormData fd_groupComposite = new FormData();
		fd_groupComposite.width = 225;
		fd_groupComposite.bottom = new FormAttachment(100);
		fd_groupComposite.top = new FormAttachment(0);
		groupComposite.setLayoutData(fd_groupComposite);
		// groupComposite.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false,
		// true, 1, 1));
		formToolkit.paintBordersFor(groupComposite);
		groupComposite.setLayout(new GridLayout(1, false));

		createGroupSection(groupComposite);

		// Composite contentComposite = formToolkit.createComposite(sashForm,
		// SWT.NONE);
		Composite contentComposite = formToolkit
				.createComposite(this, SWT.NONE);
		FormData fd_contentComposite = new FormData();
		fd_contentComposite.right = new FormAttachment(100);
		fd_contentComposite.top = new FormAttachment(0);
		fd_contentComposite.bottom = new FormAttachment(100);
		fd_contentComposite.left = new FormAttachment(groupComposite);
		contentComposite.setLayoutData(fd_contentComposite);
		// contentComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
		// true, 1, 1));
		contentComposite.setLayout(new GridLayout(1, false));

		createContentTableSection(contentComposite);
		createOverviewSection(contentComposite);

		// sashForm.setWeights(new int[] { 1, 2 });

		this.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				groupTreeViewer.setSelection(StructuredSelection.EMPTY);
				contentTableViewer.setLabelProvider(tableLabelProvider);
				contentTableViewer.setInput(null);
			}

			@Override
			public void focusGained(FocusEvent e) {

			}
		});

		final MenuManager menuManager = new MenuManager();
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(MenuCreatorFactory
				.createPWMMenuListener(true));
		final Menu menu = menuManager.createContextMenu(groupTreeViewer
				.getControl());
		groupTreeViewer.getControl().setMenu(menu);

		final MenuManager menuManager2 = new MenuManager();
		menuManager2.setRemoveAllWhenShown(true);
		menuManager2.addMenuListener(MenuCreatorFactory
				.createPWMMenuListener(false));
		final Menu menu2 = menuManager2.createContextMenu(contentTableViewer
				.getControl());
		contentTableViewer.getControl().setMenu(menu2);
		// FIXME this hack enables the menu
		// The menuAboutToShow method from the MenuManager is never triggered
		// for some unknown reason! o.O
		contentTableViewer.getControl().addMenuDetectListener(
				new MenuDetectListener() {
					@Override
					public void menuDetected(MenuDetectEvent e) {
						contentTableViewer.getControl().setMenu(menu2);
						contentTableViewer.getControl().getMenu()
								.setVisible(true);
					}
				});

		initWidgets(true);
	}

	private void createGroupSection(Composite groupComposite) {

		Section groupSection = formToolkit.createSection(groupComposite,
				Section.TITLE_BAR);
		groupSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));
		formToolkit.paintBordersFor(groupSection);
		groupSection.setText("Groups");

		Composite groupSectionComposite = formToolkit.createComposite(
				groupSection, SWT.NONE);
		formToolkit.paintBordersFor(groupSectionComposite);
		groupSection.setClient(groupSectionComposite);
		groupSectionComposite.setLayout(new GridLayout(1, false));

		groupTreeViewer = new TreeViewer(groupSectionComposite,
				SWT.FULL_SELECTION);
		groupTree = groupTreeViewer.getTree();
		groupTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1,
				1));
		formToolkit.paintBordersFor(groupTree);
	}

	private void createContentTableSection(Composite contentComposite) {

		Section contentTableSection = formToolkit.createSection(
				contentComposite, Section.TITLE_BAR);
		contentTableSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL,
				true, true, 1, 1));
		formToolkit.paintBordersFor(contentTableSection);
		contentTableSection.setText("Entries");

		Composite contentTableComposite = formToolkit.createComposite(
				contentTableSection, SWT.NONE);
		formToolkit.paintBordersFor(contentTableComposite);
		contentTableSection.setClient(contentTableComposite);
		contentTableComposite.setLayout(new GridLayout(1, false));

		contentTableViewer = new TableViewer(contentTableComposite,
				SWT.FULL_SELECTION);
		contentTable = contentTableViewer.getTable();
		contentTable.setHeaderVisible(true);
		contentTable.setLinesVisible(true);
		contentTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1));
		formToolkit.paintBordersFor(contentTable);
	}

	private void createOverviewSection(Composite contentComposite) {

		Section contentOverviewSection = formToolkit.createSection(
				contentComposite, Section.TWISTIE | Section.TITLE_BAR);
		contentOverviewSection.setLayoutData(new GridData(SWT.FILL, SWT.BOTTOM,
				true, false, 1, 1));
		formToolkit.paintBordersFor(contentOverviewSection);
		contentOverviewSection.setText("Entry Overview");
		contentOverviewSection.setExpanded(true);

		Composite contentOverviewComposite = formToolkit.createComposite(
				contentOverviewSection, SWT.NONE);
		formToolkit.paintBordersFor(contentOverviewComposite);
		contentOverviewSection.setClient(contentOverviewComposite);
		contentOverviewComposite.setLayout(new GridLayout(4, false));

		Label nameLabel = formToolkit.createLabel(contentOverviewComposite,
				"Title:", SWT.NONE);
		nameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));

		nameFormText = formToolkit.createFormText(contentOverviewComposite,
				false);
		nameFormText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		formToolkit.paintBordersFor(nameFormText);
		nameFormText.setText("", false, false);

		Label dateAddedLabel = formToolkit.createLabel(
				contentOverviewComposite, "Date Added:", SWT.NONE);
		dateAddedLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));

		dateAddedFormText = formToolkit.createFormText(
				contentOverviewComposite, false);
		dateAddedFormText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		formToolkit.paintBordersFor(dateAddedFormText);
		dateAddedFormText.setText("", false, false);

		Label usernameLabel = formToolkit.createLabel(contentOverviewComposite,
				"User Name:", SWT.NONE);
		usernameLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));

		usernameFormText = formToolkit.createFormText(contentOverviewComposite,
				false);
		usernameFormText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		formToolkit.paintBordersFor(usernameFormText);
		usernameFormText.setText("", false, false);

		Label dateModLabel = formToolkit.createLabel(contentOverviewComposite,
				"Date Modified:", SWT.NONE);
		dateModLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));

		dateModFormText = formToolkit.createFormText(contentOverviewComposite,
				false);
		dateModFormText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		formToolkit.paintBordersFor(dateModFormText);
		dateModFormText.setText("", false, false);

		Label passwordLabel = formToolkit.createLabel(contentOverviewComposite,
				"Password:", SWT.NONE);
		passwordLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));

		passwordFormText = formToolkit.createFormText(contentOverviewComposite,
				false);
		passwordFormText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		formToolkit.paintBordersFor(passwordFormText);
		passwordFormText.setText("", false, false);

		Label dateExpLabel = formToolkit.createLabel(contentOverviewComposite,
				"Expiration Date:", SWT.NONE);
		dateExpLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));

		dateExpFormText = formToolkit.createFormText(contentOverviewComposite,
				false);
		dateExpFormText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		formToolkit.paintBordersFor(dateExpFormText);
		dateExpFormText.setText("", false, false);

		Label urlLabel = formToolkit.createLabel(contentOverviewComposite,
				"URL:", SWT.NONE);
		urlLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));

		urlLink = formToolkit.createButton(contentOverviewComposite, "",
				SWT.NONE);
		urlLink.setAlignment(SWT.LEFT);
		urlLink.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				3, 1));

		urlLink.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Desktop d = Desktop.getDesktop();
				if (d == null)
					return;
				URI uri = null;
				try {
					uri = new URI(urlLink.getText());
				} catch (Exception e1) {
				}

				if (uri != null) {
					boolean error = false;
					try {
						d.browse(uri);
					} catch (IOException e1) {
						error = true;
					}
					if (error && !urlLink.getText().contains("://")) {
						try {
							uri = new URI("https://" + urlLink.getText());
						} catch (Exception e1) {
						}
						error = false;
						try {
							d.browse(uri);
						} catch (IOException e1) {
							error = true;
						}
					}
					if (error && !urlLink.getText().contains("://")) {
						try {
							uri = new URI("http://" + urlLink.getText());
						} catch (Exception e1) {
						}
						error = false;
						try {
							d.browse(uri);
						} catch (IOException e1) {
							error = true;
						}
					}
					if (error)
						MessageDialog.openInformation(
								getShell(),
								"Attention",
								String.format("Can't open '%s'!",
										urlLink.getText()));
				} else {
					MessageDialog.openInformation(getShell(), "Attention",
							String.format("Can't open '%s'! No valid URI?",
									urlLink.getText()));
				}
			}
		});

		Label descLabel = formToolkit.createLabel(contentOverviewComposite,
				"Description:", SWT.NONE);
		descLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false,
				1, 1));

		Composite composite = formToolkit.createComposite(
				contentOverviewComposite, SWT.NONE);
		composite.setLayout(new FormLayout());
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true,
				3, 1));
		formToolkit.paintBordersFor(composite);

		descText = formToolkit.createText(composite, "", SWT.WRAP
				| SWT.V_SCROLL | SWT.MULTI);
		FormData fd_descText = new FormData();
		fd_descText.height = 50;
		fd_descText.right = new FormAttachment(100);
		fd_descText.top = new FormAttachment(0);
		fd_descText.left = new FormAttachment(0);
		descText.setLayoutData(fd_descText);
		descText.setEditable(false);
		descText.setText("");
	}

	public void initWidgets(boolean init) {

		if (init) {
			groupTreeViewer.setContentProvider(treeContentProvider);
			treeLabelProvider = new InternalTreeLabelProvider(groupTreeViewer);
			groupTreeViewer
					.addSelectionChangedListener(selectionChangedListener);
			groupTreeViewer.addDoubleClickListener(selectionChangedListener);
			contentTableViewer.setContentProvider(tableContentProvider);
			tableLabelProvider = new InternalTableLabelProvider(
					contentTableViewer);
			contentTableViewer
					.addSelectionChangedListener(selectionChangedListener);
		}

		groupTreeViewer.setLabelProvider(treeLabelProvider);
		groupTreeViewer.setInput(container);
		groupTreeViewer.setSelection(StructuredSelection.EMPTY);

		contentTableViewer.setLabelProvider(tableLabelProvider);
		contentTableViewer.setInput(null);
	}

	public void initWidgets() {
		initWidgets(false);
	}

	public void setContainer(PWMContainer container) {
		this.container = container;
		initWidgets();
	}

	public void updateGroupContent(PWMGroup group) {
		if (group == null)
			return;
		currentGroup = group;
		contentTableViewer.setLabelProvider(tableLabelProvider);
		contentTableViewer.setInput(group.getEntries());
		contentTableViewer.setSelection(StructuredSelection.EMPTY);
	}

	private boolean currentlyShowsContent = false;

	public void clearContent() {
		contentTableViewer.setLabelProvider(tableLabelProvider);
		contentTableViewer.setInput(null);
		currentlyShowsContent = false;
	}

	public boolean isCurrentlyShowsContent() {
		return currentlyShowsContent;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	@Override
	public Object getInternalAdapter(Class<?> adapter) {
		if (PWMGroup.class.isAssignableFrom(adapter))
			return currentGroup;
		else if (PWMContainerGroup.class.isAssignableFrom(adapter))
			return currentContainerGroup;
		else if (PWMGroupEntry.class.isAssignableFrom(adapter))
			return currentEntry;
		else if (String.class.isAssignableFrom(adapter))
			return currentGroupPassword;
		return null;
	}

	private class InternalTreeContentProvider implements ITreeContentProvider {

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof PWMContainer) {
				return ((PWMContainer) inputElement).getGroups().toArray();
			} else if (inputElement instanceof PWMContainerGroup) {
				return ((PWMContainerGroup) inputElement).getGroups().toArray();
			}
			return null;
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof PWMContainer) {
				return ((PWMContainer) parentElement).getGroups().toArray();
			} else if (parentElement instanceof PWMContainerGroup) {
				return ((PWMContainerGroup) parentElement).getGroups()
						.toArray();
			}
			return null;
		}

		@Override
		public Object getParent(Object element) {
			if (element instanceof PWMContainer) {
				return null;
			} else if (element instanceof PWMContainerGroup) {
				return PWMUtils.findParentPWMObject(container,
						(PWMContainerGroup) element);
			}
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			if (element instanceof PWMContainer) {
				return !((PWMContainer) element).getGroups().isEmpty();
			} else if (element instanceof PWMContainerGroup) {
				return !((PWMContainerGroup) element).getGroups().isEmpty();
			}
			return false;
		}
	}

	private class InternalTreeLabelProvider extends
			DefaultAbstractTreeTableLabelProvider {

		public InternalTreeLabelProvider(Viewer viewer) {
			super(viewer);
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof PWMContainer) {
				return "Root Node";
			} else if (element instanceof PWMContainerGroup) {
				return ((PWMContainerGroup) element).getName();
			}
			return null;
		}

		@Override
		public String getText(Object element) {
			if (element instanceof PWMContainer) {
				return "Root Node";
			} else if (element instanceof PWMContainerGroup) {
				return ((PWMContainerGroup) element).getName();
			}
			return super.getText(element);
		}
	}

	private class InternalTableLabelProvider extends
			DefaultAbstractTableLabelProvider implements ITableColorProvider {

		private DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

		public InternalTableLabelProvider(Viewer viewer) {
			super(viewer);
			String[] header = new String[] { "Title", "Username", "URL",
					"Date Added", "Date Modified" };
			createTableColumns(header);
			initTableCoulmnWeights(viewer.getControl().getParent().getLayout(),
					header.length, new int[] { 150, 150, 250, 75, 75 },
					new int[] { 0, 0, 0, 0, 0 }, new boolean[] { true, true,
							true, false, false }, new boolean[] { true, true,
							true, false, false });
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof PWMGroupEntry) {
				switch (columnIndex) {
				case 0:
					return ((PWMGroupEntry) element).getName();
				case 1:
					return ((PWMGroupEntry) element).getUsername();
				case 2:
					return ((PWMGroupEntry) element).getUrl();
				case 3:
					return df.format(((PWMGroupEntry) element).getDateAdded()
							.getTime());
				case 4:
					return df.format(((PWMGroupEntry) element)
							.getDateModified().getTime());
				}
			}
			return null;
		}

		private final Color RED = Display.getCurrent().getSystemColor(
				SWT.COLOR_RED);
		private final Color WHITE = Display.getCurrent().getSystemColor(
				SWT.COLOR_WHITE);

		@Override
		public Color getBackground(Object element, int columnIndex) {
			if (element instanceof PWMGroupEntry) {
				Calendar exp = ((PWMGroupEntry) element).getDateExpiration();
				if (exp != null && !exp.after(Calendar.getInstance()))
					return RED;
			}
			return null;
		}

		@Override
		public Color getForeground(Object element, int columnIndex) {
			if (element instanceof PWMGroupEntry) {
				Calendar exp = ((PWMGroupEntry) element).getDateExpiration();
				if (exp != null && !exp.after(Calendar.getInstance()))
					return WHITE;
			}
			return null;
		}
	}

	private class InternalSelectionChangedListener implements
			ISelectionChangedListener, IDoubleClickListener {

		private DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

		@Override
		public void doubleClick(DoubleClickEvent event) {
			if (event.getSource() == groupTreeViewer) {

				IStructuredSelection selection = (IStructuredSelection) groupTreeViewer
						.getSelection();
				if (selection != null && !selection.isEmpty()) {
					PWMContainerGroup cg = (PWMContainerGroup) selection
							.getFirstElement();
					if (cg.getContent() != null) {
						String pw = "";
						PWMGroup group = null;
						// try with empty password
						try {
							group = PWMUtils.loadPWMGroup(cg, pw);
						} catch (JAXBException e) {
							e.printStackTrace();
							Logger.logError(e);
						}
						if (group == null) {
							PasswordDialog dialog = new PasswordDialog(
									getShell(), cg);
							if (dialog.open() == TitleAreaDialog.OK) {
								pw = dialog.getPassword();
							}
						}
						// try with specified password
						boolean pwProblem = false;
						try {
							group = PWMUtils.loadPWMGroup(cg, pw);
							if (group == null)
								pwProblem = true;
						} catch (JAXBException e) {
							Logger.logError(e);
							pwProblem = true;
						}
						if (pwProblem) {
							MessageDialog.openError(getShell(),
									"PWM - Wrong Password?",
									"The password you've specified seems to be incorrect...\n"
											+ "Please try again!");
						} else
							setupPWMGroup(cg, group, pw, true);
					} else {
						setupPWMGroup(cg, null, null, true);
					}
				} else {
					setupPWMGroup(null, null, null, true);
				}
			}
		}

		private final Color RED = Display.getCurrent().getSystemColor(
				SWT.COLOR_RED);
		private final Color WHITE = Display.getCurrent().getSystemColor(
				SWT.COLOR_WHITE);

		@Override
		public void selectionChanged(SelectionChangedEvent event) {

			if (event.getSource() == groupTreeViewer) {

				IStructuredSelection selection = (IStructuredSelection) groupTreeViewer
						.getSelection();
				if (selection != null && !selection.isEmpty()) {
					PWMContainerGroup cg = (PWMContainerGroup) selection
							.getFirstElement();
					setupPWMGroup(cg, currentGroup, null, false);
				} else {
					setupPWMGroup(null, null, null, false);
				}

			} else if (event.getSource() == contentTableViewer) {

				PWMGroupEntry entry = (PWMGroupEntry) ((IStructuredSelection) contentTableViewer
						.getSelection()).getFirstElement();
				if (entry != null) {
					currentEntry = entry;

					nameFormText.setText(
							entry.getName() != null ? entry.getName() : "",
							false, true);
					usernameFormText.setText(
							entry.getUsername() != null ? entry.getUsername()
									: "", false, true);
					urlLink.setText(entry.getUrl() != null ? entry.getUrl()
							: "");
					descText.setText(entry.getDescription() != null ? entry
							.getDescription() : "");
					descText.setToolTipText(entry.getDescription() != null ? entry
							.getDescription() : null);
					dateAddedFormText.setText(
							entry.getDateAdded() != null ? df.format(entry
									.getDateAdded().getTime()) : "", false,
							true);
					dateModFormText.setText(
							entry.getDateModified() != null ? df.format(entry
									.getDateModified().getTime()) : "", false,
							true);
					dateExpFormText.setText(
							entry.getDateExpiration() != null ? df.format(entry
									.getDateExpiration().getTime()) : "",
							false, true);
					Calendar exp = entry.getDateExpiration();
					if (exp != null && !exp.after(Calendar.getInstance())) {
						dateExpFormText.setBackground(RED);
						dateExpFormText.setForeground(WHITE);
					} else {
						dateExpFormText.setBackground(dateAddedFormText
								.getBackground());
						dateExpFormText.setForeground(dateAddedFormText
								.getForeground());
					}
					StringBuffer sb = new StringBuffer();
					if (entry.getPassword() != null)
						for (int i = 0; i < entry.getPassword().length(); i++)
							sb.append("*");
					passwordFormText.setText(sb.toString(), false, true);
				}
				PWMStates.enableEntriesState(true);
				PWMStates.entrySelectedState(entry != null);
			}
		}

		private void setupPWMGroup(PWMContainerGroup containerGroup,
				PWMGroup group, String pw, boolean reveal) {
			currentContainerGroup = containerGroup;
			if (group != null) {
				currentGroup = group;
				currentGroupPassword = pw;
				if (reveal) {
					contentTableViewer.setLabelProvider(tableLabelProvider);
					contentTableViewer.setInput(group.getEntries());
					contentTableViewer.setSelection(StructuredSelection.EMPTY);
				}
				currentlyShowsContent = true;
			} else {
				currentGroup = null;
				if (reveal) {
					contentTableViewer.setLabelProvider(tableLabelProvider);
					contentTableViewer.setInput(null);
				}
				currentlyShowsContent = false;
			}
			PWMStates.subgroupSelectedState(containerGroup != null);
			PWMStates.enableEntriesState(group != null);
			PWMStates
					.entrySelectedState(reveal ? false
							: ((IStructuredSelection) contentTableViewer
									.getSelection()).getFirstElement() != null);
		}
	}
}
