package com.sherwin.shercolor.common.dao;

import com.sherwin.shercolor.common.domain.CdsMessage;

public interface CdsMessageDao {
	
	/***
	 * Read a CdsMessage record for the CDS Message Id and Module provided.
	 * @param cdsMessageId - CDS Message Id
	 * @param module - Module
	 * @return Single CdsMessage record or null if not found
	 */
	public CdsMessage read(String cdsMessageId, String module);

}
