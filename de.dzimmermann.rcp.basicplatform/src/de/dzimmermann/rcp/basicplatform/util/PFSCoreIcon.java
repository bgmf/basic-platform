package de.dzimmermann.rcp.basicplatform.util;

/**
 * This Enumeration contains as much of the SSDTool internal images as possible.
 * 
 * @author danielz
 * @version 0.2
 * @since PFSRCPCore V0.2.3
 */
public enum PFSCoreIcon {

	/**
	 * the current splash logo as a .gif icon
	 */
	PFSCORE_ICON_SPLASH("ipoqueSplash.gif"),
	/**
	 * the current logo in 16x16 pixel
	 */
	PFSCORE_ICON_LOGO_16("ipqlogo_16.gif"),
	/**
	 * the current logo in 32x32 pixel
	 */
	PFSCORE_ICON_LOGO_32("ipqlogo_32.gif"),
	/**
	 * the current logo in 64x64 pixel
	 */
	PFSCORE_ICON_LOGO_64("ipqlogo_64.gif"),
	/**
	 * the current logo in 128x128 pixel
	 */
	PFSCORE_ICON_LOGO_128("ipqlogo_128.gif"),
	/**
	 * the current logo in 256x256 pixel
	 */
	PFSCORE_ICON_LOGO_256("ipqlogo_256.gif"),
	/**
	 * the current logo in 512x512 pixel
	 */
	PFSCORE_ICON_LOGO_512("ipqlogo_512.gif"),

	/**
	 * an Eclipse folder icon
	 */
	PFSCORE_ICON_FILE("file.gif"),
	/**
	 * an Eclipse zip archive icon
	 */
	PFSCORE_ICON_ARCHIVE_ZIP("zip_obj.gif"),
	/**
	 * an Eclipse folder icon
	 */
	PFSCORE_ICON_FOLDER("fldr_obj.gif"),
	/**
	 * an Eclipse opened folder icon
	 */
	PFSCORE_ICON_FOLDER_OPENED("folder_opened.gif"),

	/**
	 * an Eclipse editor save icon
	 */
	PFSCORE_ICON_EDITOR_SAVE("save_edit.gif"),
	/**
	 * an Eclipse editor save-as icon
	 */
	PFSCORE_ICON_EDITOR_SAVE_AS("saveas_edit.gif"),
	/**
	 * an Eclipse editor save-all icon
	 */
	PFSCORE_ICON_EDITOR_SAVE_ALL("saveall_edit.gif"),
	/**
	 * an Eclipse editor undo icon
	 */
	PFSCORE_ICON_EDITOR_UNDO("undo_edit.gif"),
	/**
	 * an Eclipse editor redo icon
	 */
	PFSCORE_ICON_EDITOR_REDO("redo_edit.gif"),
	/**
	 * an Eclipse editor delete icon
	 */
	PFSCORE_ICON_EDITOR_ADD("add_exc.gif"),
	/**
	 * an Eclipse editor delete icon
	 */
	PFSCORE_ICON_EDITOR_DELETE("delete_edit.gif"),
	/**
	 * an Eclipse icon showing a symbol for a default editor
	 */
	PFSCORE_ICON_EDITOR_DEFAULT_AREA("editor_area.gif"),

	/**
	 * an Eclipse home navigation icon
	 */
	PFSCORE_ICON_NAVIGATION_HOME("home_nav.gif"),
	/**
	 * an Eclipse backward navigation icon
	 */
	PFSCORE_ICON_NAVIGATION_BACKWARDS("backward_nav.gif"),
	/**
	 * an Eclipse forward navigation icon
	 */
	PFSCORE_ICON_NAVIGATION_FORWARD("forward_nav.gif"),

	/**
	 * an Eclipse icon showing a views content in a tree like structure
	 */
	PFSCORE_ICON_VIEW_CONTENT("content.gif"),
	/**
	 * an Eclipse icon showing a views content in hierarchy tree like structure
	 */
	PFSCORE_ICON_VIEW_CONTENT_HIERARCHY("hierarchy_co.gif"),
	/**
	 * an Eclipse icon for content informations (a book)
	 */
	PFSCORE_ICON_VIEW_DISPLAY_CONTENTS("contents_view.gif"),
	/**
	 * an Eclipse icon showing a flat layout view symbol (eg. a table)
	 */
	PFSCORE_ICON_VIEW_LAYOUT_FLAT("flatLayout.gif"),
	/**
	 * an Eclipse icon showing a hierarchical layout view symbor(e.g. a tree)
	 */
	PFSCORE_ICON_VIEW_LAYOUT_HIERARCHICAL("hierarchicalLayout.gif"),
	/**
	 * an Eclipse icon showing a tree layout, very similar to the
	 * {@link #PFSCORE_ICON_VIEW_LAYOUT_HIERARCHICAL}
	 */
	PFSCORE_ICON_VIEW_LAYOUT_TREE("tree_mode.gif"),
	/**
	 * an Eclipse icon showing a default view symbol (empty symbol content)
	 */
	PFSCORE_ICON_VIEW_DEFAULT("defaultview_misc.gif"),

