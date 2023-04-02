package io.github.yaforster.flexcaptcha.textbased.handling.impl;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.awt.Button;

import javax.crypto.spec.IvParameterSpec;

import org.junit.Test;
import org.mockito.Mockito;

import io.github.yaforster.flexcaptcha.Captcha;
import io.github.yaforster.flexcaptcha.CipherHandler;
import io.github.yaforster.flexcaptcha.textbased.TextCaptcha;
import io.github.yaforster.flexcaptcha.textbased.rendering.impl.SimpleTextImageRenderer;
import io.github.yaforster.flexcaptcha.textbased.textgen.impl.SimpleCaptchaTextGenerator;

/**
 * Tests {@link SimpleTextCaptchaHandler}
 * 
 * @author Yannick Forster
 *
 */

public class SimpleTextCaptchaHandlerTest {

	final SimpleTextCaptchaHandler handler = new SimpleTextCaptchaHandler();
	final SimpleCaptchaTextGenerator generator = new SimpleCaptchaTextGenerator();
	final SimpleTextImageRenderer renderer = new SimpleTextImageRenderer();
	final CipherHandler cipherHandler =  getCHMock();
	final Button dummySerializable = new Button();
	final String password = "ThisIsMyPassword!";

	@Test
	public void testGenerateGeneric() {
		TextCaptcha captcha = handler.generate(10, cipherHandler, "ABC", password, generator, renderer, 60, 300, true);
		assertTrue(captcha.getToken().length()>0);
        assertNotNull(captcha.getImgData());
	}

	@Test
	public void testGenerateGenericEmptySalt() {
		TextCaptcha captcha = handler.generate(10, cipherHandler, "", password, generator, renderer, 60, 300, true);
		assertTrue(captcha.getToken().length()>0);
        assertNotNull(captcha.getImgData());
	}

	@Test
	public void testGenerateGenericNullSalt() {
		TextCaptcha captcha = handler.generate(10, cipherHandler, null, password, generator, renderer, 60, 300, true);
		assertTrue(captcha.getToken().length()>0);
        assertNotNull(captcha.getImgData());
	}

	@Test
	public void testGenerateGenericAllPixelMinimum() {
		TextCaptcha captcha = handler.generate(1, cipherHandler, "", password, generator, renderer, 3, 1, true);
		assertTrue(captcha.getToken().length()>0);
        assertNotNull(captcha.getImgData());
	}

	@Test
	public void testGenerateGenericLengthZero() {
		assertThrows(IllegalArgumentException.class, () -> handler.generate(0, cipherHandler,"", password, generator, renderer, 1, 1, true));
	}

	@Test
	public void testGenerateGenericLengthNegative() {
		assertThrows(IllegalArgumentException.class, () -> handler.generate(-1, cipherHandler,"", password, generator, renderer, 1, 1, true));
	}

	@Test
	public void testGenerateGenericIllegalHeight() {
		assertThrows(IllegalArgumentException.class, () -> handler.generate(1, cipherHandler,"", password, generator, renderer, 1, 1, true));
	}

	@Test
	public void testGenerateGenericNegativeHeight() {
		assertThrows(IllegalArgumentException.class, () -> handler.generate(1, cipherHandler,"", password, generator, renderer, -3, 1, true));
	}

	@Test
	public void testGenerateGenericIllegalWidth() {
		assertThrows(IllegalArgumentException.class, () -> handler.generate(1, cipherHandler,"", password, generator, renderer, 3, 0, true));
	}

	@Test
	public void testGenerateGenericNegativeWidth() {
		assertThrows(IllegalArgumentException.class, () -> handler.generate(1, cipherHandler,"", password, generator, renderer, 3, -1, true));
	}

	@Test
	public void testGenerateGenericNull() {
		assertThrows(IllegalArgumentException.class, () -> handler.generate(10, cipherHandler,null, password, null, null, 60, 300, true));
	}

	@Test
	public void testGenerateGenericShort() {
		TextCaptcha captcha = handler.generate(5, cipherHandler,"ABC", password, generator, renderer, 60, 300, true);
		assertTrue(captcha.getToken().length()>0);
        assertNotNull(captcha.getImgData());
	}

	@Test
	public void testGenerateGenericWithDummyObj() {
		TextCaptcha captcha = handler.generate(5, cipherHandler,dummySerializable, password, generator, renderer, 60, 300, true);
		assertTrue(captcha.getToken().length()>0);
        assertNotNull(captcha.getImgData());
	}

	@Test
	public void testValidateEmptyString() {
		assertThrows(IllegalArgumentException.class, () -> handler.toCaptcha("", cipherHandler,dummySerializable, password, new SimpleTextImageRenderer(), 60, 300, true));
	}

	@Test
	public void testToCaptchaAndValidate() {
		TextCaptcha captcha = handler.toCaptcha("TESTSTRING", cipherHandler,dummySerializable, password, new SimpleTextImageRenderer(), 60, 300, true);
		assertTrue(captcha.getToken().length()>0);
        assertNotNull(captcha.getImgData());
		assertTrue(handler.validate("TESTSTRING", captcha.getToken(), cipherHandler, dummySerializable, password));
	}
	
	@Test
	public void testValidation() {
		String myText = "myText";
		Captcha captcha = handler.toCaptcha(myText, cipherHandler, dummySerializable, password, renderer, 100, 50, false);
		assertTrue(handler.validate(myText, captcha.getToken(), cipherHandler, dummySerializable, password));
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
