package de.dzimmermann.rcp.bsgtaucha.mgt.model;

import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import de.dzimmermann.rcp.bsgtaucha.mgt.handler.OpenEditorHandler;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.DesEncrypter;

public class EncryptionDateAdapter extends XmlAdapter<String, Date> {

	@Override
	public Date unmarshal(String v) throws Exception {

		DesEncrypter decrypter = OpenEditorHandler.desEncrypter;
		if (decrypter != null) {
			String result = decrypter.decrypt(v);
			if (result != null) {
				return new Date(DatatypeConverter.parseDate(result)
						.getTimeInMillis());
			}
		}

		return new Date(DatatypeConverter.parseDate(v).getTimeInMillis());
	}

	@Override
	public String marshal(Date v) throws Exception {

		if (v == null) {
			return null;
		}

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(v.getTime());

		DesEncrypter encrypter = OpenEditorHandler.desEncrypter;
		if (encrypter != null) {
			String result = encrypter.encrypt((javax.xml.bind.DatatypeConverter
					.printDate(c)));
			if (result != null) {
				return result;
			}
		}

		return (javax.xml.bind.DatatypeConverter.printDate(c));
	}
}
