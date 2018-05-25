package com.sherwin.shercolor.common.domain;


import com.google.gson.annotations.SerializedName;

public class OeFormRespFormula {

	@SerializedName("scRequestID")
	private String scRequestId;

	@SerializedName("FormulaType")
	private String formulaType;
	
	@SerializedName("FormulaSource")
	private String formulaSourceDescr;
	
	@SerializedName("FormulaPct")
	private int formulaPct;
	
	@SerializedName("FormulaFbBase")
	private String formulaFbBase;
	
	@SerializedName("FormulaFbSubBase")
	private String formulaFbSubBase;
	
	@SerializedName("clrnt-tint-sys-id")
	private String[] clrntTintSysId;
	
	@SerializedName("clrnt-nm")
	private String[] clrntNm;
	
	@SerializedName("clrnt-shots")
	private int[] clrntShots;
	
	@SerializedName("clrnt-incr1")
	private String[] clrntIncr1;
	
	@SerializedName("clrnt-incr2")
	private String[] clrntIncr2;
	
	@SerializedName("clrnt-incr3")
	private String[] clrntIncr3;
	
	@SerializedName("clrnt-incr4")
	private String[] clrntIncr4;
	
	@SerializedName("clrnt-qual")
	private String[] clrntQual;
	
	@SerializedName("clrnt-status-ind")
	private String[] clrntStatusInd;
	
	@SerializedName("formula-exists")
	private String formulaExists;
	
	@SerializedName("func-status")
	private String funcStatus;
	
	@SerializedName("clrnt-code")
	private String[] clrntCode;
	
	@SerializedName("proj-curve")
	private Double[] projCurve;
	
	@SerializedName("measuredCurve")
	private Double[] measCurve;
	
	@SerializedName("formulation-time")
	private Double formulaTime;
	
	@SerializedName("formula-cost")
	private Double formulaCost;
	
	@SerializedName("formula-spd")
	private Double formulaSpd;
	
	@SerializedName("formula-illum")
	private String[] formulaIllum;
	
	@SerializedName("formula-deltae")
	private Double[] formulaDeltaE;
	
	@SerializedName("formula-avgdeltae")
	private Double formulaAvgDeltaE;
	
	@SerializedName("formula-metidx")
	private Double formulaMetidx;
	
	@SerializedName("formula-deltae2")
	private Double formulaDeltaE2;
	
	@SerializedName("formula-oddeltae1")
	private Double formulaOdDeltaE1;
	
	@SerializedName("formula-oddeltae2")
	private Double formulaOdDeltaE2;
	
	@SerializedName("formula-cr1")
	private Double formulaCr1;
	
	@SerializedName("formula-cr2")
	private Double formulaCr2;
	
	@SerializedName("formula-rule")
	private String formulaRule;
	
	@SerializedName("formula-prod-nbr")
	private String formulaProdNbr;
	
	@SerializedName("formula-proc-order")
	private int formulaProcOrder;
	
	@SerializedName("formula-prod-rev")
	private String formulaProdRev;
	
	@SerializedName("formula-clrnt-list")
	private String formulaClrntList;
	
	@SerializedName("formula-de-comment")
	private String formulaDeComment;
	
	@SerializedName("formula-color-eng-ver")
	private String formulaColorEngVer;
	
	@SerializedName("formula-pass-nbr")
	private int formulaPassNbr;
	
	@SerializedName("FormulationWarning")
	private String formulationWarning;
	
	@SerializedName("dbRowiD")
	private String dbRowId;
	
	@SerializedName("deltaeWarning")
	private String deltaEWarning;
	
	/////////////  GETTERS  ////////////////
	
