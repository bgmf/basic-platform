package de.dzimmermann.rcp.basicplatform.ui.util.colorprovider;

import java.awt.Color;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;

/**
 * A ColorProvider represents something like a finite state machine that returns
 * new {@link Color}s upon successive calls to the
 * {@link ColorProvider#getNextColor()} method.
 * 
 * @author werner
 * @since V0.2.0
 */
public interface ColorProvider {

	/**
	 * Computes a new {@link Color} based upon the current state of the
	 * <code>ColorProvider</code> and its algorithm and returns it.
	 * 
	 * @return the next <code>Color</code>
	 */
	public Color getNextColor();

	/**
	 * Computes a new <code>int</code> based upon the current state of the
	 * <code>ColorProvider</code> and its algorithm and returns it.
	 * 
	 * @return the next color as a primitive <code>int</code>
	 */
	public int getNextColorRGB();

	/**
	 * Computes a new <code>int</code>-array based upon the current state of the
	 * <code>ColorProvider</code> and its algorithm and returns it.
	 * 
	 * @return the next color as an array of primitive <code>int</code>
	 */
	public int[] getNextColorRGBArray();

	/**
	 * Computes a new {@link SWT} {@link org.eclipse.swt.graphics.Color} based
	 * upon the current state of the <code>ColorProvider</code> and its
	 * algorithm and returns it.
	 * 
	 * @param device
	 *            the {@link Device} on wich this color should be allocated
	 * @return the next color as an <code>SWT Color</code> object
	 */
	public org.eclipse.swt.graphics.Color getNextColorSWT(Device device);
}
