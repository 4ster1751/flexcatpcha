package io.github.yaforster.flexcaptcha;

import io.github.yaforster.flexcaptcha.core.Captcha;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CaptchaTest {

    @Test
    void getImgDataAsBase64() {
        Captcha captcha = new Captcha("someToken", new byte[]{1, 2, 3, 4, 5});
        String imgBytesBase64 = captcha.getImgDataAsBase64();
        assertEquals("AQIDBAU=", imgBytesBase64);
    }
}