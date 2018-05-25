package com.sherwin.shercolor.common.dao;

import com.sherwin.shercolor.common.domain.CdsFbProd;

public interface CdsFbProdDao {

	/***
	 * Read a CdsFbColor record for the Product Company, Sales Number and FB Base provided.
	 * @param prodComp - Product Company
	 * @param salesNbr - Sales Number
	 * @param fbBase - Formula Book Base
	 * @return Single CdsFbProd record or null if not found
	 */
	public CdsFbProd read(String prodComp, String salesNbr, String fbBase);
	
}
