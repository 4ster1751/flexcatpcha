package io.github.yaforster.flexcaptcha.textbased.handling.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.awt.Button;

import org.junit.Test;

import io.github.yaforster.flexcaptcha.CipherHandler;
import io.github.yaforster.flexcaptcha.textbased.TextCaptcha;
import io.github.yaforster.flexcaptcha.textbased.rendering.impl.SimpleTextImageRenderer;
import io.github.yaforster.flexcaptcha.textbased.textgen.impl.SimpleCaptchaTextGenerator;

public class SecureTextCaptchaHandlerTest {

	SecureTextCaptchaHandler handler = new SecureTextCaptchaHandler();
	SimpleCaptchaTextGenerator generator = new SimpleCaptchaTextGenerator();
	SimpleTextImageRenderer renderer = new SimpleTextImageRenderer();
	CipherHandler cipherHandler =  new CipherHandler();
	Button dummySerializable = new Button();
	String password = "ThisIsMyPassword!";

	@Test
	public void testGenerateGeneric() {
		TextCaptcha captcha = handler.generate(10, cipherHandler, "ABC", password, generator, renderer, 60, 300);
		assertTrue(captcha.getToken().length()>0);
		assertTrue(captcha.getImgData() != null);
	}

	@Test
	public void testGenerateGenericEmptySalt() {
		TextCaptcha captcha = handler.generate(10, cipherHandler, "", password, generator, renderer, 60, 300);
		assertTrue(captcha.getToken().length()>0);
		assertTrue(captcha.getImgData() != null);
	}

	@Test
	public void testGenerateGenericNullSalt() {
		TextCaptcha captcha = handler.generate(10, cipherHandler, null, password, generator, renderer, 60, 300);
		assertTrue(captcha.getToken().length()>0);
		assertTrue(captcha.getImgData() != null);
	}

	@Test
	public void testGenerateGenericAllPixelMinimum() {
		TextCaptcha captcha = handler.generate(1, cipherHandler, "", password, generator, renderer, 3, 1);
		assertTrue(captcha.getToken().length()>0);
		assertTrue(captcha.getImgData() != null);
	}

	@Test
	public void testGenerateGenericLengthZero() {
		assertThrows(IllegalArgumentException.class, () -> {
			handler.generate(0, cipherHandler,"", password, generator, renderer, 1, 1);
		});
	}

	@Test
	public void testGenerateGenericLengthNegative() {
		assertThrows(IllegalArgumentException.class, () -> {
			handler.generate(-1, cipherHandler,"", password, generator, renderer, 1, 1);
		});
	}

	@Test
	public void testGenerateGenericIllegalHeight() {
		assertThrows(IllegalArgumentException.class, () -> {
			handler.generate(1, cipherHandler,"", password, generator, renderer, 1, 1);
		});
	}

	@Test
	public void testGenerateGenericNegativeHeight() {
		assertThrows(IllegalArgumentException.class, () -> {
			handler.generate(1, cipherHandler,"", password, generator, renderer, -3, 1);
		});
	}

	@Test
	public void testGenerateGenericIllegalWidth() {
		assertThrows(IllegalArgumentException.class, () -> {
			handler.generate(1, cipherHandler,"", password, generator, renderer, 3, 0);
		});
	}

	@Test
	public void testGenerateGenericNegativeWidth() {
		assertThrows(IllegalArgumentException.class, () -> {
			handler.generate(1, cipherHandler,"", password, generator, renderer, 3, -1);
		});
	}

	@Test
	public void testGenerateGenericNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			handler.generate(10, cipherHandler,null, password, null, null, 60, 300);
		});
	}

	@Test
	public void testGenerateGenericShort() {
		TextCaptcha captcha = handler.generate(5, cipherHandler,"ABC", password, generator, renderer, 60, 300);
		assertTrue(captcha.getToken().length()>0);
		assertTrue(captcha.getImgData() != null);
	}

	@Test
	public void testGenerateGenericWithDummyObj() {
		TextCaptcha captcha = handler.generate(5, cipherHandler,dummySerializable, password, generator, renderer, 60, 300);
		assertTrue(captcha.getToken().length()>0);
		assertTrue(captcha.getImgData() != null);
	}

	@Test
	public void testValidateEmptyString() {
		assertThrows(IllegalArgumentException.class, () -> {
			handler.toCaptcha("", cipherHandler,dummySerializable, password, new SimpleTextImageRenderer(), 60, 300);
		});
	}

}
