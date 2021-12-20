package de.forster.flexcaptcha;

import java.util.Base64;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Object representing a captcha output consisting of the image data to display
 * and transport the actual captcha, and a token representing the hashed and
 * salted solution
 * 
 * @author Yannick Forster
 *
 */

@Getter
@Setter
@AllArgsConstructor
public class Captcha {

	/**
	 * String representation of the image containing the visual captcha
	 */
	private byte[] imgData;
	/**
	 * String containing the hash of the original solution salted with a specified
	 * object
	 */
	private String token = "";

	/**
	 * Returns the image data byte array as base64 string.
	 * 
	 * @return
	 */
	public String getImgDataAsBase64() {
		if (imgData == null) {
			throw new IllegalStateException("Cannot convert empty image data to string.");
		}
		return Base64.getEncoder().encodeToString(imgData);
	}
}
