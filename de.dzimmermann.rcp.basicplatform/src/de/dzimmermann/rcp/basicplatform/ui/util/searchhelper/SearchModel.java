package de.dzimmermann.rcp.basicplatform.ui.util.searchhelper;

import java.util.HashMap;
import java.util.Map;

/**
 * This class contains all search data groups represented in two ways:
 * <ol>
 * <li>search options by target id</li>
 * <li>search options by plugin id</li>
 * </ol>
 * 
 * @author dzimmermann
 * @since PFSRCPCore 0.5.3
 * @version 0.1
 */
public class SearchModel {

	private Map<String, SearchData> targetSearch;
	private Map<String, SearchData> pluginSearch;

	public SearchModel() {
		targetSearch = new HashMap<String, SearchData>();
		pluginSearch = new HashMap<String, SearchData>();
	}

	/**
	 * The method is used to retrieve a map of target-ids(the id of the
	 * contribution)-to-search-data.
	 * 
	 * @return the map containing the search data connected to the target id
	 */
	public Map<String, SearchData> getTargetSearch() {
		return targetSearch;
	}

	/**
	 * The method is used to retrieve a map of plugin-ids(-to-search-data.
	 * 
	 * @return the map containing the search data connected to the plugin id
	 */
	public Map<String, SearchData> getPluginSearch() {
		return pluginSearch;
	}
}
