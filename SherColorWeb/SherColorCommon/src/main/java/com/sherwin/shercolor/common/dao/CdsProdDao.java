package com.sherwin.shercolor.common.dao;

import java.util.List;

import com.sherwin.shercolor.common.domain.CdsProd;

public interface CdsProdDao {

	/***
	 * Read a CdsProd record for the Sales Number provided.
	 * @param salesNbr - Sales Number
	 * @return Single CdsProd record or null if not found
	 */
	public CdsProd read(String salesNbr);
	
	/***
	 * Retrieve a list of CdsProd records for the Base Type provided.
	 * @param base - Base Type
	 * @return - List of CdsProd records or Zero sized list if none found
	 */
	public List<CdsProd> listForBaseType(String base);
	
	/***
	 * Retrieve a list of CdsProd sales number keys for autocomplete.
	 */
	public List<CdsProd> listForAutocompleteProduct(String partialProductId);

	/***
	 * Retrieve a list of CdsProd sales number keys for autocomplete.
	 */
	public List<CdsProd> listForAutocompleteProductActive(String partialProductId);
}
