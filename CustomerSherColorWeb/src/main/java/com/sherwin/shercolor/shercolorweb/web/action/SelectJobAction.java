package com.sherwin.shercolor.shercolorweb.web.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import org.springframework.stereotype.Component;


@SuppressWarnings("serial")
@Component
public class SelectJobAction extends ActionSupport  implements SessionAware, LoginRequired  {

	static Logger logger = LoggerFactory.getLogger(SelectJobAction.class);
	private Map<String, Object> sessionMap;
	private String reqGuid;
	
	public String lookupJob() {
		
		 try {
		     return SUCCESS;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String reset() {
		
		 try {
		     return SUCCESS;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String startNewJob() {
		
		 try {
		     return SUCCESS;
		} catch (RuntimeException e) {
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
