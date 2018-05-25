package com.sherwin.shercolor.common.domain;

import java.util.ArrayList;
import java.util.List;

import com.sherwin.shercolor.common.domain.FormulaIngredient;

public class FormulaInfo implements Cloneable{
	private String guid;
	private String colorComp;
	private String colorId;
	private String prodComp;
	private String salesNbr;
	private String clrntSysId;
	private String clrntSysName;
	private List<String> incrementHdr = new ArrayList<String>();
	private List<FormulaIngredient> ingredients = new ArrayList<FormulaIngredient>();
	private Double[] projectedCurve;
	private Double[] measuredCurve;
	private Double formulationTime;
	private Double colorantCost;
	private Double spd;
	private Double[] deltaEs;
	private Double averageDeltaE;
	private Double metamerismIndex;
	private Double deltaEThin;
	private Double deltaEOverDarkThick;
	private Double deltaEOverDarkThin;
	private Double contrastRatioThick;
	private Double contrastRatioThin;
	private String rule;
	private String deltaEComment;
	private String colorEngineVersion;
	private String oeRequestId; // OpenEdge Formulation RequestId
	private String source;
	private String sourceDescr;
	private String formulaExists;
	private String funcStatus;
	private String[] illums;
	private String prodNbr;
	private String sizeCode;
	private int procOrder;
	private String prodRev;
	private String clrntList;
	private int enginePassNbr;
	private String formulationWarning;
	private String deltaEWarning;
	private String fbBase;
	private String fbSubBase;
	private int percent;
	
	public Object clone(){
		FormulaInfo copyFormInfo = null;
		try {
			copyFormInfo = (FormulaInfo) super.clone();
		} catch (CloneNotSupportedException e){ 
			throw new InternalError(e.toString());
		}
		List<FormulaIngredient> copyIngredients = new ArrayList<FormulaIngredient>();
		for(FormulaIngredient orig : ingredients) copyIngredients.add((FormulaIngredient) orig.clone());
		copyFormInfo.setIngredients(copyIngredients);
		return copyFormInfo;
	}

	public String getGuid() {
		return guid;
	}
	public String getColorComp() {
		return colorComp;
	}
	public String getColorId() {
		return colorId;
	}
	public String getProdComp() {
		return prodComp;
	}
	public String getSalesNbr() {
		return salesNbr;
	}
	public String getClrntSysId() {
		return clrntSysId;
	}
	public String getClrntSysName() {
		return clrntSysName;
	}
	public List<String> getIncrementHdr() {
		return incrementHdr;
	}
	public List<FormulaIngredient> getIngredients() {
		return ingredients;
	}
	public Double[] getProjectedCurve() {
		return projectedCurve;
	}
	public Double[] getMeasuredCurve() {
		return measuredCurve;
	}
	public Double getFormulationTime() {
		return formulationTime;
	}
	public Double getColorantCost() {
		return colorantCost;
	}
	public Double getSpd() {
		return spd;
	}
	public Double[] getDeltaEs() {
		return deltaEs;
	}
	public Double getAverageDeltaE() {
		return averageDeltaE;
	}
	public Double getMetamerismIndex() {
		return metamerismIndex;
	}
	public Double getDeltaEThin() {
		return deltaEThin;
	}
	public Double getDeltaEOverDarkThick() {
		return deltaEOverDarkThick;
	}
	public Double getDeltaEOverDarkThin() {
		return deltaEOverDarkThin;
	}
	public Double getContrastRatioThick() {
		return contrastRatioThick;
	}
	public Double getContrastRatioThin() {
		return contrastRatioThin;
	}
	public String getRule() {
		return rule;
	}
	public String getDeltaEComment() {
		return deltaEComment;
	}
	public String getColorEngineVersion() {
		return colorEngineVersion;
	}
	public String getOeRequestId() {
		return oeRequestId;
	}
	public String getSource() {
		return source;
	}
	public String getSourceDescr() {
		return sourceDescr;
	}
	public String getFormulaExists() {
		return formulaExists;
	}
	public String getFuncStatus() {
		return funcStatus;
	}
	public String[] getIllums() {
		return illums;
	}
	public String getProdNbr() {
		return prodNbr;
	}
	public String getSizeCode() {
		return sizeCode;
	}
	public int getProcOrder() {
		return procOrder;
	}
	public String getProdRev() {
		return prodRev;
	}
	public String getClrntList() {
		return clrntList;
	}
	public int getEnginePassNbr() {
		return enginePassNbr;
	}
	public String getFormulationWarning() {
		return formulationWarning;
	}
	public String getDeltaEWarning() {
		return deltaEWarning;
	}
	public String getFbBase() {
		return fbBase;
	}
	public String getFbSubBase() {
		return fbSubBase;
	}
	public int getPercent() {
		return percent;
	}
	
