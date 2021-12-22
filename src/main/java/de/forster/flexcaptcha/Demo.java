package de.forster.flexcaptcha;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

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

import de.forster.flexcaptcha.enums.Case;
import de.forster.flexcaptcha.handling.CaptchaHandler;
import de.forster.flexcaptcha.handling.impl.SimpleCaptchaHandler;
import de.forster.flexcaptcha.rendering.impl.EffectChainRenderer;
import de.forster.flexcaptcha.rendering.impl.SimpleImageRenderer;
import de.forster.flexcaptcha.rendering.impl.TwirledImageRenderer;
import de.forster.flexcaptcha.textgen.impl.SimpleCaptchaTextGenerator;

public class Demo {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = 3;
	    for (int i=1; i<=n; i++) {
			generateSimpleCaptcha(scanner);
			generateDistortedCaptcha(scanner);
			generateChainedEffectCaptcha(scanner);
		}
		scanner.close();
	}

	private static void generateSimpleCaptcha(Scanner scanner) {
		try {
			SimpleCaptchaTextGenerator generator = new SimpleCaptchaTextGenerator();
			String s = generator.generate(10, Case.UPPERCASE);
			CaptchaHandler handler = new SimpleCaptchaHandler();
			String saltSource = "Hello World!";
			Captcha captcha = handler.toCaptcha(s, saltSource, new SimpleImageRenderer(), 100, 300);
			display(scanner, saltSource, captcha);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void generateDistortedCaptcha(Scanner scanner) {
		try {
			SimpleCaptchaTextGenerator generator = new SimpleCaptchaTextGenerator();
			String s = generator.generate(10, Case.LOWERCASE);
			CaptchaHandler handler = new SimpleCaptchaHandler();
			String saltSource = "Hello World!";
			Captcha captcha = handler.toCaptcha(s, saltSource, new TwirledImageRenderer(), 100, 300);
			display(scanner, saltSource, captcha);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void generateChainedEffectCaptcha(Scanner scanner) {
		try {
			SimpleCaptchaTextGenerator generator = new SimpleCaptchaTextGenerator();
			String s = generator.generate(10, Case.UPPERCASE);
			CaptchaHandler handler = new SimpleCaptchaHandler();
			String saltSource = "Hello World!";
			EffectChainRenderer renderer = new EffectChainRenderer();
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
			Captcha captcha = handler.toCaptcha(s, saltSource, renderer, 100, 300);
			display(scanner, saltSource, captcha);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	

	private static void display(Scanner scanner, String saltSource, Captcha captcha) throws IOException {
		System.out.println("Lösung eingeben:");
		JFrame frame = renderToFrame(captcha);
		getUserInput(saltSource, captcha, scanner, frame);
	}
	
	private static JFrame renderToFrame(Captcha captcha) throws IOException {
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

	private static void getUserInput(Serializable saltSource, Captcha captcha, Scanner scanner, JFrame frame) {
		String input = scanner.next();
		Validator validator = new Validator();
		CipherHandler ch = new CipherHandler();
		Boolean correct = validator.validateInput(input, captcha.getToken(), saltSource, CaptchaHandler.password, ch);
		frame.dispose();
		if(correct==null) {
			System.out.println("Es konnte keine Validierung durchgeführt werden.");
		}
		System.out.println(correct ? "Korrekt" : "Falsch");
		
	}



}
