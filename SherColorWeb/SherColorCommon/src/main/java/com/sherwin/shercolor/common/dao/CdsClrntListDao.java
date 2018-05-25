package com.sherwin.shercolor.common.dao;

import java.util.HashMap;

import com.sherwin.shercolor.common.domain.CdsClrntList;

public interface CdsClrntListDao {
	
	/***
	 * Read a CdsClrntList record for the Colorant List Name, Colorant System Id and Tint System Id provided.
	 * @param clrntListId - Colorant List Name
	 * @param clrntSysId - Colorant System Id
	 * @param tintSysId - Tint System Id
	 * @return  Single CdsClrntList record or null if not found
	 */
	public CdsClrntList read(String clrntListId, String clrntSysId, String tintSysId);
	
	/***
	 * Retrieve a Hash Map of CdsClrntList records for the Colorant List and Colorant System Id provided, Key to map is Tint System ID
	 * @param clrntListId - Colorant List Id
	 * @param clrntSysId - Colorant System Id
	 * @return - HashMap<String,CdsClrntList> where key to map is TintSysId or empty HashMap is none found
	 */
	public HashMap<String,CdsClrntList> mapTintSysIdForClrntList(String clrntListId, String clrntSysId);
	
	

}
