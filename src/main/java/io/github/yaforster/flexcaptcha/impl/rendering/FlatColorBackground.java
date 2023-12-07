package io.github.yaforster.flexcaptcha.impl.rendering;

import io.github.yaforster.flexcaptcha.core.AbstractCaptchaImageBackground;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;
import java.awt.image.BufferedImage;

@Getter
@AllArgsConstructor
public class FlatColorBackground extends AbstractCaptchaImageBackground {

    /**
     * Color of the Captcha Background
     */
    protected final Color backgroundColor;

    @Override
    public void drawBackground(final BufferedImage captchaImage) {
        Graphics captchaImageGraphic = captchaImage.getGraphics();
        captchaImageGraphic.setColor(backgroundColor);
        captchaImageGraphic.fillRect(0, 0, captchaImage.getWidth(), captchaImage.getHeight());
    }
}
