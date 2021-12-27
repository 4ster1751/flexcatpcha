package de.forster.flexcaptcha.rendering.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

import com.jhlabs.image.TwirlFilter;

import de.forster.flexcaptcha.rendering.ImageRenderer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TwirledImageRenderer implements ImageRenderer{
	
	/**
	 * Set of possible colors of the letters in the captcha image
	 */
	Color[] textCols = new Color[] {Color.blue, Color.red, Color.darkGray, Color.magenta, Color.black};
	/**
	 * Radius integer used for the twirling effect. Higher numbers result in a stronger effect. 10 per default.
	 */
	float twirlStrength = -0.3f;

	@Override
	public BufferedImage render(final String captchaTextInput, int height, int width) {
		SimpleImageRenderer simpleRenderer = new SimpleImageRenderer();
		simpleRenderer.setTextCols(textCols);
		BufferedImage image = simpleRenderer.render(captchaTextInput, height, width);
		return applytwirl(image);
	}

	/**
	 * Applies a twisting effect of the entire graphic originating in the center of
	 * the image
	 * 
	 * @param height  pixel count of the height of the graphic
	 * @param width   pixel count of the width of the graphic
	 * @param graphic the Graphics2D object containing the graphic in which the
	 *                image is constructed
	 * @return 
	 */
	private BufferedImage applytwirl(BufferedImage image) {
		TwirlFilter filter = new TwirlFilter();
		ThreadLocalRandom random = ThreadLocalRandom.current();
		float angle = getTwirlStrength();
		if(random.nextBoolean()) {
			angle=angle*(-1);
		}
		filter.setAngle(angle);
		return filter.filter(image, new BufferedImage(image.getWidth(), image.getHeight(), image.getType()));
	}

}