	///////////////////////// SETTERS ////////////////////////////////////
	
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public void setColorComp(String colorComp) {
		this.colorComp = colorComp;
	}
	public void setColorId(String colorId) {
		this.colorId = colorId;
	}
	public void setProdComp(String prodComp) {
		this.prodComp = prodComp;
	}
	public void setSalesNbr(String salesNbr) {
		this.salesNbr = salesNbr;
	}
	public void setClrntSysId(String clrntSysId) {
		this.clrntSysId = clrntSysId;
	}
	public void setClrntSysName(String clrntSysName) {
		this.clrntSysName = clrntSysName;
	}
	public void setIncrementHdr(List<String> incrementHdr) {
		this.incrementHdr = incrementHdr;
	}
	public void setIngredients(List<FormulaIngredient> ingredients) {
		this.ingredients = ingredients;
	}
	public void setProjectedCurve(Double[] projectedCurve) {
		this.projectedCurve = projectedCurve;
	}
	public void setMeasuredCurve(Double[] measuredCurve) {
		this.measuredCurve = measuredCurve;
	}
	public void setFormulationTime(Double formulationTime) {
		this.formulationTime = formulationTime;
	}
	public void setColorantCost(Double colorantCost) {
		this.colorantCost = colorantCost;
	}
	public void setSpd(Double spd) {
		this.spd = spd;
	}
	public void setDeltaEs(Double[] deltaEs) {
		this.deltaEs = deltaEs;
	}
	public void setAverageDeltaE(Double averageDeltaE) {
		this.averageDeltaE = averageDeltaE;
	}
	public void setMetamerismIndex(Double metamerismIndex) {
		this.metamerismIndex = metamerismIndex;
	}
	public void setDeltaEThin(Double deltaEThin) {
		this.deltaEThin = deltaEThin;
	}
	public void setDeltaEOverDarkThick(Double deltaEOverDarkThick) {
		this.deltaEOverDarkThick = deltaEOverDarkThick;
	}
	public void setDeltaEOverDarkThin(Double deltaEOverDarkThin) {
		this.deltaEOverDarkThin = deltaEOverDarkThin;
	}
	public void setContrastRatioThick(Double contrastRatioThick) {
		this.contrastRatioThick = contrastRatioThick;
	}
	public void setContrastRatioThin(Double contrastRatioThin) {
		this.contrastRatioThin = contrastRatioThin;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public void setDeltaEComment(String deltaEComment) {
		this.deltaEComment = deltaEComment;
	}
	public void setColorEngineVersion(String colorEngineVersion) {
		this.colorEngineVersion = colorEngineVersion;
	}
	public void setOeRequestId(String oeRequestId) {
		this.oeRequestId = oeRequestId;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public void setSourceDescr(String sourceDescr) {
		this.sourceDescr = sourceDescr;
	}
	public void setFormulaExists(String formulaExists) {
		this.formulaExists = formulaExists;
	}
	public void setFuncStatus(String funcStatus) {
		this.funcStatus = funcStatus;
	}
	public void setIllums(String[] illums) {
		this.illums = illums;
	}
	public void setProdNbr(String prodNbr) {
		this.prodNbr = prodNbr;
	}
	public void setSizeCode(String sizeCode) {
		this.sizeCode = sizeCode;
	}
	public void setProcOrder(int procOrder) {
		this.procOrder = procOrder;
	}
	public void setProdRev(String prodRev) {
		this.prodRev = prodRev;
	}
	public void setClrntList(String clrntList) {
		this.clrntList = clrntList;
	}
	public void setEnginePassNbr(int enginePassNbr) {
		this.enginePassNbr = enginePassNbr;
	}
	public void setFormulationWarning(String formulationWarning) {
		this.formulationWarning = formulationWarning;
	}
	public void setDeltaEWarning(String deltaEWarning) {
		this.deltaEWarning = deltaEWarning;
	}
	public void setFbBase(String fbBase) {
		this.fbBase = fbBase;
	}
	public void setFbSubBase(String fbSubBase) {
		this.fbSubBase = fbSubBase;
	}
	public void setPercent(int percent) {
		this.percent = percent;
	}
	
}
