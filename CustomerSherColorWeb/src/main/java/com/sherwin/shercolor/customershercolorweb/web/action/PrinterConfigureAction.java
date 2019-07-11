package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.LinkedHashMap;
import java.util.List;
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

public class PrinterConfigureAction extends ActionSupport implements SessionAware, LoginRequired {

	private Map<String, Object> sessionMap;
	private CustomerService customerService;
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(PrinterConfigureAction.class);

	private String reqGuid;
	private String message;
	
	

	
	private boolean siteHasPrinter;
	
	
	
	
	//return default company value
	public String getDefaultPrinterTypeValue(){
		return "Zebra GK420d";
	}
	
	/*
	public PrinterConfigureAction(){
		spectrotypes = new LinkedHashMap<String, String>();
		
		spectrotypes.put("Ci52+SWS",CI52);
		spectrotypes.put("Ci62",CI62);
		spectrotypes.put("Ci52+SWW",CI52SWW);
		spectrotypes.put("Ci62+SWW",CI62SWW);
	}
	*/

	// User hit the backup button on the Color page
	public String backItUp() {
		try {
			//blank out the color info as we are backing up.
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);

			sessionMap.put(reqGuid, reqObj);
		     return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
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
					
				}
		     return SUCCESS;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ERROR;
		}
	}
	
	public String saveNewPrinter(){
		CustWebDevices custWebDev = new CustWebDevices();
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		System.out.println("Begin Saving CustWebDevice.");
		
		if(reqObj !=null) {
			//.out.println("request object getPrinter getMmodel is " + reqObj.getSpectro().getModel());
			System.out.println("request object  getPrinterMmodel is " + reqObj.getPrinterModel());
			custWebDev.setCustomerId(reqObj.getCustomerID());
			custWebDev.setSerialNbr("1234");//reqObj.getSpectro().getSerialNbr());
			custWebDev.setDeviceModel("Zebra");//reqObj.getSpectro().getModel());
			custWebDev.setDeviceType("PRINTER");
		}

			if(custWebDev.getCustomerId()!=null && custWebDev.getSerialNbr() != null
					&& custWebDev.getDeviceModel() != null){
				System.out.println("Saving CUSTWEBDEVICES Model num is: " + custWebDev.getDeviceModel() + " Serial num is:" + custWebDev.getSerialNbr());
				SwMessage returnMessage = customerService.saveCustDevice(custWebDev);
				System.out.println("back from saveCustDevice without error");
				if(returnMessage!=null){
					//save if new customer id otherwise do nothing.  If error return error
					addActionError("Config not complete.  Critical DB error saving color eye device data.  Please try again.  Call support if this persists.");
					return ERROR;
				}
				System.out.println("made it past returnMessage check");
				setSiteHasPrinter(true);
/*				spectro = new SpectroInfo();
				spectro.setSerialNbr(custWebDev.getSerialNbr());
				spectro.setModel(custWebDev.getDeviceModel());
*/				System.out.println("set the spectro object");
			}
			else {
				addActionError("Invalid Color Eye data - customer, model type or serial number is black");
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

	

	public boolean isSiteHasPrinter() {
		return siteHasPrinter;
	}

	public void setSiteHasPrinter(boolean siteHasPrinter) {
		this.siteHasPrinter = siteHasPrinter;
	}
	
	
}