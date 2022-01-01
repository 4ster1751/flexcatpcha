package io.github.forster.flexcaptcha.textbased.rendering.impl;


import org.junit.Test;

import io.github.forster.flexcaptcha.textbased.rendering.TextImageRendererTest;

public class CleanTextImageRendererTest extends TextImageRendererTest<CleanTextImageRenderer> {

	@Test
	public void testRender() {
		render();
	}

	protected CleanTextImageRenderer createRenderer() {
		return new CleanTextImageRenderer();
	}

}
