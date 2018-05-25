package com.sherwin.shercolor.common.service;

import java.util.List;

import javax.validation.ValidationException;

import com.sherwin.shercolor.common.domain.CdsProd;
import com.sherwin.shercolor.common.domain.CdsProdCharzd;
import com.sherwin.shercolor.common.domain.OeServiceProdDataSet;
import com.sherwin.shercolor.common.domain.PosProd;
import com.sherwin.shercolor.common.domain.ProductFillInfo;
import com.sherwin.shercolor.util.domain.SwMessage;

public interface ProductService {
	
	/***
	 * Executes the product validation routines.
	 * @param salesNbr - Sales Number
	 * @return True when valid, false when failure (exception will also be thrown on failure)
	 */
	public List<SwMessage> validateProduct(String salesNbr) throws ValidationException;
	
	/***
	 * Returns a list containing SalesNbr, ProdNbr+SzCd and UPC for products that have a Sales Number or Product Number or UPC starting with the search criteria provided.
	 * @param searchCriteria
	 * @return
	 */
	public List<CdsProd> productAutocomplete(String searchCriteria);
	public List<CdsProd> productAutocompleteActive(String searchCriteria);
	
	/***
	 * Returns a list containing CdsProd records for products that have a Sales Number or Product Number or UPC starting with the search criteria provided.
	 * Checks CdsProd first, if none are found, searches PosProd.  If PosProd records are found, they are dummied into CdsProd format for return. 
	 * @param searchCriteria
	 * @return
	 */
	public List<CdsProd> productAutocompleteBoth(String searchCriteria);
	public List<CdsProd> productAutocompleteBothActive(String searchCriteria);
	
	/***
	 * Returns a list containing SalesNbr, ProdNbr+SzCd and UPC for products that have a Sales Number or Product Number or UPC starting with the search criteria provided.
	 * @param searchCriteria
	 * @return
	 */
	public List<PosProd> autocompleteProduct(String searchCriteria);
	
	/***
	 * Returns a CdsProd given the sales number.
	 * @param salesNbr
	 * @return CdsProd for the given salesNbr
	 */
	public CdsProd readCdsProd(String salesNbr);
	
	/***
	 * Returns a SalesNbr (type String) for either SalesNbr or UPC provided.
	 * @param upcOrSalesNbr
	 * @return String (unformatted Sales Number)
	 */
	public String getSalesNbr(String upcOrSalesNbr);
	
	/***
	 * Returns a CdsProdCharzd given the product number.
	 * @param prodNbr
	 * @param clrntSysId
	 * @return CdsProdCharzd for the given prodNbr
	 */
	public CdsProdCharzd readCdsProdCharzd(String prodNbr, String clrntSysId);
	
	/***
	 * Returns a String representing the size code provided.
	 * @param sizeCode - Product Size Code (e.g. 16)
	 * @return - Text String representation for the size code or blank if not defined.
	 */
	public String getSizeText(String sizeCode);
	
	/***
	 * Returns the Product's formulation data set for the given Sales Number from Oracle DB
	 * @param salesNbr - Product Sales Number
	 * @return OeServcieProdDataSet or null if none found
	 */
	public OeServiceProdDataSet getDsProdFromOracleBySalesNbr(String salesNbr);
	
	/***
	 * Returns the Product's formulation data set for the given Sales Number from OpenEdge Formulation Server
	 * @param salesNbr - Product Sales Number
	 * @return OeServcieProdDataSet or null if none found
	 */
	public OeServiceProdDataSet getDsProdFromOpenEdgeBySalesNbr(String salesNbr);
	
	/***
	 * Returns the POS Product record for the Sales Number provided
	 * @param salesNbr - Product Sales Number
	 * @return PosProd or null if none found
	 */
	public PosProd readPosProd(String salesNbr);
	
	public ProductFillInfo getProductFillInfo(String salesNbr, String clrntSysId);
	
	
}
