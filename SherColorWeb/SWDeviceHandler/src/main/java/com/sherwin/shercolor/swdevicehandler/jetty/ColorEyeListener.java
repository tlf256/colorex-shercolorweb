package com.sherwin.shercolor.swdevicehandler.jetty;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.util.log.Log;
//import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;
import org.ini4j.Profile.Section;

// long term, we do not want these here - these should be loaded
// on an as needed basis.
//import com.sherwin.spectro.xrite.Ci62Impl;
//import com.sherwin.spectro.xrite.Ci52SWSImpl;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sherwin.shercolor.swdevicehandler.device.AbstractSpectroImpl;
import com.sherwin.shercolor.swdevicehandler.domain.Configuration;
import com.sherwin.shercolor.swdevicehandler.domain.Message;
import com.sherwin.shercolor.swdevicehandler.domain.SpectroConfiguration;
import com.sherwin.shercolor.swdevicehandler.domain.SpectroMessage;
import com.sherwin.shercolor.swdevicehandler.domain.TinterMessage;
import com.sherwin.shercolor.swdevicehandler.jetty.TinterListener.ReadFromDeviceQueueThread;
import com.sherwin.shercolor.swdevicehandler.view.TheUserConsole;


public class ColorEyeListener implements WebSocketListener {
	//static Logger logger = LogManager.getLogger(ColorEyeListener.class);
    private static final Logger LOG = Log.getLogger(ColorEyeListener.class);
    
    private Integer testme=0;
    private String mName;
    private Session mSession;
	private String spectroClass;
	private boolean spectroListenerStarted=false;


	  private final static BlockingQueue<String> listenerQueue= new LinkedBlockingQueue<>();
	
	//final private Ci62Impl ci62 = new Ci62Impl();
	private AbstractSpectroImpl spectro = null;
	
	private AbstractSpectroImpl getSpectro(String className){
		if (spectro==null){

			try {
				testme++;
				spectro = (AbstractSpectroImpl)Class.forName(className).newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				//logger.error(e.getMessage());
				e.printStackTrace();
			} catch (Exception ex) {
				//logger.error(ex.getMessage());
				ex.printStackTrace();
			}
		}
		return this.spectro;
	}
	
	private boolean DetectSpectro(SpectroMessage theMsg, SpectroConfiguration localConfig) {
		boolean returnStatus = false;
		try {
			if (localConfig!=null) {
				if (localConfig.getModel()!=null) {
					returnStatus = true;
				}
			}

		} catch (Exception e) {
			LOG.warn(e.getMessage());
		}
		return returnStatus;
	}
	
	private String ConfigureSpectro(SpectroMessage theMsg, SpectroConfiguration localConfig) {
		int spectroSoftwareReturnCode = 0;
		String returnString = "";
		
		try {
			//first, confirm that the requested spectro software is installed.
			spectroSoftwareReturnCode = VerifySpectroSoftwareInstalled(theMsg.getModel());
			
			if (spectroSoftwareReturnCode==0) {
				//The software is in place.  return control to main listener routine, let the spectro class
				//be instantiated and call the spectro specific CONFIGURE routine.
				returnString = "";
			} else {
				//Post a user friendly return string.  This can be displayed back to the user.
				switch(spectroSoftwareReturnCode) {
				case 1:
					returnString = "1=./spectro/SPECTRO.INI not found.  Confirm file and directory are present.";
					break;
				case 2:
					returnString = "2=DLL required for specified color eye was not found.  Please confirm DLL is installed in the appropriate directory.";
					break;
				case 3:
					returnString = "3=SPECTRO.INI is an invalid INI file format.";
					break;
				case 4:
					returnString = "4=An unexpected error occurred while verifying that the color eye software is installed on local PC. See SWDeviceHandler log for details.";
					break;
				case 5:
					returnString = "5=SPECTRO.INI is missing the Drivers section.";
					break;
				default:
					returnString = "99=An unexpected error occurred while verifying that the color eye software is installed on local PC.";
					break;
				
				}
			}
		} catch (Exception e) {
			returnString = e.getMessage();
		}
		return returnString;
		
	}
	
