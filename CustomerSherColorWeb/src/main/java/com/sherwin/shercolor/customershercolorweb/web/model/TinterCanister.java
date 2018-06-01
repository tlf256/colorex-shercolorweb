package com.sherwin.shercolor.customershercolorweb.web.model;

import java.util.Comparator;

import com.sherwin.shercolor.common.domain.CdsClrntIncr;

public class TinterCanister {

	private int position;
	private String clrntCode;
	private String clrntName;
	private Double maxCanisterFill;
	private Double fillAlarmLevel;
	private Double fillStopLevel;
	private Double currentClrntAmount;
	private String rgbHex;
	
	public String getClrntCode() {
		return clrntCode;
	}
	public String getClrntName() {
		return clrntName;
	}
	public int getPosition() {
		return position;
	}
	public Double getMaxCanisterFill() {
		return maxCanisterFill;
	}
	public Double getFillAlarmLevel() {
		return fillAlarmLevel;
	}
	public Double getFillStopLevel() {
		return fillStopLevel;
	}
	public Double getCurrentClrntAmount() {
		return currentClrntAmount;
	}
	public String getRgbHex() {
		return rgbHex;
	}
	
	/////////////  SETTERS   ///////////////////

	public void setClrntCode(String clrntCode) {
		this.clrntCode = clrntCode;
	}
	public void setClrntName(String clrntName) {
		this.clrntName = clrntName;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public void setMaxCanisterFill(Double maxCanisterFill) {
		this.maxCanisterFill = maxCanisterFill;
	}
	public void setFillAlarmLevel(Double fillAlarmLevel) {
		this.fillAlarmLevel = fillAlarmLevel;
	}
	public void setFillStopLevel(Double fillStopLevel) {
		this.fillStopLevel = fillStopLevel;
	}
	public void setCurrentClrntAmount(Double currentClrntAmount) {
		this.currentClrntAmount = currentClrntAmount;
	}
	public void setRgbHex(String rgbHex) {
		this.rgbHex = rgbHex;
	}

	public static Comparator<TinterCanister> NameComparator = new Comparator<TinterCanister>(){
		public int compare(TinterCanister e1, TinterCanister e2){
			//ascending order
			String name1 = e1.getClrntName().toUpperCase();
			String name2 = e2.getClrntName().toUpperCase();
			return name1.compareTo(name2);
		}
	};

	public static Comparator<TinterCanister> PositionComparator = new Comparator<TinterCanister>(){
		public int compare(TinterCanister e1, TinterCanister e2){
			//ascending order
			if(e1.getPosition() < e2.getPosition()) return -1;
			if(e1.getPosition() > e2.getPosition()) return 1;
			return 0;
		}
	};


	
}
