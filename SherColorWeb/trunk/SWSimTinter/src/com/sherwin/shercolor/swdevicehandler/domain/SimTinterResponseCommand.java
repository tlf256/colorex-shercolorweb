package com.sherwin.shercolor.swdevicehandler.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="command")
public class SimTinterResponseCommand {

	private String name;
	private int waitMilliseconds;
	private int errorNumber;
	private String errorMessage;
	List<SimTinterResponseErrorListItem> errorList = new ArrayList<SimTinterResponseErrorListItem>();
	
	
	
	public String getName() {
		return name;
	}
	public int getWaitMilliseconds() {
		return waitMilliseconds;
	}
	public int getErrorNumber() {
		return errorNumber;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public List<SimTinterResponseErrorListItem> getErrorList() {
		return errorList;
	}
	
	
	@XmlElement(name="name")
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement(name="waitmilliseconds")
	public void setWaitMilliseconds(int waitMilliseconds) {
		this.waitMilliseconds = waitMilliseconds;
	}
	
	@XmlElement(name="errornumber")
	public void setErrorNumber(int errorNumber) {
		this.errorNumber = errorNumber;
	}
	
	@XmlElement(name="errormessage")
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@XmlElementWrapper(name="errorlist")
	@XmlElement(name="errorlistitem")
	public void setErrorList(List<SimTinterResponseErrorListItem> errorList) {
		this.errorList = errorList;
	}
	
	
}
