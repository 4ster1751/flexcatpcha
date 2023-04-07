package io.github.yaforster.flexcaptcha.textbased.rendering.impl;

import io.github.yaforster.flexcaptcha.textbased.rendering.TextImageRendererTest;
import org.junit.Test;

public class TwirledTextImageRendererTest extends TextImageRendererTest<TwirledTextImageRenderer> {

    @Test
    public void testRender() {
        render();
    }

    @Override
    protected TwirledTextImageRenderer createRenderer() {
        return new TwirledTextImageRenderer();
    }

}
