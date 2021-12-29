package de.forster.flexcaptcha;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.NotDirectoryException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;

import com.jhlabs.image.AbstractBufferedImageOp;
import com.jhlabs.image.DitherFilter;
import com.jhlabs.image.MarbleFilter;
import com.jhlabs.image.RippleFilter;
import com.jhlabs.image.TwirlFilter;

import de.forster.flexcaptcha.imgbased.ImageCaptcha;
import de.forster.flexcaptcha.imgbased.handling.ImageCaptchaHandler;
import de.forster.flexcaptcha.imgbased.handling.impl.ImageLoader;
import de.forster.flexcaptcha.imgbased.handling.impl.SimpleImageCaptchaHandler;
import de.forster.flexcaptcha.textbased.TextCaptcha;
import de.forster.flexcaptcha.textbased.enums.Case;
import de.forster.flexcaptcha.textbased.handling.TextCaptchaHandler;
import de.forster.flexcaptcha.textbased.handling.impl.SimpleTextCaptchaHandler;
import de.forster.flexcaptcha.textbased.rendering.impl.EffectChainTextImageRenderer;
import de.forster.flexcaptcha.textbased.rendering.impl.SimpleTextImageRenderer;
import de.forster.flexcaptcha.textbased.rendering.impl.TwirledTextImageRenderer;
import de.forster.flexcaptcha.textbased.textgen.impl.SimpleCaptchaTextGenerator;

public class Demo {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = 3;
		String password = "ThisIsMyPassword!";
	    for (int i=1; i<=n; i++) {
			generateSimpleCaptcha(scanner, password);
			generateDistortedCaptcha(scanner, password);
			generateChainedEffectCaptcha(scanner, password);
			generateImageCaptcha(scanner, password);
		}
		scanner.close();
	}

	private static void generateImageCaptcha(Scanner scanner, String password) {
		ImageCaptchaHandler handler = new SimpleImageCaptchaHandler();
		ImageLoader loader = new ImageLoader();
		try {
			BufferedImage[] solutionImages = loader.getImagesfromPath("C:\\Entwicklung\\Data\\Flexcaptcha\\Img\\Solution");
			BufferedImage[] fillImages = loader.getImagesfromPath("C:\\Entwicklung\\Data\\Flexcaptcha\\Img\\Fill");
			String saltSource = "Hello World!";
			int gridWidth = 3;
			ImageCaptcha captcha = handler.generate(gridWidth, saltSource, password, solutionImages, fillImages);
			JFrame frame = renderImgCaptchaToFrame(captcha);
			System.out.println("Lösung eingeben:");
			getUserInput(saltSource, password, captcha, scanner);
			frame.dispose();
		} catch (NotDirectoryException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void generateSimpleCaptcha(Scanner scanner, String password) {
		try {
			SimpleCaptchaTextGenerator generator = new SimpleCaptchaTextGenerator();
			String s = generator.generate(10, Case.UPPERCASE);
			TextCaptchaHandler handler = new SimpleTextCaptchaHandler();
			String saltSource = "Hello World!";
			TextCaptcha captcha = handler.toCaptcha(s, saltSource, password, new SimpleTextImageRenderer(), 100, 300);
			displayTextCaptcha(scanner, saltSource, password, captcha);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void generateDistortedCaptcha(Scanner scanner, String password) {
		try {
			SimpleCaptchaTextGenerator generator = new SimpleCaptchaTextGenerator();
			String s = generator.generate(10, Case.LOWERCASE);
			TextCaptchaHandler handler = new SimpleTextCaptchaHandler();
			String saltSource = "Hello World!";
			TextCaptcha captcha = handler.toCaptcha(s, saltSource, password, new TwirledTextImageRenderer(), 100, 300);
			displayTextCaptcha(scanner, saltSource, password, captcha);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void generateChainedEffectCaptcha(Scanner scanner, String password) {
		try {
			SimpleCaptchaTextGenerator generator = new SimpleCaptchaTextGenerator();
			String s = generator.generate(10, Case.UPPERCASE);
			TextCaptchaHandler handler = new SimpleTextCaptchaHandler();
			String saltSource = "Hello World!";
			EffectChainTextImageRenderer renderer = new EffectChainTextImageRenderer();
			ArrayList<AbstractBufferedImageOp> filters = new ArrayList<AbstractBufferedImageOp>();
			Color[] textCols = new Color[] {Color.blue, Color.darkGray, Color.black};
			renderer.setBufferedOps(filters);
			renderer.setTextCols(textCols);
			filters.add(new TwirlFilter());
			filters.add(new RippleFilter());
			filters.add(new DitherFilter());
			MarbleFilter marbleFilter = new MarbleFilter();
			marbleFilter.setXScale(2f);
			marbleFilter.setYScale(2f);
			filters.add(marbleFilter);
			TextCaptcha captcha = handler.toCaptcha(s, saltSource, password, renderer, 100, 300);
			displayTextCaptcha(scanner, saltSource, password, captcha);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void displayTextCaptcha(Scanner scanner, String saltSource, String password, TextCaptcha captcha) throws IOException {
		System.out.println("Lösung eingeben:");
		JFrame frame = renderTextCaptchaToFrame(captcha);
		getUserInput(saltSource, password, captcha, scanner);
		frame.dispose();
	}
	
	private static JFrame renderImgCaptchaToFrame(ImageCaptcha captcha) throws IOException {
		JFrame frame = new JFrame();
		frame.setTitle("All indices with cars?");
		frame.setAlwaysOnTop (true);
		frame.setSize(768, 768);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLayout(new GridLayout(3,3));
		Stream.of(captcha.getImgData()).forEach(data -> {
			try {
				InputStream myInputStream = new ByteArrayInputStream(data);
				BufferedImage img =  ImageIO.read(myInputStream);
				JLabel label = new JLabel();
				label.setIcon(new ImageIcon(img));
				frame.getContentPane().add(label);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);
		return frame;
	}
	
	private static JFrame renderTextCaptchaToFrame(TextCaptcha captcha) throws IOException {
		InputStream myInputStream = new ByteArrayInputStream(captcha.getImgData());
		BufferedImage image = ImageIO.read(myInputStream);
		JFrame frame = new JFrame();
		frame.setTitle("Rendered Image");
		frame.setAlwaysOnTop (true);
		frame.setSize(image.getWidth(), image.getHeight());
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		JLabel label = new JLabel();
		label.setIcon(new ImageIcon(image));
		frame.getContentPane().add(label, BorderLayout.CENTER);
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);
		return frame;
	}

	private static void getUserInput(Serializable saltSource, String password, Captcha captcha, Scanner scanner) {
		String input = scanner.next();
		Validator validator = new Validator();
		CipherHandler ch = new CipherHandler();
		Boolean correct = validator.validateInput(input, captcha.getToken(), saltSource, password, ch);
		if(correct==null) {
			System.out.println("Es konnte keine Validierung durchgeführt werden.");
		}
		System.out.println(correct ? "Korrekt" : "Falsch");
		
	}



}
