package de.dzimmermann.rcp.basicplatform.ui.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.eclipse.ui.part.ViewPart;

import de.dzimmermann.rcp.basicplatform.SSDToolPlugin;
import de.dzimmermann.rcp.basicplatform.model.BasicPlatformSessionModel;
import de.dzimmermann.rcp.basicplatform.model.PCPApplicationMode;
import de.dzimmermann.rcp.basicplatform.model.Task;
import de.dzimmermann.rcp.basicplatform.model.TaskGroupRestriction;
import de.dzimmermann.rcp.basicplatform.model.TaskSortingType;
import de.dzimmermann.rcp.basicplatform.services.IAvailablePluginService;
import de.dzimmermann.rcp.basicplatform.services.IGroupService;
import de.dzimmermann.rcp.basicplatform.ui.util.Logger;
import de.dzimmermann.rcp.basicplatform.ui.util.TaskTreeContentProvider;
import de.dzimmermann.rcp.basicplatform.ui.util.TaskTreeLabelProvider;
import de.dzimmermann.rcp.basicplatform.ui.view.action.OpenTaskRelatedWidgetCNFAction;
import de.dzimmermann.rcp.basicplatform.util.SSDToolUtils;

/**
 * Experimental Plug-In navigation to present different approaches then the
 * {@link CNFNavigatorView}.<br>
 * This one is using pShelfs and uses a MS Outlook-like presentation.
 * 
 * @author dzimmermann
 * @since SSDTool 0.1.0
 * @version 0.3
 */
