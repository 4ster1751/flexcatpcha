package de.forster.flexcaptcha.core;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.awt.Button;

import org.junit.Test;

import de.forster.flexcaptcha.Captcha;
import de.forster.flexcaptcha.handling.impl.SimpleCaptchaHandler;
import de.forster.flexcaptcha.rendering.impl.SimpleCaptchaImageRenderer;
import de.forster.flexcaptcha.textgen.impl.SimpleCaptchaTextGenerator;

public class SimpleCaptchaHandlerTest {
	
	private static final int TOKENLENGTH = 153;
	SimpleCaptchaHandler handler = new SimpleCaptchaHandler();
	SimpleCaptchaTextGenerator generator = new SimpleCaptchaTextGenerator();
	SimpleCaptchaImageRenderer renderer = new SimpleCaptchaImageRenderer();
	Button dummyObject = new Button();
	
	@Test
	public void testGenerategGeneric() {
		Captcha captcha = handler.generate(10, "ABC", generator,renderer, 60, 300);
		assertTrue(captcha.getToken().length()==TOKENLENGTH);
		assertTrue(captcha.getImgData()!=null);
	}
	
	@Test
	public void testGenerategGenericEmptySalt() {
		Captcha captcha = handler.generate(10, "", generator,renderer, 60, 300);
		assertTrue(captcha.getToken().length()==TOKENLENGTH);
		assertTrue(captcha.getImgData()!=null);
	}
	
	@Test
	public void testGenerategGenericNullSalt() {
		Captcha captcha = handler.generate(10, null, generator,renderer, 60, 300);
		assertTrue(captcha.getToken().length()==TOKENLENGTH);
		assertTrue(captcha.getImgData()!=null);
	}
	
	@Test
	public void testGenerategGenericAllPixelMinimum() {
		Captcha captcha = handler.generate(1, "", generator,renderer, 3, 1);
		assertTrue(captcha.getToken().length()==TOKENLENGTH);
		assertTrue(captcha.getImgData()!=null);
	}
	
	@Test
	public void testGenerategGenericLengthZero() {
		assertThrows(IllegalArgumentException.class, ()->{
			handler.generate(0, "", generator,renderer, 1, 1);
		});
	}
	
	@Test
	public void testGenerategGenericLengthNegative() {
		assertThrows(IllegalArgumentException.class, ()->{
			handler.generate(-1, "",generator, renderer, 1, 1);
		});
	}
	
	@Test
	public void testGenerategGenericIllegalHeight() {
		assertThrows(IllegalArgumentException.class, ()->{
			handler.generate(1, "", generator, renderer, 1, 1);
		});
	}
	
	@Test
	public void testGenerategGenericNegativeHeight() {
		assertThrows(IllegalArgumentException.class, ()->{
			handler.generate(1, "", generator, renderer, -3, 1);
		});
	}
	
	@Test
	public void testGenerategGenericIllegalWidth() {
		assertThrows(IllegalArgumentException.class, ()->{
			handler.generate(1, "", generator, renderer, 3, 0);
		});
	}
	
	@Test
	public void testGenerategGenericNegativeWidth() {
		assertThrows(IllegalArgumentException.class, ()->{
			handler.generate(1, "", generator, renderer, 3, -1);
		});
	}
	
	@Test
	public void testGenerategGenericNull() {
		assertThrows(IllegalArgumentException.class, ()->{
			handler.generate(10, null, null, null, 60, 300);
		});
	}
	
	@Test
	public void testGenerategGenericShort() {
		Captcha captcha = handler.generate(5, "ABC", generator, renderer, 60, 300);
		assertTrue(captcha.getToken().length()==TOKENLENGTH);
		assertTrue(captcha.getImgData()!=null);
	}
	
	@Test
	public void testGenerategGenericWithDummyObj() {
		Captcha captcha = handler.generate(5, dummyObject, generator, renderer, 60, 300);
		assertTrue(captcha.getToken().length()==TOKENLENGTH);
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
		assertTrue(captcha.getToken().length()==TOKENLENGTH);
		assertTrue(captcha.getImgData()!=null);
		assertTrue(handler.validate("TESTSTRING", captcha.getToken(), dummyObject));
	}

}
