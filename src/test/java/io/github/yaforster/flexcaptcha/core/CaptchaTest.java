package io.github.yaforster.flexcaptcha.core;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import io.github.yaforster.flexcaptcha.Captcha;
import io.github.yaforster.flexcaptcha.textbased.TextCaptcha;

/**
 * Tests {@link Captcha}
 * 
 * @author Yannick Forster
 *
 */
public class CaptchaTest {

	@Test
	public void testGetImgDataAsBase64() {
		TextCaptcha captcha = new TextCaptcha(new byte[] { -119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72 }, "ABC");
		assertTrue(captcha.getImgDataAsBase64().equals("iVBORw0KGgoAAAANSUg="));
	}

}
