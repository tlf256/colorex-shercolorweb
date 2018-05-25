package com.sherwin.shercolor.common.domain;

import com.google.gson.annotations.SerializedName;

public class OeFormInputRequest {
	@SerializedName("scRequestID")
	private String scRequestId;

	@SerializedName("ColorantSys")
	private String clrntSysId;
	
	@SerializedName("ColorComp")
	private String colorComp;

	@SerializedName("ColorID")
	private String colorId;
	
	@SerializedName("SalesNbr")
	private String salesNbr;
	
	@SerializedName("isVinylSafe")
	private boolean vinylSafe;
	
	@SerializedName("isHighProf")
	private boolean highPerformance;
	
	@SerializedName("ForceIntExt")
	private String forceIntExt;
	
	@SerializedName("pctFormula")
	private int pctFormula;
	
	@SerializedName("FormulaFbBase")
	private String fbBase;
	
	@SerializedName("FormulaFbSubBase")
	private String fbSubBase;
	
	@SerializedName("getProdFamily")
	private boolean getProdFamily;
	
	@SerializedName("ColorCurve")
	private double[] colorCurve;
	
	@SerializedName("illum")
	private String[] illum;
	
	@SerializedName("dbRowID")
	private String dbRowId;
	
	@SerializedName("isProjection")
	private boolean projection;
	
	@SerializedName("isCorrection")
	private boolean correction;

	/////////////   GETTERS   ////////////

	public String getScRequestId() {
		return scRequestId;
	}

	public boolean isProjection() {
		return projection;
	}

	public boolean isCorrection() {
		return correction;
	}

	public String getClrntSysId() {
		return clrntSysId;
	}

	public String getColorComp() {
		return colorComp;
	}

	public String getColorId() {
		return colorId;
	}

	public String getSalesNbr() {
		return salesNbr;
	}

	public boolean isVinylSafe() {
		return vinylSafe;
	}

	public boolean isHighPerformance() {
		return highPerformance;
	}

	public String getForceIntExt() {
		return forceIntExt;
	}

	public int getPctFormula() {
		return pctFormula;
	}

	public String getFbBase() {
		return fbBase;
	}

	public String getFbSubBase() {
		return fbSubBase;
	}
	
	public String[] getIllum() {
		return illum;
	}

	public boolean isGetProdFamily() {
		return getProdFamily;
	}

	public double[] getColorCurve() {
		return colorCurve;
	}

	public String getDbRowId() {
		return dbRowId;
	}

	/////////////   SETTERS   ////////////
	
	public void setScRequestId(String scRequestId) {
		this.scRequestId = scRequestId;
	}

	public void setClrntSysId(String clrntSysId) {
		this.clrntSysId = clrntSysId;
	}

	public void setColorComp(String colorComp) {
		this.colorComp = colorComp;
	}

	public void setColorId(String colorId) {
		this.colorId = colorId;
	}

	public void setSalesNbr(String salesNbr) {
		this.salesNbr = salesNbr;
	}

	public void setVinylSafe(boolean vinylSafe) {
		this.vinylSafe = vinylSafe;
	}

	public void setHighPerformance(boolean highPerformance) {
		this.highPerformance = highPerformance;
	}

	public void setForceIntExt(String forceIntExt) {
		this.forceIntExt = forceIntExt;
	}

	public void setPctFormula(int pctFormula) {
		this.pctFormula = pctFormula;
	}

	public void setFbBase(String fbBase) {
		this.fbBase = fbBase;
	}

	public void setFbSubBase(String fbSubBase) {
		this.fbSubBase = fbSubBase;
	}

	public void setIllum(String[] illum) {
		this.illum = illum;
	}

	public void setGetProdFamily(boolean getProdFamily) {
		this.getProdFamily = getProdFamily;
	}

	public void setColorCurve(double[] colorCurve) {
		this.colorCurve = colorCurve;
	}

	public void setDbRowId(String dbRowId) {
		this.dbRowId = dbRowId;
	}
	
	public void setProjection(boolean projection) {
		this.projection = projection;
	}

	public void setCorrection(boolean correction) {
		this.correction = correction;
	}

	
}