	public String getSWDeviceHandlerVersion() {
    	String result = "";
    	String line = "";
    	//Get file from resources folder
    	
    	InputStream in = getClass().getResourceAsStream("/version.txt"); 
    	BufferedReader reader = new BufferedReader(new InputStreamReader(in));

    	try  {

    	    while ((line = reader.readLine()) != null)
    	    {
    	        result=line;
    	    }

    		reader.close();
    		LOG.info("SWDeviceHandler version " + result);
    	} catch (IOException e) {
    		LOG.warn(e.getMessage());
    	} catch (Exception e) {
    		LOG.warn(e.getMessage());
    	}

    	return result;
	}
	
	private String getBitness() {
		//routine to check the bitness (32 bt vs 64 bit) of Java that the PC/SWDeviceHandler is running under.
		//We will use this to determine which version of the spectro DLL to use.  We will also log this info 
		//into the log and onto the console, for troubleshooting purposes.
		String returnedBits = "";
		
		try {
			returnedBits = System.getProperty("sun.arch.data.model");
			System.out.println("Running java in " + returnedBits + " bit mode.");
			LOG.info("Running java in " + returnedBits + " bit mode.");
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			System.out.println("Unable to determine what bitness of Java is running.");
		}
		return returnedBits;
	}
	

	private int VerifySpectroSoftwareInstalled(String spectroModel){
		//returnCode messages:
		//  0 - requested spectro software installed.
		//  1 - c:\swdevicehandler\spectro\SPECTRO.INI not found.  Confirm file and directory present.
		//  2 - DLL required for specified spectro not found.  Confirm spectro DLL is loaded in appropriate directory.
		//  3 - SPECTRO.INI is an invalid INI file format.
		//  4 - An unknown/unexpected error occurred.
		//  5 - SPECTRO.INI is missing the Drivers section.

		//9/29/2017-BKP-Today's assumption is that we will use the whatever DLLs are available in
		//              a SPECIFIC directory - c:\swdevicehandler\spectro.  The DLLs there can be
		//              confirmed for the spectro type by checking SPECTRO.INI, which defines 
		//              what X-Rite spectros are available, and the appropriate DLL to use for it.
		//              The DLL in place in this directory must be the appropriate bitness for the
		//              machine - i.e. no need to determine 32 bit or 64 bit.
		
		//First, check if c:\swdevicehandler\spectro\SPECTRO.INI exists.  If not, there's no spectro
		//software installed.
		//String filename="C:\\swdevicehandler\\spectro\\SPECTRO.INI";
		String filename="./spectro/SPECTRO.INI";

		try {
			File fconfig = new File(filename);
			if(!fconfig.exists()){
				return 1;
			}
			
			//SPECTRO.INI exists.  Read it in, and check in the [DRIVERS] section for the passed in 
			//spectro (spectros that have their software/DLLs installed.)
			Ini spectroIni = new Ini();
			try {
				spectroIni.load(fconfig);
			} catch (InvalidFileFormatException iffe) {
				//INI file is corrupt
				return 3;
			}
			Section driverSection = spectroIni.get("Drivers");
			
			if (driverSection==null) {
				return 5;
			}
			
			String theDllName = driverSection.fetch(spectroModel);
			
			//theDllName = "C:\\swdevicehandler\\spectro\\" + driverSection.fetch(spectroModel);
			theDllName = "./spectro/" + driverSection.fetch(spectroModel);
			
			//Confirm that the DLL file is present in this directory.
			File fDll = new File(theDllName);
			if(!fDll.exists()){
				return 2;
			} 
		
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return 4;
		}

		return 0;

	}
	
