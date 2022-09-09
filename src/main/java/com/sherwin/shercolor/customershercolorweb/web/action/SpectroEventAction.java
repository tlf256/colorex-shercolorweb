package com.sherwin.shercolor.customershercolorweb.web.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CustWebSpectroEvents;
import com.sherwin.shercolor.common.service.SpectroService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.SpectroInfo;
import com.sherwin.shercolor.util.domain.SwMessage;

public class SpectroEventAction extends ActionSupport implements SessionAware, LoginRequired {
	
	@Autowired
	SpectroService spectroService;
	
	static Logger logger = LogManager.getLogger(SpectroEventAction.class.getName());
	private Map<String, Object> sessionMap;
	private String reqGuid;
	
	private CustWebSpectroEvents spectroEvent = new CustWebSpectroEvents();
	
	private Map<String,Object> spectroMessage;
	
	private String responseDate;
	private String requestDate;
	
	private String spectroModel;
	private String spectroSerialNbr;
	private String spectroPort;
	RequestObject reqObj;
	
	@Override
	public String execute(){
		String retVal=null;
		
		try {
			reqObj = (RequestObject) sessionMap.get(reqGuid);
			
			if(reqObj == null) {
				logger.error("Session expired");
				return ERROR;
			}
			spectroEvent.setCustomerId(reqObj.getCustomerID());
			retVal = processSpectroEvent();
			
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
		return retVal;
	}
	
	private void getItemsFromSpectroConfig() {
		
		if(spectroMessage.get("spectroConfig")!=null) {
			String spectroConfig = spectroMessage.get("spectroConfig").toString();
			String configString = StringUtils.substringBetween(spectroConfig, "{", "}");
			String[] config = configString.split(",");
			
			spectroEvent.setSpectroSerialNbr(config[0].substring(7));
			spectroEvent.setSpectroPort(config[1].substring(6));
			spectroEvent.setSpectroModel(config[2].substring(7));
			
		}
		
		if(spectroEvent.getSpectroCommand().equals("ReadConfig")) {
			reqObj.setSpectroModel(spectroEvent.getSpectroModel());
			reqObj.setSpectroSerialNbr(spectroEvent.getSpectroSerialNbr());
		}
	}
	
	private String processSpectroEvent() {
		SimpleDateFormat jsdf = new SimpleDateFormat("EE MMM d y H:m:s 'GMT'Z (zz)");
		Date requestTime; 
		Date responseTime;
		// Taking data from SpectroMessage map and pair it to the SpectroEvents object
		// Catch will generate backup time stamps just in case a failure occurs
		try {
			requestTime = jsdf.parse(requestDate);
		} catch (Exception e) {
			requestTime = new Date();
		}
		try {
			responseTime = jsdf.parse(responseDate);
		} catch (Exception e) {
			responseTime = new Date();
		}
		
		spectroEvent.setRequestTime(requestTime);
		spectroEvent.setResponseTime(responseTime);
		if(spectroMessage.get("model")!=null) spectroEvent.setSpectroModel(spectroMessage.get("model").toString());
		if(spectroMessage.get("command")!=null) spectroEvent.setSpectroCommand(spectroMessage.get("command").toString());
		if(spectroMessage.get("responseMessage")!=null) spectroEvent.setResponseMsg(spectroMessage.get("responseMessage").toString());
		if(spectroMessage.get("rc")!=null) spectroEvent.setResponseCode(Integer.parseInt(spectroMessage.get("rc").toString()));
		if(spectroMessage.get("errorCode")!=null) spectroEvent.setErrorCode(Integer.parseInt(spectroMessage.get("errorCode").toString()));
		if(spectroMessage.get("errorMessage")!=null) spectroEvent.setErrorMsg(spectroMessage.get("errorMessage").toString());
		if(spectroMessage.get("deltaE")!=null) spectroEvent.setDeltaE(Integer.parseInt(spectroMessage.get("deltaE").toString()));
		getItemsFromSpectroConfig();
		
		List<SwMessage> errorList = spectroService.writeSpectroEvent(spectroEvent);
		sessionMap.put(reqGuid, reqObj);
		if (!errorList.isEmpty()) {
			return ERROR;
		}
		return SUCCESS;
	}
	
	@Override
	public void setSession(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}
	
	public String getReqGuid() {
		return reqGuid;
	}

	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}
	
	public void setSpectroMessage(Map<String, Object> spectroMessage) {
		this.spectroMessage = spectroMessage;
	}
	
	public Map<String, Object> getSpectroMessage() {
		return spectroMessage;
	}

	public String getResponseDate() {
		return responseDate;
	}

	public void setResponseDate(String responseDate) {
		this.responseDate = responseDate;
	}

	public String getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(String requestDate) {
		this.requestDate = requestDate;
	}
	
}
