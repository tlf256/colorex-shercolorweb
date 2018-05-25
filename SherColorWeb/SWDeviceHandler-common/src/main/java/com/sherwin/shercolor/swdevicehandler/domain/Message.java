package com.sherwin.shercolor.swdevicehandler.domain;

import java.util.Locale;

public class Message {
	String id; 
	String messageName;
	Locale locale;
	 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMessageName() {
		return messageName;
	}
	public void setMessageName(String messageName) {
		this.messageName = messageName;
	}
	
	public Locale getLocale() {
		return locale;
	}
	
	public void setLocale(Locale theLocale) {
		this.locale = theLocale;
	}
	
}
