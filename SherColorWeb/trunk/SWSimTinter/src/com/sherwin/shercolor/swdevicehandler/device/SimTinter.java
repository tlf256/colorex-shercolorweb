package com.sherwin.shercolor.swdevicehandler.device;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sherwin.shercolor.swdevicehandler.domain.Calibration;
import com.sherwin.shercolor.swdevicehandler.domain.Configuration;
import com.sherwin.shercolor.swdevicehandler.domain.GData;
import com.sherwin.shercolor.swdevicehandler.domain.Message;
import com.sherwin.shercolor.swdevicehandler.domain.SimCalibration;
import com.sherwin.shercolor.swdevicehandler.domain.SimTinterCommandResponseList;
import com.sherwin.shercolor.swdevicehandler.domain.SimTinterResponseCommand;
import com.sherwin.shercolor.swdevicehandler.domain.SimTinterResponseErrorListItem;
import com.sherwin.shercolor.swdevicehandler.domain.TinterMessage;



public class SimTinter extends AbstractTinterImpl implements TinterInterface {
	static Logger logger = LogManager.getLogger(SimTinter.class);
	Configuration configuration = new Configuration();
	public static boolean DEBUG = false;


	public SimTinter(){
		//init();  for now handling in TinterListener... make it easier to unit test too!
		//get initial config from disk and store in memory.
		this.setConfiguration(Configuration.ReadFromDisk(null));
	}

	
	@Override
	protected	
	void ProcessQueueItem(Message m){
		TinterMessage msg = null;
		System.out.println("Received item in SimTinter queue.");
		
		try{
			if(m != null  && m.getMessageName()!=null){
				if(m.getMessageName().equalsIgnoreCase("TinterMessage")){
					msg = (TinterMessage)m;
					String command = msg.getCommand();
					if(command !=null && !command.isEmpty()){
						switch(command){
							case "ReadConfig":
								ReadConfig(msg);
								break;
							case "DetectStatus":
							case "InitStatus": 
								InitStatus(msg);  //status of last detect/init command
								break;
							case "CalDownload":
								CalDownload(msg);
								break;
							case "CalUpload":
								CalUpload(msg);
								break;
							case "Config":
								Config(msg);
								break;
							default:
								ExecuteCommand(msg, command);
								break;
						}
						
					} else {
						//Return Error command is empty or null
						msg.setErrorMessage("Tinter command was empty or null.");	
						msg.setErrorNumber(-1);
						msg.setCommandRC(-1);
					}
				} else {
					//Not a Tinter Message,  Return Error 
					msg = (TinterMessage)m;
					msg.setMessageName("TinterMessage");
					msg.setErrorMessage("Message Name: "+ m.getMessageName() + " not recognized");
					msg.setErrorNumber(-2);
					msg.setCommandRC(-2);
					System.out.println("Message Name: "+ m.getMessageName() + " not recognized");
				}
				Respond(msg);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
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
	private void ExecuteCommand(TinterMessage msg, String command){
		System.out.println(command + " started");

		//Read xml file with command and their responses

		try{
			// set defaults in case we dont find command in file...
			int waitMilli = 5000;
			String errorMessage = "Simulated " + msg.getCommand() + " executed";
			int errorNumber = 0;
			List<com.sherwin.shercolor.swdevicehandler.domain.Error> errorList = new ArrayList<com.sherwin.shercolor.swdevicehandler.domain.Error>();
			
			// read XML file with command responses
			File filename = new File("simtinterresponses.xml");
			List<SimTinterResponseCommand> responseList = readXMLfile(filename.getAbsolutePath());
			if(responseList!=null && responseList.size()>0){
				for(SimTinterResponseCommand item : responseList){
					if(command.equals(item.getName())){
						waitMilli = item.getWaitMilliseconds();
						errorNumber = item.getErrorNumber();
						errorMessage = item.getErrorMessage();
						if(item.getErrorList()!=null) {
							for(SimTinterResponseErrorListItem errorListItem : item.getErrorList()){
								com.sherwin.shercolor.swdevicehandler.domain.Error addError = new com.sherwin.shercolor.swdevicehandler.domain.Error();
								addError.setNum(errorListItem.getCode());
								addError.setMessage(errorListItem.getMessage());
								System.out.println("Added to errorList " + addError.getNum() + " " + addError.getMessage());
								errorList.add(addError);
							}
						} else {
							System.out.println("Error List is null");
						}
						break;
					}
				}
			} else {
				System.out.println("No XML file for Responses - using default");
			}
			
			// perform simulation
			Thread.sleep(waitMilli);
			msg.setErrorMessage(errorMessage);
			msg.setErrorNumber(errorNumber);
			msg.setCommandRC(errorNumber);
			msg.setErrorList(errorList);

			logger.info(command + " result " + msg.getCommandRC() + " " +  msg.getErrorMessage());
			System.out.println("rc=" + msg.getCommandRC() + " " +  msg.getErrorMessage());
			System.out.println(command + " completed");
			
			if(command.equalsIgnoreCase("Detect")){
				//save this result into the last detect message for retrieval
				//anytime someone wants the last detect status!
				logger.info("It is a Detect, setting LastInitDate and Message");
				final DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				//String date = (sdf.format(Calendar.getInstance().getTime()));
				String date = (sdf.format(new Date())).toString();
				logger.info("Setting lastInitDate and string date is " + date);
				//msg.setLastInitDate(Long.getLong(date).longValue());
				msg.setLastInitDate(Long.parseLong(date));
				logger.info("lastInitDate has been set to " + msg.getLastInitDate());
				this.setLastInitMessage(msg);
			}
		} catch (InterruptedException e) {
			logger.info(command + " threw InterruptedException : " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.info(command + " threw Exception : " + e.getMessage());
			e.printStackTrace();
		}
	}

	private List<SimTinterResponseCommand> readXMLfile(String filename){
		System.out.println("top of readXMLfile, filename is " + filename);
		
		List<SimTinterResponseCommand> responseList = null;
		JAXBContext jaxbContext;
		try {
			jaxbContext = JAXBContext.newInstance(SimTinterCommandResponseList.class);
			
	        XMLInputFactory xif = XMLInputFactory.newInstance();
	        XMLStreamReader xsr = xif.createXMLStreamReader(new FileInputStream(filename));
	        xsr = new MyStreamReaderDelegate(xsr);

			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

			SimTinterCommandResponseList responseFile = (SimTinterCommandResponseList) jaxbUnmarshaller.unmarshal(xsr);
			
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

	private void ReadConfig(TinterMessage msg){
		if(this.getConfiguration() != null){
			msg.setConfiguration(getConfiguration());
			msg.setErrorMessage("Config Returned.");	
			msg.setErrorNumber(0);
			msg.setCommandRC(0);
		}
	}

	public void CalUpload(TinterMessage msg){		
		SimCalibration cCal = new SimCalibration();
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
	
	public void CalDownload(TinterMessage msg){
		String exceptionMessage=null;
		Calibration cal =  msg.getCalibration();
		if(cal != null){
			SimCalibration cCal = new SimCalibration(cal);
			exceptionMessage = cCal.toDisk();
		
			GData gdata = msg.getGdata();
			if(gdata != null){			
				exceptionMessage += gdata.toDisk();				
			}
			else{
				System.out.println("Gdata is null");
				logger.error("Gdata is null");
			}
			msg.setJavaMessage(exceptionMessage);
		} else {
			System.out.println("cal is null");
			logger.error("cal is null");
		}
	}
	
	@Override
	public void Detect(TinterMessage msg){

	}
	
	@Override
	public void Dispense(TinterMessage msg){
		
	}

	@Override
	public void PurgeAll(TinterMessage arg0) {
		// TODO Auto-generated method stub
		
	}
	

	public Configuration getConfiguration() {
		return configuration;
	}
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
		
		
		
}
