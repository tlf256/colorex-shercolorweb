package com.sherwin.shercolor.customershercolorweb.web.model;

import java.util.Date;
import java.util.List;


public class TinterInfo {

	private String clrntSysId;
	private String model;
	private String serialNbr;
	private boolean autoNozzleCover;
	private String lastPurgeDate;
	private String lastPurgeUser;
	private boolean ecalOnFile;
	private boolean tinterOnFile;
	private List<String> lastInitErrorList;
	private List<TinterCanister> canisterList;
	
	public String getClrntSysId() {
		return clrntSysId;
	}
	public String getModel() {
		return model;
	}
	public String getSerialNbr() {
		return serialNbr;
	}
	public boolean isAutoNozzleCover() {
		return autoNozzleCover;
	}
	public String getLastPurgeDate() {
		return lastPurgeDate;
	}
	public String getLastPurgeUser() {
		return lastPurgeUser;
	}
	public boolean isEcalOnFile() {
		return ecalOnFile;
	}
	public boolean isTinterOnFile() {
		return tinterOnFile;
	}
	public List<TinterCanister> getCanisterList() {
		return canisterList;
	}
	public List<String> getLastInitErrorList() {
		return lastInitErrorList;
	}
	public void setClrntSysId(String clrntSysId) {
		this.clrntSysId = clrntSysId;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public void setSerialNbr(String serialNbr) {
		this.serialNbr = serialNbr;
	}
	public void setAutoNozzleCover(boolean autoNozzleCover) {
		this.autoNozzleCover = autoNozzleCover;
	}
	public void setCanisterList(List<TinterCanister> canisterList) {
		this.canisterList = canisterList;
	}
	public void setLastPurgeDate(String lastPurgeDate) {
		this.lastPurgeDate = lastPurgeDate;
	}
	public void setLastPurgeUser(String lastPurgeUser) {
		this.lastPurgeUser = lastPurgeUser;
	}
	public void setEcalOnFile(boolean ecalOnFile) {
		this.ecalOnFile = ecalOnFile;
	}
	public void setTinterOnFile(boolean tinterOnFile) {
		this.tinterOnFile = tinterOnFile;
	}
	public void setLastInitErrorList(List<String> lastInitErrorList) {
		this.lastInitErrorList = lastInitErrorList;
	}
	
	
}
