package de.forster.flexcaptcha.textbased.handling.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.awt.Button;

import javax.crypto.spec.IvParameterSpec;

import org.junit.Test;
import org.mockito.Mockito;

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

public class SimpleTextCaptchaHandlerTest {

	SimpleTextCaptchaHandler handler = new SimpleTextCaptchaHandler();
	SimpleCaptchaTextGenerator generator = new SimpleCaptchaTextGenerator();
	SimpleTextImageRenderer renderer = new SimpleTextImageRenderer();
	CipherHandler cipherHandler =  getCHMock();
	Button dummySerializable = new Button();
	String password = "ThisIsMyPassword!";

	@Test
	public void testGenerategGeneric() {
		TextCaptcha captcha = handler.generate(10, cipherHandler, "ABC", password, generator, renderer, 60, 300);
		assertTrue(captcha.getToken().length()>0);
		assertTrue(captcha.getImgData() != null);
	}

	@Test
	public void testGenerategGenericEmptySalt() {
		TextCaptcha captcha = handler.generate(10, cipherHandler, "", password, generator, renderer, 60, 300);
		assertTrue(captcha.getToken().length()>0);
		assertTrue(captcha.getImgData() != null);
	}

	@Test
	public void testGenerategGenericNullSalt() {
		TextCaptcha captcha = handler.generate(10, cipherHandler, null, password, generator, renderer, 60, 300);
		assertTrue(captcha.getToken().length()>0);
		assertTrue(captcha.getImgData() != null);
	}

	@Test
	public void testGenerategGenericAllPixelMinimum() {
		TextCaptcha captcha = handler.generate(1, cipherHandler, "", password, generator, renderer, 3, 1);
		assertTrue(captcha.getToken().length()>0);
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
		assertTrue(captcha.getToken().length()>0);
		assertTrue(captcha.getImgData() != null);
	}

	@Test
	public void testGenerategGenericWithDummyObj() {
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

	@Test
	public void testToCaptchaAndValidate() {
		TextCaptcha captcha = handler.toCaptcha("TESTSTRING", cipherHandler,dummySerializable, password, new SimpleTextImageRenderer(), 60, 300);
		assertTrue(captcha.getToken().length()>0);
		assertTrue(captcha.getImgData() != null);
		assertTrue(handler.validate("TESTSTRING", captcha.getToken(), dummySerializable));
	}
	
	private CipherHandler getCHMock() {
		CipherHandler cipherHandler = Mockito.mock(CipherHandler.class);
		Mockito.when(cipherHandler.generateIV())
				.thenReturn(new IvParameterSpec(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 }));
		Mockito.when(cipherHandler.decryptString(any(byte[].class), anyString(), any()))
				.thenReturn(new byte[] { 1, 2, 3 });
		Mockito.when(cipherHandler.encryptString(any(byte[].class), anyString(), any(), any(byte[].class)))
				.thenReturn(new byte[] { 1, 2, 3 });
		return cipherHandler;
	}

}
