package de.dzimmermann.rcp.bsgtaucha.mgt.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.xml.bind.JAXBException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

import de.dzimmermann.rcp.bsgtaucha.mgt.model.BandTypes;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.EntryType;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.ModelComparator;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.ModelComparator.Type;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.PersonType;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.RootType;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.WorkType;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.intermediate.BandActionsRoot;
import de.dzimmermann.rcp.bsgtaucha.mgt.model.intermediate.PersonListRoot;

public class OpenDocumentCreator {

	/**
	 * A different method of creating OpenDocument files...<br>
	 * <ol>
	 * <li>start by iterating over openDocumentTemplate zip entries</li>
	 * <li>create a new output zip (ODF) file by using the outputFile parameter</li>
	 * <li>test if the contentEntries map contains an equally named entry
	 * <ul>
	 * <li>contains equal entry: create a new entry and push it into the output
	 * zip file</li>
	 * <li>not containing an equal entry: use the current entry from the
	 * template file</li>
	 * <li>not containing any entries: same as above</li>
	 * </ul>
	 * </li>
	 * <li>push the template files content into the outputFile</li>
	 * <li>close the current entry and procced with the first step until no more
	 * entries are found</li>
	 * </ol>
	 * 
	 * @param openDocumentTemplate
	 *            the ODF file (a zip file with several xml files for the
	 *            content... and other content)
	 * @param outputFile
	 *            the ODF file created by using the template and - if necessarry
	 *            - replaced content entries
	 * @param contentEntries
	 *            a {@link Map} of content entries (e.g. "content.xml", ...) and
	 *            a respective XSLT files
	 * @param sourceDoc
	 *            the source document which will be processed through the XSLT
	 *            file (most commonly the clients.xml, the worklist.xml or in
	 *            rare occasions the grouped_clients.xml)
	 * @param xsltParameters
	 *            sometimes some of the XSLT documents might use parameters for
	 *            some special document specific settings or additional variable
	 *            content such as an authors name
	 * @throws IOException
	 *             used to catch different exceptions which can occur on the ODF
	 *             zip file handling
	 * @throws TransformerException
	 *             only occurs, when the XSLT transformation fails
	 */
	public static void createODFFile(File openDocumentTemplate,
			File outputFile, Map<String, File> contentEntries, File sourceDoc,
			Map<String, String> xsltParameters) throws IOException,
			TransformerException {

		byte[] buf = new byte[1024];
		int len;

		ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(
				openDocumentTemplate));

		ZipOutputStream outStream = new ZipOutputStream(new FileOutputStream(
				outputFile));

		ZipEntry entry = null;

		while ((entry = zipInputStream.getNextEntry()) != null) {

			boolean isContained = false;
			for (String key : contentEntries.keySet()) {
				if (entry.getName().equals(key)) {
					isContained = true;
				}
			}

			if (!isContained) {

				outStream.putNextEntry(new ZipEntry(entry.getName()));

				while ((len = zipInputStream.read(buf)) > 0)
					outStream.write(buf, 0, len);

				outStream.closeEntry();
			}

		}

		for (String key : contentEntries.keySet()) {

			if (contentEntries.get(key) == null
					|| !contentEntries.get(key).isFile()) {
				throw new RuntimeException(
						contentEntries.get(key) == null ? "XSLT file for entry "
								+ key + " is null!"
								: !contentEntries.get(key).isFile() ? "Content "
										+ key
										+ " can't be handled: Invalid file!"
										: "Cannot handle content entry "
												+ key
												+ " with connected transformation file "
												+ contentEntries.get(key));
			}

			ZipEntry newEntry = new ZipEntry(key);

			outStream.putNextEntry(newEntry);

			writeXmlDocument(contentEntries.get(key), sourceDoc, outStream,
					xsltParameters);

			outStream.closeEntry();
		}

