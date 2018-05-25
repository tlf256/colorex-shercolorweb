package com.sherwin.shercolor.common.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class OeServiceColorStand {

	@SerializedName("spectro-model")
	private String spectroModel;

	@SerializedName("spectro-mode")
	private String spectroMode;

	@SerializedName("color-comp")
	private String colorComp;

	@SerializedName("color-id")
	private String colorId;

	@SerializedName("seq-nbr")
	private int seqNbr;

	@SerializedName("curve")
	private BigDecimal[] curve;

	@SerializedName("entry-date")
	private Date entryDate;

	@SerializedName("eff-date")
	private Date effDate;

	@SerializedName("exp-date")
	private Date expDate;

	@SerializedName("cds-adl-fld")
	private String cdsAdlFld;

	@SerializedName("spectro-sn")
	private String spectroSn;

	@SerializedName("meas-date")
	private Date measDate;

	@SerializedName("meas-time")
	private String measTime;

	@SerializedName("cie-l-value")
	private double cieLValue;

	@SerializedName("cie-a-value")
	private double cieAValue;

	@SerializedName("cie-b-value")
	private double cieBValue;

	@SerializedName("curve-key")
	private int curveKey;

	@SerializedName("form-eff-dt")
	private Date formEffDt;

	@SerializedName("dbRowiD")
	private String dbRowId;

	//////////////  Constructors   ///////////////////
	public OeServiceColorStand(){
		// default
	}
	
	public OeServiceColorStand(CdsColorStand cdsColorStand){
		this.spectroModel = cdsColorStand.getSpectroModel();
		this.spectroMode = cdsColorStand.getSpectroMode();
		this.colorComp = cdsColorStand.getColorComp();
		this.colorId = cdsColorStand.getColorId();
		this.seqNbr = cdsColorStand.getSeqNbr();
		this.curve = cdsColorStand.getCurve();
		this.entryDate = cdsColorStand.getEntryDate();
		this.effDate = cdsColorStand.getEffDate();
		this.expDate = cdsColorStand.getExpDate();
		this.cdsAdlFld = cdsColorStand.getCdsAdlFld();
		this.spectroSn = cdsColorStand.getSpectroSn();
		this.measDate = cdsColorStand.getMeasDate();
		this.measTime = cdsColorStand.getMeasTime();
		this.cieLValue = cdsColorStand.getCieLValue();
		this.cieAValue = cdsColorStand.getCieAValue();
		this.cieBValue = cdsColorStand.getCieBValue();
		this.curveKey = cdsColorStand.getCurveKey();
		this.formEffDt = cdsColorStand.getFormEffDt();
		this.dbRowId = "";
		
	}


	////////////////   GETTERS   /////////////////////////

	public String getSpectroModel() {
		return spectroModel;
	}

	public String getSpectroMode() {
		return spectroMode;
	}

	public String getColorComp() {
		return colorComp;
	}

	public String getColorId() {
		return colorId;
	}

	public int getSeqNbr() {
		return seqNbr;
	}

	public BigDecimal[] getCurve() {
		return curve;
	}

	public Date getEntryDate() {
		return entryDate;
	}

	public Date getEffDate() {
		return effDate;
	}

	public Date getExpDate() {
		return expDate;
	}

	public String getCdsAdlFld() {
		return cdsAdlFld;
	}

	public String getSpectroSn() {
		return spectroSn;
	}

	public Date getMeasDate() {
		return measDate;
	}

	public String getMeasTime() {
		return measTime;
	}

	public double getCieLValue() {
		return cieLValue;
	}

	public double getCieAValue() {
		return cieAValue;
	}

	public double getCieBValue() {
		return cieBValue;
	}

	public int getCurveKey() {
		return curveKey;
	}

	public Date getFormEffDt() {
		return formEffDt;
	}

	public String getDbRowId() {
		return dbRowId;
	}

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

	public void setCurveKey(int curveKey) {
		this.curveKey = curveKey;
	}

	public void setFormEffDt(Date formEffDt) {
		this.formEffDt = formEffDt;
	}

	public void setDbRowId(String dbRowId) {
		this.dbRowId = dbRowId;
	}


	
}
