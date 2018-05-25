package com.sherwin.shercolor.common.dao;

import java.util.List;

import com.sherwin.shercolor.common.domain.CustWebTinterEvents;

public interface CustWebTinterEventsDao {

	/***
	 * Read a Tinter Event for the provided GUID
	 * @param guid - GUID (record key)
	 * @return - Returns the CustWebTinterEvent or null if not found
	 */
	public CustWebTinterEvents read(String guid);

	/***
	 * Insert a Tinter Event record (this will stamp a GUID on the record provided)
	 * @param tintEvent - Tinter Event record to be written (no GUID should be provided)
	 * @return - Return the GUID record key on success or null if the record could not be inserted.
	 */
	public String create(CustWebTinterEvents tintEvent);
	
	/***
	 * Locate the last successful purge tinter event for the Customer ID and Tinter provided 
	 * @param customerId - Customer ID
	 * @param clrntSysId - Colorant System ID (e.g. "CCE", "BAC", "844", etc).
	 * @param tinterModel - Tinter Model (e.g. "COROB D600", "FM 8000DE", etc).
	 * @param tinterSerialNbr - Tinter Serial Number
	 * @return - Returns a single CustWebTinterEvent record or null if none found.
	 */
	public CustWebTinterEvents findLastPurge(String customerId, String clrntSysId, String tinterModel, String tinterSerialNbr);
	
	/***
	 * Get a list of Tinter Events for the Customer, Tinter and Function provided.
	 * @param customerId - Customer ID
	 * @param clrntSysId - Colorant System ID (e.g. "CCE", "BAC", "844", etc).
	 * @param tinterModel - Tinter Model (e.g. "COROB D600", "FM 8000DE", etc).
	 * @param tinterSerialNbr - Tinter Serial Number
	 * @param function - Tinter Function
	 * @param ascendingOrder - true for ascending (oldest to newest), false for descending order (newest to oldest)
	 * @return - Returns a list of Tinter Events in ascending order or an empty list if none found.
	 */
	public List<CustWebTinterEvents> listForTinterAndFunction(String customerId, String clrntSysId, String tinterModel, String tinterSerialNbr, String function, boolean ascendingOrder);
	

}
