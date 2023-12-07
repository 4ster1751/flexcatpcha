package io.github.yaforster.flexcaptcha.core;

import lombok.AllArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
public abstract class AbstractCaptchaGenerator {

    protected AbstractCaptchaCipher captchaCipher;
    protected AbstractCaptchaRenderer renderer;

    /**
     * @param solution   predefined string solution from which the image and the token are generated
     * @param saltSource Object used during creation of the captcha token to ensure authenticity
     * @return Captcha object containing the image data of the visual captcha and the token
     */
    public abstract Captcha generate(final String solution, final Serializable saltSource);

    /**
     * Validates the token from a previous captcha generation call against the given user input and a serializable
     * object used when the token was originally created.
     *
     * @param token      token generated from a previous call to the generate method.
     * @param userInput  answer given by the user read from the rendered image
     * @param saltSource Object used during creation of the captcha token to ensure authenticity
     * @return true if the validation was successful
     */
    public abstract boolean validate(final String token, final String userInput, final Serializable saltSource);
}
