package com.sherwin.shercolor.common.dao;

import java.util.List;

import com.sherwin.shercolor.common.domain.CdsMiscCodes;

public interface CdsMiscCodesDao {

	/***
	 * Read a CdsMiscCodes record for the Miscellaneous Type and Miscellaneous Code provided.
	 * @param miscType - Miscellaneous Type
	 * @param miscCode - Miscellaneous Code
	 * @return Single CdsMiscCodes record or null if not found
	 */
	public CdsMiscCodes read(String miscType, String miscCode);
	
	/***
	 * Retrieve a list of CdsMiscCodes for the Miscellaneous Type provided.
	 * @param miscType - Miscellaneous Type
	 * @return - List of CdsMiscCodes records or Zero sized list if none found
	 */
	public List<CdsMiscCodes> listForType(String miscType);
	
}
