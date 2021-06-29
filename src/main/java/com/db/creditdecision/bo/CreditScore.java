package com.db.creditdecision.bo;

/**
 * 
 * @author Ramesh
 *
 */
public class CreditScore {

	private int ssnNumber;
	private int creditScore;

	public CreditScore() {
		super();
	}

	public CreditScore(int ssnNumber, int creditScore) {
		super();
		this.ssnNumber = ssnNumber;
		this.creditScore = creditScore;
	}

	public int getSsnNumber() {
		return ssnNumber;
	}

	public void setSsnNumber(int ssnNumber) {
		this.ssnNumber = ssnNumber;
	}

	public int getCreditScore() {
		return creditScore;
	}

	public void setCreditScore(int creditScore) {
		this.creditScore = creditScore;
	}

}
