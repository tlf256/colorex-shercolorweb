package com.sherwin.shercolor.cal.web.action;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.ParameterAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import com.sherwin.shercolor.cal.domain.CalTemplate;
import com.sherwin.shercolor.cal.domain.Ecal;
import com.sherwin.shercolor.cal.service.CalService;

@Component
public class EcalAction extends ActionSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private final static Logger log = LogManager.getLogger(EcalAction.class);

	@Autowired
	CalService service;
	
	String customerid=null;
	String colorantid=null;
	String tintermodel=null;
	String tinterserial=null; 
	String uploaddate=null; 
	String uploadtime=null;
	String filename=null;
	byte[] data=null;
	List<Ecal> ecalList=null;
	
	public String GetECalList(){

	
		setEcalList(service.getEcalList(customerid,colorantid,tintermodel,tinterserial));
		
	
		return SUCCESS;
	}
	public String SelectEcal(){
		Ecal cal = service.selectEcal(filename);
		System.out.println("selecting ecal for " + filename);
		if(cal != null){
			
			setData(cal.getData());
		}
		return SUCCESS;
	}

	public String UploadEcal(){
		Ecal ecal = new Ecal();
		ecal.setCustomerid(this.getCustomerid());
		ecal.setColorantid(this.getColorantid());
		ecal.setTintermodel(this.getTintermodel());
		ecal.setTinterserial(this.getTinterserial());
		ecal.setUploaddate(this.getUploaddate());
		ecal.setUploadtime(this.getUploadtime());
		ecal.setFilename(this.getFilename());
		ecal.setData(this.getData());
		System.out.println("Uploading Ecal");
		if(this.getData()!=null){
		System.out.println("size=" + this.getData().length);
		}
		else{
			System.out.println("Data array is null");
		}
		service.uploadEcal(ecal);
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
	
	@TypeConversion(converter="com.sherwin.shercolor.cal.web.action.StringToByteArrayConverter")
	public void setData(byte[] data) {
		this.data = data;
	}
	public List<Ecal> getEcalList() {
		return ecalList;
	}
	public void setEcalList(List<Ecal> ecalList) {
		this.ecalList = ecalList;
	}
	
	public void setParameters(Map<String, String[]> parameters) {
		for(String item:parameters.keySet()){
			System.out.println(item);
			if(item=="data"){
				System.out.println("item = data");
			   // String[] data = parameters.get("data");
			   // System.out.println(x);
			}
			
		}
		
	}

	

	
	
}
