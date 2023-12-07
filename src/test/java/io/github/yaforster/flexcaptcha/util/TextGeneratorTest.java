package io.github.yaforster.flexcaptcha.util;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TextGeneratorTest {

    static final int TESTSTRINGLENGTH10 = 10;
    static final int TESTSTRINGLENGTH5 = 5;

    @Test
    public final void testGetCaptchaStringInt() {
        TextGenerator generator = TextGenerator.builder().build();
        String s = generator.generate(TESTSTRINGLENGTH10);
        assertEquals(TESTSTRINGLENGTH10, s.length());
    }

    @Test
    public final void testGetCaptchaStringInt5() {
        TextGenerator generator = TextGenerator.builder().build();
        String s = generator.generate(TESTSTRINGLENGTH5);
        assertEquals(TESTSTRINGLENGTH5, s.length());
    }

    @Test
    public final void testGetCaptchaStringIntLowerCase() {
        TextGenerator generator = TextGenerator.builder().build();
        String s = generator.generate(TESTSTRINGLENGTH10, TextCase.LOWERCASE);
        assertEquals(TESTSTRINGLENGTH10, s.length());
        assertEquals(s, s.toLowerCase(Locale.ROOT));
    }

    @Test
    public final void testGetCaptchaStringIntUpperCase() {
        TextGenerator generator = TextGenerator.builder().build();
        String s = generator.generate(TESTSTRINGLENGTH10, TextCase.UPPERCASE);
        assertEquals(TESTSTRINGLENGTH10, s.length());
        assertEquals(s, s.toUpperCase(Locale.ROOT));
    }

    @Test
    public final void testGetCaptchaStringIntStringCaseNumOnly() {
        TextGenerator generator = TextGenerator.builder().characterbase("1234567890").build();
        String s = generator.generate(TESTSTRINGLENGTH10);
        assertEquals(TESTSTRINGLENGTH10, s.length());
        assertTrue(s.chars().allMatch(Character::isDigit));
    }

    @Test
    public final void testGetCaptchaStringIntStringCaseLetterOnly() {
        TextGenerator generator = TextGenerator.builder().characterbase("abcdefg").build();
        String s = generator.generate(TESTSTRINGLENGTH10);
        assertEquals(TESTSTRINGLENGTH10, s.length());
        assertTrue(s.chars().allMatch(Character::isLetter));
    }

    @Test
    public final void testGetCaptchaStringIntStringCaseNumOnlyMixedCase() {
        TextGenerator generator = TextGenerator.builder().characterbase("1234567890").build();
        String s = generator.generate(TESTSTRINGLENGTH10, TextCase.MIXEDCASE);
        assertEquals(TESTSTRINGLENGTH10, s.length());
        assertTrue(s.chars().allMatch(Character::isDigit));
    }

    @Test
    public final void testGetCaptchaStringIntStringCaseLetterOnlyMixedCase() {
        TextGenerator generator = TextGenerator.builder().characterbase("abcdefg").build();
        String s = generator.generate(TESTSTRINGLENGTH10, TextCase.MIXEDCASE);
        assertEquals(TESTSTRINGLENGTH10, s.length());
        assertTrue(s.chars().allMatch(Character::isLetter));
    }

    @Test
    public final void testGetCaptchaStringMixedStringMixedCase() {
        TextGenerator generator = TextGenerator.builder().characterbase("abcdefg123456").build();
        String s = generator.generate(TESTSTRINGLENGTH10, TextCase.MIXEDCASE);
        assertEquals(TESTSTRINGLENGTH10, s.length());
        assertTrue(s.chars().allMatch(i -> (Character.isLetter(i) || Character.isDigit(i))));
    }

    @Test
    public final void testGetCaptchaStringMixedStringUpperCase() {
        TextGenerator generator = TextGenerator.builder().characterbase("abcdefg123456").build();
        String s = generator.generate(TESTSTRINGLENGTH10, TextCase.UPPERCASE);
        assertEquals(TESTSTRINGLENGTH10, s.length());
        assertTrue(s.chars()
                .allMatch(i -> ((Character.isLetter(i) && Character.isUpperCase(i)) || Character.isDigit(i))));
    }

    @Test
    public final void testGetCaptchaStringMixedStringLowerCase() {
        TextGenerator generator = TextGenerator.builder().characterbase("abcdefg123456").build();
        String s = generator.generate(TESTSTRINGLENGTH10, TextCase.LOWERCASE);
        assertEquals(TESTSTRINGLENGTH10, s.length());
        assertTrue(s.chars()
                .allMatch(i -> ((Character.isLetter(i) && Character.isLowerCase(i)) || Character.isDigit(i))));
    }
}