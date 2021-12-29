package de.forster.flexcaptcha.textbased.rendering.impl;


import org.junit.Test;

import de.forster.flexcaptcha.textbased.rendering.TextImageRendererTest;

public class CleanTextImageRendererTest extends TextImageRendererTest<CleanTextImageRenderer> {

	@Test
	public void testRender() {
		render();
	}

	protected CleanTextImageRenderer createRenderer() {
		return new CleanTextImageRenderer();
	}

}
