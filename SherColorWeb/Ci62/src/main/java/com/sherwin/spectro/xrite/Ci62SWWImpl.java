package com.sherwin.spectro.xrite;


import java.math.BigDecimal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sherwin.shercolor.colormath.domain.ColorCoordinates;
import com.sherwin.shercolor.colormath.functions.ColorCoordinatesCalculator;
import com.sherwin.shercolor.colormath.functions.ColorCoordinatesCalculatorImpl;
import com.sherwin.shercolor.colormath.functions.ColorDifferenceCalculator;
import com.sherwin.shercolor.swdevicehandler.device.AbstractSpectroImpl;
import com.sherwin.shercolor.swdevicehandler.domain.Message;
import com.sherwin.shercolor.swdevicehandler.domain.SpectralCurve;
import com.sherwin.shercolor.swdevicehandler.domain.SpectroConfiguration;
import com.sherwin.shercolor.swdevicehandler.domain.SpectroMessage;
import java.util.ResourceBundle;
import java.util.Locale;

import com.sun.jna.NativeLibrary;

public class Ci62SWWImpl extends AbstractSpectroImpl {
	static Logger logger = LogManager.getLogger(Ci62SWWImpl.class);
	private CI62SWWXDSV4DLL sDll;
	private static String DEVICENAME = "Ci62+SWW";
	//force to default to en_US for now...
	
