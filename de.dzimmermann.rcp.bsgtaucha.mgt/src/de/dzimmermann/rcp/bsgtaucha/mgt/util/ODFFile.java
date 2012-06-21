package de.dzimmermann.rcp.bsgtaucha.mgt.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;

import de.dzimmermann.rcp.basicplatform.ui.util.Logger;
import de.dzimmermann.rcp.bsgtaucha.mgt.Activator;

public enum ODFFile {

	//
	PERSON_SIMPLE(false, true, "Personenliste (CSV-Datei)", null,
			new ContentEntry[] { new ContentEntry(null,
					"csv_person_simple.xslt", false) }, new Parameter[] {}),
	//
	// PERSON_COMPLEX(true, true, null, new ContentEntry[] {}, new Parameter[]
	// {}),
	//
	BAND_ACTIONS_SIMPLE(false, false, "Bänderliste (CSV-Datei)", null,
			new ContentEntry[] { new ContentEntry(null,
					"csv_bandactions_simple.xslt", false) }, new Parameter[] {}),
	//
	BAND_ACTIONS_COMPLEX(true, false, "Bänderliste (OpenDocument-Tabelle)",
			"band-action-template.ods", new ContentEntry[] { new ContentEntry(
					"content.xml", "band_action_content.xslt", true) },
			new Parameter[] {}),
	//
	ENTRIES_SIMPLE(false, false, "Tätigkeiten (CSV-Datei)", null,
			new ContentEntry[] { new ContentEntry(null,
					"csv_entries_simple.xslt", false) }, new Parameter[] {});

	private final boolean complex;
	private final boolean needAdmin;

	private final String name;

	private final InputStream template;

	private final ContentEntry[] contentEntries;
	private final Parameter[] parameters;

	private ODFFile(boolean complex, boolean needAdmin, String name,
			String internalTemplate, ContentEntry[] contentEntries,
			Parameter[] parameters) {

		this.complex = complex;
		this.needAdmin = needAdmin;

		this.name = name;

		InputStream tmp_template = null;
		try {
			String path = "/export-templates/template/" + internalTemplate;
			URL pluginBasedURL = new URL("platform:/plugin/"
					+ Activator.PLUGIN_ID + path);
			if (FileLocator.find(pluginBasedURL) != null) {
				tmp_template = FileLocator.openStream(Activator.getDefault()
						.getBundle(), new Path(path), false);
			}
		} catch (MalformedURLException e) {
			tmp_template = null;
			Logger.logError(e);
		} catch (IOException e) {
			tmp_template = null;
			Logger.logError(e);
		}
		this.template = tmp_template;

		this.contentEntries = contentEntries;
		this.parameters = parameters;
	}

	public boolean isComplex() {
		return complex;
	}

	public boolean isNeedAdmin() {
		return needAdmin;
	}

	public String getName() {
		return name;
	}

	public InputStream getTemplate() {
		return template;
	}

	public ContentEntry[] getContentEntries() {
		return contentEntries;
	}

	public Parameter[] getParameters() {
		return parameters;
	}

	public static class ContentEntry {

		private String entryName;
		private InputStream xslt;

		public ContentEntry() {
		}

		public ContentEntry(String entryName, String internalXslt,
				boolean complex) {
			this.entryName = entryName;
			try {
				String path = "/export-templates/"
						+ (complex ? "complex/" : "plain/") + internalXslt;
				URL pluginBasedURL = new URL("platform:/plugin/"
						+ Activator.PLUGIN_ID + path);
				if (FileLocator.find(pluginBasedURL) != null) {
					xslt = FileLocator.openStream(Activator.getDefault()
							.getBundle(), new Path(path), false);
				}
			} catch (MalformedURLException e) {
				xslt = null;
				Logger.logError(e);
			} catch (IOException e) {
				xslt = null;
				Logger.logError(e);
			}
		}

		public String getEntryName() {
			return entryName;
		}

		public void setEntryName(String entryName) {
			this.entryName = entryName;
		}

		public InputStream getXslt() {
			return xslt;
		}

		public void setXslt(InputStream xslt) {
			this.xslt = xslt;
		}
	}

	public static class Parameter {

		private String parameter;
		private String value;

		public Parameter() {
		}

		public Parameter(String parameter, String value) {
			this.parameter = parameter;
			this.value = value;
		}

		public String getParameter() {
			return parameter;
		}

		public void setParameter(String parameter) {
			this.parameter = parameter;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}
}
