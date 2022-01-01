package io.github.forster.flexcaptcha.textbased.textgen.impl;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;

import io.github.forster.flexcaptcha.textbased.enums.Case;
import io.github.forster.flexcaptcha.textbased.textgen.CaptchaTextGenerator;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Yannick Forster
 * 
 * This class is used to provide logic to generate or
 * accept a String representing the image content and the solution for
 * the Captcha for further processing
 */
@Getter
@Setter
public class SimpleCaptchaTextGenerator implements CaptchaTextGenerator {

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
	@Override
	public String generate(int length, String characterbase, Case charCase) {
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
			char c = pickRandomChar(characterbase);
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
	private char pickRandomChar(String src) {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		int index = random.nextInt(src.length());
		return src.charAt(index);
	}

}
