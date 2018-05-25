package com.sherwin.shercolor.common.domain;


import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name="CDS_CIE_STDS")
@IdClass(CdsCieStdsPK.class)
public class CdsCieStds {
	private String illumCode;
	private BigDecimal[] xValue;
	private BigDecimal[] yValue;
	private BigDecimal[] zValue;
	private double xTotal;
	private double yTotal;
	private double zTotal;
	private String cdsAdlFld;
	
	
	@Id
	@Column(name="illum_code")
	public String getIllumCode() {
		return illumCode;
	}
	
	@Column(name="x_value" )
	@org.hibernate.annotations.Type(type="com.sherwin.shercolor.common.domain.usertype.CurveType")
	public BigDecimal[] getXValue() {
		return xValue;
	}
	
	@Column(name="y_value" )
	@org.hibernate.annotations.Type(type="com.sherwin.shercolor.common.domain.usertype.CurveType")
	public BigDecimal[] getYValue() {
		return yValue;
	}
	
	@Column(name="z_value" )
	@org.hibernate.annotations.Type(type="com.sherwin.shercolor.common.domain.usertype.CurveType")
	public BigDecimal[] getZValue() {
		return zValue;
	}
	
	@Column(name="x_total")
	public double getXTotal() {
		return xTotal;
	}
	
	@Column(name="y_total")
	public double getYTotal() {
		return yTotal;
	}
	
	@Column(name="z_total")
	public double getZTotal() {
		return zTotal;
	}
	
	@Column(name="cds_adl_fld")
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
