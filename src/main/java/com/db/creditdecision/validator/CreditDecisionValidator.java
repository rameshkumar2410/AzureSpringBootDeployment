package com.db.creditdecision.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.db.creditdecision.bo.ApplicantDetails;
import com.db.creditdecision.service.CreditScoreService;

/**
 * class for Validating the Credit Decision methods
 * 
 * @author murtrame
 *
 */
@Component
public class CreditDecisionValidator {

	@Autowired
	CreditScoreService creditScoreData;

	/**
	 * 
	 * @param applicantDetails
	 * @return
	 */
	public boolean validateApplicantDetails(ApplicantDetails applicantDetails) {
		boolean flag = false;
		if (null == applicantDetails) {
			flag = false;
		} else {
			if (applicantDetails.getSsnNumber() != 0 && applicantDetails.getLoanAmount() != 0
					&& applicantDetails.getCurrentAnnualIncome() != 0) {
				flag = true;
			}
		}

		return flag;
	}

	/**
	 * validateLoanSanctionHistory
	 * 
	 * @param applicantDetails
	 * @return true if SSN number already applied loan
	 */
	public boolean validateLoanSanctionHistory(ApplicantDetails applicantDetails) {
		boolean flag = false;
		if (creditScoreData.getLoanSanctionedSSNNumber().stream()
				.anyMatch(ssn -> ssn.equals(applicantDetails.getSsnNumber()))) {
			flag = true;
		}
		return flag;
	}

}
