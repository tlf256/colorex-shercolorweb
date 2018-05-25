package com.sherwin.shercolor.common.dao;

import java.util.List;

import com.sherwin.shercolor.common.domain.CdsProdFamily;

public interface CdsProdFamilyDao {

	/***
	 * Read a CdsProdFamily record for the Product Family Name and Product Number provided.
	 * @param name - Product Family Name
	 * @param prodNbr - Product Number
	 * @return Single CdsProdFamily record or null if not found
	 */
	public CdsProdFamily read(String name, String prodNbr);
	
	/***
	 * Read the primary CdsProdFamily record for the Product Number provided.
	 * @param prodNbr - Product Number
	 * @return Single CdsProdFamily record or null if not found
	 */
	public CdsProdFamily readByProdNbrPrimary(String prodNbr);

	/***
	 * Retrieve a list of CdsProdFamily records for the Product Number provided. Note this will return the Product's primary family details.
	 * @param prodNbr - Product Number
	 * @param strictlyPrimary - Indicator to bring back first related product family when no primary product found for Product Number
	 * @return - List of CdsProdFamily records or Zero sized list if none found
	 */
	public List<CdsProdFamily> listFullFamilyForPrimaryProdNbr(String prodNbr, boolean strictlyPrimary);

	/***
	 * Retrieve a list of CdsProdFamily records for the Product Number provided. 
	 * @param prodNbr - Product Number
	 * @return - List of CdsProdFamily records or Zero sized list if none found
	 */
	public List<CdsProdFamily> listForProdNbr(String prodNbr);
}
