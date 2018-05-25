package com.sherwin.shercolor.common.domain;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class OeFormRespDataSet {

	@SerializedName("cdsColorantTbl")
	private ArrayList<OeFormRespFormula> formRespClrntRow;

	@SerializedName("Messages")
	private ArrayList<OeFormRespMessage> messages;
	
	
	public ArrayList<OeFormRespMessage> getMessages() {
		return messages;
	}

	public void setMessages(ArrayList<OeFormRespMessage> messages) {
		this.messages = messages;
	}


	public ArrayList<OeFormRespFormula> getFormRespClrntRow() {
		return formRespClrntRow;
	}

	public void setFormRespClrntRow(ArrayList<OeFormRespFormula> formRespClrntRow) {
		this.formRespClrntRow = formRespClrntRow;
	}
	
	
}
