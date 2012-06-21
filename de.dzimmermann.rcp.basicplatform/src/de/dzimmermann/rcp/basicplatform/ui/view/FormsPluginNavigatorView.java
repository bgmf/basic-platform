package de.dzimmermann.rcp.basicplatform.ui.view;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import de.dzimmermann.rcp.basicplatform.SSDToolPlugin;
import de.dzimmermann.rcp.basicplatform.handler.BasicPlatformStates;
import de.dzimmermann.rcp.basicplatform.model.BasicPlatformSessionModel;
import de.dzimmermann.rcp.basicplatform.model.Task;
import de.dzimmermann.rcp.basicplatform.util.Messages;
import de.dzimmermann.rcp.basicplatform.util.PFSCoreIconProvider;

/**
 * Experimental Plug-In navigation to present different approaches then the
 * {@link CNFNavigatorView}.<br>
 * This one is using Eclipse Forms.
 * 
 * @author dzimmermann
 * @since SSDTool 0.4.5
 * @version 0.1
 */
public class FormsPluginNavigatorView extends AbstractPluginNavigatorView {

	/**
	 * The id of this view...
	 */
	public static final String ID = FormsPluginNavigatorView.class.getName();

	/**
	 * The toolkit used to display all Eclipse Forms adapted widgets.
	 */
	private final FormToolkit formToolkit = new FormToolkit(
			Display.getDefault());

	/**
	 * Memento section for the Eclipse Forms navigation
	 */
	private static final String FORMS_MEMENTO = "ssdtool.forms_navigation"; //$NON-NLS-1$
	/**
	 * Memento sub-section for the last opened plugin (Eclipse Forms Section)
	 */
	private static final String SELECTED_PLUGIN = FORMS_MEMENTO
			+ ".selected_plugin"; //$NON-NLS-1$
	/**
	 * The delimeter used to list the last opened plugin sections
	 */
	private static final String SELECTED_PLUGIN_DELIMETER = ";"; //$NON-NLS-1$
	/**
	 * Memento sub-section for the last task, selected within the last opened
	 * plugin
	 * 
	 * @see #SELECTED_PLUGIN
	 */
	private static final String SELECTED_TASK = SELECTED_PLUGIN
			+ ".current_task"; //$NON-NLS-1$

	/**
	 * Key for the pshelfitems widget method setData() and getData() to allow
	 * the usage of multiple datasets per widget<br>
	 * --&gt; the plugin behind the item
	 */
	private static final String FORMS_ITEM_TASK = "task"; //$NON-NLS-1$
	/**
	 * Key for the pshelfitems widget method setData() and getData() to allow
	 * the usage of multiple datasets per widget --&gt; the viewer as displayed
	 * within the items body
	 */
	private static final String FORMS_ITEM_VIEWER = "viewer"; //$NON-NLS-1$

	@Override
	public void init(IViewSite site, IMemento memento) throws PartInitException {
		super.init(site, memento);
	}

	@Override
	public void saveState(IMemento memento) {
		super.saveState(memento);

		IMemento formsMemento = memento.createChild(FORMS_MEMENTO);

		StringBuffer openPlugins = new StringBuffer();

		// iterate over all sections/plugins
		for (int i = 0; i < ((ScrolledForm) pluginNavigator).getBody()
				.getChildren().length; i++) {

			Control c = ((ScrolledForm) pluginNavigator).getBody()
					.getChildren()[i];
			if (c instanceof Section && ((Section) c).isExpanded()) {

				// retrieve each plugins name...
				String pluginName = ((Section) c).getText();
				// ... and append it to the string buffer
				openPlugins.append(pluginName);

				// append delimiter for more to come (possibly...)
				openPlugins.append(SELECTED_PLUGIN_DELIMETER);

				// get the current active task
				// --> there is a special need, that in all sections only one
				// selection may be active (looks better and is the real
				// intention of how this whole stuff should work)
				// --> otherwise the latest selection would overwrite the first
				// one(s)
				if (((Section) c).getData(FORMS_ITEM_VIEWER) instanceof TreeViewer) {
					TreeViewer tv = (TreeViewer) ((Section) c)
							.getData(FORMS_ITEM_VIEWER);
					IStructuredSelection selection = (IStructuredSelection) tv
							.getSelection();
					if (selection != null && !selection.isEmpty()
							&& selection.getFirstElement() instanceof Task) {
						// store the current selected task
						formsMemento.putString(SELECTED_TASK,
								((Task) selection.getFirstElement()).getName());
					}
				}
			}
		}

		// store the current open plugin sections
		formsMemento.putString(SELECTED_PLUGIN, openPlugins.toString());
	}

