package de.forster.flexcaptcha;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Base64;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Validator {
	Logger log = LogManager.getLogger(Validator.class);

	/**
	 * Validates the given answer by comparing it to the token if hashed and salted
	 * the same way. The passed ciperHandler will decrypt the CaptchaHandler
	 * implementation and run its validation method.
	 * 
	 * @param input         the given answer to be validated
	 * @param token         the returned token generated with the original captcha
	 * @param saltSource    the salt source used to generate the captcha
	 * @param password    	the password used to encrypt the implementation reference
	 * @param cipherHandler Cipherhandler object used to decrypt the name of the
	 *                      implementation from the token
	 * @return {@link Boolean} of the validation result. null if the validation
	 *         encountered a problem.
	 */
	public Boolean validateInput(String input, CipherHandler cipherHandler, String token, Serializable saltSource, String password) {
			try {
				System.out.println(token);
				String splitString = token.split(CaptchaHandler.DELIMITER)[1];
				byte[] splitStringBytes = Base64.getDecoder().decode(splitString);
				String decryptedName = new String(cipherHandler.decryptString(splitStringBytes, password, saltSource));
				Class<?> handler = Class.forName(decryptedName);
				Constructor<?> constructor = handler.getConstructor();
				CaptchaHandler instanceOfMyClass = (CaptchaHandler) constructor.newInstance(new Object[]{});
				return instanceOfMyClass.validate(input, token.split(CaptchaHandler.DELIMITER)[0], saltSource);
			}
			catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
					| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				log.fatal("Could not instantiate CaptchaHandler implementation from decrypted token. Validation can not proceed.");
				e.printStackTrace();
				return null;
			}
			
	}

}
