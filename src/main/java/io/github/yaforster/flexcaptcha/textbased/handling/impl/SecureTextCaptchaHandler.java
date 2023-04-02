package io.github.yaforster.flexcaptcha.textbased.handling.impl;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.Base64;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.github.yaforster.flexcaptcha.CipherHandler;
import io.github.yaforster.flexcaptcha.textbased.TextCaptcha;
import io.github.yaforster.flexcaptcha.textbased.enums.Case;
import io.github.yaforster.flexcaptcha.textbased.handling.TextCaptchaHandler;
import io.github.yaforster.flexcaptcha.textbased.rendering.TextImageRenderer;
import io.github.yaforster.flexcaptcha.textbased.textgen.CaptchaTextGenerator;

/**
 * Provides basic captcha handling regarding the generation of a simplistic visual
 * representation of the text-based captcha string as well as encrypting the
 * token and salt object
 * 
 * @author Yannick Forster
 *
 */
public class SecureTextCaptchaHandler implements TextCaptchaHandler {

	/**
	 * Log4J Logger
	 */
	final Logger log = LogManager.getLogger(SimpleTextCaptchaHandler.class);

	/**
	 * The image format
	 */
	private static final String IMG_FORMAT = "JPEG";

	@Override
	public TextCaptcha generate(int length, CipherHandler cipherHandler, Serializable saltSource, String password,
			CaptchaTextGenerator textGenerator, Case charCase, TextImageRenderer renderer, int height, int width, boolean addSelfReference) {
		checkInputs(length, textGenerator, renderer, height, width);
		String captchaText = textGenerator.generate(length, textGenerator.generate(length, charCase), charCase);
		return toCaptcha(captchaText, cipherHandler, saltSource, password, renderer, height, width, addSelfReference);
	}

	@Override
	public TextCaptcha toCaptcha(String captchaText, CipherHandler cipherHandler, Serializable saltSource,
			String password, TextImageRenderer renderer, int height, int width, boolean addSelfReference ) {
		TextCaptcha captcha;
		BufferedImage image = renderer.render(captchaText, height, width);
		byte[] imgData = convertImageToByteArray(image, IMG_FORMAT);
		byte[] encryptedToken = cipherHandler.encryptString(captchaText.getBytes(), password, saltSource);
		String tokenString = Base64.getEncoder().encodeToString(encryptedToken);
		if(addSelfReference) {
			tokenString = addSelfReference(cipherHandler, tokenString, saltSource, password);
		}
		captcha = new TextCaptcha(imgData, tokenString);
		return captcha;
	}

	@Override
	public boolean validate(String answer, String token, CipherHandler cipherHandler, Serializable saltSource,
			String password) {
		byte[] decoded = Base64.getDecoder().decode(token);
		byte[] decryptedToken = cipherHandler.decryptString(decoded, password, saltSource);
		return answer.equals(new String(decryptedToken));

	}

}
