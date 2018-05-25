package com.sherwin.shercolor.common.dao;

import java.util.List;

import com.sherwin.shercolor.common.domain.CdsColorBase;

public interface CdsColorBaseDao {
	
	/***
	 * Read a CdsColorBase record for the Color Company, Color Id, Interior/Exterior indicator and Sequence Number provided.
	 * @param colorComp - Color Company
	 * @param colorId - Color Id
	 * @param ieFlag - Interior/Exterior indicator
	 * @param seqNbr - Sequence Number
	 * @return Single CdsColorBase record or null if not found
	 */
	public CdsColorBase read(String colorComp, String colorId, String ieFlag, int seqNbr);
	
	/***
	 * Retrieve a List of CdsColorBase records that contain Forced color bases for the Color Company, Color Id and Interior/Exterior indicator provided.
	 * @param colorComp - Color Company
	 * @param colorId - Color Id
	 * @return - List of CdsColorBase records or Zero sized list if none found
	 */
	public List<CdsColorBase> listForceBasesForColorCompIdIntExt(String colorComp, String colorId, String ieFlag);
	
	/***
	 * Retrieve a List of CdsColorBase records for the Color Company, Color Id and Interior/Exterior indicator provided.
	 * @param colorComp - Color Company
	 * @param colorId - Color Id
	 * @param ieFlag - Interior/Exterior indicator
	 * @return - List of CdsColorBase records or Zero sized list if none found
	 */
	public List<CdsColorBase> listForColorCompIdIntExt(String colorComp, String colorId, String ieFlag);
	
	/***
	 * Retrieve a List of CdsColorBase records for the Color Company, Color Id, Interior/Exterior indicator and Product Company provided.
	 * @param colorComp - Color Company
	 * @param colorId - Color Id
	 * @param ieFlag - Interior/Exterior indicator
	 * @param prodComp - Product Company
	 * @return - List of CdsColorBase records or Zero sized list if none found
	 */
	public List<CdsColorBase> listForColorCompIdIntExtProdComp(String colorComp, String colorId, String ieFlag, String prodComp);
	
	/***
	 * Retrieve a list of bases for the Color Company, Color Id, Interior/Exterior indicator and Product Company provided.
	 * @param colorComp - Color Company
	 * @param colorId - Color Id
	 * @param ieFlag - Interior/Exterior indicator
	 * @param prodComp - Product Company
	 * @return - List of String or Zero sized list if none found
	 */
	public List<String> listBasesForColorCompIdIntExtProdComp(String colorComp, String colorId, String ieFlag, String prodComp);
	
	/***
	 * Retrieve a List of CdsColorBase records for the Color Company and Color Id provided. 
	 * WARNING - This provides all product company bases and interior/exterior types and should rarely be used.
	 * @param colorComp - Color Company
	 * @param colorId - Color Id
	 * @return - List of CdsColorBase records or Zero sized list if none found
	 */
	public List<CdsColorBase> listForColorCompId(String colorComp, String colorId);
	

}
