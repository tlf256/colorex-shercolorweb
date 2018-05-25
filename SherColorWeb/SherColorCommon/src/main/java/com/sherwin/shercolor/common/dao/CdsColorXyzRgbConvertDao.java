package com.sherwin.shercolor.common.dao;

import com.sherwin.shercolor.common.domain.CdsColorXyzRgbConvert;

public interface CdsColorXyzRgbConvertDao {
	
	/***
	 * Read a CdsColorXyzRgbvConvert record for the Illuminant and Observer provided.
	 * @param illuminant - Illuminant (e.g. D65, A, F2, etc.)
	 * @param observer - Observer Degree (e.g. 2 or 10)
	 * @return  Single CdsColorXyzRgbvConvert record or null if not found
	 */
	public CdsColorXyzRgbConvert read(String illuminant, int observer);
	

}
