package com.sherwin.shercolor.shercolorweb.web.action;

import java.util.Map;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import org.springframework.stereotype.Component;

@Component
public class DeviceHandlerUtilityAction extends ActionSupport  implements SessionAware, LoginRequired {

	private static final long serialVersionUID = 1L;

	static Logger logger = LoggerFactory.getLogger(DeviceHandlerUtilityAction.class.getName());
	private Map<String, Object> sessionMap;
	private String reqGuid;
	private String spectroModel;
	private String spectroSerial;
	
	
	public String display() {
		
		 try {
		     return SUCCESS;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	public void setSession(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;		
	}

	public String getReqGuid() {
		return reqGuid;
	}


	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}


	
}
