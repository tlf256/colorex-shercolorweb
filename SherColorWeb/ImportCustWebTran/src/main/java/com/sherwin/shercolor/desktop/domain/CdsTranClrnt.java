package com.sherwin.shercolor.desktop.domain;

import java.util.Date;

public class CdsTranClrnt {
	private int controlNbr;
	private int lineNbr;
	private int tranDetSeq;
	private int tranClrntSeq;
	private String clrntSys;
	private String clrntCode;
	private int clrntAmt;
	private String clrntQual;
	private boolean needExtracted;
	private String cdsAdlFld;
	private int mqExtractedState;
	private Date mqSentDate;
	
	public int getControlNbr() {
		return controlNbr;
	}
	public int getLineNbr() {
		return lineNbr;
	}
	public int getTranDetSeq() {
		return tranDetSeq;
	}
	public int getTranClrntSeq() {
		return tranClrntSeq;
	}
	public String getClrntSys() {
		return clrntSys;
	}
	public String getClrntCode() {
		return clrntCode;
	}
	public int getClrntAmt() {
		return clrntAmt;
	}
	public String getClrntQual() {
		return clrntQual;
	}
	public boolean isNeedExtracted() {
		return needExtracted;
	}
	public String getCdsAdlFld() {
		return cdsAdlFld;
	}
	public int getMqExtractedState() {
		return mqExtractedState;
	}
	public Date getMqSentDate() {
		return mqSentDate;
	}
	
	///////////////////    SETTERS     ////////////////////////
	
	public void setControlNbr(int controlNbr) {
		this.controlNbr = controlNbr;
	}
	public void setLineNbr(int lineNbr) {
		this.lineNbr = lineNbr;
	}
	public void setTranDetSeq(int tranDetSeq) {
		this.tranDetSeq = tranDetSeq;
	}
	public void setTranClrntSeq(int tranClrntSeq) {
		this.tranClrntSeq = tranClrntSeq;
	}
	public void setClrntSys(String clrntSys) {
		this.clrntSys = clrntSys;
	}
	public void setClrntCode(String clrntCode) {
		this.clrntCode = clrntCode;
	}
	public void setClrntAmt(int clrntAmt) {
		this.clrntAmt = clrntAmt;
	}
	public void setClrntQual(String clrntQual) {
		this.clrntQual = clrntQual;
	}
	public void setNeedExtracted(boolean needExtracted) {
		this.needExtracted = needExtracted;
	}
	public void setCdsAdlFld(String cdsAdlFld) {
		this.cdsAdlFld = cdsAdlFld;
	}
	public void setMqExtractedState(int mqExtractedState) {
		this.mqExtractedState = mqExtractedState;
	}
	public void setMqSentDate(Date mqSentDate) {
		this.mqSentDate = mqSentDate;
	}
	

}
