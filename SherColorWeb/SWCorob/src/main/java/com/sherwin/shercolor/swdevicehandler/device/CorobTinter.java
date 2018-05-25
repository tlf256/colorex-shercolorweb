package com.sherwin.shercolor.swdevicehandler.device;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zeroturnaround.exec.InvalidExitValueException;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;

import com.sherwin.shercolor.swdevicehandler.domain.Colorant;
import com.sherwin.shercolor.swdevicehandler.domain.Calibration;
import com.sherwin.shercolor.swdevicehandler.domain.Canister;
import com.sherwin.shercolor.swdevicehandler.domain.CanisterMap;
import com.sherwin.shercolor.swdevicehandler.domain.Error;
import com.sherwin.shercolor.swdevicehandler.domain.ErrorMessages;
import com.sherwin.shercolor.swdevicehandler.domain.Message;
import com.sherwin.shercolor.swdevicehandler.domain.Configuration;
import com.sherwin.shercolor.swdevicehandler.domain.CorobCalibration;
import com.sherwin.shercolor.swdevicehandler.domain.GData;
import com.sherwin.shercolor.swdevicehandler.domain.TinterMessage;

import gnu.io.CommPortIdentifier;
import libpkg.MyDbfReader;
import libpkg.MyDbfWriter;
import libpkg.MyNullPointerFoundException;



public class CorobTinter extends AbstractTinterImpl implements TinterInterface {
	final static double CC_PER_OZ = 29.574;
	static Logger logger = LogManager.getLogger(CorobTinter.class);
	ErrorMessages errorMessages = new ErrorMessages();
	Configuration configuration = new Configuration();


	boolean hardwareError = false; // 7E00 error found
	public static boolean DEBUG = false;


	public CorobTinter(){
		//get initial config from disk and store in memory.
		this.setConfiguration(Configuration.ReadFromDisk(null));
	}

	@Override
	protected
	void ProcessQueueItem(Message m){
		TinterMessage msg = null;
		System.out.println("Received item in Corob queue.");
		logger.info("Received item in Corob queue.");
		if(m != null  && m.getMessageName()!=null){
			switch(m.getMessageName()){
			case "TinterMessage":
				msg = (TinterMessage)m;	
				if(msg.getLocale() != null){
					errorMessages.setLocale(msg.getLocale());
				}
				String command = msg.getCommand();

				if(command !=null){
					
					switch(command){
					case "Agitate":
						System.out.println(this.getClass().getSimpleName() + " Sending " + command + " to device" );
						Agitate(msg);
						System.out.println("rc=" + msg.getErrorNumber() + " " +  msg.getErrorMessage());
						break;	
					case "BellowsDown":
						System.out.println(this.getClass().getSimpleName() + " Sending " + command + " to device" );
						BellowsDown(msg);
						System.out.println("rc=" + msg.getErrorNumber() + " " +  msg.getErrorMessage());
						break;	
					case "BellowsUp":
						System.out.println(this.getClass().getSimpleName() + " Sending " + command + " to device" );
						BellowsUp(msg);
						System.out.println("rc=" + msg.getErrorNumber() + " " +  msg.getErrorMessage());
						break;	
					case "CloseNozzle":
						System.out.println(this.getClass().getSimpleName() + " Sending " + command + " to device" );
						CloseNozzle(msg);
						System.out.println("rc=" + msg.getErrorNumber() + " " +  msg.getErrorMessage());
						break;	
					case "Detect":
					case "Init":
						System.out.println(this.getClass().getSimpleName() + " Sending " + command + " to device" );
						Detect(msg);
						System.out.println("rc=" + msg.getErrorNumber() + " " +  msg.getErrorMessage());
						break;
					case "DetectStatus":
					case "InitStatus": 
						InitStatus(msg);  //status of last detect/init command
						break;
					case "Dispense":
						System.out.println(this.getClass().getSimpleName() + " Sending " + command + " to device" );
						Dispense(msg);
						System.out.println("rc=" + msg.getErrorNumber() + " " +  msg.getErrorMessage());
						break;	
					case "OpenNozzle":
						System.out.println(this.getClass().getSimpleName() + " Sending " + command + " to device" );
						OpenNozzle(msg);
						System.out.println("rc=" + msg.getErrorNumber() + " " +  msg.getErrorMessage());
						break;	
					case "PurgeAll":
						System.out.println(this.getClass().getSimpleName() + " Sending " + command + " to device" );
						PurgeAll(msg);
						System.out.println("rc=" + msg.getErrorNumber() + " " +  msg.getErrorMessage());
						break;	
					case "Recirculate":
						System.out.println(this.getClass().getSimpleName() + " Sending " + command + " to device" );
						Recirculate(msg);
						System.out.println("rc=" + msg.getErrorNumber() + " " +  msg.getErrorMessage());
						break;	
						//configuration stuff
					case "ReadConfig":
						ReadConfig(msg);
						break;	
					case "Config":
						Config(msg);
						break;
					case "CalDownload":
						CalDownload(msg);
						break;

					case "CalUpload":
						CalUpload(msg);
						break;
					default:
						msg.setErrorMessage(command + " not recognized as a valid tinter command.");	
						msg.setErrorNumber(-1005);
						break;
					}
				}


				System.out.println("Process Queue Item...Message Name: "+ m.getMessageName() + " received");
				Respond(msg);
				break;


			default:
				msg = (TinterMessage)m;
				msg.setMessageName("TinterMessage");
				msg.setErrorMessage("Message Name: "+ m.getMessageName() + " not recognized");
				System.out.println("Message Name: "+ m.getMessageName() + " not recognized");
				Respond(msg);
			}

		}

	}




