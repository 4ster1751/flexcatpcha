package de.forster.flexcaptcha.core;

public class Test {
	
	public static void main(String[] args) {
		generateSimpleCaptcha();
		generateDistortedCaptcha();
	}

	private static void generateSimpleCaptcha() {
		CaptchaTextGenerator generator = new CaptchaTextGenerator();
		String s = generator.getCaptchaString(10, Case.UPPERCASE);
		System.out.println(s);
		CaptchaHandler handler = new SimpleCaptchaHandler();
		String saltSource = "Hello World!";
		Captcha captcha = handler.toCaptcha(s, saltSource, new SimpleCaptchaImageRenderer(), 100, 300);
		System.out.println(captcha.getImgDataAsBase64());
	}
	
	private static void generateDistortedCaptcha() {
		CaptchaTextGenerator generator = new CaptchaTextGenerator();
		String s = generator.getCaptchaString(10, Case.MIXEDCASE);
		System.out.println(s);
		CaptchaHandler handler = new SimpleCaptchaHandler();
		String saltSource = "Hello World!";
		Captcha captcha = handler.toCaptcha(s, saltSource, new TwirledCaptchaImageRenderer(), 100, 300);
		System.out.println(captcha.getImgDataAsBase64());
	}

}
