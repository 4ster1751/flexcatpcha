package de.forster.flexcaptcha.imgbased.handling;

import java.awt.Button;
import java.awt.image.BufferedImage;

import de.forster.flexcaptcha.imgbased.handling.impl.SimpleImageCaptchaHandler;
import lombok.Getter;

/**
 * Wraps some objects to be used as arguments in tests for
 * {@link ImageCaptchaHandler} and its implementations.
 * 
 * @author Yannick Forster
 *
 */
@Getter
public class ImageCaptchaHandlerTestHelper {

	ImageCaptchaHandler handler = new SimpleImageCaptchaHandler();
	BufferedImage[] dummyArr = new BufferedImage[] { new BufferedImage(10, 10, BufferedImage.TYPE_3BYTE_BGR) };
	BufferedImage[] dummyArr2 = new BufferedImage[] { new BufferedImage(15, 15, BufferedImage.TYPE_4BYTE_ABGR) };
	Button dummySerializable = new Button();
	String password = "ThisIsMyPassword!";

}
