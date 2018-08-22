package com.sherwin.shercolor.desktop.domain;

import java.util.Date;

public class CdsTranMast {
	private int controlNbr;
	private int lineNbr;
	private String salesNbr;
	private String colorComp;
	private String colorId;
	private String colorName;
	private String prefBase;
	private String actBase;
	private String spectroModel;
	private String spectroMode;
	private String tranStatus;
	private String prodSource;
	private int qtyComplete;
	private int qtyInProg;
	private boolean needExtracted;
	private String cdsAdlField;
	private int qtyMistint;
	private int qtyRecon;
	private boolean colorAutoRetrieve;
	private String origColorId;
	private String origColorComp;
	private int qtyNosale;
	private int qtyNosaleRecon;
	private String cFld1;
	private String cFld2;
	private String cFld3;
	private String cFld4;
	private String cFld5;
	private String cFld6;
	private String cFld7;
	private String cFld8;
	private String cFld9;
	private String cFld10;
	private String activeStatus;
	private String prodRev;
	private String clrntSysSuffix;
	private String tintableSalesNbr;
	private String locId;
	private Integer mqExtractedState;
	private Date mqSentDate;
	private String roomUse;
	private String customFormulaScope;

	public int getControlNbr() {
		return controlNbr;
	}
	public int getLineNbr() {
		return lineNbr;
	}
	public String getSalesNbr() {
		return salesNbr;
	}
	public String getColorComp() {
		return colorComp;
	}
	public String getColorId() {
		return colorId;
	}
	public String getColorName() {
		return colorName;
	}
	public String getPrefBase() {
		return prefBase;
	}
	public String getActBase() {
		return actBase;
	}
	public String getSpectroModel() {
		return spectroModel;
	}
	public String getSpectroMode() {
		return spectroMode;
	}
	public String getTranStatus() {
		return tranStatus;
	}
	public String getProdSource() {
		return prodSource;
	}
	public int getQtyComplete() {
		return qtyComplete;
	}
	public int getQtyInProg() {
		return qtyInProg;
	}
	public boolean isNeedExtracted() {
		return needExtracted;
	}
	public String getCdsAdlField() {
		return cdsAdlField;
	}
	public int getQtyMistint() {
		return qtyMistint;
	}
	public int getQtyRecon() {
		return qtyRecon;
	}
	public boolean isColorAutoRetrieve() {
		return colorAutoRetrieve;
	}
	public String getOrigColorId() {
		return origColorId;
	}
	public String getOrigColorComp() {
		return origColorComp;
	}
	public int getQtyNosale() {
		return qtyNosale;
	}
	public int getQtyNosaleRecon() {
		return qtyNosaleRecon;
	}
	public String getcFld1() {
		return cFld1;
	}
	public String getcFld2() {
		return cFld2;
	}
	public String getcFld3() {
		return cFld3;
	}
	public String getcFld4() {
		return cFld4;
	}
	public String getcFld5() {
		return cFld5;
	}
	public String getcFld6() {
		return cFld6;
	}
	public String getcFld7() {
		return cFld7;
	}
	public String getcFld8() {
		return cFld8;
	}
	public String getcFld9() {
		return cFld9;
	}
	public String getcFld10() {
		return cFld10;
	}
	public String getActiveStatus() {
		return activeStatus;
	}
	public String getProdRev() {
		return prodRev;
	}
	public String getClrntSysSuffix() {
		return clrntSysSuffix;
	}
	public String getTintableSalesNbr() {
		return tintableSalesNbr;
	}
	public String getLocId() {
		return locId;
	}
	public Integer getMqExtractedState() {
		return mqExtractedState;
	}
	public Date getMqSentDate() {
		return mqSentDate;
	}
	public String getRoomUse() {
		return roomUse;
	}
	public String getCustomFormulaScope() {
		return customFormulaScope;
	}
	
	/////////////////    SETTERS    /////////////////////
	
