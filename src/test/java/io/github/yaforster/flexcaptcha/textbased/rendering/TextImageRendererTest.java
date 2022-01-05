package io.github.yaforster.flexcaptcha.textbased.rendering;

import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;

import org.junit.Before;
import org.junit.Test;

import lombok.Getter;

/**
 * Tests {@link TextImageRenderer} interface
 * 
 * @author Yannick Forster
 *
 */
@Getter
public abstract class TextImageRendererTest<T extends TextImageRenderer> {
	
	private T renderer;

    protected abstract T createRenderer();

    @Before 
    public void setUp() {
        renderer = createRenderer();
    }

    @Test
    public void render() {
		int targetHeight = 100;
		int widthHeight = 200;
		BufferedImage image = renderer.render("TEST", targetHeight, widthHeight);
		assertTrue(image.getHeight()==targetHeight);
		assertTrue(image.getWidth()==widthHeight);
	}

}
