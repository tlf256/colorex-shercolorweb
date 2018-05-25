package com.sherwin.shercolor.common.dao;


import com.sherwin.shercolor.common.domain.CdsClrntCost;

public interface CdsClrntCostDao {

	/***
	 * Read a CdsClrntCost record for the Colorant System Id and Tint System Id provided.
	 * @param clrntSysId - Colorant System Id
	 * @param tintSysId - Tint System Id
	 * @return Single CdsClrntCost record or null if not found
	 */
	public CdsClrntCost read(String clrntSysId, String tintSysId);
}
