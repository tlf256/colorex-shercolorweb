package com.sherwin.shercolor.common.dao;

import java.util.List;

import com.sherwin.shercolor.common.domain.CdsClrntSys;

public interface CdsClrntSysDao {
	
	/***
	 * Read a CdsClrntSys record for the Colorant System Id provided.
	 * @param clrntSysId - Colorant System Id
	 * @return Single CdsClrntSys record or null if not found
	 */
	public CdsClrntSys read(String clrntSysId);
	
	/***
	 * Retrieve a List of CdsClrntSys records.
	 * @param activeOnly - True if you only want active colorant systems, False if you want all colorant systems
	 * @return - List of CdsClrntSys records or Zero sized list if none found
	 */
 	public List<CdsClrntSys> listForClrntSysId(Boolean activeOnly);
}
