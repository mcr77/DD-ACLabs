package de.dialogdata.aclabs.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtils {
	
	public static String encryptString(String input) {
		if (input != null) {
			MessageDigest messageDigest;
			try {//test
				messageDigest = MessageDigest.getInstance("SHA-1");
				messageDigest.update(input.getBytes(), 0, input.length());
				String hexEncrypted = new BigInteger(1, messageDigest.digest()).toString(16);
				return hexEncrypted;
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException("Not very likely ... ");
			}
		} else {
			return null;
		}

	}
}
