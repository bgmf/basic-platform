package de.dzimmermann.rcp.basicplatform.model;

public class TrustStore {

	private final String internalPath;
	private final String optionalPassword;

	public TrustStore(String internalPath, String optionalPassword) {
		this.internalPath = internalPath;
		this.optionalPassword = optionalPassword;
	}

	public String getInternalPath() {
		return internalPath;
	}

	public String getOptionalPassword() {
		return optionalPassword;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((internalPath == null) ? 0 : internalPath.hashCode());
		result = prime
				* result
				+ ((optionalPassword == null) ? 0 : optionalPassword.hashCode());
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
		TrustStore other = (TrustStore) obj;
		if (internalPath == null) {
			if (other.internalPath != null)
				return false;
		} else if (!internalPath.equals(other.internalPath))
			return false;
		if (optionalPassword == null) {
			if (other.optionalPassword != null)
				return false;
		} else if (!optionalPassword.equals(other.optionalPassword))
			return false;
		return true;
	}
}
