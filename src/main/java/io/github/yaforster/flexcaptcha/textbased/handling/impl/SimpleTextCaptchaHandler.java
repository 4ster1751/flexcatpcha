package io.github.yaforster.flexcaptcha.textbased.handling.impl;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.yaforster.flexcaptcha.CipherHandler;
import io.github.yaforster.flexcaptcha.textbased.TextCaptcha;
import io.github.yaforster.flexcaptcha.textbased.enums.Case;
import io.github.yaforster.flexcaptcha.textbased.handling.TextCaptchaHandler;
import io.github.yaforster.flexcaptcha.textbased.rendering.TextImageRenderer;
import io.github.yaforster.flexcaptcha.textbased.textgen.CaptchaTextGenerator;

/**
 * Provides basic captcha handling in regards to generating a simplistic visual
 * representation of the text-based captcha string as well as hashing the token
 * and salt object
 * 
 * @author Yannick Forster
 *
 */
public class SimpleTextCaptchaHandler implements TextCaptchaHandler {

	/**
	 * Log4J Logger
	 */
	Logger log = LogManager.getLogger(SimpleTextCaptchaHandler.class);

	/**
	 * The image format
	 */
	private static final String IMG_FORMAT = "JPEG";

	/**
	 * Generates a TextCaptcha object containing the token and the images.
	 */
	@Override
	public TextCaptcha generate(int length, CipherHandler cipherHandler, Serializable saltSource, String password,
			CaptchaTextGenerator textgenerator, Case charCase, TextImageRenderer renderer, int height, int width, boolean addSelfReference) {
		checkInputs(length, textgenerator, renderer, height, width);
		String captchaText = textgenerator.generate(length, textgenerator.generate(length, charCase), charCase);
		return makeTextCaptcha(saltSource, cipherHandler, password, renderer, height, width, captchaText, addSelfReference);
	}

	/**
	 * Validates whether or not the answer to the given captcha is correct. To do
	 * this, the answer and the salt source are combined and checked against the
	 * token
	 */
	@Override
	public boolean validate(String answer, String token, CipherHandler cipherHandler, Serializable saltSource, String password) {
		return token.split(DELIMITER)[0].equals(makeToken(answer, saltSource));
	}

	/**
	 * Generates the completed captcha object with a picture based on the specified
	 * text directly, The implementation of creating the image is given by the
	 * specified renderer. The salt source specifies an arbitrary object used to
	 * salt the token. Use this method if you want a captcha knowing the solution
	 * beforehand, as opposed to have it randomly generated.
	 * 
	 */
	@Override
	public TextCaptcha toCaptcha(String captchaText, CipherHandler cipherHandler, Serializable saltSource, String password,
			TextImageRenderer renderer, int height, int width, boolean addSelfReference) {
		return makeTextCaptcha(saltSource, cipherHandler, password, renderer, height, width, captchaText, addSelfReference);
	}

	/**
	 * Generates the completed captcha object with a picture based on the specified
	 * text, heigth and width. The implementation of creating the image is given by
	 * the specified renderer. The salt source specifies an arbitrary object used to
	 * salt the token.
	 * 
	 * @param saltSource  arbitrary object to be used to salt the solution hash for
	 *                    added security and to allow for authenticating the given
	 *                    answer
	 * @param password    the password used to encrypt the implementation reference
	 * @param renderer    Implementation of the ImageRenderer interface controlling
	 *                    how the specified captcha is generated as an image.
	 * @param height      pixel height of the captcha image
	 * @param width       pixel width of the catpcha image
	 * @param captchaText text the catpcha should display
	 * @return {@link TextCaptcha} containing the finalized captcha
	 */
	private TextCaptcha makeTextCaptcha(Serializable saltSource, CipherHandler cipherHandler, String password, TextImageRenderer renderer,
			int height, int width, String captchaText, boolean addSelfReference) {
		BufferedImage image = renderer.render(captchaText, height, width);
		try {
			byte[] imgData = convertImageToByteArray(image, IMG_FORMAT);
			if (imgData != null) {
				String token = makeToken(captchaText, saltSource);
				if(addSelfReference) {
					token = addSelfReference(cipherHandler, token, saltSource, password);
				}
				TextCaptcha captcha = new TextCaptcha(imgData, token);
				return captcha;
			}
		} catch (IOException e) {
			log.error("Error creating the captcha object: " + e.getMessage());
			return null;
		}
		return null;
	}

}
