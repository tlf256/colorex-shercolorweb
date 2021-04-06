package com.sherwin.shercolor.customershercolorweb.web.action;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;


import com.opensymphony.xwork2.ActionSupport;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sherwin.shercolor.colormath.domain.ColorCoordinates;
import com.sherwin.shercolor.common.domain.CustWebSpectroRemote;
import com.sherwin.shercolor.common.service.ColorService;
import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;

public class ManageStoredMeasurementsAction extends ActionSupport implements SessionAware, LoginRequired {

	// 06/24/2019 BXW: This Action class supports the functionality for the corresponding jsp page
	//					Will require a good amount of work to allow the dataTable to function properly

	@Autowired
	CustomerService customerService;
	
	@Autowired
	ColorService colorService;

	private Map<String, Object> sessionMap;
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(SpectroConfigureAction.class);
	private String reqGuid;

	private CustWebSpectroRemote custWebSpectroRemote;
	private List<CustWebSpectroRemote> measurementTable;
	private List<CustWebSpectroRemote> newMeasurements;
	private List<CustWebSpectroRemote> loadTable;
	
	private String measurementDate;
	private String measurementSerialNbr;
	private String measurementModel;
	private String measurementName;
	private String measurementDescription;
	private String measurementMeasuredCurve;
	private String measurementRgbHex;

	// Load Measurement Table Information
	private String measurementJson;
	private String[] hexList;
	
