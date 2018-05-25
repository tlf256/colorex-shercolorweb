package com.sherwin.shercolor.common.domain;

import java.util.List;

import com.sherwin.shercolor.util.domain.SwMessage;

public class FormulationResponse {
	private String status;
	private List<SwMessage> messages;
	private List<FormulaInfo> formulas;
	
	public String getStatus() {
		return status;
	}
	public List<SwMessage> getMessages() {
		return messages;
	}
	public List<FormulaInfo> getFormulas() {
		return formulas;
	}
	
	////////////////  SETTERS  /////////////////////////
	
	public void setStatus(String status) {
		this.status = status;
	}
	public void setMessages(List<SwMessage> messages) {
		this.messages = messages;
	}
	public void setFormulas(List<FormulaInfo> formulas) {
		this.formulas = formulas;
	}
	
	

}