	Locale locale = new Locale("en", "US");
	ResourceBundle resourceBundle = ResourceBundle.getBundle("Ci62SWWErrors",locale);
	
private void CreateDllObj() {
	try {
		NativeLibrary.addSearchPath(getSpectroDllName(), getSpectroDllPath());
		sDll = CI62SWWXDSV4DLL.INSTANCE;
	}catch(NoClassDefFoundError ee) {
		//If we get here, it means that the DLL is probably in place, but the wrong bitness.  The Detect/Configure
		//should have detected the PRESENCE of the DLL - this means the DLL couldn't be accessed from this version of
		//Java (most likely.)
		logger.error(ee.getMessage());
		errorCode = 99;
		errorMsg = "Unable to open " + getSpectroDllPath() + DEVICENAME + ".  Check that the file is present and is the correct version (32/64 bit) for the running version of Java";
		sDll = null;
	} catch (UnsatisfiedLinkError ule) {
		logger.error(ule.getMessage());
		errorCode = 99;
		errorMsg = "Unable to open " + getSpectroDllPath() + DEVICENAME + ".  Check that the file is present and is the correct version (32/64 bit) for the running version of Java";
		sDll = null;
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorCode = 100;
		errorMsg = e.getMessage();
		sDll = null;
	}
}

public Ci62SWWImpl() {
//initialize the Dll object to null
	logger.error("starting Ci62SWW");
	sDll = null;
}

//BKP 7-18-2017 - new code for processing data off QUEUE
@Override
protected void ProcessQueueItem(Message m){
	SpectroMessage msg = (SpectroMessage) m;
	boolean boolResult=false;
	String strResult = "";
	String strBitness = "";
	
	setSpectroDllName(DEVICENAME);
	//Detect whether we are using 32 bit or 64 bit Java, and set the DLL path
	//appropriately.  If unknown, log and return an error.  Ideally, we will 
	//be setting the Dll path from a configuration setting.
//	strBitness = System.getProperty("sun.arch.data.model");
//	if (strBitness.equals("32")) {
//		setSpectroDllPath("C:\\XRite\\XDS4_Ci62_SDK_1.2.2\\Drivers\\Win32");
//	} else if (strBitness.equals("64")) {
//		setSpectroDllPath("C:\\XRite\\XDS4_Ci62_SDK_1.2.2\\Drivers\\x64");
//	} else {
//		this.errorCode = 1;
//		this.errorMsg = "unknown response of JVM bitness - " + strBitness;
//		logger.error(this.errorMsg);
//		return;
//	}
	
	setSpectroDllPath("./spectro/");
	
	//reset the Spectro object's error code and message each time prior to 
	//processing the queue item.
	this.errorCode = null;
	this.errorMsg = null;
	String i18nMessage = "";
	
	try {
		//logger.error("testing");
		if(m != null){
			String str = msg.getCommand();
			str = str.trim();
			String strCmd = str;
			String strArg = "";
			if(str !=null){
				if (str.indexOf(" ")!=-1) {
					strCmd = str.substring(0, str.indexOf(' '));
					strArg = str.substring(str.indexOf(' ') + 1);
				}
				switch(strCmd){
				case "ReadConfig":
					SpectroConfiguration theSpectroConfig = ReadConfig();
					msg.setSpectroConfig(theSpectroConfig);
					try {
						this.getFromDeviceQueue().put(msg);
						//UpdateConsole(msg);
					} catch (InterruptedException e) {
						logger.error(e.getMessage());
						logger.error(e.getStackTrace());
					}
					break;
				case "WriteConfig":
					WriteConfig(msg);
					try {
						this.getFromDeviceQueue().put(msg);
						//UpdateConsole(msg);
					} catch (InterruptedException e) {
						logger.error(e.getMessage());
						logger.error(e.getStackTrace());
					}
					break;
				case "Configure":
					String outputMessage = "";
					outputMessage = Configure(msg, false);
					//note - <00> is what is sent back on a successful configure (actually, what is sent from successful setting
					//of default items.)  The SetOption call to the device returns <00> on success.
					if (!outputMessage.isEmpty() && !outputMessage.equals("<00>")) {
						msg.setResponseMessage(outputMessage);
					}
					if (this.errorCode!=null) {
						msg.setErrorCode(this.getErrorCode());
						i18nMessage = getInternationalErrorMessage(this.getClass().getSimpleName(),msg.getCommand());
						msg.setErrorMessage(i18nMessage);
						//msg.setErrorMessage(this.getClass().getSimpleName() + " - " + msg.getCommand() + " executed with result: " + this.getErrorMsg());
					}
					try {
						this.getFromDeviceQueue().put(msg);
						//UpdateConsole(msg);
					} catch (InterruptedException e) {
						logger.error(e.getMessage());
						logger.error(e.getStackTrace());
					}
					break;
				case "ConfigureOverride":
					String outputMessage2 = "";
					outputMessage2 = Configure(msg, true);
					//note - <00> is what is sent back on a successful configure (actually, what is sent from successful setting
					//of default items.)  The SetOption call to the device returns <00> on success.
					if (!outputMessage2.isEmpty() && !outputMessage2.equals("<00>")) {
						msg.setErrorCode(101);
						msg.setErrorMessage(outputMessage2);
					} else {
						if (this.errorCode!=null) {
							msg.setErrorCode(this.getErrorCode());
							i18nMessage = getInternationalErrorMessage(this.getClass().getSimpleName(),msg.getCommand());
							msg.setErrorMessage(i18nMessage);
							//msg.setErrorMessage(this.getClass().getSimpleName() + " - " + msg.getCommand() + " executed with result: " + this.getErrorMsg());
						}
					}
					try {
						this.getFromDeviceQueue().put(msg);
						//UpdateConsole(msg);
					} catch (InterruptedException e) {
						logger.error(e.getMessage());
						logger.error(e.getStackTrace());
					}
					break;
				case "Detect":
					strResult = Detect();
					if (strResult.equals("true")) {
						//we detected it, let's get the serial number and post the serial number 
						//and model number in all appropriate spots.
						String theSerNo = GetSerialNumber();
						msg.setModel(DEVICENAME);
						SpectroConfiguration thisSpectroConfig = new SpectroConfiguration();
						thisSpectroConfig.setModel(DEVICENAME);
						thisSpectroConfig.setSerial(theSerNo);
						thisSpectroConfig.setPort("USB");
						msg.setSpectroConfig(thisSpectroConfig);
					}
					msg.setResponseMessage(strResult);
					if (this.errorCode!=null) {
						msg.setErrorCode(this.getErrorCode());
						i18nMessage = getInternationalErrorMessage(this.getClass().getSimpleName(),msg.getCommand());
						msg.setErrorMessage(i18nMessage);
						//msg.setErrorMessage(this.getClass().getSimpleName() + " - " + msg.getCommand() + " executed with result: " + this.getErrorMsg());
					}
					try {
						this.getFromDeviceQueue().put(msg);
						//UpdateConsole(msg);
					} catch (InterruptedException e) {
						logger.error(e.getMessage());
						logger.error(e.getStackTrace());
					}
					break;
				case "Measure":
					//Before we even call Measure, let's check the calibration status first.
					if(GetCalStatus()==1) {
						msg.setErrorCode(233);
						i18nMessage = getInternationalErrorMessage(this.getClass().getSimpleName(),msg.getCommand());
						msg.setErrorMessage(i18nMessage);
						//msg.setErrorMessage(this.getClass().getSimpleName() + " - " + msg.getCommand() + ": The device is not calibrated.  Please calibrate the " + DEVICENAME + ", then try measuring again.");
						return;
					}
					SpectralCurve curve = Measure();
					msg.setCurve(curve);
					//set error fields if there was an error.
					if (this.errorCode!=null) {
						msg.setErrorCode(errorCode);
						i18nMessage = getInternationalErrorMessage(this.getClass().getSimpleName(),msg.getCommand());
						msg.setErrorMessage(i18nMessage);
						//msg.setErrorMessage(this.getClass().getSimpleName() + " - " + msg.getCommand() + " executed with result: " + this.getErrorMsg());
					}
					try {
						this.getFromDeviceQueue().put(msg);
						//UpdateConsole(msg);
					} catch (InterruptedException e) {
						logger.error(e.getMessage());
						logger.error(e.getStackTrace());
					} 
	
					break;
				case "SWMeasure":
					//Before we even call Measure, let's check the calibration status first.
					if(GetCalStatus()==1) {
						msg.setErrorCode(233);
						i18nMessage = getInternationalErrorMessage(this.getClass().getSimpleName(),msg.getCommand());
						msg.setErrorMessage(i18nMessage);
						//msg.setErrorMessage(this.getClass().getSimpleName() + " - " + msg.getCommand() + ": The device is not calibrated.  Please calibrate the " + DEVICENAME + ", then try measuring again.");
					} else {
						SpectralCurve swcurve = SWMeasure();
						msg.setCurve(swcurve);
						//set error fields if there was an error.
						if (this.errorCode!=null) {
							msg.setErrorCode(errorCode);
							i18nMessage = getInternationalErrorMessage(this.getClass().getSimpleName(),msg.getCommand());
							msg.setErrorMessage(i18nMessage);
							//msg.setErrorMessage(this.getClass().getSimpleName() + " - " + msg.getCommand() + " executed with result: " + this.getErrorMsg());
						}
					}
					try {
						this.getFromDeviceQueue().put(msg);
						//UpdateConsole(msg);
					} catch (InterruptedException e) {
						logger.error(e.getMessage());
						logger.error(e.getStackTrace());
					}
	
					break;
				case "Calibrate":
					//System.out.println(this.getClass().getSimpleName() + " Sending " + str + " to device" );
					boolResult = Calibrate();
					msg.setResponseMessage(String.valueOf(boolResult)); 
					if (this.errorCode!=null) {
						msg.setErrorCode(errorCode);
						i18nMessage = getInternationalErrorMessage(this.getClass().getSimpleName(),msg.getCommand());
						msg.setErrorMessage(i18nMessage);
						//msg.setErrorMessage(this.getClass().getSimpleName() + " - " + msg.getCommand() + " executed with result: " + this.getErrorMsg());
					}
					try {
						this.getFromDeviceQueue().put(msg);
						//UpdateConsole(msg);
					} catch (InterruptedException e) {
						logger.error(e.getMessage());
						logger.error(e.getStackTrace());
					}
	
					break;

				case "GetClock":
				case "GetCalWhiteTimeout":
				case "GetVolumeLevel":
				case "GetVersion":
				case "GetCalPlaqueSerial":
				case "GetCheckPlaqueSerial":
				case "CalibrateWhite":
				case "CalibrateBlack":
				case "MeasureGreen":
				case "ScanIsSupported":
				case "ClearSamples":
				case "AbortCalibration":
				case "GetSampleCount":
				case "GetSpectralSetCount":
				case "GetWavelengthCount":
				case "GetCalibrationStandard":
				case "GetInterfaceVersion":
				case "GetCalMode":
				case "Ci62DisplayRefreshDisable":
				case "Ci62DisplayRefreshEnable":
				case "Ci62GetGui":
				case "Ci62GetInstSerial":
				case "Ci62GetOEMSerial":
				case "Ci62GetModelType":
				case "Ci62GetClock":
				case "Ci62GetVersion":
				case "Ci62GetCalPlaqueSerial":
				case "Ci62GetCheckPlaqueSerial":
				case "Ci62GetCalStatus":
				case "Ci62GetCalWhiteTimeout":
				case "Ci62GetVolumeLevel":
				case "Ci62GetErrorLog":
				case "Ci62ClearErrorLog":
				case "GetCalStatus":
				case "GetCalStatusMinUntilCalExpiration":
				case "GetCalSteps":
				case "GetCalProgress":
				case "GetLastErrorCode":
				case "GetLastErrorMessage":
				case "SetExtendedReflectanceMode":
				case "GetExtendedReflectanceMode":
				case "GetSerialNumber":
				case "GetAvailableSettings":
					System.out.println(this.getClass().getSimpleName() + " - Sending " + strCmd + " to device" );
					//logger.error(this.getClass().getSimpleName() + " - Sending " + strCmd + " to device");
					try {
						strResult = String.valueOf(this.getClass().getMethod(strCmd).invoke(this));
						msg.setResponseMessage(strResult);
						if (this.errorCode!=null) {
							msg.setErrorCode(this.getErrorCode());
							i18nMessage = getInternationalErrorMessage(this.getClass().getSimpleName(),msg.getCommand());
							msg.setErrorMessage(i18nMessage);
							//msg.setErrorMessage(this.getClass().getSimpleName() + " - " + msg.getCommand() + " executed with result: " + this.getErrorMsg());
						}
						try {
							System.out.println("response-" + msg.getResponseMessage() + " errCode-" + msg.getErrorCode() + " errMsg-" + msg.getErrorMessage());
							this.getFromDeviceQueue().put(msg);
							//UpdateConsole(msg);
						} catch (InterruptedException e) {
							logger.error(e.getMessage());
							logger.error(e.getStackTrace());
						}
					} catch (Exception e1) {
						logger.error(e1.getMessage());
						logger.error(e1.getStackTrace());
						
						//UpdateConsole(e1.getLocalizedMessage());
						try {
							this.getFromDeviceQueue().put(msg);
						} catch (InterruptedException e) {
							logger.error(e.getMessage());
							logger.error(e.getStackTrace());
						}
	
					}
	
					break;
				case "SetClock":
				case "SetOption":
				case "GetOption":
				case "GetSettingOptions":
				case "SetCalWhiteTimeout":
				case "SetVolumeLevel":	
				case "Ci62SetGui":
				case "Ci62SetCalWhiteTimeout":
				case "Ci62SetVolumeLevel":
				case "Ci62SetClock":
					//Use this option for single string argument parameter
					//String parameter
					String[] multiArgs = strArg.split(" ");
					int argCnt = multiArgs.length;
					Class[] paramString = new Class[argCnt];
					for(int cntr=0;cntr<argCnt;cntr++) {
						paramString[cntr] = String.class;
					}
					System.out.println(this.getClass().getSimpleName() + " - Sending " + strCmd + " to device" );
					try {
						strResult = String.valueOf(this.getClass().getDeclaredMethod(strCmd,paramString).invoke(this, multiArgs));
						msg.setResponseMessage(strResult);
						//a successful clock set will return '<00>'.  If not this, return a nice error message.
						if (!strCmd.startsWith("Get")) {
							if (!strResult.equals("<00>")) {
								errorCode = 234;
								errorMsg = strCmd + " UNSUCCESSFUL - check for valid values in argument list " + strArg;
							}
						}
						if (this.errorCode!=null) {
							msg.setErrorCode(this.getErrorCode());
							i18nMessage = getInternationalErrorMessage(this.getClass().getSimpleName(),msg.getCommand());
							msg.setErrorMessage(i18nMessage);
							//msg.setErrorMessage(this.getClass().getSimpleName() + " - " + msg.getCommand() + " executed with result: " + this.getErrorMsg());
						}
						try {
							this.getFromDeviceQueue().put(msg);
							//UpdateConsole(msg);
						} catch (InterruptedException e) {
							logger.error(e.getMessage());
							logger.error(e.getStackTrace());
						}
					} catch (Exception e1) {
						logger.error(e1.getMessage());
						logger.error(e1.getStackTrace());
						
						////UpdateConsole(e1.getLocalizedMessage());
						try {
							this.getFromDeviceQueue().put(msg);
						} catch (InterruptedException e) {
							logger.error(e.getMessage());
							logger.error(e.getStackTrace());
						}
	
					}
	
					break;
				case "Ci62GetCalPlaqueSpin":
				case "Ci62GetCalPlaqueSpex":
				case "Ci62GetCheckPlaqueSpin":
				case "Ci62GetCheckPlaqueSpex":
					System.out.println(this.getClass().getSimpleName() + " - Sending " + strCmd + " to device" );
					try {
						SpectralCurve curveGeneric = (SpectralCurve) this.getClass().getMethod(strCmd).invoke(this);
						msg.setCurve(curveGeneric);
						
						if (this.errorCode!=null) {
							msg.setErrorCode(this.getErrorCode());
							i18nMessage = getInternationalErrorMessage(this.getClass().getSimpleName(),msg.getCommand());
							msg.setErrorMessage(i18nMessage);
							//msg.setErrorMessage(this.getClass().getSimpleName() + " - " + msg.getCommand() + " executed with result: " + this.getErrorMsg());
						}
						try {
							this.getFromDeviceQueue().put(msg);
							//UpdateConsole(msg);
						} catch (InterruptedException e) {
							logger.error(e.getMessage());
							logger.error(e.getStackTrace());
						}
					} catch (Exception e1) {
						logger.error(e1.getMessage());
						logger.error(e1.getStackTrace());
						
						////UpdateConsole(e1.getLocalizedMessage());
						try {
							this.getFromDeviceQueue().put(msg);
						} catch (InterruptedException e) {
							logger.error(e.getMessage());
							logger.error(e.getStackTrace());
						}
	
					}
	
					break;
				
				default:
					//do not put error message for Websocket connect that comes here in case we later want to do something on connect.
					if (!str.contains("WebSocket Connect")){
						//System.out.println(this.getClass().getSimpleName() + " Could not find " + strCmd + " command for device" );
						msg.setErrorCode(235);
						i18nMessage = getInternationalErrorMessage(this.getClass().getSimpleName(),msg.getCommand());
						msg.setErrorMessage(i18nMessage);
						logger.error(this.getClass().getSimpleName() + " - Could not find " + strCmd + " command for device");
						try {
							this.getFromDeviceQueue().put(msg);
							//UpdateConsole(msg);
						} catch (InterruptedException e) {
							logger.error(e.getMessage());
							logger.error(e.getStackTrace());
						}
					}
				}
			}
		}
	} catch (Exception ex) {
		//System.out.println("ProcessQueueItem error - " + ex.getMessage());
		logger.error("ProcessQueueItem error - " + ex.getMessage());
		logger.error(ex.getStackTrace());
	}
}

public void WriteConfig(SpectroMessage msg){
	SpectroConfiguration thisConfig;
	
	thisConfig = new SpectroConfiguration(msg.getSpectroConfig()); 
	
	try {
		if (thisConfig.getModel().equals("") || thisConfig.getModel().equals(null)) {
			thisConfig.setModel(DEVICENAME);
		}
		
		if (thisConfig.getSerial().equals("") || thisConfig.getSerial().equals(null)) {
			thisConfig.setSerial(GetSerialNumber());
		}
		//What to do about port?  It should always be a USB port.  do this for now.
		if (thisConfig.getPort().equals("") || thisConfig.getPort().equals(null)) {
			thisConfig.setPort("USB");
		}
	} catch (Exception e) {
		logger.error(e.getMessage());
	}
	
	String exceptionMessage = thisConfig.SaveToDisk(null);
	msg.setResponseMessage(exceptionMessage);
}

public SpectroConfiguration ReadConfig(){
	SpectroConfiguration thatConfig = null;
	try {
		
		thatConfig = SpectroConfiguration.ReadFromDisk(null);
	} catch (Exception e) {
		logger.error(e.getMessage());
	}
	return thatConfig;
}

public String Configure(SpectroMessage msg, boolean isOverride) {
	String returnString = "";
	String currentSerialNumber = "";
	try {
		//Assumption is the SpectroInfo record is set from the Configure web page, which means it's just the model, 
		//AND the local PC has the model's required softwrae installed correctly (this would have been confirmed
		//by the ColorEyeListener's Configure preprocessing code.)  Let's check the local PC configuration and see
		//if there's even one there.  If not, this is a new configuration for this station.  If so, it may just be
		//a new device.l
		logger.error("in Ci62SWW configure");
	
		SpectroConfiguration localPCConfig = ReadConfig();
		currentSerialNumber = GetSerialNumber();
		if (localPCConfig==null) {
			//we do not have one locally.  Get the serial number of the device connected, update the msg SpectroInfo,
			//and use that to write to disk.
			msg.getSpectroConfig().setSerial(currentSerialNumber);
			WriteConfig(msg);
		} else {
			//there was something there.  Let's do a comparison.
			if (localPCConfig.getModel().equals(msg.getSpectroConfig().getModel())) {
				//models are equal, how about serial numbers?
				if (localPCConfig.getSerial().equals(currentSerialNumber)) {
					//serial numbers are also equal.  So they reconfigured the device that was already there.
					//return with returnString blank as all is well.
					//20180129-BKP-Add calls to set what would be SW defaults - volume to 2, reading mode to pressure and button.
					returnString = setSWDefaults();
					return returnString;
				} else {
					//same model, but different serial number.  If it's not an override, set the warning messsage
					//and return.
					if (!isOverride) {
						returnString = "Current local PC configured for " + localPCConfig.getModel() 
										+ "serial number " + localPCConfig.getSerial()
										+ " but a " + msg.getSpectroConfig().getModel() + " serial number " + currentSerialNumber
										+ " is trying to be configured.";	
						return returnString;
					}
				}
				
			} else {
				if(!isOverride) {
					returnString = "Current local PC configured for " + localPCConfig.getModel() 
										+ " but a " + msg.getSpectroConfig().getModel() 
										+ " is trying to be configured.";	
					return returnString;
				}
			}
			//If we make it here, something is different, but the override was set.  Update the localPCConfig and 
			//write it back out.
			localPCConfig.setModel(msg.getSpectroConfig().getModel());
			localPCConfig.setSerial(currentSerialNumber);
			localPCConfig.setPort("USB");
			msg.setSpectroConfig(localPCConfig);
			WriteConfig(msg);
			//20180129-BKP-Add calls to set what would be SW defaults - volume to 2, reading mode to pressure and button.
			returnString = setSWDefaults();
		}
	} catch (Exception e) {
		logger.error(e.getMessage());
	}
	return returnString;
}

public String GetExtendedReflectanceMode() {
	String returnResponse = "";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnResponse;
				}
			}
			returnResponse = sDll.Execute("param get useextendedrefls");
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnResponse;
}

