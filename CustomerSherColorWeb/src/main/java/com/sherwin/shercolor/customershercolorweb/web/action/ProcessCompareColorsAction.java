package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.colormath.domain.ColorDifference;
import com.sherwin.shercolor.common.service.ColorService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import org.springframework.stereotype.Component;

@Component
public class ProcessCompareColorsAction extends ActionSupport implements SessionAware, LoginRequired {
	private static final long serialVersionUID = 1L;
	static Logger logger = LoggerFactory.getLogger(ProcessCompareColorsAction.class);
	private Map<String, Object> sessionMap;
	private String reqGuid;
	private boolean compare;
	private Map<String, Double> compareResults;
	private ColorDifference colorDiff;
	
	@Autowired
	private ColorService colorService;
	
	public String display() {
		try {
			setCompare(true);
			
			return SUCCESS;
		} catch(RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String execute() {
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			compareResults = new HashMap<String, Double>();
			
			setColorDiff(colorService.getColorDifference(reqObj.getColorCoordMap().get("standard"), reqObj.getColorCoordMap().get("sample")));
			
			return SUCCESS;
		} catch(RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
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

	public boolean isCompare() {
		return compare;
	}

	public void setCompare(boolean compare) {
		this.compare = compare;
	}

	public Map<String, Double> getCompareResults() {
		return compareResults;
	}

	public void setCompareResults(Map<String, Double> compareResults) {
		this.compareResults = compareResults;
	}

	public ColorDifference getColorDiff() {
		return colorDiff;
	}

	public void setColorDiff(ColorDifference colorDiff) {
		this.colorDiff = colorDiff;
	}
}
