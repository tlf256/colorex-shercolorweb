package com.sherwin.shercolor.customerprofilesetup.web.dto;

public class JobFields {

	private int seqNbr;
	private String fieldDefault;
	private boolean entryRequired;
	private boolean active;
	private String screenLabel;
	
	public String getFieldDefault() {
		return fieldDefault;
	}
	public void setFieldDefault(String fieldDefault) {
		if(fieldDefault == null) {
			this.fieldDefault = "";
		} else {
			this.fieldDefault = fieldDefault;
		}
	}
	public int getSeqNbr() {
		return seqNbr;
	}
	public void setSeqNbr(int seqNbr) {
		this.seqNbr = seqNbr;
	}
	public boolean isEntryRequired() {
		return entryRequired;
	}
	public void setEntryRequired(boolean entryRequired) {
		this.entryRequired = entryRequired;
	}
	public String getScreenLabel() {
		return screenLabel;
	}
	public void setScreenLabel(String screenLabel) {
		this.screenLabel = screenLabel;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	
}
