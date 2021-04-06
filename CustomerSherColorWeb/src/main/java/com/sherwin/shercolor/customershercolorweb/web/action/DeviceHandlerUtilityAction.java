package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.SpectroInfo;

public class DeviceHandlerUtilityAction extends ActionSupport  implements SessionAware, LoginRequired {

	private static final long serialVersionUID = 1L;

	static Logger logger = LogManager.getLogger(DeviceHandlerUtilityAction.class.getName());
	private Map<String, Object> sessionMap;
	private String reqGuid;
	private String spectroModel;
	private String spectroSerial;
	
	
	public String display() {
		
		 try {
		     return SUCCESS;
		} catch (Exception e) {
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
