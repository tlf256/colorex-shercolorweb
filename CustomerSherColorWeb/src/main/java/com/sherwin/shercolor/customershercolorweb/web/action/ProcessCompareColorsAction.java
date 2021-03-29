package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class ProcessCompareColorsAction extends ActionSupport implements SessionAware, LoginRequired {
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(ProcessCompareColorsAction.class);
	private Map<String, Object> sessionMap;
	private String reqGuid;
	private boolean compare;
	
	public String display() {
		try {
			setCompare(true);
			
			return SUCCESS;
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String execute() {
		try {
			
			
			return SUCCESS;
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
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

	public boolean isCompare() {
		return compare;
	}

	public void setCompare(boolean compare) {
		this.compare = compare;
	}
}
