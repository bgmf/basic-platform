package de.dzimmermann.rcp.basicplatform.model;

public class TaskGroupRestriction {

	private String name = null;

	private boolean inverse = false;

	public TaskGroupRestriction() {
	}

	public TaskGroupRestriction(final String name, final boolean inverse) {
		this.name = name;
		this.inverse = inverse;
	}

	public String getName() {
		return name;
	}

	public boolean isInverse() {
		return inverse;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskGroupRestriction other = (TaskGroupRestriction) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
