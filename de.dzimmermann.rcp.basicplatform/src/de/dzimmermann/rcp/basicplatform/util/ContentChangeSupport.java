package de.dzimmermann.rcp.basicplatform.util;

/**
 * used by the special content groupes within the special content dialog, which
 * implements the methods listed below
 * 
 * @author danielz
 */
public interface ContentChangeSupport {

	/**
	 * a calling object will force a certain behaviour on the implementing side.
	 * comparable to the Listener-concept
	 * 
	 * @param content
	 */
	public void setContent(String content);
}
