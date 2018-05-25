package com.sherwin.shercolor.common.dao;

import java.util.HashMap;
import java.util.List;

import com.sherwin.shercolor.common.domain.CdsClrnt;

public interface CdsClrntDao {

	/***
	 * Read a CdsClrnt record for the Colorant System Id and Tint System Id provided.
	 * @param clrntSysId - Colorant System Id
	 * @param tintSysId - Tint System Id
	 * @return Single CdsClrnt record or null if not found
	 */
	public CdsClrnt read(String clrntSysId, String tintSysId);
	
	/***
	 * Read a CdsClrnt record for the Colorant System Id and Formula Book System Id provided.
	 * @param clrntSysId - Colorant System Id
	 * @param fbSysId - Formula Book System Id
	 * @return Single CdsClrnt record or null if not found
	 */
	public CdsClrnt readByFbSysId(String clrntSysId, String fbSysId);
	
	/***
	 * Retrieve a List of CdsClrnt records for the Colorant System Id provided.
	 * @param clrntSysId - Colorant System Id
	 * @param activeOnly - True if you only want active colorants, False if you want all colorants
	 * @return - List of CdsClrnt records or Zero sized list if none found
	 */
	public List<CdsClrnt> listForClrntSys(String clrntSysId, Boolean activeOnly);
	
	/***
	 * Retrieve a Hash Map of CdsClrnt records for the Colorant System Id provided, Key to map is Tint System ID
	 * @param clrntSysId - Colorant System Id
	 * @param activeOnly - True if you only want active colorants, False if you want all colorants
	 * @return - HashMap<String,CdsClrnt> where key to map is TintSysId or empty HashMap is none found
	 */
	public HashMap<String,CdsClrnt> mapTintSysIdForClrntSys(String clrntSysId, Boolean activeOnly);
	
}
