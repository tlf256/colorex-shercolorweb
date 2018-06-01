package com.sherwin.shercolor.colormath.functions;

import java.math.BigDecimal;

import com.sherwin.shercolor.colormath.domain.ColorCoordinates;

public interface ColorCoordinatesCalculator {

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
	
	
}
