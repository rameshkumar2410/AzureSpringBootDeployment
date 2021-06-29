package com.db.creditdecision.bo;

/**
 * 
 * @author Ramesh
 *
 */
public class ApplicantDetails {

	private int ssnNumber;
	private int loanAmount;
	private int currentAnnualIncome;

	public int getSsnNumber() {
		return ssnNumber;
	}

	public void setSsnNumber(int ssnNumber) {
		this.ssnNumber = ssnNumber;
	}

	public int getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(int loanAmount) {
		this.loanAmount = loanAmount;
	}

	public int getCurrentAnnualIncome() {
		return currentAnnualIncome;
	}

	public void setCurrentAnnualIncome(int currentAnnualIncome) {
		this.currentAnnualIncome = currentAnnualIncome;
	}

}