	 public void initSpectroQueueListener(){
		 if(this.isSpectroListenerStarted()==false){
			 getSpectro(getSpectroClass()).init();//init to queue
			 setSpectroListenerStarted(true);
			
			 //init from queue
			ReadFromDeviceQueueThread readFromQ = new ReadFromDeviceQueueThread();
			readFromQ.setMyQueue(getSpectro(getSpectroClass()).getFromDeviceQueue());
			readFromQ.start();
			
			//Check the bitness once here, mainly for startup logging.
			String junkstring = getBitness();
			String junkstring2 = getSWDeviceHandlerVersion();
			

		 }
	 }
	 
	 public void initConsoleQueueListener(){

	 }
	 
	 private void disconnect() {
		 if (mSession != null && mSession.isOpen()) {
			 mSession.close();
		 }
	 }
    
	 
    @Override
    public void onWebSocketConnect(Session session) {
    	LOG.info("onWebSocketConnect: {}", session.getRemoteAddress());
    	   
    	try {
    	   
    		initConsoleQueueListener();
    		
    		mSession = session;

    	} catch (Exception ex) {
    		LOG.warn(ex);
			ex.printStackTrace();
    	}
           

    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
    	LOG.info("onWebSocketClose: {} - {}", statusCode, reason);
        disconnect();
    }

    @Override
    public void onWebSocketError(Throwable cause) {
    	LOG.warn("onWebSocketError", cause);
        disconnect();
    }
    
