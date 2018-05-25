package com.sherwin.shercolor.swdevicehandler.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;


public class SpectroConfiguration {
	String model;
	String serial;
	String port; // you cannot set this, rather, when we find a port by iteration, we will set this.
	static Logger logger = LogManager.getLogger(SpectroConfiguration.class);


	public SpectroConfiguration(){

	}
	
	public SpectroConfiguration(SpectroConfiguration new_config){
		setModel(new_config.getModel());
		setSerial(new_config.getSerial());
		setPort(new_config.getPort());
	}

	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getSerial() {
		return serial;
	}
	public void setSerial(String serial) {
		this.serial = serial;
	}


	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	


	public String SaveToDisk(String test){
		String filename;
		if(test != null && test.equalsIgnoreCase("test")){
			filename="./spectro/spectroconfig_test.conf";	
		}
		else{
			filename="./spectro/spectroconfig.conf";
		}

		String exceptionMsg = "Success";

		File fconfig = new File(filename);
		fconfig.getParentFile().mkdirs(); // Will create parent directories if not exists

		try {
			fconfig.createNewFile();
			FileOutputStream fout = new FileOutputStream(fconfig,false);
			String json = new Gson().toJson(this);
			byte b[]=json.getBytes();//converting string into byte array    
			fout.write(b);    
			fout.close();    
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			exceptionMsg=e.getMessage();
			logger.error(e.getMessage());
			logger.error("stacktrace", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			exceptionMsg=e.getMessage();
			logger.error(e.getMessage());
			logger.error("stacktrace", e);
		} catch (Exception e) {
			e.printStackTrace();
			exceptionMsg=e.getMessage();
			logger.error(e.getMessage());
			logger.error("stacktrace", e);
		}
		return exceptionMsg;
	}

	public static SpectroConfiguration ReadFromDisk(String test){
		String filename;
		SpectroConfiguration c=null;
		if(test != null && test.equalsIgnoreCase("test")){
			filename="./spectro/spectroconfig_test.conf";	
		}
		else{
			filename="./spectro/spectroconfig.conf";
		}
		System.out.println("Checking for existence of:" + filename);
		File fconfig = new File(filename);
		if(fconfig.exists()){
			System.out.println( filename + " exists");

			try{
				String json = FileUtils.readFileToString(fconfig);
				c = new Gson().fromJson(json, SpectroConfiguration.class);
			}
			catch(IOException ie){
				ie.printStackTrace();
				logger.error(ie.getMessage());
				logger.error("stacktrace", ie);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return c;
	}

}
