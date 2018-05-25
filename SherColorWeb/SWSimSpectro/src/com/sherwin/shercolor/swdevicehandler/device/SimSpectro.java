package com.sherwin.shercolor.swdevicehandler.device;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sherwin.shercolor.swdevicehandler.domain.Message;
import com.sherwin.shercolor.swdevicehandler.domain.SimSpectroCommandResponseList;
import com.sherwin.shercolor.swdevicehandler.domain.SimSpectroResponseCommand;
import com.sherwin.shercolor.swdevicehandler.domain.SpectralCurve;
import com.sherwin.shercolor.swdevicehandler.domain.SpectroConfiguration;
import com.sherwin.shercolor.swdevicehandler.domain.SpectroMessage;



public class SimSpectro extends AbstractSpectroImpl  {
	static Logger logger = LogManager.getLogger(SimSpectro.class);
	SpectroConfiguration configuration = new SpectroConfiguration();
	public static boolean DEBUG = false;
	String spectroResponseXmlPath = ".";


	public SimSpectro(){
		//init();  for now handling in TinterListener... make it easier to unit test too!
		//get initial config from disk and store in memory.
		System.out.println("initializing SimSpectro");
		logger.error("initializing SimSpectro");
		this.setConfiguration(SpectroConfiguration.ReadFromDisk(null));
		System.out.println("done initializing SimSpectro");
		logger.error("done initializing SimSpectro");
	}

	
	@Override
	protected	
	void ProcessQueueItem(Message m){
		SpectroMessage msg = null;
		logger.error("Received item in SimSpectro queue.");
		System.out.println("Received item in SimSpectro queue.");
		
		try{
			if(m != null  && m.getMessageName()!=null){
				if(m.getMessageName().equalsIgnoreCase("SpectroMessage")){
					msg = (SpectroMessage)m;
					String command = msg.getCommand();
					if(command !=null && !command.isEmpty()){
						switch(command){
							case "ReadConfig":
								ReadConfig(msg);
								break;
							default:
								ExecuteCommand(msg, command);
								break;
						}
						
					} else {
						//Return Error command is empty or null
						msg.setErrorMessage("Spectro command was empty or null.");	
						msg.setErrorCode(-1);
						msg.setResponseMessage("");
					}
				} else {
					//Not a Spectro Message,  Return Error 
					msg = (SpectroMessage)m;
					msg.setMessageName("SpectroMessage");
					msg.setErrorMessage("Message Name: "+ m.getMessageName() + " not recognized");
					msg.setErrorCode(-2);
					msg.setResponseMessage("");
					System.out.println("Message Name: "+ m.getMessageName() + " not recognized");
				}
				this.getFromDeviceQueue().put(msg);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

	}
	
	private void ExecuteCommand(SpectroMessage msg, String command){
		System.out.println(command + " started");

		//Read xml file with command and their responses

		try{
			// set defaults in case we dont find command in file...
			int waitMilli = 50;
			//String errorMessage = "Simulated " + msg.getCommand() + " executed";
			String errorMessage = this.getClass().getSimpleName() + " - Could not find " + msg.getCommand() + " command for device";
			int errorNumber = -1;
			String responseMessage = "";
			SpectralCurve theCurve = new SpectralCurve();
			
			// read XML file with command responses
			File filename = new File(spectroResponseXmlPath + "/spectro/simspectroresponses.xml");
			List<SimSpectroResponseCommand> responseList = readXMLfile(filename.getAbsolutePath());
			if(responseList!=null && responseList.size()>0){
				for(SimSpectroResponseCommand item : responseList){
					if(command.equals(item.getName())){
						waitMilli = item.getWaitMilliseconds();
						responseMessage = item.getResponseMessage();
						errorNumber = item.getErrorNumber();
						errorMessage = item.getErrorMessage();
						System.out.println("item.getCurvePoints is " + item.getCurvePoints());
						theCurve = item.getCurve();
						break;
					}
				}
			} else {
				System.out.println("No XML file for Responses - using default");
			}
			
			// perform simulation
			Thread.sleep(waitMilli);
			msg.setErrorMessage(errorMessage);
			msg.setErrorCode(errorNumber);
			msg.setResponseMessage(responseMessage);
			msg.setCurve(theCurve);

			logger.info(command + " result" + msg.getResponseMessage() + " " +  msg.getErrorMessage());
			System.out.println("rc=" + msg.getResponseMessage() + " " +  msg.getErrorMessage());
			System.out.println(command + " completed");
			
		} catch (InterruptedException e) {
			logger.info(command + " threw InterruptedException : " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.info(command + " threw Exception : " + e.getMessage());
			e.printStackTrace();
		}
	}

	private List<SimSpectroResponseCommand> readXMLfile(String filename){
		System.out.println("top of readXMLfile, filename is " + filename);
		
		List<SimSpectroResponseCommand> responseList = null;
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(SimSpectroCommandResponseList.class);
			
	        XMLInputFactory xif = XMLInputFactory.newInstance();
	        XMLStreamReader xsr = xif.createXMLStreamReader(new FileInputStream(filename));
	        xsr = new MyStreamReaderDelegate(xsr);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			SimSpectroCommandResponseList responseFile = (SimSpectroCommandResponseList) jaxbUnmarshaller.unmarshal(xsr);
			
			if(responseFile!=null && responseFile.getResponseCommmand()!=null && responseFile.getResponseCommmand().size()>0){
				responseList = responseFile.getResponseCommmand();
			}
		} catch (JAXBException | XMLStreamException e) {
			logger.info("readXMLfile: JAXBException OR XMLStreamException result" + e.getMessage());
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			logger.info("readXMLfile: FileNotFoundException result" + e.getMessage());
			e.printStackTrace();
		} catch (SecurityException e) {
			// couldn't open file
			logger.info("readXMLfile: SecurityException result" + e.getMessage());
			e.printStackTrace();
		}
		
		return responseList;
		
	}

	private static class MyStreamReaderDelegate extends StreamReaderDelegate {

	    public MyStreamReaderDelegate(XMLStreamReader xsr) {
	        super(xsr);
	    }

	    @Override
	    public String getAttributeLocalName(int index) {
	        return super.getAttributeLocalName(index).toLowerCase();
	    }

	    @Override
	    public String getLocalName() {
	        return super.getLocalName().toLowerCase();
	    }

	}

	private void ReadConfig(SpectroMessage msg){
		logger.error("in ReadConfig");
		if(this.getConfiguration() != null){
			msg.setSpectroConfig(getConfiguration());
			msg.setErrorMessage("");	
			msg.setErrorCode(0);
			msg.setResponseMessage("");
		} else {
			try {
				this.setConfiguration(SpectroConfiguration.ReadFromDisk(null));
				msg.setSpectroConfig(getConfiguration());
				msg.setErrorMessage("");	
				msg.setErrorCode(0);
				msg.setResponseMessage("");
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
	}


	public SpectroConfiguration getConfiguration() {
		return configuration;
	}
	
	public void setConfiguration(SpectroConfiguration aconfiguration) {
		configuration = aconfiguration;
	}
	
	public String getSpectroResponseXmlPath() {
		return spectroResponseXmlPath;
	}
	
	public void setSpectroResponseXmlPath(String theSpectroResponseXmlPath) {
		spectroResponseXmlPath = theSpectroResponseXmlPath;
	}


	@Override
	public SpectralCurve Measure() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean Calibrate() {
		// TODO Auto-generated method stub
		return false;
	}
		
		
		
}
