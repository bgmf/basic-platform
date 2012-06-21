package de.dzimmermann.rcp.bsgtaucha.mgt.model;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import de.dzimmermann.rcp.bsgtaucha.mgt.handler.OpenEditorHandler;
import de.dzimmermann.rcp.bsgtaucha.mgt.util.DesEncrypter;

public class EncryptionDecryptionAdapter extends XmlAdapter<String, String> {

	@Override
	public String unmarshal(String v) throws Exception {

		String result = v;

		DesEncrypter decrypter = OpenEditorHandler.desEncrypter;
		if (decrypter != null) {
			result = decrypter.decrypt(result);
			if (result == null) {
				result = v;
			}
		}

		return result;
	}

	@Override
	public String marshal(String v) throws Exception {

		String result = v;

		DesEncrypter encrypter = OpenEditorHandler.desEncrypter;
		if (encrypter != null) {
			result = encrypter.encrypt(result);
			if (result == null) {
				result = v;
			}
		}

		return result;
	}
}
