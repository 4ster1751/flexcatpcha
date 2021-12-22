package de.forster.flexcaptcha.rendering.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.jhlabs.image.AbstractBufferedImageOp;

import de.forster.flexcaptcha.rendering.ImageRenderer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EffectChainRenderer implements ImageRenderer {
	
	/**
	 * Set of possible colors of the letters in the captcha image
	 */
	Color[] textCols = new Color[] {Color.blue, Color.red, Color.darkGray, Color.magenta, Color.black};
	
	/**
	 * List of operations that will be applied to the image during rendering.
	 */
	List<AbstractBufferedImageOp> bufferedOps = new ArrayList<AbstractBufferedImageOp>();
	
	@Override
	public BufferedImage render(final String captchaTextInput, int height, int width) {
		BufferedImage image = new SimpleImageRenderer().render(captchaTextInput, height, width);
		if(!bufferedOps.isEmpty()) {
			image = applyFilters(image);
		}
		return image;
	}

	private BufferedImage applyFilters(BufferedImage image) {
		bufferedOps.forEach(op -> op.filter(image, image));
		return image;
	}
}
