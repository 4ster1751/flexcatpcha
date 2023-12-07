package io.github.yaforster.flexcaptcha.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Interface for declaring methods used for to generate randomized Strings
 *
 * @author Yannick Forster
 */
public abstract class AbstractTextGenerator {

    /**
     * String containing every character that is allowed for the generation of
     * Strings. Excludes some characters by default that may be confusing when
     * rotated in a certain way.
     */
    protected final static String DEFAULT_CHARACTER_BASE = "abcdefghjkmpqrstuvwxy2345689";
    /**
     * String consisting of the set of letters from which the method will randomly pick characters
     */
    protected final String characterbase;

    protected AbstractTextGenerator(String characterbase) {
        this.characterbase = getCharCaseOrDefault(characterbase);
    }

    protected static String getCharCaseOrDefault(String characterbase) {
        if (characterbase != null && !StringUtils.isBlank(characterbase)) {
            return characterbase;
        }
        else {
            return DEFAULT_CHARACTER_BASE;
        }
    }


    /**
     * Generates a new randomized String of the specified length consisting of a
     * randomly selected set of characters from the given string
     *
     * @param length the generated string is supposed to have
     * @return randomized String of the specified length consisting of a randomly
     * selected set of characters from the given string
     */
    public final String generate(int length) {
        return generate(length, TextCase.MIXEDCASE);
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
    abstract String generate(int length, TextCase charTextCase);

}