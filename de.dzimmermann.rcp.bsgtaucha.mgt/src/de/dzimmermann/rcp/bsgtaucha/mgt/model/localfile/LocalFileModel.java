package de.dzimmermann.rcp.bsgtaucha.mgt.model.localfile;

import org.eclipse.core.filesystem.IFileStore;

import de.dzimmermann.rcp.bsgtaucha.mgt.model.RootType;

public class LocalFileModel implements Comparable<LocalFileModel> {

	private final IFileStore fileStore;

	private final String fileName;
	private final long lastModified;

	private RootType model;

	public LocalFileModel(IFileStore fileStore, String fileName,
			long lastModified, RootType model) {

		this.fileStore = fileStore;

		this.fileName = fileName;
		this.lastModified = lastModified;

		this.model = model;
	}

	public IFileStore getFileStore() {
		return fileStore;
	}

	public String getFileName() {
		return fileName;
	}

	public long getLastModified() {
		return lastModified;
	}

	public void setModel(RootType model) {
		this.model = model;
	}

	public RootType getModel() {
		return model;
	}

	@Override
	public int compareTo(LocalFileModel o) {
		return fileName.compareTo(o.fileName);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fileName == null) ? 0 : fileName.hashCode());
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
		LocalFileModel other = (LocalFileModel) obj;
		if (fileName == null) {
			if (other.fileName != null)
				return false;
		} else if (!fileName.equals(other.fileName))
			return false;
		return true;
	}
}