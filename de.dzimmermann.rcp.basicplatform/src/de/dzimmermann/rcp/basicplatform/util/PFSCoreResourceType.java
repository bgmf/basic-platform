package de.dzimmermann.rcp.basicplatform.util;

/**
 * This enum defines the set of possible resource types with a string
 * representation.
 * 
 * @author dzimmermann
 * @version 0.1
 */
public enum PFSCoreResourceType {

	ICON("ICON");

	private String type;

	private PFSCoreResourceType(String type) {
		this.type = type;
	}

	public static PFSCoreResourceType getResourceTypeByString(String type) {
		for (PFSCoreResourceType rte : PFSCoreResourceType.values()) {
			if (rte.type.equals(type)) {
				return rte;
			}
		}
		return null;
	}

	/**
	 * {@link PFSCoreResourceType#ICON} specific settings
	 * 
	 * @author dzimmermann
	 * 
	 */
	public enum IconResource {

		ENABLED(true, "ICON.enabled"), DISABLED(false, "ICON.disabled");

		private boolean enabled;
		private String type;

		private IconResource(boolean enabled, String type) {
			this.enabled = enabled;
			this.type = type;
		}

		public static IconResource getIconResourceByBoolean(boolean enabled) {
			for (IconResource ir : IconResource.values()) {
				if (ir.enabled == enabled) {
					return ir;
				}
			}
			return null;
		}

		public String getType() {
			return type;
		}

		public static IconResource getIconResourceByBoolean(String type) {
			for (IconResource ir : IconResource.values()) {
				if (ir.type.equals(type)) {
					return ir;
				}
			}
			return null;
		}
	}
}
