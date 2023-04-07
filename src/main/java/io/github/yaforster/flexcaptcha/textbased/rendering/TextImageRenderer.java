package io.github.yaforster.flexcaptcha.textbased.rendering;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Describes the logic required to render
 *
 * @author Yannick Forster
 */
public interface TextImageRenderer {

    /**
     * Creates a {@link BufferedImage} based on the input string, the pixel height
     * and pixel width
     *
     * @param captchaTextInput String the image will show
     * @param height           the height of the image to be generated
     * @param width            the width of the image to be generated
     * @return the generated image as {@link BufferedImage}
     */
    BufferedImage render(final String captchaTextInput, int height, int width);

    /**
     * Picks a random color from the array of possible text colors
     *
     * @param colors Color array from which to pick a random element.
     * @return Color object, picked randomly out of the textCols-Field array
     */
    default Color pickRandomColor(Color[] colors) {
        if (colors.length == 1) {
            return colors[0];
        }
        ThreadLocalRandom r = ThreadLocalRandom.current();
        int i = r.nextInt(colors.length);
        return colors[i];
    }
}