public String SetExtendedReflectanceMode() {
	String returnResponse = "";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnResponse;
				}
			}
			returnResponse = sDll.Execute("param set UseExtendedRefls 1");
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnResponse;
}

public String GetSerialNumber() {
	String returnSerialNumber = "";
	try {
		logger.error("in Ci62SWW GetSerialNumber");
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnSerialNumber;
				}
			}
			returnSerialNumber = sDll.GetSerialNum();
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnSerialNumber;
}

public String Detect() {
	String detectStatus = "false";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return detectStatus;
				}
			}
			detectStatus = "true";
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return detectStatus;
}

public String GetCalMode() {
	String returnCalMode = "";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnCalMode;
				}
			}
			returnCalMode = sDll.GetCalMode();
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnCalMode;
}

public String SetOption(String theSetting, String theOption) {
	String returnOption = "";
	boolean boolRetOption = false;
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnOption;
				}
			}
			boolRetOption = sDll.SetOption(theSetting, theOption);
			if (boolRetOption) {
				returnOption = "<00>";
			} else {
				returnOption = "";
			}
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnOption;
}


public String GetOption(String theSetting) {
	String returnOption = "";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnOption;
				}
			}
			returnOption = sDll.GetOption(theSetting);
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnOption;
}

public String GetSettingOptions(String theSetting) {
	String returnOption = "";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnOption;
				}
			}
			returnOption = sDll.GetSettingOptions(theSetting);
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnOption;
}

