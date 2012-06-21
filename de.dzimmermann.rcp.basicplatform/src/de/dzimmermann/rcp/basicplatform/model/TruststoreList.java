package de.dzimmermann.rcp.basicplatform.model;

import java.util.HashMap;

/**
 * A simple Map for Plugin-Name/Internal-Path pairs.
 * 
 * @author dzimmermann
 * @since SSDTool (PCP) 0.4.2
 * @version 0.1
 */
public class TruststoreList extends HashMap<String, TrustStore> {

	/**
	 * the serial uid
	 */
	private static final long serialVersionUID = 1L;

	public void addTrustStore(String plugin, TrustStore trustStore) {
		this.put(plugin, trustStore);
	}

	public void addTrustStore(String plugin, String internalPath) {
		this.put(plugin, new TrustStore(internalPath, null));
	}

	public void addTrustStore(String plugin, String internalPath,
			String optionalPassword) {
		this.put(plugin, new TrustStore(internalPath, optionalPassword));
	}
}
