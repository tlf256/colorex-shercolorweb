package com.sherwin.shercolor.customershercolorweb.web.model;

import com.sherwin.shercolor.common.model.FormulaIngredient;

import java.util.Date;
import java.util.List;



public class CorrectionStep {
	private int cycle;
	private int unitNbr;
	private int step;
	private String reason;
	private String status;
	private String userId;
	private Date dateTime;
	private String jsDateTime;
	private String corrMethod;
	private boolean mergedWithOrig;
	private String clrntSysId;
	List<FormulaIngredient> ingredients;
	
	public int getCycle() {
		return cycle;
	}
	public int getUnitNbr() {
		return unitNbr;
	}
	public int getStep() {
		return step;
	}
	public String getReason() {
		return reason;
	}
	public String getStatus() {
		return status;
	}
	public String getUserId() {
		return userId;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public String getJsDateTime() {
		return jsDateTime;
	}
	public String getCorrMethod() {
		return corrMethod;
	}
	public boolean isMergedWithOrig() {
		return mergedWithOrig;
	}
	public String getClrntSysId() {
		return clrntSysId;
	}
	public List<FormulaIngredient> getIngredients() {
		return ingredients;
	}
	public void setCycle(int cycle) {
		this.cycle = cycle;
	}
	public void setUnitNbr(int unitNbr) {
		this.unitNbr = unitNbr;
	}
	public void setStep(int step) {
		this.step = step;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public void setJsDateTime(String jsDateTime) {
		this.jsDateTime = jsDateTime;
	}
	public void setCorrMethod(String corrMethod) {
		this.corrMethod = corrMethod;
	}
	public void setMergedWithOrig(boolean mergedWithOrig) {
		this.mergedWithOrig = mergedWithOrig;
	}
	public void setClrntSysId(String clrntSysId) {
		this.clrntSysId = clrntSysId;
	}
	public void setIngredients(List<FormulaIngredient> ingredients) {
		this.ingredients = ingredients;
	}

	
}
