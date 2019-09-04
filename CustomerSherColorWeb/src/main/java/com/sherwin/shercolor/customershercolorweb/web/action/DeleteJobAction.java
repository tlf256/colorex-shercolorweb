package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CustWebTran;
import com.sherwin.shercolor.common.service.TranHistoryService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.util.domain.SwMessage;

@SuppressWarnings("serial")
public class DeleteJobAction extends ActionSupport implements SessionAware, LoginRequired {
	
	static Logger logger = LogManager.getLogger(DeleteJobAction.class);
	private Map<String, Object> sessionMap;
	private String reqGuid;
	private int controlNbr;
	private String result;
	
	@Autowired
	TranHistoryService tranHistoryService;
	
	public String execute(){
		try {
			
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			
			String customerId = reqObj.getCustomerID();
			CustWebTran custWebTran = tranHistoryService.readTranHistory(customerId, controlNbr, 1);
			custWebTran.setDeleted(true);
			
			List<SwMessage> displayMsgs = new ArrayList<SwMessage>();
			SwMessage msg = tranHistoryService.updateTranHistory(custWebTran);
			if(msg != null) {
				displayMsgs.add(msg);
				reqObj.setDisplayMsgs(displayMsgs);
			}
			
			sessionMap.put(reqGuid, reqObj);
			
			result = "success";
			
			return SUCCESS;
			
		} catch (HibernateException he) {
			logger.error("HibernateException Caught: " + he.toString() + " " + he.getMessage());
			result = "error";
			return ERROR;
		} catch (Exception e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage());
			result = "error";
			return ERROR;
		}
	}
	
	@Override
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

	public int getControlNbr() {
		return controlNbr;
	}

	public void setControlNbr(int controlNbr) {
		this.controlNbr = controlNbr;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
