package de.forster.flexcaptcha;

import java.io.Serializable;
import java.util.Base64;

import de.forster.flexcaptcha.enums.Case;
import de.forster.flexcaptcha.rendering.CaptchaImageRenderer;
import de.forster.flexcaptcha.textgen.CaptchaTextGenerator;

/**
 * Interface for the various ways in which a captcha could potentially be
 * created.
 * 
 * @author Yannick Forster
 *
 */
public interface CaptchaHandler {

	String DELIMITER = "_";
	String password =  "ThisIsMyPassword";

	/**
	 * Generates a captcha of a given character length and salts the hashed solution
	 * with the given object for checking authenticity during verification. Uses the
	 * given String as a source of all possible characters from which the captcha
	 * string is to be generated with mixed case.
	 * 
	 * @param length          specifies the length
	 * @param saltSource      Object used during creation of the captcha token to
	 *                        ensure authenticity
	 * @param characterSource String from which the random characters used during
	 *                        creation of the captcha are picked
	 * @return Captcha object containing the image data of the visual captcha and
	 *         the token containing the hashed and salted solution
	 */
	default public Captcha generate(int length, Serializable saltSource, CaptchaTextGenerator textgenerator, CaptchaImageRenderer renderer, int height, int width) {
		return generate(length,  saltSource, textgenerator, Case.MIXEDCASE, renderer, height, width);
	}
	
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
	 * @return appended token string
	 */
	default String addSelfReference(String token, Serializable saltSource) {
		CipherHandler ch = new CipherHandler();
		byte[] ivBytes = ch.generateIV().getIV();
		byte[] encryptedBytes = ch.encryptString(this.getClass().getName().getBytes(), password, saltSource, ivBytes);
		String base64 = Base64.getEncoder().encodeToString(encryptedBytes);
		return token+DELIMITER+base64;
	}

	/**
	 * Generates a captcha of a given character length and salts the hashed solution
	 * with the given object for checking authenticity during verification. Uses the
	 * given String as a source of all possible characters from which the captcha
	 * string is to be generated with the specified case.
	 * 
	 * @param length          specifies the length
	 * @param saltSource      Object used during creation of the captcha token to
	 *                        ensure authenticity
	 * @param characterSource String from which the random characters used during
	 *                        creation of the captcha are picked
	 * @param charCase        Case enum specifying the case in which the captcha
	 *                        text letters should be generated
	 * @return Captcha object containing the image data of the visual captcha and
	 *         the token containing the hashed and salted solution
	 */
	public Captcha generate(int length,  Serializable saltSource, CaptchaTextGenerator textgenerator, Case charCase, CaptchaImageRenderer renderer, int height, int width);

	/**
	 * Generates a captcha from a given string and salt object
	 * 
	 * @param captchaText predefined string from which the image and the token are
	 *                    generated
	 * @return Captcha object containing the image data of the visual captcha and
	 *         the token containing the hashed and salted solution
	 */
	public Captcha toCaptcha(String captchaText, Serializable saltSource, CaptchaImageRenderer renderer, int height, int width);

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
	public boolean validate(String answer, String token, Serializable saltSource);

}
