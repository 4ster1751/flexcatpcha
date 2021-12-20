package de.forster.flexcaptcha.core;

import java.io.Serializable;

/**
 * Interface for the various ways in which a captcha could potentially be
 * created.
 * 
 * @author Yannick Forster
 *
 */
public interface CaptchaHandler {

	/**
	 * Generates a captcha of a given character length and salts the hashed solution
	 * with the given object for checking authenticity during verification. Uses the
	 * library default character base for randomizing the captcha string with mixed
	 * case.
	 * 
	 * @param length     specifies the length
	 * @param saltSource Object used during creation of the captcha token to ensure
	 *                   authenticity
	 * @return Captcha object containing the image data of the visual captcha and
	 *         the token containing the hashed and salted solution
	 */
	default public Captcha generate(int length, Serializable saltSource, CaptchaImageRenderer renderer, int height, int width) {
		CaptchaTextGenerator generator = new CaptchaTextGenerator();
		String defaultCharBase = generator.getDEFAULT_CHARACTER_BASE();
		return generate(length, defaultCharBase, saltSource, renderer, height, width);
	}

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
	default public Captcha generate(int length, String characterSource, Serializable saltSource, CaptchaImageRenderer renderer, int height, int width) {
		return generate(length, characterSource, Case.MIXEDCASE, saltSource, renderer, height, width);
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
	public Captcha generate(int length, String characterSource, Case charCase, Serializable saltSource, CaptchaImageRenderer renderer, int height, int width);

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
