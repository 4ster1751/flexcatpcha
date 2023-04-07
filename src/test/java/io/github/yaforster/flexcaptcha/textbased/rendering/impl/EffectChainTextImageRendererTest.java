package io.github.yaforster.flexcaptcha.textbased.rendering.impl;

import io.github.yaforster.flexcaptcha.textbased.rendering.TextImageRendererTest;
import org.junit.Test;

//TODO: Implement testing effect chaining
public class EffectChainTextImageRendererTest extends TextImageRendererTest<EffectChainTextImageRenderer> {

    @Test
    public void testRender() {
        render();
    }

    @Override
    protected EffectChainTextImageRenderer createRenderer() {
        return new EffectChainTextImageRenderer();
    }

}
