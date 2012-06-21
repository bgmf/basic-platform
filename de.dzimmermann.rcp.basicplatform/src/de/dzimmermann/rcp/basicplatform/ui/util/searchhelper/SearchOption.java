package de.dzimmermann.rcp.basicplatform.ui.util.searchhelper;

/**
 * This class represents an option.<br>
 * While the {@code name} attribute describes an unique identifier, the {@code
 * label} attribute is used to create a human readable name. <br>
 * If a plugin is using this approach, it must know how to handle the name...
 * 
 * 
 * @author dzimmermann
 * @since PFSRCPCore 0.5.3
 * @version 0.1
 */
public class SearchOption {

	protected String name;
	protected String label;

	public SearchOption(String name, String label) {
		this.name = name;
		this.label = label;
	}

	public String getName() {
		return name;
	}

	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return String.format("%s : %s", name, label);
	}
}
