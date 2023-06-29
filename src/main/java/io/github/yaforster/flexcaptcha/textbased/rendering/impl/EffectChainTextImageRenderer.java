package io.github.yaforster.flexcaptcha.textbased.rendering.impl;

import com.jhlabs.image.AbstractBufferedImageOp;
import io.github.yaforster.flexcaptcha.textbased.rendering.TextImageRenderer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Text Captcha rendering using a chain of Filters that will be applied to the generated image.
 *
 * @author Yannick Forster
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class EffectChainTextImageRenderer implements TextImageRenderer {

    /**
     * Set of possible colors of the letters in the captcha image
     */
    private Color[] textCols = new Color[]{Color.blue, Color.red, Color.darkGray, Color.magenta, Color.black};

    /**
     * List of operations that will be applied to the image during rendering.
     */
    private List<AbstractBufferedImageOp> bufferedOps = new ArrayList<>(0);

    @Override
    public BufferedImage render(final String captchaTextInput, int height, int width) {
        BufferedImage image = new SimpleTextImageRenderer().render(captchaTextInput, height, width);
        if (!bufferedOps.isEmpty()) {
            applyFilters(image);
        }
        return image;
    }

    private void applyFilters(BufferedImage image) {
        bufferedOps.forEach(op -> op.filter(image, image));
    }
}
