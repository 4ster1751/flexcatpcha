package de.forster.flexcaptcha.core;

import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;

import org.junit.Test;

import de.forster.flexcaptcha.rendering.impl.SimpleCaptchaImageRenderer;

public class SimpleImageRendererTest {
	
	SimpleCaptchaImageRenderer renderer = new SimpleCaptchaImageRenderer();

	@Test
	public void testRender() {
		int targetHeight = 100;
		int widthHeight = 200;
		BufferedImage image = renderer.render("TEST", targetHeight, widthHeight);
		assertTrue(image.getHeight()==targetHeight);
		assertTrue(image.getWidth()==widthHeight);
	}

}