	public void setControlNbr(int controlNbr) {
		this.controlNbr = controlNbr;
	}
	public void setLineNbr(int lineNbr) {
		this.lineNbr = lineNbr;
	}
	public void setSalesNbr(String salesNbr) {
		this.salesNbr = salesNbr;
	}
	public void setColorComp(String colorComp) {
		this.colorComp = colorComp;
	}
	public void setColorId(String colorId) {
		this.colorId = colorId;
	}
	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	public void setPrefBase(String prefBase) {
		this.prefBase = prefBase;
	}
	public void setActBase(String actBase) {
		this.actBase = actBase;
	}
	public void setSpectroModel(String spectroModel) {
		this.spectroModel = spectroModel;
	}
	public void setSpectroMode(String spectroMode) {
		this.spectroMode = spectroMode;
	}
	public void setTranStatus(String tranStatus) {
		this.tranStatus = tranStatus;
	}
	public void setProdSource(String prodSource) {
		this.prodSource = prodSource;
	}
	public void setQtyComplete(int qtyComplete) {
		this.qtyComplete = qtyComplete;
	}
	public void setQtyInProg(int qtyInProg) {
		this.qtyInProg = qtyInProg;
	}
	public void setNeedExtracted(boolean needExtracted) {
		this.needExtracted = needExtracted;
	}
	public void setCdsAdlField(String cdsAdlField) {
		this.cdsAdlField = cdsAdlField;
	}
	public void setQtyMistint(int qtyMistint) {
		this.qtyMistint = qtyMistint;
	}
	public void setQtyRecon(int qtyRecon) {
		this.qtyRecon = qtyRecon;
	}
	public void setColorAutoRetrieve(boolean colorAutoRetrieve) {
		this.colorAutoRetrieve = colorAutoRetrieve;
	}
	public void setOrigColorId(String origColorId) {
		this.origColorId = origColorId;
	}
	public void setOrigColorComp(String origColorComp) {
		this.origColorComp = origColorComp;
	}
	public void setQtyNosale(int qtyNosale) {
		this.qtyNosale = qtyNosale;
	}
	public void setQtyNosaleRecon(int qtyNosaleRecon) {
		this.qtyNosaleRecon = qtyNosaleRecon;
	}
	public void setcFld1(String cFld1) {
		this.cFld1 = cFld1;
	}
	public void setcFld2(String cFld2) {
		this.cFld2 = cFld2;
	}
	public void setcFld3(String cFld3) {
		this.cFld3 = cFld3;
	}
	public void setcFld4(String cFld4) {
		this.cFld4 = cFld4;
	}
	public void setcFld5(String cFld5) {
		this.cFld5 = cFld5;
	}
	public void setcFld6(String cFld6) {
		this.cFld6 = cFld6;
	}
	public void setcFld7(String cFld7) {
		this.cFld7 = cFld7;
	}
	public void setcFld8(String cFld8) {
		this.cFld8 = cFld8;
	}
	public void setcFld9(String cFld9) {
		this.cFld9 = cFld9;
	}
	public void setcFld10(String cFld10) {
		this.cFld10 = cFld10;
	}
	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}
	public void setProdRev(String prodRev) {
		this.prodRev = prodRev;
	}
	public void setClrntSysSuffix(String clrntSysSuffix) {
		this.clrntSysSuffix = clrntSysSuffix;
	}
	public void setTintableSalesNbr(String tintableSalesNbr) {
		this.tintableSalesNbr = tintableSalesNbr;
	}
	public void setLocId(String locId) {
		this.locId = locId;
	}
	public void setMqExtractedState(Integer mqExtractedState) {
		this.mqExtractedState = mqExtractedState;
	}
	public void setMqSentDate(Date mqSentDate) {
		this.mqSentDate = mqSentDate;
	}
	public void setRoomUse(String roomUse) {
		this.roomUse = roomUse;
	}
	public void setCustomFormulaScope(String customFormulaScope) {
		this.customFormulaScope = customFormulaScope;
	}
	

}
