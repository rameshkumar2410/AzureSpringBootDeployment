package com.db.creditdecision.bo;

import java.util.Date;

/**
 * 
 * @author Ramesh
 *
 */
public class LoanSanctionDetails {

	private int ssnNumber;
	private int sanctionedAmount;
	private String eligibility;
	private Date sanctionedDate;

	public LoanSanctionDetails() {
		super();
	}

	public LoanSanctionDetails(int ssnNumber, int sanctionedAmount, String eligibility, Date sanctionedDate) {
		super();
		this.ssnNumber = ssnNumber;
		this.sanctionedAmount = sanctionedAmount;
		this.eligibility = eligibility;
		this.sanctionedDate = sanctionedDate;
	}

	public Date getSanctionedDate() {
		return sanctionedDate;
	}

	public void setSanctionedDate(Date sanctionedDate) {
		this.sanctionedDate = sanctionedDate;
	}

	public int getSsnNumber() {
		return ssnNumber;
	}

	public void setSsnNumber(int ssnNumber) {
		this.ssnNumber = ssnNumber;
	}

	public int getSanctionedAmount() {
		return sanctionedAmount;
	}

	public void setSanctionedAmount(int sanctionedAmount) {
		this.sanctionedAmount = sanctionedAmount;
	}

	public String getEligibility() {
		return eligibility;
	}

	public void setEligibility(String eligibility) {
		this.eligibility = eligibility;
	}

}
