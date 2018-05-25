package com.sherwin.shercolor.common.dao;

import java.util.List;

import com.sherwin.shercolor.common.domain.CustWebParms;

public interface CustWebParmsDao {
	
	/***
	 * Read a CustWebParms record for the Customer Id and Sequence Number provided.
	 * @param customerId - Customer Id
	 * @param SeqNbr - Sequence Number
	 * @return Single CustWebParms record or null if not found
	 */
	public CustWebParms read(String customerId, int SeqNbr);
	
	/***
	 * Read the default CustWebParms record for the Customer Id provided.
	 * @param customerId - Customer Id
	 * @return Single CustWebParms record or null if not found
	 */
	public CustWebParms readByCustId(String customerId);
	
	/***
	 * Read all CustWebParms records for the Customer Id provided.
	 * @param customerId - Customer Id
	 * @return List of CustWebParms records or null if not found
	 */
	public List<CustWebParms> readAllByCustId(String customerId);
	
	

}
