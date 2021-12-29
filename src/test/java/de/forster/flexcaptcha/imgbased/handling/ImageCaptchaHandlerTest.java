package de.forster.flexcaptcha.imgbased.handling;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import de.forster.flexcaptcha.CipherHandler;
import de.forster.flexcaptcha.imgbased.ImageCaptcha;

/**
 * Tests the default methods of the {@link ImageCaptchaHandler} interface using
 * an arbitrary implementation
 * 
 * @author Yannick Forster
 *
 */
//TODO: Mock CipherHandler
public class ImageCaptchaHandlerTest {

	ImageCaptchaHandlerTestHelper helper = new ImageCaptchaHandlerTestHelper();
	CipherHandler cipherHandler = new CipherHandler();

	@Test
	public void testWithDummyObjs_ShouldWork() {
		ImageCaptcha captcha = helper.getHandler().generate(2, cipherHandler, helper.getDummySerializable(),
				helper.getPassword(), helper.getDummyArr(), helper.getDummyArr2());
		assertTrue(captcha.getToken().length() == 173);
		assertTrue(ArrayUtils.isNotEmpty(captcha.getImgData()));
	}

	@Test
	public void testWithDifferentDummyObjs_ShouldWork() {
		ImageCaptcha captcha = helper.getHandler().generate(2, cipherHandler, helper.getDummySerializable(),
				helper.getPassword(), helper.getDummyArr(), helper.getDummyArr2());
		assertTrue(captcha.getToken().length() == 173);
		assertTrue(Arrays.stream(captcha.getImgData()).distinct().count() > 2);
	}

	@Test
	public void testNullSerializable_Shouldwork() {
		ImageCaptcha captcha = helper.getHandler().generate(2, cipherHandler, null, helper.getPassword(),
				helper.getDummyArr(), helper.getDummyArr2());
		assertTrue(captcha.getToken().length() == 173);
		assertTrue(ArrayUtils.isNotEmpty(captcha.getImgData()));
	}

	@Test
	public void testEmptySerializable_Shouldwork() {
		ImageCaptcha captcha = helper.getHandler().generate(2, cipherHandler, "", helper.getPassword(),
				helper.getDummyArr(), helper.getDummyArr2());
		assertTrue(captcha.getToken().length() == 173);
		assertTrue(ArrayUtils.isNotEmpty(captcha.getImgData()));
	}

	@Test
	public void testNullArrays() {
		assertThrows(IllegalArgumentException.class, () -> {
			helper.getHandler().generate(2, cipherHandler, null, helper.getPassword(), null, null);
		});
	}

	@Test
	public void testEmptyArrays() {
		assertThrows(IllegalArgumentException.class, () -> {
			helper.getHandler().generate(2, cipherHandler, null, helper.getPassword(), new BufferedImage[] {},
					new BufferedImage[] {});
		});
	}

	@Test
	public void testNullSolutionArray() {
		assertThrows(IllegalArgumentException.class, () -> {
			helper.getHandler().generate(2, cipherHandler, null, helper.getPassword(), helper.getDummyArr(), null);
		});
	}

	@Test
	public void testEmptySolutionArray() {
		assertThrows(IllegalArgumentException.class, () -> {
			helper.getHandler().generate(2, cipherHandler, null, helper.getPassword(), new BufferedImage[] {}, null);
		});
	}

	@Test
	public void testNullFillArray() {
		assertThrows(IllegalArgumentException.class, () -> {
			helper.getHandler().generate(2, cipherHandler, null, helper.getPassword(), null, helper.getDummyArr());
		});
	}

	@Test
	public void testEmptyFillArray() {
		assertThrows(IllegalArgumentException.class, () -> {
			helper.getHandler().generate(2, cipherHandler, null, helper.getPassword(), helper.getDummyArr(),
					new BufferedImage[] {});
		});
	}

	@Test
	public void autoResizeTest30x30() throws IOException {
		BufferedImage smallImage = new BufferedImage(10, 10, BufferedImage.TYPE_3BYTE_BGR);
		BufferedImage largeImage = new BufferedImage(30, 30, BufferedImage.TYPE_3BYTE_BGR);
		BufferedImage[] images = new BufferedImage[] { smallImage, largeImage };
		ImageCaptcha captcha = helper.getHandler().generate(2, cipherHandler, helper.getDummySerializable(),
				helper.getPassword(), images, images);
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
		assertTrue(Stream.of(resizedImages).allMatch(img -> (img.getHeight() == 30 && img.getWidth() == 30)));
	}

	@Test
	public void autoResizeTest30x10() throws IOException {
		BufferedImage smallImage = new BufferedImage(10, 10, BufferedImage.TYPE_3BYTE_BGR);
		BufferedImage largeImage = new BufferedImage(10, 30, BufferedImage.TYPE_3BYTE_BGR);
		BufferedImage[] images = new BufferedImage[] { smallImage, largeImage };
		ImageCaptcha captcha = helper.getHandler().generate(2, cipherHandler, helper.getDummySerializable(),
				helper.getPassword(), images, images);
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
		assertTrue(Stream.of(resizedImages).allMatch(img -> (img.getHeight() == 30 && img.getWidth() == 10)));
	}

	@Test
	public void autoResizeTest10x30() throws IOException {
		BufferedImage smallImage = new BufferedImage(10, 10, BufferedImage.TYPE_3BYTE_BGR);
		BufferedImage largeImage = new BufferedImage(30, 10, BufferedImage.TYPE_3BYTE_BGR);
		BufferedImage[] images = new BufferedImage[] { smallImage, largeImage };
		ImageCaptcha captcha = helper.getHandler().generate(2, cipherHandler, helper.getDummySerializable(),
				helper.getPassword(), images, images);
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
		assertTrue(Stream.of(resizedImages).allMatch(img -> (img.getHeight() == 10 && img.getWidth() == 30)));
	}

}
