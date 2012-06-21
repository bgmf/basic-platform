package de.dzimmermann.rcp.bsgtaucha.mgt.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.core.runtime.Platform;

public class DesEncrypter {

	public static DesEncrypter getInstance(String passPhrase) {
		return new DesEncrypter(passPhrase);
	}

	public static boolean isEqualPassPhrase(String cipher, String passPhrase) {
		try {
			if (cipher != null && passPhrase == null) {
				return false;
			}
			if (cipher == null && passPhrase != null) {
				return true;
			}
			if (cipher == null && passPhrase == null) {
				return true;
			}
			if (Platform.getOS().toLowerCase().contains("win")) {
				return MessageDigest.isEqual(cipher.getBytes("windows-1252"),
						getDigest(passPhrase).getBytes("windows-1252"));
			} else {
				return MessageDigest.isEqual(cipher.getBytes("UTF-8"),
						getDigest(passPhrase).getBytes("UTF-8"));
			}
		} catch (UnsupportedEncodingException e) {
		}
		return false;
	}

	public static String getDigest(String content) {
		if (content == null)
			return null;
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1", "SUN");
			if (Platform.getOS().toLowerCase().contains("win")) {
				return new String(digest.digest(content
						.getBytes("windows-1252")), "windows-1252");
			} else {
				return new String(digest.digest(content.getBytes("UTF-8")),
						"UTF-8");
			}
		} catch (NoSuchAlgorithmException e) {
		} catch (NoSuchProviderException e) {
		} catch (UnsupportedEncodingException e) {
		}
		return null;
	}

	Cipher ecipher;
	Cipher dcipher;

	// 8-byte Salt
	byte[] salt = { (byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
			(byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03 };

	// Iteration count
	int iterationCount = 19;

	DesEncrypter(String passPhrase) {
		if (passPhrase == null)
			return;
		try {
			// Create the key
			KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), salt,
					iterationCount);
			SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES")
					.generateSecret(keySpec);
			ecipher = Cipher.getInstance(key.getAlgorithm());
			dcipher = Cipher.getInstance(key.getAlgorithm());

			// Prepare the parameter to the ciphers
			AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt,
					iterationCount);

			// Create the ciphers
			ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
			dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
		} catch (java.security.InvalidAlgorithmParameterException e) {
		} catch (java.security.spec.InvalidKeySpecException e) {
		} catch (javax.crypto.NoSuchPaddingException e) {
		} catch (java.security.NoSuchAlgorithmException e) {
		} catch (java.security.InvalidKeyException e) {
		}
	}

	public String encrypt(String str) {
		try {
			// Encode the string into bytes using utf-8
			byte[] utf8 = str.getBytes("UTF8");

			// Encrypt
			byte[] enc = ecipher.doFinal(utf8);

			// Encode bytes to base64 to get a string
			return new String(new Base64().encode(enc));
		} catch (javax.crypto.BadPaddingException e) {
		} catch (IllegalBlockSizeException e) {
		} catch (UnsupportedEncodingException e) {
		} catch (Exception e) {
		}
		return null;
	}

	public String decrypt(String str) {
		try {
			// Decode base64 to get bytes
			byte[] dec = new Base64().decode(str.getBytes());

			// Decrypt
			byte[] utf8 = dcipher.doFinal(dec);

			// Decode using utf-8
			return new String(utf8, "UTF8");
		} catch (javax.crypto.BadPaddingException e) {
		} catch (IllegalBlockSizeException e) {
		} catch (UnsupportedEncodingException e) {
		} catch (Exception e) {
		}
		return null;
	}
}
