package io.github.yaforster.flexcaptcha.textbased.textgen;

import io.github.yaforster.flexcaptcha.textbased.enums.Case;

/**
 * Interface for declaring methods used for to generate randomized Strings
 *
 * @author Yannick Forster
 */
public interface CaptchaTextGenerator {

    /**
     * String containing every character that is allowed for the generation of
     * Strings. Excludes some characters by default that may be confusing when
     * rotated in a certain way.
     */
    String DEFAULT_CHARACTER_BASE = "abcdefghjkmpqrstuvwxy2345689";

    /**
     * Generates a new randomized String of mixed case letters and numbers of the
     * given length
     *
     * @param length the generated string is supposed to have
     * @return randomized String of mixed case letters and numbers of the given
     * length
     */
    default String generate(int length) {
        return generate(length, DEFAULT_CHARACTER_BASE, Case.MIXEDCASE);
    }

    /**
     * Generates a new randomized String of the specified length consisting of a
     * randomly selected set of characters from the given string
     *
     * @param length        the generated string is supposed to have
     * @param characterbase String consisting of the set of letters from which the
     *                      method will randomly pick characters
     * @return randomized String of the specified length consisting of a randomly
     * selected set of characters from the given string
     */
    default String generate(int length, String characterbase) {
        return generate(length, characterbase, Case.MIXEDCASE);
    }

    /**
     * Generates a new randomized String of letters and numbers of the given length
     * and specified case
     *
     * @param length   the generated string is supposed to have
     * @param charCase Case enum with either lower-, upper-, or mixed case.
     * @return randomized String of letters and numbers of the given length and
     * specified case
     */
    default String generate(int length, Case charCase) {
        return generate(length, DEFAULT_CHARACTER_BASE, charCase);
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
     * selected set of characters from the given string
     */
    String generate(int length, String characterbase, Case charCase);

}