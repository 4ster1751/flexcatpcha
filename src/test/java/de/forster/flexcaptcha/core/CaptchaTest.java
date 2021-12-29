package de.forster.flexcaptcha.core;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.forster.flexcaptcha.textbased.TextCaptcha;

public class CaptchaTest {

	@Test
	public void testGetImgDataAsBase64() {
		TextCaptcha captcha = new TextCaptcha(new byte[] { -119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72 }, "ABC");
		assertTrue(captcha.getImgDataAsBase64().equals("iVBORw0KGgoAAAANSUg="));
	}

}
