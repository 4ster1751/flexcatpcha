package io.github.yaforster.flexcaptcha.textbased.enums;

import lombok.Getter;

/**
 * Enum object representing the three different ways of using lettercases.
 *
 * @author Yannick Forster
 */
@Getter
public enum Case {

    /**
     * Uppercase letters only
     */
    UPPERCASE(0),
    /**
     * Lowercase letters only
     */
    LOWERCASE(1),
    /**
     * Both uppercase and lowercase letters
     */
    MIXEDCASE(2);

    /**
     * A number representing the available cases
     */
    private final int caseNum;

    Case(int caseNum) {
        this.caseNum = caseNum;
    }

}
