package io.github.yaforster.flexcaptcha.core;

import io.github.yaforster.flexcaptcha.Captcha;
import io.github.yaforster.flexcaptcha.textbased.TextCaptcha;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests {@link Captcha}
 *
 * @author Yannick Forster
 */
public class CaptchaTest {

    @Test
    public void testGetImgDataAsBase64() {
        TextCaptcha captcha = new TextCaptcha(new byte[]{-119, 80, 78, 71, 13, 10, 26, 10, 0, 0, 0, 13, 73, 72}, "ABC");
        assertEquals("iVBORw0KGgoAAAANSUg=", captcha.getImgDataAsBase64());
    }

}
