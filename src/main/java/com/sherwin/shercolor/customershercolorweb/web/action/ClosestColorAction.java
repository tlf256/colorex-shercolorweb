package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class ClosestColorAction extends ActionSupport implements SessionAware, LoginRequired {
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(ClosestColorAction.class);
	private Map<String, Object> sessionMap;
	private String reqGuid;
	private boolean measure;
	
	public String display() {
		//redirect to coloreye measurement
		setMeasure(true);
		return SUCCESS;
	}
	
	public void setSession(Map<String, Object> sessionMap) {
		this.setSessionMap(sessionMap);		
	}

	public Map<String, Object> getSessionMap() {
		return sessionMap;
	}

	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}

	public String getReqGuid() {
		return reqGuid;
	}

	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}
	
	public boolean isMeasure() {
		return measure;
	}

	public void setMeasure(boolean measure) {
		this.measure = measure;
	}
}
