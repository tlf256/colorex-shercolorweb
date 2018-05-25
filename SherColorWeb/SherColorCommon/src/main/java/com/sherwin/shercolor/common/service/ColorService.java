package com.sherwin.shercolor.common.service;

import java.math.BigDecimal;


import com.sherwin.shercolor.colormath.domain.ColorCoordinates;
import com.sherwin.shercolor.common.domain.CustWebParms;
import com.sherwin.shercolor.common.domain.OeServiceColorDataSet;


public interface ColorService {
	
	/***
	 * Calculate Color Coordinates (XYZ, LAB, LCH, RGB) for provided curve based on D65 10 degree observer
	 * @param curve - Color Curve(double[])
	 * @return - return ColorCoordinates record or null if unable to calculate
	 */
	public ColorCoordinates getColorCoordinates(double[] curve);
	
	/***
	 * Calculate Color Coordinates (XYZ, LAB, LCH, RGB) for provided curve and illuminant code
	 * @param curve - Color Curve(BigDecimal[])
	 * @param illumCode - Illuminant Code (e.g. D65, A, F2, etc.)
	 * @return - return ColorCoordinates record or null if failure to calculate
	 */
	public ColorCoordinates getColorCoordinates(BigDecimal[] curve,String illumCode);
	
	/***
	 * Calculate Color Coordinates (XYZ, LAB, LCH, RGB) for provided color company and color id based on D65 10 degree observer
	 * @param curve - Color Curve(double[])
	 * @return - return ColorCoordinates record or null if unable to calculate
	 */
	//public ColorCoordinates getColorCoordinates(String colorComp, String colorID);
	
	/***
	 * Calculate Color Coordinates (XYZ, LAB, LCH, RGB) for provided color company, color id  and illuminant code
	 * @param colorComp - color Company
	 * @param colorID - color ID
	 * @param illumCode - Illuminant Code (e.g. D65, A, F2, etc.)
	 * @return - return ColorCoordinates record or null if failure to calculate
	 */
	public ColorCoordinates getColorCoordinates(String colorComp, String colorID,String illumCode);
	
	/***
	 * Returns the Color's formulation data set for the given Color Company and Color Id
	 * @param colorComp - Color Company
	 * @param colorId - Color ID
	 * @return OeServcieColorDataSet or null if none found
	 */
	public OeServiceColorDataSet getDsColorFromOracle(String colorComp, String colorId);

	/***
	 * Determines if we have rights to or own the color for provided color company and color id
	 * @param colorComp - Color Company
	 * @param colorId - Color ID
	 * @param custWebParms - Customer's SherColor operational parameters
	 * @return - return TRUE when we own the rights to the color, FALSE when we do NOT own rights to the color
	 */
	public boolean isCompanyOwnedColor(String colorComp, String colorId, CustWebParms custWebParms);
	

}
