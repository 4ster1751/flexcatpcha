package io.github.yaforster.flexcaptcha;

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
public abstract class Captcha {
	/**
	 * String containing the hash of the original solution salted with a specified
	 * object
	 */
	private String token;
}
