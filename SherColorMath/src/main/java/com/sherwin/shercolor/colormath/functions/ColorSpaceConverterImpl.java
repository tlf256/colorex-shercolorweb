package com.sherwin.shercolor.colormath.functions;

import java.math.BigDecimal;
import java.math.MathContext;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;



import com.sherwin.shercolor.colormath.domain.ColorCoordinates;
import com.sherwin.shercolor.colormath.domain.CdsCieStds;
import com.sherwin.shercolor.colormath.domain.CdsColorXyzRgbConvert;

public class ColorSpaceConverterImpl implements ColorSpaceConverter{

	static Logger logger = LogManager.getLogger(ColorSpaceConverterImpl.class);
	


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
	    //ideally, we'd read this out of a file.  But for now, hardcode.
		CdsColorXyzRgbConvert returnConvert = new CdsColorXyzRgbConvert();

	
		try {
			switch (illuminant) {
			case "D65": 
				switch (observer) {
				case 2:
					returnConvert.setIlluminant("D65");
					returnConvert.setObserver(2);
					returnConvert.setIlluminant_desc("D65: Noon Daylight 6504K");
					returnConvert.setAbxb(new BigDecimal("-0.49853"));
					returnConvert.setAbyb(new BigDecimal("0.04156"));
					returnConvert.setAbzb(new BigDecimal("1.05723"));
					returnConvert.setAgxg(new BigDecimal("-1.53714"));
					returnConvert.setAgyg(new BigDecimal("1.87601"));
					returnConvert.setAgzg(new BigDecimal("-0.20403"));
					returnConvert.setArxr(new BigDecimal("3.24045"));
					returnConvert.setAryr(new BigDecimal("-0.96927"));
					returnConvert.setArzr(new BigDecimal("0.05564"));
					returnConvert.setCds_adl_fld("");
					break;
				case 10:	
					returnConvert.setIlluminant("D65");
					returnConvert.setObserver(10);
					returnConvert.setIlluminant_desc("D65: Noon Daylight 6504K");
					returnConvert.setAbxb(new BigDecimal("-0.49842"));
					returnConvert.setAbyb(new BigDecimal("0.04149"));
					returnConvert.setAbzb(new BigDecimal("1.07532"));
					returnConvert.setAgxg(new BigDecimal("-1.53679"));
					returnConvert.setAgyg(new BigDecimal("1.87296"));
					returnConvert.setAgzg(new BigDecimal("-0.20752"));
					returnConvert.setArxr(new BigDecimal("3.23973"));
					returnConvert.setAryr(new BigDecimal("-0.96769"));
					returnConvert.setArzr(new BigDecimal("0.0566"));
					returnConvert.setCds_adl_fld("");
					break;
				default:
					break;
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			
		}
		
		return returnConvert;
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
		    //ideally, we'd read this out of a file.  But for now, hardcode.
		CdsCieStds returnStds = new CdsCieStds();
		BigDecimal[] xValArray = new BigDecimal[40];
		BigDecimal[] yValArray = new BigDecimal[40];
		BigDecimal[] zValArray = new BigDecimal[40];
		
		try {
			switch (illumCode) {
			case "D65": 
				returnStds.setIllumCode("D65");
				returnStds.setCdsAdlFld("");
				xValArray[0] = new BigDecimal("0");
				xValArray[1] = new BigDecimal("0");
				xValArray[2] = new BigDecimal("0");
				xValArray[3] = new BigDecimal("0.008");
				xValArray[4] = new BigDecimal("0.137");
				xValArray[5] = new BigDecimal("0.676");
				xValArray[6] = new BigDecimal("1.603");
				xValArray[7] = new BigDecimal("2.451");
				xValArray[8] = new BigDecimal("3.418");
				xValArray[9] = new BigDecimal("3.699");
				xValArray[10] = new BigDecimal("3.064");
				xValArray[11] = new BigDecimal("1.933");
				xValArray[12] = new BigDecimal("0.802");
				xValArray[13] = new BigDecimal("0.156");
				xValArray[14] = new BigDecimal("0.039");
				xValArray[15] = new BigDecimal("0.347");
				xValArray[16] = new BigDecimal("1.07");
				xValArray[17] = new BigDecimal("2.17");
				xValArray[18] = new BigDecimal("3.397");
				xValArray[19] = new BigDecimal("4.732");
				xValArray[20] = new BigDecimal("6.07");
				xValArray[21] = new BigDecimal("7.311");
				xValArray[22] = new BigDecimal("8.291");
				xValArray[23] = new BigDecimal("8.634");
				xValArray[24] = new BigDecimal("8.672");
				xValArray[25] = new BigDecimal("7.93");
				xValArray[26] = new BigDecimal("6.446");
				xValArray[27] = new BigDecimal("4.669");
				xValArray[28] = new BigDecimal("3.095");
				xValArray[29] = new BigDecimal("1.859");
				xValArray[30] = new BigDecimal("1.056");
				xValArray[31] = new BigDecimal("0.57");
				xValArray[32] = new BigDecimal("0.274");
				xValArray[33] = new BigDecimal("0.121");
				xValArray[34] = new BigDecimal("0.058");
				xValArray[35] = new BigDecimal("0.028");
				xValArray[36] = new BigDecimal("0.012");
				xValArray[37] = new BigDecimal("0.006");
				xValArray[38] = new BigDecimal("0.003");
				xValArray[39] = new BigDecimal("0.002");
				returnStds.setXValue(xValArray);
				yValArray[0] = new BigDecimal("0");
				yValArray[1] = new BigDecimal("0");
				yValArray[2] = new BigDecimal("0");
				yValArray[3] = new BigDecimal("0.001");
				yValArray[4] = new BigDecimal("0.014");
				yValArray[5] = new BigDecimal("0.069");
				yValArray[6] = new BigDecimal("0.168");
				yValArray[7] = new BigDecimal("0.3");
				yValArray[8] = new BigDecimal("0.554");
				yValArray[9] = new BigDecimal("0.89");
				yValArray[10] = new BigDecimal("1.29");
				yValArray[11] = new BigDecimal("1.838");
				yValArray[12] = new BigDecimal("2.52");
				yValArray[13] = new BigDecimal("3.226");
				yValArray[14] = new BigDecimal("4.32");
				yValArray[15] = new BigDecimal("5.621");
				yValArray[16] = new BigDecimal("6.907");
				yValArray[17] = new BigDecimal("8.059");
				yValArray[18] = new BigDecimal("8.668");
				yValArray[19] = new BigDecimal("8.855");
				yValArray[20] = new BigDecimal("8.581");
				yValArray[21] = new BigDecimal("7.951");
				yValArray[22] = new BigDecimal("7.106");
				yValArray[23] = new BigDecimal("6.004");
				yValArray[24] = new BigDecimal("5.079");
				yValArray[25] = new BigDecimal("4.065");
				yValArray[26] = new BigDecimal("2.999");
				yValArray[27] = new BigDecimal("2.042");
				yValArray[28] = new BigDecimal("1.29");
				yValArray[29] = new BigDecimal("0.746");
				yValArray[30] = new BigDecimal("0.417");
				yValArray[31] = new BigDecimal("0.223");
				yValArray[32] = new BigDecimal("0.107");
				yValArray[33] = new BigDecimal("0.047");
				yValArray[34] = new BigDecimal("0.023");
				yValArray[35] = new BigDecimal("0.011");
				yValArray[36] = new BigDecimal("0.005");
				yValArray[37] = new BigDecimal("0.002");
				yValArray[38] = new BigDecimal("0.001");
				yValArray[39] = new BigDecimal("0.001");
				returnStds.setYValue(yValArray);
				zValArray[0] = new BigDecimal("0");
				zValArray[1] = new BigDecimal("0");
				zValArray[2] = new BigDecimal("-0.002");
				zValArray[3] = new BigDecimal("0.033");
				zValArray[4] = new BigDecimal("0.612");
				zValArray[5] = new BigDecimal("3.11");
				zValArray[6] = new BigDecimal("7.627");
				zValArray[7] = new BigDecimal("12.095");
				zValArray[8] = new BigDecimal("17.537");
				zValArray[9] = new BigDecimal("19.888");
				zValArray[10] = new BigDecimal("17.695");
				zValArray[11] = new BigDecimal("13");
				zValArray[12] = new BigDecimal("7.699");
				zValArray[13] = new BigDecimal("3.938");
				zValArray[14] = new BigDecimal("2.046");
				zValArray[15] = new BigDecimal("1.049");
				zValArray[16] = new BigDecimal("0.544");
				zValArray[17] = new BigDecimal("0.278");
				zValArray[18] = new BigDecimal("0.122");
				zValArray[19] = new BigDecimal("0.035");
				zValArray[20] = new BigDecimal("0.001");
				zValArray[21] = new BigDecimal("0");
				zValArray[22] = new BigDecimal("0");
				zValArray[23] = new BigDecimal("0");
				zValArray[24] = new BigDecimal("0");
				zValArray[25] = new BigDecimal("0");
				zValArray[26] = new BigDecimal("0");
				zValArray[27] = new BigDecimal("0");
				zValArray[28] = new BigDecimal("0");
				zValArray[29] = new BigDecimal("0");
				zValArray[30] = new BigDecimal("0");
				zValArray[31] = new BigDecimal("0");
				zValArray[32] = new BigDecimal("0");
				zValArray[33] = new BigDecimal("0");
				zValArray[34] = new BigDecimal("0");
				zValArray[35] = new BigDecimal("0");
				zValArray[36] = new BigDecimal("0");
				zValArray[37] = new BigDecimal("0");
				zValArray[38] = new BigDecimal("0");
				zValArray[39] = new BigDecimal("0");
				returnStds.setZValue(zValArray);
				returnStds.setXTotal(94.811);
				returnStds.setYTotal(100);
				returnStds.setZTotal(107.304);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			
		}
		
		return returnStds;
	}
	


  }