	// User hit the backup button on the Color page
	public String backItUp() {
		try {
			//blank out the color info as we are backing up.
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			sessionMap.put(reqGuid, reqObj);
			return SUCCESS;
			
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return ERROR;
			}
	}
		
		
	public String display() {
		 

		try {
			//System.out.println("IN MANAGESTOREDMEASUREMENTSACTION DISPLAY METHOD");
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			sessionMap.put(reqGuid, reqObj);
			String customerId = reqObj.getCustomerID();
			
			// Retrieve all stored measurements that the Customer has saved off to the DB
			setMeasurementTable(customerService.getCustWebSpectroRemotes(customerId));
			
		    return SUCCESS;
		    
		    
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String obtainAllRGBColors() {
		
		try {
			//System.out.println("IN MANAGESTOREDMEASUREMENTSACTION OBTAINALLRGBCOLORS METHOD");
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			sessionMap.put(reqGuid, reqObj);
			
			JsonObject jsonObject = new JsonParser().parse(measurementJson).getAsJsonObject();		
			JsonParser parser = new JsonParser();
			JsonObject obj = parser.parse(measurementJson).getAsJsonObject();
			JsonArray measurementsJSON = obj.getAsJsonArray("storedMeasurements");
			int i = 0;
			hexList = new String[measurementsJSON.size()];
			for(JsonElement measurementElement : measurementsJSON) {
				JsonObject measurement = measurementElement.getAsJsonObject();
				
				String sampleCurve = measurement.getAsJsonObject("measureCurve").toString();
				sampleCurve = sampleCurve.replaceFirst("(.*\\[)", "");
				sampleCurve = sampleCurve.replaceFirst("(\\].*)", "");
				String[] curveStringArray = sampleCurve.split(",");
				BigDecimal[] rgbHexCurve = new BigDecimal[curveStringArray.length];
				double[] measureCurve = new double[curveStringArray.length];
				int index = 0;
				for(BigDecimal pt : rgbHexCurve) {
					rgbHexCurve[index] = new BigDecimal(curveStringArray[index]);
					measureCurve[index] = rgbHexCurve[index].doubleValue();
					index++;
				}
				
				ColorCoordinates colorCoord = colorService.getColorCoordinates(rgbHexCurve, "D65");
				if (colorCoord != null) {
					
					hexList[i] = colorCoord.getRgbHex();
				}
				i++;
			}
			return SUCCESS;
		} catch (Exception e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage(), e);
			e.printStackTrace();
			return ERROR;
		}
	}
	
	public String obtainAllStoredMeasurements() {
		try {
			//System.out.println("IN MANAGESTOREDMEASUREMENTSACTION DISPLAY METHOD");
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			sessionMap.put(reqGuid, reqObj);
			String customerId = reqObj.getCustomerID();
			
			// Retrieve all stored measurements that the Customer has saved off to the DB
			setMeasurementTable(customerService.getCustWebSpectroRemotes(customerId));
			
		    return SUCCESS;
		    
		    
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String saveNewSpectroRemotes() {
		newMeasurements = new ArrayList<CustWebSpectroRemote>();
		hexList[0] = hexList[0].replace("\"", "");
		hexList[0] = hexList[0].replace("[", "");
		hexList[0] = hexList[0].replace("]", "");
		
		String[] rgbList = hexList[0].split(",");
		try {
			//System.out.println("IN MANAGESTOREDMEASUREMENTSACTION EXECUTE METHOD");
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			String customerId = reqObj.getCustomerID();
			JsonObject jsonObject = new JsonParser().parse(measurementJson).getAsJsonObject();
			//setCustWebSpectroRemote(customerService.getCustWebSpectroRemotes(customerId));
			
			JsonParser parser = new JsonParser();
			JsonObject obj = parser.parse(measurementJson).getAsJsonObject();
			JsonArray measurementsJSON = obj.getAsJsonArray("storedMeasurements");
			
			// TODO : Parse JSON array by extracting the values and creating a List of CustWebSpectroRemote objects
			int i = -1;
			for(JsonElement measurementElement : measurementsJSON) {
				i++;
				JsonObject measurement = measurementElement.getAsJsonObject();
				CustWebSpectroRemote newMeasurement = new CustWebSpectroRemote();
				
				// Retrieve the Customer ID from the reqObj
				newMeasurement.setCustomerId(customerId);
				String addMeasurement = "";
				// Before processing the entire JSON, detect if the measurement is even going
				// to be added to the DB by Parsing the JSON for the addMeasurement boolean
				try {
					addMeasurement = measurement.getAsJsonPrimitive("addMeasurement").toString();
				} catch (Exception e) {
					addMeasurement = "false";
				}
				if(addMeasurement.equals("true")) {
					
					// Parse JSON and convert JSON > String > String[] > BigDecimal[]
					// to obtain all points in the curve
					String sampleCurve = measurement.getAsJsonObject("measureCurve").toString();
					sampleCurve = sampleCurve.replaceFirst("(.*\\[)", "");
					sampleCurve = sampleCurve.replaceFirst("(\\].*)", "");
					String[] curveStringArray = sampleCurve.split(",");
					BigDecimal[] rgbHexCurve = new BigDecimal[curveStringArray.length];
					double[] measureCurve = new double[curveStringArray.length];
					int index = 0;
					for(BigDecimal pt : rgbHexCurve) {
						rgbHexCurve[index] = new BigDecimal(curveStringArray[index]);
						measureCurve[index] = rgbHexCurve[index].doubleValue();
						index++;
					}
					newMeasurement.setMeasuredCurve(measureCurve);
					
					// Retrieve Spectro Model from the reqObj (NO LONGER, GET THIS FROM THE JSON)
					// Parse JSON and convert JSON > String
					// to obtain the spectro model
					String model = measurement.getAsJsonPrimitive("spectroModel").toString();
					model = model.replaceAll("\"", "");
					newMeasurement.setModel(model);
					
					//Get RGB Hex from list passed in from the JSP page
					newMeasurement.setRGBHex(rgbList[i]);
					
					// Parse JSON and convert JSON > String > Date
					// to obtain the DateTime
					String sampleDateTime = measurement.getAsJsonPrimitive("sampleDateTime").toString();
					sampleDateTime = sampleDateTime.replaceAll("\"", "");
					//String sampleTime = measurement.getAsJsonPrimitive("sampleTime").toString();
					//sampleTime = sampleTime.replaceAll("\"", "");
					//String sampleDateTime = sampleDate + " " + sampleTime;
					SimpleDateFormat dateTimeFormat = new SimpleDateFormat("MM/dd/yy hh:mm:ss");
					Date dateTime = dateTimeFormat.parse(sampleDateTime);
					newMeasurement.setSampleDateTime(dateTime);
					
					// Always set to empty as the customer will have to provide a description
					// for the measurement entry (DIALOG INPUT NOW ALLOWS USER TO SET THIS BEFORE IT GETS SAVED)
					// Parse JSON and convert JSON > String
					// to obtain the sample's description
					String sampleDescr = measurement.getAsJsonPrimitive("sampleDescr").toString();
					sampleDescr = sampleDescr.replaceAll("\"", "");
					newMeasurement.setSampleDescr(sampleDescr);
					
					// Parse JSON and convert JSON > String
					// to obtain the Sample Name
					String sampleName = measurement.getAsJsonPrimitive("sampleName").toString();
					sampleName = sampleName.replaceAll("\"", "");
					newMeasurement.setSampleName(sampleName);
					
					// Obtain Spectro Serial Number from the reqObj (NO LONGER, GET THIS FROM THE JSON)
					// Parse JSON and convert JSON > String
					// to obtain the spectro serial number
					String serialNbr = measurement.getAsJsonPrimitive("spectroSerialNbr").toString();
					serialNbr = serialNbr.replaceAll("\"", "");
					newMeasurement.setSerialNbr(serialNbr);
					
					newMeasurements.add(newMeasurement);
					
				}
			}
			
			customerService.saveCustWebSpectroRemotes(newMeasurements);
			
			return SUCCESS;
		} catch (Exception e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage(), e);
			e.printStackTrace();
			return ERROR;
		}
	}
	
	public String deleteSpectroRemote() {
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			String customerId = reqObj.getCustomerID();
			
			SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MMM/yy hh:mm:ss a");
			Date dateTime = dateTimeFormat.parse(measurementDate);
			
			customerService.deleteCustWebSpectroRemote(customerId, dateTime, measurementSerialNbr);
			return SUCCESS;
		} catch (Exception e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String updateSpectroRemote() {
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			String customerId = reqObj.getCustomerID();
			
			SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MMM/yy hh:mm:ss a");
			Date dateTime = dateTimeFormat.parse(measurementDate);
			
			CustWebSpectroRemote measurement = new CustWebSpectroRemote();
			measurement.setCustomerId(customerId);
			measurement.setSampleDateTime(dateTime);
			measurement.setSerialNbr(measurementSerialNbr);
			measurement.setModel(measurementModel);
			measurement.setSampleName(measurementName);
			measurement.setSampleDescr(measurementDescription);
			
			String[] curveStringArray = measurementMeasuredCurve.split(",");
			double[] measureCurve = new double[curveStringArray.length];
			int index = 0;
			for(double pt : measureCurve) {
				measureCurve[index] = Double.parseDouble(curveStringArray[index]);
				index++;
			}
			measurement.setMeasuredCurve(measureCurve);
			measurement.setRGBHex(measurementRgbHex);
			
			customerService.updateCustWebSpectroRemote(measurement);
			
			
			return SUCCESS;
		} catch (Exception e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage(), e);
			return ERROR;
		}
	}
	
	public CustWebSpectroRemote getCustWebSpectroRemote(){
		return custWebSpectroRemote;
	}
	
	public void setCustWebSpectroRemote(CustWebSpectroRemote custWebSpectroRemote) {
		this.custWebSpectroRemote = custWebSpectroRemote;
	}
	
	public List<CustWebSpectroRemote> getMeasurementTable() {
		return measurementTable;
	}

	public void setMeasurementTable(List<CustWebSpectroRemote> measurementTable) {
		this.measurementTable = measurementTable;
	}

	public List<CustWebSpectroRemote> getLoadTable() {
		return loadTable;
	}

	public void setLoadTable(List<CustWebSpectroRemote> loadTable) {
		this.loadTable = loadTable;
	}

	public String getMeasurementJson() {
		return measurementJson;
	}
	
	public void setMeasurementJson(String measurementJson) {
		this.measurementJson = measurementJson;
	}

	public void setSession(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;		
	}

	public String getReqGuid() {
		return reqGuid;
	}

	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}


