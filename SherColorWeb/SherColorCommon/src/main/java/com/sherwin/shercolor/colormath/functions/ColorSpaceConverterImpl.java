package com.sherwin.shercolor.colormath.functions;

import java.math.BigDecimal;
import java.math.MathContext;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;

import com.sherwin.shercolor.colormath.domain.ColorCoordinates;
import com.sherwin.shercolor.common.dao.CdsCieStdsDao;
import com.sherwin.shercolor.common.dao.CdsColorXyzRgbConvertDao;
import com.sherwin.shercolor.common.domain.CdsCieStds;
import com.sherwin.shercolor.common.domain.CdsColorXyzRgbConvert;

public class ColorSpaceConverterImpl implements ColorSpaceConverter{

	static Logger logger = LogManager.getLogger(ColorSpaceConverterImpl.class);
	
	@Autowired
	private CdsColorXyzRgbConvertDao cdsColorXyzRgbConvertDao;

	@Autowired
	private CdsCieStdsDao cdsCieStdsDao;

	public static BigDecimal[] STDX;
	public static BigDecimal[] STDY;
	public static BigDecimal[] STDZ;
	public static BigDecimal XTOTAL;
	public static BigDecimal YTOTAL;
	public static BigDecimal ZTOTAL;
	
    /**
     * reference white in XYZ coordinates
     */
    //public double[] D65 = {95.0429, 100.0, 108.8900};
	public double[] D65 = {94.811D, 100.0D, 107.304D};
    public double[] whitePoint = D65;

    /**
     * XYZ to sRGB conversion matrix
     */
    // - use D65 10degree
    public double[][] Mi  = {{ 3.23973, -1.53679, -0.49842},
            				 {-0.96769,  1.87296,  0.04149},
            				 { 0.0566, -0.20752,  1.07532}};


    /**
     * Convert XYZ to LAB.
     * @param X
     * @param Y
     * @param Z
     * @return Lab values
     */
    public double[] XYZtoLAB(double X, double Y, double Z) {
    	
    	try {
    		double x = X / whitePoint[0];
    		double y = Y / whitePoint[1];
    		double z = Z / whitePoint[2];
	
    		if (x > 0.008856) {
    			x = Math.pow(x, 1.0 / 3.0);
    		}
    		else {
    			x = (7.787 * x) + (16.0 / 116.0);
    		}
    		if (y > 0.008856) {
    			y = Math.pow(y, 1.0 / 3.0);
    		}
    		else {
    			y = (7.787 * y) + (16.0 / 116.0);
    		}
    		if (z > 0.008856) {
    			z = Math.pow(z, 1.0 / 3.0);
    		}
			else {
				z = (7.787 * z) + (16.0 / 116.0);
			}
	
    		double[] result = new double[3];
	
    		result[0] = (116.0 * y) - 16.0;
    		result[1] = 500.0 * (x - y);
    		result[2] = 200.0 * (y - z);
	
    		return result;
    	} catch (Exception e){
			logger.error(e.getMessage());
			throw(e);
		}
    }

    /**
     * Convert XYZ to LAB.
     * @param XYZ
     * @return Lab values
     */
    public double[] XYZtoLAB(double[] XYZ) {
      return XYZtoLAB(XYZ[0], XYZ[1], XYZ[2]);
    }

    /**
     * Convert XYZ to RGB.
     * @param X
     * @param Y
     * @param Z
     * @return RGB in int array.
     */
    public int[] XYZtoRGB(double X, double Y, double Z) {
      int[] result = new int[3];
      try {
	      double x = X / 100.0;
	      double y = Y / 100.0;
	      double z = Z / 100.0;
	
	      // [r g b] = [X Y Z][Mi]
	      double r = (x * Mi[0][0]) + (y * Mi[0][1]) + (z * Mi[0][2]);
	      double g = (x * Mi[1][0]) + (y * Mi[1][1]) + (z * Mi[1][2]);
	      double b = (x * Mi[2][0]) + (y * Mi[2][1]) + (z * Mi[2][2]);
	
	      // assume sRGB
	      if (r > 0.0031308) {
	        r = ((1.055 * Math.pow(r, 1.0 / 2.4)) - 0.055);
	      }
	      else {
	        r = (r * 12.92);
	      }
	      if (g > 0.0031308) {
	        g = ((1.055 * Math.pow(g, 1.0 / 2.4)) - 0.055);
	      }
	      else {
	        g = (g * 12.92);
	      }
	      if (b > 0.0031308) {
	        b = ((1.055 * Math.pow(b, 1.0 / 2.4)) - 0.055);
	      }
	      else {
	        b = (b * 12.92);
	      }
	
	      r = (r < 0) ? 0 : r;
	      g = (g < 0) ? 0 : g;
	      b = (b < 0) ? 0 : b;
	
	      // convert 0..1 into 0..255
	      result[0] = (int) Math.round(r * 255);
	      result[1] = (int) Math.round(g * 255);
	      result[2] = (int) Math.round(b * 255);
	
	      return result;
  	} catch (Exception e){
			logger.error(e.getMessage());
			throw(e);
		}
    }

