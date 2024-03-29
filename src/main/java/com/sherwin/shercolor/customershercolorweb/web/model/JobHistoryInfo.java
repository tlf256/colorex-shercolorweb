package com.sherwin.shercolor.customershercolorweb.web.model;

import java.util.Date;
import java.util.List;

import com.sherwin.shercolor.common.domain.FormulaIngredient;

/*
 * Author:  BMW
 * Date:	1/25/2019
 * Desc:	Model for LookupJobAction to pass around information to
 * 			to the dataTable in DisplayJobs.jsp 
 */

public class JobHistoryInfo{
	private String clrntSysId;
	private int controlNbr;
	private String colorId;
	private String colorName;
	private String colorNotes;
	private String rgbHex;
	private String prodNbr;
	private int quantityDispensed;
	private int quantityOrdered;
	private int numberOfColorants;
	private List<JobField> jobFieldList;
	private List<FormulaIngredient> recipe;
	private String formulaDisplay;
	private String sizeCode;
	private String canType;
	private Date jobCreationDate;
	
	public String getClrntSysId() {
		return clrntSysId;
	}
	public void setClrntSysId(String clrntSysId) {
		this.clrntSysId = clrntSysId;
	}
	public int getControlNbr() {
		return controlNbr;
	}
	public void setControlNbr(int controlNbr) {
		this.controlNbr = controlNbr;
	}
	public String getColorId() {
		return colorId;
	}
	public void setColorId(String colorId) {
		this.colorId = colorId;
	}
	public String getColorName() {
		return colorName;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	public String getRgbHex() {
		return rgbHex;
	}
	public void setRgbHex(String rgbHex) {
		this.rgbHex = rgbHex;
	}
	public String getProdNbr() {
		return prodNbr;
	}
	public void setProdNbr(String prodNbr) {
		this.prodNbr = prodNbr;
	}
	public int getQuantityDispensed() {
		return quantityDispensed;
	}
	public void setQuantityDispensed(int quantityDispensed) {
		this.quantityDispensed = quantityDispensed;
	}
	public int getQuantityOrdered() {
		return quantityOrdered;
	}
	public void setQuantityOrdered(int quantityOrdered) {
		this.quantityOrdered = quantityOrdered;
	}
	public int getNumberOfColorants() {
		return numberOfColorants;
	}
	public void setNumberOfColorants(int numberOfColorants) {
		this.numberOfColorants = numberOfColorants;
	}
	public List<JobField> getJobFieldList() {
		return jobFieldList;
	}
	public void setJobFieldList(List<JobField> jobFieldList) {
		this.jobFieldList = jobFieldList;
	}
	public List<FormulaIngredient> getRecipe() {
		return recipe;
	}
	public void setRecipe(List<FormulaIngredient> recipe) {
		this.recipe = recipe;
	}
	public void setSizeCode(String sizeCode) {
		this.sizeCode = sizeCode;
	}
	public String getSizeCode() {
		return sizeCode;
	}

	public String getFormulaDisplay() {
		return formulaDisplay;
	}
	public void setFormulaDisplay(String formulaDisplay) {
		this.formulaDisplay = formulaDisplay;
	}
	public String getCanType() {
		return canType;
	}
	public void setCanType(String canType) {
		this.canType = canType;
	}
	public Date getJobCreationDate() {
		return jobCreationDate;
	}
	public void setJobCreationDate(Date jobCreationDate) {
		this.jobCreationDate = jobCreationDate;
	}
	public String getColorNotes() {
		return colorNotes;
	}
	public void setColorNotes(String colorNotes) {
		this.colorNotes = colorNotes;
	}
}