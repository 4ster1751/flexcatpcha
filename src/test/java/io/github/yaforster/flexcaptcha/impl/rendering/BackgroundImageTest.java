package io.github.yaforster.flexcaptcha.impl.rendering;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BackgroundImageTest {

    @Test
    void drawBackground_test_pixels_of_image() {
        Color testColor = Color.RED;
        BufferedImage fictionalImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        fictionalImage.setRGB(0, 0, testColor.getRGB());
        BackgroundImage background = new BackgroundImage(Color.BLUE, fictionalImage);
        BufferedImage testImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);

        background.drawBackground(testImage);

        int pixel = testImage.getRGB(0, 0);
        Color pixelColor = new Color(pixel, true);

        assertEquals(testColor, pixelColor);
    }
}