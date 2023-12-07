package io.github.yaforster.flexcaptcha.core;

import lombok.AllArgsConstructor;

import java.awt.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@AllArgsConstructor
public abstract class AbstractCaptchaRenderer {

    protected int pictureHeight;
    protected int pictureWidth;
    protected List<Color> availableTextColors;
    protected String imgFileFormat;

    /**
     * Generates the visual representation of the captcha and return it as array of bytes.
     *
     * @param textToRender String respresentation of the captcha solution that should be visible in the rendering
     *                     result.
     * @return byte array containing the finished rendering result.
     */
    public abstract byte[] renderAndConvertToBytes(final String textToRender);

    /**
     * Picks a random color from the array of possible text colors
     *
     * @param colors Color array from which to pick a random element.
     * @return Color object, picked randomly out of the textCols-Field array
     */
    protected final Color pickRandomColor(List<Color> colors) {
        if (colors.size() == 1) {
            return colors.get(0);
        }
        ThreadLocalRandom r = ThreadLocalRandom.current();
        int i = r.nextInt(colors.size());
        return colors.get(i);
    }
}
