package de.forster.flexcaptcha.core;

import static org.junit.Assert.*;

import java.awt.Button;

import org.junit.Test;

public class SimpleCaptchaHandlerTest {
	
	SimpleCaptchaHandler handler = new SimpleCaptchaHandler();
	Button dummyObject = new Button();
	
	@Test
	public void testGenerategGeneric() {
		Captcha captcha = handler.generate(10, "ABC", new SimpleCaptchaImageRenderer(), 60, 300);
		assertTrue(captcha.getToken().length()==24);
		assertTrue(captcha.getToken().endsWith("=="));
		assertTrue(captcha.getImgData()!=null);
	}
	
	@Test
	public void testGenerategGenericEmptySalt() {
		Captcha captcha = handler.generate(10, "", new SimpleCaptchaImageRenderer(), 60, 300);
		assertTrue(captcha.getToken().length()==24);
		assertTrue(captcha.getToken().endsWith("=="));
		assertTrue(captcha.getImgData()!=null);
	}
	
	@Test
	public void testGenerategGenericNullSalt() {
		Captcha captcha = handler.generate(10, null, new SimpleCaptchaImageRenderer(), 60, 300);
		assertTrue(captcha.getToken().length()==24);
		assertTrue(captcha.getToken().endsWith("=="));
		assertTrue(captcha.getImgData()!=null);
	}
	
	@Test
	public void testGenerategGenericAllPixelMinimum() {
		Captcha captcha = handler.generate(1, "", new SimpleCaptchaImageRenderer(), 3, 1);
		assertTrue(captcha.getToken().length()==24);
		assertTrue(captcha.getToken().endsWith("=="));
		assertTrue(captcha.getImgData()!=null);
	}
	
	@Test
	public void testGenerategGenericLengthZero() {
		assertThrows(IllegalArgumentException.class, ()->{
			handler.generate(0, "", new SimpleCaptchaImageRenderer(), 1, 1);
		});
	}
	
	@Test
	public void testGenerategGenericLengthNegative() {
		assertThrows(IllegalArgumentException.class, ()->{
			handler.generate(-1, "", new SimpleCaptchaImageRenderer(), 1, 1);
		});
	}
	
	@Test
	public void testGenerategGenericIllegalHeight() {
		assertThrows(IllegalArgumentException.class, ()->{
			handler.generate(1, "", new SimpleCaptchaImageRenderer(), 1, 1);
		});
	}
	
	@Test
	public void testGenerategGenericNegativeHeight() {
		assertThrows(IllegalArgumentException.class, ()->{
			handler.generate(1, "", new SimpleCaptchaImageRenderer(), -3, 1);
		});
	}
	
	@Test
	public void testGenerategGenericIllegalWidth() {
		assertThrows(IllegalArgumentException.class, ()->{
			handler.generate(1, "", new SimpleCaptchaImageRenderer(), 3, 0);
		});
	}
	
	@Test
	public void testGenerategGenericNegativeWidth() {
		assertThrows(IllegalArgumentException.class, ()->{
			handler.generate(1, "", new SimpleCaptchaImageRenderer(), 3, -1);
		});
	}
	
	@Test
	public void testGenerategGenericNullRenderer() {
		assertThrows(IllegalArgumentException.class, ()->{
			handler.generate(10, null, null, 60, 300);
		});
	}
	
	@Test
	public void testGenerategGenericShort() {
		Captcha captcha = handler.generate(5, "ABC", new SimpleCaptchaImageRenderer(), 60, 300);
		assertTrue(captcha.getToken().length()==24);
		assertTrue(captcha.getToken().endsWith("=="));
		assertTrue(captcha.getImgData()!=null);
	}
	
	@Test
	public void testGenerategGenericWithDummyObj() {
		Captcha captcha = handler.generate(5, dummyObject, new SimpleCaptchaImageRenderer(), 60, 300);
		assertTrue(captcha.getToken().length()==24);
		assertTrue(captcha.getToken().endsWith("=="));
		assertTrue(captcha.getImgData()!=null);
	}
	
	@Test
	public void testValidateEmptyString() {
		assertThrows(IllegalArgumentException.class, ()->{
			handler.toCaptcha("", dummyObject, new SimpleCaptchaImageRenderer(), 60, 300);
		});
	}

	@Test
	public void testToCaptchaAndValidate() {
		Captcha captcha = handler.toCaptcha("TESTSTRING", dummyObject, new SimpleCaptchaImageRenderer(), 60, 300);
		assertTrue(captcha.getToken().length()==24);
		assertTrue(captcha.getToken().endsWith("=="));
		assertTrue(captcha.getImgData()!=null);
		assertTrue(handler.validate("TESTSTRING", captcha.getToken(), dummyObject));
	}

}
