package io.github.yaforster.flexcaptcha.impl.rendering;

import com.jhlabs.image.AbstractBufferedImageOp;
import io.github.yaforster.flexcaptcha.core.AbstractCaptchaImageBackground;
import io.github.yaforster.flexcaptcha.core.AbstractCaptchaRenderer;
import lombok.Builder;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class CaptchaRenderer extends AbstractCaptchaRenderer {

    private static final int DEFAULT_PICTURE_HEIGHT = 100;
    private static final int DEFAULT_PICTURE_WIDTH = 300;
    private static final String DEFAULT_IMAGE_FORMAT = "JPG";
    /**
     * Default value for the background of the image. Renders as plain white background with no texture.
     */
    private static final AbstractCaptchaImageBackground DEFAULT_BACKGROUND = new FlatColorBackground(Color.white);
    /**
     * Default value of the upper bound of randomized angle any rendered character can have
     */
    private static final double DEFAULT_MAX_LETTER_ROTATION_ANGLE = 0.35d;
    private static final String DEFAULT_FONT = "Verdana";
    /**
     * Defines the maximum angle that can be used to rotate a single character in the captcha
     */
    private final Double maximumLetterRotationAngle;
    /**
     * String name of the font to be used when writing characters to the image
     */
    private final String fontName;
    /**
     * List of operations that will be applied to the image during rendering.
     */
    private final List<AbstractBufferedImageOp> imageOperationsList;
    /**
     * Settings for how the Background of the Captcha is supposed to be rendered.
     */
    private final AbstractCaptchaImageBackground imageBackground;
    /**
     * Settings for the noise added to the image.
     */
    private final NoiseSettings noiseSettings;

    @Builder
    public CaptchaRenderer(int pictureHeight, int pictureWidth, List<Color> availableTextColors, String imgFileFormat
            , Double maximumLetterRotationAngle, String fontName, List<AbstractBufferedImageOp> imageOperationsList,
                           AbstractCaptchaImageBackground imageBackground, NoiseSettings noiseSettings) {
        super(getPictureHeightOrDefault(pictureHeight), getPictureWidthOrDefault(pictureWidth),
                getAvailableTextColorsOrDefault(availableTextColors), getImageFileFormatOrDefault(imgFileFormat));
        this.maximumLetterRotationAngle = getMaximumLetterRotationAngleOrDefault(maximumLetterRotationAngle);
        this.fontName = getFontNameOrDefault(fontName);
        this.imageOperationsList = getImageOperationsListOrDefault(imageOperationsList);
        this.imageBackground = getImageBackgroundOrDefault(imageBackground);
        this.noiseSettings = getNoiseSettingsOrDefault(noiseSettings);
    }

    /**
     * Checks the given pictureHeight and uses the default instead if the value is 0
     */
    private static int getPictureHeightOrDefault(int pictureHeight) {
        return pictureHeight != 0 ? pictureHeight : DEFAULT_PICTURE_HEIGHT;
    }

    /**
     * Checks the given pictureWidth and uses the default instead if the value is 0
     */
    private static int getPictureWidthOrDefault(int pictureWidth) {
        return pictureWidth != 0 ? pictureWidth : DEFAULT_PICTURE_WIDTH;
    }

    private static List<Color> getAvailableTextColorsOrDefault(List<Color> availableTextColors) {
        return (availableTextColors == null || availableTextColors.isEmpty()) ?
                Collections.singletonList(Color.BLACK) : availableTextColors;
    }

    private static String getImageFileFormatOrDefault(String imgFileFormat) {
        return StringUtils.isBlank(imgFileFormat) ? DEFAULT_IMAGE_FORMAT : imgFileFormat;
    }

    private static Double getMaximumLetterRotationAngleOrDefault(Double maxrotateAngle) {
        return Optional.ofNullable(maxrotateAngle).orElse(DEFAULT_MAX_LETTER_ROTATION_ANGLE);
    }

    private static String getFontNameOrDefault(String fontName) {
        return StringUtils.isBlank(fontName) ? DEFAULT_FONT : fontName;
    }

    private static List<AbstractBufferedImageOp> getImageOperationsListOrDefault(List<AbstractBufferedImageOp> imageOperationsList) {
        return Optional.ofNullable(imageOperationsList).orElse(Collections.emptyList());
    }

    private static AbstractCaptchaImageBackground getImageBackgroundOrDefault(AbstractCaptchaImageBackground imageBackground) {
        return Optional.ofNullable(imageBackground).orElse(DEFAULT_BACKGROUND);
    }

    /**
     * Sets a clean NoiseSetting that will not produce any noise when rendered if the given noiseSetting is null or
     * contains no color.
     */
    private static NoiseSettings getNoiseSettingsOrDefault(NoiseSettings noiseSettings) {
        if (noiseSettings == null || noiseSettings.distortionsColor() == null) {
            return null;
        }
        else {
            return noiseSettings;
        }
    }

    /**
     * @return Shortcut to get a default renderer without any user customized settings
     */
    public static CaptchaRenderer getDefaultCaptchaRenderer() {
        return CaptchaRenderer.builder().build();
    }

    @Override
    public final byte[] renderAndConvertToBytes(String textToRender) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            BufferedImage image = new BufferedImage(pictureWidth, pictureHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphic = image.createGraphics();
            imageBackground.drawBackground(image);
            if (noiseSettings != null) {
                drawDistortions(graphic);
            }
            drawText(textToRender, image);
            graphic.dispose();
            applyFilters(image);
            ImageIO.write(image, imgFileFormat, bos);
            return bos.toByteArray();
        }
        catch (IOException e) {
            throw new CaptchaRenderingException(e);
        }
    }

    /**
     * Draws distortions onto the given Graphics2D object in the shape of randomly
     * generated rectangles and dots to help obscure the text in the captcha image
     *
     * @param graphic the Graphics2D object onto which the distortions are to be
     *                drawn
     */
    private void drawDistortions(Graphics2D graphic) {
        int lineCount = (pictureWidth * noiseSettings.noiseIntensity()) / 100;
        int dotCount = ((pictureHeight * pictureWidth) * noiseSettings.noiseIntensity()) / 100;
        for (int i = 0; i < lineCount; i++) {
            drawRectangleNoise(graphic);
        }
        for (int j = 0; j < dotCount; j++) {
            drawLineNoise(graphic);
        }
    }

    /**
     * prepares the writing of the given captcha text onto the specified Graphics2d
     * object
     *
     * @param textToRender string containing the text to write
     * @param image        the image on which to draw the text
     */
    private void drawText(String textToRender, BufferedImage image) {
        int chars = textToRender.length();
        TextRenderingData textRenderingData = buildTextRenderingData(image, chars);
        IntStream.range(0, chars).boxed().forEachOrdered(i -> {
            char charToDraw = textToRender.charAt(i);
            drawCharacter(image, textRenderingData, i, charToDraw);
        });
    }

    /**
     * Applies the stored operations in imageOperationsList to the image.
     *
     * @param image the image on which to render
     */
    private void applyFilters(BufferedImage image) {
        imageOperationsList.stream().forEachOrdered(op -> op.filter(image, image));
    }

    private void drawRectangleNoise(Graphics2D graphic) {
        graphic.setColor(noiseSettings.distortionsColor());
        SecureRandom rnd = new SecureRandom();
        int L = (int) (rnd.nextDouble() * pictureHeight / 2.0);
        int X = (int) (rnd.nextDouble() * pictureWidth - L);
        int Y = (int) (rnd.nextDouble() * pictureHeight - L);
        graphic.draw3DRect(X, Y, L << 1, L << 1, true);
    }

    private void drawLineNoise(Graphics2D graphic) {
        Color darkerBackgrnd = noiseSettings.distortionsColor().darker();
        graphic.setColor(darkerBackgrnd);
        SecureRandom rnd = new SecureRandom();
        int x = Math.abs(rnd.nextInt()) % pictureWidth;
        int y = Math.abs(rnd.nextInt()) % pictureHeight;
        graphic.drawLine(x, y, x, y);
    }

    /**
     * Builds a {@link TextRenderingData} object for cleaner passing of multiple arguments within this class
     *
     * @param image the image on which to draw the text
     * @return {@link TextRenderingData} with text rendering information derived from the given image and the number
     * of characters
     */
    private TextRenderingData buildTextRenderingData(BufferedImage image, int chars) {
        Graphics2D graphic = image.createGraphics();
        Font textFont = new Font(fontName, Font.BOLD, (int) (image.getHeight() / 2.5));
        graphic.setColor(pickRandomColor(availableTextColors));
        graphic.setFont(textFont);
        FontMetrics fontMetrics = graphic.getFontMetrics();
        int maxAdvance = fontMetrics.getMaxAdvance();
        int fontHeight = fontMetrics.getHeight();
        int charDim = Math.max(maxAdvance, fontHeight);
        int margin = image.getWidth() / 16;
        float spaceForLetters = (-margin << 1) + image.getWidth();
        float spaceBetweenCharacters = spaceForLetters / (chars - 1.0f);
        return new TextRenderingData(textFont, fontMetrics, margin, spaceBetweenCharacters, maxAdvance, fontHeight,
                charDim);
    }

    /**
     * Measures the font and draws each character of the given string to the
     * Graphics2D object at a randomized angle.
     *
     * @param tRD        Text rendering data used to bundle all relevant data about the font used to compute their
     *                   placement within the captcha image.
     * @param image      the image on which to draw the text
     * @param charToDraw the individual character to measure and draw
     * @param index      running index of the character in the source string
     */
    private void drawCharacter(BufferedImage image, TextRenderingData tRD, Integer index, char charToDraw) {
        BufferedImage charImage = getImageOfAngledRenderedCharacter(tRD, charToDraw);
        int charDim = tRD.charDim();
        int x = getHorizontalPlacementOfCharacter(tRD, index, charDim);
        int y = (image.getHeight() - charDim) / 2;
        image.createGraphics().drawImage(charImage, x, y, charDim, charDim, null, null);
    }

    /**
     * @param tRD        Text rendering data used to bundle all relevant data about the font used to compute their
     *                   placement within the captcha image.
     * @param charToDraw the individual character to measure and draw
     * @return BufferedImage containing a single rendered and angled character to be merged with the main captcha image.
     */
    private BufferedImage getImageOfAngledRenderedCharacter(TextRenderingData tRD, char charToDraw) {
        int charDim = tRD.charDim();
        int charWidth = tRD.fontMetrics().charWidth(charToDraw);
        int halfCharDim = charDim / 2;
        double angle = getRandomAngleWithinMaximumBounds();
        int charX = (int) (0.5 * charDim - 0.5 * charWidth);
        BufferedImage charImage = new BufferedImage(charDim, charDim, BufferedImage.TYPE_INT_ARGB);
        Graphics2D charGraphics = charImage.createGraphics();
        charGraphics.translate(halfCharDim, halfCharDim);
        charGraphics.transform(AffineTransform.getRotateInstance(angle));
        charGraphics.translate(-halfCharDim, -halfCharDim);
        charGraphics.setColor(pickRandomColor(availableTextColors));
        charGraphics.setFont(tRD.font());
        charGraphics.drawString(String.valueOf(charToDraw), charX, (charDim - tRD.fontMetrics()
                .getAscent()) / 2 + tRD.fontMetrics().getAscent());
        charGraphics.dispose();
        return charImage;
    }

    private static int getHorizontalPlacementOfCharacter(TextRenderingData tRD, Integer index, int charDimensions) {
        return (int) (tRD.margin() + tRD.spaceBetweenCharacters() * (index.floatValue()) - charDimensions / 2.0f);
    }

    private double getRandomAngleWithinMaximumBounds() {
        return (new SecureRandom().nextDouble() - 0.5) * maximumLetterRotationAngle;
    }


}
