package com.sherwin.shercolor.common.dao;

import java.util.List;

import com.sherwin.shercolor.common.domain.PosProd;

public interface PosProdDao {
	
	/***
	 * Read a PosProd record for the Sales Number provided.
	 * @param salesNbr - Sales Number
	 * @return - Single PosProd record or null if not found
	 */
	public PosProd read(String salesNbr);
	
	/***
	 * Read a PosProd record for the Product Number and Size Code provided.
	 * @param prodNbr - Product Number
	 * @param szCd - Size Code
	 * @return - Single PosProd record or null if not found
	 */
	public PosProd readByProdNbrSzCd(String prodNbr, String szCd);
	
	/***
	 * Read a PosProd record for the UPC provided.
	 * @param upc - UPC
	 * @return - Single PosProd record or null if not found
	 */
	public PosProd readByUpc(String upc);
	
	/***
	 * Retrieve SalesNbr, ProdNbr+SzCd and UPC for any product records that have a Sales Number, Product Number, or UPC starting with info provided.
	 * @param partialProductInfo
	 * @return - List of Strings or empty list if none found.
	 */
	public List<String> stringListForAutocomplete(String partialProductInfo);
	
	/***
	 * Retrieve PosProd record for any product records that have a Sales Number, Product Number, or UPC starting with info provided.
	 * @param partialProductInfo
	 * @return - List of Strings or empty list if none found.
	 */
	public List<PosProd> listForAutocomplete(String partialProductInfo);

	/***
	 * Retrieve PosProd record for any product records that have a Sales Number, Product Number, or UPC starting with info provided.
	 * @param partialProductInfo
	 * @return - List of Strings or empty list if none found.
	 */
	public List<PosProd> listForAutocompleteActive(String partialProductInfo);

	/***
	 * Retrieve a List of PosProd records where the ProdNbr matches the search criteria.
	 * @param searchCriteria - Product Number you are searching for
	 * @param exactMatch - True if you want exact Product Number match, False if you want a partial Product Number match
	 * @return - List of PosProd records or Zero sized list if none found
	 */
	public List<PosProd> listForProdNbr(String searchCriteria, Boolean exactMatch);
	
}