	public String[] getHexList() {
		return hexList;
	}


	public void setHexList(String[] hexList) {
		this.hexList = hexList;
	}


	public Map<String, Object> getSessionMap() {
		return sessionMap;
	}


	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	public String getMeasurementDate() {
		return measurementDate;
	}


	public void setMeasurementDate(String measurementDate) {
		this.measurementDate = measurementDate;
	}


	public String getMeasurementSerialNbr() {
		return measurementSerialNbr;
	}


	public void setMeasurementSerialNbr(String measurementSerialNbr) {
		this.measurementSerialNbr = measurementSerialNbr;
	}


	public String getMeasurementModel() {
		return measurementModel;
	}


	public void setMeasurementModel(String measurementModel) {
		this.measurementModel = measurementModel;
	}


	public String getMeasurementName() {
		return measurementName;
	}


	public void setMeasurementName(String measurementName) {
		this.measurementName = measurementName;
	}


	public String getMeasurementDescription() {
		return measurementDescription;
	}


	public void setMeasurementDescription(String measurementDescription) {
		this.measurementDescription = measurementDescription;
	}


	public String getMeasurementMeasuredCurve() {
		return measurementMeasuredCurve;
	}


	public void setMeasurementMeasuredCurve(String measurementMeasuredCurve) {
		this.measurementMeasuredCurve = measurementMeasuredCurve;
	}


	public String getMeasurementRgbHex() {
		return measurementRgbHex;
	}


	public void setMeasurementRgbHex(String measurementRgbHex) {
		this.measurementRgbHex = measurementRgbHex;
	}


	public List<CustWebSpectroRemote> getNewMeasurements() {
		return newMeasurements;
	}


	public void setNewMeasurements(List<CustWebSpectroRemote> newMeasurements) {
		this.newMeasurements = newMeasurements;
	}
	
}