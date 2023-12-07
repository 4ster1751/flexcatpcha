package io.github.yaforster.flexcaptcha.util;

import lombok.Builder;

import java.security.SecureRandom;
import java.util.stream.IntStream;

/**
 * @author Yannick Forster
 * <p>
 * This class is used to provide logic to generate or accept a String
 * representing the image content and the solution for the Captcha for
 * further processing
 */
public class TextGenerator extends AbstractTextGenerator {

    @Builder
    public TextGenerator(String characterbase) {
        super(characterbase);
    }

    /**
     * Checks the given Case enum and modifies the case of the character based on
     * the enum.
     *
     * @param charTextCase Case enum defining the character to either be lowercase,
     *                     uppercase or random case.
     * @param c            the character
     * @return modified char
     */
    private static char setCase(TextCase charTextCase, char c) {
        return switch (charTextCase) {
            case LOWERCASE:
                yield Character.toLowerCase(c);
            case UPPERCASE:
                yield Character.toUpperCase(c);
            default:
                SecureRandom random = new SecureRandom();
                if (random.nextBoolean()) {
                    yield Character.toUpperCase(c);
                }
                else {
                    yield c;
                }
        };
    }

    /**
     * Returns a single character from the String at a random position in the string
     *
     * @param src source String from which the character is randomly pulled
     * @return character pulled from the String at random point
     */
    private static char pickRandomChar(String src) {
        SecureRandom random = new SecureRandom();
        int index = random.nextInt(src.length());
        return src.charAt(index);
    }

    /**
     * Constructs the output String by repeatedly copying a single character from
     * the characterbase-String at a random point until the specified length is
     * reached. The case-enum controls whether the case of the letters. Mixed
     * case will randomize the case of each character every time it is picked from
     * the source string
     *
     * @param length       the generated string is supposed to have
     * @param charTextCase Case enum with either lower-, upper-, or mixed case.
     * @return randomized String of the specified length consisting of a randomly
     * selected set of characters from the given string
     */
    private String getRandomLetters(int length, TextCase charTextCase) {
        StringBuffer charbuf = new StringBuffer(0);
        IntStream.range(0, length).forEach(i -> {
            appendBufferByRandomCharOfCase(charTextCase, charbuf);
        });
        return charbuf.toString();
    }

    private void appendBufferByRandomCharOfCase(TextCase charTextCase, StringBuffer charbuf) {
        char c = pickRandomCharWithCase(charTextCase);
        charbuf.append(c);
    }

    private char pickRandomCharWithCase(TextCase charTextCase) {
        char randomChar = pickRandomChar(characterbase);
        return setCase(charTextCase, randomChar);
    }

    /**
     * Generates a new randomized String of the specified length consisting of a
     * randomly selected set of characters from the given string, generated as
     * either upper-, lower- or mixed case, depending on the case specified
     *
     * @param length       the generated string is supposed to have
     * @param charTextCase Case enum with either lower-, upper-, or mixed case.
     * @return randomized String of the specified length consisting of a randomly
     * selected set of characters from the given string
     */
    @Override
    public final String generate(int length, TextCase charTextCase) {
        return getRandomLetters(length, charTextCase);
    }

}