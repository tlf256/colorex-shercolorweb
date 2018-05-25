package com.sherwin.shercolor.desktop.domain;

import java.util.Date;

public class CdsTranDet {
	private int controlNbr;
	private int lineNbr;
	private int tranDetSeq;
	private int tranMastUnit;
	private String tranCode;
	private Date tranDate;
	private String tranTime;
	private String sourceInd;
	private String formSource;
	private String destId;
	private String userId;
	private String needExtracted;
	private String lotNbr;
	private double[] projectCurve;
	private double[] measureCurve;
	private String illumPrimary;
	private String illumSecondary;
	private String illumTertiary;
	private double specDev;
	private double deltaePrimary;
	private double deltaeSecondary;
	private double deltaeTertiary;
	private double metamerismIdx;
	private double deltaeFilm2;
	private double odDeltaeFilm1;
	private double odDeltaeFilm2;
	private double crFilm1;
	private double crFilm2;
	private double cost;
	private String notes;
	private String cdsAdlFld;
	private int qtyDispensed;
	private int corrCycle;
	private int formPct;
	private String rule;
	private int storeNbr;
	private String colorEngVer;
	private String percentSource;
	private int mqExtractedState;
	private Date mqSentDate;
	private String tinterSerialNbr;
	private String spectroSerialNbr;
	private String spectroModel;
	private String mqSentGuid;

	public int getControlNbr() {
		return controlNbr;
	}
	public int getLineNbr() {
		return lineNbr;
	}
	public int getTranDetSeq() {
		return tranDetSeq;
	}
	public int getTranMastUnit() {
		return tranMastUnit;
	}
	public String getTranCode() {
		return tranCode;
	}
	public Date getTranDate() {
		return tranDate;
	}
	public String getTranTime() {
		return tranTime;
	}
	public String getSourceInd() {
		return sourceInd;
	}
	public String getFormSource() {
		return formSource;
	}
	public String getDestId() {
		return destId;
	}
	public String getUserId() {
		return userId;
	}
	public String getNeedExtracted() {
		return needExtracted;
	}
	public String getLotNbr() {
		return lotNbr;
	}
	public double[] getProjectCurve() {
		return projectCurve;
	}
	public double[] getMeasureCurve() {
		return measureCurve;
	}
	public String getIllumPrimary() {
		return illumPrimary;
	}
	public String getIllumSecondary() {
		return illumSecondary;
	}
	public String getIllumTertiary() {
		return illumTertiary;
	}
	public double getSpecDev() {
		return specDev;
	}
	public double getDeltaePrimary() {
		return deltaePrimary;
	}
	public double getDeltaeSecondary() {
		return deltaeSecondary;
	}
	public double getDeltaeTertiary() {
		return deltaeTertiary;
	}
	public double getMetamerismIdx() {
		return metamerismIdx;
	}
	public double getDeltaeFilm2() {
		return deltaeFilm2;
	}
	public double getOdDeltaeFilm1() {
		return odDeltaeFilm1;
	}
	public double getOdDeltaeFilm2() {
		return odDeltaeFilm2;
	}
	public double getCrFilm1() {
		return crFilm1;
	}
	public double getCrFilm2() {
		return crFilm2;
	}
	public double getCost() {
		return cost;
	}
	public String getNotes() {
		return notes;
	}
	public String getCdsAdlFld() {
		return cdsAdlFld;
	}
	public int getQtyDispensed() {
		return qtyDispensed;
	}
	public int getCorrCycle() {
		return corrCycle;
	}
	public int getFormPct() {
		return formPct;
	}
	public String getRule() {
		return rule;
	}
	public int getStoreNbr() {
		return storeNbr;
	}
	public String getColorEngVer() {
		return colorEngVer;
	}
	public String getPercentSource() {
		return percentSource;
	}
	public int getMqExtractedState() {
		return mqExtractedState;
	}
	public Date getMqSentDate() {
		return mqSentDate;
	}
	public String getTinterSerialNbr() {
		return tinterSerialNbr;
	}
	public String getSpectroSerialNbr() {
		return spectroSerialNbr;
	}
	public String getSpectroModel() {
		return spectroModel;
	}
	public String getMqSentGuid() {
		return mqSentGuid;
	}
	
	//////////////////    SETTERS    ////////////////////////
	
