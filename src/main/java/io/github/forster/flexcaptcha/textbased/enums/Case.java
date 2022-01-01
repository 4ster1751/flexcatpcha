package io.github.forster.flexcaptcha.textbased.enums;

/**
 * Enum object representing the three different ways of using lettercases.
 * 
 * @author Yannick Forster
 *
 */
public enum Case {

	UPPERCASE(0), LOWERCASE(1), MIXEDCASE(2);

	private int caseNum;

	Case(int caseNum) {
		this.caseNum = caseNum;
	}

	public int getNum() {
		return caseNum;
	}

}
