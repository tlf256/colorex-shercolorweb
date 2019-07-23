package com.sherwin.shercolor.customershercolorweb.web.action;


import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;

import com.opensymphony.xwork2.ActionSupport;

import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

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
	private String printerModel;
	

	
	
	private void setIsPrinterConfigured() {
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		System.out.println("Looking for printer devices");

		List<CustWebDevices> devices = customerService.getCustDevices(reqObj.getCustomerID());
		for (CustWebDevices d: devices) {
		
			if(d.getDeviceType().equalsIgnoreCase("PRINTER")) {
				reqObj.setPrinterConfigured(true);
				setSiteHasPrinter(true);
				setPrinterModel(d.getDeviceModel());
				System.out.println("Device found for " + reqObj.getCustomerID() + " - " + d.getDeviceType());
			}
			else {
				setSiteHasPrinter(false);
			}
		}

	}

	public String display() {
		setIsPrinterConfigured();
		return SUCCESS;

	}
	
	public String saveNewPrinter(){
		CustWebDevices custWebDev = new CustWebDevices();
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		System.out.println("Begin Saving CustWebDevice.");
		
		if(reqObj !=null) {
			//.out.println("request object getPrinter getMmodel is " + reqObj.getSpectro().getModel());
			//System.out.println("request object  getPrinterMmodel is " + reqObj.getPrinterModel());
			custWebDev.setCustomerId(reqObj.getCustomerID());
			custWebDev.setSerialNbr("Default");//reqObj.getSpectro().getSerialNbr());
			custWebDev.setDeviceModel(getPrinterModel());//reqObj.getSpectro().getModel());
			custWebDev.setDeviceType("PRINTER");
			reqObj.setPrinterConfigured(true);
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

			}
			else {
				addActionError("Printer Model is blank");
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

	public String getPrinterModel() {
		return printerModel;
	}

	public void setPrinterModel(String printerModel) {
		this.printerModel = printerModel;
	}
	
	
}