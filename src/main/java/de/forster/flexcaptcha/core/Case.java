package de.forster.flexcaptcha.core;

public enum Case {
	
	UPPERCASE(0),
	LOWERCASE(1),
	MIXEDCASE(2);
	
	private int caseNum;

	Case(int caseNum) {
		this.caseNum = caseNum;
	}
	
	public int getNum() {
		return caseNum;
	}

}
