package com.sherwin.shercolor.common.dao;

import java.util.List;

import com.sherwin.shercolor.common.domain.CdsClrntIncr;

public interface CdsClrntIncrDao {
	
	/***
	 * Read a CdsClrntIncr record for the Colorant System Id and Increment provided.
	 * @param clrntSysId - Colorant System Id
	 * @param incr - Increment
	 * @return Single CdsClrntIncr record or null if not found
	 */
	public CdsClrntIncr read(String clrntSysId, String incr);
	
	/***
	 * Retrieve a List of CdsClrntIncr records for the Colorant System Id provided.
	 * @param clrntSysId - Colorant System Id
	 * @return - List of CdsClrntIncr records or Zero sized list if none found
	 */
	public List<CdsClrntIncr> listForClrntSys(String clrntSysId);
	

}
