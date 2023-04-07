package io.github.yaforster.flexcaptcha.textbased.rendering.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

import com.jhlabs.image.TwirlFilter;

import io.github.yaforster.flexcaptcha.textbased.rendering.TextImageRenderer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Text image handler that adds a twirl effect to the rendered text captcha
 * image
 * 
 * @author Yannick Forster
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TwirledTextImageRenderer implements TextImageRenderer {

	/**
	 * Set of possible colors of the letters in the captcha image
	 */
	Color[] textCols = new Color[] { Color.blue, Color.red, Color.darkGray, Color.magenta, Color.black };
	/**
	 * Radius integer used for the twirling effect. Higher numbers result in a
	 * stronger effect. 10 per default.
	 */
	float twirlStrength = -0.3f;

	@Override
	public BufferedImage render(final String captchaTextInput, int height, int width) {
		SimpleTextImageRenderer simpleRenderer = new SimpleTextImageRenderer();
		simpleRenderer.setTextCols(textCols);
		BufferedImage image = simpleRenderer.render(captchaTextInput, height, width);
		return applytwirl(image);
	}

	/**
	 * Applies a twisting effect of the entire graphic originating in the center of
	 * the image
	 *
	 */
	private BufferedImage applytwirl(BufferedImage image) {
		TwirlFilter filter = new TwirlFilter();
		ThreadLocalRandom random = ThreadLocalRandom.current();
		float angle = getTwirlStrength();
		if (random.nextBoolean()) {
			angle = angle * (-1);
		}
		filter.setAngle(angle);
		return filter.filter(image, new BufferedImage(image.getWidth(), image.getHeight(), image.getType()));
	}

}