	public void setControlNbr(int controlNbr) {
		this.controlNbr = controlNbr;
	}
	public void setLineNbr(int lineNbr) {
		this.lineNbr = lineNbr;
	}
	public void setTranDetSeq(int tranDetSeq) {
		this.tranDetSeq = tranDetSeq;
	}
	public void setTranMastUnit(int tranMastUnit) {
		this.tranMastUnit = tranMastUnit;
	}
	public void setTranCode(String tranCode) {
		this.tranCode = tranCode;
	}
	public void setTranDate(Date tranDate) {
		this.tranDate = tranDate;
	}
	public void setTranTime(String tranTime) {
		this.tranTime = tranTime;
	}
	public void setSourceInd(String sourceInd) {
		this.sourceInd = sourceInd;
	}
	public void setFormSource(String formSource) {
		this.formSource = formSource;
	}
	public void setDestId(String destId) {
		this.destId = destId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setNeedExtracted(String needExtracted) {
		this.needExtracted = needExtracted;
	}
	public void setLotNbr(String lotNbr) {
		this.lotNbr = lotNbr;
	}
	public void setProjectCurve(double[] projectCurve) {
		this.projectCurve = projectCurve;
	}
	public void setMeasureCurve(double[] measureCurve) {
		this.measureCurve = measureCurve;
	}
	public void setIllumPrimary(String illumPrimary) {
		this.illumPrimary = illumPrimary;
	}
	public void setIllumSecondary(String illumSecondary) {
		this.illumSecondary = illumSecondary;
	}
	public void setIllumTertiary(String illumTertiary) {
		this.illumTertiary = illumTertiary;
	}
	public void setSpecDev(double specDev) {
		this.specDev = specDev;
	}
	public void setDeltaePrimary(double deltaePrimary) {
		this.deltaePrimary = deltaePrimary;
	}
	public void setDeltaeSecondary(double deltaeSecondary) {
		this.deltaeSecondary = deltaeSecondary;
	}
	public void setDeltaeTertiary(double deltaeTertiary) {
		this.deltaeTertiary = deltaeTertiary;
	}
	public void setMetamerismIdx(double metamerismIdx) {
		this.metamerismIdx = metamerismIdx;
	}
	public void setDeltaeFilm2(double deltaeFilm2) {
		this.deltaeFilm2 = deltaeFilm2;
	}
	public void setOdDeltaeFilm1(double odDeltaeFilm1) {
		this.odDeltaeFilm1 = odDeltaeFilm1;
	}
	public void setOdDeltaeFilm2(double odDeltaeFilm2) {
		this.odDeltaeFilm2 = odDeltaeFilm2;
	}
	public void setCrFilm1(double crFilm1) {
		this.crFilm1 = crFilm1;
	}
	public void setCrFilm2(double crFilm2) {
		this.crFilm2 = crFilm2;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public void setCdsAdlFld(String cdsAdlFld) {
		this.cdsAdlFld = cdsAdlFld;
	}
	public void setQtyDispensed(int qtyDispensed) {
		this.qtyDispensed = qtyDispensed;
	}
	public void setCorrCycle(int corrCycle) {
		this.corrCycle = corrCycle;
	}
	public void setFormPct(int formPct) {
		this.formPct = formPct;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public void setStoreNbr(int storeNbr) {
		this.storeNbr = storeNbr;
	}
	public void setColorEngVer(String colorEngVer) {
		this.colorEngVer = colorEngVer;
	}
	public void setPercentSource(String percentSource) {
		this.percentSource = percentSource;
	}
	public void setMqExtractedState(int mqExtractedState) {
		this.mqExtractedState = mqExtractedState;
	}
	public void setMqSentDate(Date mqSentDate) {
		this.mqSentDate = mqSentDate;
	}
	public void setTinterSerialNbr(String tinterSerialNbr) {
		this.tinterSerialNbr = tinterSerialNbr;
	}
	public void setSpectroSerialNbr(String spectroSerialNbr) {
		this.spectroSerialNbr = spectroSerialNbr;
	}
	public void setSpectroModel(String spectroModel) {
		this.spectroModel = spectroModel;
	}
	public void setMqSentGuid(String mqSentGuid) {
		this.mqSentGuid = mqSentGuid;
	}
	
	
	
	
	

}
