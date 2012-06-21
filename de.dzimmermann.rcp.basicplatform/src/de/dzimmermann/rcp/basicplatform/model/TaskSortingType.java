package de.dzimmermann.rcp.basicplatform.model;

/**
 * This type is used to handle the sorting of the tasks.
 * 
 * @author dzimmermann
 */
public enum TaskSortingType {

	/**
	 * Default sorting. Ascending by the name of the Task.
	 */
	ASCENDING("asc"),

	/**
	 * Descending by the name of the task.
	 */
	DESCENDING("desc"),

	/**
	 * No sorting at all.
	 */
	NO_SORTING("not");

	private final String name;

	private TaskSortingType(final String name) {
		this.name = name;
	}

	public static TaskSortingType getType(String name) {
		for (TaskSortingType type : values()) {
			if (type.name.equals(name))
				return type;
		}
		return null;
	}
}
