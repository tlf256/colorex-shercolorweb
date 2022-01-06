package com.sherwin.shercolor.shercolorweb.web.model;

import java.util.List;

public class ManualIngredient {

	private List<String> availColorants;
	private String selectedColorant;
	private List<String> increments;
	
	
	public List<String> getAvailColorants() {
		return availColorants;
	}
	public String getSelectedColorant() {
		return selectedColorant;
	}
	public List<String> getIncrements() {
		return increments;
	}
	
	////////////////////   SETTERS  //////////////////////////////
	
	public void setAvailColorants(List<String> availColorants) {
		this.availColorants = availColorants;
	}
	public void setSelectedColorant(String selectedColorant) {
		this.selectedColorant = selectedColorant;
	}
	public void setIncrements(List<String> increments) {
		this.increments = increments;
	}
	
	
	
	
}