    /**
     * Convert XYZ to RGB
     * @param XYZ in a double array.
     * @return RGB in int array.
     */
    public int[] XYZtoRGB(double[] XYZ) {
      return XYZtoRGB(XYZ[0], XYZ[1], XYZ[2]);
    }


	public ColorCoordinates XYZtoRGB(ColorCoordinates colorCoords , String illuminant, int observer) {
		CdsColorXyzRgbConvert vcc;
		MathContext mc = new MathContext(9);
		BigDecimal VCR = new BigDecimal("0");
		BigDecimal VCRX = new BigDecimal("0");
		BigDecimal VCRY = new BigDecimal("0");
		BigDecimal VCRZ = new BigDecimal("0");
		BigDecimal VCG = new BigDecimal("0");
		BigDecimal VCB = new BigDecimal("0");
		BigDecimal bd100 = new BigDecimal("100");
		BigDecimal bd1292 = new BigDecimal("12.92");
		BigDecimal bd055 = new BigDecimal(".055");
		BigDecimal bd1055 = new BigDecimal("1.055");
		double powerraise = 1/2.4;
		BigDecimal bdpowerraise = new BigDecimal(powerraise,mc);
		BigDecimal bd0031308 = new BigDecimal("0.0031308");
		BigDecimal bdneg0031308 = new BigDecimal("0.0031308").multiply(new BigDecimal("-1"));
		
		try {
			BigDecimal bdX = BigDecimal.valueOf(colorCoords.getCieX());
			BigDecimal bdY = BigDecimal.valueOf(colorCoords.getCieY());
			BigDecimal bdZ = BigDecimal.valueOf(colorCoords.getCieZ());
			
			
	        int WhiteDigitalCount = 255;
	        int BlackDigitalCount = 0;
	        
			//Get the VCC numbers.
			vcc = GetVCC(illuminant, observer);
			
			if (vcc!=null) {
				 /* Conversion to RGB based on ITU-R BT.709 Reference Primaries
		            url http://www.color.org/sRGB.html
		            From Color in Business Science and industry Judd and Wyszecki 3rd edition
		            pp 234-244 and pp54-55 */
	
		        VCRX = vcc.getArxr().multiply(bdX.divide(bd100,mc));
	//	        System.out.println(" VCR X = " + VCRX.toString());
		        VCRY = vcc.getAgxg().multiply(bdY.divide(bd100,mc));	
	/*	        System.out.println(" Y is " + theXYZ.getY());
		        System.out.println(" agxg is " + vcc.getAgxg());
		        System.out.println(" VCR X + Y = " + VCRY.toString());*/
		        VCRZ = vcc.getAbxb().multiply(bdZ.divide(bd100,mc));
	/*	        System.out.println(" VCR X + Y + Z = " + VCRZ.toString());
		        System.out.println(" Z is " + theXYZ.getZ());
		        System.out.println(" abxb is " + vcc.getAbxb());*/
		        
		        VCR = VCRX.add(VCRY.add(VCRZ));
		        
		        			        
		        VCG = vcc.getAryr().multiply(bdX.divide(bd100,mc));
		        VCG = VCG.add(vcc.getAgyg().multiply(bdY.divide(bd100,mc)));
		        VCG = VCG.add(vcc.getAbyb().multiply(bdZ.divide(bd100,mc)));
		        
		        VCB = vcc.getArzr().multiply(bdX.divide(bd100,mc));
		        VCB = VCB.add(vcc.getAgzg().multiply(bdY.divide(bd100,mc)));
		        VCB = VCB.add(vcc.getAbzb().multiply(bdZ.divide(bd100,mc)));
		        
	/*	        System.out.println(" VCR = " + VCR.toString());
		        System.out.println(" VCG = " + VCG.toString());
		        System.out.println(" VCB = " + VCB.toString()); */
	
		        /* Gamma Correction Original
		         sRGB Tristimulus Values Transformed to Nonlinear sR'G'B'
		         This Closely Fits a Gamma of 2.2
		         from sRGB update 12/4/2006 FXO
		                If VCR <= 0.00304 Then
	        	 new code added for wider range,
		         from Specification of bg-sRGB  */
	
	
		        if (VCR.compareTo(bdneg0031308) <= 0) {
		            VCR = bd1055.negate().multiply(RaiseToPower(VCR.negate(),bdpowerraise));
		            VCR = VCR.add(bd055);
		        } else {
		        	if (VCR.compareTo(bdneg0031308) > 0 && VCR.compareTo(bd0031308) <= 0) {
		        
		        		VCR = bd1292.multiply(VCR);
		        	} else {
		        		if (VCR.compareTo(bd0031308) > 0) {
		        			VCR = bd1055.multiply(RaiseToPower(VCR,bdpowerraise));
		        			VCR = VCR.subtract(bd055);
		        			
		        		}
		        	}
		        }
		        
	
		        
		        if (VCG.compareTo(bdneg0031308) <= 0) {
		            VCG = bd1055.negate().multiply(RaiseToPower(VCG.negate(),bdpowerraise));
		            VCG = VCG.add(bd055);
		        } else {
		        	if (VCG.compareTo(bdneg0031308) > 0 && VCG.compareTo(bd0031308) <= 0) {
		        		VCG = bd1292.multiply(VCG);
		        	} else { 
		        		if (VCG.compareTo(bd0031308) > 0) {
		        			VCG = bd1055.multiply(RaiseToPower(VCG,bdpowerraise));
		        			VCG = VCG.subtract(bd055);
		        		}
		        	}
		        }
		        	
		        	
		        if (VCB.compareTo(bdneg0031308) <= 0) {
		            VCB = bd1055.negate().multiply(RaiseToPower(VCB.negate(),bdpowerraise));
		            VCB = VCB.add(bd055);
		        } else {
		        	if (VCB.compareTo(bdneg0031308) > 0 && VCB.compareTo(bd0031308) <= 0) {
		        		VCB = bd1292.multiply(VCB);
		        	} else {
			        	if (VCB.compareTo(bd0031308) > 0) {
		        			VCB = bd1055.multiply(RaiseToPower(VCB,bdpowerraise));
		        			VCB = VCB.subtract(bd055);
			        	}
		        	}
		        }
	
	/*	        System.out.println("AFTER COMPARISON");
		        System.out.println(" VCR = " + VCR.toString());
		        System.out.println(" VCG = " + VCG.toString());
		        System.out.println(" VCB = " + VCB.toString()); */
	
		        if (VCR.compareTo(BigDecimal.ONE) > 0) {
		            VCR = BigDecimal.ONE;
		        }
	
		        if (VCG.compareTo(BigDecimal.ONE) > 0) {
		            VCG = BigDecimal.ONE;
		        }
	
		        if (VCB.compareTo(BigDecimal.ONE) > 0) {
		            VCB = BigDecimal.ONE;
		        }
	
		        if (VCR.compareTo(BigDecimal.ZERO) < 0) {
		            VCR = BigDecimal.ZERO;
		        }
	
		        if (VCG.compareTo(BigDecimal.ZERO) < 0) {
		            VCG = BigDecimal.ZERO;
		        }
	
		        if (VCB.compareTo(BigDecimal.ZERO) < 0) {
		            VCB = BigDecimal.ZERO;
		        }
	
		        // Convert non-linear sR'G'B' to Digital Code Values
		        colorCoords.setRgbRed((int) Math.round(((WhiteDigitalCount - BlackDigitalCount) * VCR.doubleValue()) + BlackDigitalCount));
		        colorCoords.setRgbGreen((int) Math.round(((WhiteDigitalCount - BlackDigitalCount) * VCG.doubleValue()) + BlackDigitalCount));
		        colorCoords.setRgbBlue((int) Math.round(((WhiteDigitalCount - BlackDigitalCount) * VCB.doubleValue()) + BlackDigitalCount));
	
			} else {
		        colorCoords.setRgbRed(0);
		        colorCoords.setRgbGreen(0);
		        colorCoords.setRgbBlue(0);
			}
			
			return colorCoords;
    	} catch (Exception e){
			logger.error(e.getMessage());
			throw(e);
		}
		
	}
	
