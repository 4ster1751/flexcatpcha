package de.forster.flexcaptcha.textbased.handling.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.awt.Button;

import org.junit.Test;

import de.forster.flexcaptcha.CipherHandler;
import de.forster.flexcaptcha.textbased.TextCaptcha;
import de.forster.flexcaptcha.textbased.rendering.impl.SimpleTextImageRenderer;
import de.forster.flexcaptcha.textbased.textgen.impl.SimpleCaptchaTextGenerator;

/**
 * Tests {@link SimpleTextCaptchaHandler}
 * 
 * @author Yannick Forster
 *
 */

//TODO: Mock CipherHandler
public class SimpleTextCaptchaHandlerTest {

	private static final int TOKENLENGTH = 173;
	SimpleTextCaptchaHandler handler = new SimpleTextCaptchaHandler();
	SimpleCaptchaTextGenerator generator = new SimpleCaptchaTextGenerator();
	SimpleTextImageRenderer renderer = new SimpleTextImageRenderer();
	CipherHandler cipherHandler =  new CipherHandler();
	Button dummySerializable = new Button();
	String password = "ThisIsMyPassword!";

	@Test
	public void testGenerategGeneric() {
		TextCaptcha captcha = handler.generate(10, cipherHandler, "ABC", password, generator, renderer, 60, 300);
		assertTrue(captcha.getToken().length() == TOKENLENGTH);
		assertTrue(captcha.getImgData() != null);
	}

	@Test
	public void testGenerategGenericEmptySalt() {
		TextCaptcha captcha = handler.generate(10, cipherHandler, "", password, generator, renderer, 60, 300);
		assertTrue(captcha.getToken().length() == TOKENLENGTH);
		assertTrue(captcha.getImgData() != null);
	}

	@Test
	public void testGenerategGenericNullSalt() {
		TextCaptcha captcha = handler.generate(10, cipherHandler, null, password, generator, renderer, 60, 300);
		assertTrue(captcha.getToken().length() == TOKENLENGTH);
		assertTrue(captcha.getImgData() != null);
	}

	@Test
	public void testGenerategGenericAllPixelMinimum() {
		TextCaptcha captcha = handler.generate(1, cipherHandler, "", password, generator, renderer, 3, 1);
		assertTrue(captcha.getToken().length() == TOKENLENGTH);
		assertTrue(captcha.getImgData() != null);
	}

	@Test
	public void testGenerategGenericLengthZero() {
		assertThrows(IllegalArgumentException.class, () -> {
			handler.generate(0, cipherHandler,"", password, generator, renderer, 1, 1);
		});
	}

	@Test
	public void testGenerategGenericLengthNegative() {
		assertThrows(IllegalArgumentException.class, () -> {
			handler.generate(-1, cipherHandler,"", password, generator, renderer, 1, 1);
		});
	}

	@Test
	public void testGenerategGenericIllegalHeight() {
		assertThrows(IllegalArgumentException.class, () -> {
			handler.generate(1, cipherHandler,"", password, generator, renderer, 1, 1);
		});
	}

	@Test
	public void testGenerategGenericNegativeHeight() {
		assertThrows(IllegalArgumentException.class, () -> {
			handler.generate(1, cipherHandler,"", password, generator, renderer, -3, 1);
		});
	}

	@Test
	public void testGenerategGenericIllegalWidth() {
		assertThrows(IllegalArgumentException.class, () -> {
			handler.generate(1, cipherHandler,"", password, generator, renderer, 3, 0);
		});
	}

	@Test
	public void testGenerategGenericNegativeWidth() {
		assertThrows(IllegalArgumentException.class, () -> {
			handler.generate(1, cipherHandler,"", password, generator, renderer, 3, -1);
		});
	}

	@Test
	public void testGenerategGenericNull() {
		assertThrows(IllegalArgumentException.class, () -> {
			handler.generate(10, cipherHandler,null, password, null, null, 60, 300);
		});
	}

	@Test
	public void testGenerategGenericShort() {
		TextCaptcha captcha = handler.generate(5, cipherHandler,"ABC", password, generator, renderer, 60, 300);
		assertTrue(captcha.getToken().length() == TOKENLENGTH);
		assertTrue(captcha.getImgData() != null);
	}

	@Test
	public void testGenerategGenericWithDummyObj() {
		TextCaptcha captcha = handler.generate(5, cipherHandler,dummySerializable, password, generator, renderer, 60, 300);
		assertTrue(captcha.getToken().length() == TOKENLENGTH);
		assertTrue(captcha.getImgData() != null);
	}

	@Test
	public void testValidateEmptyString() {
		assertThrows(IllegalArgumentException.class, () -> {
			handler.toCaptcha("", cipherHandler,dummySerializable, password, new SimpleTextImageRenderer(), 60, 300);
		});
	}

	@Test
	public void testToCaptchaAndValidate() {
		TextCaptcha captcha = handler.toCaptcha("TESTSTRING", cipherHandler,dummySerializable, password, new SimpleTextImageRenderer(), 60, 300);
		assertTrue(captcha.getToken().length() == TOKENLENGTH);
		assertTrue(captcha.getImgData() != null);
		assertTrue(handler.validate(cipherHandler, "TESTSTRING", captcha.getToken(), dummySerializable));
	}

}
