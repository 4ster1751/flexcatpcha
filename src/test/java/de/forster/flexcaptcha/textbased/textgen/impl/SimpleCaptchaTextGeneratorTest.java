package de.forster.flexcaptcha.textbased.textgen.impl;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import de.forster.flexcaptcha.textbased.enums.Case;

/**
 * Tests {@link SimpleCaptchaTextGenerator}
 * 
 * @author Yannick Forster
 *
 */
public class SimpleCaptchaTextGeneratorTest {
	SimpleCaptchaTextGenerator generator;
	static final int TESTSTRINGLENGTH10 = 10;
	static final int TESTSTRINGLENGTH5 = 5;

	@Before
	public void init() {
		generator = new SimpleCaptchaTextGenerator();
	}

	@Test
	public void testGetCaptchaStringInt() {
		String s = generator.generate(TESTSTRINGLENGTH10);
		assertTrue("CaptchaString is not of specified length.", s.length() == TESTSTRINGLENGTH10);
	}

	@Test
	public void testGetCaptchaStringInt5() {
		String s = generator.generate(TESTSTRINGLENGTH5);
		assertTrue("CaptchaString is not of specified length.", s.length() == TESTSTRINGLENGTH5);
	}

	@Test
	public void testGetCaptchaStringIntLowerCase() {
		String s = generator.generate(TESTSTRINGLENGTH10, Case.LOWERCASE);
		assertTrue("CaptchaString is not of specified length.", s.length() == TESTSTRINGLENGTH10);
		assertTrue("CaptchaString is not lowercase", s.equals(s.toLowerCase(Locale.ROOT)));
	}

	@Test
	public void testGetCaptchaStringIntUpperCase() {
		String s = generator.generate(TESTSTRINGLENGTH10, Case.UPPERCASE);
		assertTrue("CaptchaString is not of specified length.", s.length() == TESTSTRINGLENGTH10);
		assertTrue("CaptchaString is not uppercase", s.equals(s.toUpperCase(Locale.ROOT)));
	}

	@Test
	public void testGetCaptchaStringIntStringCaseNumOnly() {
		String s = generator.generate(TESTSTRINGLENGTH10, "123456789");
		assertTrue("CaptchaString is not of specified length.", s.length() == TESTSTRINGLENGTH10);
		assertTrue("CaptchaString is not a number", s.chars().allMatch(i -> Character.isDigit(i)));
	}

	@Test
	public void testGetCaptchaStringIntStringCaseLetterOnly() {
		String s = generator.generate(TESTSTRINGLENGTH10, "abcdefghijklmnopqrstuvwxyz");
		assertTrue("CaptchaString is not of specified length.", s.length() == TESTSTRINGLENGTH10);
		assertTrue("CaptchaString is not a letter", s.chars().allMatch(i -> Character.isLetter(i)));
	}

	@Test
	public void testGetCaptchaStringIntStringCaseNumOnlyMixedCase() {
		String s = generator.generate(TESTSTRINGLENGTH10, "123456789", Case.MIXEDCASE);
		assertTrue("CaptchaString is not of specified length.", s.length() == TESTSTRINGLENGTH10);
		assertTrue("CaptchaString is not a number", s.chars().allMatch(i -> Character.isDigit(i)));
	}

	@Test
	public void testGetCaptchaStringIntStringCaseLetterOnlyMixedCase() {
		String s = generator.generate(TESTSTRINGLENGTH10, "abcdefghijklmnopqrstuvwxyz", Case.MIXEDCASE);
		assertTrue("CaptchaString is not of specified length.", s.length() == TESTSTRINGLENGTH10);
		assertTrue("CaptchaString is not a letter", s.chars().allMatch(i -> Character.isLetter(i)));
	}

	@Test
	public void testGetCaptchaStringMixedStringMixedCase() {
		String s = generator.generate(TESTSTRINGLENGTH10, "abcdefghijklmnopqrstuvwxyz1234567890",
				Case.MIXEDCASE);
		assertTrue("CaptchaString is not of specified length.", s.length() == TESTSTRINGLENGTH10);
		assertTrue("CaptchaString is not a letter or a number",
				s.chars().allMatch(i -> (Character.isLetter(i) || Character.isDigit(i))));
	}

	@Test
	public void testGetCaptchaStringMixedStringUpperCase() {
		String s = generator.generate(TESTSTRINGLENGTH10, "abcdefghijklmnopqrstuvwxyz1234567890",
				Case.UPPERCASE);
		assertTrue("CaptchaString is not of specified length.", s.length() == TESTSTRINGLENGTH10);
		assertTrue("CaptchaString contains character or case that should not appear with the given configuration.",
				s.chars().allMatch(i -> ((Character.isLetter(i) && Character.isUpperCase(i)) || Character.isDigit(i))));
	}

	@Test
	public void testGetCaptchaStringMixedStringLowerCase() {
		String s = generator.generate(TESTSTRINGLENGTH10, "abcdefghijklmnopqrstuvwxyz1234567890",
				Case.LOWERCASE);
		assertTrue("CaptchaString is not of specified length.", s.length() == TESTSTRINGLENGTH10);
		assertTrue("CaptchaString contains character or case that should not appear with the given configuration.",
				s.chars().allMatch(i -> ((Character.isLetter(i) && Character.isLowerCase(i)) || Character.isDigit(i))));
	}

}
