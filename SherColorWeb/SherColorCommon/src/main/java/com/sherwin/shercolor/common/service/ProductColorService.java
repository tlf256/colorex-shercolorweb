package com.sherwin.shercolor.common.service;

import java.util.List;

import com.sherwin.shercolor.util.domain.SwMessage;

public interface ProductColorService {

	/***
	 * Validates Color and Product combination
	 * @param colorComp - Color Company
	 * @param colorId - Color Id
	 * @param salesNbr - Product Sales Number
	 * @return a list of 0 or more SwMessages.  0 indicates successful validation. Messages can be warnings or errors and must be evaluated.
	 */
	public List<SwMessage> validate(String colorComp, String colorId, String salesNbr);
	
	
}
