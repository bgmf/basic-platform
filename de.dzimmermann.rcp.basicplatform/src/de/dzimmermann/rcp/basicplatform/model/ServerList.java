package de.dzimmermann.rcp.basicplatform.model;

import java.util.LinkedHashMap;

/**
 * This is a simple list of servers, available
 * 
 * @author dzimmermann
 * @version 0.1
 * @since PCP 0.3.0
 */
public class ServerList extends LinkedHashMap<String, String> {

	/**
	 * The serializable id.
	 */
	private static final long serialVersionUID = 1L;

	public void addServer(final String name, final String url) {
		this.put(name, url);
	}
}