public String GetAvailableSettings() {
	String returnAvailSettings = "";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnAvailSettings;
				}
			}
			returnAvailSettings = sDll.GetAvailableSettings();
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnAvailSettings;
}

@Override
public SpectralCurve Measure() {
	SpectralCurve retCurve = null;
	boolean exitMeasureLoop = false;
	double sleptForSeconds = 0;
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					measCurve = null;
					return null;
				}
			}
			String returnResponse = sDll.Execute("display refresh enable");
			errorMsg = sDll.GetLastErrorString();
			errorCode = sDll.GetLastErrorCode();
			returnResponse = sDll.Execute("gui set measure");
			errorMsg = sDll.GetLastErrorString();
			errorCode = sDll.GetLastErrorCode();
			returnResponse = sDll.Execute("display refresh disable");
			errorMsg = sDll.GetLastErrorString();
			errorCode = sDll.GetLastErrorCode();
			// let's try this - just check for measurement. the sDll.Measure() INITIATES a measurement, which is
			// not what we want.  We want the human BEAN to initiate the measurement.  So let's just loop for
			// a minute or two and see if they do something with it.
			while(!exitMeasureLoop) {
				if (sDll.IsDataReady()) {
				    int datSetCount = sDll.GetSpectralSetCount();
				    
				    //zero out the curve we are going to post to.
				    BigDecimal[] thisCurve = new BigDecimal[40] ;
				    for (int bdinit = 0; bdinit < 40; ++bdinit) {
				    	thisCurve[bdinit] = BigDecimal.ZERO;
				    }
				    
				    for( int dataSet = 0; dataSet < datSetCount; ++dataSet )
					{
				    	//in our case, we only want SPIN, not SPEX.
				    	if (sDll.GetSpectralSetName(dataSet).equalsIgnoreCase("SPIN")) {
					        for( int wl = 0; wl < 40; ++wl )
					        {
					            float value = sDll.GetSpectralData( dataSet, wl );
					            thisCurve[wl] = BigDecimal.valueOf(value);
					           
					        }
					        retCurve = new SpectralCurve();
					        retCurve.setCurve(thisCurve);
					        retCurve.setCurvePointCnt(40);
				    	}
					}
				    exitMeasureLoop = true;
				} else {
					Thread.sleep(500);
					sleptForSeconds = sleptForSeconds + .5;
					if (sleptForSeconds > 60) {
						exitMeasureLoop = true;
					}
				}
			}
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
		measCurve = null;
	}
	
	measCurve = retCurve;
	return retCurve;
}

