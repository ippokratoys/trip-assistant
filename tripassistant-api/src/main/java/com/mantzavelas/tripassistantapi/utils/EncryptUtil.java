package com.mantzavelas.tripassistantapi.utils;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

public enum EncryptUtil {

	;

	private static final String password = PropertyUtil.getProperty("app.security.encPassword");
	private static final String salt = PropertyUtil.getProperty("app.security.encSalt");
	private static TextEncryptor encryptor = Encryptors.text(password, salt);

	public static String encrypt(String data) {
		return encryptor.encrypt(data);
	}

	public static String decrypt(String encryptedData) {
		return encryptor.decrypt(encryptedData);
	}
}
