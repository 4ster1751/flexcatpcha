package io.github.yaforster.flexcaptcha.imgbased;

import io.github.yaforster.flexcaptcha.Captcha;
import lombok.Getter;
import lombok.Setter;

/**
 * Object representing an image-based captcha output consisting of the image
 * data of the images in the grid to display and transport the actual captcha,
 * and a token representing the hashed and salted solution
 * 
 * @author Yannick Forster
 *
 */

@Getter
@Setter
public class ImageCaptcha extends Captcha {

	public ImageCaptcha(byte[][] imgData, String token) {
		super(token);
		this.imgData = imgData;
	}
	/**
	 * array of byte arrays of the images of the captcha
	 */
	byte[][] imgData;
}
