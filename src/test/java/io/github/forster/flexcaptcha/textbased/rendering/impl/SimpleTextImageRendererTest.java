package io.github.forster.flexcaptcha.textbased.rendering.impl;

import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.junit.Test;

import io.github.forster.flexcaptcha.textbased.rendering.TextImageRendererTest;

public class SimpleTextImageRendererTest extends TextImageRendererTest<SimpleTextImageRenderer>{

	@Test
	public void testRender() {
		render();
	}
	
	@Test
	public void renderWithNonExistantFont_ShouldWork() {
		getRenderer().setFontName("DoesntExist");
		render();
	}
	
	@Test
	public void renderColorTest_ShouldWork() {
		getRenderer().setDistortCol(Color.red).setBackgrndCol(Color.red).setTextCols(new Color[] {Color.red});
		BufferedImage img = getRenderer().render("Test", 50, 50);
		int pixelColInt = (img.getRGB(25, 25) & 0x00ff0000) >> 16;;
		assertTrue(pixelColInt == Color.red.getRed());
	}

	@Override
	protected SimpleTextImageRenderer createRenderer() {
		return new SimpleTextImageRenderer();
	}

}
