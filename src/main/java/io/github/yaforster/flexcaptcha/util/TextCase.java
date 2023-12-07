package io.github.yaforster.flexcaptcha.util;

import lombok.Getter;

/**
 * Enum object representing the three different ways of using lettercases.
 *
 * @author Yannick Forster
 */
@Getter
public enum TextCase {

    /**
     * Uppercase letters only
     */
    UPPERCASE,
    /**
     * Lowercase letters only
     */
    LOWERCASE,
    /**
     * Both uppercase and lowercase letters
     */
    MIXEDCASE

}