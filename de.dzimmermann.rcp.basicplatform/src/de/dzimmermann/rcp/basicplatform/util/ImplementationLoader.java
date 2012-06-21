package de.dzimmermann.rcp.basicplatform.util;

/**
 * Helper class to load an implementation for a specified class by appending an
 * <code>Impl</code> for example to the classes name and try to load it from the
 * specified classes {@link ClassLoader}.
 * 
 * @author dzimmermann
 * 
 */
public class ImplementationLoader {

	/**
	 * Default value for the implementation appendix. (<code>Impl</code>)
	 */
	public static final String DEFAULT_IMPLEMENTATION_SUFFIX = "Impl"; //$NON-NLS-1$

	/**
	 * Create a new instance of an implementation for the specified class.<br>
	 * The parameter <code>implementationAppendix</code> specifies the name of
	 * the implementation by appending this string to the base classes name.
	 * 
	 * @param type
	 *            the base class to load an implementation for
	 * @param implementationAppendix
	 *            the appended string for the a class to add to, to find the
	 *            implementation to the <code>type</code> parameter
	 * @return an instance of the implementation for the base class
	 */
	public static Object newInstance(final Class<?> type,
			String implementationAppendix) {
		Object result = null;
		try {
			Class<?> clazz = type.getClassLoader().loadClass(
					type.getName() + implementationAppendix);
			result = clazz.getConstructor().newInstance();
		} catch (Throwable throwable) {
			String message = String.format(
					"Could not load implementation for $s with appendix %s!", //$NON-NLS-1$
					type.getName(), implementationAppendix);
			throw new RuntimeException(message, throwable);
		}
		return result;
	}

	/**
	 * See {@link #newInstance(Class, String)}, using the default appendix.<br>
	 * See {@link #DEFAULT_IMPLEMENTATION_SUFFIX}.
	 * 
	 * @param type
	 *            the base class to load an implementation for
	 * @return an instance of the implementation for the base class
	 */
	public static Object newInstance(final Class<?> type) {
		return newInstance(type, DEFAULT_IMPLEMENTATION_SUFFIX);
	}
}
