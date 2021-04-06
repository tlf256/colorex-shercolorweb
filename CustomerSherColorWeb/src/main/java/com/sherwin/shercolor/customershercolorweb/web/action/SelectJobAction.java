package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;


@SuppressWarnings("serial")
public class SelectJobAction extends ActionSupport  implements SessionAware, LoginRequired  {

	static Logger logger = LogManager.getLogger(SelectJobAction.class);
	private Map<String, Object> sessionMap;
	private String reqGuid;
	
	public String lookupJob() {
		
		 try {
		     return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String reset() {
		
		 try {
		     return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String startNewJob() {
		
		 try {
		     return SUCCESS;
		} catch (Exception e) {
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
}
