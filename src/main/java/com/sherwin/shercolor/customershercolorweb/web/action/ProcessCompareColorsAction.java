package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.colormath.domain.ColorDifference;
import com.sherwin.shercolor.common.service.ColorService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

public class ProcessCompareColorsAction extends ActionSupport implements SessionAware, LoginRequired {
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(ProcessCompareColorsAction.class);
	private transient Map<String, Object> sessionMap;
	private String reqGuid;
	private boolean measureSample;
	private transient ColorDifference colorDiff;
	private boolean compareColors;
	private String colorId;
	private String rgbHexStd;
	private String rgbHexTrl;
	private String colorName;
	private String colorComp;
	
	@Autowired
	private transient ColorService colorService;
	
	public String display() {
		try {
			setMeasureSample(true);
			return SUCCESS;
		} catch(RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	@Override
	public String execute() {
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			
			setColorDiff(colorService.getColorDifference(reqObj.getColorCoordMap().get("standard"), reqObj.getColorCoordMap().get("sample")));
			rgbHexStd = reqObj.getColorCoordMap().get("standard").getRgbHex();
			rgbHexTrl = reqObj.getColorCoordMap().get("sample").getRgbHex();

			setColorComp(reqObj.getColorComp());
			setColorName(reqObj.getColorName());
			setColorId(reqObj.getColorID());
			
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

	public boolean isMeasureSample() {
		return measureSample;
	}

	public void setMeasureSample(boolean measureSample) {
		this.measureSample = measureSample;
	}

	public ColorDifference getColorDiff() {
		return colorDiff;
	}

	public void setColorDiff(ColorDifference colorDiff) {
		this.colorDiff = colorDiff;
	}

	public boolean isCompareColors() {
		return compareColors;
	}

	public void setCompareColors(boolean compareColors) {
		this.compareColors = compareColors;
	}
	
	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}
	
	public String getColorComp() {
		return colorComp;
	}

	public void setColorComp(String colorComp) {
		this.colorComp = colorComp;
	}
	
	public String getRgbHexTrl() {
		return rgbHexTrl;
	}

	public void setRgbHexTrl(String rgbHexTrl) {
		this.rgbHexTrl = rgbHexTrl;
	}
	
	public String getRgbHexStd() {
		return rgbHexStd;
	}

	public void setRgbHexStd(String rgbHexStd) {
		this.rgbHexStd = rgbHexStd;
	}
	
	public String getColorId() {
		return colorId;
	}

	public void setColorId(String colorId) {
		this.colorId = colorId;
	}
	
}
