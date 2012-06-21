package de.dzimmermann.rcp.basicplatform.util;

import java.io.PrintWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableOutputFormatter {

	/*
	 * global set up
	 */

	private boolean lineCounter = false;

	private boolean aligned = true;
	private String delimiter = "|";
	private String textDecollator = "\"";

	private String dateFormat = "yyyy-MM-dd HH:mm:ss";
	private SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

	private boolean inetAdressAsIs = false;
	private String nullReplacement = "[null]";

	/**
	 * If you want to have an header/content independent column with a line
	 * counter, use this method to switch this either on or of.<br/>
	 * The default is <code>false</code>=off.
	 * 
	 * @param lineCounter
	 *            <code>true</code>, if the line counter will be visible,
	 *            <code>false</code> otherwise
	 */
	public void setLineCounter(boolean lineCounter) {
		this.lineCounter = lineCounter;
	}

	/**
	 * Check, whether or not a first column with a line counter will be
	 * displayed or not.<br/>
	 * The default is <code>false</code>=off.
	 * 
	 * @return <code>true</code>, if the line counter will be visible,
	 *         <code>false</code> otherwise
	 */
	public boolean isLineCounter() {
		return lineCounter;
	}

	/**
	 * Set whether or not the output is formatted aligned or unaligned. This
	 * means, if it is aligned, that each column is as wide as its longest
	 * member. Unaligned, on the other hand, means, that the content is written
	 * without any empty space.<br/>
	 * The default is <code>true</code>=aligned.
	 * 
	 * @param alligned
	 *            <code>true</code>, if the content is aligned,
	 *            <code>false</code> otherwise
	 */
	public void setAligned(boolean alligned) {
		this.aligned = alligned;
	}

	/**
	 * Returns whether or not the output is formatted aligned or unaligned. This
	 * means, if it is aligned, that each column is as wide as its longest
	 * member. Unaligned, on the other hand, means, that the content is written
	 * without any empty space.<br/>
	 * The default is <code>true</code>=aligned.
	 * 
	 * @return <code>true</code>, if the content is aligned, <code>false</code>
	 *         otherwise
	 */
	public boolean isAligned() {
		return aligned;
	}

	/**
	 * Set the new delimiter to separate the header/content values.<br/>
	 * The default value is &quot;|&quot;
	 * 
	 * @param delimiter
	 *            the new delimiter
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	/**
	 * Return the delimiter used to separate the header/content values.<br/>
	 * The default value is &quot;|&quot;
	 * 
	 * @return the current delimiter
	 */
	public String getDelimiter() {
		return delimiter;
	}

	/**
	 * Sets the current text decollator to separate the text from the delimiter
	 * (to allow delimiter symbols within the text). <br/>
	 * The default value is '<code>&quot;</code>' (double quotation marks).
	 * 
	 * @param textDecollator
	 *            the new text decollator
	 */
	public void setTextDecollator(String textDecollator) {
		this.textDecollator = textDecollator;
	}

	/**
	 * Returns the current text decollator to separate the text from the
	 * delimiter (to allow delimiter symbols within the text). <br/>
	 * The default value is '<code>&quot;</code>' (double quotation marks).
	 * 
	 * @return the current text decollator
	 */
	public String getTextDecollator() {
		return textDecollator;
	}

	/**
	 * Set the formatting String for {@link Calendar} and {@link Date} objects.<br/>
	 * The default is &quot;<code>yyyy-MM-dd HH:mm:ss</code>&quot;.
	 * 
	 * @param dateFormat
	 *            the new {@link Date} formatting option
	 * @see SimpleDateFormat
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
		this.sdf = new SimpleDateFormat(dateFormat);
	}

	/**
	 * Returns the current String that is used to parse {@link Calendar} and
	 * {@link Date} objects.<br/>
	 * The default is &quot;<code>yyyy-MM-dd HH:mm:ss</code>&quot;.
	 * 
	 * @return the current {@link Date} formatting option
	 * @see SimpleDateFormat
	 */
	public String getDateFormat() {
		return dateFormat;
	}

	/**
	 * Set the IP address formatting.<br/>
	 * See: {@link #isInetAdressAsIs()}<br/>
	 * The default is <code>false</code>=off.
	 * 
	 * @param inetAdressAsIs
	 *            <code>true</code>, if the IP is written as the
	 *            {@link InetAddress#toString()} method dictates,
	 *            <code>false</code> otherwise for the host address only
	 *            formatting
	 */
	public void setInetAdressAsIs(boolean inetAdressAsIs) {
		this.inetAdressAsIs = inetAdressAsIs;
	}

	/**
	 * Returns how to display an IP address (Java type {@link InetAddress}):
	 * within it's default String representation or only the host address.<br/>
	 * The default is <code>false</code>=off.
	 * 
	 * @return <code>true</code>, if the IP is written as the
	 *         {@link InetAddress#toString()} method dictates,
	 *         <code>false</code> otherwise for the host address only formatting
	 */
	public boolean isInetAdressAsIs() {
		return inetAdressAsIs;
	}

	/**
	 * Returns the current String used to replace uninitialized values (
	 * <code>null</code> values) within the data to display.<br/>
	 * The default is &quot;<code>[null]</code>&quot;.
	 * 
	 * @return the current <code>null</code> replacement
	 */
	public String getNullReplacement() {
		return nullReplacement;
	}

	/**
	 * Set the String to replace uninitialized values ( <code>null</code>
	 * values) within the data to display.<br/>
	 * The default is &quot;<code>[null]</code>&quot;.<br/>
	 * <br/>
	 * <i><b>Attention:</b> This value must not be null!</i>
	 * 
	 * @param nullReplacement
	 *            the new <code>null</code> replacement
	 */
	public void setNullReplacement(String nullReplacement) {
		if (nullReplacement == null)
			nullReplacement = "[null]";
		this.nullReplacement = nullReplacement;
	}

	/*
	 * content to format
	 */

	private String[] header;
	private List<Object[]> content;

	/**
	 * Use this method, if you want to resent the initial header line. This may
	 * be useful, if you want to reuse the formatter.
	 * 
	 * @param header
	 *            the new header
	 */
	public void setHeader(String[] header) {
		this.header = header;
	}

	/**
	 * Retrieve the specified header.
	 * 
	 * @return the given header
	 */
	public String[] getHeader() {
		return header;
	}

	/**
	 * Use this method, if you want to reset the initial content, for example,
	 * if you want to reuse the formatter.
	 * 
	 * @param content
	 *            the new content
	 */
	public void setContent(List<Object[]> content) {
		this.content = content;
	}

	/**
	 * Retrieve the specified content.
	 * 
	 * @return the given content
	 */
	public List<Object[]> getContent() {
		return content;
	}

	/*
	 * constructors
	 */

	/**
	 * Default constructor to initiate the {@link TableOutputFormatter}.<br/>
	 * You need to specify the header and the content to format.
	 * 
	 * @param header
	 *            The header of the table.
	 * @param content
	 *            The content of the table. Be aware, that each lines content
	 *            need to have the same lenght as the header, since the headers
	 *            lengths is used to print the data.
	 */
	public TableOutputFormatter(String[] header, List<Object[]> content) {
		this.header = header;
		this.content = content;
	}

	/*
	 * formatting
	 */

	/**
	 * Format the specified input and print it to {@link System#out}.
	 */
	public void format() {
		format(new PrintWriter(System.out));
	}

	/**
	 * Format the specified input and print it into the specified {@link Writer}
	 * .
	 * 
	 * @param out
	 */
	public void format(Writer out) {

		PrintWriter writer = new PrintWriter(out, true);

		// initiate some helper variables
		int[] maxLength = getMaxLenght();
		int headerLenght = header.length;
		int contentCount = content.size();

		int lineCountFormatSpacer = getLineCountFormatSpacer(contentCount);

		// initiate the formatting (line count format and line format Strings)
		String lineCountFormat = getLineCountFormat(lineCountFormatSpacer);
		String lineFormat = getLineFormat(headerLenght, maxLength);

		// print the header
		printHeader(writer, headerLenght, lineCountFormat, lineFormat);

		// if the output should be aligned, print the table separation for
		// better readability
		if (aligned) {
			printTableSeparatorForAllignedOutput(writer, lineCountFormatSpacer,
					headerLenght, maxLength);
		}

		// print the content (each line separately)
		int lineNumber = 1;
		for (Object[] line : content) {
			printLineContent(writer, line, lineNumber, headerLenght,
					lineCountFormat, lineFormat);
			lineNumber++;
		}
	}

	private void printHeader(PrintWriter writer, int headerLenght,
			String lineCountFormat, String lineFormat) {
		if (lineCounter) {
			Object[] newHeader = new Object[headerLenght + 1];
			for (int i = 0; i < headerLenght + 1; i++) {
				if (i == 0)
					newHeader[i] = "#";
				else
					newHeader[i] = header[i - 1];
			}
			writer.printf(lineCountFormat.toString() + lineFormat,
					(Object[]) newHeader);
		} else {
			writer.printf(lineFormat, (Object[]) header);
		}
	}

	private void printTableSeparatorForAllignedOutput(PrintWriter writer,
			int lineCountFormatSpacer, int headerLenght, int[] maxLength) {
		StringBuilder sb = new StringBuilder();
		if (lineCounter) {
			sb.append(aligned ? "-" : "");
			for (int i = 0; i < lineCountFormatSpacer; i++) {
				sb.append("-");
			}
			sb.append(aligned ? "-" : "");
			sb.append("+");
		}
		for (int i = 0; i < headerLenght; i++) {
			if (i == 0)
				sb.append("-");
			for (int j = 0; j < maxLength[i]; j++) {
				sb.append("-");
			}
			if ((i + 1) < headerLenght)
				sb.append("-+-");
			else
				sb.append("-");
		}
		writer.println(sb.toString());
	}

	private void printLineContent(PrintWriter writer, Object[] line,
			int lineNumber, int headerLenght, String lineCountFormat,
			String lineFormat) {
		String[] lineContent = new String[headerLenght];
		for (int i = 0; i < headerLenght; i++) {
			String s = null;
			try {
				if (line[i] == null)
					s = nullReplacement;
				else if (classFormatters.get(line[i].getClass()) != null)
					s = classFormatters.get(line[i].getClass())
							.formatClassContent(line[i]);
				else if (line[i] instanceof Date)
					s = sdf.format((Date) line[i]);
				else if (line[i] instanceof Calendar)
					s = sdf.format(((Calendar) line[i]).getTime());
				else
					s = line[i].toString();
				// security check
				if (s == null)
					s = nullReplacement;
			} catch (Exception e) {
				s = nullReplacement;
			}
			if (s != null)
				lineContent[i] = s;
		}
		if (lineCounter) {
			Object[] newLineContent = new Object[headerLenght + 1];
			for (int i = 0; i < headerLenght + 1; i++) {
				if (i == 0)
					newLineContent[i] = "" + lineNumber;
				else
					newLineContent[i] = lineContent[i - 1];
			}
			writer.printf(lineCountFormat.toString() + lineFormat,
					(Object[]) newLineContent);
		} else {
			writer.printf(lineFormat, (Object[]) lineContent);
		}
	}

	/*
	 * helper methods
	 */

	private int[] getMaxLenght() {

		int headerLenght = header.length;
		int[] result = new int[headerLenght];

		for (int i = 0; i < headerLenght; i++)
			result[i] = header[i].length();

		for (Object[] line : content) {
			for (int i = 0; i < headerLenght; i++) {
				String s = null;
				try {
					if (line[i] == null)
						s = nullReplacement;
					else if (classFormatters.get(line[i].getClass()) != null)
						s = classFormatters.get(line[i].getClass())
								.formatClassContent(line[i]);
					else if (line[i] instanceof Date)
						s = sdf.format((Date) line[i]);
					else if (line[i] instanceof Calendar)
						s = sdf.format(((Calendar) line[i]).getTime());
					else
						s = line[i].toString();
					// security check
					if (s == null)
						s = nullReplacement;
				} catch (Exception e) {
					s = nullReplacement;
				}
				if (s != null && s.length() > result[i])
					result[i] = s.length();
			}
		}

		return result;
	}

	private String getLineFormat(int headerLenght, int[] maxLength) {
		StringBuilder lineFormat = new StringBuilder();
		for (int i = 0; i < headerLenght; i++) {
			lineFormat.append(aligned ? " " : "");
			lineFormat.append(!aligned ? textDecollator : "");
			lineFormat.append("%" + (aligned ? "-" + maxLength[i] : "") + "s");
			lineFormat.append(!aligned ? textDecollator : "");
			lineFormat.append(aligned ? " " : "");
			if ((i + 1) < headerLenght)
				lineFormat.append(delimiter);
		}
		lineFormat.append("%n");
		return lineFormat.toString();
	}

	private int getLineCountFormatSpacer(int contentCount) {
		int lineCountFormatSpacer = 0;
		if (aligned)
			lineCountFormatSpacer = ("" + contentCount).length();
		return lineCountFormatSpacer;
	}

	private String getLineCountFormat(int lineCountFormatSpacer) {
		StringBuilder lcSb = new StringBuilder("");
		lcSb.append(aligned ? " " : "");
		if (aligned)
			lcSb.append("%-" + lineCountFormatSpacer + "s");
		else
			lcSb.append(textDecollator + "%s" + textDecollator);
		lcSb.append(aligned ? " " : "");
		lcSb.append(delimiter);
		return lcSb.toString();
	}

	/*
	 * separate class formatter, used to handle the formatting for a special
	 * class separately - allows to override the default Date/Calendar and
	 * InetAddress formatting or the default #toString() method
	 */

	private Map<Class<?>, ClassFormatter> classFormatters = new HashMap<Class<?>, ClassFormatter>();

	public void addClassFormatter(Class<?> clazz, ClassFormatter classFormatter) {
		classFormatters.put(clazz, classFormatter);
	}

	public ClassFormatter getClassFormatter(Class<?> clazz) {
		return classFormatters.get(clazz);
	}

	public ClassFormatter removeClassFormatter(Class<?> clazz) {
		return classFormatters.remove(clazz);
	}

	public ClassFormatter removeClassFormatter(ClassFormatter classFormatter) {
		for (Class<?> clazz : classFormatters.keySet()) {
			if (classFormatters.get(clazz).equals(classFormatter))
				return classFormatters.remove(clazz);
		}
		return null;
	}

	public static interface ClassFormatter {
		String formatClassContent(Object object);
	}
}
