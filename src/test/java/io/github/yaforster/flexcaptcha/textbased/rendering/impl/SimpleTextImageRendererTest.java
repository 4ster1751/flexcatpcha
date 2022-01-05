package io.github.yaforster.flexcaptcha.textbased.rendering.impl;

import org.junit.Test;

import io.github.yaforster.flexcaptcha.textbased.rendering.TextImageRendererTest;

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

	@Override
	protected SimpleTextImageRenderer createRenderer() {
		return new SimpleTextImageRenderer();
	}

}
