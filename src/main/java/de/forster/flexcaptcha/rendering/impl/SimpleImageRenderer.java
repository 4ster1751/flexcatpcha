package de.forster.flexcaptcha.rendering.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;

import de.forster.flexcaptcha.rendering.ImageRenderer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Implements a rendering logic taking in an input string to generate a
 * visualization of said string and return it in an base64 string representation
 * for easier transportation
 * 
 * @author Yannick Forster
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleImageRenderer implements ImageRenderer {

	/**
	 * Color of the Captcha Background
	 */
	Color backgrndCol = Color.white;
	/**
	 * Set of possible colors of the letters in the captcha image
	 */
	Color[] textCols = new Color[] {Color.blue};
	/**
	 * Color of distortions in the image
	 */
	Color distortCol = Color.white;
	/**
	 * Defines the maximum angle that can be used to rotate a single character in
	 * the captcha
	 */
	double maxrotateAngle = 0.45;
	/**
	 * String name of the font to be used when writing characters to the image
	 */
	String fontName = "Verdana";

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
		drawText(captchaTextInput, height, width, graphic);
		graphic.dispose();
		return image;
	}
	
	/**
	 * Draws distortions onto the given Graphics2D object in the shape of randomly
	 * generated rectangles to help obscure the text in the captcha image
	 * 
	 * @param height  pixel height of the image inside the graphics object
	 * @param width   pixel width of the image inside the graphics object
	 * @param graphic the Graphics2D object onto which the distortions are to be
	 *                drawn
	 */
	private void drawDistortions(int height, int width, Graphics2D graphic) {
		graphic.setColor(distortCol);
		for (int i = 0; i < width / 6; i++) {
			int L = (int) (Math.random() * height / 2.0);
			int X = (int) (Math.random() * width - L);
			int Y = (int) (Math.random() * height - L);
			graphic.draw3DRect(X, Y, L * 2, L * 2, true);
		}
	}

	/**
	 * prepares the writing of the given captcha text onto the specified Graphics2d
	 * object
	 * 
	 * @param captchaTextInput string containing the text to write
	 * @param height           pixel count of the height of the graphic
	 * @param width            pixel count of the width of the graphic
	 * @param graphic          the Graphics2D object containing the graphic in which
	 *                         the image is constructed
	 */
	private void drawText(String captchaTextInput, int height, int width, Graphics2D graphic) {
		Font textFont = new Font(fontName, Font.BOLD, (int) (height / 2.5));
		graphic.setColor(pickRandomColor(textCols));
		graphic.setFont(textFont);
		FontMetrics fontMetrics = graphic.getFontMetrics();
		int maxAdvance = fontMetrics.getMaxAdvance();
		int fontHeight = fontMetrics.getHeight();
		int margin = width / 16;
		int chars = captchaTextInput.length();
		float spaceForLetters = -margin * 2 + width;
		float spacePerChar = spaceForLetters / (chars - 1.0f);
		IntStream.range(0, chars).boxed().forEachOrdered(i -> {
			drawCharacter(graphic, textFont, fontMetrics, height, maxAdvance, fontHeight, margin, spacePerChar, i, captchaTextInput);
		});
	}

	/**
	 * Measures the font and draws each character of the given string to the
	 * Graphics2D object at a randomized angle.
	 * 
	 * @param graphic      the Graphics2D object containing the graphic in which the
	 *                     image is constructed
	 * @param textFont     Font object containing the font in which the characters
	 *                     are to be drawn
	 * @param fontMetrics  fontmetrics object used to measure the characters in the
	 *                     string
	 * @param height       pixel count of the height of the graphic
	 * @param maxAdvance   measured approximate maxAdvance width
	 * @param fontHeight   measured standard height of the font
	 * @param margin       calculated based on the width to define an approximate
	 *                     margin between each letter
	 * @param spacePerChar the space that the entire string will approximately
	 *                     require
	 * @param index        running index of the character in the source string
	 * @param sourceString the source string
	 */
	private void drawCharacter(Graphics2D graphic, Font textFont, FontMetrics fontMetrics, int height, int maxAdvance,
			int fontHeight, int margin, float spacePerChar, Integer index, String sourceString) {
		int charWidth = fontMetrics.charWidth(sourceString.charAt(index));
		int charDim = Math.max(maxAdvance, fontHeight);
		int halfCharDim = (int) (charDim / 2);
		BufferedImage charImage = new BufferedImage(charDim, charDim, BufferedImage.TYPE_INT_ARGB);
		Graphics2D charGraphics = charImage.createGraphics();
		charGraphics.translate(halfCharDim, halfCharDim);
		double angle = (Math.random() - 0.5) * maxrotateAngle;
		charGraphics.transform(AffineTransform.getRotateInstance(angle));
		charGraphics.translate(-halfCharDim, -halfCharDim);
		charGraphics.setColor(pickRandomColor(textCols));
		charGraphics.setFont(textFont);
		int charX = (int) (0.5 * charDim - 0.5 * charWidth);
		charGraphics.drawString("" + sourceString.charAt(index), charX, (int) ((charDim - fontMetrics.getAscent()) / 2 + fontMetrics.getAscent()));
		float x = margin + spacePerChar * (index) - charDim / 2.0f;
		int y = (int) ((height - charDim) / 2);
		graphic.drawImage(charImage, (int) x, y, charDim, charDim, null, null);
		charGraphics.dispose();
	}	

}
