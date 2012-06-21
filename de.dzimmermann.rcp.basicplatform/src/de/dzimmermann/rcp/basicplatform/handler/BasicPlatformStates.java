package de.dzimmermann.rcp.basicplatform.handler;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.ISourceProviderService;

import de.dzimmermann.rcp.basicplatform.model.Task;

public class BasicPlatformStates extends AbstractSourceProvider {

	public static final String DEFAULT_ACTIVITIES_ENABLED_STATE = "de.dzimmermann.rcp.basicplatform.activities.default"; //$NON-NLS-1$
	public static final String ENABLED = "ENABLED"; //$NON-NLS-1$
	public static final String DISABLED = "DISABLED"; //$NON-NLS-1$
	private boolean activitiesDefaultEnabled = false;

	public static final String CURRENT_TASK_ID_STATE = "de.dzimmermann.rcp.basicplatform.task.id"; //$NON-NLS-1$
	public static final String EMPTY = "EMPTY"; //$NON-NLS-1$
	private Task selectedTask = null;

	public static final String[] PROVIDES_SOURCE_NAMES = {
			DEFAULT_ACTIVITIES_ENABLED_STATE, CURRENT_TASK_ID_STATE };

	public static Map<String, String> state_map;
	static {
		state_map = new HashMap<String, String>();
		state_map.put(DEFAULT_ACTIVITIES_ENABLED_STATE, DISABLED);
		state_map.put(CURRENT_TASK_ID_STATE, EMPTY);
	}

	@Override
	public void dispose() {
		state_map.clear();
	}

	@Override
	public String[] getProvidedSourceNames() {
		return PROVIDES_SOURCE_NAMES;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map getCurrentState() {
		return state_map;
	}

	private void toggleDefaultActivities(boolean enabled) {
		activitiesDefaultEnabled = enabled;
		state_map.put(DEFAULT_ACTIVITIES_ENABLED_STATE,
				(activitiesDefaultEnabled ? ENABLED : DISABLED));
		fireSourceChanged(ISources.WORKBENCH, DEFAULT_ACTIVITIES_ENABLED_STATE,
				state_map.get(DEFAULT_ACTIVITIES_ENABLED_STATE));
	}

	public static void toggleDefaultActivitiesState(boolean newState) {
		// Get the source provider service
		ISourceProviderService sourceProviderService = (ISourceProviderService) PlatformUI
				.getWorkbench().getActiveWorkbenchWindow()
				.getService(ISourceProviderService.class);
		// now retrieve the service
		BasicPlatformStates stateService = (BasicPlatformStates) sourceProviderService
				.getSourceProvider(BasicPlatformStates.DEFAULT_ACTIVITIES_ENABLED_STATE);
		stateService.toggleDefaultActivities(newState);
	}

	public static boolean getDefaultActivitiesState() {
		// Get the source provider service
		ISourceProviderService sourceProviderService = (ISourceProviderService) PlatformUI
				.getWorkbench().getActiveWorkbenchWindow()
				.getService(ISourceProviderService.class);
		// now retrieve the service
		BasicPlatformStates stateService = (BasicPlatformStates) sourceProviderService
				.getSourceProvider(BasicPlatformStates.DEFAULT_ACTIVITIES_ENABLED_STATE);
		return stateService.activitiesDefaultEnabled;
	}

	private void toggleTask(Task task) {
		selectedTask = task;
		state_map.put(
				CURRENT_TASK_ID_STATE,
				(task == null ? EMPTY : task.getId() == null ? EMPTY : task
						.getId()));
		fireSourceChanged(ISources.WORKBENCH, CURRENT_TASK_ID_STATE,
				state_map.get(CURRENT_TASK_ID_STATE));
	}

	public static void toggleTaskState(Task task) {
		// Get the source provider service
		ISourceProviderService sourceProviderService = (ISourceProviderService) PlatformUI
				.getWorkbench().getActiveWorkbenchWindow()
				.getService(ISourceProviderService.class);
		// now retrieve the service
		BasicPlatformStates stateService = (BasicPlatformStates) sourceProviderService
				.getSourceProvider(BasicPlatformStates.CURRENT_TASK_ID_STATE);
		stateService.toggleTask(task);
	}

	public static Task getTaskState() {
		// Get the source provider service
		ISourceProviderService sourceProviderService = (ISourceProviderService) PlatformUI
				.getWorkbench().getActiveWorkbenchWindow()
				.getService(ISourceProviderService.class);
		// now retrieve the service
		BasicPlatformStates stateService = (BasicPlatformStates) sourceProviderService
				.getSourceProvider(BasicPlatformStates.CURRENT_TASK_ID_STATE);
		return stateService.selectedTask;
	}
}
