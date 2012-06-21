package de.dzimmermann.rcp.basicplatform.util;

/**
 * Using the PersistingSupport means you need a special type of obejct ot be
 * persisted. I introduced this interface because to allow a stricter MVC
 * (Model-View-Controller) to be supported by a GUI.
 * <p>
 * 
 * The idea is to provide to options:
 * <ol>
 * <li>idependently set the object to persist and finally persist it (or not)</li>
 * <li>directly persist an object</li>
 * </ol>
 * <p>
 * 
 * @author dzimmermann
 * @since PFSRCPCore 0.4.0
 * @version 0.1
 * 
 * @param <T>
 *            the type of objects that may be persisten by this persisting
 *            support
 */
public interface PersistingSupport<T> {

	/**
	 * Classes that implement this interface may simply set the data to persist
	 * via this method.
	 * 
	 * @param toPersist
	 *            the obejct to persist
	 */
	public void setPersistingContext(T toPersist);

	/**
	 * An implementing class may return the data to persist to an interested
	 * party.
	 * 
	 * @return returns an object of the current type, depending on the
	 *         implementation
	 */
	public T getPersistingContext();

	/**
	 * If the data to persist is provided via the
	 * {@link #setPersistingContext(Object)} method or any other way, this
	 * method will persist it in the way, the implementor wants it to be done.
	 */
	public void persistContext();

	/**
	 * This method is used to directly persist an object of the current type.
	 * Whether an implementation may only support this or simply call the
	 * {@link #setPersistingContext(Object)} and {@link #persistContext()}
	 * methods lays in the hand of the implementor.
	 * 
	 * @param toPersist
	 */
	public void persistContext(T toPersist);
}
