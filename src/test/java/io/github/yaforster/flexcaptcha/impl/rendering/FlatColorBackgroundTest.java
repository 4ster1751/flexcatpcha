package io.github.yaforster.flexcaptcha.impl.rendering;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FlatColorBackgroundTest {

    @Test
    void drawBackground_test_pixels_of_background_color() {
        Color testColor = Color.BLUE;
        FlatColorBackground background = new FlatColorBackground(testColor);
        BufferedImage testImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);

        background.drawBackground(testImage);

        int pixel = testImage.getRGB(0, 0);
        Color pixelColor = new Color(pixel, true);

        assertEquals(testColor, pixelColor);
    }
}