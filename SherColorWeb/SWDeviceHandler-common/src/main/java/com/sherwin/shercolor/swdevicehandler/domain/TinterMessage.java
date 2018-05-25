package com.sherwin.shercolor.swdevicehandler.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;

public class TinterMessage extends Message {
	//String id;                          //uuid
    String command;                    //command to execute
    /*String colorantSystem;                   // Colorant system in use
    String model;                     // tinter model
    String serial;                    // tinter serial
    List<Canister> canisterLayout =  new ArrayList<Canister>(); //colorant assignments
    */
    final static String LD_FILE = "lastdetect.txt";    
    Configuration configuration;
    Calibration calibration;
    GData gdata;
    List<Colorant> shotList = new ArrayList<Colorant>();  

       /* 
		String clrntCode;
		short clrntShots;   // colorant shot array
		short clrntUom;   // colorant shot uom array
		short clrntPumpPos;   // colorant pump position array
		
		*/

	long status;                        // Status Code, 0 = successful completion 
                                        // 1 = command still in process
                                        // 2 = Fail - no color specified
                                        // 3 = Bad UOM or Can't talk to GTI or Command not recognized
                                        // Anything else will be type of status returned from GTI as
                                        //      returned by GetRespRec 
 //   String companyModel;              // model with the company name prepended (if not a sherwin store) 11/1/04 TAH
 //   String DispenserFware;            // Dispenser Firmware version
//	String posVersion; //char posVersion[7];
//	String tinterDriverVersion; //char tinterDriverVersion[10];   String DispenserDrvVer;           // Dispenser driver version
 //   String GTIVer;                    // GTI version
//    String PCModel;                   // PC model number
//    String PCSN;                      // PC serial number
//    long lLastCommand;                  // If the current command is a get response, 
                                        // this is the command
                                        // which was executing before the get response
//    long lMisc;                         // Miscellaneous data that needs to be passed
                                        //      i.e. GTI transaction ID
	String javaMessage;                 //exception, file not found, etc
	
	//HashMap<Integer,String> errorMessageMap = new HashMap<Integer,String>(); // map of errorNumber and errorMessage (corob can have more than one)
	long commandRC;                    //initial return code from Dsp command
	long errorNumber;                  // Actual GTI+Driver response code
    short errorSeverity;
    String errorMessage;
    List<Error> errorList = new ArrayList<Error>();
    long lastInitDate;  //optional field to send back last detect date.
    
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}

	public Configuration getConfiguration() {
		return configuration;
	}
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public Calibration getCalibration() {
		return calibration;
	}
	public void setCalibration(Calibration calibration) {
		this.calibration = calibration;
	}
	public List<Colorant> getShotList() {
		return shotList;
	}
	public void setShotList(List<Colorant> shotList) {
		this.shotList = shotList;
	}
	public long getStatus() {
		return status;
	}
	public void setStatus(long status) {
		this.status = status;
	}
	public String getJavaMessage() {
		return javaMessage;
	}
	public void setJavaMessage(String javaMessage) {
		this.javaMessage = javaMessage;
	}
	public long getCommandRC() {
		return commandRC;
	}
	public void setCommandRC(long commandRC) {
		this.commandRC = commandRC;
	}
	public long getErrorNumber() {
		return errorNumber;
	}
	public void setErrorNumber(long errorNumber) {
		this.errorNumber = errorNumber;
	}
	public short getErrorSeverity() {
		return errorSeverity;
	}
	public void setErrorSeverity(short errorSeverity) {
		this.errorSeverity = errorSeverity;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public List<Error> getErrorList() {
		return errorList;
	}
	public void setErrorList(List<Error> errorList) {
		this.errorList = errorList;
	}
	public long getLastInitDate() {
		return lastInitDate;
	}
	public void setLastInitDate(long lastInitDate) {
		this.lastInitDate = lastInitDate;
	} 
	
	public GData getGdata() {
		return gdata;
	}
	public void setGdata(GData gdata) {
		this.gdata = gdata;
	}
	public String SaveToDisk(String test){
		String filename;
		if(test != null && test.equalsIgnoreCase("test")){
			filename="lastdetect_test.txt";	
		}
		else{
			filename=LD_FILE;
		}

		String exceptionMsg = "Success";

		File fconfig = new File(filename);
	

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
	public static TinterMessage ReadFromDisk(String test){
		String filename;
		TinterMessage m=null;
		if(test != null && test.equalsIgnoreCase("test")){
			filename="lastdetect_test.txt";	
		}
		else{
			filename=LD_FILE;
		}
		String exceptionMsg = "";
		System.out.println("Checking for existence of:" + filename);
		File fconfig = new File(filename);
		if(fconfig.exists()){
			System.out.println( filename + " exists");

			try{
				String json = FileUtils.readFileToString(fconfig);
				m = new Gson().fromJson(json, TinterMessage.class);
			}
			catch(IOException e){
				e.printStackTrace();
				exceptionMsg=e.getMessage();
			}
		}
		return m;
	}
}
