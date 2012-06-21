package de.dzimmermann.rcp.basicplatform.services;

import java.util.List;

/**
 * Use this service, when you want to retrieve all the groups that a caller is
 * assigned to.<br/>
 * This approach was intended to be used to check, whether the caller is an
 * administrator or not. This can be used to make some plugin specific
 * additional filtering, for example to examine, if a caller can create specific
 * and "admin-only" features (for example statistics).<br/>
 * On the other hand a scenario as to inform a user about his current rights
 * within the PCP might also be appropriate.
 * 
 * @author dzimmermann
 */
public interface IGroupService {

	public static final String GROUP_ADMIN = "admin";
	public static final String GROUP_USERS = "users";
	public static final String GROUP_STATISTICS = "statistics";
	public static final String GROUP_RECORD_PROCESSING = "record processing";
	public static final String GROUP_REPORT_SETTINGS = "report settings";
	public static final String GROUP_UPDATE = "update";
	public static final String GROUP_SOLICITOR = "solicitor";

	/**
	 * Retrieve the groups that the caller is assigned to.<br/>
	 * The check is done within an implicit context.
	 * 
	 * @return the groups that the caller is assigned to
	 */
	List<String> getAssignedGroups();

	/**
	 * Retrieve the groups that the caller is assigned to.<br/>
	 * In this approach the context is specified by the caller.<br/>
	 * <b>Attention:</b> This can be implementation specific!
	 * 
	 * @param context
	 *            the context for the check
	 * @return the groups that the caller is assigned to
	 */
	List<String> getAssignedGroups(Object context);

	/**
	 * Check whether or not the caller is a developer within an implicit
	 * context.
	 * 
	 * @return <code>true</code>, if the caller is a developer
	 */
	boolean isDeveloper();

	/**
	 * Check whether or not the caller is a developer within a specific context.<br/>
	 * The context might be something like the current users name, or something
	 * else.<br/>
	 * <b>Attention:</b> This can be implementation specific!
	 * 
	 * @param context
	 *            the context for the check
	 * @return <code>true</code>, if the caller is a developer
	 */
	boolean isDeveloper(Object context);
}
