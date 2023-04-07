package io.github.yaforster.flexcaptcha.textbased.rendering.impl;


import io.github.yaforster.flexcaptcha.textbased.rendering.TextImageRendererTest;
import org.junit.Test;

public class CleanTextImageRendererTest extends TextImageRendererTest<CleanTextImageRenderer> {

    @Test
    public void testRender() {
        render();
    }

    protected CleanTextImageRenderer createRenderer() {
        return new CleanTextImageRenderer();
    }

}
