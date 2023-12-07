package io.github.yaforster.flexcaptcha.core;

import java.awt.image.BufferedImage;

public abstract class AbstractCaptchaImageBackground {

    /**
     * Draws the background into the given {@link BufferedImage} of the captcha image.
     *
     * @param captchaImage {@link BufferedImage} to draw the background of.
     */
    public abstract void drawBackground(final BufferedImage captchaImage);

}
