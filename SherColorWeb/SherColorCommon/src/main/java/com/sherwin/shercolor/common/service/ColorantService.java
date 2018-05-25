package com.sherwin.shercolor.common.service;

import java.util.List;

import com.sherwin.shercolor.common.domain.CdsClrnt;
import com.sherwin.shercolor.common.domain.CdsClrntSys;

public interface ColorantService {
	
	/***
	 * Return all Colorant Systems that can be used for the Color Company, Color Id and Product Sales Number provided.
	 * @param salesNbr - Product Sales Number
	 * @return - List of CdsClrntSys records or Zero sized list if none found
	 */
	public List<CdsClrntSys> getAvailableColorantSystems(String salesNbr);
	
	/***
	 * Return a single Colorant System record for the Colorant System Id provided.
	 * @param clrntSysId - Colorant System Id
	 * @return - Single CdsClrntSys record or null if not available.
	 */
	public CdsClrntSys getColorantSystem(String clrntSysId);
	

	/***
	 * Return all Active Colorants for the Colorant System provided.
	 * @param clrntSysId - Colorant System Id
	 * @return - List of CdsClrnt records or Zero sized list if none available 
	 */
	public List<CdsClrnt> getColorantList(String clrntSysId);
	
	/***
	 * Return all Active Colorants for the Colorant System based on Product and Vinyl Safe Scenario.
	 * @param clrntSysId - Colorant System Id
	 * @param salesNbr - Product Sales Number
	 * @param vinylSafeRequested - True if Vinyl Safe ingredients only, false when no Vinyl Safe restrictions apply
	 * @return - List of CdsClrnt records or Zero sized list if none available 
	 */
	public List<CdsClrnt> getColorantList(String clrntSysId, String salesNbr, boolean vinylSafeRequested);
	
	/***
	 * Return all Colorant Increment Headers for the Colorant System provided.
	 * @param clrntSysId - Colorant System Id
	 * @return - List of the Increment Headers sorted large to small
	 */
	public List<String> getColorantIncrementHeader(String clrntSysId);
	
}
