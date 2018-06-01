package com.sherwin.shercolor.colormath.functions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sherwin.shercolor.colormath.domain.AutoBase;
import com.sherwin.shercolor.colormath.domain.LineSeg;

public class ColorBaseCalculator {
	static Logger logger = LogManager.getLogger(ColorBaseCalculator.class);
	
	public static final LineSeg[] LINE_SEGS_SHERWIN_WILLIAMS = new LineSeg[3];
	public static final LineSeg[] lINE_SEGS_DURON            = new LineSeg[4];
	
	static{
		LINE_SEGS_SHERWIN_WILLIAMS[0] = new LineSeg('U', 40.0, 0.0,  66.0, 70.0);
		LINE_SEGS_SHERWIN_WILLIAMS[1] = new LineSeg('D', 64.0, 0.0,  90.0, 70.0);
		LINE_SEGS_SHERWIN_WILLIAMS[2] = new LineSeg('X', 88.0, 0.0, 100.0, 30.0);
		
		lINE_SEGS_DURON[0] = new LineSeg('U', 26.0, 0.0,  76.0, 90.0);
		lINE_SEGS_DURON[1] = new LineSeg('A', 38.0, 0.0,  88.0, 90.0);
		lINE_SEGS_DURON[2] = new LineSeg('D', 55.0, 0.0, 100.0, 85.0);
		lINE_SEGS_DURON[3] = new LineSeg('M', 74.0, 0.0, 100.0, 70.0);
	}
	
	public static void calcBaseSherwinWilliams(AutoBase autobase) {
		double[] dists = getDistances(autobase, LINE_SEGS_SHERWIN_WILLIAMS);
		
		if (dists[0] < 0.0) { // If the first distance is negative
			autobase.setBase1('U'); // this entry is in the Ultra-Deep region
			autobase.setBase2('D'); // Primary is U, secondary is D
			return;
		}
		
		if (dists[1] < 0.0) { // if the second distance is negative
			autobase.setBase1('D');; // this entry is in the Deep region
			if (Math.abs(dists[0]) < Math.abs(dists[1])) {
				autobase.setBase2('U'); // the secondary is the closer of Ultra-Deep
			}
			else {
				autobase.setBase2('X'); // or extra-white
			}
			return;
		}

		if (dists[2] > 0.0) { // if the third distance is positive
			autobase.setBase1('L'); // this entry is in the Luminous region
			autobase.setBase2('X'); // with a secondary of Extra-White
			return;
		}

		autobase.setBase1('X'); // this must be extra-white
		if (Math.abs(dists[1]) < Math.abs(dists[2])) {
			autobase.setBase2('D'); // secondary is the closer of Deep
		}
		else {
			autobase.setBase2('L'); // or luminous
		}
	}
	
	public static void calcBaseDuron(AutoBase autobase) {
		double[] dists = getDistances(autobase, lINE_SEGS_DURON);

		if (dists[0] < 0.0) { // If the first distance is negative
			autobase.setBase1('N'); // this entry is in the Neutral region
			autobase.setBase2('A'); // Primary is N, secondary is A
			return;
		}

		if (dists[1] < 0.0) { // if the second distance is negative
			autobase.setBase1('A'); // this entry is in the Accent region
			if (Math.abs(dists[0]) < Math.abs(dists[1])) {
				autobase.setBase2('N'); // the secondary is the closer of Neutral
			} else {
				autobase.setBase2('D'); // or Deep
			}
			return;
		}

		if (dists[2] < 0.0) { // if the third distance is negative
			autobase.setBase1('D'); // this entry is in the Deep region
			if (Math.abs(dists[1]) < Math.abs(dists[2])){
				autobase.setBase2('A'); // the secondary is the closer of Accent
			}
			else{
				autobase.setBase2('M'); // or Midtone
			}
			return;
		}

		if (dists[3] > 0.0) { // if the fourth distance is positive
			autobase.setBase1('W'); // this entry is in the White region
			autobase.setBase2('M'); // with a secondary of Midtone
			return;
		}

		autobase.setBase1('M'); // this must be Mid-tone
		if (Math.abs(dists[2]) < Math.abs(dists[3])){
			autobase.setBase2('D'); // secondary is the closer of Deep
		}
		else{
			autobase.setBase2('W'); // or White
		}
	}
	
	private static double[] getDistances(AutoBase autobase, LineSeg[] linesegs){
		int len = linesegs.length;
		
		double[] result = new double[len];
		
		autobase.setC(Math.sqrt((autobase.getA() * autobase.getA()) + (autobase.getB() * autobase.getB())));
		
		double slopeab, slopeac, angBAC;
		
		for(int i=0; i<len; i++){
			
			LineSeg lineSeg = linesegs[i];
			
			// calculate the slope of line segment AB
			slopeab = Math.atan((lineSeg.endy - lineSeg.begy) / (lineSeg.endx - lineSeg.begx));
			
			// calculate the slope of line segment AC
			slopeac = Math.atan((autobase.getC() - lineSeg.begy) / (autobase.getL() - lineSeg.begx));
            
			// if slopeac is negative, complement it
			if(slopeac < 0.0){
				slopeac = slopeac + Math.PI;
			}
			
			// Angle BAC is the included angle
			angBAC = slopeab - slopeac;
			
            // Now compute distance to line
			result[i] = Math.sin(angBAC) * 
					Math.sqrt(
							(autobase.getL() - lineSeg.begx) * (autobase.getL() - lineSeg.begx) +
							(autobase.getC() - lineSeg.begy) * (autobase.getC() - lineSeg.begy) );
		}

		
		return result;
	}

}
