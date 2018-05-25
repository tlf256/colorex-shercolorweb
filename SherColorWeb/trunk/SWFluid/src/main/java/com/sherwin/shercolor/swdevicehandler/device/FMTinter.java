package com.sherwin.shercolor.swdevicehandler.device;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.zeroturnaround.exec.InvalidExitValueException;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;
import org.zeroturnaround.exec.StartedProcess;

import com.google.gson.Gson;
import com.sherwin.shercolor.swdevicehandler.domain.Calibration;

import com.sherwin.shercolor.swdevicehandler.domain.Configuration;

import com.sherwin.shercolor.swdevicehandler.domain.FMCalibration;

import com.sherwin.shercolor.swdevicehandler.domain.Message;
import com.sherwin.shercolor.swdevicehandler.domain.TinterMessage;

public class FMTinter extends AbstractTinterImpl implements TinterInterface {
	//private Socket socket;
	private static boolean socketServerStarted = false;
	
	static Logger logger = LogManager.getLogger(FMTinter.class);

	private static StartedProcess process;
	/*
	 * We will create a new socket 55218, write to it/read from it, then close it, each time we want to do something.
	 */
	private Socket socket = null;
	
	

	Configuration configuration = new Configuration();
	//get initial config from disk and store in memory.
	public FMTinter(){
		this.setConfiguration(Configuration.ReadFromDisk(null));
		this.DetectAtStartup();

	}
	public FMTinter(boolean startSocketServer){
		this.setConfiguration(Configuration.ReadFromDisk(null));
		if(startSocketServer==true){
			this.DetectAtStartup();
		}

	}
	public FMTinter(boolean startSocketServer, String test){
		this.setConfiguration(Configuration.ReadFromDisk(test));
		if(startSocketServer==true){
			this.DetectAtStartup();
		}

	}
	@Override
	protected
	void ProcessQueueItem(Message m){
		TinterMessage msg = null;
		System.out.println("Received item in FM queue.");


		if(m != null  && m.getMessageName()!=null){
			switch(m.getMessageName()){
			case "TinterMessage":
				msg = (TinterMessage)m;	

				String command = msg.getCommand();

				if(command !=null){
					/*		Configuration new_config = msg.getConfiguration();
					if(new_config !=null  && new_config.getCanisterLayout() != null){
						try{
							this.setConfiguration(new Configuration(new_config));
						}
						catch(Exception e){
							logger.error("stacktrace",e);
						}
					}
					 */		
					switch(command){
					case "Agitate":
						System.out.println(this.getClass().getSimpleName() + " Sending " + command + " to device" );
						Agitate(msg);
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
						logger.info(this.getClass().getSimpleName() + " Sending " + command + " to device");
						Detect(msg);
						System.out.println("rc=" + msg.getErrorNumber() + " " +  msg.getErrorMessage());
						logger.info("rc=" + msg.getErrorNumber() + " " +  msg.getErrorMessage());
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
	@Override
	public void InitStatus(TinterMessage msg){
		ExecuteCommand(msg);
	}
	public void StartFMDriver(TinterMessage msg){
		String ipAddress = "127.0.0.1";

		Integer i  = new Integer("0");
		try{
			this.setSocket(new Socket(ipAddress,55218));
			this.getSocket().setKeepAlive(false);
			this.getSocket().setSoLinger(false, 0);
			this.getSocket().setSoTimeout(79000); // set read timeout to 79 secs
			BufferedReader in = new BufferedReader(
					new InputStreamReader(this.getSocket().getInputStream()));
			PrintWriter out = new PrintWriter(this.getSocket().getOutputStream(), true);

			//out.println(json);
			//System.out.println("Sending " + json);
			//out.flush();
			String resp_json = "";
			StringBuffer buf = new StringBuffer();
			int c = 0;

			try{
				resp_json = in.readLine();
				if(resp_json!=null && resp_json.length() > 0 && resp_json.charAt(0)=='{'){
					TinterMessage msg2 = new Gson().fromJson(resp_json,TinterMessage.class);
					if(msg2.getErrorMessage() != null){
						if(msg2.getId().contentEquals(msg.getId())){
							msg.setErrorMessage(msg2.getErrorMessage());

							msg.setErrorNumber(msg2.getErrorNumber());
							msg.setLastInitDate(msg2.getLastInitDate());
						}
					}
				}
				this.getSocket().close();
				
			}
			catch( java.net.SocketTimeoutException|java.net.ConnectException ex){
				logger.error("Time out waiting for response from FM Tinter Socket Server");
				msg.setErrorMessage("Time out waiting for response from FM Tinter Socket Server");
				msg.setErrorNumber(-7);
				if(!this.getSocket().isClosed())	this.getSocket().close();
			}
			catch( Exception ex){
				logger.error(ex.getMessage());
				msg.setErrorMessage(ex.getMessage());
				msg.setErrorNumber(-1);
				if(!this.getSocket().isClosed())	this.getSocket().close();
			}
		}
		catch(Exception e){
			logger.error(e.getMessage());
			msg.setErrorNumber(-1);
			msg.setErrorMessage(e.getMessage());
			logger.error("stacktrace",e);
		}
	}

	private void OpenDriverProgressSocket(TinterMessage msg){
		boolean connectionError = false;
		String connectionMessage=null;
		int tries = 0;
		do{
			
		try{
			connectionError = false;
			String ipAddress = "127.0.0.1";
		
			this.setSocket(new Socket(ipAddress,55218));
			this.getSocket().setKeepAlive(false);
			this.getSocket().setSoLinger(false, 0);
			this.getSocket().setSoTimeout(25000); // set read timeout to 25 secs since we are getting progress
			tries = 0;
			logger.error("*Socket Connected*");
		}
		catch( Exception ex){
			connectionError = true;
			ex.printStackTrace();
			connectionMessage = ex.getMessage();
			logger.error("stacktrace",ex);
			if(this.getSocket()!=null){
				try {
					this.getSocket().close();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			try {
				Thread.sleep(1000);
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tries++;
			logger.error(" Tinter Message:" + msg.getCommand() + " tries:" + tries );			
			
		}
		}while(this.getSocket()== null || !this.getSocket().isConnected() || this.getSocket().isClosed() || (tries > 0 && tries < 5));
		if(tries >= 5){
			msg.setErrorMessage("Socket Connection javaMessage:" + connectionMessage);
			msg.setErrorNumber(-1);
		}
	}
	
	/**
	 * @param msg
	 * @param respond
	 * loop sending InitStatus command to .NET while the .NET is starting up and triggering events that are updating the lastDetect message.
	 * The InitStatus will read whatever the current LastDetect is and send it to us.
	 * This is all the workaround because we have to restart the socket server everytime we want to change the XML file.
	 */
	public void StartDriverProgress(TinterMessage msg, boolean respond){ //Send command that sends back status updates (right now only Init, but Dispense in future)

		String id = msg.getId();
		logger.info("Execute Command with Progress.");

		OpenDriverProgressSocket(msg);
		if(msg.getErrorNumber() != -1){
			try{
			
				BufferedReader in = new BufferedReader(
						new InputStreamReader(this.getSocket().getInputStream()));
				msg.setCommand("InitStatus");
				PrintWriter out = new PrintWriter(this.getSocket().getOutputStream(), true);
				logger.error("about to Gson to Json:" + msg.getCommand());
				String json = new Gson().toJson(msg);
				logger.error("finished Gson to Json:" + msg.getCommand());
				msg.setCommand("Detect");  // command is actually Detect so switch it to that when sending back to the web page.
				String resp_json = "";
				StringBuffer buf = new StringBuffer();
				int c = 0;

				do{
					out.println(json); // send command to .NET
					out.flush();

					try{
						resp_json = in.readLine();
						System.out.println("recvd:"+resp_json);
						logger.error("resp_json:" + resp_json);
						if(resp_json !=null && resp_json.length() > 0 && resp_json.charAt(0)=='{'){
							TinterMessage msg2 = new Gson().fromJson(resp_json,TinterMessage.class);
							if(msg2.getId().contentEquals(msg.getId())){
								if(msg2.getErrorMessage() != null){
									msg.setErrorMessage(msg2.getErrorMessage());

									msg.setErrorNumber(msg2.getErrorNumber());
									if(!msg.getErrorMessage().contains("Initialization Done") && msg.getErrorNumber() >= 0){ //in progress or warning
										if(respond == true){
											//get everything to line up
											if(msg.getErrorMessage().contains("Parameter Check")){
												int index1 = msg.getErrorMessage().indexOf("Board");
												int index1b = msg.getErrorMessage().indexOf("14") + 2;
												if(index1b==1 || index1b < 0) index1b = msg.getErrorMessage().indexOf("17") + 2;
												int index2 = msg.getErrorMessage().indexOf("Upd");
												//logger.info("index1b:" + index1b);
												if(index1 > 1 && index1b>1 && index2 > 1){
													//logger.info("index1b inside:" + index1b);
													String newmsg = msg.getErrorMessage().substring(index1, index1b) + " " + msg.getErrorMessage().substring(index2);
													msg.setErrorMessage(newmsg);
												}

											}

											this.Respond(msg);  // only if you think the webpage is going to read all of these progress updates.
										}
										System.out.println( msg.getErrorNumber() + "+" +msg.getErrorMessage());
									}
									else{
										System.out.println( msg.getErrorMessage());
										break; //normal response will send final message
									}
								}	
							}
							else{
								logger.error("Message id recvd from Tinter: " + msg2.getId() + " does not equal message sent:" + msg.getId());
							}
						}
					}
					catch( java.net.SocketTimeoutException ex){
						logger.error("Time out waiting for response from FM Tinter Socket Server");
						if(msg.getErrorMessage() == null || msg.getErrorMessage().length() == 0 || msg.getErrorMessage().contentEquals("0")){
							msg.setErrorMessage("Time out waiting for response from FM Tinter Socket Server");
							msg.setErrorNumber(-7);
						}
						if(!this.getSocket().isClosed())	this.getSocket().close();
						break;
					}
				} while(true);
				this.getSocket().close();
			}
			catch( Exception ex){
				ex.printStackTrace();
				logger.error("stacktrace",ex);

				logger.error("JavaMessage:" + ex.getMessage() + "from Tinter Message:" + msg.getCommand());
				msg.setErrorMessage("JavaMessage:" + ex.getMessage() + " Tinter Message:" + msg.getCommand());
				msg.setErrorNumber(-1);
			}
		}

	}
	/*
	public void ExecuteCommandWithProgress(TinterMessage msg, boolean respond){ //Send command that sends back status updates (right now only Init, but Dispense in future)
		String ipAddress = "127.0.0.1";
		String id = msg.getId();
		logger.info("Execute Command with Progress.");
		try{
			Socket socket = new Socket(ipAddress,55218);
			if(this.getConfiguration().getModel().contains("IFC") ){
				switch(msg.getCommand()){
				case "Dispense": 
					//TODO find greatest shot size and set timeout accordingly by model
					this.getSocket().setSoTimeout(240000); // the dispense is so slow for a DVX, 17oz/min = 600rpm
					break;
				case "PurgeAll":
					this.getSocket().setSoTimeout(180000); // the dispense is so slow for a DVX, 600rpm
					break;
				default:
					this.getSocket().setSoTimeout(35000); // the dispense is so slow for a DVX, 600rpm
					break;


				}
			}
			else{ //FM = 960rpm or 30oz/min
				switch(msg.getCommand()){
				case "Dispense": 
					//TODO find greatest shot size and set timeout accordingly by model
					this.getSocket().setSoTimeout(180000); // the dispense is so slow for a DVX, 17oz/min = 600rpm
					break;
				case "PurgeAll":
					this.getSocket().setSoTimeout(35000); // the dispense is so slow for a DVX, 600rpm
					break;
				default:
					this.getSocket().setSoTimeout(35000); // the dispense is so slow for a DVX, 600rpm
					break;


				}
				}

			BufferedReader in = new BufferedReader(
					new InputStreamReader(this.getSocket().getInputStream()));
			PrintWriter out = new PrintWriter(this.getSocket().getOutputStream(), true);
			String json = new Gson().toJson(msg);
			out.println(json); // send command to .NET
			out.flush();
			String resp_json = "";
			StringBuffer buf = new StringBuffer();
			int c = 0;

			do{
				try{
					resp_json = in.readLine();
					if(resp_json.length() > 0){
						TinterMessage msg2 = new Gson().fromJson(resp_json,TinterMessage.class);
						if(msg2.getId().contentEquals(msg.getId())){
							msg.setErrorMessage(msg2.getErrorMessage());

							msg.setErrorNumber(msg2.getErrorNumber());
							if(!msg.getErrorMessage().contains("Initialization Done") && msg.getErrorNumber() >= 0){ //in progress or warning
								if(respond == true){
									if(msg.getErrorMessage().contains("Parameter Check")){

										int index1 = msg.getErrorMessage().indexOf("Board");
										int index2 = msg.getErrorMessage().indexOf("Upd");
										if(index1 > 0 && index2 > 0){
											String newmsg = msg.getErrorMessage().substring(index1, 53) + " " + msg.getErrorMessage().substring(index2);
											msg.setErrorMessage(newmsg);
										}
									}
									this.Respond(msg);  // only if you think the webpage is going to read all of these progress updates.
								}
								System.out.println( msg.getErrorNumber() + "+" +msg.getErrorMessage());
							}
							else{
								System.out.println( msg.getErrorMessage());
								break; //normal response will send final message
							}
						}	
						else{
							logger.error("Message id recvd from Tinter: " + msg2.getId() + " does not equal message sent:" + msg.getId());
						}
					}
				}
				catch( java.net.SocketTimeoutException ex){
					ex.printStackTrace();
					logger.error("Time out waiting for response from FM Tinter Socket Server");
					if(msg.getErrorMessage().length() == 0 || msg.getErrorMessage().contentEquals("0")){
						msg.setErrorMessage("Time out waiting for response from FM Tinter Socket Server");
						msg.setErrorNumber(-7);
					}
					if(!this.getSocket().isClosed())	this.getSocket().close();
					break;
				}
			} while(true);
			this.getSocket().close();
		}
		catch( Exception ex){
			ex.printStackTrace();
			logger.error("JavaMessage:" + ex.getMessage() + " Tinter Message:" + msg.getErrorMessage());
			msg.setErrorMessage("JavaMessage:" + ex.getMessage() + " Tinter Message:" + msg.getErrorMessage());
		}


	}
	 */
	public void ExecuteCommand(TinterMessage msg){  //Send command to FMSocketServer
		String ipAddress = "127.0.0.1";
		String id = msg.getId();
		Integer i  = new Integer("0");
		try{
			this.setSocket(new Socket(ipAddress,55218));
			this.getSocket().setKeepAlive(false);
			this.getSocket().setSoLinger(false, 0);
			if(this.getConfiguration().getModel().contains("IFC") ){
				switch(msg.getCommand()){
				case "Dispense": 
					//TODO find greatest shot size and set timeout accordingly by model
					this.getSocket().setSoTimeout(240000); // the dispense is so slow for a DVX, 17oz/min = 600rpm
					break;
				case "PurgeAll":
					this.getSocket().setSoTimeout(180000); // the dispense is so slow for a DVX, 600rpm
					break;
				default:
					this.getSocket().setSoTimeout(35000); // the dispense is so slow for a DVX, 600rpm
					break;


				}
			}
			else{ //FM = 960rpm or 30oz/min
				switch(msg.getCommand()){
				case "Dispense": 
					//TODO find greatest shot size and set timeout accordingly by model
					this.getSocket().setSoTimeout(180000); // the dispense is so slow for a DVX, 17oz/min = 600rpm
					break;
				case "PurgeAll":
					this.getSocket().setSoTimeout(35000); // the dispense is so slow for a DVX, 600rpm
					break;
				default:
					this.getSocket().setSoTimeout(35000); // the dispense is so slow for a DVX, 600rpm
					break;


				}
			}
			BufferedReader in = new BufferedReader(
					new InputStreamReader(this.getSocket().getInputStream()));
			PrintWriter out = new PrintWriter(this.getSocket().getOutputStream(), true);
			String json = new Gson().toJson(msg);
			out.println(json);
			System.out.println("Sending " + json);
			out.flush();
			String resp_json = "";
			StringBuffer buf = new StringBuffer();
			int c = 0;
			/*
			while(!in.ready()){
				i = i + 1;
				System.out.println("Thinking about it:" + i);
			}
			 */
			try{
				if(!msg.getCommand().contentEquals("Quit")){
					resp_json = in.readLine();
					if(resp_json!=null && resp_json.length() > 0 && resp_json.charAt(0)=='{'){
						TinterMessage msg2 = new Gson().fromJson(resp_json,TinterMessage.class);
						if(msg2.getId().contentEquals(msg.getId())){//protect against a different message coming back than the one we expected. 
																	//just ignore it, if that is the case.
							msg.setErrorMessage(msg2.getErrorMessage());
							msg.setErrorList(msg2.getErrorList());
							msg.setErrorNumber(msg2.getErrorNumber());
							msg.setLastInitDate(msg2.getLastInitDate());
						}
					}
				}
				this.getSocket().close();
			}
			catch( java.net.SocketTimeoutException |java.net.ConnectException ex){
				logger.error("Time out waiting for response from FM Tinter Socket Server");
				msg.setErrorMessage("Time out waiting for response from FM Tinter Socket Server");
				msg.setErrorNumber(-7);
				if(!this.getSocket().isClosed())	this.getSocket().close();
			}
			catch( Exception ex){
				ex.printStackTrace();
				logger.error(ex.getMessage());
				msg.setErrorMessage(ex.getMessage());
				msg.setErrorNumber(-1);
				if(!this.getSocket().isClosed())	this.getSocket().close();
			}
		}
		catch( java.net.SocketTimeoutException |java.net.ConnectException ex){
			logger.error("Time out waiting for response from FM Tinter Socket Server");
			msg.setErrorMessage("Time out waiting for response from FM Tinter Socket Server");
			msg.setErrorNumber(-7);

		}
		catch(Exception e){
			logger.error("stacktrace",e);
			logger.error(e.getMessage());
			msg.setErrorNumber(-1);
			msg.setErrorMessage(e.getMessage());
			e.printStackTrace();
		}
	}


	@Override
	public void Detect(TinterMessage msg) {

		System.out.println("Detecting");
		logger.info("Detecting");
		//1st iterate serial ports to find one with tinter.


		StopFMSocketServer();
        
		StartFMSocketServer();

		StartDriverProgress(msg,true);

		logger.info("Detect result" + msg.getErrorNumber() + " " +  msg.getErrorMessage());


		//save this result into the last detect message for retrieval
		//anytime someone wants the last detect status!
		final DateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		String date = (sdf.format(Calendar.getInstance().getTime()));
		msg.setLastInitDate(Long.parseLong(date));
		this.setLastInitMessage(msg);

	}
	public void DetectAtStartup() {

		System.out.println("Detecting");
		logger.info("Detecting");
		//1st iterate serial ports to find one with tinter.




		StartFMSocketServer();
		TinterMessage msg = new TinterMessage();
		msg.setMessageName("TinterMessage");
		msg.setCommand("StartDriver");
		msg.setId("StartDriver");
		StartDriverProgress(msg,false);

		logger.info("Detect result" + msg.getErrorNumber() + " " +  msg.getErrorMessage());


		//save this result into the last detect message for retrieval
		//anytime someone wants the last detect status!
		final DateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		String date = (sdf.format(Calendar.getInstance().getTime()));
		msg.setLastInitDate(Long.parseLong(date));
		this.setLastInitMessage(msg);

	}

	public void Agitate(TinterMessage msg) {
		System.out.println("Agitating");
		ExecuteCommand(msg);
		logger.info("/M result" + msg.getErrorNumber() + " " +  msg.getErrorMessage());
		System.out.println("rc=" + msg.getErrorNumber() + " " +  msg.getErrorMessage());
	}

	public void OpenNozzle(TinterMessage msg) {
		System.out.println("Open Nozzle");
		ExecuteCommand(msg);
		logger.info("OpenNozzle result" + msg.getErrorNumber() + " " +  msg.getErrorMessage());
		System.out.println("rc=" + msg.getErrorNumber() + " " +  msg.getErrorMessage());
	}
	public void CloseNozzle(TinterMessage msg) {
		System.out.println("Close Nozzle");
		ExecuteCommand(msg);
		logger.info("/G1 result" + msg.getErrorNumber() + " " +  msg.getErrorMessage());
		System.out.println("rc=" + msg.getErrorNumber() + " " +  msg.getErrorMessage());
	}

	public void Recirculate(TinterMessage msg) {
		System.out.println("Recirc");
		ExecuteCommand(msg);
		logger.info("/J result" + msg.getErrorNumber()+ " " +  msg.getErrorMessage());
		System.out.println("rc=" + msg.getErrorNumber() + " " +  msg.getErrorMessage());
	}



	@Override
	public void Dispense(TinterMessage msg) {

		System.out.println("Dispensing");

		if(msg.getShotList()!=null){
			ExecuteCommand(msg);
			/*
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
							System.out.println("Dispense: Appending position " + colorant.getPosition() + " to dispense");
							itemsToDispense = true;
						}
						else if(colorant.getCode()!=""){
							Long position = this.getConfiguration().getCanisterMap().getCodeMap().get(colorant.getCode());
							System.out.println("Dispense: Appending " + colorant.getCode() + " position " + position + "to dispense");
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
			System.out.println(cmd.toString());
			System.out.println("rc=" + msg.getErrorNumber() + " " +  msg.getErrorMessage());

			//UpdateLevels(); //webpage will do
		}
		else{
			msg.setErrorMessage("Nothing to dispense");
			msg.setErrorNumber(-115);
		}
			 */
		}
	}
	@Override
	public void PurgeAll(TinterMessage msg){
		ExecuteCommand(msg);
	}

	public void CalDownload(TinterMessage msg){
		String exceptionMessage=null;
		Calibration cal =  msg.getCalibration();
		if(cal != null){
			FMCalibration fmCal = new FMCalibration(cal);
			exceptionMessage = fmCal.toDisk();


			msg.setJavaMessage(exceptionMessage);
		}	
	}
	public void CalUpload(TinterMessage msg){		
		FMCalibration fmCal = new FMCalibration();
		Configuration config = this.getConfiguration();
		String datetime =  new SimpleDateFormat( "yyyyMMdd_HHmm" ).format( Calendar.getInstance().getTime());
		String filename= config.getColorantSystem() + "_" + config.getModel() + "_" + config.getSerial()+"_" + datetime; // + Calendar.YEAR + (Calendar.MONTH + 1) + Calendar.DATE + "_" + Calendar.HOUR_OF_DAY + Calendar.MINUTE ;
		String uFilename= filename.toUpperCase() + ".zip";
		uFilename = uFilename.replace(" ", "");   // (/\s+/g, '');
		fmCal.setFilename(uFilename);
		String exceptionMessage = fmCal.fromDisk();
		msg.setCalibration(fmCal);
		msg.setConfiguration(config);
		msg.setJavaMessage(exceptionMessage);

	}
	
	public void Config(TinterMessage msg){
		
		String exceptionMessage = msg.getConfiguration().SaveToDisk(null);
		CalDownload(msg);
		this.setConfiguration(new Configuration(msg.getConfiguration())); // do I need this copy constructor here?  Putting it just to be safe

	
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

	public Configuration getConfiguration() {
		return configuration;
	}
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void StopFMSocketServer(){


		TinterMessage msg = new TinterMessage();
		msg.setMessageName("TinterMessage");
		msg.setCommand("Quit");
		msg.setId("Quit");
		ExecuteCommand(msg);
		try {
			while(FMTinter.isProcessRunning("FMAsyncSocketServer.exe")){
				Thread.sleep(100);
			}
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			logger.error("stacktrace",e);
		}


	}

	public static String  StartFMSocketServer(){

		String output=null;
		String caught = "";
		Long rc = 0L;
		String errorMessage;
		File serverDirectory = new File("FMSocketServer");

		try {
			if(!FMTinter.isProcessRunning("FMAsyncSocketServer.exe") && (FMTinter.process == null || (FMTinter.process != null && !FMTinter.process.getProcess().isAlive())) && serverDirectory.exists()){

				try {
					ProcessExecutor executor = new ProcessExecutor().directory(serverDirectory)
							.command("FMSocketServer/FMAsyncSocketServer.exe")
							.timeout(80, TimeUnit.SECONDS);
					FMTinter.process = executor.destroyOnExit().start();

					System.out.println("FM Socket Server Starting");
					org.apache.logging.log4j.Logger log1 = LogManager.getLogger(FMTinter.class);
					log1.info("FM Socket Server starting");		
					try {
						//wait for Socket server to start.
						while(!FMTinter.isProcessRunning("FMAsyncSocketServer.exe")){
							Thread.sleep(100);
						}
						Thread.sleep(1000); // wait an additional second for things to fire up due to timing issues.
					} catch (IOException | InterruptedException e) {
						// TODO Auto-generated catch block
						logger.error("stacktrace",e);
					}
				} catch (InvalidExitValueException | IOException e ) {
					// TODO Auto-generated catch block
					logger.error("stacktrace",e);
					caught="Error from ProcessExecutor " + e.getMessage();				
				}  
			}
			else{
				caught="FMSocketServer already running";
				System.out.println("FMSocketServer already running.");
			}
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			logger.error("stacktrace",e);
			caught = "Error checking FMSocketServerProcess: " + e.getMessage();
		}
		errorMessage = (caught);

		return errorMessage;
	}		
	// http://stackoverflow.com/a/19005828/3764804
	private static boolean isProcessRunning(String processName) throws IOException, InterruptedException
	{
		ProcessBuilder processBuilder = new ProcessBuilder("tasklist.exe");
		Process process = processBuilder.start();
		String tasksList = toString(process.getInputStream());

		return tasksList.contains(processName);
	}

	// http://stackoverflow.com/a/5445161/3764804
	@SuppressWarnings("resource")
	private static String toString(InputStream inputStream)
	{
		Scanner scanner = new Scanner(inputStream, "UTF-8").useDelimiter("\\A");
		String string = scanner.hasNext() ? scanner.next() : "";
		scanner.close();

		return string;
	}
	
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}





