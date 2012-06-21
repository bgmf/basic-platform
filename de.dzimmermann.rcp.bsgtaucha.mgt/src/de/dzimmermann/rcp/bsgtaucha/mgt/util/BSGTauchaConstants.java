package de.dzimmermann.rcp.bsgtaucha.mgt.util;

import de.dzimmermann.rcp.basicplatform.util.BasicPlatformPluginConstants;

public abstract class BSGTauchaConstants {

	public static final String LAST_OPEN_DIR = BasicPlatformPluginConstants.OPEN_SAVE_LAST_DIR_KEY
			+ ".bsg"; //$NON-NLS-1$

	public static final String MODEL_DIR = "bsg-data"; //$NON-NLS-1$

	public static final String MODEL_EXTENSION = ".xml.gz"; //$NON-NLS-1$
	public static final String MODEL_EXTENSION_BACKUP = ".bak"; //$NON-NLS-1$

	public static final String LAST_MODEL_PREFERENCE = "LAST_MODEL";

	// OpenEditorHandler
	public static final String BAND_TASK_ID = "de.dzimmermann.rcp.bsgtaucha.mgt.band";
	public static final String WORK_ENTRIES_TASK_ID = "de.dzimmermann.rcp.bsgtaucha.mgt.workEntries";
	public static final String PERSON_TASK_ID = "de.dzimmermann.rcp.bsgtaucha.mgt.persons";
	public static final String WORK_TASK_ID = "de.dzimmermann.rcp.bsgtaucha.mgt.tasks";

	// OpenFileHandler
	public static final String OPEN_FILE = "de.dzimmermann.rcp.bsgtaucha.mgt.openFile";

	public static final String DEFAULT_PASSWORD_SUFFIX = ".def.pwd";
	public static final String ADVANCED_PASSWORD_SUFFIX = ".adv.pwd";
}
