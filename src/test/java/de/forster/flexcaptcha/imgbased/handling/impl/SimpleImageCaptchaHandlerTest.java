package de.forster.flexcaptcha.imgbased.handling.impl;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import org.junit.Test;

import de.forster.flexcaptcha.CipherHandler;
import de.forster.flexcaptcha.imgbased.ImageCaptcha;
import de.forster.flexcaptcha.imgbased.handling.ImageCaptchaHandlerTestHelper;

/**
 * Tests {@link SimpleImageCaptchaHandler}
 * 
 * @author Yannick Forster
 *
 */

//TODO: Mock CipherHandler
public class SimpleImageCaptchaHandlerTest {

	ImageCaptchaHandlerTestHelper helper = new ImageCaptchaHandlerTestHelper();
	SimpleImageCaptchaHandler handler = new SimpleImageCaptchaHandler();
	CipherHandler cipherHandler = new CipherHandler();

	@Test
	public void testAllNull() {
		assertThrows("gridWidth must be larger than 1.", IllegalArgumentException.class, () -> {
			handler.generate(0, cipherHandler, null, null, null, null, 0, 0);
		});
	}

	@Test
	public void testGridWidth0_ShouldFail() {
		assertThrows(IllegalArgumentException.class, () -> {
			handler.generate(0, cipherHandler, helper.getDummySerializable(), helper.getPassword(),
					helper.getDummyArr(), helper.getDummyArr2());
		});
	}

	@Test
	public void testGridWidth1_ShouldFail() {
		assertThrows(IllegalArgumentException.class, () -> {
			handler.generate(1, cipherHandler, helper.getDummySerializable(), helper.getPassword(),
					helper.getDummyArr(), helper.getDummyArr2());
		});
	}

	@Test
	public void manualResizeTest30x10() throws IOException {
		BufferedImage smallImage = new BufferedImage(10, 10, BufferedImage.TYPE_3BYTE_BGR);
		BufferedImage largeImage = new BufferedImage(10, 30, BufferedImage.TYPE_3BYTE_BGR);
		BufferedImage[] images = new BufferedImage[] { smallImage, largeImage };
		ImageCaptcha captcha = helper.getHandler().generate(2, cipherHandler, helper.getDummySerializable(),
				helper.getPassword(), images, images, 30, 10);
		BufferedImage[] resizedImages = getResizedImages(captcha);
		assertTrue(Stream.of(resizedImages).allMatch(img -> (img.getHeight() == 30 && img.getWidth() == 10)));
	}

	@Test
	public void manualResizeTest30x30() throws IOException {
		BufferedImage smallImage = new BufferedImage(10, 10, BufferedImage.TYPE_3BYTE_BGR);
		BufferedImage largeImage = new BufferedImage(10, 30, BufferedImage.TYPE_3BYTE_BGR);
		BufferedImage[] images = new BufferedImage[] { smallImage, largeImage };
		ImageCaptcha captcha = helper.getHandler().generate(2, cipherHandler, helper.getDummySerializable(),
				helper.getPassword(), images, images, 30, 30);
		BufferedImage[] resizedImages = getResizedImages(captcha);
		assertTrue(Stream.of(resizedImages).allMatch(img -> (img.getHeight() == 30 && img.getWidth() == 30)));
	}

	@Test
	public void manualResizeTest10x30() throws IOException {
		BufferedImage smallImage = new BufferedImage(10, 10, BufferedImage.TYPE_3BYTE_BGR);
		BufferedImage largeImage = new BufferedImage(10, 30, BufferedImage.TYPE_3BYTE_BGR);
		BufferedImage[] images = new BufferedImage[] { smallImage, largeImage };
		ImageCaptcha captcha = helper.getHandler().generate(2, cipherHandler, helper.getDummySerializable(),
				helper.getPassword(), images, images, 10, 30);
		BufferedImage[] resizedImages = getResizedImages(captcha);
		assertTrue(Stream.of(resizedImages).allMatch(img -> (img.getHeight() == 10 && img.getWidth() == 30)));
	}

	private BufferedImage[] getResizedImages(ImageCaptcha captcha) {
		BufferedImage[] resizedImages = Stream.of(captcha.getImgData()).map(data -> {
			try {
				ByteArrayInputStream stream = new ByteArrayInputStream(data);
				BufferedImage resized = ImageIO.read(stream);
				return resized;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}).toArray(BufferedImage[]::new);
		return resizedImages;
	}

}
