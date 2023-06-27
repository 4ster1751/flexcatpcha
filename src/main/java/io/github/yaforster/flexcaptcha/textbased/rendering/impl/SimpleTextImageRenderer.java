package io.github.yaforster.flexcaptcha.textbased.rendering.impl;

import io.github.yaforster.flexcaptcha.textbased.rendering.TextImageRenderer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * Example implementation.
 * <p>
 * Implements a rendering logic taking in an input string to generate a
 * visualization of said string and return it in a base64 string representation
 * for easier transportation
 *
 * @author Yannick Forster
 */
@SuppressWarnings("DuplicatedCode")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SimpleTextImageRenderer implements TextImageRenderer {

    /**
     * Color of the Captcha Background
     */
    private Color backgrndCol = Color.white;
    /**
     * Set of possible colors of the letters in the captcha image
     */
    private Color[] textCols = new Color[]{Color.blue};
    /**
     * Color of distortions in the image
     */
    private Color distortCol = Color.white;
    /**
     * Defines the maximum angle that can be used to rotate a single character in
     * the captcha
     */
    private double maxrotateAngle = 0.45;
    /**
     * String name of the font to be used when writing characters to the image
     */
    private String fontName = "Verdana";

    /**
     * Renders a captcha image of specified height and widght of the given string
     */
    @Override
    public BufferedImage render(final String captchaTextInput, int height, int width) {
        if (StringUtils.isEmpty(captchaTextInput)) {
            throw new IllegalArgumentException("The specified captcha string is empty.");
        }
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphic = image.createGraphics();
        graphic.setColor(backgrndCol);
        graphic.fillRect(0, 0, width, height);
        drawDistortions(height, width, graphic);
        drawText(captchaTextInput, image);
        graphic.dispose();
        return image;
    }

    /**
     * Draws distortions onto the given Graphics2D object in the shape of randomly
     * generated rectangles and dots to help obscure the text in the captcha image
     *
     * @param height  pixel height of the image inside the graphics object
     * @param width   pixel width of the image inside the graphics object
     * @param graphic the Graphics2D object onto which the distortions are to be
     *                drawn
     */
    protected void drawDistortions(int height, int width, Graphics2D graphic) {
        graphic.setColor(distortCol);
        for (int i = 0; i < width / 64; i++) {
            int L = (int) (Math.random() * height / 2.0);
            int X = (int) (Math.random() * width - L);
            int Y = (int) (Math.random() * height - L);
            graphic.draw3DRect(X, Y, L * 2, L * 2, true);
        }
        Color darkerBackgrnd = backgrndCol.darker();
        graphic.setColor(darkerBackgrnd);
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int dotCount = height * width / 4;
        for (int j = 0; j < dotCount; j++) {
            int x = Math.abs(random.nextInt()) % width;
            int y = Math.abs(random.nextInt()) % height;
            graphic.drawLine(x, y, x, y);
        }
    }

    /**
     * prepares the writing of the given captcha text onto the specified Graphics2d
     * object
     *
     * @param captchaTextInput string containing the text to write
     * @param image            the image on which to draw the text
     */
    protected void drawText(String captchaTextInput, BufferedImage image) {
        Graphics2D graphic = image.createGraphics();
        Font textFont = new Font(fontName, Font.BOLD, (int) (image.getHeight() / 2.5));
        graphic.setColor(pickRandomColor(textCols));
        graphic.setFont(textFont);
        FontMetrics fontMetrics = graphic.getFontMetrics();
        int margin = image.getWidth() / 16;
        int chars = captchaTextInput.length();
        float spaceForLetters = -margin * 2 + image.getWidth();
        float spacePerChar = spaceForLetters / (chars - 1.0f);
        IntStream.range(0, chars).boxed().forEachOrdered(i -> {
            char charToDraw = captchaTextInput.charAt(i);
            drawCharacter(image, textFont, fontMetrics, margin, spacePerChar, i, charToDraw);
        });
    }

    /**
     * Measures the font and draws each character of the given string to the
     * Graphics2D object at a randomized angle.
     *
     * @param textFont     Font object containing the font in which the characters
     *                     are to be drawn
     * @param fontMetrics  fontmetrics object used to measure the characters in the
     *                     string
     * @param margin       calculated based on the width to define an approximate
     *                     margin between each letter
     * @param spacePerChar the space that the entire string will approximately
     *                     require
     * @param index        running index of the character in the source string
     */
    private void drawCharacter(BufferedImage image, Font textFont, FontMetrics fontMetrics, int margin,
                               float spacePerChar, Integer index, char charToDraw) {
        int maxAdvance = fontMetrics.getMaxAdvance();
        int fontHeight = fontMetrics.getHeight();
        int charWidth = fontMetrics.charWidth(charToDraw);
        int charDim = Math.max(maxAdvance, fontHeight);
        int halfCharDim = charDim / 2;
        BufferedImage charImage = new BufferedImage(charDim, charDim, BufferedImage.TYPE_INT_ARGB);
        Graphics2D charGraphics = charImage.createGraphics();
        charGraphics.translate(halfCharDim, halfCharDim);
        double angle = (Math.random() - 0.5) * maxrotateAngle;
        charGraphics.transform(AffineTransform.getRotateInstance(angle));
        charGraphics.translate(-halfCharDim, -halfCharDim);
        charGraphics.setColor(pickRandomColor(textCols));
        charGraphics.setFont(textFont);
        int charX = (int) (0.5 * charDim - 0.5 * charWidth);
        charGraphics.drawString(String.valueOf(charToDraw), charX,
                (charDim - fontMetrics.getAscent()) / 2 + fontMetrics.getAscent());
        float x = margin + spacePerChar * (index) - charDim / 2.0f;
        int y = (image.getHeight() - charDim) / 2;
        image.createGraphics().drawImage(charImage, (int) x, y, charDim, charDim, null, null);
        charGraphics.dispose();
    }

}
