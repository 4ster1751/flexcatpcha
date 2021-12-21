package de.forster.flexcaptcha;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Base64;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.forster.flexcaptcha.handling.CaptchaHandler;

public class Validator {
	Logger log = LogManager.getLogger(Validator.class);
	
	public Boolean validateInput(String input, String token, Serializable saltSource, String password, CipherHandler cipherHandler) {
			try {
				String splitString = token.split(CaptchaHandler.DELIMITER)[1];
				byte[] splitStringBytes = Base64.getDecoder().decode(splitString);
				String decryptedName = new String(cipherHandler.decryptString(splitStringBytes, password, saltSource));
				Class<?> handler = Class.forName(decryptedName);
				Constructor<?> constructor = handler.getConstructor();
				CaptchaHandler instanceOfMyClass = (CaptchaHandler) constructor.newInstance(new Object[]{});
				return instanceOfMyClass.validate(input, token.split(CaptchaHandler.DELIMITER)[0], saltSource);
			} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
					| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				log.fatal("Could not instantiate CaptchaHandler implementation from decrypted token. Validation can not proceed.");
				e.printStackTrace();
				return null;
			}
			
	}

}
