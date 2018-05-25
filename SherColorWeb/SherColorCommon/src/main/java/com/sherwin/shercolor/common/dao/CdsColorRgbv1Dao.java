package com.sherwin.shercolor.common.dao;

import com.sherwin.shercolor.common.domain.CdsColorRgbv1;

public interface CdsColorRgbv1Dao {

	/***
	 * Read a CdsColorRgbv1 record for the Color Company and Color Id provided.
	 * @param colorComp - Color Company
	 * @param colorId - Color Id
	 * @return Single CdsColorRgbv1 record or null if not found
	 */
	public CdsColorRgbv1 read(String colorComp, String colorId);
}
