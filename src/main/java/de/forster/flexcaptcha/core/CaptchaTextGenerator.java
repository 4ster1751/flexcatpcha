package de.forster.flexcaptcha.core;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;

/**
 * @author Yannick Forster
 * 
 * This class is used to provide logic to generate or
 * accept a String representing the image content and the solution for
 * the Captcha for further processing
 */
public class CaptchaTextGenerator {
	
	private final String DEFAULT_CHARACTER_BASE = "abcdefghjkmpqrstuvwxy2345689";

	/**
	 * Generates a new randomized String of mixed case letters and numbers of the
	 * given length
	 * 
	 * @param length the generated string is supposed to have
	 * @return randomized String of mixed case letters and numbers of the
	 * given length
	 */
	public String getCaptchaString(int length) {
		return getCaptchaString(length, DEFAULT_CHARACTER_BASE, Case.MIXEDCASE);
	}
	
	/**
	 * Generates a new randomized String of the specified length consisting of a
	 * randomly selected set of characters from the given string
	 * 
	 * @param length        the generated string is supposed to have
	 * @param characterbase String consisting of the set of letters from which the
	 *                      method will randomly pick characters
	 * @return randomized String of the specified length consisting of a
	 * randomly selected set of characters from the given string
	 */
	public String getCaptchaString(int length, String characterbase) {
		return getCaptchaString(length, characterbase, Case.MIXEDCASE);
	}
	
	/**
	 * Generates a new randomized String of letters and numbers of the given length
	 * and specified case
	 * 
	 * @param length   the generated string is supposed to have
	 * @param charCase Case enum with either lower-, upper-, or mixed case.
	 * @return randomized String of letters and numbers of the given length and
	 *         specified case
	 */
	public String getCaptchaString(int length, Case charCase) {
		return getCaptchaString(length, DEFAULT_CHARACTER_BASE, charCase);
	}

	/**
	 * Generates a new randomized String of the specified length consisting of a
	 * randomly selected set of characters from the given string, generated as
	 * either upper-, lower- or mixed case, depending on the case specified
	 * 
	 * @param length        the generated string is supposed to have
	 * @param characterbase String consisting of the set of letters from which the
	 *                      method will randomly pick characters * @param charCase
	 * @param charCase      Case enum with either lower-, upper-, or mixed case.
	 * @return randomized String of the specified length consisting of a randomly
	 *         selected set of characters from the given string
	 */
	public String getCaptchaString(int length, String characterbase, Case charCase) {
		if(StringUtils.isEmpty(characterbase)) {
			throw new IllegalArgumentException("the specified character base from which to draw the captcha characters is empty.");
		}
		return getRandomLetters(length, characterbase, charCase);
	}

	/**
	 * Constructs the output String by repeatedly copying a single character from
	 * the characterbase-String at a random point until the specified length is
	 * reached. The case-enum controls whether or not the case of the letters. Mixed
	 * case will randomize the case of each character every time it is picked from
	 * the source string
	 * 
	 * @param length the generated string is supposed to have
	 * @param characterbase String consisting of the set of letters from which the
	 *                      method will randomly pick characters * @param charCase
	 * @param charCase Case enum with either lower-, upper-, or mixed case.
	 * @return randomized String of the specified length consisting of a randomly
	 *         selected set of characters from the given string
	 */
	private String getRandomLetters(int length, String characterbase, Case charCase) {
		StringBuffer charbuf = new StringBuffer();
		IntStream.range(0, length).forEach(i -> {
			char c = getRandomChar(characterbase);
			c = setCase(charCase, c);
			charbuf.append(c);
		});
		return charbuf.toString();
	}

	/**
	 * Checks the given Case enum and modifies the case of the character based on
	 * the enum.
	 * 
	 * @param charCase Case enum defining the character to either be lowercase,
	 *                 uppercase or random case.
	 * @param c        the character
	 * @return modified char
	 */
	private char setCase(Case charCase, char c) {
		switch(charCase) {
		case LOWERCASE:
			c = Character.toLowerCase(c);
			break;
		case UPPERCASE:
			c = Character.toUpperCase(c);
			break;
		default:
			ThreadLocalRandom random = ThreadLocalRandom.current();
			if(random.nextBoolean()) {
				c = Character.toUpperCase(c);
			}
			break;
		}
		return c;
	}

	/**
	 * Returns a single character from the String at a random position in the string
	 * 
	 * @param src source String from which the character is randomly pulled
	 * @return character pulled from the String at random point
	 */
	private char getRandomChar(String src) {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		int index = random.nextInt(src.length());
		return src.charAt(index);
	}
	
	public String getDEFAULT_CHARACTER_BASE() {
		return DEFAULT_CHARACTER_BASE;
	}

}
