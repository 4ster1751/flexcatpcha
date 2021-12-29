package de.forster.flexcaptcha;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface CaptchaHandler {

	Logger log = LogManager.getLogger(CaptchaHandler.class);
	
	String ALGORITHM_NAME = "SHA-256";
	String DELIMITER = "_";

	/**
	 * Appends a given token with an encrypted self reference used for validation at
	 * a later point. This is required because each handler implementation allows
	 * for a customized validation and token generation logic, and validation of a
	 * token can not be done reliably without knowing the implementation that
	 * created it. This method encrypts the fully qualified name of the
	 * implementation and appends it to the token. The {@link Validator} class can
	 * be used to decrypt the token, instantiate the CaptchaHandler implementation
	 * and run its validation.
	 * 
	 * @param token token to be appended
	 * @param saltSource the salt source used for the encryption.
	 * @param password    the password used to encrypt the implementation reference
	 * @return appended token string
	 */
	default String addSelfReference(CipherHandler cipherHandler, String token, Serializable saltSource, String password) {
		byte[] ivBytes =  cipherHandler.generateIV().getIV();
		byte[] encryptedBytes = cipherHandler.encryptString(this.getClass().getName().getBytes(), password, saltSource, ivBytes);
		String base64 = Base64.getEncoder().encodeToString(encryptedBytes);
		return token+DELIMITER+base64;
	}

	/**
	 * Validates the answer to the captcha based on the token and the salt object.
	 * Returns true if the answer is correct and the token authentic
	 * 
	 * @param answer     the given solution to the captcha to be validated
	 * @param token      the returned token originally created with the captcha
	 * @param saltSource the salt source originally used to salt the hashed solution
	 *                   to create the token. Will be used again to validate the
	 *                   answer
	 * @return boolean whether or not the captcha is valid
	 */
	boolean validate(CipherHandler cipherHandler, String answer, String token, Serializable saltSource);
	
 	/**
	 * Creates the token based on the captcha solution and the object to be used for
	 * salting
	 * 
	 * @param sourceString captcha solution as string
	 * @param saltSource  arbitrary object to be used to salt the solution hash for
	 *                    added security and to allow for authenticating the given
	 *                    answer
	 * @return String of the token
	 */
	default String makeToken(CipherHandler cipherHandler, String sourceString, Serializable saltSource) {
		byte[] captchaTextBytes = sourceString.getBytes();
		byte[] saltObjectBytes = getSaltObjectBytes(saltSource);
		try {
			MessageDigest md = MessageDigest.getInstance(ALGORITHM_NAME);
			md.update(captchaTextBytes);
			md.update(saltObjectBytes);
			byte[] outputBytes = md.digest();
			return Base64.getEncoder().encodeToString(outputBytes);
		} catch (NoSuchAlgorithmException e) {
			log.error("Error creating the captcha token: " + e.getMessage());
			return null;
		}
	}
	


	/**
	 * Converts the given object to a byte array
	 * 
	 * @param saltSource object to be used as salt
	 * @return byte array of the object
	 */
	default byte[] getSaltObjectBytes(Serializable saltSource) {
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(saltSource);
			baos.close();
			oos.close();
			return baos.toByteArray();
		} catch (IOException e) {
			log.error("Error converting the salt object source to byte array: " + e.getMessage());
			return null;
		}
	}


}