package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;
import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.service.ColorService;
import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class SpectroCalibrateAction extends ActionSupport implements SessionAware, LoginRequired {

	@Autowired
	private ColorService colorService;
	@Autowired
	private CustomerService customerService;
	private transient Map<String, Object> sessionMap;
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(SpectroCalibrateAction.class);
	private String reqGuid;
	private String message;
	private boolean measureColor;
	private boolean measureStandard;
	private boolean measureSample;
	private boolean closestColors;
	private boolean compareColors;
	
	public SpectroCalibrateAction(){
		//default constructor?
		
	}
	
	// User hit the backup button on the Color page
	public String backItUp() {
		try {
			//blank out the color info as we are backing up.
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			reqObj.setColorComp("");
			reqObj.setColorID("");
			reqObj.setColorName("");
			reqObj.setIntBases("");
			reqObj.setExtBases("");
			reqObj.setRgbHex("");
			reqObj.setPrimerId(null);
			reqObj.setColorType("");
			reqObj.setColorVinylOnly(false);
			sessionMap.put(reqGuid, reqObj);
		     return SUCCESS;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String display() {

		 try {
		     return SUCCESS;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = Encode.forHtml(message);
	}

	public void setSession(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;		
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public String getReqGuid() {
		return reqGuid;
	}

	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}

	public ColorService getColorService() {
		return colorService;
	}

	public void setColorService(ColorService colorService) {
		this.colorService = colorService;
	}

	public boolean isMeasureColor() {
		return measureColor;
	}

	public void setMeasureColor(boolean measureColor) {
		this.measureColor = measureColor;
	}

	public boolean isMeasureSample() {
		return measureSample;
	}

	public void setMeasureSample(boolean measureSample) {
		this.measureSample = measureSample;
	}

	public boolean isMeasureStandard() {
		return measureStandard;
	}

	public void setMeasureStandard(boolean measureStandard) {
		this.measureStandard = measureStandard;
	}

	public boolean isClosestColors() {
		return closestColors;
	}

	public void setClosestColors(boolean closestColors) {
		this.closestColors = closestColors;
	}

	public boolean isCompareColors() {
		return compareColors;
	}

	public void setCompareColors(boolean compareColors) {
		this.compareColors = compareColors;
	}

}

