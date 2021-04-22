package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CustWebColorantsTxt;
import com.sherwin.shercolor.common.domain.CustWebTinterEvents;
import com.sherwin.shercolor.common.domain.CustWebTinterEventsDetail;
import com.sherwin.shercolor.common.service.TinterService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterCanister;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterInfo;
import com.sherwin.shercolor.util.domain.SwMessage;

public class RemoveTinterAction extends ActionSupport implements SessionAware, LoginRequired {
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(RemoveTinterAction.class);
	private Map<String, Object> sessionMap;
	private String reqGuid;
	
	@Autowired
	TinterService tinterService;
	
	public String execute() {
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			List<CustWebColorantsTxt> colorantsTxtList = new ArrayList<CustWebColorantsTxt>();
			
			for(TinterCanister tinterCanister : reqObj.getTinter().getCanisterList()) {
				CustWebColorantsTxt custWebcolorantsTxt = new CustWebColorantsTxt();
				custWebcolorantsTxt.setCustomerId(reqObj.getCustomerID());
				custWebcolorantsTxt.setTinterSerialNbr(reqObj.getTinter().getSerialNbr());
				custWebcolorantsTxt.setTinterModel(reqObj.getTinter().getModel());
				custWebcolorantsTxt.setClrntSysId(reqObj.getTinter().getClrntSysId());
				custWebcolorantsTxt.setClrntCode(tinterCanister.getClrntCode());
				custWebcolorantsTxt.setPosition(tinterCanister.getPosition());
				custWebcolorantsTxt.setMaxCanisterFill(tinterCanister.getMaxCanisterFill());
				custWebcolorantsTxt.setFillAlarmLevel(tinterCanister.getFillAlarmLevel());
				custWebcolorantsTxt.setFillStopLevel(tinterCanister.getFillStopLevel());
				custWebcolorantsTxt.setCurrentClrntAmount(tinterCanister.getCurrentClrntAmount());
				colorantsTxtList.add(custWebcolorantsTxt);
			}
			
			logger.info("Removing " + reqObj.getTinter().getModel() + " tinter");
			
			boolean result = tinterService.deleteColorantsTxt(colorantsTxtList);
			
			if(result) {
				logger.info("Tinter removal successful");
			} else {
				logger.info("Tinter removal unsuccessful");
				
				// log tinter event
				List<SwMessage> logResult = createConfigTinterEvent(reqObj);
				
				if(logResult != null && logResult.size() > 0) {
					logger.error("Error logging Config tinter event. Error code: " + logResult.get(0).getCode() +
							" error message: " + logResult.get(0).getMessage());
				}
			}
			
			// reset session TinterInfo
			TinterInfo tinterInfo = new TinterInfo();
			reqObj.setTinter(tinterInfo);
			
			return SUCCESS;
			
		} catch(RuntimeException e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage(), e);
			return ERROR;
		}
	}
	
	private List<SwMessage> createConfigTinterEvent(RequestObject reqObj) {
		CustWebTinterEvents cwte = new CustWebTinterEvents();
		List<CustWebTinterEventsDetail> cwteList = new ArrayList<CustWebTinterEventsDetail>();
		
		Date currDate = new Date();
		
		cwte.setGuid(reqObj.getGuid());
		cwte.setCustomerId(reqObj.getCustomerID());
		cwte.setClrntSysId(reqObj.getTinter().getClrntSysId());
		cwte.setTinterModel(reqObj.getTinter().getModel());
		cwte.setTinterSerialNbr(reqObj.getTinter().getSerialNbr());
		cwte.setDateTime(currDate);
		cwte.setFunction("Configure");
		cwte.setAppVersion("");
		cwte.setTinterDriverVersion("");
		cwte.setEventDetails("");
		cwte.setErrorStatus("1");
		cwte.setErrorSeverity("0");
		cwte.setErrorNumber("-1");
		cwte.setErrorMessage(getText("removeTinterAction.couldNotDeleteTinter", new String[] {reqObj.getTinter().getModel(), reqObj.getTinter().getSerialNbr()}));
		
		List<SwMessage> writeErrorList = tinterService.writeTinterEventAndDetail(cwte, cwteList);
		
		return writeErrorList;
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
