package com.sherwin.shercolor.common.validation;

import com.sherwin.shercolor.common.domain.CdsColorMast;

public interface ColorValidator {
	
	/***
	 * Verify color has a entry in the CdsColorMast table.
	 * @param colorComp - Color Company
	 * @param colorId - Color Id
	 * @return - CdsColorMast record or an exception if invalid.
	 */
	public CdsColorMast checkColor(String colorComp, String colorId);
	

}