	/**
	 * an Eclipse print icon
	 */
	PFSCORE_ICON_MISC_PRINT("print.gif"),
	/**
	 * an Eclipse help icon
	 */
	PFSCORE_ICON_MISC_HELP("help.gif"),
	/**
	 * an Eclipse trash can icon
	 */
	PFSCORE_ICON_MISC_TRASH("trash.gif"),
	/**
	 * an Eclipse icon for configs
	 */
	PFSCORE_ICON_MISC_CONFIG("configs.gif"),
	/**
	 * an Eclipse icon showing a small bulb for quick fixes
	 */
	PFSCORE_ICON_MISC_QUICKFIX("quickfix_obj.gif"),
	/**
	 * an Eclipse icon to start something
	 */
	PFSCORE_ICON_MISC_START("progress_start.gif"),
	/**
	 * an Eclipse icon to stop something
	 */
	PFSCORE_ICON_MISC_STOP("progress_stop.gif"),
	/**
	 * an Eclipse icon to pause something
	 */
	PFSCORE_ICON_MISC_PAUSE("pause.gif"),
	/**
	 * a simple icon to mark sth. as not finished or not selected (e.g. table or
	 * tree entries)
	 */
	PFSCORE_ICON_MISC_UNCHECKED("unchecked.gif"),
	/**
	 * a simple icon to mark sth. as finished or selected (e.g. table or tree
	 * entries)
	 */
	PFSCORE_ICON_MISC_CHECKED("checked.gif"),
	/**
	 * an Eclipse icon to mark sth. as not finished or not selected (e.g. table
	 * or tree entries)
	 */
	PFSCORE_ICON_MISC_INCOMPLETE("incomplete_tsk.gif"),
	/**
	 * an Eclipse icon to mark sth. as finished or selected (e.g. table or tree
	 * entries)
	 */
	PFSCORE_ICON_MISC_COMPLETE("complete_tsk.gif"),
	/**
	 * an Eclipse icon to add sth.
	 */
	PFSCORE_ICON_MISC_ADD("add_exc.gif"),
	/**
	 * an Eclipse icon to remove sth.
	 */
	PFSCORE_ICON_MISC_REMOVE("remove.gif"),
	/**
	 * an Eclipse icon to remove all
	 */
	PFSCORE_ICON_MISC_REMOVE_ALL("removeall.gif"),
	/**
	 * an Eclipse icon to display settings
	 */
	PFSCORE_ICON_MISC_SETTINGS("settings_obj.gif"),
	/**
	 * an Eclipse icon to display/hide a toolbar
	 */
	PFSCORE_ICON_MISC_TOOLBAR("toolbar.gif"),
	/**
	 * an Eclipse icon to symbolize a history list
	 */
	PFSCORE_ICON_MISC_HISTORY_LIST("history_list.gif"),
	/**
	 * an Eclipse icon showing a default perspective symbol
	 */
	PFSCORE_ICON_MISC_DEFAULT_PERSPECTIVE("default_persp.gif"),
	/**
	 * an Eclipse icon a cheatsheet taskgroup icon used for edit sth.
	 */
	PFSCORE_ICON_MISC_EDIT_CHEATSHEET("cheatsheet_taskgroup_obj.gif"),
	/**
	 * an Eclipse image for cheatsheet used within wizards
	 */
	PFSCORE_ICON_WIZARD_CHEATSHEET_NEW("new_cheatsheet_wiz.png"),
	/**
	 * an Eclipse image for folders used within wizards
	 */
	PFSCORE_ICON_WIZARD_FOLDER("newfolder_wiz.png");

	/**
	 * this value defines the SSDTool's path to the enabled icons
	 */
	protected static final String ENABLED_ICON_PATH = "icons/";
	/**
	 * this value defines the SSDTool's path to the disabled icons
	 */
	protected static final String DISABLED_ICON_PATH = "icons/disabled/";

	/**
	 * the "icon path" is the name of the icen, the rest will be decided by the
	 */
	protected final String iconName;

	protected boolean enabled;

	private PFSCoreIcon(String iconName) {
		this.iconName = iconName;
		this.enabled = true;
	}

	public String getIconName() {
		return iconName;
	}

	/**
	 * sets the enables flag, default is <code>true</code>
	 * 
	 * @param enabled
	 *            decides whether the enabled ore disabled icon is used, if the
	 *            selected on is not present, the other one will be used instead
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean getEnabled() {
		return enabled;
	}

	/**
	 * retrieve a {@link PFSCoreIcon} object by the given icons name (e.g.
	 * 'ipqlogo_16.gif')
	 * 
	 * @param iconName
	 *            the icons name
	 * @param enabled
	 *            used, to specify whether the enabled or disabled icon should
	 *            be used
	 * @return the respective {@link PFSCoreIcon} object or <code>null</code>
	 */
	public static PFSCoreIcon getIconTypeByIconPath(String iconName,
			boolean enabled) {

		for (PFSCoreIcon iconType : values()) {
			if (iconType.getIconName().toLowerCase()
					.equals(iconName.toLowerCase())) {
				iconType.enabled = enabled;
				return iconType;
			}
		}

		return null;
	}

	@Override
	public String toString() {
		return name() + ": "
				+ (enabled ? ENABLED_ICON_PATH : DISABLED_ICON_PATH)
				+ getIconName();
	}
}
