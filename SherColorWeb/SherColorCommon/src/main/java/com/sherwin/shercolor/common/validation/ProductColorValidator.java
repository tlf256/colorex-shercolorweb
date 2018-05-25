package com.sherwin.shercolor.common.validation;

import javax.validation.ValidationException;


public interface ProductColorValidator {

	/***
	 * 
	 * @param colorComp - Color Company
	 * @param colorId - Color Id
	 * @param salesNbr - Product sales number
	 * @return - True if passes, false when failure (failure will also throw an exception).
	 */
	public boolean checkProductColorBaseMatch(String colorComp, String colorId, String salesNbr) throws ValidationException;
	
	/***
	 * Check if Color has a forced Base requirement and if Product selected meets the requirement 
	 * @param colorComp - Color Company
	 * @param colorId - Color Id
	 * @param salesNbr - Product sales number
	 * @return - True if passes, false when failure (failure will also throw an exception).
	 */
	public boolean checkForceBaseAssignment(String colorComp, String colorId, String salesNbr) throws ValidationException;

	/***
	 * Check if Color has product restrictions and if Product passes the restrictions.
	 * @param colorComp - Color Company
	 * @param colorId - Color Id
	 * @param salesNbr - Product sales number
	 * @return - True if passes, false when failure (failure will also throw an exception).
	 */
	public boolean checkProductExcludedFromColor(String colorComp, String colorId, String salesNbr) throws ValidationException;
	
	/***
	 * Check if Color and Product have been flagged as an invalid combination.
	 * @param colorComp - Color Company
	 * @param colorId - Color Id
	 * @param salesNbr - Product sales number
	 * @return - True if passes, false when failure (failure will also throw an exception).
	 */
	public boolean checkAfcdDelete(String colorComp, String colorId, String salesNbr) throws ValidationException;

	/***
	 * Check if Color has a Vinyl Safe requirement and if the Product meets the requirement.
	 * @param colorComp - Color Company
	 * @param colorId - Color Id
	 * @param salesNbr - Product sales number
	 * @return - True if passes, false when failure (failure will also throw an exception).
	 */
	public boolean checkVinylSafeRestrictions(String colorComp, String colorId, String salesNbr) throws ValidationException;
	
	
}