//This method manually handles the extended reflectance functionality.  It looks like it works
//in the device itself, but what the DLL is returning is only the first 31 points, even when 
//extended.
public SpectralCurve SWMeasure() {
	SpectralCurve retCurve = null;
	boolean exitMeasureLoop = false;
	double sleptForSeconds = 0;
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					measCurve = null;
					return null;
				}
			}
			int lastErrCode = sDll.GetLastErrorCode();
			String lastErrMsg = sDll.GetLastErrorString();
			String returnResponse = sDll.Execute("display refresh enable");
			lastErrMsg = sDll.GetLastErrorString();
			lastErrCode = sDll.GetLastErrorCode();
			returnResponse = sDll.Execute("gui set measure");
			lastErrMsg = sDll.GetLastErrorString();
			lastErrCode = sDll.GetLastErrorCode();
			returnResponse = sDll.Execute("display refresh disable");
			lastErrMsg = sDll.GetLastErrorString();
			lastErrCode = sDll.GetLastErrorCode();
			//First, force the Ci62 to NOT be in extended reflectance mode.
			returnResponse = sDll.Execute("param set UseExtendedRefls 0");
			lastErrMsg = sDll.GetLastErrorString();
			lastErrCode = sDll.GetLastErrorCode();
			// let's try this - just check for measurement. the sDll.Measure() INITIATES a measurement, which is
			// not what we want.  We want the human BEAN to initiate the measurement.  So let's just loop for
			// a minute or two and see if they do something with it.
			while(!exitMeasureLoop) {
				lastErrMsg = sDll.GetLastErrorString();
				lastErrCode = sDll.GetLastErrorCode();
				if (sDll.IsDataReady()) {
					int wlCount = sDll.GetWavelengthCount();
				    int datSetCount = sDll.GetSpectralSetCount();
				    
				    //zero out the curve we are going to post to.
				    BigDecimal[] thisCurve = new BigDecimal[40] ;
				    for (int bdinit = 0; bdinit < 40; ++bdinit) {
				    	thisCurve[bdinit] = BigDecimal.ZERO;
				    }
				    
				    for( int dataSet = 0; dataSet < datSetCount; ++dataSet )
					{
				    	//in our case, we only want SPIN, not SPEX.
				    	if (sDll.GetSpectralSetName(dataSet).equalsIgnoreCase("SPIN")) {
					        for( int wl = 0; wl < 31; ++wl )
					        {
					            float value = sDll.GetSpectralData( dataSet, wl );
					            thisCurve[wl + 4] = BigDecimal.valueOf(value);
					           
					        }
					        retCurve = new SpectralCurve();
					        retCurve.setCurve(thisCurve);
					        retCurve.setCurvePointCnt(40);
				    	}
					}
				    exitMeasureLoop = true;
				} else {
					Thread.sleep(500);
					sleptForSeconds = sleptForSeconds + .5;
					if (sleptForSeconds > 60) {
						exitMeasureLoop = true;
					}
				}
			}
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
		measCurve = null;
	}
	
	measCurve = retCurve;
	return retCurve;
}

public String GetInterfaceVersion() {
	String strInterfaceVersion = "";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return strInterfaceVersion;
				}
			}
			strInterfaceVersion = sDll.GetInterfaceVersion();
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	return strInterfaceVersion;
}

public String GetCalibrationStandard() {
	String strCalibrationStandard = "";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return strCalibrationStandard;
				}
			}
			strCalibrationStandard = sDll.GetCalibrationStandard();
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	return strCalibrationStandard;
}

public int GetSpectralSetCount() {
	int intSpectralSetCount = 0;
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return intSpectralSetCount;
				}
			}
			intSpectralSetCount = sDll.GetSpectralSetCount();
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	return intSpectralSetCount;
}

