package de.dzimmermann.rcp.basicplatform.util;

/**
 * Interface to add the possibility to set an editor dirty from an embedded
 * component.<br>
 * Comparable to a Listener-concept.
 * 
 * @author danielz
 * @since PFSRCPCore V0.2.2
 * @version 1.0
 */
public interface DirtyEditorSupport {

	/**
	 * Set an editor to dirty - the implementing editor should do the rest
	 */
	public void setDirty();

	/**
	 * Set an editor to not dirty - the implementing editor should do the rest
	 */
	public void setNotDirty();

	/**
	 * Checks if the editor/widget is dirty and neads some interaction
	 * 
	 * @return <code>true</code>, if the widget is dirty, otherwise it will
	 *         return <code>false</code>
	 */
	public boolean isDirty();
}
