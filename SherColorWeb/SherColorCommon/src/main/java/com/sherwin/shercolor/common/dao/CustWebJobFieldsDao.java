package com.sherwin.shercolor.common.dao;

import java.util.List;

import com.sherwin.shercolor.common.domain.CustWebJobFields;

public interface CustWebJobFieldsDao {

	/***
	 * Read a CustWebJobFields record for the Customer Id and Sequence Number provided.
	 * @param customerId - Customer Id
	 * @param seqNbr - Sequence Number
	 * @return Single CustWebJobFields record or null if not found
	 */
	public CustWebJobFields read(String customerId, int seqNbr);
	
	/***
	 * Retrieve a List of CustWebJobFields records for the Customer Id provided.
	 * @param customerId - Customer Id
	 * @return - List of CustWebJobFields records or Zero sized list if none found
	 */
	public List<CustWebJobFields> listForCustomerId (String customerId);
	
}
