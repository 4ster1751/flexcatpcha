package io.github.yaforster.flexcaptcha.textbased.rendering.impl;

import org.junit.Test;

import io.github.yaforster.flexcaptcha.textbased.rendering.TextImageRendererTest;

public class TwirledTextImageRendererTest extends TextImageRendererTest<TwirledTextImageRenderer>{

	@Test
	public void testRender() {
		render();
	}

	@Override
	protected TwirledTextImageRenderer createRenderer() {
		return new TwirledTextImageRenderer();
	}

}
