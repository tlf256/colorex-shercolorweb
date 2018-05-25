package com.sherwin.shercolor.util.domain;
import org.apache.logging.log4j.Level;

public class SwMessage {
	private Level severity;
	private String code;
	private String message;
	
	public SwMessage() {
	}
	
	public SwMessage(Level severity, String cd, String msg) {
		this.severity = severity;
		this.code = cd;
		this.message = msg;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Level getSeverity() {
		return severity;
	}
	public void setSeverity(Level severity) {
		this.severity = severity;
	}
}
