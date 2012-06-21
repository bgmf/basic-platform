package de.dzimmermann.rcp.pwm.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.ISourceProviderService;

public class PWMStates extends AbstractSourceProvider {

	// states
	public static final String STATE_TRUE = "TRUE";
	public static final String STATE_FALSE = "FALSE";

	// variables
	public static final String SUBGROUP_SELECTED = "de.zimmermann.rcp.pwm.subgroup.selected.state";
	public static final String ENTRIES_AVAILABLE = "de.zimmermann.rcp.pwm.entries.available.state";
	public static final String ENTRY_SELECTED = "de.zimmermann.rcp.pwm.entry.selected.state";

	// local data

	public static final String[] PROVIDES_SOURCE_NAMES = { SUBGROUP_SELECTED,
			ENTRIES_AVAILABLE, ENTRY_SELECTED };

	public static Map<String, String> state_map;
	static {
		state_map = new HashMap<String, String>();
		state_map.put(SUBGROUP_SELECTED, STATE_FALSE);
		state_map.put(ENTRIES_AVAILABLE, STATE_FALSE);
		state_map.put(ENTRY_SELECTED, STATE_FALSE);
	}

	@Override
	public void dispose() {
		state_map.clear();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map getCurrentState() {
		return state_map;
	}

	@Override
	public String[] getProvidedSourceNames() {
		return PROVIDES_SOURCE_NAMES;
	}

	// SUBGROUP_SELECTED

	private boolean subgroupSelected = false;

	private void subgroupSelected(boolean enabled) {
		subgroupSelected = enabled;
		state_map.put(SUBGROUP_SELECTED, (subgroupSelected ? STATE_TRUE
				: STATE_FALSE));
		fireSourceChanged(ISources.WORKBENCH, SUBGROUP_SELECTED,
				state_map.get(SUBGROUP_SELECTED));
	}

	public static void subgroupSelectedState(boolean enabled) {
		ISourceProviderService sourceProviderService = (ISourceProviderService) PlatformUI
				.getWorkbench().getActiveWorkbenchWindow()
				.getService(ISourceProviderService.class);
		PWMStates stateService = (PWMStates) sourceProviderService
				.getSourceProvider(PWMStates.SUBGROUP_SELECTED);
		stateService.subgroupSelected(enabled);
	}

	public static boolean getSubgroupSelectedState() {
		ISourceProviderService sourceProviderService = (ISourceProviderService) PlatformUI
				.getWorkbench().getActiveWorkbenchWindow()
				.getService(ISourceProviderService.class);
		PWMStates stateService = (PWMStates) sourceProviderService
				.getSourceProvider(PWMStates.SUBGROUP_SELECTED);
		return stateService.subgroupSelected;
	}

	// ENTRIES_AVAILABLE

	private boolean entriesEnabled = false;

	private void enableEntries(boolean enabled) {
		entriesEnabled = enabled;
		state_map.put(ENTRIES_AVAILABLE, (entriesEnabled ? STATE_TRUE
				: STATE_FALSE));
		fireSourceChanged(ISources.WORKBENCH, ENTRIES_AVAILABLE,
				state_map.get(ENTRIES_AVAILABLE));
	}

	public static void enableEntriesState(boolean enabled) {
		ISourceProviderService sourceProviderService = (ISourceProviderService) PlatformUI
				.getWorkbench().getActiveWorkbenchWindow()
				.getService(ISourceProviderService.class);
		PWMStates stateService = (PWMStates) sourceProviderService
				.getSourceProvider(PWMStates.ENTRIES_AVAILABLE);
		stateService.enableEntries(enabled);
	}

	public static boolean getEnableEntriesState() {
		ISourceProviderService sourceProviderService = (ISourceProviderService) PlatformUI
				.getWorkbench().getActiveWorkbenchWindow()
				.getService(ISourceProviderService.class);
		PWMStates stateService = (PWMStates) sourceProviderService
				.getSourceProvider(PWMStates.ENTRIES_AVAILABLE);
		return stateService.entriesEnabled;
	}

	// ENTRY_SELECTED

	private boolean entrySelected = false;

	private void entrySelected(boolean enabled) {
		entrySelected = enabled;
		state_map.put(ENTRY_SELECTED,
				(entrySelected ? STATE_TRUE : STATE_FALSE));
		fireSourceChanged(ISources.WORKBENCH, ENTRY_SELECTED,
				state_map.get(ENTRY_SELECTED));
	}

	public static void entrySelectedState(boolean enabled) {
		ISourceProviderService sourceProviderService = (ISourceProviderService) PlatformUI
				.getWorkbench().getActiveWorkbenchWindow()
				.getService(ISourceProviderService.class);
		PWMStates stateService = (PWMStates) sourceProviderService
				.getSourceProvider(PWMStates.ENTRY_SELECTED);
		stateService.entrySelected(enabled);
	}

	public static boolean getEntrySelectedState() {
		ISourceProviderService sourceProviderService = (ISourceProviderService) PlatformUI
				.getWorkbench().getActiveWorkbenchWindow()
				.getService(ISourceProviderService.class);
		PWMStates stateService = (PWMStates) sourceProviderService
				.getSourceProvider(PWMStates.ENTRY_SELECTED);
		return stateService.entrySelected;
	}
}
