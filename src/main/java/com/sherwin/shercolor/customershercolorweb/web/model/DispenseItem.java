package com.sherwin.shercolor.customershercolorweb.web.model;

public class DispenseItem {
	String clrntName;
	String clrntCode;
	int shots;
	int uom;
	int position;
	double decimalOunces;
	
	public String getClrntName() {
		return clrntName;
	}
	public String getClrntCode() {
		return clrntCode;
	}
	public int getShots() {
		return shots;
	}
	public int getUom() {
		return uom;
	}
	public int getPosition() {
		return position;
	}
	public double getDecimalOunces() {
		return decimalOunces;
	}
	
	public void setClrntName(String clrntName) {
		this.clrntName = clrntName;
	}
	public void setClrntCode(String clrntCode) {
		this.clrntCode = clrntCode;
	}
	public void setShots(int shots) {
		this.shots = shots;
	}
	public void setUom(int uom) {
		this.uom = uom;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public void setDecimalOunces(double decimalOunces) {
		this.decimalOunces = decimalOunces;
	}
	
	
}