    @Override
    public void onWebSocketText(String json) {

    	LOG.info("onWebSocketText: {}", json);
		if(json != null){

    	try {
			JsonParser parser = new JsonParser();
			JsonObject obj = parser.parse(json).getAsJsonObject();
			String messageName = new String();
			try{
				messageName = (String)(obj.get("messageName").getAsString());
			}
			catch(Exception e){
				e.printStackTrace();
				try {
					getmSession().getRemote().sendString("Exception with Parsing Message Name");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			if(messageName.equalsIgnoreCase("SpectroMessage")){
				String PCSpectroModel = null;
				String RemoteSpectroModel = null;
				String StartupSpectroModel = null;
				
				SpectroConfiguration PCConfiguration = SpectroConfiguration.ReadFromDisk(null);
				if (PCConfiguration!=null) {
					PCSpectroModel = PCConfiguration.getModel();
				}
				

				//We have retrieved spectro setting from the PC.  Now see if there's one in the message.
				SpectroMessage msg = new Gson().fromJson(json,SpectroMessage.class);
				if(msg != null){
					String spectroCommand = msg.getCommand().toUpperCase();
	
					switch (spectroCommand) {
					case "DETECT":
						//We need to read in the PC's local configuration (if present) to determine the 
						//model for Jar startup.
						if(!DetectSpectro(msg,PCConfiguration)) {
							//return the message completed as "false".
							//20171031-BKP-at this point, we know that it is not even configured.  Return that.
							msg.setResponseMessage("notconfigured");
							json = new Gson().toJson(msg);
							try {
								getmSession().getRemote().sendString(json );
							} catch (IOException e) {
								LOG.warn("Send Error", e.getCause());
								e.printStackTrace();
							}
							return;
						} else {
							//set the startup spectro model from the PC's conf file
							//and continue.  The Detect method in the specific model's jar
							//will check that the configured device is actually connected and
							//return the message with a true/false response.
							StartupSpectroModel = PCSpectroModel;
							//also, check the message and if the model and serial number are not set, set them.
							String msgModel = msg.getSpectroConfig().getModel();
							String msgSerial = msg.getSpectroConfig().getSerial();
							if (msg.getSpectroConfig().getModel()==null || msg.getSpectroConfig().getModel().isEmpty()) {
								msg.setSpectroConfig(PCConfiguration);
							}
						}
						break;
					case "CONFIGURE":
						String errMsg = ConfigureSpectro(msg,PCConfiguration);
						if (errMsg.isEmpty()) {
							//empty error means the software is in place.  start the queue for the specified model pass this
							//configure message through.
							StartupSpectroModel = msg.getSpectroConfig().getModel();
						} else {
							//error.  Split the errMsg on =, post the error code to response code and error to error message
							//and return the string.
							String errCodeNMsg[] = errMsg.split("=",2);
							try {
							msg.setErrorCode(Integer.parseInt(errCodeNMsg[0]));
							} catch (NumberFormatException nfe) {
								msg.setErrorCode(0);
							}
							msg.setErrorMessage(errCodeNMsg[1]);
							json = new Gson().toJson(msg);
							try {
								getmSession().getRemote().sendString(json );
							} catch (IOException e) {
								LOG.warn("Send Error", e.getCause());
								e.printStackTrace();
							}
							return;
						}
						break;
					default:
						//any other command, just pass through, as the spectro model info in the message 
						//should be assumed correct.
						StartupSpectroModel = PCSpectroModel;
						break;
					}

	    	
		    		if(StartupSpectroModel.startsWith("Ci52")){
		    			//20180502*BKP*Added code to detect the Ci52+SWW device
		    			if (StartupSpectroModel.endsWith("SWW")) {
		    				setSpectroClass("com.sherwin.spectro.xrite.Ci52SWWImpl"); 
		    			} else {
		    				setSpectroClass("com.sherwin.spectro.xrite.Ci52SWSImpl"); 
		    			}
					}
					else if(StartupSpectroModel.startsWith("Ci62") ){
		    			//20180502*BKP*Added code to detect the Ci62+SWW device
		    			if (StartupSpectroModel.endsWith("SWW")) {
		    				setSpectroClass("com.sherwin.spectro.xrite.Ci62SWWImpl"); 
		    			} else {
		    				setSpectroClass("com.sherwin.spectro.xrite.Ci62SWWImpl"); 	
		    			}
					} 
					else {
						setSpectroClass("com.sherwin.shercolor.swdevicehandler.device.SimSpectro"); 
						LOG.warn("initializing SimSpectro");
					}
					initSpectroQueueListener();
					
					try {
						getSpectro(getSpectroClass()).getToDeviceQueue().put(msg);
	
						System.out.println(json +" command received from:" + getmSession().getRemoteAddress());
					} catch (InterruptedException e) {
						LOG.warn("Queueput Error", e.getCause());
					} catch (Exception ee) {
						LOG.warn("Unexpected error", ee.getCause());
					}
		    	       	
				}
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
    }

    @Override
    public void onWebSocketBinary(byte[] arg0, int arg1, int arg2) {
    	LOG.info("onWebSocketBinary");
        disconnect();
    }

	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public Session getmSession() {
		return mSession;
	}

	public void setmSession(Session mSession) {
		this.mSession = mSession;
	}

	public static BlockingQueue<String> getListenerqueue() {
		return listenerQueue;
	}

	 
	public void setSpectro(AbstractSpectroImpl spectro) {
		this.spectro = spectro;
	}
	  
	public String getSpectroClass() {
		return spectroClass;
	}
	public void setSpectroClass(String spectroClass) {
		this.spectroClass = spectroClass;
	}
	
	public boolean isSpectroListenerStarted() {
		return spectroListenerStarted;
	}
	
	public void setSpectroListenerStarted(boolean spectroListenerStarted) {
		this.spectroListenerStarted = spectroListenerStarted;
	}
	
	class  ReadFromDeviceQueueThread extends Thread{
		private BlockingQueue<? extends Message> myQueue=null;

		public void run() {
			//do this when thread executes
			SpectroMessage msg=null;
			while (true){
				try {
					msg =  (SpectroMessage) getMyQueue().take();
					if(msg != null){
						String json = new Gson().toJson(msg);
						getmSession().getRemote().sendString(json );
						//System.out.println(json +" sent from console to webpage");
					}

					//Block the thread for a short time, but be sure
					//to check the InterruptedException for cancellation

					Thread.sleep(100);
				} catch (InterruptedException | IOException ex) {
					LOG.info(ex.getMessage());
				}
			}
		}

		public BlockingQueue<? extends Message> getMyQueue() {
			return myQueue;
		}

		public void setMyQueue(BlockingQueue<? extends Message> blockingQueue) {
			this.myQueue = blockingQueue;
		}
	}
}