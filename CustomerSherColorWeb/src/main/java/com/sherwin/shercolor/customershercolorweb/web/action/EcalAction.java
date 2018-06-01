package com.sherwin.shercolor.customershercolorweb.web.action;


import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
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
	private final static Logger log = LogManager.getLogger(EcalAction.class);

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
		setEcalList(service.getEcalTemplate(colorantid, tintermodel));
		return SUCCESS;
	}
	public String GetEcalsByCustomer(){
		//we call this function from the ProcessConfig right now.
		setEcalList(service.getEcalsByCustomer(customerid));
		return SUCCESS;
	}

	public String GetECalList(){

		setEcalList(service.getEcalList(customerid,colorantid,tintermodel,tinterserial));		

		return SUCCESS;
	}
	
	public String SelectEcal(){
		System.out.println("selecting ecal for " + filename);
		CustWebEcal cal = service.selectEcal(filename);	
		if(cal != null){			
			setData(cal.getData());
		}
		return SUCCESS;
	}

	public String SelectGData(){
		System.out.println("selecting gdata for " + colorantid);
		CustWebEcal cal = service.selectGData(colorantid);	
		if(cal != null){			
			setData(cal.getData());
		}
		return SUCCESS;
	}
	
	public String UploadEcal(){
		System.out.println("inside EcalAction");
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		System.out.println("inside EcalAction, got reqObj, now getting tinter");
		tinter = reqObj.getTinter();
		System.out.println("inside Ecal, got tinter, returning success");

		//TODO get last Config date info to be shown on screen...

		// setup tinter stuff if needed
		if(tinter==null) System.out.println("inside ProcessConfigAction_display and tinter object is null");
		
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
			System.out.println("Upload Ecal size=" + this.getData().length);
			log.info("Upload Ecal size=" + this.getData().length);
		}
		else{
			System.out.println("Data array is null");
			log.error("Upload Ecal data array is null");
			return ERROR;
		}
		service.uploadEcal(ecal);
		return SUCCESS;
	}

	public String display(){
		System.out.println("inside EcalAction");
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		System.out.println("inside EcalAction, got reqObj, now getting tinter");
		tinter = reqObj.getTinter();
		System.out.println("inside Ecal, got tinter, returning success");

		//TODO get last Config date info to be shown on screen...

		// setup tinter stuff if needed
		if(tinter==null) System.out.println("inside EcalAction_display and tinter object is null");
		
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
		System.out.println("CustomerID=" +reqObj.getCustomerID());

		return SUCCESS;
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
