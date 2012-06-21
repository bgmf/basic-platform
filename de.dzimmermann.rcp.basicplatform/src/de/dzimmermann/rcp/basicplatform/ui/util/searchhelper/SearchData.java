package de.dzimmermann.rcp.basicplatform.ui.util.searchhelper;

import java.util.ArrayList;
import java.util.List;


/**
 * This is the model as it was retrieved from the extension point.<br>
 * Note, that this describes only the search stuff for exactly one plugin.
 * 
 * @author dzimmermann
 * @since PFSRCPCore 0.5.3
 * @version 0.1
 */
public class SearchData {

	protected String targetId;
	protected String searchMessage;

	protected SearchHelperFactory helperFactory;

	protected boolean hintSearch;
	protected boolean hintCancel;

	protected List<SearchOption> options;

	public SearchData(String targetId, String searchMessage,
			SearchHelperFactory helperFactory) {
		this.targetId = targetId;
		this.searchMessage = searchMessage;
		this.helperFactory = helperFactory;
	}

	public String getTargetId() {
		return targetId;
	}

	public String getSearchMessage() {
		return searchMessage;
	}

	public SearchHelperFactory getHelperFactory() {
		return helperFactory;
	}

	public void setHintSearch(boolean hintSearch) {
		this.hintSearch = hintSearch;
	}

	public boolean isHintSearch() {
		return hintSearch;
	}

	public void setHintCancel(boolean hintCancel) {
		this.hintCancel = hintCancel;
	}

	public boolean isHintCancel() {
		return hintCancel;
	}

	public List<SearchOption> getOptions() {
		if (options == null) {
			options = new ArrayList<SearchOption>();
		}
		return options;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (SearchOption o : options) {
			sb.append("\t" + o.toString() + "\n");
		}
		String options = sb.toString();
		options.substring(0, options.length() - 2);
		return String.format("SearchData [%ntarget-id = %s%nsearch-msg = %s%n"
				+ "helper-factory=[%s]%n"
				+ "hint-search = %b%nhint-cancel = %b%n"
				+ "search-options = [%n%s%n\t]%n]", targetId, searchMessage,
				helperFactory.toString(), hintSearch, hintCancel, options);
	}
}