	public String getScRequestId() {
		return scRequestId;
	}
	public String getFormulaType() {
		return formulaType;
	}
	public String getFormulaSourceDescr() {
		return formulaSourceDescr;
	}
	public int getFormulaPct() {
		return formulaPct;
	}
	public String getFormulaFbBase() {
		return formulaFbBase;
	}
	public String getFormulaFbSubBase() {
		return formulaFbSubBase;
	}
	public String[] getClrntTintSysId() {
		return clrntTintSysId;
	}
	public String[] getClrntNm() {
		return clrntNm;
	}
	public int[] getClrntShots() {
		return clrntShots;
	}
	public String[] getClrntIncr1() {
		return clrntIncr1;
	}
	public String[] getClrntIncr2() {
		return clrntIncr2;
	}
	public String[] getClrntIncr3() {
		return clrntIncr3;
	}
	public String[] getClrntIncr4() {
		return clrntIncr4;
	}
	public String[] getClrntQual() {
		return clrntQual;
	}
	public String[] getClrntStatusInd() {
		return clrntStatusInd;
	}
	public String getFormulaExists() {
		return formulaExists;
	}
	public String getFuncStatus() {
		return funcStatus;
	}
	public String[] getClrntCode() {
		return clrntCode;
	}
	public Double[] getProjCurve() {
		return projCurve;
	}
	public Double[] getMeasCurve() {
		return measCurve;
	}
	public Double getFormulaTime() {
		return formulaTime;
	}
	public Double getFormulaCost() {
		return formulaCost;
	}
	public Double getFormulaSpd() {
		return formulaSpd;
	}
	public String[] getFormulaIllum() {
		return formulaIllum;
	}
	public Double[] getFormulaDeltaE() {
		return formulaDeltaE;
	}
	public Double getFormulaAvgDeltaE() {
		return formulaAvgDeltaE;
	}
	public Double getFormulaMetidx() {
		return formulaMetidx;
	}
	public Double getFormulaDeltaE2() {
		return formulaDeltaE2;
	}
	public Double getFormulaOdDeltaE1() {
		return formulaOdDeltaE1;
	}
	public Double getFormulaOdDeltaE2() {
		return formulaOdDeltaE2;
	}
	public Double getFormulaCr1() {
		return formulaCr1;
	}
	public Double getFormulaCr2() {
		return formulaCr2;
	}
	public String getFormulaRule() {
		return formulaRule;
	}
	public String getFormulaProdNbr() {
		return formulaProdNbr;
	}
	public int getFormulaProcOrder() {
		return formulaProcOrder;
	}
	public String getFormulaProdRev() {
		return formulaProdRev;
	}
	public String getFormulaClrntList() {
		return formulaClrntList;
	}
	public String getFormulaDeComment() {
		return formulaDeComment;
	}
	public String getFormulaColorEngVer() {
		return formulaColorEngVer;
	}
	public int getFormulaPassNbr() {
		return formulaPassNbr;
	}
	public String getFormulationWarning() {
		return formulationWarning;
	}
	public String getDbRowId() {
		return dbRowId;
	}
	public String getDeltaEWarning() {
		return deltaEWarning;
	}
	
	///////////////  SETTERS  /////////////////////
	
