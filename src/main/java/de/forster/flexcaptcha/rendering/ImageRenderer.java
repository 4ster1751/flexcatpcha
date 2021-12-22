package de.forster.flexcaptcha.rendering;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Describes the logic required to render
 * 
 * @author Yannick Forster
 *
 */
public interface ImageRenderer {
	BufferedImage render(final String captchaTextInput, int height, int width);
	
	/**
	 * Picks a random color from the array of possible text colors
	 * 
	 * @return Color object, picked randomly out of the textCols-Field array
	 */
	default Color pickRandomColor(Color[] colors) {
		if(colors.length==1) {
			return colors[0];
		}
		ThreadLocalRandom r = ThreadLocalRandom.current();
		int i = r.nextInt(colors.length);
		return colors[i];
	}
}
