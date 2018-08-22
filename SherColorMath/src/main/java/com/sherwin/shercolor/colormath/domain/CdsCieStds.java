package com.sherwin.shercolor.colormath.domain;


import java.math.BigDecimal;

public class CdsCieStds {
	private String illumCode;
	private BigDecimal[] xValue;
	private BigDecimal[] yValue;
	private BigDecimal[] zValue;
	private double xTotal;
	private double yTotal;
	private double zTotal;
	private String cdsAdlFld;
	
	

	public String getIllumCode() {
		return illumCode;
	}
	

	public BigDecimal[] getXValue() {
		return xValue;
	}

	public BigDecimal[] getYValue() {
		return yValue;
	}
	

	public BigDecimal[] getZValue() {
		return zValue;
	}
	

	public double getXTotal() {
		return xTotal;
	}
	

	public double getYTotal() {
		return yTotal;
	}
	

	public double getZTotal() {
		return zTotal;
	}
	

	public String getCdsAdlFld() {
		return cdsAdlFld;
	}
	
	////////////////////  SETTINGS  ////////////////////////
	
	public void setIllumCode(String illumCode) {
		this.illumCode = illumCode;
	}
	public void setXValue(BigDecimal[] xValue) {
		this.xValue = xValue;
	}
	public void setYValue(BigDecimal[] yValue) {
		this.yValue = yValue;
	}
	public void setZValue(BigDecimal[] zValue) {
		this.zValue = zValue;
	}
	public void setXTotal(double xTotal) {
		this.xTotal = xTotal;
	}
	public void setYTotal(double yTotal) {
		this.yTotal = yTotal;
	}
	public void setZTotal(double zTotal) {
		this.zTotal = zTotal;
	}
	public void setCdsAdlFld(String cdsAdlFld) {
		this.cdsAdlFld = cdsAdlFld;
	}
	
	
}
