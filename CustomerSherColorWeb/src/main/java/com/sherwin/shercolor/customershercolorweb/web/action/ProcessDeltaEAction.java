package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

import com.sherwin.shercolor.common.domain.FormulaInfo;

import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;


public class ProcessDeltaEAction extends ActionSupport implements SessionAware, LoginRequired {
	private Map<String, Object> sessionMap;

	static Logger logger = LogManager.getLogger(ProcessDeltaEAction.class);
	private String reqGuid;
	private FormulaInfo displayFormula;
	
	private static final long serialVersionUID = 1L;
	public String executeYes() {
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			reqObj.getDisplayFormula().setDeltaEWarning("");
			displayFormula = reqObj.getDisplayFormula();
			sessionMap.put(reqGuid, reqObj);
			
			return SUCCESS;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String executeNo() {			
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			reqObj.setClrntSys("");
			sessionMap.put(reqGuid, reqObj);
			
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

	public FormulaInfo getDisplayFormula() {
		return displayFormula;
	}

	public void setDisplayFormula(FormulaInfo displayFormula) {
		this.displayFormula = displayFormula;
	}

}
