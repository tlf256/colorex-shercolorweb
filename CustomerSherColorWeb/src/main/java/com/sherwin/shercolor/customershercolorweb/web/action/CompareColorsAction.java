package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.service.ColorMastService;

public class CompareColorsAction extends ActionSupport implements SessionAware, LoginRequired {
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(CompareColorsAction.class);
	private Map<String, Object> sessionMap;
	private String reqGuid;
	private Map<String, String> sourceOptions;
	private String SW;
	private String COMPETITIVE;
	private String CUSTOMMATCH;
	private String MEASURE;
	private List<String> colorCompanies;
	
	@Autowired
	private ColorMastService colorMastService;
	
	public String display() {
		try {
			
			buildSourceOptionsMap();
			buildCompaniesList();
			
			return SUCCESS;
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public void buildSourceOptionsMap() {
		sourceOptions = new LinkedHashMap<String, String>();
		
		SW = "Sherwin-Williams Color"; //getText("");
		COMPETITIVE = "Competitive Color"; //getText("");
		CUSTOMMATCH = "Existing Custom Match"; //getText("");
		MEASURE = "Measure Color"; //getText("processColorAction.savedCi62Measurement");
		
		sourceOptions.put("SW", SW);
		sourceOptions.put("COMPET",COMPETITIVE);
		sourceOptions.put("CUSTOMMATCH", CUSTOMMATCH);
		sourceOptions.put("MEASURE", MEASURE);
	}
	
	public void buildCompaniesList() {
		colorCompanies = new ArrayList<String>();
		
		colorCompanies.add(getText("processColorAction.all")); 
		 
		String [] colorCompaniesArray = colorMastService.listColorCompanies(false);
		for (String company : colorCompaniesArray) {
			if (!company.equals("SHERWIN-WILLIAMS")){
				colorCompanies.add(company);
			}
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

	public Map<String, String> getSourceOptions() {
		return sourceOptions;
	}

	public void setSourceOptions(Map<String, String> sourceOptions) {
		this.sourceOptions = sourceOptions;
	}

	public List<String> getColorCompanies() {
		return colorCompanies;
	}

	public void setColorCompanies(List<String> colorCompanies) {
		this.colorCompanies = colorCompanies;
	}
}
