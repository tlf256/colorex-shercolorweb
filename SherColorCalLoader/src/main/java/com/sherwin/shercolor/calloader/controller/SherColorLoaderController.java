package com.sherwin.shercolor.calloader.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sherwin.shercolor.common.domain.CustWebEcal;
import com.sherwin.shercolor.common.service.EcalService;

@Component
public class SherColorLoaderController {
	 @Autowired
	 EcalService myservice;
	 
	 CustWebEcal ecal = new CustWebEcal();
	 
	 public void fillFields(File selectedFile){
	        byte[] data=null;
	        Path path = null;
	        try {
	        	if(selectedFile !=null){
	        		path = selectedFile.toPath();
	        		 data = Files.readAllBytes(path);
	        	}
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        String filename= selectedFile.getName();
			String[] arr = filename.split("_");
			String time = "0001";
			if(arr.length >=5){
				 time = arr[4].substring(0, 4);
			}
			if(arr.length >=6){
				 time = arr[5].substring(0, 4);
			}
	        ecal.setCustomerid("TEST");
	        ecal.setColorantid(arr[0]);
	        ecal.setTintermodel(arr[1]);
	        ecal.setTinterserial(arr[2]);
	        ecal.setFilename(filename);
	        ecal.setUploaddate(arr[3]);
	        ecal.setUploadtime(time);
	        ecal.setData(data);
		}
	 public void UploadEcal(String customerId){
		 getEcal().setCustomerid(customerId);
		 myservice.uploadEcal(getEcal());
	 }

	public EcalService getMyService() {
		return myservice;
	}

	public void setMyService(EcalService service) {
		this.myservice = service;
	}

	public CustWebEcal getEcal() {
		return ecal;
	}

	public void setEcal(CustWebEcal ecal) {
		this.ecal = ecal;
	}
	 
	 
}