public int GetWavelengthCount() {
	int intWavelengthCount = 0;
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return intWavelengthCount;
				}
			}
			intWavelengthCount = sDll.GetWavelengthCount();
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	return intWavelengthCount;
}

public boolean IsConnected() {
	boolean bolIsConnected = false;
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			bolIsConnected = sDll.IsConnected();
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	return bolIsConnected;
}

public void Shutdown() {
	try {
		if (sDll==null) {
			return;
		}
		if(sDll.IsConnected()) {
			if (!sDll.Disconnect()) {
				errorCode = sDll.GetLastErrorCode();
				errorMsg = sDll.GetLastErrorString();
				logger.error("Disconnect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
				return; 
			} 
		}
		//disconnect was successful (or it was never connected), destroy the dll
		sDll = null;
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
}

public String GetLastErrorString() {
	String lastErrorString = "";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return lastErrorString;
				}
			}
			lastErrorString = sDll.GetLastErrorString();
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	return lastErrorString;
}

public int GetLastErrorCode() {
	int lastErrorCode = 0;
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					
					return lastErrorCode;
				}
			}
			lastErrorCode = sDll.GetLastErrorCode();
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	return lastErrorCode;
}

public String GetCalSteps() {
	String returnCalSteps = "";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnCalSteps;
				}
			}
			returnCalSteps = sDll.GetCalSteps();
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	return returnCalSteps;
}

public int GetCalStatus() {
	//initialize to -1, successful status will return either 0 (not calibrated) or 1 (calibrated)
	int returnCalStatus = -1;
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnCalStatus;
				}
			}
			returnCalStatus = sDll.GetCalStatus();
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	return returnCalStatus;
}

public int GetCalProgress() {
	//initialize to -1, successful status will return either 0 (completed successfully), 1 (still in progress)
	//Any other setting is an error, and the error message and error code should be set.
	int returnCalProgress = -1;
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnCalProgress;
				}
			}
			returnCalProgress = sDll.GetCalProgress();
			if (returnCalProgress!=0 && returnCalProgress!=1) {
				errorCode = sDll.GetLastErrorCode();
				errorMsg = sDll.GetLastErrorString();
			}
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	return returnCalProgress;
}

public int GetSampleCount() {
	//initialize to 0
	int intSampleCount = 0;
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return intSampleCount;
				}
			}
			intSampleCount = sDll.GetSampleCount();
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	return intSampleCount;
}

private boolean CalibrateThis(String calType){
	
	boolean calibrateSuccess = false;
	int calProgress = 0;
	boolean exitCalibrateLoop = false;
	double sleptForSeconds = 0;
	double calibrationTimeout = 60;
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return calibrateSuccess;
				}
			}
			calibrateSuccess = sDll.CalibrateStep(calType);
			//CalibrateStep should return immediately as we are set to Device
			while(!exitCalibrateLoop) {
				calProgress = GetCalProgress();
				if (calProgress==0) {
					exitCalibrateLoop = true;
				} else {
					if (calProgress==1) {
						//reports it is not complete
						exitCalibrateLoop = false;
						Thread.sleep(500);
						sleptForSeconds = sleptForSeconds + .5;
						if (sleptForSeconds > calibrationTimeout) {
							exitCalibrateLoop = true;
						}
					} else {
						//error occurred during calibration.
						errorCode = sDll.GetLastErrorCode();
						errorMsg = sDll.GetLastErrorString();
						exitCalibrateLoop = true;
					}
				}
			}
	
			calProgress = GetCalProgress();
			if (calProgress==0) {
				calibrateSuccess = true;
			} else {
				if (calProgress==1) {
					//reports it is not complete, but we've timed out of the loop.
					errorCode = 236;
					errorMsg = "Timeout - no physical action to calibrate occurred in " + calibrationTimeout + " seconds";
					calibrateSuccess = false;
				} else {
					//error occurred during calibration.
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					calibrateSuccess = false;
				}
			}
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	return calibrateSuccess;	
}

public boolean CalibrateWhite(){
	
	boolean calibrateSuccess = false;
	String prepResponse = "";

	try {
		//On the Ci62, we need to unlock the screen/keys and set the screen to Main for calibration to work.
		//if this is not done, it throws an error.
		prepResponse = Ci62DisplayRefreshEnable();
		if (!prepResponse.equals("<00>")) {
			errorMsg = "Ci62Impl - CalibrateWhite (DisplayRefreshEnable) returned " + prepResponse;
			logger.error(errorMsg);
			return calibrateSuccess;
		}
		
		prepResponse = Ci62SetGui("Main");
		if (!prepResponse.equals("<00>")) {
			errorMsg = "Ci62Impl - CalibrateWhite (Ci62SetGui Main) returned " + prepResponse;
			logger.error(errorMsg);
			return calibrateSuccess;
		}
		
		calibrateSuccess = CalibrateThis("White");
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	return calibrateSuccess;	
}

public boolean CalibrateBlack(){
	
	boolean calibrateSuccess = false;

	try {
		calibrateSuccess = CalibrateThis("Black");
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	return calibrateSuccess;	
}

public boolean MeasureGreen(){
	SpectralCurve greenCurve;
	double[] greenCurveArray = new double[40];
	
	SpectralCurve checkPlaque;
	double[] checkPlaqueArray = new double[40];
	ColorCoordinates greenCurveCoord;
	ColorCoordinates checkPlaqueCoord;
	
	
	ColorCoordinatesCalculator ccc = new ColorCoordinatesCalculatorImpl();
	ColorDifferenceCalculator cdc = new ColorDifferenceCalculator(); 
	double deltaE = 0;
	boolean measureSuccess = false;
	String prepResponse = "";

	try {

		
		//first, measure the green tile.
		//System.out.println("in MeasureGreen");
		greenCurve = SWMeasure();
		
		//System.out.println("got the green curve");
		//now, retrieve the green tile 
		checkPlaque = Ci62GetCheckPlaqueSpin();
		//System.out.println("got the check plaque");
		
		//Get the color coordinates for each curve.
		for (int x=0; x < 40; x++) {
			greenCurveArray[x] = greenCurve.getCurve()[x].doubleValue();
			checkPlaqueArray[x] = checkPlaque.getCurve()[x].doubleValue();
		}
		//System.out.println("got both curves into double value curves");
		
		
		greenCurveCoord = ccc.getColorCoordinates(greenCurveArray);
		//System.out.println("got the green curve coord");
		checkPlaqueCoord = ccc.getColorCoordinates(checkPlaqueArray);
		//System.out.println("got the check plaque coord");
		
		//System.out.println("greenCurve L-" + greenCurveCoord.getCieL() + " A -" + greenCurveCoord.getCieA() + " B - " + greenCurveCoord.getCieB());
		//System.out.println("checkPlaque L-" + checkPlaqueCoord.getCieL() + " A -" + checkPlaqueCoord.getCieA() + " B - " + checkPlaqueCoord.getCieB());
		//calculate the delta E
		deltaE = cdc.calculateDeltaE(checkPlaqueCoord, greenCurveCoord);
		
		//System.out.println("Delta E is " + deltaE);
		
		if (deltaE > .5) {
			// too far out, unsuccessful.
			measureSuccess = false;
		} else {
			measureSuccess = true;
		}
		
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	return measureSuccess;	
}

public boolean ScanIsSupported(){
	
	boolean bolScanSupported = false;
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return bolScanSupported;
				}
			}
			bolScanSupported = sDll.ScanIsSupported();
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	return bolScanSupported;
	
}

public boolean AbortCalibration(){
	
	boolean calibrateAbort = false;
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return calibrateAbort;
				}
			}
			calibrateAbort = sDll.AbortCalibration();
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	return calibrateAbort;
	
}

