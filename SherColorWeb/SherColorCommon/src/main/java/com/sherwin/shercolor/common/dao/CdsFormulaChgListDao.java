package com.sherwin.shercolor.common.dao;

import java.util.List;

import com.sherwin.shercolor.common.domain.CdsFormulaChgList;

public interface CdsFormulaChgListDao {

	/***
	 * Read a CdsFormulaChgList record for the Color Company, Color Id, Sales Number and Colorant System Id provided.
	 * @param colorComp - Color Company
	 * @param colorId - Color Id
	 * @param salesNbr - Product Sales Number
	 * @param clrntSysId - Colorant System Id
	 * @return Single CdsFormulaChgList record or null if not found
	 */
	public CdsFormulaChgList read(String colorComp, String colorId, String salesNbr, String clrntSysId);
	
	/***
	 * Get a list of CdsFormulaChgList delete records for the Color Company, Color Id and Sales Number and Colorant System Id provided.
	 * @param colorComp - Color Company
	 * @param colorId - Color Id
	 * @param salesNbr - Product Sales Number
	 * @return Single CdsFormulaChgList record or null if not found
	 */
	public List<CdsFormulaChgList> listDeletesForColorAndProduct(String colorComp, String colorId, String salesNbr);

	/***
	 * Get a list of Product Numbers for AFCD Alternate Base Requirements using the Color Company and Color Id provided.
	 * @param colorComp - Color Company
	 * @param colorId - Color Id
	 * @return - List of Product Numbers or an empty list if none found.
	 */
	public List<String> listProductForAlternateBaseRequired(String colorComp, String colorId);
}
