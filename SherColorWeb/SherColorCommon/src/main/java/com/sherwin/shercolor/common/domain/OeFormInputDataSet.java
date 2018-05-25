package com.sherwin.shercolor.common.domain;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class OeFormInputDataSet {

	@SerializedName("ServiceReq")
	private ArrayList<OeFormInputRequest> formInputRequest;
	
	@SerializedName("cdsParms")
	private ArrayList<OeFormInputParms> formInputParms;
	
	@SerializedName("cdsColorantTbl")
	private ArrayList<OeFormInputColorants> currentIngredients;

	public ArrayList<OeFormInputRequest> getFormInputRequest() {
		return formInputRequest;
	}

	public ArrayList<OeFormInputParms> getFormInputParms() {
		return formInputParms;
	}

	public ArrayList<OeFormInputColorants> getCurrentIngredients() {
		return currentIngredients;
	}

	public void setFormInputRequest(ArrayList<OeFormInputRequest> formInputRequest) {
		this.formInputRequest = formInputRequest;
	}

	public void setFormInputParms(ArrayList<OeFormInputParms> formInputParms) {
		this.formInputParms = formInputParms;
	}

	public void setCurrentIngredients(ArrayList<OeFormInputColorants> currentIngredients) {
		this.currentIngredients = currentIngredients;
	}


}
