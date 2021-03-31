package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.colormath.domain.ColorCoordinates;
import com.sherwin.shercolor.colormath.functions.ColorCoordinatesCalculator;
import com.sherwin.shercolor.common.domain.CustWebTran;
import com.sherwin.shercolor.common.service.TranHistoryService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

public class CompareColorsCustomMatchAction extends ActionSupport implements SessionAware, LoginRequired {
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(CompareColorsCustomMatchAction.class);
	private Map<String, Object> sessionMap;
	private String reqGuid;
	private int lookupControlNbr;
	
	@Autowired
	private TranHistoryService tranHistoryService;
	
	@Autowired
	private ColorCoordinatesCalculator colorCoordCalc;
	
	public String execute() {
		try {
			//setFilter(false);
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			Map<String, ColorCoordinates> coordMap = new HashMap<String, ColorCoordinates>();
			
			String customerId = reqObj.getCustomerID();
			
			CustWebTran webTran = tranHistoryService.readTranHistory(customerId, lookupControlNbr, 1);
			
			ColorCoordinates colorCoord = getColorCoordinates(webTran);
			
			coordMap.put("standard", colorCoord);
			
			reqObj.setColorCoordMap(coordMap);
			
			sessionMap.put(reqGuid, reqObj);
			
			return SUCCESS;
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	private ColorCoordinates getColorCoordinates(CustWebTran cwt) {
		ColorCoordinates colorCoord = new ColorCoordinates();
		
		colorCoord = colorCoordCalc.getColorCoordinates(cwt.getMeasCurve());
		
		return colorCoord;
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
	
	public int getLookupControlNbr() {
		return lookupControlNbr;
	}

	public void setLookupControlNbr(int lookupControlNbr) {
		this.lookupControlNbr = lookupControlNbr;
	}

}
