package com.db.creditdecision.controller;

import java.util.Date;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.creditdecision.bo.ApplicantDetails;
import com.db.creditdecision.bo.CreditScore;
import com.db.creditdecision.bo.LoanSanctionDetails;
import com.db.creditdecision.constants.ApplicationConstant;
import com.db.creditdecision.service.CreditScoreService;
import com.db.creditdecision.validator.CreditDecisionValidator;

/**
 * This class is for CreditDecision
 * 
 * @author Ramesh
 *
 */
@RestController
@RequestMapping("/creditdecision")
public class CreditDecisionController {

	private static final Logger LOGGER = Logger.getLogger(CreditDecisionController.class);

	@Autowired
	CreditDecisionValidator creditDecisionValidator;
	
	@Autowired
	CreditScoreService creditScoreService;

	@Value("${app.db.api.getCreditScoreURL}")
	private String getCreditScoreURL;

	/**
	 * Method to calculate the Loan amount for the SSN Number
	 * 
	 * @param applicantDetails
	 * @return ResponseEntity of LoanSanctioned Details
	 */
	@PostMapping(path = "/calculateLoanAmount/", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<LoanSanctionDetails> calculateLoanAmount(@RequestBody final ApplicantDetails applicantDetails) {
		CreditScore creditscore = null;
		int sanctionedLoanAmount = 0;
		ResponseEntity<LoanSanctionDetails> responseEntity = null;
		LoanSanctionDetails loanSanctionDetails = new LoanSanctionDetails();
		try {
			if (creditDecisionValidator.validateApplicantDetails(applicantDetails)) {
				if (!creditDecisionValidator.validateLoanSanctionHistory(applicantDetails)) {
					creditscore = creditScoreService.getCreditScore(applicantDetails);
					if (creditscore != null) {
						loanSanctionDetails.setSsnNumber(creditscore.getSsnNumber());
						if (creditscore.getCreditScore() > 700) {
							sanctionedLoanAmount = applicantDetails.getCurrentAnnualIncome() / 2;

							loanSanctionDetails.setEligibility(ApplicationConstant.ELIGIBLE);
							if (applicantDetails.getLoanAmount() <= sanctionedLoanAmount) {
								loanSanctionDetails.setSanctionedAmount(applicantDetails.getLoanAmount());
							} else {
								loanSanctionDetails.setSanctionedAmount(sanctionedLoanAmount);
							}
							loanSanctionDetails.setSanctionedDate(new Date());
							responseEntity = new ResponseEntity<LoanSanctionDetails>(loanSanctionDetails,
									HttpStatus.OK);
						} else {

							loanSanctionDetails.setEligibility(ApplicationConstant.NOT_ELIGIBLE);
							loanSanctionDetails.setSanctionedAmount(sanctionedLoanAmount);
							responseEntity = new ResponseEntity<LoanSanctionDetails>(loanSanctionDetails,
									HttpStatus.OK);
						}
					} else {
						loanSanctionDetails.setEligibility(ApplicationConstant.SSN_NOT_FOUND);
						responseEntity = new ResponseEntity<LoanSanctionDetails>(loanSanctionDetails, HttpStatus.NOT_FOUND);

					}

				} else {
					loanSanctionDetails.setSsnNumber(applicantDetails.getSsnNumber());
					loanSanctionDetails.setEligibility(ApplicationConstant.LOAN_SANCTIONED);
					responseEntity = new ResponseEntity<LoanSanctionDetails>(loanSanctionDetails, HttpStatus.OK);
				}

			} else {
				loanSanctionDetails.setEligibility(ApplicationConstant.INVALID_DATA);
				responseEntity = new ResponseEntity<LoanSanctionDetails>(loanSanctionDetails, HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			LOGGER.error("Exception Occured inside calculateLoanAmount : " + e.getMessage());
			loanSanctionDetails.setEligibility(ApplicationConstant.EXCEPTION);
			responseEntity = new ResponseEntity<LoanSanctionDetails>(loanSanctionDetails, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return responseEntity;

	}

}