	@Override
	public void createPartControl(Composite parent) {

		parent.setLayout(new FillLayout(SWT.VERTICAL));

		ScrolledForm scrolledForm = formToolkit.createScrolledForm(parent);
		// formToolkit.paintBordersFor(scrolledForm);
		formToolkit.decorateFormHeading(scrolledForm.getForm());
		scrolledForm.getBody().setLayout(new GridLayout(1, true));

		scrolledForm.setText(Messages
				.getString("FormsPluginNavigatorView.scrolledForm.Text")); //$NON-NLS-1$

		Image img = PFSCoreIconProvider.getImageByIconName("fugue_plug.png", //$NON-NLS-1$
				true);
		scrolledForm.setImage(img);

		pluginNavigator = scrolledForm;

		super.createPartControl(parent);
	}

	/**
	 * When finished with the creation of the visualization, this method allows
	 * to set the selection as it was when this view was closed (on application
	 * shutdown).
	 * 
	 * @since 0.1
	 * @version 0.1
	 * 
	 * @see #saveState(IMemento)
	 */
	@Override
	protected void restoreState(IMemento memento) {

		super.restoreState(memento);

		if (memento == null) {
			return;
		}

		IMemento formsMemento = memento.getChild(FORMS_MEMENTO);

		if (formsMemento == null) {
			return;
		}

		String[] currentPlugin = formsMemento.getString(SELECTED_PLUGIN).split(
				SELECTED_PLUGIN_DELIMETER);
		Set<String> currentPluginSet = new HashSet<String>(
				Arrays.asList(currentPlugin));
		String currentTask = formsMemento.getString(SELECTED_TASK);

		for (Control item : ((ScrolledForm) pluginNavigator).getBody()
				.getChildren()) {

			if (!(item instanceof Section)) {
				continue;
			}

			Assert.isLegal(item.getData(FORMS_ITEM_TASK) instanceof Task);
			Assert.isLegal(item.getData(FORMS_ITEM_VIEWER) instanceof TreeViewer);

			Task t = (Task) item.getData(FORMS_ITEM_TASK);
			TreeViewer tv = (TreeViewer) item.getData(FORMS_ITEM_VIEWER);

			if (currentPluginSet.contains(t.getName())) {
				((Section) item).setExpanded(true);
				StructuredSelection selection = getSelectedTask(currentTask, t,
						false);
				if (selection != null && !selection.isEmpty()) {
					tv.removeSelectionChangedListener(this);
					tv.setSelection(selection);
					tv.addSelectionChangedListener(this);
					tv.getTree().setFocus();
				}
			}
		}
	}

	@Override
	protected void createPluginNavigation(Object navigation,
			Task ssdToolRootTask) {

		for (Task pluginRootTask : ssdToolRootTask.getChildren()) {

			// ### bugfix: if already disabled (via plugin.xml or such manners)
			// there is no need to potentially re-enable the plugin
			if (!pluginRootTask.isEnabled())
				continue;
			// ### end of bugfix ###

			if (BasicPlatformSessionModel.getInstance().getVisiblePlugins() != null
					&& !BasicPlatformSessionModel.getInstance()
							.getVisiblePlugins()
							.contains(pluginRootTask.getTaskPluginID()))
				pluginRootTask.setEnabled(false);

			// ### bugfix: the navigator is working with the tree, because the
			// disabling is handled by the viewers providers. This one is
			// different and needs manual removal of the plugins. ###
			if (!pluginRootTask.isEnabled())
				continue;
			// ### end of bugfix ###

			Section section = formToolkit.createSection(
					((ScrolledForm) navigation).getBody(), Section.TWISTIE
							| Section.TITLE_BAR);
			section.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false,
					1, 1));
			// formToolkit.paintBordersFor(section);
			section.setData(FORMS_ITEM_TASK, pluginRootTask);
			section.setText(pluginRootTask.getName());

