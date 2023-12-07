package io.github.yaforster.flexcaptcha;

import io.github.yaforster.flexcaptcha.core.Captcha;
import io.github.yaforster.flexcaptcha.core.CaptchaGenerator;
import io.github.yaforster.flexcaptcha.impl.rendering.CaptchaRenderer;
import io.github.yaforster.flexcaptcha.impl.token.CaptchaCipher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CaptchaGeneratorTest {

    @Test
    void generate_with_default_should_work() {
        CaptchaCipher cipher = CaptchaCipher.builder().build();
        CaptchaRenderer renderer = CaptchaRenderer.getDefaultCaptchaRenderer();
        CaptchaGenerator generator = new CaptchaGenerator(cipher, renderer);
        assertDoesNotThrow(() -> generator.generate("abc123", "salt"));
    }

    @Test
    void test_validation_should_work() {
        CaptchaCipher cipher = CaptchaCipher.builder().build();
        CaptchaRenderer renderer = CaptchaRenderer.getDefaultCaptchaRenderer();
        CaptchaGenerator generator = new CaptchaGenerator(cipher, renderer);
        assertTrue(generator.validate("vnBn8x3bpm3wkvJYANdy9VNRijRowlFyq72US0ja4Jo=", "aBc123", "salt"));
    }

    @Test
    void test_can_validate_own_tokens() {
        CaptchaCipher cipher = CaptchaCipher.builder().build();
        CaptchaRenderer renderer = CaptchaRenderer.getDefaultCaptchaRenderer();
        CaptchaGenerator generator = new CaptchaGenerator(cipher, renderer);
        String solution = "aBc123";
        String someSalt = "salt";
        Captcha captcha = generator.generate(solution, someSalt);
        assertTrue(generator.validate(captcha.token(), solution, someSalt));
    }
}