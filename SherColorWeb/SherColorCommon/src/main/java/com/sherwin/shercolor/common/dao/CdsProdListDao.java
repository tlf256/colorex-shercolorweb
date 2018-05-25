package com.sherwin.shercolor.common.dao;

import java.util.List;

import com.sherwin.shercolor.common.domain.CdsProdList;

public interface CdsProdListDao {

	/***
	 * Read a CdsProdList record for the Product List Id and Sales Number provided.
	 * @param prodListId - Product List Id
	 * @param salesNbr - Sales Number
	 * @return Single CdsProdList record or null if not found
	 */
	public CdsProdList read(String prodListId, String salesNbr);
	
	/***
	 * Read a List of CdsProdList records for the Sales Number provided.
	 * @param salesNbr - Sales Number
	 * @return list of 0 or more CdsProdList records
	 */
	public List<CdsProdList> getAllExclusions(String salesNbr);
	
}
