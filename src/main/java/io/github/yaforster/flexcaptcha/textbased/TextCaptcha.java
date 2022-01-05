package io.github.yaforster.flexcaptcha.textbased;

import java.util.Base64;

import io.github.yaforster.flexcaptcha.Captcha;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TextCaptcha extends Captcha {

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
