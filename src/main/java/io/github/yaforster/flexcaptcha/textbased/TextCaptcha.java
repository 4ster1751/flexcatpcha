package io.github.yaforster.flexcaptcha.textbased;

import java.util.Base64;

import io.github.yaforster.flexcaptcha.Captcha;
import lombok.Getter;
import lombok.Setter;

/**
 * Object representing a text-based captcha output consisting of the image data
 * of the visualized text to display and transport the actual captcha, and a
 * token representing the solution
 * 
 * @author Yannick Forster
 */

@Getter
@Setter
public class TextCaptcha extends Captcha {

	/**
	 * Creates a new TextCaptcha object and adds the byte array containing the image
	 * data and the token
	 * 
	 * @param imgData byte array for the image data
	 * @param token   the token
	 */
	public TextCaptcha(byte[] imgData, String token) {
		super(token);
		this.imgData = imgData;
	}

	/**
	 * String representation of the image containing the visual captcha
	 */
	private byte[] imgData;

	/**
	 * Returns the image data byte array as base64 string.
	 * 
	 * @return String of the base64-encoded imgData byte-array
	 */
	public String getImgDataAsBase64() {
		if (imgData == null) {
			throw new IllegalStateException("Cannot convert empty image data to string.");
		}
		return Base64.getEncoder().encodeToString(imgData);
	}

}