	public  void ExecuteCommand(TinterMessage msg, String command){

		String output=null;
		String caught = "";
		Long rc = 0L;
		File DspDirectory = new File("/Program Files (x86)/Dsp_Win");
		File localDirectory = new File("/local");
		if(DspDirectory.exists()){
			if(localDirectory.exists()){
				try {
					Future<ProcessResult> future = new ProcessExecutor().directory(localDirectory)
							.command("/Program Files (x86)/Dsp_Win/DSP.exe",command)
							.timeout(60, TimeUnit.SECONDS)
							.readOutput(true).start().getFuture();
					output = future.get(600, TimeUnit.SECONDS).outputUTF8();
					rc = (long) future.get(600, TimeUnit.SECONDS).getExitValue();
					if(DEBUG == true){
						System.out.println(output);
						logger.info(output);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("stacktrace",e);

					caught="Error from ProcessExecutor " + e.getMessage();				
				}  
			}
			else{
				caught = "Calibration dir does not exist";
			}
		}
		else{
			caught="Dsp not installed";
		}
		msg.setJavaMessage(caught);

		if(caught.length() == 0){

			if(command.equalsIgnoreCase("/I") && rc == 4){
				rc = 0L;  //assume simulator which doesn't take the /I command.
			}
			String errorMessage = errorMessages.getInternationalErrorMessage(rc);
			//String errorMessage = errorMessages.getRC_map().get(rc);
			//String errorMessage = errorMessages.getMap().get(0xFC94);

			msg.setErrorMessage(errorMessage);
			msg.setCommandRC(rc);
			msg.setErrorNumber(rc);
			logger.info(command + " executed with result (" + rc + " ) " + output );
			logger.info(command + " executed with result (" + rc + " )  Message:" + errorMessage );
		}
		else{
			msg.setCommandRC(-1);
			msg.setErrorMessage(caught);
			msg.setJavaMessage(caught);
			logger.info(command + " executed with result (" + rc + " ) " + output + "caught (" + caught + " ) ");
		}
		return ;
	}

	private Error BuildErrorMessage(TinterMessage tmpMsg){
		//find our result in the list of errors
		long pumpNumber =0;
		String sErr=new String();
		Error newError = new Error();

		//lOurResult=FindResultInListofPrimaryErrors(rc);
		pumpNumber = tmpMsg.getCommandRC() & 0x00FF;
		// messages between 0x100 and 0x4400 have pump number in last two digits of error code
		if(tmpMsg.getCommandRC() >=0x0100 && tmpMsg.getCommandRC() <=0x4400 && pumpNumber >= 0x01  && pumpNumber <=32){
			long primaryErrorNum = tmpMsg.getCommandRC() & 0xFF00;

			//found!  note:primary list always has a circuit# associated with it.
			// add colorant and pump # to error.

			if(this.getConfiguration().getCanisterMap()!=null){
				if(this.getConfiguration().getCanisterMap().getMap().get(pumpNumber) != null && this.getConfiguration().getCanisterMap().getMap().get(pumpNumber) != "NA"){ // if color is in map
					//sErr=String.format("CPS#%04X-%s[#%d:%s]* ",tmpMsg.getCommandRC(),errorMessages.getMap().get(primaryErrorNum),pumpNumber,this.getConfiguration().getCanisterMap().getMap().get(pumpNumber));							
					sErr=String.format("%s[#%d:%s]",errorMessages.getInternationalErrorMessage(primaryErrorNum),pumpNumber,this.getConfiguration().getCanisterMap().getMap().get(pumpNumber));
					newError.setNum(tmpMsg.getCommandRC());
					newError.setMessage(sErr);
					//this.getErrorMessageList().add(sErr);
					logger.info("BuildErrorMessage result:" + sErr);
				}
				else{

					//pump not found in canister map
					if(tmpMsg.getCommandRC() == 0x7E00){ // false error.  set to success
						setHardwareError(true);
						String tmp = String.format("CPS#%04X", tmpMsg.getCommandRC());
						logger.info("Overriding " + tmp + " which is a firmware bug that does not indicate an actual error ");
					}
					else{
						//sErr=String.format("CPS#%04X-%s",tmpMsg.getCommandRC(),errorMessages.getMap().get(primaryErrorNum));							
						sErr=String.format("%s",errorMessages.getMap().get(primaryErrorNum));
						newError.setNum(tmpMsg.getCommandRC());
						newError.setMessage(sErr);
						logger.info("%d:%s:Not putting error for pump we don't need." + sErr);

					}
				}
			}
			else{
				//sErr=String.format("CPS#%04X", tmpMsg.getCommandRC());
				sErr=String.format("%s [canister map is null]",errorMessages.getMap().get(primaryErrorNum));
				newError.setNum(tmpMsg.getCommandRC());
				newError.setMessage(sErr);
			}

		}
		else{
			//we didn't find it in primary list, go to secondary one.

			//sErr=String.format("CPS#%04X-%s",tmpMsg.getCommandRC(),errorMessages.getMap().get(tmpMsg.getCommandRC()));							
			sErr=String.format("%s",errorMessages.getInternationalErrorMessage(tmpMsg.getCommandRC()));
			newError.setNum(tmpMsg.getCommandRC());
			newError.setMessage(sErr);
			//errorMessageList.add(sErr);
			logger.info("BuildErrorMessage result:" + sErr);

		}

		return newError;


	}
	// ############################################################################
	// ProcessErrors
	// Translates the error code into our error number and message
	// Calls ExecPopen to communicate with tinter
	//  1. /Q3 to get how many errors
	//  2. /Q1 to get first error
	//  3. /Q2 to get next errors
	//  4. /Q0 to clear errors.
	// Will query the tinter to see if there are errors from the last command
	// Error codes less than 0x8000, depending on error, may contain the device
	// number in the last byte. Ex. 0x0702 is returned, valve number 2 is where the
	// error resides.
	void ProcessErrors(TinterMessage msg){

		List<Error> errorList = new ArrayList<Error>();
		long errorQueueCnt=0;
		setHardwareError(false); // reset this

		TinterMessage tmpMsg= new TinterMessage();

		/* send a Q3 to see if there are any errors */
		if(DEBUG==true){
			errorQueueCnt=2;
		}
		else{
			ExecuteCommand(tmpMsg,"/Q3");
			errorQueueCnt=tmpMsg.getCommandRC();
		}


		logger.info("Error count from Q3 is [" + errorQueueCnt + "]");

		if(errorQueueCnt >= 1){

			/* send a Q1 to get first error */
			if(DEBUG==true){
				tmpMsg.setCommandRC(0x0B01);
			}
			else{
				ExecuteCommand(tmpMsg,"/Q1");
			}

			logger.info("RC from Q1 is  [" + tmpMsg.getCommandRC() + "]");
			Error tmpError1 =  BuildErrorMessage(tmpMsg);
			if(tmpError1 !=null){
				errorList.add(tmpError1);
			}
			//decrement error count
			errorQueueCnt = errorQueueCnt - 1;
			//if there is more than 1 error, go into this while loop.
			while(errorQueueCnt >=1){
				//figure out what the next error is.
				tmpMsg = new TinterMessage();

				//send a Q2 to get second error
				if(DEBUG==true){
					tmpMsg.setCommandRC(0x0B0A);
				}
				else{
					ExecuteCommand(tmpMsg,"/Q2");
				}
				logger.info("RC from Q2 is  [" + tmpMsg.getCommandRC() + "]");

				//find our result in the list of errors
				Error tmpError2 =  BuildErrorMessage(tmpMsg);
				if(tmpError2 !=null){
					errorList.add(tmpError2);
				}

				//decrement error count
				errorQueueCnt = errorQueueCnt - 1;

			}
			//reset error queue - /Q0
			if(DEBUG==false){
				ExecuteCommand(tmpMsg,"/Q0");
				logger.info("Error Queue Reset rc=" + tmpMsg.getCommandRC());
			}


			//if we threw away all errors because they were NA or the 7E00 error

			if(errorList.size()==0){
				msg.setErrorNumber(0);
				if(this.isHardwareError()){
					msg.setCommandRC(0); //the original error was a false error.
				}
			}
			else{		
				try{

					msg.setErrorList(errorList);

					if(msg.getCommandRC()==0){
						msg.setErrorNumber(ErrorMessages.WARN_TEXT_IN_BUFFER);
						msg.setErrorMessage("Warning Messages in ErrorList");

					}
					else{
						msg.setErrorNumber(ErrorMessages.ERR_TEXT_IN_BUFFER);
						msg.setErrorMessage("Error Messages in ErrorList");
					}
				}
				catch(Exception e){
					e.printStackTrace();
					logger.error("stacktrace",e);
					msg.setJavaMessage(e.getMessage());
				}
			}				
		}
		else {
			//there was never an error to begin with - no errors in the queue.
			logger.error("Q3 ret 0.  Leaving error number [%ld]",msg.getErrorNumber());
			System.out.println("Q3 ret 0.  Leaving error number "  + msg.getErrorNumber());
			//msg.setErrorNumber(0);
		}
		/* done elsewhere
		else if(errorQueueCnt == -1){
			if(msg.getCommandRC() > 0){
				long errornum = -39000 - msg.getCommandRC();
				msg.setErrorNumber(errornum);
				logger.error("Q3 ret -1.  Setting error number to [%ld]",msg.getErrorNumber());
				System.out.println("Q3 ret -1.  Setting error number to " + msg.getErrorNumber());
			}
		}

		 */

	}


	public void IterateSerialPorts(TinterMessage msg){
		Enumeration portList = CommPortIdentifier.getPortIdentifiers();
		String port = "";
		String communic="";

		MyDbfReader dbfReader1;
		if(portList.hasMoreElements()){ // are there any serial ports?
			try {
				while(portList.hasMoreElements()){
					dbfReader1 = new MyDbfReader("/local/disps.dbf");
					dbfReader1.getDbfInputStream().close();//close stream to prevent further reading.
					//create writer classes to write updates back to dbfs updating spec gravity
					MyDbfWriter dbfWriter1=new MyDbfWriter(dbfReader1);
					LinkedHashMap<String, Object> row = dbfWriter1.get_DBF_hash_list().get(0); //get only row in dbf
					communic = (String)row.get("COMMUNIC");
					System.out.println("Communication parms" + communic );
					logger.info("Communication parms" + communic );
					System.out.println(portList.hasMoreElements()); // if false no commports detected

					System.out.println("Has more elements");
					logger.info("Has more elements");
					CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
					if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {

						port = portId.getName().substring(3);

						System.out.println("com #" + portId.getName() + " ");
						if(port.length()==1){ //fix because com port can be 2 digits or 1, so make sure it always takes 2.
							port = port + " ";
						}
						communic = port + communic.substring(2);//replace first two chars in string with com port.
						row.put("COMMUNIC", communic);
						dbfWriter1.Write();  //write port so DSP command will use it in this /R command.
						ExecuteCommand(msg,"/R");
						if(msg.getCommandRC() != -1 && msg.getCommandRC() != 3){ // if no comm error, we have found our tinter
							logger.info("Found tinter on com #" + portId.getName() + " ");
							this.getConfiguration().setPort(portId.getName());
							msg.getConfiguration().setPort(portId.getName());

							break;
						}
						else{
							//this is checked for null before we get here so no need to check here.
							this.getConfiguration().setPort("");
							msg.getConfiguration().setPort("");
						}
					}

				}
				//now let's set wdisps.dbf for CorobTech
				dbfReader1 = new MyDbfReader("/local/wdisps.dbf");
				dbfReader1.getDbfInputStream().close();//close stream to prevent further reading.
				//create writer classes to write updates back to dbfs updating spec gravity
				MyDbfWriter dbfWriter1=new MyDbfWriter(dbfReader1);
				LinkedHashMap<String, Object> row = dbfWriter1.get_DBF_hash_list().get(0); //get only row in dbf
				row = dbfWriter1.get_DBF_hash_list().get(0); //get only row in dbf
				communic = (String)row.get("COMMUNIC");
				int sz = communic.indexOf("|");
				logger.info("Communication parms" + communic );
				//String[] comm_arr = communic.split("|");
				String newport =  port.trim();
				String new_communic = new String();
				new_communic = newport + communic.substring(sz);
				
				row.put("COMMUNIC", new_communic);
				dbfWriter1.Write();  //write port so CorobTech can use it.

			} catch (Exception e) {
				msg.setJavaMessage(e.getMessage());
				// TODO Auto-generated catch block
				logger.error("stacktrace",e);

			}//reads file into memory

		}
		else{
			logger.info("No serial port on PC");
			msg.setErrorMessage("No Serial Port on PC.  Connect a USB to serial adapter and try again.");
			msg.setErrorNumber(-16);
		}
	}
	public void CalDownload(TinterMessage msg){
		String exceptionMessage=null;
		Calibration cal =  msg.getCalibration();
		if(cal != null){
			CorobCalibration cCal = new CorobCalibration(cal);
			exceptionMessage = cCal.toDisk();

			GData gdata = msg.getGdata();
			if(gdata != null){			
				exceptionMessage += gdata.toDisk();				
			}
			else{
				logger.info("Gdata is null");
			}
			msg.setJavaMessage(exceptionMessage);
		}	
	}
	public void CalUpload(TinterMessage msg){		
		CorobCalibration cCal = new CorobCalibration();
		Configuration config = this.getConfiguration();
		String datetime =  new SimpleDateFormat( "yyyyMMdd_HHmm" ).format( Calendar.getInstance().getTime());
		String filename= config.getColorantSystem() + "_" + config.getModel() + "_" + config.getSerial()+"_" + datetime; // + Calendar.YEAR + (Calendar.MONTH + 1) + Calendar.DATE + "_" + Calendar.HOUR_OF_DAY + Calendar.MINUTE ;
		String uFilename= filename.toUpperCase() + ".zip";
		uFilename = uFilename.replace(" ", "");   // (/\s+/g, '');
		cCal.setFilename(uFilename);
		String exceptionMessage = cCal.fromDisk();
		msg.setCalibration(cCal);
		msg.setConfiguration(config);
		msg.setJavaMessage(exceptionMessage);

	}
	public void Config(TinterMessage msg){
		this.setConfiguration(new Configuration(msg.getConfiguration())); // IMPORTANT!! This copy constructor creates the canister map.
		String exceptionMessage = this.getConfiguration().SaveToDisk(null);
		
		CalDownload(msg);

	
		msg.setJavaMessage(exceptionMessage);
		msg.setErrorMessage(exceptionMessage);
		if(exceptionMessage.length()>0 && !exceptionMessage.equalsIgnoreCase("Success")){
			msg.setCommandRC(-1);
			logger.error("Exception Message = " + exceptionMessage);			
		}

		// don't do a detect here.  Keep Detect in its own function.  Force the web page, unit test, etc to send a separate detect!
		//what about calibration files?
	}
	public void ReadConfig(TinterMessage msg){
		if(this.getConfiguration() != null){
			msg.setConfiguration(getConfiguration());
		}
	}
	@Override
	public void Detect(TinterMessage msg) {
		logger.info("Detecting");
		//1st iterate serial ports to find one with tinter.
		if(this.getConfiguration() != null){
			msg.setConfiguration(getConfiguration());
		}
		if(msg.getConfiguration() != null){
			ExecuteCommand(msg,"/R");
			logger.info("/R result" + msg.getCommandRC() + " " +  msg.getErrorMessage());
			logger.info("rc=" + msg.getCommandRC() + " " +  msg.getErrorMessage());
			if(msg.getCommandRC() != -1 && msg.getCommandRC() != 3){ // if no comm error, we have found our tinter in the same place as it was before:
				logger.info("Found tinter still on same com port");			
			}
			else{
				IterateSerialPorts(msg);	
			}
			switch((int)msg.getCommandRC()){
			case 3:
				//ProcessErrors(msg);
				//long errornum = -39000 - msg.getCommandRC();
				//msg.setErrorNumber(errornum);

				logger.error("/R rc = 3.  Could not talk to tinter");
				logger.info("/R rc = 3.  Could not talk to tinter ");
				break;
			case -1:
				//done in ExecuteCommand msg.setErrorNumber(-1);
				logger.error("/R rc = -1.  Setting error number to [%ld]",msg.getErrorNumber());
				logger.info("/R rc = -1.  Setting error number to " + msg.getErrorNumber());

				break;
			default:
				// if no comm error, we have found our tinter and can initialize it.
				ExecuteCommand(msg,"/I");
				ProcessErrors(msg);
				logger.info("/I rc[" + msg.getCommandRC() + "] " +  msg.getErrorMessage());
				logger.info("rc=" + msg.getCommandRC() + " " +  msg.getErrorMessage());
				//Nothing else to do.  ExecCommand does everything we need.			
			}
		}
		else{
			msg.setErrorMessage("Tinter not configured.  Please configure your tinter.");
			msg.setErrorNumber(-5);
		}
		//save this result into the last detect message for retrieval
		//anytime someone wants the last detect status!
		final DateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		String date = (sdf.format(Calendar.getInstance().getTime()));
		msg.setLastInitDate(Long.parseLong(date));
		this.setLastInitMessage(msg);

	}

	public void Agitate(TinterMessage msg) {
		System.out.println("Agitating");
		ExecuteCommand(msg,"/M");
		logger.info("/M result" + msg.getCommandRC() + " " +  msg.getErrorMessage());
		System.out.println("rc=" + msg.getCommandRC() + " " +  msg.getErrorMessage());
	}

	public void OpenNozzle(TinterMessage msg) {
		System.out.println("Open Nozzle");
		ExecuteCommand(msg,"/G1");
		logger.info("/G1 result" + msg.getCommandRC() + " " +  msg.getErrorMessage());
		System.out.println("rc=" + msg.getCommandRC() + " " +  msg.getErrorMessage());
	}
	public void CloseNozzle(TinterMessage msg) {
		System.out.println("Close Nozzle");
		ExecuteCommand(msg,"/G0");
		logger.info("/G1 result" + msg.getCommandRC() + " " +  msg.getErrorMessage());
		System.out.println("rc=" + msg.getCommandRC() + " " +  msg.getErrorMessage());
	}

	public void Recirculate(TinterMessage msg) {
		System.out.println("Recirc");
		ExecuteCommand(msg,"/J");
		logger.info("/J result" + msg.getCommandRC() + " " +  msg.getErrorMessage());
		System.out.println("rc=" + msg.getCommandRC() + " " +  msg.getErrorMessage());
	}
	public void BellowsUp(TinterMessage msg) {
		System.out.println("BellowsUp");
		ExecuteCommand(msg,"/B0");
		logger.info("/B0 result" + msg.getCommandRC() + " " +  msg.getErrorMessage());
		System.out.println("rc=" + msg.getCommandRC() + " " +  msg.getErrorMessage());
	}
	public void BellowsDown(TinterMessage msg) {
		System.out.println("BellowsDown");
		ExecuteCommand(msg,"/B1");
		logger.info("/B1 result" + msg.getCommandRC() + " " +  msg.getErrorMessage());
		System.out.println("rc=" + msg.getCommandRC() + " " +  msg.getErrorMessage());
	}
	public String DispenseAmt(double shots, int uom){

		double amt = 0.0;
		if(uom == 0){
			uom=128;
		}

		amt = (double)(shots * (double) (CC_PER_OZ / (double)uom));
		return String.format("%.4f", amt);
	}

	@Override
	public void Dispense(TinterMessage msg) {
		boolean itemsToDispense = false;
		logger.info("Dispensing");
		StringBuilder cmd= new StringBuilder();
		cmd.append("/D");
		if(msg.getShotList()!=null){
			for(Colorant colorant:msg.getShotList()){
				String amt="0.00";
				if(msg.getCommand().equalsIgnoreCase("PurgeAll")){
					amt="1.000"; //PurgeAll gets 1.0 cc
				}
				else{
					amt = DispenseAmt(colorant.getShots(),colorant.getUom());
				}
				Double d_amt = Double.valueOf(amt);
				if(d_amt > 0.0){ // shots = number of 1/128ths to dispense
					// amt,d_amt = number of cc's to dispense					
					if(this.getConfiguration()!=null  && this.getConfiguration().getCanisterMap() !=null){
						if(colorant.getPosition() != 0){					
							cmd.append("(").append(colorant.getPosition()).append(":").append(amt).append(")");
							logger.info("Dispense: Appending position " + colorant.getPosition() + " to dispense");
							itemsToDispense = true;
						}
						else if(colorant.getCode()!=""){
							Long position = this.getConfiguration().getCanisterMap().getCodeMap().get(colorant.getCode());
							logger.info("Dispense: Appending " + colorant.getCode() + " position " + position + "to dispense");
							cmd.append("(").append(String.valueOf(position)).append(":").append(amt).append(")");
							itemsToDispense = true;
						}					
					}						
				}
			}
		}
		if(itemsToDispense == true){
			ExecuteCommand(msg,cmd.toString());
			if(msg.getCommandRC() != 0x08){ // can sensor error.  No need to read queue.
				ProcessErrors(msg);
			}
			logger.info(cmd.toString());
			logger.info("rc=" + msg.getErrorNumber() + " " +  msg.getErrorMessage());

			//UpdateLevels(); //webpage will do
		}
		else{
			msg.setErrorMessage("Nothing to dispense");
			msg.setErrorNumber(-115);
		}
	}
	@Override
	public void PurgeAll(TinterMessage msg){
		int numCanisters = 0;
		int loop_counter=0;
		msg.setShotList(new ArrayList<Colorant>());
		if(this.getConfiguration() != null && this.getConfiguration().getCanisterLayout() !=null){
			for(Canister canister:this.getConfiguration().getCanisterLayout()){

				if(!canister.getCode().equalsIgnoreCase("NA")){
					loop_counter++;	

					Colorant colorant = new Colorant();

					colorant.setPosition((int) canister.getPump());
					//hardcoded above to 1cc
					msg.getShotList().add(colorant);
					numCanisters++;

				}
				if(numCanisters==8){
					this.Dispense(msg);
					if(msg.getCommandRC()==0){
						numCanisters=0;//reset counter of items to be dispensed, but still keep going through the loop
						msg.getShotList().clear();
					}
					else{
						numCanisters=0; // command error.  break out of loop
						logger.error("Purge All Failed with rc=" + msg.getCommandRC() + " Error Message:" + msg.getErrorMessage());
						break;
					}
				}
			}
			//now dispense second 8
			if(numCanisters > 0){
				this.Dispense(msg);
			}
		}
		if(loop_counter == 0){
			msg.setErrorMessage("Could not obtain canister layout for purge.  Configure tinter and try again.");
			msg.setErrorNumber(-116);
		}
	}
	public ErrorMessages getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(ErrorMessages errorMessages) {
		this.errorMessages = errorMessages;
	}



	public Configuration getConfiguration() {
		return configuration;
	}
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}


	public boolean isHardwareError() {
		return hardwareError;
	}

	public void setHardwareError(boolean hardwareError) {
		this.hardwareError = hardwareError;
	}

}