			if (pluginRootTask.getDescription() != null
					&& !pluginRootTask.getDescription().isEmpty()) {
				Label label = formToolkit.createLabel(section,
						pluginRootTask.getDescription(), SWT.WRAP);
				section.setDescriptionControl(label);
			}

			Image image = null;

			if (pluginRootTask.getImage() != null) {

				image = pluginRootTask.getImage();

			} else if (pluginRootTask.getImageString() != null) {

				image = PFSCoreIconProvider.getImageByIconName(
						pluginRootTask.getImageString(), true);

				String imageString = pluginRootTask.getImageString();
				if (image == null && imageString != null) {
					ImageDescriptor imgDesc = SSDToolPlugin
							.getImageDescriptor(imageString);
					image = imgDesc != null ? imgDesc.createImage() : null;
				}
			}

			if (image != null) {
				Label label = formToolkit.createLabel(section, "", SWT.NONE); //$NON-NLS-1$
				label.setImage(image);
				section.setTextClient(label);
			}

			createSinglePluginTree(section, pluginRootTask);

			section.addExpansionListener(new IExpansionListener() {
				@Override
				public void expansionStateChanging(ExpansionEvent e) {
				}

				@Override
				public void expansionStateChanged(ExpansionEvent e) {
				}
			});
		}
	}

	@Override
	protected void createSinglePluginTree(final Object pluginItem,
			final Task pluginRootTask) {

		final Composite composite = formToolkit.createComposite(
				((Section) pluginItem), SWT.NONE);
		composite.setLayout(new GridLayout(1, true));
		((Section) pluginItem).setClient(composite);

		final TreeViewer viewer = createTaskTree(composite, pluginRootTask);

		viewer.getTree().setLayoutData(
				new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		((Section) pluginItem).setData(FORMS_ITEM_VIEWER, viewer);
		viewer.setData(FORMS_ITEM_VIEWER, ((Section) pluginItem));

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				if (event.getSelection() == null
						|| event.getSelection().isEmpty()) {
					return;
				}
				Assert.isTrue(((IStructuredSelection) event.getSelection())
						.getFirstElement() instanceof Task);
				Task task = (Task) ((IStructuredSelection) event.getSelection())
						.getFirstElement();
				Assert.isLegal(((TreeViewer) event.getSource())
						.getData(FORMS_ITEM_VIEWER) instanceof Section);
				BasicPlatformStates.toggleTaskState(task);
				fireTaskSelectionEvent(task);
				Section section = (Section) ((TreeViewer) event.getSource())
						.getData(FORMS_ITEM_VIEWER);
				for (int i = 0; i < section.getParent().getChildren().length; i++) {
					if (section.getParent().getChildren()[i] instanceof Section
							&& section.getParent().getChildren()[i] != section) {
						((TreeViewer) section.getParent().getChildren()[i]
								.getData(FORMS_ITEM_VIEWER))
								.setSelection(StructuredSelection.EMPTY);
					}
				}
			}
		});

		viewer.getTree().addTreeListener(new TreeListener() {

			@Override
			public void treeExpanded(TreeEvent e) {
				asyncUpdate();
			}

			@Override
			public void treeCollapsed(TreeEvent e) {
				asyncUpdate();
			}

			private void asyncUpdate() {
				getSite().getShell().getDisplay().asyncExec(new Runnable() {
					@Override
					public void run() {
						((ScrolledForm) pluginNavigator).layout(true, true);
					}
				});
			}
		});
	}

	@Override
	public void dispose() {
		if (((ScrolledForm) pluginNavigator) != null
				&& !((ScrolledForm) pluginNavigator).isDisposed()) {
			((ScrolledForm) pluginNavigator).dispose();
			pluginNavigator = null;
		}
		super.dispose();
	}
}
