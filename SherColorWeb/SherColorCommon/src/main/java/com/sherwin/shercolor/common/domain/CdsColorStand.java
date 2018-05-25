package com.sherwin.shercolor.common.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name="CDS_COLOR_STAND")
@IdClass(CdsColorStandPK.class)
public class CdsColorStand {
	private String spectroModel;     
	private String spectroMode;   
	private String colorComp;     
	private String colorId;       
	private int seqNbr;        
	private BigDecimal[] curve;             
	private Date entryDate;        
	private Date effDate;          
	private Date expDate;          
	private String cdsAdlFld;    
	private String spectroSn;     
	private Date measDate;         
	private String measTime;      
	private double cieLValue;    
	private double cieAValue;    
	private double cieBValue;
	private Integer curveKey;
	private Date formEffDt;
	

	@Id
	@Column(name="spectro_model")
	public String getSpectroModel() {
		return spectroModel;
	}
	
	@Id
	@Column(name="spectro_mode")
	public String getSpectroMode() {
		return spectroMode;
	}
	
	@Id
	@Column(name="color_comp")
	public String getColorComp() {
		return colorComp;
	}
	
	@Id
	@Column(name="color_id")
	public String getColorId() {
		return colorId;
	}
	
	@Id
	@Column(name="seq_nbr")
	public int getSeqNbr() {
		return seqNbr;
	}
	
	@org.hibernate.annotations.Type(type="com.sherwin.shercolor.common.domain.usertype.CurveType")
	@Column(name="curve" )
	public BigDecimal[] getCurve() {
		return curve;
	}
	
	@Column(name="entry_date")
	public Date getEntryDate() {
		return entryDate;
	}
	
	@Column(name="eff_date")
	public Date getEffDate() {
		return effDate;
	}
	
	@Column(name="exp_date")
	public Date getExpDate() {
		return expDate;
	}
	
	@Column(name="cds_adl_fld")
	public String getCdsAdlFld() {
		return cdsAdlFld;
	}
	
	@Column(name="spectro_sn")
	public String getSpectroSn() {
		return spectroSn;
	}
	
	@Column(name="meas_date")
	public Date getMeasDate() {
		return measDate;
	}
	
	@Column(name="meas_time")
	public String getMeasTime() {
		return measTime;
	}
	
	@Column(name="cie_l_value")
	public double getCieLValue() {
		return cieLValue;
	}
	
	@Column(name="cie_a_value")
	public double getCieAValue() {
		return cieAValue;
	}
	
	@Column(name="cie_b_value")
	public double getCieBValue() {
		return cieBValue;
	}
	
	@Column(name="curve_key")
	public Integer getCurveKey() {
		return curveKey;
	}

	@Column(name="form_eff_dt")
	public Date getFormEffDt() {
		return formEffDt;
	}

	/////////////////// SETTERS /////////////////////////
	
	public void setSpectroModel(String spectroModel) {
		this.spectroModel = spectroModel;
	}
	public void setSpectroMode(String spectroMode) {
		this.spectroMode = spectroMode;
	}
	public void setColorComp(String colorComp) {
		this.colorComp = colorComp;
	}
	public void setColorId(String colorId) {
		this.colorId = colorId;
	}
	public void setSeqNbr(int seqNbr) {
		this.seqNbr = seqNbr;
	}
	public void setCurve(BigDecimal[] curve) {
		this.curve = curve;
	}
	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}
	public void setEffDate(Date effDate) {
		this.effDate = effDate;
	}
	public void setExpDate(Date expDate) {
		this.expDate = expDate;
	}
	public void setCdsAdlFld(String cdsAdlFld) {
		this.cdsAdlFld = cdsAdlFld;
	}
	public void setSpectroSn(String spectroSn) {
		this.spectroSn = spectroSn;
	}
	public void setMeasDate(Date measDate) {
		this.measDate = measDate;
	}
	public void setMeasTime(String measTime) {
		this.measTime = measTime;
	}
	public void setCieLValue(double cieLValue) {
		this.cieLValue = cieLValue;
	}
	public void setCieAValue(double cieAValue) {
		this.cieAValue = cieAValue;
	}
	public void setCieBValue(double cieBValue) {
		this.cieBValue = cieBValue;
	}    
	public void setCurveKey(Integer curveKey) {
		this.curveKey = curveKey;
	}
	public void setFormEffDt(Date formEffDt) {
		this.formEffDt = formEffDt;
	}



}
