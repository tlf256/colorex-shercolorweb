package com.sherwin.shercolor.customershercolorweb.web.model;

public class autoComplete {
	private String label;
	private String value;
	
	public autoComplete(String label, String value) {
		this.label = label;
		this.value = value;
	}
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
}