		try {
			outStream.close();
		} catch (IOException e) {
		}
		try {
			zipInputStream.close();
		} catch (IOException e) {
		}
	}

	public static void createODFFile(InputStream openDocumentTemplate,
			File outputFile, Map<String, InputStream> contentEntries,
			InputStream sourceDoc, Map<String, String> xsltParameters)
			throws IOException, TransformerException {

		byte[] buf = new byte[1024];
		int len;

		ZipInputStream zipInputStream = new ZipInputStream(openDocumentTemplate);

		ZipOutputStream outStream = new ZipOutputStream(new FileOutputStream(
				outputFile));

		ZipEntry entry = null;

		while ((entry = zipInputStream.getNextEntry()) != null) {

			boolean isContained = false;
			for (String key : contentEntries.keySet()) {
				if (entry.getName().equals(key)) {
					isContained = true;
				}
			}

			if (!isContained) {

				outStream.putNextEntry(new ZipEntry(entry.getName()));

				while ((len = zipInputStream.read(buf)) > 0)
					outStream.write(buf, 0, len);

				outStream.closeEntry();
			}

		}

		for (String key : contentEntries.keySet()) {

			if (contentEntries.get(key) == null) {
				throw new RuntimeException(
						contentEntries.get(key) == null ? "XSLT file for entry "
								+ key + " is null!"
								: contentEntries.get(key) == null ? "Content "
										+ key
										+ " can't be handled: Invalid file!"
										: "Cannot handle content entry "
												+ key
												+ " with connected transformation file "
												+ contentEntries.get(key));
			}

			ZipEntry newEntry = new ZipEntry(key);

			outStream.putNextEntry(newEntry);

			StreamSource xsltSource = new StreamSource(contentEntries.get(key));
			StreamSource source = new StreamSource(sourceDoc);
			StreamResult result = new StreamResult(outStream);
			writeXmlDocument(xsltSource, source, result, xsltParameters);

			outStream.closeEntry();
		}

		try {
			outStream.close();
		} catch (IOException e) {
		}
		try {
			zipInputStream.close();
		} catch (IOException e) {
		}
	}

	public static void writeXmlDocument(Document doc, OutputStream out)
			throws TransformerException {

		writeXmlDocument(null, doc, out);
	}

	public static void writeXmlDocument(File xsltFile, File sourceDoc,
			OutputStream out) throws TransformerException {
		writeXmlDocument(xsltFile, sourceDoc, out, null);
	}

	public static void writeXmlDocument(File xsltFile, File sourceDoc,
			OutputStream out, Map<String, String> xsltParameter)
			throws TransformerException {

		Source source = new StreamSource(sourceDoc);

		Result result = new StreamResult(new PrintWriter(out));

		Source xsltSource = null;

		if (xsltFile != null) {
			xsltSource = new StreamSource(xsltFile);
		}

		writeXmlDocument(xsltSource, source, result, xsltParameter);
	}

	public static void writeXmlDocument(InputStream xsltFile, File sourceDoc,
			OutputStream out) throws TransformerException {
		writeXmlDocument(xsltFile, sourceDoc, out, null);
	}

	public static void writeXmlDocument(InputStream xsltFile, File sourceDoc,
			OutputStream out, Map<String, String> xsltParameter)
			throws TransformerException {
		writeXmlDocument(xsltFile, sourceDoc, out, xsltParameter, null);
	}

	public static void writeXmlDocument(InputStream xsltFile, File sourceDoc,
			OutputStream out, Map<String, String> xsltParameter, String encoding)
			throws TransformerException {

		Source source = new StreamSource(sourceDoc);

		Result result = new StreamResult(new PrintWriter(out));

		Source xsltSource = null;

		if (xsltFile != null) {
			xsltSource = new StreamSource(xsltFile);
		}

		writeXmlDocument(xsltSource, source, result, xsltParameter, encoding);
	}

	public static void writeXmlDocument(File xsltFile, Document sourceDoc,
			OutputStream out) throws TransformerException {
		writeXmlDocument(xsltFile, sourceDoc, out, null);
	}

	public static void writeXmlDocument(File xsltFile, Document sourceDoc,
			OutputStream out, Map<String, String> xsltParameter)
			throws TransformerException {

		Source source = new DOMSource(sourceDoc);

		Result result = new StreamResult(new PrintWriter(out));

		Source xsltSource = null;

		if (xsltFile != null) {
			xsltSource = new StreamSource(xsltFile);
		}

		writeXmlDocument(xsltSource, source, result, xsltParameter);
	}

	public static void writeXmlDocument(Source xsltDocument,
			Source sourceDocument, Result resultDocument,
			Map<String, String> xsltParameter) throws TransformerException {
		writeXmlDocument(xsltDocument, sourceDocument, resultDocument,
				xsltParameter, null);
	}

	public static void writeXmlDocument(Source xsltDocument,
			Source sourceDocument, Result resultDocument,
			Map<String, String> xsltParameter, String encoding)
			throws TransformerException {

		TransformerFactory tf = TransformerFactory.newInstance();

		try {
			tf.setAttribute("http://saxon.sf.net/feature/version-warning",
					Boolean.valueOf(false));
		} catch (IllegalArgumentException e) {
		}

		try {
			tf.setAttribute("indent-number", new Integer(2));

		} catch (IllegalArgumentException e) {
		}

		Transformer trans;

		if (xsltDocument != null) {
			trans = tf.newTransformer(xsltDocument);
		} else
			trans = tf.newTransformer();

		if (xsltDocument != null && xsltParameter != null
				&& !xsltParameter.isEmpty()) {
			for (String key : xsltParameter.keySet()) {
				trans.setParameter(key, xsltParameter.get(key));
			}
		}

		trans.setOutputProperty(OutputKeys.INDENT, "yes");
		if (encoding != null && !encoding.isEmpty()) {
			trans.setOutputProperty(OutputKeys.ENCODING, encoding);
		}
		trans.transform(sourceDocument, resultDocument);
	}

	public static File extractModelDataIntoFile(RootType model, ODFFile type)
			throws IOException, JAXBException {

		File sourceFile = null;

		RootType clone = model.getClone();
		SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");

		switch (type) {
		case PERSON_SIMPLE:
			Collections.sort(clone.getPersons().getPerson(),
					new ModelComparator(clone, Type.DEFAULT));
			PersonListRoot persons = new PersonListRoot();
			for (PersonType dataPerson : clone.getPersons().getPerson()) {
				PersonListRoot.Person p = new PersonListRoot.Person();
				p.setActiveMember(dataPerson.isActiveMember() != null
						&& dataPerson.isActiveMember());
				p.setBirthday(dataPerson.getBirthday());
				p.setCity(dataPerson.getCity());
				p.setEmail(dataPerson.getEmail());
				p.setFax(dataPerson.getFax());
				p.setFirstName(dataPerson.getFirstname());
				p.setMobile(dataPerson.getMobile());
				p.setName(dataPerson.getName());
				p.setPhone(dataPerson.getPhone());
				p.setPostalCode(dataPerson.getPostalcode());
				p.setStreet(dataPerson.getStreet());
				persons.getPersons().add(p);
			}
			sourceFile = File.createTempFile("tmp-person-list-", ".tmp",
					new File(System.getProperty("java.io.tmpdir")));
			BSGTauchaUtils.saveModel(PersonListRoot.class, persons, sourceFile);
			break;
		case BAND_ACTIONS_SIMPLE:
		case BAND_ACTIONS_COMPLEX:
			Collections.sort(clone.getWorks().getEntry(), new ModelComparator(
					clone, Type.DEFAULT));
			BandActionsRoot bandActions = new BandActionsRoot();
			for (EntryType entry : clone.getWorks().getEntry()) {
				if (entry.getBandAction() == null)
					continue;
				BandActionsRoot.BandAction ba = new BandActionsRoot.BandAction();
				ba.setAmount(entry.getBandAction().getAmount());
				ba.setBandAdded(entry.getBandAction().getAmount() > 0);
				if (ba.isBandAdded()) {
					ba.setCause("Eingang"); // XXX static value sensible?
					ba.setComment("Bestellung");
				} else {
					WorkType wt = getWorkByID(clone, entry.getWorkId());
					ba.setCause(wt != null ? wt.getName() : "");
					ba.setComment(entry.getComment() != null ? entry
							.getComment().replace("\"", "'") : "");
				}
				ba.setDate(entry.getDate());
				ba.setDateString(df.format(entry.getDate()));
				ba.setTime(entry.getTime() == 0 ? 0 : entry.getTime());
				PersonType person = getPersonByID(clone, entry.getPersonId());
				ba.setPersonString(person != null ? ((person.getFirstname())
						+ " " + (person.getName())) : "");
				ba.setBandTypes(entry.getBandAction().getBandType());
				ba.setType(getTypeValue(entry.getBandAction().getBandType()));
				ba.setFullAmountC(getFullAmountToDate(clone, entry.getDate(),
						BandTypes.CONTINUOUS));
				ba.setFullAmountL(getFullAmountToDate(clone, entry.getDate(),
						BandTypes.LATERAL));
				ba.getTrack().addAll(entry.getBandAction().getTrack());
				ba.setTrackString(BandActionsRoot.BandAction
						.getTrackString(entry.getBandAction()));
				bandActions.getBandActions().add(ba);
			}
			sourceFile = File.createTempFile("tmp-ba-list-", ".tmp", new File(
					System.getProperty("java.io.tmpdir")));
			BSGTauchaUtils.saveModel(BandActionsRoot.class, bandActions,
					sourceFile);
			break;
		case ENTRIES_SIMPLE:
			Collections.sort(clone.getWorks().getEntry(), new ModelComparator(
					clone, Type.DEFAULT));
			BandActionsRoot bandActions2 = new BandActionsRoot();
			for (EntryType entry : clone.getWorks().getEntry()) {
				BandActionsRoot.BandAction ba = new BandActionsRoot.BandAction();
				WorkType wt = getWorkByID(clone, entry.getWorkId());
				ba.setCause(wt != null ? wt.getName() : "");
				ba.setComment(entry.getComment() != null ? entry.getComment()
						.replace("\"", "'") : "");
				ba.setDate(entry.getDate());
				ba.setDateString(df.format(entry.getDate()));
				ba.setTime(entry.getTime() == 0 ? 0 : entry.getTime());
				PersonType person = getPersonByID(clone, entry.getPersonId());
				ba.setPersonString(person != null ? ((person.getFirstname())
						+ " " + (person.getName())) : "");
				if (entry.getBandAction() != null) {
					ba.setAmount(entry.getBandAction().getAmount() == null ? null
							: entry.getBandAction().getAmount());
					ba.setBandAdded(entry.getBandAction().getAmount() != null
							&& entry.getBandAction().getAmount() > 0);
					ba.setType(getTypeValue(entry.getBandAction().getBandType()));
					ba.setBandTypes(entry.getBandAction().getBandType());
					ba.getTrack().addAll(entry.getBandAction().getTrack());
					ba.setTrackString(BandActionsRoot.BandAction
							.getTrackString(entry.getBandAction()));
				}
				bandActions2.getBandActions().add(ba);
			}
			sourceFile = File.createTempFile("tmp-entries-", ".tmp", new File(
					System.getProperty("java.io.tmpdir")));
			BSGTauchaUtils.saveModel(BandActionsRoot.class, bandActions2,
					sourceFile);
			break;
		}

		return sourceFile;
	}

	private static WorkType getWorkByID(RootType root, String wid) {
		if (root.getWorks() == null)
			return null;
		for (WorkType wt : root.getWorks().getWork()) {
			if (wt.getId().equals(wid))
				return wt;
		}
		return null;
	}

	private static PersonType getPersonByID(RootType root, String pid) {
		if (root.getPersons() == null)
			return null;
		for (PersonType pt : root.getPersons().getPerson()) {
			if (pt.getId().equals(pid))
				return pt;
		}
		return null;
	}

	private static int getFullAmountToDate(RootType root, Date date,
			BandTypes bt) {
		int amount = 0;
		if (root.getWorks() != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			c.set(Calendar.MILLISECOND, 0);
			Date cleanDate = c.getTime();
			for (EntryType et : root.getWorks().getEntry()) {
				if (et.getBandAction() == null)
					continue;
				if (et.getBandAction().getBandType() != bt)
					continue;
				c.setTime(et.getDate());
				c.set(Calendar.HOUR_OF_DAY, 0);
				c.set(Calendar.MINUTE, 0);
				c.set(Calendar.SECOND, 0);
				c.set(Calendar.MILLISECOND, 0);
				Date toTest = c.getTime();
				if (toTest.getTime() <= cleanDate.getTime()) {
					amount += et.getBandAction().getAmount();
				}
			}
		}
		return amount;
	}

	public static String getTypeValue(BandTypes bt) {
		switch (bt) {
		case CONTINUOUS:
			return "Endlos";
		case LATERAL:
			return "Quer";
		}
		return "";
	}
}
