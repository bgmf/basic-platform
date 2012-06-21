package de.dzimmermann.rcp.basicplatform.ui.util.colorprovider.impl;

import java.awt.Color;

import org.eclipse.swt.graphics.Device;

import de.dzimmermann.rcp.basicplatform.ui.util.colorprovider.ColorProvider;

/**
 * This ColorProvider produces only 'pure' colors. A pure Color is considered
 * having only one color component (red, green, blue) not zero. If one component
 * reaches 255 it gets zeroed and the next component gets incremented. The order
 * is blue, green, red.
 * 
 * @author werner
 * @since PFSRCPCore V0.2.0
 */
public class DefaultColorProvider implements ColorProvider {

	private final int increment;

	private int currentColorValue;

	private ColorPosition currentColorPosition;

	private final static int DEFAULT_COLOR_INCREMENT = 1;

	private final static Color DEFAULT_START_COLOR = new Color(0);

	public DefaultColorProvider() {
		this(DEFAULT_START_COLOR, DEFAULT_COLOR_INCREMENT);
	}

	/**
	 * This color should be pure (see general description) or the results might
	 * look a bit funny.
	 * 
	 * @param startColor
	 */
	public DefaultColorProvider(Color startColor) {
		this(startColor, DEFAULT_COLOR_INCREMENT);
	}

	public DefaultColorProvider(int increment) {
		this(DEFAULT_START_COLOR, increment);
	}

	public DefaultColorProvider(Color startColor, int increment) {
		this.currentColorValue = startColor.getRGB();
		this.increment = increment;
		this.currentColorPosition = ColorPosition.BLUE;
	}

	@Override
	public Color getNextColor() {
		Color returnColor = new Color(currentColorValue);

		int colorSaturation = (currentColorValue & currentColorPosition
				.getColorMask()) >> currentColorPosition.getShiftValue();

		if (colorSaturation + this.increment >= 255) {
			this.currentColorValue = 0;
			this.currentColorPosition = currentColorPosition
					.getNextColorPosition();
		}

		currentColorValue += (currentColorPosition.getIncrement() * this.increment);

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

	/**
	 * A private enumeration that helps cycling the colors and provides values
	 * for some shift operations and the like.
	 * 
	 * @author werner
	 * 
	 */
	private enum ColorPosition {
		BLUE(0), GREEN(8), RED(16);

		private ColorPosition(int shiftValue) {
			this.shiftValue = shiftValue;
		}

		private int shiftValue;

		public int getShiftValue() {
			return shiftValue;
		}

		public int getIncrement() {
			return 1 << shiftValue;
		}

		public int getColorMask() {
			return 0xFF << shiftValue;
		}

		public ColorPosition getNextColorPosition() {
			if (this == BLUE) {
				return GREEN;
			} else if (this == GREEN) {
				return RED;
			}
			return BLUE;
		}
	}
}