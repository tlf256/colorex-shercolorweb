package com.sherwin.shercolor.common.dao;

import java.util.List;

import com.sherwin.shercolor.common.domain.CdsColorMast;

public interface CdsColorMastDao {

	/***
	 * Read a CdsColorMast record for the Color Company and Color Id provided.
	 *  
	 * @param colorComp - Color Company
	 * @param colorId - Color Id
	 * @return Single CdsColorMast record or null if not found
	 */
	public CdsColorMast read(String colorComp, String colorId);
	

	/***
	 * Retrieve Color Id, Name and Location Code for any SW color records that have a Color Id, Name, or Location Code starting with info provided.
	 * @param partialColorInfo - Color Info you are searching for
	 * @return  - List of Strings or empty list if none found.
	 */
	public List<String> stringListForSwColorAutocomplete(String partialColorInfo);
	
	/***
	 * Retrieve full CdsColorMast record for any SW color records that have a Color Id, Name, or Location Code starting with info provided.
	 * @param partialColorInfo - Color Info you are searching for
	 * @return  - List of CdsColorMast records or empty list if none found.
	 */

	public List<CdsColorMast> listForSwColorAutocomplete(String partialColorInfo);
	
	/***
	 * Retrieve Color Id, Name and Location Code for any Competitive color records that have a Color Id, Name, or Location Code starting with info provided.
	 * @param partialColorInfo - Color Info you are searching for
	 * @return  - List of Strings or empty list if none found.
	 */
	public List<String> stringListForCompetColorAutocomplete(String partialColorInfo);
	
	/***
	 * Retrieve full CdsColorMast record for any Competitive color records that have a Color Id, Name, or Location Code starting with info provided.
	 * @param partialColorInfo - Color Info you are searching for
	 * @return  - List of CdsColorMast records or empty list if none found.
	 */
	public List<CdsColorMast> listForCompetColorAutocomplete(String partialColorInfo);
	
	/***
	 * Retrieve Color Company, Id and Name for any color records that have a Color Id starting with match criteria provided.
	 * @param partialColorId - Color Id you are searching for
	 * @return - List of Strings or empty list if none found.
	 */
	public List<String> listForAutocompleteColorId(String partialColorId);
	
	/***
	 * Retrieve Color Company, Id and Name for SW color records that have a Color Id starting with match criteria provided.
	 * @param partialColorId - Color Id you are searching for
	 * @return
	 */
	public List<String> listForAutocompleteSwColorId(String partialColorId);
	
	/***
	 * Retrieve Color Company, Id and Name for any color records that have a Color Name starting with match criteria provided.
	 * @param partialColorName - Color Name you are searching for
	 * @return - 
	 */
	public List<String> listForAutocompleteColorName(String partialColorName);
	
	/***
	 * Retrieve Color Company, Id and Name for SW color records that have a Color Name starting with match criteria provided.
	 * @param partialColorName - Color Name you are searching for
	 * @return - 
	 */
	public List<String> listForAutocompleteSwColorName(String partialColorName);
	
	
	/***
	 * Retrieve a List of CdsColorMast records where the ColorName matches the search criteria.
	 *  
	 * @param searchCriteria - Color Name you are searching for
	 * @param exactMatch - True if you want exact name match, False if you want a partial name match
	 * @return - List of CdsColorMast records or Zero sized list if none found
	 */
	public List<CdsColorMast> listForColorNameMatch(String searchCriteria, Boolean exactMatch);
	
	/***
	 * Retrieve a List of CdsColorMast records where the ColorId matches the search criteria.
	 * 
	 * @param searchCriteria - Color ID you are searching for
	 * @param exactMatch - True if you want exact Color Id match, False if you want a partial Color Id match
	 * @return - List of CdsColorMast records or Zero sized list if none found
	 */
	public List<CdsColorMast> listForColorIdMatch(String searchCriteria, Boolean exactMatch);
	
	/***
	 * Retrieve a list of all unique color companies found on the CdsColorMast table.
	 * @return  - List of Strings or empty list if none found.
	 */
	public List<String> listOfColorCompanies();
}
