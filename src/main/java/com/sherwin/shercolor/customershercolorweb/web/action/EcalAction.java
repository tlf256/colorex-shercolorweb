package com.sherwin.shercolor.customershercolorweb.web.action;


import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import com.sherwin.shercolor.common.domain.CustWebEcal;
import com.sherwin.shercolor.common.service.EcalService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterInfo;

@Component
public class EcalAction extends ActionSupport implements SessionAware, LoginRequired{
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	private final static Logger logger = LogManager.getLogger(EcalAction.class);

	@Autowired
	EcalService service;

	private Map<String, Object> sessionMap;
	private String reqGuid=null;
	
	String customerid=null;
	String colorantid=null;
	String tintermodel=null;
	String tinterserial=null; 
	String uploaddate=null; 
	String uploadtime=null;
	String filename=null;
	byte[] data=null;
	
	List<CustWebEcal> ecalList=null;
	
	private TinterInfo tinter;
	
	public String GetEcalTemplate(){
		try {
			setEcalList(service.getEcalTemplate(colorantid, tintermodel));
			return SUCCESS;
		} catch(RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	public String GetEcalsByCustomer(){
		try {
			//we call this function from the ProcessConfig right now.
			setEcalList(service.getEcalsByCustomer(customerid));
			return SUCCESS;
		} catch(RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	public String GetECalList(){
		try {
			setEcalList(service.getEcalList(customerid,colorantid,tintermodel,tinterserial));
			return SUCCESS;
		} catch(RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String SelectEcal(){
		try {
			logger.debug(Encode.forJava("selecting ecal for " + filename));
			CustWebEcal cal = service.selectEcal(filename);	
			if(cal != null){			
				setData(cal.getData());
			}
			return SUCCESS;
		} catch(RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	public String SelectGData(){
		try {
			logger.debug(Encode.forJava("selecting gdata for " + colorantid));
			CustWebEcal cal = service.selectGData(colorantid);	
			if(cal != null){			
				setData(cal.getData());
			}
			return SUCCESS;
		} catch(RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String UploadEcal(){
		try {
			logger.debug("inside EcalAction");
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			logger.debug("inside EcalAction, got reqObj, now getting tinter");
			tinter = reqObj.getTinter();
			logger.debug("inside Ecal, got tinter, returning success");

			//TODO get last Config date info to be shown on screen...

			// setup tinter stuff if needed
			if(tinter==null) logger.error("inside ProcessConfigAction_display and tinter object is null");
			
			CustWebEcal ecal = new CustWebEcal();
			ecal.setCustomerid(reqObj.getCustomerID());
			ecal.setColorantid(tinter.getClrntSysId());
			ecal.setTintermodel(tinter.getModel());
			ecal.setTinterserial(tinter.getSerialNbr());
			ecal.setUploaddate(this.getUploaddate());
			ecal.setUploadtime(this.getUploadtime());
			ecal.setFilename(this.getFilename());
			ecal.setData(this.getData());
			if(this.getData()!=null){
				logger.info("Upload Ecal size=" + this.getData().length);
			}
			else{
				logger.error("Upload Ecal data array is null");
				return ERROR;
			}
			service.uploadEcal(ecal);
			return SUCCESS;
		} catch(RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}

	public String display(){
		try {
			logger.debug("inside EcalAction");
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			logger.debug("inside EcalAction, got reqObj, now getting tinter");
			tinter = reqObj.getTinter();
			logger.debug("inside Ecal, got tinter, returning success");

			//TODO get last Config date info to be shown on screen...

			// setup tinter stuff if needed
			if(tinter==null) logger.error("inside EcalAction_display and tinter object is null");
			
			//get List of Ecals
			if(reqObj.getCustomerID()!=null && tinter !=null){
				setEcalList(service.getEcalList(reqObj.getCustomerID(),tinter.getClrntSysId(),tinter.getModel(),tinter.getSerialNbr()));		
			}
			else if(reqObj.getCustomerID()!=null){
				
				this.setEcalList(service.getEcalsByCustomer(reqObj.getCustomerID()));
			}
			else{
				String customerID="TEST";
				this.setEcalList(service.getEcalsByCustomer(customerID));
			}
			//get List of Template Cals
			logger.debug(Encode.forJava("CustomerID=" +reqObj.getCustomerID()));

			return SUCCESS;
		} catch(RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	public String test(){
		return SUCCESS;
	}

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}

	public String getColorantid() {
		return colorantid;
	}

	public void setColorantid(String colorantid) {
		this.colorantid = colorantid;
	}

	public String getTintermodel() {
		return tintermodel;
	}

	public void setTintermodel(String tintermodel) {
		this.tintermodel = tintermodel;
	}

	public String getTinterserial() {
		return tinterserial;
	}

	public void setTinterserial(String tinterserial) {
		this.tinterserial = tinterserial;
	}

	public String getUploaddate() {
		return uploaddate;
	}

	public void setUploaddate(String uploaddate) {
		this.uploaddate = uploaddate;
	}

	public String getUploadtime() {
		return uploadtime;
	}

	public void setUploadtime(String uploadtime) {
		this.uploadtime = uploadtime;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}


	public byte[] getData() {
		return data;
	}

	@TypeConversion(converter="com.sherwin.shercolor.common.web.model.StringToByteArrayConverter")
	public void setData(byte[] data) {
		this.data = data;
	}
	public List<CustWebEcal> getEcalList() {
		return ecalList;
	}
	public void setEcalList(List<CustWebEcal> ecalList) {
		this.ecalList = ecalList;
	}
	public void setSession(Map<String, Object> session) {
		this.sessionMap = session;	
		
	}
	public String getReqGuid() {
		return reqGuid;
	}
	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}
	public TinterInfo getTinter() {
		return tinter;
	}
	public void setTinter(TinterInfo tinter) {
		this.tinter = tinter;
	}

	

}
