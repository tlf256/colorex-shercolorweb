package com.sherwin.shercolor.swdevicehandler.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;


public class Configuration {
	String colorantSystem;
	String model;
	String serial;
	String port; // you cannot set this, rather, when we find a port by iteration, we will set this.
	List<Canister> canisterLayout;
	CanisterMap canisterMap = null;
	static Logger logger = LogManager.getLogger(Configuration.class);


	public Configuration(){

	}
	public Configuration(Configuration new_config){
		setColorantSystem(new_config.getColorantSystem());
		setModel(new_config.getModel());
		setSerial(new_config.getSerial());
		setPort(new_config.getPort());
		setCanisterLayout(new_config.getCanisterLayout());
		if(getCanisterLayout() != null  && getCanisterLayout().size()>0){
			setCanisterMap(new CanisterMap(getCanisterLayout()));
		}
	}
	public String getColorantSystem() {
		return colorantSystem;
	}
	public void setColorantSystem(String colorantSystem) {
		this.colorantSystem = colorantSystem;
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
	public List<Canister> getCanisterLayout() {
		return canisterLayout;
	}
	public void setCanisterLayout(List<Canister> canisterLayout) {
		this.canisterLayout = canisterLayout;
	}
	public CanisterMap getCanisterMap() {
		return canisterMap;
	}
	public void setCanisterMap(CanisterMap canisterMap) {
		this.canisterMap = canisterMap;
	}


	public String SaveToDisk(String test){
		String filename;
		if(test != null && test.equalsIgnoreCase("test")){
			//filename="c:\\swdevicehandler\\tinterconfig_test.conf";
			filename="./tinterconfig_test.conf";
		}
		else{
			//filename="c:\\swdevicehandler\\tinterconfig.conf";
			filename="./tinterconfig.conf";
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			exceptionMsg=e.getMessage();
		}
		return exceptionMsg;
	}

	public static Configuration ReadFromDisk(String test){
		String filename;
		Configuration c=null;
		if(test != null && test.equalsIgnoreCase("test")){
			//filename="c:\\swdevicehandler\\tinterconfig_test.conf";	
			filename="./tinterconfig_test.conf";
		}
		else{
			//filename="c:\\swdevicehandler\\tinterconfig.conf";
			filename="./tinterconfig.conf";
		}
		String exceptionMsg = "";
		System.out.println("Checking for existence of:" + filename);
		File fconfig = new File(filename);
		if(fconfig.exists()){
			System.out.println( fconfig.getAbsolutePath() + filename + " exists");

			try{
				String json = FileUtils.readFileToString(fconfig);
				c = new Gson().fromJson(json, Configuration.class);
			}
			catch(IOException e){
				e.printStackTrace();
				exceptionMsg=e.getMessage();
			}
		}
		return c;
	}

}
