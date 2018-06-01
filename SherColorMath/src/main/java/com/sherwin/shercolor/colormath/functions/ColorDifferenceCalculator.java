package com.sherwin.shercolor.colormath.functions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sherwin.shercolor.colormath.domain.ColorCoordinates;

public class ColorDifferenceCalculator {
	static Logger logger = LogManager.getLogger(ColorDifferenceCalculator.class);

	public double calculateDeltaE(ColorCoordinates standard, ColorCoordinates trial) {
		double L1 = standard.getCieL();
		double a1 = standard.getCieA();
		double b1 = standard.getCieB();
		
		double L2 = trial.getCieL();
		double a2 = trial.getCieA();
		double b2 = trial.getCieB();
		
		double result = Math.sqrt(Math.pow(L2 - L1, 2) + Math.pow(a2 - a1, 2) + Math.pow(b2 - b1, 2));
		
		return result;
	}
	
	
	public double calculateSpd(double[] standard, double[] trial){
		double spd = 0D;
		for(int i=0;i<standard.length;i++){
			spd = spd + Math.pow(standard[i] - trial[i], 2);
		}
		spd = Math.sqrt(spd);
		
		return spd;
	}
	
	public boolean passStipulationCheck(ColorCoordinates standard, ColorCoordinates trial){
		boolean stipOk = false;
		double xStd = standard.getCieX();
		double yStd = standard.getCieY();
		double zStd = standard.getCieZ();
		double xTrial = trial.getCieX();
		double yTrial = trial.getCieY();
		double zTrial = trial.getCieZ();
		
		double xyDiff, yzDiff, zxDiff;
		String xyStipStd;
		xyDiff = xStd = yStd;
		if (xyDiff < -.1D){
			xyStipStd = "X<Y";
		} else {
			if(xyDiff > .1D){
				xyStipStd = "X>Y";
			} else {
				xyStipStd = "X?Y";
			}
		}
		
		String yzStipStd;
		yzDiff = yStd = zStd;
		if (yzDiff < -.1D){
			yzStipStd = "Y<Z";
		} else {
			if(yzDiff > .1D){
				yzStipStd = "Y>Z";
			} else {
				yzStipStd = "Y?Z";
			}
		}
		
		String zxStipStd;
		zxDiff = zStd = xStd;
		if (zxDiff < -.1D){
			zxStipStd = "Z<X";
		} else {
			if(zxDiff > .1D){
				zxStipStd = "Z>X";
			} else {
				zxStipStd = "Z?X";
			}
		}
		
		// calc ratio trial to std
		double xRatio = 100 * (xTrial/xStd);
		double yRatio = 100 * (yTrial/yStd);
		double zRatio = 100 * (zTrial/zStd);
		
		String xyStipTrial;
		xyDiff = xRatio = yRatio;
		if (xyDiff < -.1D){
			xyStipTrial = "X<Y";
		} else {
			if(xyDiff > .1D){
				xyStipTrial = "X>Y";
			} else {
				xyStipTrial = "X?Y";
			}
		}
		
		String yzStipTrial;
		yzDiff = yRatio = zRatio;
		if (yzDiff < -.1D){
			yzStipTrial = "Y<Z";
		} else {
			if(yzDiff > .1D){
				yzStipTrial = "Y>Z";
			} else {
				yzStipTrial = "Y?Z";
			}
		}
		
		String zxStipTrial;
		zxDiff = zRatio = xRatio;
		if (zxDiff < -.1D){
			zxStipTrial = "Z<X";
		} else {
			if(zxDiff > .1D){
				zxStipTrial = "Z>X";
			} else {
				zxStipTrial = "Z?X";
			}
		}

		// check if stips for xy, yz, zx, if all ok then stip is ok
		boolean xyOk = false, yzOk = false, zxOk = false;
		if (xyStipStd.equalsIgnoreCase("X?Y") || xyStipStd.equalsIgnoreCase(xyStipTrial) || xyStipTrial.equalsIgnoreCase("X=Y")) xyOk = true;
		if (yzStipStd.equalsIgnoreCase("Y?X") || yzStipStd.equalsIgnoreCase(yzStipTrial) || yzStipTrial.equalsIgnoreCase("Y=X")) yzOk = true;
		if (zxStipStd.equalsIgnoreCase("Z?X") || zxStipStd.equalsIgnoreCase(zxStipTrial) || zxStipTrial.equalsIgnoreCase("Z=Z")) zxOk = true;

		if (xyOk && yzOk && zxOk) stipOk = true;
		else stipOk = false;
		
		return stipOk;
	}

}