	protected CdsColorXyzRgbConvert GetVCC(String illuminant, int observer) {
		return cdsColorXyzRgbConvertDao.read(illuminant, observer);
	}
	
	protected BigDecimal RaiseToPower(BigDecimal theBase, BigDecimal thePower) {
		double tempCalc = 0;
		try {
			tempCalc = Math.pow(theBase.doubleValue(), thePower.doubleValue());
			return new BigDecimal(tempCalc);
    	} catch (Exception e){
			logger.error(e.getMessage());
			throw(e);
		}
	}

	public ColorCoordinates CurveToXYZLAB(BigDecimal[] curve, String illumCode) {
		CdsCieStds cieRec = null;
		ColorCoordinates colorCoords = new ColorCoordinates();
		MathContext mc = new MathContext(12);
		BigDecimal xStd = new BigDecimal("0");
		BigDecimal yStd = new BigDecimal("0");
		BigDecimal zStd = new BigDecimal("0");
		
		BigDecimal bd100 = new BigDecimal("100");
		
		//Get the Cie std for the given illumination code.
		//04/24/2013 - BKP - Changed to set the main cierec on the first read.  This
		//                   should prevent tens of millions of DB reads in the process.
//		if (MakeRGB.getMainCieRec() != null) {
//			logger.info("not null");
//			cieRec = MakeRGB.getMainCieRec();
//		} else {
//			logger.info("null");
//			cieRec = GetCdsCieStds(illumCode);
//			MakeRGB.setMainCieRec(cieRec);
//		}
		
		try {
			if(STDX==null || STDY==null || STDZ==null){
				cieRec = GetCdsCieStds(illumCode);
				STDX = cieRec.getXValue();
				STDY = cieRec.getYValue();
				STDZ = cieRec.getZValue();
				XTOTAL = BigDecimal.valueOf(cieRec.getXTotal());
				YTOTAL = BigDecimal.valueOf(cieRec.getYTotal());
				ZTOTAL = BigDecimal.valueOf(cieRec.getZTotal());
			}
			
			
			if (curve!=null) {
				//process the curve points into the X Y and Z properties of the returnRec.
				
				for(int i=0;i<40;i++){
					xStd = xStd.add(STDX[i].multiply(curve[i].divide(bd100)));
					yStd = yStd.add(STDY[i].multiply(curve[i].divide(bd100)));
					zStd = zStd.add(STDZ[i].multiply(curve[i].divide(bd100)));
				}
	
				// now do Lab and LCH
				BigDecimal xFract = xStd.divide(XTOTAL,mc);
				BigDecimal yFract = yStd.divide(YTOTAL,mc);
				BigDecimal zFract = zStd.divide(ZTOTAL,mc);
				
				double newxFract = Math.cbrt(xFract.doubleValue());
				double newyFract = Math.cbrt(yFract.doubleValue());
				double newzFract = Math.cbrt(zFract.doubleValue());
				
				double lVal = 116.0 * newyFract - 16.0;
				double aVal = 500.0 * (newxFract - newyFract);
				double bVal = 200.0 * (newyFract - newzFract);
				double cVal = Math.sqrt(aVal * aVal + bVal * bVal);
				double hVal = Math.atan2(aVal, bVal);
				
	
				colorCoords.setCieX(xStd.doubleValue());
				colorCoords.setCieY(yStd.doubleValue());
				colorCoords.setCieZ(zStd.doubleValue());
				colorCoords.setCieL(lVal);
				colorCoords.setCieA(aVal);
				colorCoords.setCieB(bVal);
				colorCoords.setCieC(cVal);
				colorCoords.setCieH(hVal);
				
			} else {
				//what to do if the illumination code is bad or not on file?
				//return null? return error?
				colorCoords = null;
				
			}
		
		return colorCoords;
    	} catch (Exception e){
			logger.error(e.getMessage());
			throw(e);
		}
		
	}
	
	protected CdsCieStds GetCdsCieStds(String illumCode) {
			return cdsCieStdsDao.read(illumCode);
	}
	


  }