public abstract class AbstractPluginNavigatorView extends ViewPart implements
		ISelectionChangedListener, IDoubleClickListener {

	/**
	 * the memento from the last session
	 * 
	 * @since 0.2
	 */
	private IMemento memento;

	/**
	 * The comparator used to order the tasks.
	 */
	private final TaskComparator taskComparator;

	/**
	 * The object the plugin navigation will be displayed on.
	 */
	protected Object pluginNavigator;

	public AbstractPluginNavigatorView() {
		taskComparator = new TaskComparator();
	}

	@Override
	public void init(IViewSite site, IMemento memento) throws PartInitException {
		super.init(site, memento);
		this.memento = memento;
	}

	public void setPluginNavigator(Object pluginNavigator) {
		this.pluginNavigator = pluginNavigator;
	}

	/**
	 * Call this method, <b>after</b> creating a {@link #pluginNavigator}
	 * object!
	 */
	@Override
	public void createPartControl(Composite parent) {

		Task ssdToolRootTask = SSDToolPlugin.getDefault().getSSDToolModel()
				.getRootTask();

		if (ssdToolRootTask == null)
			return;

		// check available plugins from server
		try {
			IAvailablePluginService service = SSDToolUtils
					.getAvailablePluginService();
			if (service != null) {
				List<String> availablePlugins = null;
				boolean isDeveloper = service.isDeveloper();
				if (isDeveloper && SSDToolUtils.isDeveloperMode()) {
					availablePlugins = new ArrayList<String>();
					for (Task t : ssdToolRootTask.getChildren()) {
						availablePlugins.add(t.getTaskPluginID());
					}
				} else {
					availablePlugins = service.availablePlugins();
				}
				if (availablePlugins == null)
					BasicPlatformSessionModel.getInstance().setVisiblePlugins(
							null);
				else
					BasicPlatformSessionModel.getInstance().setVisiblePlugins(
							new HashSet<String>(availablePlugins));
			} else {
				BasicPlatformSessionModel.getInstance().setVisiblePlugins(null);
			}
		} catch (Exception e) {
			Logger.logError(e);
		}

		// restrict by plugin.xml configuration
		try {
			boolean isDeveloper = false;
			IAvailablePluginService service = SSDToolUtils
					.getAvailablePluginService();
			if (service != null)
				isDeveloper = service.isDeveloper();
			IGroupService service2 = SSDToolUtils.getGroupService();
			List<String> groups = null;
			if (service2 != null)
				groups = service2.getAssignedGroups();
			if (groups != null) {
				for (Task t : ssdToolRootTask.getChildren()) {
					if (t.getGroupRestrictions() == null
							|| t.getGroupRestrictions().isEmpty())
						continue;
					boolean block = false;
					for (TaskGroupRestriction tgr : t.getGroupRestrictions()) {
						block = tgr.isInverse() ? groups
								.contains(tgr.getName()) : !groups.contains(tgr
								.getName());
						if (block)
							break;
					}
					if (BasicPlatformSessionModel.getInstance()
							.getVisiblePlugins() == null) {
						if (t.isEnabled())
							t.setEnabled(!block || isDeveloper);
					} else {
						if (block
								&& BasicPlatformSessionModel.getInstance()
										.getVisiblePlugins()
										.contains(t.getTaskPluginID())
								&& !isDeveloper)
							BasicPlatformSessionModel.getInstance()
									.getVisiblePlugins()
									.remove(t.getTaskPluginID());
					}
				}
			}
		} catch (Exception e) {
			Logger.logError(e);
		}

		ssdToolRootTask.setSortChildren(TaskSortingType.ASCENDING);
		orderTasks(ssdToolRootTask);

		createPluginNavigation(pluginNavigator, ssdToolRootTask);

		createActions();
		initializeToolBar();
		initializeMenu();

		restoreState(memento);

		getSite().setSelectionProvider(selectionProvider);
	}

	/**
	 * a little helper to order all tasks (recursive call for all children)
	 * 
	 * @param root
	 *            the task, which children shall be ordered
	 */
	private void orderTasks(Task root) {
		if (root.getChildren() != null) {
			taskComparator.sorting = root.getSortChildren() == null ? TaskSortingType.ASCENDING
					: root.getSortChildren();
			Collections.sort(root.getChildren(), taskComparator);
			for (Task child : root.getChildren()) {
				orderTasks(child);
			}
		}
	}

	/**
	 * When finished with the creation of the visualization, this method allows
	 * to set the selection as it was when this view was closed (on application
	 * shutdown).<br>
	 * This default implementation does nothing.
	 * 
	 * @since 0.2
	 * @version 0.2
	 * 
	 * @see #saveState(IMemento)
	 */
	protected void restoreState(IMemento memento) {
		if (memento == null) {
			return;
		}
	}

	/**
	 * This method creates the {@link StructuredSelection} for the viewer within
	 * the body of each item.<br>
	 * Used to restore the last state of the shelf.
	 * 
	 * @param selectedTaskName
	 *            the name of the task, that was open the last time
	 * @param t
	 *            the task to check
	 * @param include
	 *            Decide whether or not the given tasks name should be checked
	 *            as well. This is used because the method will be called
	 *            recursively to check each tasks child.
	 * @return the {@link StructuredSelection} object with the last active
	 *         {@link Task}, or <code>null</code> - indicating, that no task was
	 *         selected
	 * 
	 * @since 0.3
	 * 
	 * @see #restoreState()
	 */
	protected StructuredSelection getSelectedTask(String selectedTaskName,
			Task t, boolean include) {
		StructuredSelection selection = null;
		if (include && t.getName().equals(selectedTaskName)) {
			selection = new StructuredSelection(t);
		} else {
			if (t.getChildren() != null) {
				for (Task t2 : t.getChildren()) {
					selection = getSelectedTask(selectedTaskName, t2, true);
					if (selection != null) {
						break;
					}
				}
			}
		}
		return selection;
	}

	/**
	 * This method created the plugin items on the main shelf. Each plugin is
	 * visualized by using a dedicated shelf item.
	 * 
	 * @param navigation
	 *            the main object on which the plugin items will be displayed
	 * @param ssdToolRootTask
	 *            the PCPs plugin root
	 */
	protected abstract void createPluginNavigation(Object navigation,
			Task ssdToolRootTask);

	/**
	 * This method displays each plugins sub tasks within a tree viewer (quite
	 * similar to the default navigation).<br>
	 * Be aware, that the plugins root has no option to being used as a task to
	 * open a editor or something like this as well!
	 * 
	 * @param pluginItem
	 *            the item on which the tree will be displayed
	 * @param pluginRootTask
	 *            the task, indicating the root of each plugin
	 */
	protected abstract void createSinglePluginTree(final Object pluginItem,
			final Task pluginRootTask);

	protected TreeViewer createTaskTree(final Composite parent,
			final Task pluginRootTask) {

		final TreeViewer viewer = new TreeViewer(parent, SWT.FULL_SELECTION);
		viewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);

		// XXX handling of integrated tasks (task with parent-id)
		// see the implementations of this navigation
		TaskTreeContentProvider contentProvider = new InternalTaskTreeContentProvider();
		viewer.setContentProvider(contentProvider);

		ColumnViewerToolTipSupport.enableFor(viewer, ToolTip.NO_RECREATE);
		// FancyToolTipSupport.enableFor(viewer, ToolTip.NO_RECREATE);

		TaskTreeLabelProvider labelProvider = new TaskTreeLabelProvider();
		viewer.setLabelProvider(labelProvider);

		viewer.setInput(pluginRootTask);

		viewer.addDoubleClickListener(AbstractPluginNavigatorView.this);
		viewer.addSelectionChangedListener(AbstractPluginNavigatorView.this);

		// TODO Handle open on Hover! See PFSCoreUtils.isSelectOnHover()

		if (pluginRootTask.getHelpContextId() != null
				&& !pluginRootTask.getHelpContextId().isEmpty()) {
			IWorkbenchHelpSystem helpSystem = getSite().getWorkbenchWindow()
					.getWorkbench().getHelpSystem();
			helpSystem.setHelp(parent, pluginRootTask.getHelpContextId());
			helpSystem.setHelp(viewer.getTree(),
					pluginRootTask.getHelpContextId());
		}

		return viewer;
	}

	/**
	 * Create the actions.
	 */
	protected void createActions() {
	}

	/**
	 * Initialize the toolbar.
	 */
	protected void initializeToolBar() {
		@SuppressWarnings("unused")
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	protected void initializeMenu() {
		@SuppressWarnings("unused")
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
	}

	@Override
	public void setFocus() {
	}

	@Override
	public void doubleClick(final DoubleClickEvent event) {
		if (!SSDToolUtils.isOpenOnSingleClick()) {
			if (event.getSource() instanceof StructuredViewer) {
				OpenTaskRelatedWidgetCNFAction openAction = new OpenTaskRelatedWidgetCNFAction(
						getSite().getPage(), event.getSelection(),
						(StructuredViewer) event.getSource());
				openAction.run();
			}
		}
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		if (SSDToolUtils.isOpenOnSingleClick()) {
			if (event.getSource() instanceof StructuredViewer) {
				OpenTaskRelatedWidgetCNFAction openAction = new OpenTaskRelatedWidgetCNFAction(
						getSite().getPage(), event.getSelection(),
						(StructuredViewer) event.getSource());
				openAction.run();
			}
		}
	}

	@SuppressWarnings("unused")
	private static class FancyToolTipSupport extends ColumnViewerToolTipSupport {

		protected FancyToolTipSupport(ColumnViewer viewer, int style,
				boolean manualActivation) {
			super(viewer, style, manualActivation);
		}

		protected Composite createToolTipContentArea(Event event,
				Composite parent) {
			Composite comp = new Composite(parent, SWT.NONE);
			GridLayout l = new GridLayout(1, false);
			l.horizontalSpacing = 0;
			l.marginWidth = 0;
			l.marginHeight = 0;
			l.verticalSpacing = 0;

			comp.setLayout(l);
			Browser browser = new Browser(comp, SWT.BORDER);
			browser.setText(getText(event));
			browser.setLayoutData(new GridData(200, 150));

			return comp;
		}

		public boolean isHideOnMouseDown() {
			return false;
		}

		public static final void enableFor(ColumnViewer viewer, int style) {
			new FancyToolTipSupport(viewer, style, false);
		}
	}

	private class InternalTaskTreeContentProvider extends
			TaskTreeContentProvider {

		@Override
		public Object[] getChildren(Object parentElement) {
			if (PCPApplicationMode.RAP == BasicPlatformSessionModel
					.getInstance().getApplicationMode()) {
				Set<String> visiblePlugins = BasicPlatformSessionModel
						.getInstance().getVisiblePlugins();
				List<Task> parentChildren = ((Task) parentElement)
						.getChildren();
				List<Task> newTasks = new LinkedList<Task>();
				if (parentChildren != null)
					for (int i = 0; i < parentChildren.size(); i++) {
						Task child = parentChildren.get(i);
						boolean isVisible = false;
						if (child.getParentTaskID() != null
								&& !child.getParentTaskID().isEmpty()
								&& visiblePlugins.contains(child
										.getParentTaskID())
								&& visiblePlugins.contains(child.getId()))
							isVisible = true;
						if (isVisible && child.isEnabled())
							newTasks.add(child);
					}
				return newTasks.toArray(new Task[0]);
			} else {
				return super.getChildren(parentElement);
			}
		}
	}

	protected TaskSelection selection = null;

	public TaskSelection getSelection() {
		return selection;
	}

	private final TaskSelectionProvider selectionProvider = new TaskSelectionProvider();

	protected void fireTaskSelectionEvent(Task task) {
		selection = new TaskSelection(task);
		selectionProvider.fireEvent();
	}

	public final class TaskSelectionProvider implements ISelectionProvider {

		private List<ISelectionChangedListener> listener = new ArrayList<ISelectionChangedListener>();

		@Override
		public void addSelectionChangedListener(
				ISelectionChangedListener listener) {
			if (!this.listener.contains(listener))
				this.listener.add(listener);
		}

		@Override
		public void removeSelectionChangedListener(
				ISelectionChangedListener listener) {
			if (this.listener.contains(listener))
				this.listener.remove(listener);
		}

		@Override
		public ISelection getSelection() {
			return selection;
		}

		@Override
		public void setSelection(ISelection selection) {
			Assert.isTrue(selection instanceof TaskSelection);
			AbstractPluginNavigatorView.this.selection = (TaskSelection) selection;
		}

		protected void fireEvent() {
			for (ISelectionChangedListener l : listener) {
				l.selectionChanged(new SelectionChangedEvent(this, selection));
			}
		}
	}

	public final class TaskSelection implements ISelection,
			IStructuredSelection {

		private final Task selectedTask;

		public TaskSelection(final Task selectedTask) {
			this.selectedTask = selectedTask;
		}

		@Override
		public boolean isEmpty() {
			return selectedTask == null;
		}

		@Override
		public int size() {
			return selectedTask == null ? 0 : 1;
		}

		@Override
		public Object getFirstElement() {
			return selectedTask;
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Iterator iterator() {
			return selectedTask == null ? null : Collections.singletonList(
					selectedTask).iterator();
		}

		@Override
		public Object[] toArray() {
			return selectedTask == null ? null : Collections.singletonList(
					selectedTask).toArray();
		}

		@SuppressWarnings("rawtypes")
		@Override
		public List toList() {
			return selectedTask == null ? null : Collections
					.singletonList(selectedTask);
		}
	}

	private final class TaskComparator implements Comparator<Task> {

		protected TaskSortingType sorting;

		@Override
		public int compare(Task o1, Task o2) {
			int result = 0;
			if (sorting != null) {
				switch (sorting) {
				case ASCENDING:
					result = o1.getName().compareTo(o2.getName());
					break;
				case DESCENDING:
					result = (o1.getName().compareTo(o2.getName())) * (-1);
					break;
				case NO_SORTING:
					result = 0;
					break;
				}
			}
			return result;
		}
	}
}
