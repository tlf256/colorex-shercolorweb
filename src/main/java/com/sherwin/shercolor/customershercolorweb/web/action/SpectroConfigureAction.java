package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;

import com.opensymphony.xwork2.ActionSupport;

import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.SpectroInfo;
import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.common.domain.CustWebDevices;
import com.sherwin.shercolor.util.domain.SwMessage;

public class SpectroConfigureAction extends ActionSupport implements SessionAware, LoginRequired {

	private Map<String, Object> sessionMap;
	private CustomerService customerService;
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(SpectroConfigureAction.class);

	private String reqGuid;
	private String message;
	
	private static final String CI52 = "Ci52+SWS";
	private static final String CI62 = "Ci62";
	private static final String CI52SWW = "Ci52+SWW";
	private static final String CI62SWW = "Ci62+SWW";

	private Map<String, String> spectrotypes;
	private String selectedSpectroType;
	private boolean siteHasSpectro;
	private SpectroInfo spectro;
	
	//return company type list for radio options
	public Map<String, String> getSpectrotypes() {
		return spectrotypes;
	}
	
	//return default company value
	public String getDefaultSpectroTypeValue(){
		return "Ci52+SWS";
	}
	
	public SpectroConfigureAction(){
		spectrotypes = new LinkedHashMap<String, String>();
		
		spectrotypes.put("Ci52+SWS",CI52);
		spectrotypes.put("Ci62",CI62);
		spectrotypes.put("Ci52+SWW",CI52SWW);
		spectrotypes.put("Ci62+SWW",CI62SWW);
	}

	// User hit the backup button on the Color page
	public String backItUp() {
		try {
			//blank out the color info as we are backing up.
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);

			sessionMap.put(reqGuid, reqObj);
		     return SUCCESS;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String display() {
		 RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		int custId;

		 try {
				try {
					custId = Integer.valueOf(reqObj.getCustomerID());
				} catch (Exception e) {
					//we couldn't convert to integer, so it's definitely not an id that could
					//be an internal user.
					custId = 0;
				}
				
				//Only include these internal types if the customer ID is an "internal" customer ID
				//(400000000-400000016, or six digit starting with 99, ie 990001, etc.)
				if (custId < 990001 || (custId > 999999 && custId < 400000000) || custId > 400000016) {
				//if ((custId > 400000000 && custId < 400000017) || (custId > 990000 && custId < 999999) ) {
					spectrotypes.remove("Ci52+SWS",CI52);
					spectrotypes.remove("Ci62",CI62);
				}
		     return SUCCESS;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String saveNewSpectro(){
		CustWebDevices custWebDev = new CustWebDevices();
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		logger.debug("Begin Saving CustWebDevice.");
		try {
			if(reqObj !=null) {
				logger.debug("request object getSpectro getMmodel is " + reqObj.getSpectro().getModel());
				logger.debug("request object  getSpectroMmodel is " + reqObj.getSpectroModel());
				custWebDev.setCustomerId(reqObj.getCustomerID());
				custWebDev.setSerialNbr(reqObj.getSpectro().getSerialNbr());
				custWebDev.setDeviceModel(reqObj.getSpectro().getModel());
				custWebDev.setDeviceType("SPECTRO");
			}

				if(custWebDev.getCustomerId()!=null && custWebDev.getSerialNbr() != null
						&& custWebDev.getDeviceModel() != null){
					logger.debug("Saving CUSTWEBDEVICES Model num is: " + custWebDev.getDeviceModel() + " Serial num is:" + custWebDev.getSerialNbr());
					SwMessage returnMessage = customerService.saveCustDevice(custWebDev);
					logger.debug("back from saveCustDevice without error");
					if(returnMessage!=null){
						//save if new customer id otherwise do nothing.  If error return error
						addActionError(getText("global.configNotComplete"));
						return ERROR;
					}
					logger.debug("made it past returnMessage check");
					siteHasSpectro = true;
					spectro = new SpectroInfo();
					spectro.setSerialNbr(custWebDev.getSerialNbr());
					spectro.setModel(custWebDev.getDeviceModel());
					logger.debug("set the spectro object");
				}
				else {
					addActionError(getText("spectroConfigureAction.invalidColorEyeData"));
					return ERROR;
				}
		} catch(RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}

		return SUCCESS;
	}
	
	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
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

	public String getReqGuid() {
		return reqGuid;
	}

	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}

	public String getSelectedSpectroType() {
		return selectedSpectroType;
	}

	public void setSelectedSpectroType(String selectedSpectroType) {
		this.selectedSpectroType = Encode.forHtml(selectedSpectroType);
	}
	
	public boolean isSiteHasSpectro() {
		return siteHasSpectro;
	}

	public SpectroInfo getSpectro() {
		return spectro;
	}
}