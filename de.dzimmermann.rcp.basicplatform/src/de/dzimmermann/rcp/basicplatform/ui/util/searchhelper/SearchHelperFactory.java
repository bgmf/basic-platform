package de.dzimmermann.rcp.basicplatform.ui.util.searchhelper;

/**
 * This interface is used to propagate the search option and value to an
 * interested party.
 * 
 * @author dzimmermann
 * 
 * @since PFSRCPCore 0.5.3
 * @version 0.1
 */
public interface SearchHelperFactory {

	public void setOptionName(String optionName);

	public String getOptionName();

	public void setSearchValue(String searchValue);

	public String getSearchValue();

	public void refresh();
}
