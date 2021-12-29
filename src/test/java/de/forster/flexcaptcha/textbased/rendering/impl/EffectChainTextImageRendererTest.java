package de.forster.flexcaptcha.textbased.rendering.impl;

import org.junit.Test;

import de.forster.flexcaptcha.textbased.rendering.TextImageRendererTest;

//TODO: Implement testing effect chaining
public class EffectChainTextImageRendererTest extends TextImageRendererTest<EffectChainTextImageRenderer>{

	@Test
	public void testRender() {
		render();
	}

	@Override
	protected EffectChainTextImageRenderer createRenderer() {
		return new EffectChainTextImageRenderer();
	}

}
