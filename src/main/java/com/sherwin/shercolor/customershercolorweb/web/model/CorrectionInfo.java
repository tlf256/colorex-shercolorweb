package com.sherwin.shercolor.customershercolorweb.web.model;

import java.util.List;

public class CorrectionInfo {

	private List<CorrectionStep> correctionStepList;
	private int nextUnitNbr;
	private int cycle;
	private int lastStep;
	private String corrStatus;
	private int acceptedContNbr;
	private List<DispenseItem> acceptedDispenseList;
	private List<DispenseItem> openDispenseList;
	private int[] skippedCont = {0};
	private int[] discardedCont = {0};
	
	public List<CorrectionStep> getCorrectionStepList() {
		return correctionStepList;
	}
	public int getNextUnitNbr() {
		return nextUnitNbr;
	}
	public int getCycle() {
		return cycle;
	}
	public int getLastStep() {
		return lastStep;
	}
	public String getCorrStatus() {
		return corrStatus;
	}
	public int getAcceptedContNbr() {
		return acceptedContNbr;
	}
	public List<DispenseItem> getAcceptedDispenseList() {
		return acceptedDispenseList;
	}
	public int[] getSkippedCont() {
		return skippedCont;
	}
	public int[] getDiscardedCont() {
		return discardedCont;
	}
	public void setCorrectionStepList(List<CorrectionStep> correctionStepList) {
		this.correctionStepList = correctionStepList;
	}
	public void setNextUnitNbr(int nextUnitNbr) {
		this.nextUnitNbr = nextUnitNbr;
	}
	public void setCycle(int cycle) {
		this.cycle = cycle;
	}
	public void setLastStep(int lastStep) {
		this.lastStep = lastStep;
	}
	public void setCorrStatus(String corrStatus) {
		this.corrStatus = corrStatus;
	}
	public void setAcceptedContNbr(int acceptedContNbr) {
		this.acceptedContNbr = acceptedContNbr;
	}
	public void setAcceptedDispenseList(List<DispenseItem> acceptedDispenseList) {
		this.acceptedDispenseList = acceptedDispenseList;
	}
	public void setSkippedCont(int[] skippedCont) {
		this.skippedCont = skippedCont;
	}
	public void setDiscardedCont(int[] discardedCont) {
		this.discardedCont = discardedCont;
	}
	public List<DispenseItem> getOpenDispenseList() {
		return openDispenseList;
	}
	public void setOpenDispenseList(List<DispenseItem> openDispenseList) {
		this.openDispenseList = openDispenseList;
	}
	

	
	
}
