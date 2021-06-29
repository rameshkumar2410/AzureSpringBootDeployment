package com.db.creditdecision.controller;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.db.creditdecision.bo.CreditScore;
import com.db.creditdecision.service.CreditScoreService;

/**
 * This class is for Credit Score and it act as an External Service.
 * 
 * @author Ramesh
 *
 */
@RestController
public class CreditScoreController {

	@Autowired
	CreditScoreService creditScoreData;

	/**
	 * Service to get the credit score of applied SSN number.
	 * 
	 * @param ssnNumber
	 * @return CreditScore
	 */
	@GetMapping(path = "/getCreditScore/{ssnNumber}", produces = MediaType.APPLICATION_JSON)
	public CreditScore getCreditScore(@PathVariable final int ssnNumber) {

		CreditScore creditScore = creditScoreData.getCreditScoreData().stream()
				.filter(credit -> credit.getSsnNumber() == ssnNumber).findAny().orElse(null);
		if (creditScore != null) {
			return creditScore;
		} else {
			return null;
		}
	}

}
