package com.sherwin.shercolor.colormath.functions;

import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sherwin.shercolor.colormath.domain.ColorCoordinates;

public class ColorCoordinatesCalculatorImpl implements ColorCoordinatesCalculator{
	static Logger logger = LogManager.getLogger(ColorCoordinatesCalculatorImpl.class);
	

	private ColorSpaceConverterImpl csc = new ColorSpaceConverterImpl();

	public static double[] D65_X = {
		0,0,0,0.008,0.137,0.676,1.603,2.451,3.418,3.699,3.064,1.933,0.802,0.156,0.039,0.347,1.07,2.17,3.397,4.732,6.07,7.311,8.291,8.634,8.672,7.93,6.446,4.669,3.095,1.859,1.056,0.57,0.274,0.121,0.058,0.028,0.012,0.006,0.003,0.002
	};
	
	public static double[] D65_Y = {
		0,0,0,0.001,0.014,0.069,0.168,0.3,0.554,0.89,1.29,1.838,2.52,3.226,4.32,5.621,6.907,8.059,8.668,8.855,8.581,7.951,7.106,6.004,5.079,4.065,2.999,2.042,1.29,0.746,0.417,0.223,0.107,0.047,0.023,0.011,0.005,0.002,0.001,0.001
	};
	
	public static double[] D65_Z = {
		0,0,-0.002,0.033,0.612,3.11,7.627,12.095,17.537,19.888,17.695,13,7.699,3.938,2.046,1.049,0.544,0.278,0.122,0.035,0.001,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
	};
	
	public static double x_Total = 94.811;
	public static double y_Total = 100.0;
	public static double z_Total = 107.304;
	
	public ColorCoordinates getColorCoordinates(double[] curve) {
		ColorCoordinates result = new ColorCoordinates();

		double xSum = 0; 
		double ySum = 0; 
		double zSum = 0;
		try {
			for(int i=0; i<40; i++){
				xSum = xSum + (curve[i] * D65_X[i] /100);
				ySum = ySum + (curve[i] * D65_Y[i] /100);
				zSum = zSum + (curve[i] * D65_Z[i] /100);
			}
			
			double xFract = xSum / x_Total;
			double yFract = ySum / y_Total;
			double zFract = zSum / z_Total;
			
			double newxFract = Math.cbrt(xFract);
			double newyFract = Math.cbrt(yFract);
			double newzFract = Math.cbrt(zFract);
			
			double lVal = 116.0 * newyFract - 16.0;
			double aVal = 500.0 * (newxFract - newyFract);
			double bVal = 200.0 * (newyFract - newzFract);
			double cVal = Math.sqrt(aVal * aVal + bVal * bVal);
			double hVal = Math.atan2(aVal, bVal);
			
			int[] rgb = csc.XYZtoRGB(xSum, ySum, zSum);
			
			result.setCieX(xSum);
			result.setCieY(ySum);
			result.setCieZ(zSum);
			
			result.setCieL(lVal);
			result.setCieA(aVal);
			result.setCieB(bVal);
			
			result.setCieC(cVal);
			result.setCieH(hVal);
	
			result.setRgbRed(rgb[0]);
			result.setRgbGreen(rgb[1]);
			result.setRgbBlue(rgb[2]);
			
			result.setRgbHex(String.format("#%02x%02x%02x",rgb[0],rgb[1],rgb[2]));
			
			return result;
		} catch (Exception e){
			logger.error(e.getMessage());
			throw(e);
		}
	}

	public ColorCoordinates getColorCoordinates(BigDecimal[] curve,String illumCode){
		ColorCoordinates result = new ColorCoordinates();
		try {
			result = csc.CurveToXYZLAB(curve, illumCode);
			
			result = csc.XYZtoRGB(result, illumCode, 10);
			
			result.setRgbHex(String.format("#%02x%02x%02x",result.getRgbRed(),result.getRgbGreen(),result.getRgbBlue()));
			
			return result;
		} catch (Exception e){
			logger.error(e.getMessage());
			throw(e);
		}
	}
}
