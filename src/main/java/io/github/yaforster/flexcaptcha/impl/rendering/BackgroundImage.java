package io.github.yaforster.flexcaptcha.impl.rendering;

import lombok.Getter;

import java.awt.*;
import java.awt.image.BufferedImage;

@Getter
public class BackgroundImage extends FlatColorBackground {

    private final BufferedImage backgroundImage;

    public BackgroundImage(Color backgroundImageColor, BufferedImage backgroundImage) {
        super(backgroundImageColor);
        this.backgroundImage = backgroundImage;
    }

    @Override
    public void drawBackground(final BufferedImage captchaImage) {
        super.drawBackground(captchaImage);
        Graphics2D captchaImageGraphic = captchaImage.createGraphics();
        captchaImageGraphic.drawImage(backgroundImage, null, 0, 0);
    }
}
