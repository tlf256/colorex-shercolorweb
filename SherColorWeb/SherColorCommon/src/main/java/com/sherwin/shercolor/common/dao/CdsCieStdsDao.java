package com.sherwin.shercolor.common.dao;

import com.sherwin.shercolor.common.domain.CdsCieStds;

public interface CdsCieStdsDao {

	/***
	 * Read a CdsCieStds record for the Illumination Code provided.
	 * @param illumCode - Illumination Code
	 * @return Single CdsCieStds record or null if not found
	 */
	public CdsCieStds read(String illumCode);
}
