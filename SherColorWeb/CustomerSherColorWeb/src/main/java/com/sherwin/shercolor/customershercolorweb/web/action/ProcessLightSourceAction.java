package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

public class ProcessLightSourceAction  extends ActionSupport implements SessionAware, LoginRequired  {

	private Map<String, Object> sessionMap;
	
	private Map<String, String> lightSources;
	
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(ProcessLightSourceAction.class);
	
	private String reqGuid;
	
	private String selectedLightSources;
	
	public ProcessLightSourceAction(){
		
		lightSources = new HashMap<String, String>();
		lightSources.put("A","INCANDESCENT");
		lightSources.put("D65","DAYLIGHT");
		lightSources.put("F2","FLUORESCENT");
		
	}
	

	
	//return default light source value
	public String getDefaultLightSourceValue(){
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		if (reqObj.getIntExt().toUpperCase().equals("INTERIOR")) {
			return "A";
		}
		//For EXTERIOR, INT/EXT, or something else, return Daylight.
		return "D65";
	}
	
	public String display() {
		 try {
			 //check and see if we even need to display this screen.  Only needs to be displayed for competitive and custom
			 RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			 if (reqObj.getColorType().equals("SHERWIN-WILLIAMS") || reqObj.getColorType().equalsIgnoreCase("CUSTOM")) {
					reqObj.setLightSource("D65");
					sessionMap.put(reqGuid, reqObj);
					 return SUCCESS;
			 }
			 
		 
			 return INPUT;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ERROR;
		}
		 
	}
	
	public String execute() {
		 try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			reqObj.setLightSource(selectedLightSources);
			sessionMap.put(reqGuid, reqObj);
			return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ERROR;
		}
		 
	}

	// User hit the backup button on the light source page
	public String backItUp() {
		try {

			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			//reset colorant system and vinyl safe setting.
			reqObj.setClrntSys("");
			reqObj.setVinylExclude(false);
			reqObj.setLightSource("");
			sessionMap.put(reqGuid, reqObj);
		    return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
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
	
	public String getSelectedLightSources() {
		return selectedLightSources;
	}

	public void setSelectedLightSources(String selectedLightSources) {
		this.selectedLightSources = selectedLightSources;
	}

	public Map<String, String> getLightSources() {
		return lightSources;
	}

	public void setLightSources(Map<String, String> lightSources) {
		this.lightSources = lightSources;
	}


}
