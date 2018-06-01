package com.sherwin.shercolor.colormath.functions;

import java.math.BigDecimal;

import com.sherwin.shercolor.colormath.domain.ColorCoordinates;

public interface ColorSpaceConverter {

	public ColorCoordinates CurveToXYZLAB(BigDecimal[] curve, String illumCode);
	
	public int[] XYZtoRGB(double[] XYZ);
	
	public int[] XYZtoRGB(double X, double Y, double Z);
	
	public ColorCoordinates XYZtoRGB(ColorCoordinates colorCoords , String illuminant, int observer);
	
	
}
