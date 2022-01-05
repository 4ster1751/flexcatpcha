package io.github.yaforster.flexcaptcha.textbased.rendering.impl;


import org.junit.Test;

import io.github.yaforster.flexcaptcha.textbased.rendering.TextImageRendererTest;

public class CleanTextImageRendererTest extends TextImageRendererTest<CleanTextImageRenderer> {

	@Test
	public void testRender() {
		render();
	}

	protected CleanTextImageRenderer createRenderer() {
		return new CleanTextImageRenderer();
	}

}
