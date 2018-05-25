package com.sherwin.shercolor.common.domain;


public class FormulaIngredient implements Cloneable{

	private String clrntSysId;
	private String tintSysId;
	private String fbSysId;
	private String name;
	private String engSysId;
	private String organicInd;
	private String statusInd;
	private String excludeProd;
	private String forceProd;
	private int shots;
	private int shotSize;
	private double grams;
	private double wpg;
	private int[] increment = {0,0,0,0};
	
	public Object clone(){
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}

	public String getClrntSysId() {
		return clrntSysId;
	}
	public String getTintSysId() {
		return tintSysId;
	}
	public String getFbSysId() {
		return fbSysId;
	}
	public String getName() {
		return name;
	}
	public String getEngSysId() {
		return engSysId;
	}
	public String getOrganicInd() {
		return organicInd;
	}
	public String getStatusInd() {
		return statusInd;
	}
	public String getExcludeProd() {
		return excludeProd;
	}
	public String getForceProd() {
		return forceProd;
	}
	public int getShots() {
		return shots;
	}
	public int getShotSize() {
		return shotSize;
	}
	public double getGrams() {
		return grams;
	}
	public double getWpg() {
		return wpg;
	}
	public int[] getIncrement() {
		return increment;
	}
	
	////////////////////// SETTERS ///////////////////////////
	
	public void setClrntSysId(String clrntSysId) {
		this.clrntSysId = clrntSysId;
	}
	public void setTintSysId(String tintSysId) {
		this.tintSysId = tintSysId;
	}
	public void setFbSysId(String fbSysId) {
		this.fbSysId = fbSysId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setEngSysId(String engSysId) {
		this.engSysId = engSysId;
	}
	public void setOrganicInd(String organicInd) {
		this.organicInd = organicInd;
	}
	public void setStatusInd(String statusInd) {
		this.statusInd = statusInd;
	}
	public void setExcludeProd(String excludeProd) {
		this.excludeProd = excludeProd;
	}
	public void setForceProd(String forceProd) {
		this.forceProd = forceProd;
	}
	public void setShots(int shots) {
		this.shots = shots;
	}
	public void setShotSize(int shotSize) {
		this.shotSize = shotSize;
	}
	public void setGrams(double grams) {
		this.grams = grams;
	}
	public void setIncrement(int[] increment) {
		this.increment = increment;
	}
	public void setWpg(double wpg) {
		this.wpg = wpg;
	}
	
}