public boolean ClearSamples(){
	
	boolean bolClearSamples = false;
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return bolClearSamples;
				}
			}
			bolClearSamples = sDll.ClearSamples();
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	return bolClearSamples;
	
}

@Override
public boolean Calibrate() {
	

	return false;
}

public String Ci62DisplayRefreshDisable() {
	String returnResponse = "";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnResponse;
				}
			}
			returnResponse = sDll.Execute("display refresh disable");
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnResponse;
}

public String Ci62DisplayRefreshEnable() {
	String returnResponse = "";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnResponse;
				}
			}
			returnResponse = sDll.Execute("display refresh enable");
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnResponse;
}


public String Ci62GetGui() {
	String returnResponse = "";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnResponse;
				}
			}
			returnResponse = sDll.Execute("gui get");
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnResponse;
}

public String Ci62SetGui(String strArg) {
	String returnResponse = "";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnResponse;
				}
			}
			returnResponse = sDll.Execute("gui set " + strArg);
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnResponse;
}

public String Ci62GetErrorLog() {
	String returnResponse = "";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnResponse;
				}
			}
			returnResponse = sDll.Execute("errorlog get");
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnResponse;
}

public String Ci62ClearErrorLog() {
	String returnResponse = "";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnResponse;
				}
			}
			returnResponse = sDll.Execute("errorlog clear");
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnResponse;
}

public String Ci62GetInstSerial() {
	String returnResponse = "";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnResponse;
				}
			}
			returnResponse = sDll.Execute("serialnum get INST");
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnResponse;
}

public String Ci62GetOEMSerial() {
	String returnResponse = "";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnResponse;
				}
			}
			returnResponse = sDll.Execute("serialnum get OEM");
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnResponse;
}

public String Ci62GetModelType() {
	String returnResponse = "";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnResponse;
				}
			}
			returnResponse = sDll.Execute("serialnum get MODEL");
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnResponse;
}

public String GetClock() {
    return Ci62GetClock();
}

public String Ci62GetClock() {
	String returnResponse = "";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnResponse;
				}
			}
			returnResponse = sDll.Execute("clock get");
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnResponse;
}

public String SetClock(String strTimeArgs) {
    return Ci62SetClock(strTimeArgs);
}


public String Ci62SetClock(String strTimeArgs) {
	String returnResponse = "";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnResponse;
				}
			}
			returnResponse = sDll.Execute("clock set " + strTimeArgs);
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnResponse;
}


public String GetVolumeLevel() {
	return Ci62GetVolumeLevel();
}

public String Ci62GetVolumeLevel() {
	String returnResponse = "";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnResponse;
				}
			}
			returnResponse = sDll.Execute("param get VolumeLevel");
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnResponse;
}

public String GetCalWhiteTimeout() {
	return Ci62GetCalWhiteTimeout();
}

public String Ci62GetCalWhiteTimeout() {
	String returnResponse = "";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnResponse;
				}
			}
			returnResponse = sDll.Execute("param get CalWhiteTimeout");
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnResponse;
}

public String SetVolumeLevel(String strParamArgs) {
	return Ci62SetVolumeLevel(strParamArgs);
}

public String Ci62SetVolumeLevel(String strParamArgs) {
	String returnResponse = "";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnResponse;
				}
			}
			returnResponse = sDll.Execute("param set VolumeLevel " + strParamArgs);
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnResponse;
}

public String SetCalWhiteTimeout(String strParamArgs) {
	return Ci62SetCalWhiteTimeout(strParamArgs);
}

public String Ci62SetCalWhiteTimeout(String strParamArgs) {
	String returnResponse = "";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnResponse;
				}
			}
			returnResponse = sDll.Execute("param set CalWhiteTimeout " + strParamArgs);
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnResponse;
}

public String GetVersion() {
	return Ci62GetVersion();
}

public String GetCalPlaqueSerial() {
	return Ci62GetCalPlaqueSerial();
}

public String GetCheckPlaqueSerial() {
	return Ci62GetCheckPlaqueSerial();
}

public String GetCalStatusMinUntilCalExpiration() {
	String returnResponse = "";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnResponse;
				}
			}
			returnResponse = sDll.Execute("calstatus get");
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnResponse;
}

public String Ci62GetCalStatus() {
	String returnResponse = "";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnResponse;
				}
			}
			returnResponse = sDll.Execute("calstatus get");
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnResponse;
}

