package io.github.yaforster.flexcaptcha.core;

import java.io.Serializable;

public class CaptchaGenerator extends AbstractCaptchaGenerator {

    public CaptchaGenerator(AbstractCaptchaCipher captchaCipher, AbstractCaptchaRenderer renderer) {
        super(captchaCipher, renderer);
    }

    @Override
    public final Captcha generate(final String solution, final Serializable saltSource) {
        String token = captchaCipher.generateToken(solution, saltSource);
        byte[] imgBytes = renderer.renderAndConvertToBytes(solution);
        return new Captcha(token, imgBytes);
    }

    @Override
    public final boolean validate(String token, String userInput, Serializable saltSource) {
        return captchaCipher.validateToken(token, saltSource, userInput);
    }
}
