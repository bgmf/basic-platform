package de.dzimmermann.rcp.basicplatform.ui.util.colorprovider;

import java.awt.Color;

import de.dzimmermann.rcp.basicplatform.ui.util.colorprovider.impl.DefaultColorProvider;
import de.dzimmermann.rcp.basicplatform.ui.util.colorprovider.impl.FineColorProvider;
import de.dzimmermann.rcp.basicplatform.ui.util.colorprovider.impl.RandomColorProvider;

/**
 * This factory creates an instance of a {@link ColorProvider} interface
 * implementaion.
 * 
 * @author danielz
 * @since V0.2.0
 */
public class ColorProviderFactory {

	/**
	 * returnes the default {@link ColorProvider} implementation (
	 * {@link DefaultColorProvider})
	 * 
	 * @param startColor
	 *            the color to start with
	 * @param increment
	 *            the increment steps (limt the number of color per each base
	 *            color)
	 * @return the {@link DefaultColorProvider} implementation
	 */
	public static ColorProvider getDefaultColorProvider(Color startColor,
			int increment) {

		if (startColor == null && increment == 0)
			return new DefaultColorProvider();

		if (startColor == null && increment > 0)
			return new DefaultColorProvider(increment);

		if (startColor != null && increment == 0)
			return new DefaultColorProvider(startColor);

		return new DefaultColorProvider(startColor, increment);
	}

	/**
	 * returnes a more fine {@link ColorProvider} implementation (
	 * {@link FineColorProvider})
	 * 
	 * @param startColor
	 *            the color to start with
	 * @param increment
	 *            the increment steps (limt the number of color per each base
	 *            color)
	 * @return the {@link FineColorProvider} implementation
	 */
	public static ColorProvider getFineColorProvider(Color startColor,
			int increment) {

		if (startColor == null && increment == 0)
			return new FineColorProvider();

		if (startColor == null && increment > 0)
			return new FineColorProvider(increment);

		if (startColor != null && increment == 0)
			return new FineColorProvider(startColor);

		return new FineColorProvider(startColor, increment);
	}

	/**
	 * returnes a random {@link ColorProvider} implementation (
	 * {@link RandomColorProvider})
	 * 
	 * @param startColor
	 *            the color to start with
	 * @param increment
	 *            the increment steps (limt the number of color per each base
	 *            color)
	 * @return the {@link RandomColorProvider} implementation
	 */
	public static ColorProvider getRandomColorProvider() {
		return new RandomColorProvider();
	}
}
