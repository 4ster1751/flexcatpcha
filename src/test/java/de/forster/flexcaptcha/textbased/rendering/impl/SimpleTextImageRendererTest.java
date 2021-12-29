package de.forster.flexcaptcha.textbased.rendering.impl;

import org.junit.Test;

import de.forster.flexcaptcha.textbased.rendering.TextImageRendererTest;

//TODO: Test colors
//TODO: Test non existant fonts handling
public class SimpleTextImageRendererTest extends TextImageRendererTest<SimpleTextImageRenderer>{

	@Test
	public void testRender() {
		render();
	}

	@Override
	protected SimpleTextImageRenderer createRenderer() {
		return new SimpleTextImageRenderer();
	}

}
