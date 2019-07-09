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
	private int lookupControlNbr;
	
	@Autowired
	TranHistoryService tranHistoryService;
	
	public String execute(){
		try {
			
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			
			String customerId = reqObj.getCustomerID();
			CustWebTran custWebTran = tranHistoryService.readTranHistory(customerId, lookupControlNbr, 1);
			custWebTran.setDeleted(true);
			
			List<SwMessage> displayMsgs = new ArrayList<SwMessage>();
			SwMessage msg = tranHistoryService.updateTranHistory(custWebTran);
			displayMsgs.add(msg);
			reqObj.setDisplayMsgs(displayMsgs);
			
			sessionMap.put(reqGuid, reqObj);
			
			return SUCCESS;
			
		} catch (HibernateException he) {
			logger.error("HibernateException Caught: " + he.toString() + " " + he.getMessage());
			return ERROR;
		} catch (Exception e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage());
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

	public int getLookupControlNbr() {
		return lookupControlNbr;
	}

	public void setLookupControlNbr(int lookupControlNbr) {
		this.lookupControlNbr = lookupControlNbr;
	}

}
