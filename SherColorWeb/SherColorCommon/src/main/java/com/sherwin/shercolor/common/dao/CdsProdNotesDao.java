package com.sherwin.shercolor.common.dao;

import java.util.List;

import com.sherwin.shercolor.common.domain.CdsProdNotes;

public interface CdsProdNotesDao {

	/***
	 * Read a CdsProdNotes record for the Sales Number and Sequence Number provided.
	 * @param salesNbr - Sales Number
	 * @param seqNbr - Sequence Number
	 * @return Single CdsProdNotes record or null if not found
	 */
	public CdsProdNotes read(String salesNbr, int seqNbr);
	
	/***
	 * Retrieve a list of CdsProdNotes for the Sales Number provided.
	 * @param salesNbr - Sales Number
	 * @return - List of CdsProdNotes records or Zero sized list if none found
	 */
	public List<CdsProdNotes> listForSalesNbr(String salesNbr);
	
}
