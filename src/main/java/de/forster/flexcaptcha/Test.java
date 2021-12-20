package de.forster.flexcaptcha;

import de.forster.flexcaptcha.enums.Case;
import de.forster.flexcaptcha.rendering.impl.SimpleCaptchaImageRenderer;
import de.forster.flexcaptcha.rendering.impl.TwirledCaptchaImageRenderer;
import de.forster.flexcaptcha.textgen.impl.SimpleCaptchaTextGenerator;

public class Test {
	
	public static void main(String[] args) {
		generateSimpleCaptcha();
		generateDistortedCaptcha();
	}

	private static void generateSimpleCaptcha() {
		SimpleCaptchaTextGenerator generator = new SimpleCaptchaTextGenerator();
		String s = generator.generate(10, Case.UPPERCASE);
		System.out.println(s);
		CaptchaHandler handler = new SimpleCaptchaHandler();
		String saltSource = "Hello World!";
		Captcha captcha = handler.toCaptcha(s, saltSource, new SimpleCaptchaImageRenderer(), 100, 300);
		System.out.println(captcha.getImgDataAsBase64());
	}
	
	private static void generateDistortedCaptcha() {
		SimpleCaptchaTextGenerator generator = new SimpleCaptchaTextGenerator();
		String s = generator.generate(10, Case.MIXEDCASE);
		System.out.println(s);
		CaptchaHandler handler = new SimpleCaptchaHandler();
		String saltSource = "Hello World!";
		Captcha captcha = handler.toCaptcha(s, saltSource, new TwirledCaptchaImageRenderer(), 100, 300);
		System.out.println(captcha.getImgDataAsBase64());
	}

}
