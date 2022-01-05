package com.sherwin.shercolor.customershercolorweb.web.action;


import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;

import com.opensymphony.xwork2.ActionSupport;

import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.common.domain.CustWebDevices;
import com.sherwin.shercolor.util.domain.SwMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PrinterConfigureAction extends ActionSupport implements SessionAware, LoginRequired {

	private Map<String, Object> sessionMap;
	@Autowired
	private CustomerService customerService;
	private static final long serialVersionUID = 1L;
	static Logger logger = LoggerFactory.getLogger(PrinterConfigureAction.class);

	private String reqGuid;
	private String message;
	
	

	
	private boolean siteHasPrinter;
	private String printerModel;
	

	
	
	private void setIsPrinterConfigured() {
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		logger.debug("Looking for printer devices");

		List<CustWebDevices> devices = customerService.getCustDevices(reqObj.getCustomerID());
		for (CustWebDevices d: devices) {
		
			if(d.getDeviceType().equalsIgnoreCase("PRINTER")) {
				reqObj.setPrinterConfigured(true);
				setSiteHasPrinter(true);
				setPrinterModel(d.getDeviceModel());
				logger.debug("Device found for " + reqObj.getCustomerID() + " - " + d.getDeviceType());
			}
			else {
				setSiteHasPrinter(false);
			}
		}

	}

	public String display() {
		try {
			setIsPrinterConfigured();
		} catch(RuntimeException e) {
			logger.error(e.getMessage(), e);
		}
		
		return SUCCESS;

	}
	
	public String saveNewPrinter(){
		try {
			CustWebDevices custWebDev = new CustWebDevices();
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			logger.debug("Begin Saving CustWebDevice.");
			
			if(reqObj !=null) {
				//.out.println("request object getPrinter getMmodel is " + reqObj.getSpectro().getModel());
				//logger.debug("request object  getPrinterMmodel is " + reqObj.getPrinterModel());
				custWebDev.setCustomerId(reqObj.getCustomerID());
				custWebDev.setSerialNbr("Default");//reqObj.getSpectro().getSerialNbr());
				custWebDev.setDeviceModel(getPrinterModel());//reqObj.getSpectro().getModel());
				custWebDev.setDeviceType("PRINTER");
				reqObj.setPrinterConfigured(true);
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
					setSiteHasPrinter(true);

				}
				else {
					addActionError("Printer Model is blank");
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