package de.dzimmermann.rcp.basicplatform.util;

import java.net.URLClassLoader;

/**
 * This interface provides a set of methods used to retrieve resources from the
 * PFSRCPCore's resource extension point (
 * {@link PFSCoreConstants#PFS_RCP_CORE_EXTENSION_POINT}).
 * 
 * @author dzimmermann
 * @since PFSRCPCore 0.5.0
 * @version 0.1
 */
public interface PFSCoreResourceProvider {

	public URLClassLoader getResource(String resource) throws Exception;
}
