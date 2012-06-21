package de.dzimmermann.rcp.basicplatform.ui.util.colorprovider.impl;

import java.awt.Color;
import java.util.Random;

import org.eclipse.swt.graphics.Device;

import de.dzimmermann.rcp.basicplatform.ui.util.colorprovider.ColorProvider;

/**
 * This is a lightwight implementation of the {@link ColorProvider} interface
 * providing random colors generated on the fly.
 * 
 * @author danielz
 * @since PFSRCPCore V0.2.2
 */
public class RandomColorProvider implements ColorProvider {

	/**
	 * this value indicated the current color as an int values and can only be
	 * between 0 and 16581375
	 */
	private int currentColorValue;

	/**
	 * this class does not need any difficult contructor, so this is the default
	 * one
	 */
	public RandomColorProvider() {
		currentColorValue = 0;
	}

	@Override
	public Color getNextColor() {

		Random random = new Random();
		currentColorValue = random.nextInt(16581375);

		Color returnColor = new Color(currentColorValue);

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