public SpectralCurve Ci62GetCalPlaqueSpin() {
	String returnResponse = "";
	SpectralCurve returnCurve = null;
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnCurve;
				}
			}
			returnResponse = sDll.Execute("calplaque get spin");
			//response is the curve separated by spaces.  Create a new return curve and populate the curve.
			//zero out the curve we are going to post to.
		    BigDecimal[] thisCurve = new BigDecimal[40] ;
		    for (int bdinit = 0; bdinit < 40; ++bdinit) {
		    	thisCurve[bdinit] = BigDecimal.ZERO;
		    }
		    String[] values = returnResponse.split(" ");

			for( int wl = 0; wl < 31; ++wl ) {
				thisCurve[wl + 4] = new BigDecimal(values[wl]); 
			}
			returnCurve = new SpectralCurve();
			returnCurve.setCurve(thisCurve);
			returnCurve.setCurvePointCnt(40);

		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnCurve;
}

public SpectralCurve Ci62GetCalPlaqueSpex() {
	String returnResponse = "";
	SpectralCurve returnCurve = null;
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnCurve;
				}
			}
			returnResponse = sDll.Execute("calplaque get spex");
			//response is the curve separated by spaces.  Create a new return curve and populate the curve.
			//zero out the curve we are going to post to.
		    BigDecimal[] thisCurve = new BigDecimal[40] ;
		    for (int bdinit = 0; bdinit < 40; ++bdinit) {
		    	thisCurve[bdinit] = BigDecimal.ZERO;
		    }
		    String[] values = returnResponse.split(" ");

			for( int wl = 0; wl < 31; ++wl ) {
				thisCurve[wl + 4] = new BigDecimal(values[wl]); 
			}
			returnCurve = new SpectralCurve();
			returnCurve.setCurve(thisCurve);
			returnCurve.setCurvePointCnt(40);

		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnCurve;
}

public String Ci62GetCalPlaqueSerial() {
	String returnResponse = "";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnResponse;
				}
			}
			returnResponse = sDll.Execute("calplaque get serial");
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnResponse;
}

public String Ci62GetCheckPlaqueSerial() {
	String returnResponse = "";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnResponse;
				}
			}
			returnResponse = sDll.Execute("checkplaque get serial");
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnResponse;
}

public SpectralCurve Ci62GetCheckPlaqueSpin() {
	String returnResponse = "";
	SpectralCurve returnCurve = null;
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnCurve;
				}
			}
			returnResponse = sDll.Execute("checkplaque get spin");
			//response is the curve separated by spaces.  Create a new return curve and populate the curve.
			//zero out the curve we are going to post to.
		    BigDecimal[] thisCurve = new BigDecimal[40] ;
		    for (int bdinit = 0; bdinit < 40; ++bdinit) {
		    	thisCurve[bdinit] = BigDecimal.ZERO;
		    }
		    String[] values = returnResponse.split(" ");

			for( int wl = 0; wl < 31; ++wl ) {
				thisCurve[wl + 4] = new BigDecimal(values[wl]); 
			}
			returnCurve = new SpectralCurve();
			returnCurve.setCurve(thisCurve);
			returnCurve.setCurvePointCnt(40);

		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnCurve;
}

public SpectralCurve Ci62GetCheckPlaqueSpex() {
	String returnResponse = "";
	SpectralCurve returnCurve = null;
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnCurve;
				}
			}
			returnResponse = sDll.Execute("checkplaque get spex");
			//response is the curve separated by spaces.  Create a new return curve and populate the curve.
			//zero out the curve we are going to post to.
		    BigDecimal[] thisCurve = new BigDecimal[40] ;
		    for (int bdinit = 0; bdinit < 40; ++bdinit) {
		    	thisCurve[bdinit] = BigDecimal.ZERO;
		    }
		    String[] values = returnResponse.split(" ");

			for( int wl = 0; wl < 31; ++wl ) {
				thisCurve[wl + 4] = new BigDecimal(values[wl]); 
			}
			returnCurve = new SpectralCurve();
			returnCurve.setCurve(thisCurve);
			returnCurve.setCurvePointCnt(40);

		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	return returnCurve;
}

public String Ci62GetVersion() {
	String returnResponse = "";
	try {
		if (sDll==null) {
			CreateDllObj();
		}
		if (sDll!=null) {
			if(!sDll.IsConnected()) {
				if (!sDll.Connect()) {
					errorCode = sDll.GetLastErrorCode();
					errorMsg = sDll.GetLastErrorString();
					logger.error("Connect to " + DEVICENAME + " FAILED - " + errorCode.toString() + " " + errorMsg);
					return returnResponse;
				}
			}
			returnResponse = sDll.Execute("version get");
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	
	return returnResponse;
}

private String getInternationalErrorMessage(String theClassName, String theCommand) {
	String returnString;
	try {
		System.out.println("in getInternationalErrorMessage");
		returnString = resourceBundle.getString(errorCode.toString());
		System.out.println("return message is " + returnString);
		
	} catch (Exception e) {
		//odds are error is because we cannot get error from the resourceBundle.
		logger.error(e.getMessage());
		returnString = theClassName + " - " + theCommand + " executed with result: " + this.getErrorMsg() + " (" + this.getErrorCode() + ")";
	}
	return returnString;
}

private String setSWDefaults() {
	String returnString = "<00>";
	String vlReturnString = "";
	String rmReturnString = "";
	String ctReturnString = "";
	try {
		//set the various SW settings.  Primary importance is the trigger mode.  We do NOT want it to be sofware, 
		//but instead, button and pressure.
		vlReturnString = SetVolumeLevel("2");
		rmReturnString = SetOption("Reading_Mode", "Pressure");
		ctReturnString = SetCalWhiteTimeout("24");
		if (!vlReturnString.equals("<00>")) {
			errorCode = 234;
			errorMsg = "SetVolumeLevel UNSUCCESSFUL - check for valid values in argument list 2";
			returnString = "";
		}
		if (!rmReturnString.equals("<00>")) {
			errorCode = 234;
			errorMsg = "SetOption UNSUCCESSFUL - check for valid values in argument list 'Reading_Mode', 'Pressure'";
			returnString = "";
		}
		if (!ctReturnString.equals("<00>")) {
			errorCode = 234;
			errorMsg = "SetCalWhiteTimeout UNSUCCESSFUL - check for valid values in argument list 24";
			returnString = "";
		}
		
	} catch (Exception e) {
		logger.error(e.getMessage());
		// TODO Ideally, we should have a "user friendly" error message post to the errorMsg property 
		// as it will return to the user.
		errorMsg = e.getMessage();
	}
	return returnString;
}



}
