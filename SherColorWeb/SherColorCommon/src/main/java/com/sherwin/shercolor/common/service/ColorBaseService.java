package com.sherwin.shercolor.common.service;

import java.math.BigDecimal;
import java.util.List;

public interface ColorBaseService {

	/***
	 * Retrieves the Interior Base Assignments for a Color
	 * @param colorComp - Color Company
	 * @param colorId - Color Id
	 * @param prodComp - Product Company Code (e.g. SW)
	 * @return - List of bases (String) or Zero sized list if none found
	 */
	public List<String> InteriorColorBaseAssignments(String colorComp, String colorId, String prodComp);
	
	/***
	 * Retrieves the Interior Base Assignments for a Color
	 * @param colorComp - Color Company
	 * @param colorId - Color Id
	 * @param prodComp - Product Company Code (e.g. SW)
	 * @return - List of bases (String) or Zero sized list if none found
	 */
	public List<String> ExteriorColorBaseAssignments(String colorComp, String colorId, String prodComp);
	
	/***
	 * Calculates the Auto Base for a color
	 * @param colorComp - Color Company
	 * @param colorId - Color Id
	 * @param prodComp - Product Company Code (e.g. SW)
	 * @return
	 */
	public List<String> GetAutoBase(String colorComp, String colorId, String prodComp);
	
	/***
	 * Calculates the Auto Base for a Spectral Curve
	 * @param curve - Spectral Curve (BigDecimal)
	 * @param prodComp - Product Company Code (e.g. SW)
	 */
	public List<String> GetAutoBase(BigDecimal[] curve, String prodComp);

}
