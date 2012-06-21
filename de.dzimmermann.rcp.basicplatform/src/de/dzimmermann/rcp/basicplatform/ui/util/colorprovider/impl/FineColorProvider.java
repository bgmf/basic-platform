package de.dzimmermann.rcp.basicplatform.ui.util.colorprovider.impl;

import java.awt.Color;

import org.eclipse.swt.graphics.Device;

import de.dzimmermann.rcp.basicplatform.ui.util.colorprovider.ColorProvider;

/**
 * Beside its name this {@link ColorProvider} is a very primitive one.<br>
 * Starting from a given color (or 0 if no was given) it increments a number
 * from 0 to 16581375 by a specified number (or one if noe was specified).
 * 
 * @author danielz
 * @since PFSRCPCore V0.2.2
 */
public class FineColorProvider implements ColorProvider {

	/**
	 * this is the value the current color will be shifted to provide the next
	 * one
	 */
	private final int increment;

	/**
	 * this value indicated the current color as an int values and can only be
	 * between 0 and 16581375
	 */
	private int currentColorValue;

	/**
	 * this is the default value for shifting the color value
	 */
	private final static int DEFAULT_COLOR_INCREMENT = 1;
	/**
	 * the is the default value for the color to start with
	 */
	private final static Color DEFAULT_START_COLOR = new Color(0);

	/**
	 * the default constructor used the defined values
	 * {@link #DEFAULT_COLOR_INCREMENT}={@value #DEFAULT_COLOR_INCREMENT} and
	 * {@link #DEFAULT_START_COLOR}={@value #DEFAULT_START_COLOR}
	 */
	public FineColorProvider() {
		this(DEFAULT_START_COLOR, DEFAULT_COLOR_INCREMENT);
	}

	/**
	 * using the given {@link Color} to start with, the incrementation will be
	 * set to {@link #DEFAULT_COLOR_INCREMENT}={@value #DEFAULT_COLOR_INCREMENT}
	 * 
	 * @param startColor
	 *            the {@link Color} to start with
	 */
	public FineColorProvider(Color startColor) {
		this(startColor, DEFAULT_COLOR_INCREMENT);
	}

	/**
	 * using the given increment value, the color to start with will be
	 * {@link #DEFAULT_START_COLOR}={@value #DEFAULT_START_COLOR}
	 * 
	 * @param increment
	 *            the value to increment with
	 */
	public FineColorProvider(int increment) {
		this(DEFAULT_START_COLOR, increment);
	}

	/**
	 * this is the basic constructor, which set the {@link Color} to start with
	 * and the incrementation value
	 * 
	 * @param startColor
	 *            the {@link Color} to start with
	 * @param increment
	 *            the value to increment with
	 */
	public FineColorProvider(Color startColor, int increment) {
		this.currentColorValue = startColor.getRGB();
		this.increment = increment;
	}

	@Override
	public Color getNextColor() {

		Color returnColor = new Color(currentColorValue);

		if ((currentColorValue + increment) <= 16581375)
			currentColorValue += increment;
		else {
			int overlap = currentColorValue + increment - 16581375;
			currentColorValue = overlap;
		}

		return returnColor;
	}

	@Override
	public int getNextColorRGB() {
		return getNextColor().getRGB();
	}

	@Override
	public int[] getNextColorRGBArray() {
		Color color = getNextColor();
		int[] rgb = { color.getRed(), color.getGreen(), color.getBlue() };
		return rgb;
	}

	@Override
	public org.eclipse.swt.graphics.Color getNextColorSWT(Device device) {
		int[] rgb = getNextColorRGBArray();
		return new org.eclipse.swt.graphics.Color(device, rgb[0], rgb[1],
				rgb[2]);
	}
}
