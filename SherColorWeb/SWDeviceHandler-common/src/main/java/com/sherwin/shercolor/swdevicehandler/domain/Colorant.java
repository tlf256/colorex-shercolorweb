package com.sherwin.shercolor.swdevicehandler.domain;

public class Colorant {
	String code;
	int shots;   // colorant shot array
	int uom;   // colorant shot uom array
	int position;   // colorant pump position array
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getShots() {
		return shots;
	}
	public void setShots(int shots) {
		this.shots = shots;
	}
	public int getUom() {
		return uom;
	}
	public void setUom(int uom) {
		this.uom = uom;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	
	
	
	
	
	/* fields from orig PROCALL message */
	//short clrntTankPos;   // colorant tank position array

	//long clrntTankCap;   // colorant tank capacity in ounces NOT times 1000
//	long clrntCalcAmt;   // colorant amount calculated by the GTI 
	// in ounces times 1000
	//long clrntActAmt ;   // colorant amount actually in tinter (in case 
	// tinter say "can't dispense" 
	// in ounces times 1000
	//long clrntLowAlrm;   // Amount where Progress will begin reporting 
	// Colorant low. In ounces times 1000

}