	public void setScRequestId(String scRequestId) {
		this.scRequestId = scRequestId;
	}
	public void setFormulaType(String formulaType) {
		this.formulaType = formulaType;
	}
	public void setFormulaSourceDescr(String formulaSourceDescr) {
		this.formulaSourceDescr = formulaSourceDescr;
	}
	public void setFormulaPct(int formulaPct) {
		this.formulaPct = formulaPct;
	}
	public void setFormulaFbBase(String formulaFbBase) {
		this.formulaFbBase = formulaFbBase;
	}
	public void setFormulaFbSubBase(String formulaFbSubBase) {
		this.formulaFbSubBase = formulaFbSubBase;
	}
	public void setClrntTintSysId(String[] clrntTintSysId) {
		this.clrntTintSysId = clrntTintSysId;
	}
	public void setClrntNm(String[] clrntNm) {
		this.clrntNm = clrntNm;
	}
	public void setClrntShots(int[] clrntShots) {
		this.clrntShots = clrntShots;
	}
	public void setClrntIncr1(String[] clrntIncr1) {
		this.clrntIncr1 = clrntIncr1;
	}
	public void setClrntIncr2(String[] clrntIncr2) {
		this.clrntIncr2 = clrntIncr2;
	}
	public void setClrntIncr3(String[] clrntIncr3) {
		this.clrntIncr3 = clrntIncr3;
	}
	public void setClrntIncr4(String[] clrntIncr4) {
		this.clrntIncr4 = clrntIncr4;
	}
	public void setClrntQual(String[] clrntQual) {
		this.clrntQual = clrntQual;
	}
	public void setClrntStatusInd(String[] clrntStatusInd) {
		this.clrntStatusInd = clrntStatusInd;
	}
	public void setFormulaExists(String formulaExists) {
		this.formulaExists = formulaExists;
	}
	public void setFuncStatus(String funcStatus) {
		this.funcStatus = funcStatus;
	}
	public void setClrntCode(String[] clrntCode) {
		this.clrntCode = clrntCode;
	}
	public void setProjCurve(Double[] projCurve) {
		this.projCurve = projCurve;
	}
	public void setMeasCurve(Double[] measCurve) {
		this.measCurve = measCurve;
	}
	public void setFormulaTime(Double formulaTime) {
		this.formulaTime = formulaTime;
	}
	public void setFormulaCost(Double formulaCost) {
		this.formulaCost = formulaCost;
	}
	public void setFormulaSpd(Double formulaSpd) {
		this.formulaSpd = formulaSpd;
	}
	public void setFormulaIllum(String[] formulaIllum) {
		this.formulaIllum = formulaIllum;
	}
	public void setFormulaDeltaE(Double[] formulaDeltaE) {
		this.formulaDeltaE = formulaDeltaE;
	}
	public void setFormulaAvgDeltaE(Double formulaAvgDeltaE) {
		this.formulaAvgDeltaE = formulaAvgDeltaE;
	}
	public void setFormulaMetidx(Double formulaMetidx) {
		this.formulaMetidx = formulaMetidx;
	}
	public void setFormulaDeltaE2(Double formulaDeltaE2) {
		this.formulaDeltaE2 = formulaDeltaE2;
	}
	public void setFormulaOdDeltaE1(Double formulaOdDeltaE1) {
		this.formulaOdDeltaE1 = formulaOdDeltaE1;
	}
	public void setFormulaOdDeltaE2(Double formulaOdDeltaE2) {
		this.formulaOdDeltaE2 = formulaOdDeltaE2;
	}
	public void setFormulaCr1(Double formulaCr1) {
		this.formulaCr1 = formulaCr1;
	}
	public void setFormulaCr2(Double formulaCr2) {
		this.formulaCr2 = formulaCr2;
	}
	public void setFormulaRule(String formulaRule) {
		this.formulaRule = formulaRule;
	}
	public void setFormulaProdNbr(String formulaProdNbr) {
		this.formulaProdNbr = formulaProdNbr;
	}
	public void setFormulaProcOrder(int formulaProcOrder) {
		this.formulaProcOrder = formulaProcOrder;
	}
	public void setFormulaProdRev(String formulaProdRev) {
		this.formulaProdRev = formulaProdRev;
	}
	public void setFormulaClrntList(String formulaClrntList) {
		this.formulaClrntList = formulaClrntList;
	}
	public void setFormulaDeComment(String formulaDeComment) {
		this.formulaDeComment = formulaDeComment;
	}
	public void setFormulaColorEngVer(String formulaColorEngVer) {
		this.formulaColorEngVer = formulaColorEngVer;
	}
	public void setFormulaPassNbr(int formulaPassNbr) {
		this.formulaPassNbr = formulaPassNbr;
	}
	public void setFormulationWarning(String formulationWarning) {
		this.formulationWarning = formulationWarning;
	}
	public void setDbRowId(String dbRowId) {
		this.dbRowId = dbRowId;
	}
	public void setDeltaEWarning(String deltaEWarning) {
		this.deltaEWarning = deltaEWarning;
	}
	
	
	

}
