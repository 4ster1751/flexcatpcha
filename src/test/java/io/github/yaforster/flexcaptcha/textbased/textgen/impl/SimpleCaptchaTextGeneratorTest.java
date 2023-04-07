package io.github.yaforster.flexcaptcha.textbased.textgen.impl;

import io.github.yaforster.flexcaptcha.textbased.enums.Case;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link SimpleCaptchaTextGenerator}
 *
 * @author Yannick Forster
 */
public class SimpleCaptchaTextGeneratorTest {
    static final int TESTSTRINGLENGTH10 = 10;
    static final int TESTSTRINGLENGTH5 = 5;
    SimpleCaptchaTextGenerator generator;

    @Before
    public void init() {
        generator = new SimpleCaptchaTextGenerator();
    }

    @Test
    public void testGetCaptchaStringInt() {
        String s = generator.generate(TESTSTRINGLENGTH10);
        assertEquals("CaptchaString is not of specified length.", TESTSTRINGLENGTH10, s.length());
    }

    @Test
    public void testGetCaptchaStringInt5() {
        String s = generator.generate(TESTSTRINGLENGTH5);
        assertEquals("CaptchaString is not of specified length.", TESTSTRINGLENGTH5, s.length());
    }

    @Test
    public void testGetCaptchaStringIntLowerCase() {
        String s = generator.generate(TESTSTRINGLENGTH10, Case.LOWERCASE);
        assertEquals("CaptchaString is not of specified length.", TESTSTRINGLENGTH10, s.length());
        assertEquals("CaptchaString is not lowercase", s, s.toLowerCase(Locale.ROOT));
    }

    @Test
    public void testGetCaptchaStringIntUpperCase() {
        String s = generator.generate(TESTSTRINGLENGTH10, Case.UPPERCASE);
        assertEquals("CaptchaString is not of specified length.", TESTSTRINGLENGTH10, s.length());
        assertEquals("CaptchaString is not uppercase", s, s.toUpperCase(Locale.ROOT));
    }

    @Test
    public void testGetCaptchaStringIntStringCaseNumOnly() {
        String s = generator.generate(TESTSTRINGLENGTH10, "123456789");
        assertEquals("CaptchaString is not of specified length.", TESTSTRINGLENGTH10, s.length());
        assertTrue("CaptchaString is not a number", s.chars().allMatch(Character::isDigit));
    }

    @Test
    public void testGetCaptchaStringIntStringCaseLetterOnly() {
        String s = generator.generate(TESTSTRINGLENGTH10, "abcdefghijklmnopqrstuvwxyz");
        assertEquals("CaptchaString is not of specified length.", TESTSTRINGLENGTH10, s.length());
        assertTrue("CaptchaString is not a letter", s.chars().allMatch(Character::isLetter));
    }

    @Test
    public void testGetCaptchaStringIntStringCaseNumOnlyMixedCase() {
        String s = generator.generate(TESTSTRINGLENGTH10, "123456789", Case.MIXEDCASE);
        assertEquals("CaptchaString is not of specified length.", TESTSTRINGLENGTH10, s.length());
        assertTrue("CaptchaString is not a number", s.chars().allMatch(Character::isDigit));
    }

    @Test
    public void testGetCaptchaStringIntStringCaseLetterOnlyMixedCase() {
        String s = generator.generate(TESTSTRINGLENGTH10, "abcdefghijklmnopqrstuvwxyz", Case.MIXEDCASE);
        assertEquals("CaptchaString is not of specified length.", TESTSTRINGLENGTH10, s.length());
        assertTrue("CaptchaString is not a letter", s.chars().allMatch(Character::isLetter));
    }

    @Test
    public void testGetCaptchaStringMixedStringMixedCase() {
        String s = generator.generate(TESTSTRINGLENGTH10, "abcdefghijklmnopqrstuvwxyz1234567890",
                Case.MIXEDCASE);
        assertEquals("CaptchaString is not of specified length.", TESTSTRINGLENGTH10, s.length());
        assertTrue("CaptchaString is not a letter or a number",
                s.chars().allMatch(i -> (Character.isLetter(i) || Character.isDigit(i))));
    }

    @Test
    public void testGetCaptchaStringMixedStringUpperCase() {
        String s = generator.generate(TESTSTRINGLENGTH10, "abcdefghijklmnopqrstuvwxyz1234567890",
                Case.UPPERCASE);
        assertEquals("CaptchaString is not of specified length.", TESTSTRINGLENGTH10, s.length());
        assertTrue("CaptchaString contains character or case that should not appear with the given configuration.",
                s.chars().allMatch(i -> ((Character.isLetter(i) && Character.isUpperCase(i)) || Character.isDigit(i))));
    }

    @Test
    public void testGetCaptchaStringMixedStringLowerCase() {
        String s = generator.generate(TESTSTRINGLENGTH10, "abcdefghijklmnopqrstuvwxyz1234567890",
                Case.LOWERCASE);
        assertEquals("CaptchaString is not of specified length.", TESTSTRINGLENGTH10, s.length());
        assertTrue("CaptchaString contains character or case that should not appear with the given configuration.",
                s.chars().allMatch(i -> ((Character.isLetter(i) && Character.isLowerCase(i)) || Character.isDigit(i))));
    }

}
