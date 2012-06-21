package de.dzimmermann.rcp.basicplatform.model;

public class BasicPlatformSessionModelImpl implements BasicPlatformSingletonProvider {

	private static BasicPlatformSessionModel instance;

	@Override
	public Object getInstanceInternal() {
		if (instance == null) {
			instance = new BasicPlatformSessionModel();
			instance.applicationMode = PCPApplicationMode.RCP;
		}
		return instance;
	}
}
