package com.sherwin.shercolor.customershercolorweb.web.model;

import org.owasp.encoder.Encode;

public class JobField {

	private String screenLabel;
	private String enteredValue;
	private String requiredText;
	
	public String getScreenLabel() {
		return screenLabel;
	}
	public String getEnteredValue() {
		return enteredValue;
	}
	public String getRequiredText() {
		return requiredText;
	}
	public void setScreenLabel(String screenLabel) {
		this.screenLabel = screenLabel;
	}
	public void setEnteredValue(String enteredValue) {
		if (enteredValue!=null) {
		this.enteredValue = Encode.forHtml(enteredValue);
		} else {
			this.enteredValue = enteredValue;
		}
	}
	public void setRequiredText(String requiredText) {
		this.requiredText = requiredText;
	}
	
	
}
