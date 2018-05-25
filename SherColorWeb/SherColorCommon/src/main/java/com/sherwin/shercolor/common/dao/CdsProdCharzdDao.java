package com.sherwin.shercolor.common.dao;

import java.util.List;

import com.sherwin.shercolor.common.domain.CdsProdCharzd;

public interface CdsProdCharzdDao {

	/***
	 * Read a CdsProdCharzd record for the Product Number and Colorant System Id provided
	 * @param prodNbr - Product Number
	 * @param clrntSysId - Colorant System Id
	 * @return Single CdsProdCharzd record or null if not found
	 */
	public CdsProdCharzd read(String prodNbr, String clrntSysId);
	
	/***
	 * Retrieve a list of CdsProdCharzd records for the Product Number provided
	 * @param prodNbr - Product Number
	 * @param activeOnly - True if you only want active characterizations, False if you want all characterizations
	 * @return
	 */
	public List<CdsProdCharzd> listForProdNbr(String prodNbr, boolean activeOnly);

}
