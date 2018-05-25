package com.sherwin.shercolor.common.dao;

import com.sherwin.shercolor.common.domain.CdsFbColor;

public interface CdsFbColorDao {

	/***
	 * Read a CdsFbColor record for the Color Company, Color Id, Colorant System and FB Base provided.
	 * @param colorComp - Color Company
	 * @param colorId - Color Id
	 * @param clrntSysId - Colorant System Id
	 * @param fbBase - Formula Book Base
	 * @return Single CdsFbColor record or null if not found
	 */
	public CdsFbColor read(String colorComp, String colorId, String clrntSysId, String fbBase);
	
}
