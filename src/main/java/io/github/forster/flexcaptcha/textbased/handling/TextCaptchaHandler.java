package io.github.forster.flexcaptcha.textbased.handling;

import java.io.Serializable;

import io.github.forster.flexcaptcha.CaptchaHandler;
import io.github.forster.flexcaptcha.CipherHandler;
import io.github.forster.flexcaptcha.textbased.TextCaptcha;
import io.github.forster.flexcaptcha.textbased.enums.Case;
import io.github.forster.flexcaptcha.textbased.rendering.TextImageRenderer;
import io.github.forster.flexcaptcha.textbased.textgen.CaptchaTextGenerator;

/**
 * Interface for the various ways in which a captcha could potentially be
 * created.
 * 
 * @author Yannick Forster
 *
 */
public interface TextCaptchaHandler extends CaptchaHandler {

	/**
	 * Generates a captcha of a given character length and salts the hashed solution
	 * with the given object for checking authenticity during verification. Uses the
	 * given String as a source of all possible characters from which the captcha
	 * string is to be generated with mixed case.
	 * 
	 * @param length          specifies the length
	 * @param saltSource      Object used during creation of the captcha token to
	 *                        ensure authenticity
	 * @param password        the password used to encrypt the implementation
	 *                        reference
	 * @param characterSource String from which the random characters used during
	 *                        creation of the captcha are picked
	 * @return Captcha object containing the image data of the visual captcha and
	 *         the token containing the hashed and salted solution
	 */
	default public TextCaptcha generate(int length, CipherHandler cipherHandler, Serializable saltSource, String password,
			CaptchaTextGenerator textgenerator, TextImageRenderer renderer, int height, int width) {
		return generate(length, cipherHandler, saltSource, password, textgenerator, Case.MIXEDCASE, renderer, height, width);
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
	 * @param password        the password used to encrypt the implementation
	 *                        reference
	 * @param characterSource String from which the random characters used during
	 *                        creation of the captcha are picked
	 * @param charCase        Case enum specifying the case in which the captcha
	 *                        text letters should be generated
	 * @return Captcha object containing the image data of the visual captcha and
	 *         the token containing the hashed and salted solution
	 */
	public TextCaptcha generate(int length, CipherHandler cipherHandler, Serializable saltSource, String password,
			CaptchaTextGenerator textgenerator, Case charCase, TextImageRenderer renderer, int height, int width);

	/**
	 * Generates a captcha from a given string and salt object
	 * 
	 * @param captchaText predefined string from which the image and the token are
	 *                    generated
	 * @param saltSource  Object used during creation of the captcha token to ensure
	 *                    authenticity
	 * @param password    the password used to encrypt the implementation reference
	 * @return Captcha object containing the image data of the visual captcha and
	 *         the token containing the hashed and salted solution
	 */
	public TextCaptcha toCaptcha(String captchaText, CipherHandler cipherHandler, Serializable saltSource, String password,
			TextImageRenderer renderer, int height, int width);

}
