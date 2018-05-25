package com.sherwin.shercolor.common.service;

import java.util.List;

import com.sherwin.shercolor.common.domain.CdsColorMast;
import com.sherwin.shercolor.util.domain.SwMessage;

public interface ColorMastService {
	/***
	 * Retrieve Color Company, Id and Name for any color records that have a Color Id or Name starting with match criteria provided.
	 * @param partialColorNameOrId - Color Name Or Id you are searching for
	 * @return
	 */
	public String[] autocompleteColor(String partialColorNameOrId);
	
	/***
	 * Retrieve Id and Name for any color records that have a Color Id or Name starting with match criteria provided and is an SW color.
	 * @param partialColorNameOrId - Color Name Or Id you are searching for
	 * @return
	 */
	public List<CdsColorMast> autocompleteSWColor(String partialColorNameOrId);
	
	/***
	 * Retrieve Color Company, Id and Name for any color records that have a Color Id or Name starting with match criteria provided for competitive companies.
	 * @param partialColorNameOrId - Color Name Or Id you are searching for
	 * @return
	 */
	public List<CdsColorMast> autocompleteCompetitiveColor(String partialColorNameOrId);
	
		
	/***
	 * Retrieve all Color Companies.
	 * @return
	 */
	public String[] listColorCompanies();
	
	/***
	 * Retrieve CdsColorMast record for a give Color Company/Color ID combination.
	 * @param colorComp - the color company associated with the color
	 * @param colorID - the color ID of the color
	 * @return
	 */
	public CdsColorMast read(String colorComp, String colorID);
	
	/***
	 * Retrieve a list of 0 or more SwMessages, for a give Color Company/Color ID combination.
	 * @param colorComp - the color company associated with the color
	 * @param colorID - the color ID of the color
	 * @return a list of 0 or more SwMessages.  0 indicates successful validation.
	 */
	public List<SwMessage> validate(String colorComp, String colorID);
}
