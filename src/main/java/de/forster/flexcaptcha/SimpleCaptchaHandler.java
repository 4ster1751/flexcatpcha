package de.forster.flexcaptcha;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.forster.flexcaptcha.enums.Case;
import de.forster.flexcaptcha.rendering.CaptchaImageRenderer;
import de.forster.flexcaptcha.textgen.CaptchaTextGenerator;

/**
 * Provides basic Captcha handling in regards to generating a simplistic visual
 * representation of the captcha string as well as hashing the token and salt
 * object
 * 
 * @author Yannick Forster
 *
 */
public class SimpleCaptchaHandler implements CaptchaHandler {

	private static final String IMG_FORMAT = "JPEG";
	private static final String ALGORITHM_NAME = "MD5";
	Logger log = LogManager.getLogger(SimpleCaptchaHandler.class);

	/**
	 * Generates a Captcha object containing the token and the images.
	 */
	@Override
	public Captcha generate(int length, Serializable saltSource, CaptchaTextGenerator textgenerator, Case charCase, CaptchaImageRenderer renderer, int height, int width) {
		if(renderer==null) {
			throw new IllegalArgumentException("The renderer cannot be null.");
		}
		if(textgenerator==null) {
			throw new IllegalArgumentException("The text generator cannot be null.");
		}
		if(length<=0) {
			throw new IllegalArgumentException("The length must be an integer larger than 0.");
		}
		if(height<=2) {
			throw new IllegalArgumentException("The height must be an integer larger than 2.");
		}
		if(width<=0) {
			throw new IllegalArgumentException("The width must be an integer larger than 0.");
		}
		String captchaText = textgenerator.generate(length, textgenerator.generate(length, charCase), charCase);
		return makeCaptcha(saltSource, renderer, height, width, captchaText);
	}

	/**
	 * Validates whether or not the answer to the given captcha is correct. To do
	 * this, the answer and the salt source are combined and checked against the
	 * token
	 */
	@Override
	public boolean validate(String answer, String token, Serializable saltSource) {
		return token.equals( makeToken(answer, saltSource));
	}

	/**
	 * Generates the completed captcha object with a picture based on the specified
	 * text directly, The implementation of creating the image is given by the
	 * specified renderer. The salt source specifies an arbitrary object used to
	 * salt the token. Use this method if you want a captcha knowing the solution
	 * beforehand, as opposed to have it randomly generated.
	 * 
	 */
	@Override
	public Captcha toCaptcha(String captchaText, Serializable saltSource, CaptchaImageRenderer renderer, int height, int width) {
		return makeCaptcha(saltSource, renderer, height, width, captchaText);
	}

	/**
	 * Generates the completed captcha object with a picture based on the specified
	 * text, heigth and width. The implementation of creating the image is given by
	 * the specified renderer. The salt source specifies an arbitrary object used to
	 * salt the token.
	 * 
	 * @param saltSource  arbitrary object to be used to salt the solution hash for
	 *                    added security and to allow for authenticating the given
	 *                    answer
	 * @param renderer    Implementation of the ImageRenderer interface controlling
	 *                    how the specified captcha is generated as an image.
	 * @param height      pixel height of the captcha image
	 * @param width       pixel width of the catpcha image
	 * @param captchaText text the catpcha should display
	 * @return Captcha object containing
	 */
	private Captcha makeCaptcha(Serializable saltSource, CaptchaImageRenderer renderer, int height, int width, String captchaText) {
		BufferedImage image = renderer.render(captchaText, height, width);
		try {
			byte[] imgData = convertImageToString(image);
			if (imgData!=null) {
				String token = makeToken(captchaText, saltSource);
				Captcha captcha = new Captcha(imgData, token);
				return captcha;
			}
		} catch (IOException e) {
			log.error("Error creating the captcha object: " + e.getMessage());
			return null;
		}
		return null;
	}

	/**
	 * Converts a buffered image to a base64 String
	 * 
	 * @param image Image of the captcha
	 * @param captchaText 
	 * @return base64 String of the image
	 * @throws IOException if writing the base64 string encountered an error
	 */
	private byte[] convertImageToString(BufferedImage image) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, IMG_FORMAT, bos);
			return bos.toByteArray();
		} catch (final IOException e) {
			log.error("Error converting the BufferedImage to byte array: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Creates the token based on the captcha solution and the object to be used for
	 * salting
	 * 
	 * @param captchaText captcha solution as string
	 * @param saltSource  arbitrary object to be used to salt the solution hash for
	 *                    added security and to allow for authenticating the given
	 *                    answer
	 * @return String of the token
	 */
	private String makeToken(String captchaText, Serializable saltSource) {
		byte[] captchaTextBytes = captchaText.getBytes();
		byte[] saltObjectBytes = getSaltObjectBytes(saltSource);
		try {
			MessageDigest md = MessageDigest.getInstance(ALGORITHM_NAME);
			md.update(captchaTextBytes);
			md.update(saltObjectBytes);
			byte[] outputBytes = md.digest();
			return Base64.getEncoder().encodeToString(outputBytes);
		} catch (NoSuchAlgorithmException e) {
			log.error("Error creating the captcha token: " + e.getMessage());
			return null;
		}

	}

	/**
	 * Converts the given object to a byte array
	 * 
	 * @param saltSource object to be used as salt
	 * @return byte array of the object
	 */
	private byte[] getSaltObjectBytes(Serializable saltSource) {
		ByteArrayOutputStream baos = null;
		ObjectOutputStream oos = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(saltSource);
			baos.close();
			oos.close();
			return baos.toByteArray();
		} catch (IOException e) {
			log.error("Error converting the salt object source to byte array: " + e.getMessage());
			return null;
		}
	}

}
