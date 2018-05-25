package com.sherwin.shercolor.swdevicehandler.domain;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="errorlistitem")
public class SimTinterResponseErrorListItem {

	long code;
	String message;
	
	
	public long getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}

	@XmlElement(name="code")
	public void setCode(long code) {
		this.code = code;
	}
	@XmlElement(name="message")
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
