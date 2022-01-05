package io.github.yaforster.flexcaptcha.imgbased.handling;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.awt.Button;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

import javax.crypto.spec.IvParameterSpec;
import javax.imageio.ImageIO;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;
import org.mockito.Mockito;

import io.github.yaforster.flexcaptcha.CipherHandler;
import io.github.yaforster.flexcaptcha.imgbased.ImageCaptcha;
import io.github.yaforster.flexcaptcha.imgbased.handling.impl.SimpleImageCaptchaHandler;

/**
 * Tests the default methods of the {@link ImageCaptchaHandler} interface using
 * an arbitrary implementation
 * 
 * @author Yannick Forster
 *
 */
public class ImageCaptchaHandlerTest {

	ImageCaptchaHandler handler = new SimpleImageCaptchaHandler();
	BufferedImage[] dummyArr = new BufferedImage[] { new BufferedImage(10, 10, BufferedImage.TYPE_3BYTE_BGR) };
	BufferedImage[] dummyArr2 = new BufferedImage[] { new BufferedImage(15, 15, BufferedImage.TYPE_4BYTE_ABGR) };
	Button dummySerializable = new Button();
	String password = "ThisIsMyPassword!";
	CipherHandler cipherHandler = getCHMock();
	

	@Test
	public void testWithDummyObjs_ShouldWork() {
		ImageCaptcha captcha = handler.generate(2, cipherHandler, dummySerializable,
				password, dummyArr, dummyArr2);
		assertTrue(captcha.getToken().length()>0);
		assertTrue(ArrayUtils.isNotEmpty(captcha.getImgData()));
	}

	@Test
	public void testWithDifferentDummyObjs_ShouldWork() {
		ImageCaptcha captcha = handler.generate(2, cipherHandler, dummySerializable,
				password, dummyArr, dummyArr2);
		assertTrue(captcha.getToken().length()>0);
		assertTrue(Arrays.stream(captcha.getImgData()).distinct().count() > 2);
	}

	@Test
	public void testNullSerializable_Shouldwork() {
		ImageCaptcha captcha = handler.generate(2, cipherHandler, null, password,
				dummyArr, dummyArr2);
		assertTrue(captcha.getToken().length()>0);
		assertTrue(ArrayUtils.isNotEmpty(captcha.getImgData()));
	}

	@Test
	public void testEmptySerializable_Shouldwork() {
		ImageCaptcha captcha = handler.generate(2, cipherHandler, "", password,
				dummyArr, dummyArr2);
		assertTrue(captcha.getToken().length()>0);
		assertTrue(ArrayUtils.isNotEmpty(captcha.getImgData()));
	}

	@Test
	public void testNullArrays() {
		assertThrows(IllegalArgumentException.class, () -> {
			handler.generate(2, cipherHandler, null, password, null, null);
		});
	}

	@Test
	public void testEmptyArrays() {
		assertThrows(IllegalArgumentException.class, () -> {
			handler.generate(2, cipherHandler, null, password, new BufferedImage[] {},
					new BufferedImage[] {});
		});
	}

	@Test
	public void testNullSolutionArray() {
		assertThrows(IllegalArgumentException.class, () -> {
			handler.generate(2, cipherHandler, null, password, dummyArr, null);
		});
	}

	@Test
	public void testEmptySolutionArray() {
		assertThrows(IllegalArgumentException.class, () -> {
			handler.generate(2, cipherHandler, null, password, new BufferedImage[] {}, null);
		});
	}

	@Test
	public void testNullFillArray() {
		assertThrows(IllegalArgumentException.class, () -> {
			handler.generate(2, cipherHandler, null, password, null, dummyArr);
		});
	}

	@Test
	public void testEmptyFillArray() {
		assertThrows(IllegalArgumentException.class, () -> {
			handler.generate(2, cipherHandler, null, password, dummyArr,
					new BufferedImage[] {});
		});
	}

	@Test
	public void autoResizeTest30x30() throws IOException {
		BufferedImage smallImage = new BufferedImage(10, 10, BufferedImage.TYPE_3BYTE_BGR);
		BufferedImage largeImage = new BufferedImage(30, 30, BufferedImage.TYPE_3BYTE_BGR);
		BufferedImage[] images = new BufferedImage[] { smallImage, largeImage };
		ImageCaptcha captcha = handler.generate(2, cipherHandler, dummySerializable,
				password, images, images);
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
		assertTrue(captcha.getToken().length()>0);
		assertTrue(Stream.of(resizedImages).allMatch(img -> (img.getHeight() == 30 && img.getWidth() == 30)));
	}

	@Test
	public void autoResizeTest30x10() throws IOException {
		BufferedImage smallImage = new BufferedImage(10, 10, BufferedImage.TYPE_3BYTE_BGR);
		BufferedImage largeImage = new BufferedImage(10, 30, BufferedImage.TYPE_3BYTE_BGR);
		BufferedImage[] images = new BufferedImage[] { smallImage, largeImage };
		ImageCaptcha captcha = handler.generate(2, cipherHandler, dummySerializable,
				password, images, images);
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
		assertTrue(captcha.getToken().length()>0);
		assertTrue(Stream.of(resizedImages).allMatch(img -> (img.getHeight() == 30 && img.getWidth() == 10)));
	}

	@Test
	public void autoResizeTest10x30() throws IOException {
		BufferedImage smallImage = new BufferedImage(10, 10, BufferedImage.TYPE_3BYTE_BGR);
		BufferedImage largeImage = new BufferedImage(30, 10, BufferedImage.TYPE_3BYTE_BGR);
		BufferedImage[] images = new BufferedImage[] { smallImage, largeImage };
		ImageCaptcha captcha = handler.generate(2, cipherHandler, dummySerializable,
				password, images, images);
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
		assertTrue(captcha.getToken().length()>0);
		assertTrue(Stream.of(resizedImages).allMatch(img -> (img.getHeight() == 10 && img.getWidth() == 30)));
	}
	
	private CipherHandler getCHMock() {
		CipherHandler cipherHandler = Mockito.mock(CipherHandler.class);
		Mockito.when(cipherHandler.generateIV())
				.thenReturn(new IvParameterSpec(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 }));
		Mockito.when(cipherHandler.decryptString(any(byte[].class), anyString(), any()))
				.thenReturn(new byte[] { 1, 2, 3 });
		Mockito.when(cipherHandler.encryptString(any(byte[].class), anyString(), any(), any(byte[].class)))
				.thenReturn(new byte[] { 1, 2, 3 });
		return cipherHandler;
	}

}
