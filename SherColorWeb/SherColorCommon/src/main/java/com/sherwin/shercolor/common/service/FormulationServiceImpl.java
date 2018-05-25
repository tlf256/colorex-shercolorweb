package com.sherwin.shercolor.common.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sherwin.shercolor.common.dao.CdsClrntDao;
import com.sherwin.shercolor.common.dao.CdsClrntIncrDao;
import com.sherwin.shercolor.common.dao.CdsClrntSysDao;
import com.sherwin.shercolor.common.dao.CdsColorMastDao;
import com.sherwin.shercolor.common.dao.CdsFormulaChgListDao;
import com.sherwin.shercolor.common.dao.CdsMiscCodesDao;
import com.sherwin.shercolor.common.dao.CdsProdCharzdDao;
import com.sherwin.shercolor.common.dao.CdsProdDao;
import com.sherwin.shercolor.common.dao.CdsProdFamilyDao;
import com.sherwin.shercolor.common.dao.PosProdDao;
import com.sherwin.shercolor.common.domain.CdsClrnt;
import com.sherwin.shercolor.common.domain.CdsClrntIncr;
import com.sherwin.shercolor.common.domain.CdsClrntSys;
import com.sherwin.shercolor.common.domain.CdsColorMast;
import com.sherwin.shercolor.common.domain.CdsFormulaChgList;
import com.sherwin.shercolor.common.domain.CdsMiscCodes;
import com.sherwin.shercolor.common.domain.CdsProd;
import com.sherwin.shercolor.common.domain.CdsProdCharzd;
import com.sherwin.shercolor.common.domain.CdsProdFamily;
import com.sherwin.shercolor.common.domain.CustWebParms;
import com.sherwin.shercolor.common.domain.FormulaInfo;
import com.sherwin.shercolor.common.domain.FormulaIngredient;
import com.sherwin.shercolor.common.domain.FormulationResponse;
import com.sherwin.shercolor.common.domain.OeFormInput;
import com.sherwin.shercolor.common.domain.OeFormInputColorants;
import com.sherwin.shercolor.common.domain.OeFormInputDataSet;
import com.sherwin.shercolor.common.domain.OeFormInputParms;
import com.sherwin.shercolor.common.domain.OeFormInputRequest;
import com.sherwin.shercolor.common.domain.OeFormResp;
import com.sherwin.shercolor.common.domain.OeFormRespDataSet;
import com.sherwin.shercolor.common.domain.OeFormRespFormula;
import com.sherwin.shercolor.common.domain.OeFormRespMessage;
import com.sherwin.shercolor.common.domain.OeServiceColorDataSet;
import com.sherwin.shercolor.common.domain.OeServiceColorDataSetWrapper;
import com.sherwin.shercolor.common.domain.OeServiceColorantDataSet;
import com.sherwin.shercolor.common.domain.OeServiceColorantDataSetWrapper;
import com.sherwin.shercolor.common.domain.OeServiceProdDataSet;
import com.sherwin.shercolor.common.domain.OeServiceProdDataSetWrapper;
import com.sherwin.shercolor.common.domain.OeServiceRequestWrapper;
import com.sherwin.shercolor.common.domain.OeServiceScenarioFormulation;
import com.sherwin.shercolor.common.domain.PosProd;
import com.sherwin.shercolor.common.domain.ProductFillInfo;
import com.sherwin.shercolor.common.exception.SherColorException;
import com.sherwin.shercolor.common.exception.SherColorExceptionBuilder;
import com.sherwin.shercolor.util.domain.SwMessage;

@Service
@Transactional
public class FormulationServiceImpl implements FormulationService {

	@Autowired
	SherColorExceptionBuilder sherColorExceptionBuilder;
	
	@Autowired
	Locale locale;
	
	@Autowired
	CdsClrntDao clrntDao;
	
	@Autowired
	ColorService colorService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	CdsColorMastDao colorMastDao;
	
	@Autowired
	CdsClrntSysDao clrntSysDao;
	
	@Autowired
	CdsClrntIncrDao clrntIncrDao;
	
	@Autowired
	PosProdDao posProdDao;
	
	@Autowired
	CdsProdDao cdsProdDao;
	
	@Autowired
	CdsProdCharzdDao cdsProdCharzdDao;

	@Autowired
	CdsMiscCodesDao cdsMiscCodesDao;
	
	@Autowired
	CdsProdFamilyDao cdsProdFamilyDao;
	
	@Autowired
	CdsFormulaChgListDao cdsFormulaChgListDao;
	
	@Autowired
	private ResourceBundleMessageSource messageSource;
	
	private String oeBaseUrl;

	static boolean debugon = true;
	
	static Logger logger = LogManager.getLogger(FormulationServiceImpl.class.getName());
	
	public FormulationResponse formulate(String clrntSysId, String colorComp, String colorId, String salesNbr){
		// use defaults
		OeFormInputRequest inputRequest = new OeFormInputRequest();
		FormulationResponse formResponse;
		
		inputRequest.setClrntSysId(clrntSysId);
		inputRequest.setColorComp(colorComp);
		inputRequest.setColorId(colorId);
		inputRequest.setSalesNbr(salesNbr);
		inputRequest.setHighPerformance(false);
		inputRequest.setVinylSafe(false);
		inputRequest.setPctFormula(100);
		
		try {
			formResponse = formulate(inputRequest,null,null,null);
		} catch(Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			throw e;
		}
		return formResponse;
	}

	public FormulationResponse formulate(OeFormInputRequest inputRequest, CustWebParms custWebParms){
		FormulationResponse formResponse = null;
		OeServiceProdDataSet dsProd = null;
		OeServiceColorDataSet dsColor = null;
		
		formResponse = formulate(inputRequest, custWebParms, dsProd, dsColor);
		
		return formResponse;
	}
	
	public FormulationResponse formulate(OeFormInputRequest inputRequest, CustWebParms custWebParms, OeServiceProdDataSet dsProd, OeServiceColorDataSet dsColor){
		FormulationResponse formResponse = new FormulationResponse();
		//FormulaInfo formInfo = null;

		inputRequest.setGetProdFamily(false);
		
		if(debugon && custWebParms==null) System.out.println("Top of formulate custWebParms is null");
		if(debugon && custWebParms!=null) System.out.println("Top of formulate custWebParms is NOT null");
		try {
			// build the OeFormInput object and components
			OeFormInput oeInput = new OeFormInput();

			OeFormInputDataSet oeInputDataSet = new OeFormInputDataSet();

			ArrayList<OeFormInputRequest> oeRequest = new ArrayList<OeFormInputRequest>();
			oeRequest.add(inputRequest);
			oeInputDataSet.setFormInputRequest(oeRequest);

			if(custWebParms!=null){
				ArrayList<OeFormInputParms> oeParms = new ArrayList<OeFormInputParms>();
				oeParms.add(mapCustWebParmsToOeInputParms(custWebParms));
				oeInputDataSet.setFormInputParms(oeParms);
			}
			
			oeInput.setFormulationIn(oeInputDataSet);

			OeFormResp responseObject = null;

			// call OE formulation
			responseObject = callOeFormulation(oeInput, custWebParms, dsProd, dsColor);
			
			// process response from OE formulation (note we need also send in the details of the inputRequest to fill in color comp, id, clrntSys, salesNbr)
			formResponse = processOeResponse(responseObject, inputRequest, custWebParms);
			
			// should be done except...
			// special handling for GETPCT and GETPCTINTEXT
			// if getpct returned and not 100% returned by default, call OE formulation again with percent filled in as 100%
			if((formResponse.getStatus().equalsIgnoreCase("GETPCT") || formResponse.getStatus().equalsIgnoreCase("GETPCTFBBASE")) 
					&& inputRequest.getPctFormula()==0 
					&& formResponse.getFormulas().get(0).getPercent()!=100){
				oeRequest.clear();
				inputRequest.setPctFormula(100);
				oeRequest.add(inputRequest);
				oeInput.getFormulationIn().setFormInputRequest(oeRequest);
				responseObject = callOeFormulation(oeInput, custWebParms, dsProd, dsColor);
				formResponse = processOeResponse(responseObject, inputRequest, custWebParms);
			} else {
				// if getpctintext call for interior 100% and exterior 100%, if first call changes to getpctfbbase then return that info
				if(formResponse.getStatus().equalsIgnoreCase("GETPCTINTEXT") && inputRequest.getPctFormula()==0) {
					oeRequest.clear();
					inputRequest.setPctFormula(100);
					inputRequest.setForceIntExt("INTERIOR");
					oeRequest.add(inputRequest);
					oeInput.getFormulationIn().setFormInputRequest(oeRequest);
					responseObject = callOeFormulation(oeInput, custWebParms, dsProd, dsColor);
					formResponse = processOeResponse(responseObject, inputRequest, custWebParms);
					if(formResponse.getStatus().equalsIgnoreCase("GETPCTFBBASE") || formResponse.getStatus().equalsIgnoreCase("ERROR")) {
						// changed from scpct to pct (can happen, stop here and return this response) or returned error (return this response)
					} else {
						// set fbBase indicator to Interior for user selection
						formResponse.getFormulas().get(0).setFbBase("INTERIOR");
						// save response from Interior so it can be added to exterior result
						List<FormulaInfo> intFormInfo = formResponse.getFormulas();
						
						// call it again for exterior
						oeRequest.clear();
						inputRequest.setForceIntExt("EXTERIOR");
						oeRequest.add(inputRequest);
						oeInput.getFormulationIn().setFormInputRequest(oeRequest);
						responseObject = callOeFormulation(oeInput, custWebParms, dsProd, dsColor);
						formResponse = processOeResponse(responseObject, inputRequest, custWebParms);
						
						// set fbBase indicator to Exterior for user selection
						formResponse.getFormulas().get(0).setFbBase("EXTERIOR");
						
						// tack on formula info from interior pass
						for(FormulaInfo thisIntFormInfo: intFormInfo){
							formResponse.getFormulas().add(thisIntFormInfo);
						}
						
						// we are done, set back to GETPCTINTEXT for calling program
						formResponse.setStatus("GETPCTINTEXT");
					}
					
				}
			}
			if(debugon && formResponse==null) System.out.println("Bottom of formulate formResponse is null");
			if(debugon && formResponse!=null) System.out.println("Bottom of formulate status is " + formResponse.getStatus());
			

		} catch (Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			e.printStackTrace();
			//should this also throw e?  
		} // end try

		return formResponse;
	}
	
	private FormulationResponse processOeResponse(OeFormResp responseObject, OeFormInputRequest inputRequest, CustWebParms custWebParms){
		FormulationResponse formResponse = new FormulationResponse();  // contains formInfo and status
		FormulaInfo formInfo = null;

		List<FormulaInfo> formulas = new ArrayList<FormulaInfo>();
		List<SwMessage> messages = new ArrayList<SwMessage>();
		try {
			if(responseObject.getFormulationDataSet()==null || (responseObject.getFormulationDataSet().getFormRespClrntRow()==null && responseObject.getFormulationDataSet().getMessages()==null)){
				// no messages no formulas
				if(debugon && responseObject.getFormulationDataSet()==null) System.out.println("getFormulationDataSet is null");
				if(debugon && responseObject.getFormulationDataSet().getFormRespClrntRow()==null) System.out.println("getFormRespClrntRow is null");
				if(debugon && responseObject.getFormulationDataSet().getMessages()==null) System.out.println("getMessages is null");
				if(debugon) System.out.println("null formulatDataSet returned?");
				if(debugon) messages.add(new SwMessage(Level.ERROR,"902","Empty DataSet from Formulation"));
			} else {
				// we can have formulas no messages, formulas with messages, no formulas with messages
				if(responseObject.getFormulationDataSet().getMessages()!=null){
					for(OeFormRespMessage oeMsg : responseObject.getFormulationDataSet().getMessages()){
						
						if(oeMsg.getMessageType().equalsIgnoreCase("WARNING")){
							// skip it?? YES because these are warnings like "Vinyl Safe Available but Vinyl Safe not selected"
							if(debugon) System.out.println("Ignore warning from OE formulation?? [" + oeMsg.getMessageText() + "]");
							// messages.add(new SwMessage(Level.WARN,oeMsg.getMessageCode(),oeMsg.getMessageText()));
						} else {
							if(oeMsg.getMessageType().equalsIgnoreCase("ERROR")){
								//messages.add(new SwMessage(Level.ERROR,oeMsg.getMessageCode(),oeMsg.getMessageText()));
								if(oeMsg.getMessageCode().equalsIgnoreCase("9016")){
									messages.add(new SwMessage(Level.ERROR,oeMsg.getMessageCode(),oeMsg.getMessageText()));
								} else {
									// error we can't get around, return cannot formulate to user...
									messages.add(new SwMessage(Level.ERROR,"903","Cannot formulate for this Color and Product [" + oeMsg.getMessageCode() + "]."));
								}
							} else {
								messages.add(new SwMessage(Level.INFO,oeMsg.getMessageCode(),oeMsg.getMessageText()));
							}
							if(debugon) System.out.println("Message is " + oeMsg.getMessageText());
						} // end else not warning
					} // end for each msg
					
				} // end messages not null
				
				if(responseObject.getFormulationDataSet().getFormRespClrntRow()!=null){
					// convert to SherColor Web formula object
					// loop through oeForms and map to FormulaInfo
					for(OeFormRespFormula oeFormInfo: responseObject.getFormulationDataSet().getFormRespClrntRow()){
						formInfo = mapFormRespClrntRowToFormulaInfo(inputRequest, oeFormInfo, custWebParms);
						formulas.add(formInfo);
					}
				}
			} // end else not null dataset
			
			formResponse.setMessages(messages);
			formResponse.setFormulas(formulas);
			
			String respStatus = decipherResults(messages,formulas);
			formResponse.setStatus(respStatus);
		} catch(Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			throw e;
		}
		return formResponse;
	}
	
	private OeFormResp callOeFormulation(OeFormInput oeInput, CustWebParms custWebParms, OeServiceProdDataSet dsProd, OeServiceColorDataSet dsColor){
		OeFormResp oeFormResp = null;
		
		//12/13/2016*BKP* changed this to be set from the properties table as there will be different
		//                formulation servers to call.
		//String oeBaseUrl = "http://stscsb01.sherwin.com:8080/rest/SherColorService/formulationReq";
		URL initUrl;
		HttpURLConnection conn = null;

		try {
			initUrl = new URL(oeBaseUrl.toString()+"/formulationReq");
			conn = (HttpURLConnection) initUrl.openConnection();
			//do as post... 
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept","application/json");

			if(debugon) System.out.println("using oe server " + oeBaseUrl.toString()+"/formulationReq");
			// NEW for Scenario Formulation which affects all formulation..
			if(dsProd==null) dsProd = new OeServiceProdDataSet();
			if(dsColor==null) dsColor = new OeServiceColorDataSet();
			OeServiceColorantDataSet dsColorantSys = new OeServiceColorantDataSet();
			
			// Double wrap dsProd and dsColor for Rod
			OeServiceProdDataSetWrapper oeProdWrapper = new OeServiceProdDataSetWrapper();
			oeProdWrapper.setOeProdDataSet(dsProd);
			OeServiceColorDataSetWrapper oeColorWrapper = new OeServiceColorDataSetWrapper();
			oeColorWrapper.setOeColorDataSet(dsColor);
			OeServiceColorantDataSetWrapper oeColorantWrapper = new OeServiceColorantDataSetWrapper();
			oeColorantWrapper.setOeColorantDataSet(dsColorantSys);
			
			OeServiceScenarioFormulation oeScenarioFormulation = new OeServiceScenarioFormulation();
			oeScenarioFormulation.setOeFormInput(oeInput);
			oeScenarioFormulation.setDsProdWrapper(oeProdWrapper);
			oeScenarioFormulation.setDsColorWrapper(oeColorWrapper);
			oeScenarioFormulation.setDsColorantWrapper(oeColorantWrapper);
			
			OeServiceRequestWrapper request = new OeServiceRequestWrapper();
			request.setOeServiceScenarioFormulation(oeScenarioFormulation);
			
			//Gson gson = new Gson();
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			//String inputBody = gson.toJson(oeInput);
			String inputBody = gson.toJson(request);
			
//			String input = "{\"dsFormulation\":{}}";
			if(debugon) System.out.println(inputBody);
			
			OutputStream os = conn.getOutputStream();
			os.write(inputBody.getBytes());
			os.flush();
			
			int responseCode = conn.getResponseCode();
			if(debugon) System.out.println("responsecode is " + responseCode);
			
			if(responseCode==200 || responseCode==201){
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = br.readLine()) != null){
					response.append(inputLine);
				}
				
				if(debugon) System.out.println("json response from create= " + response.toString());
				GsonBuilder builder = new GsonBuilder();
				//Gson gson = builder.create();
				gson = builder.setDateFormat("yyyy-MM-dd").create();
				oeFormResp = gson.fromJson(response.toString(), OeFormResp.class);
			} else {
				oeFormResp = buildOeErrorResponse("ERROR", Integer.toString(responseCode), "BAD HTTP RESPONSE");
			}
			
			/* json response should be something like
		 * {"dsFormulation":{"cdsColorantTbl":[{"scRequestID":"w2764Z+VZqwXFEbO2J+qxQ",
			 *                                      "FormulaSource":"SHER-COLOR FORMULA",
			 *                                      "clrnt-tint-sys-id":["CCE","R3","Y3","","","","","",""],
			 *                                      "clrnt-nm":["Color Cast","Magenta","Deep Gold","","","","","",""],
			 *                                      "clrnt-shots":[0,1,12,0,0,0,0,0,0],
			 *                                      "clrnt-incr1":["OZ","-","-","","","","","",""],
			 *                                      "clrnt-incr2":["32","-","3","","","","","",""],
			 *                                      "clrnt-incr3":["64","-","-","","","","","",""],
			 *                                      "clrnt-incr4":["128","1","-","","","","","",""],
			 *                                      "clrnt-qual":["","128","128","128","128","128","","",""],
			 *                                      "clrnt-status-ind":["","","","","","","","",""],
			 *                                      "formula-exists":false,
			 *                                      "func-status":"Formulation",
			 *                                      "clrnt-code":["","A60R3","A60Y3","","","","","",""],
			 *                                      "proj-curve":[0.0,0.0,0.0,0.0,43.81288588,61.70915961,69.58636642,72.18809724,74.47647452,75.85933805,76.48068666,76.82107687,77.13646293,77.43864655,78.77060771,80.07381558,81.33333921,82.38351345,83.71261954,84.57371593,84.98997092,85.2851212,85.66749096,85.95892191,85.74874997,85.41392684,85.26083827,85.19907594,85.2439642,85.55861712,86.01545095,86.15936637,86.02883816,85.67422628,85.09408236,0.0,0.0,0.0,0.0,0.0],
			 *                                      "formulation-time":325.0,
			 *                                      "formula-cost":0.0144336524,
			 *                                      "formula-spd":0.0371129555,
			 *                                      "formula-illum":["D65","A","F2"],
			 *                                      "formula-deltae":[0.2518051655,0.2414650732,0.2390319262],
			 *                                      "formula-avgdeltae":0.0,
			 *                                      "formula-metidx":0.0375673287,
			 *                                      "formula-deltae2":0.2217015561,
			 *                                      "formula-oddeltae1":0.8270003815,
			 *                                      "formula-oddeltae2":3.8482965711,
			 *                                      "formula-cr1":99.2051310208,
			 *                                      "formula-cr2":94.0466623293,
			 *                                      "formula-rule":"1",
			 *                                      "formula-prod-nbr":"",
			 *                                      "formula-proc-order":0,
			 *                                      "formula-prod-rev":"",
			 *                                      "formula-clrnt-list":"",
			 *                                      "formula-de-comment":"dE Rank=2,best de=.1253792449,forms lt35=2",
			 *                                      "formula-color-eng-ver":"2DK",
			 *                                      "formula-pass-nbr":3,
			 *                                      "FormulationWarning":"",
			 *                                      "dbRowiD":null,
			 *                                      "deltaeWarning":""}]}}
			 */

		} catch (MalformedURLException e) {
			//e.printStackTrace();
			logger.error(e.toString() + " " + e.getMessage());
			oeFormResp = buildOeErrorResponse("ERROR", "999", "MalformedURLException");
		} catch (ConnectException e) {
			//e.printStackTrace();
			logger.error(e.toString() + " " + e.getMessage());
			oeFormResp = buildOeErrorResponse("ERROR", "999", "ConnectException");
		} catch (IOException e) {
			//e.printStackTrace();
			logger.error(e.toString() + " " + e.getMessage());
			oeFormResp = buildOeErrorResponse("ERROR", "999", "IOException");
		} finally {
			if(conn!=null) conn.disconnect();
			if(oeFormResp==null){
				oeFormResp = new OeFormResp();
				oeFormResp = buildOeErrorResponse("ERROR", "999", "Unknown Error");
			}
		}
		
		return oeFormResp;
	}
	
	private String decipherResults(List<SwMessage> messages, List<FormulaInfo> formulas){
		String retVal = "ERROR";
		
		// any errors in messages
		boolean foundError = false;
		boolean foundIntExtScPct = false;
		
		try {
			for(SwMessage msg : messages){
				if(msg.getSeverity().isInRange(Level.FATAL,Level.ERROR)){
					if(debugon) System.out.println("Processing message " + msg.getSeverity().name() + " with level " + msg.getSeverity().intLevel() + " code is " + msg.getCode());
					if(msg.getCode().equalsIgnoreCase("9016")) {
						// special error indicates we need int/ext to get scpct
						foundIntExtScPct = true;
					} else {
						foundError = true;
					}
				}
				if(debugon) System.out.println(msg.getSeverity().intLevel() + " is " + msg.getSeverity().name());
				if(debugon) System.out.println("btw " + Level.FATAL.intLevel() + " is " + Level.FATAL.name());
			}
			if (foundError){
				retVal = "ERROR";
			} else {
				// no errors so check how many forms and if pct/fbBase/intExt needed
				if (formulas.size()==1){
					// one formula so it is either complete or Percent needed
					if(formulas.get(0).getSource().equalsIgnoreCase("PCT")){
						// percent needed
						retVal="GETPCT";
					} else if(formulas.get(0).getSource().equalsIgnoreCase("SCPCT")){
						// check if this is an "Auto" Percent of SherColor
						CdsProd cdsProd = cdsProdDao.read(formulas.get(0).getSalesNbr());
						if(cdsProd!=null && cdsProd.getPercentSource().toUpperCase().startsWith("AUTO")){
							retVal = "COMPLETE";
						} else {
							retVal = "GETPCT";
						}
					} else {
						retVal = "COMPLETE";
					}
				} else {
					// more than one formula so it could be FB(base/sub base choices) or PCT(base/subase)
					// wait, could be zero formulas because no cds-prod, tell them to get IntExt and Percent
					if(foundIntExtScPct){
						retVal = "GETPCTINTEXT";
					} else {
						if(formulas.get(0).getSource().equalsIgnoreCase("FB")){
							// base/subbase needed
							retVal="GETFBBASE";
						} else {
							// base/subbase and percent needed
							retVal = "GETPCTFBBASE";
						}
					}
				}
			}
		} catch(Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			throw e;
		}
		return retVal;
	}
	
	private OeFormResp buildOeErrorResponse(String messageType, String messageCode, String messageText){
		OeFormRespMessage oeRespMessage = new OeFormRespMessage();
		oeRespMessage.setMessageCode(messageCode);
		oeRespMessage.setMessageType(messageType);
		oeRespMessage.setMessageText(messageText);
		
		ArrayList<OeFormRespMessage> oeMessages = new ArrayList<OeFormRespMessage>();
		oeMessages.add(oeRespMessage);
		
		OeFormRespDataSet oeRespDataSet = new OeFormRespDataSet();
		oeRespDataSet.setMessages(oeMessages);
		
		OeFormResp oeFormResp = new OeFormResp();
		oeFormResp.setFormulationDataSet(oeRespDataSet);
		
		return oeFormResp;
	}
	
	private FormulaInfo mapFormRespClrntRowToFormulaInfo(OeFormInputRequest inputRequest, OeFormRespFormula oeFormInfo, CustWebParms custWebParms){
		FormulaInfo formInfo = new FormulaInfo();
		
		CdsClrnt thisClrnt;
		
		try {
			if(debugon) System.out.println(oeFormInfo.getFormulaSourceDescr());
			
			if(debugon) System.out.println(Arrays.toString(oeFormInfo.getClrntTintSysId()));
			if(debugon) System.out.println(Arrays.toString(oeFormInfo.getClrntIncr1()));
			if(debugon) System.out.println(Arrays.toString(oeFormInfo.getClrntIncr2()));
			if(debugon) System.out.println(Arrays.toString(oeFormInfo.getClrntIncr3()));
			if(debugon) System.out.println(Arrays.toString(oeFormInfo.getClrntIncr4()));
			
			formInfo = new FormulaInfo();
	
			String uuid = UUID.randomUUID().toString().replace("-", "");
			formInfo.setGuid(uuid);
			formInfo.setClrntSysId(inputRequest.getClrntSysId());
			formInfo.setColorComp(inputRequest.getColorComp());
			formInfo.setColorId(inputRequest.getColorId());
			formInfo.setSalesNbr(inputRequest.getSalesNbr());
			
			PosProd posProd = posProdDao.read(inputRequest.getSalesNbr());
			if(posProd!=null){
				formInfo.setProdNbr(posProd.getProdNbr());
				formInfo.setSizeCode(posProd.getSzCd());
			}
	
			String clrntSysName = clrntSysDao.read(inputRequest.getClrntSysId()).getClrntSysName();
			formInfo.setClrntSysName(clrntSysName);
			
			//do a single call to the colorant service to get a list of colorants for the colorant system used.
			HashMap<String,CdsClrnt> thisClrntList = clrntDao.mapTintSysIdForClrntSys(inputRequest.getClrntSysId(), true);
			
			// map from OpenEdge object to SherColorWeb object
			
			// if Competitive or Custom, need to prefix with "Custom" and suffix with "Match"
			formInfo.setSource(oeFormInfo.getFormulaType());
			boolean companyOwned = colorService.isCompanyOwnedColor(formInfo.getColorComp(),formInfo.getColorId(), custWebParms);
			switch(formInfo.getSource().toUpperCase()){
			case "ENG":
				if(companyOwned){
					formInfo.setSourceDescr(oeFormInfo.getFormulaSourceDescr());
				} else {
					formInfo.setSourceDescr("CUSTOM SHER-COLOR MATCH");
				}
				break;
			case "FB":
				if(companyOwned){
					formInfo.setSourceDescr(oeFormInfo.getFormulaSourceDescr());
				} else {
					formInfo.setSourceDescr("CUSTOM FORMULA BOOK MATCH");
				}
				break;
			case "FBVS":
				if(companyOwned){
					formInfo.setSourceDescr(oeFormInfo.getFormulaSourceDescr());
				} else {
					formInfo.setSourceDescr("CUSTOM VINYL SAFE FORMULA BOOK MATCH");
				}
				break;
			case "MAN":
				if(companyOwned){
					formInfo.setSourceDescr("CUSTOM MANUAL FORMULA"); //should never get here
				} else {
					formInfo.setSourceDescr("CUSTOM MANUAL MATCH");
				}
				break;
			case "MANV":
				if(companyOwned){
					formInfo.setSourceDescr("MANUAL VINYL SAFE FORMULA"); //should never get here
				} else {
					formInfo.setSourceDescr("CUSTOM MANUAL VINYL SAFE MATCH");
				}
				break;
			case "SCPCT":
				if(companyOwned){
					formInfo.setSourceDescr(oeFormInfo.getFormulaSourceDescr());
				} else {
					formInfo.setSourceDescr("CUSTOM " + oeFormInfo.getFormulaSourceDescr() + " MATCH");
				}
				break;
			case "VNL":
				if(companyOwned){
					formInfo.setSourceDescr(oeFormInfo.getFormulaSourceDescr());
				} else {
					formInfo.setSourceDescr("CUSTOM MANUAL MATCH");
				}
				break;
			default:
				if(companyOwned){
					formInfo.setSourceDescr(oeFormInfo.getFormulaSourceDescr());
				} else {
					formInfo.setSourceDescr("CUSTOM " + oeFormInfo.getFormulaSourceDescr() + " MATCH");
				}
				break;
			}
	
			if(debugon) System.out.println("FormSource is " + formInfo.getSource());
			// get ingredients
			formInfo.getIncrementHdr().add(oeFormInfo.getClrntIncr1()[0]);
			formInfo.getIncrementHdr().add(oeFormInfo.getClrntIncr2()[0]);
			formInfo.getIncrementHdr().add(oeFormInfo.getClrntIncr3()[0]);
			formInfo.getIncrementHdr().add(oeFormInfo.getClrntIncr4()[0]);
			for(int i=1; i<oeFormInfo.getClrntShots().length;i++){
				if(oeFormInfo.getClrntShots()[i]==0) continue;
				FormulaIngredient ingredient = new FormulaIngredient();
				
				ingredient.setClrntSysId(inputRequest.getClrntSysId());
				ingredient.setShots(oeFormInfo.getClrntShots()[i]);
				ingredient.setTintSysId(oeFormInfo.getClrntTintSysId()[i]);
				ingredient.setEngSysId(oeFormInfo.getClrntCode()[i]);
				ingredient.setName(oeFormInfo.getClrntNm()[i]);
				ingredient.setShotSize(Integer.valueOf(oeFormInfo.getClrntQual()[i]));
				if(!oeFormInfo.getClrntIncr1()[i].trim().isEmpty() && !oeFormInfo.getClrntIncr1()[i].trim().equals("-")){
					ingredient.getIncrement()[0]=Integer.valueOf(oeFormInfo.getClrntIncr1()[i].trim());
				} else {
					ingredient.getIncrement()[0]=0;
				}
				if(!oeFormInfo.getClrntIncr2()[i].trim().isEmpty() && !oeFormInfo.getClrntIncr2()[i].trim().equals("-")){
					ingredient.getIncrement()[1]=Integer.valueOf(oeFormInfo.getClrntIncr2()[i].trim());
				} else {
					ingredient.getIncrement()[1]=0;
				}
				if(!oeFormInfo.getClrntIncr3()[i].trim().isEmpty() &&!oeFormInfo.getClrntIncr3()[i].trim().equals("-")){
					ingredient.getIncrement()[2]=Integer.valueOf(oeFormInfo.getClrntIncr3()[i].trim());
				} else {
					ingredient.getIncrement()[2]=0;
				}
				if(!oeFormInfo.getClrntIncr4()[i].trim().isEmpty() && !oeFormInfo.getClrntIncr4()[i].trim().equals("-")){
					ingredient.getIncrement()[3]=Integer.valueOf(oeFormInfo.getClrntIncr4()[i].trim());
				} else {
					ingredient.getIncrement()[3]=0;
				}
				ingredient.setStatusInd(oeFormInfo.getClrntStatusInd()[i]);
				
				// fill the rest of ingredients from cdsClrnt table
				thisClrnt = thisClrntList.get(oeFormInfo.getClrntTintSysId()[i]);
				if (thisClrnt!=null){
					ingredient.setOrganicInd(thisClrnt.getOrganicInd());
					ingredient.setStatusInd(thisClrnt.getStatusInd());
					ingredient.setExcludeProd(thisClrnt.getExcludeProd());
					ingredient.setForceProd(thisClrnt.getForceProd());
				}
				formInfo.getIngredients().add(ingredient);
			} // next i
			
			// get the rest of the formula Stuff
			formInfo.setOeRequestId(oeFormInfo.getScRequestId());
			formInfo.setProjectedCurve(oeFormInfo.getProjCurve());
			formInfo.setMeasuredCurve(oeFormInfo.getMeasCurve());
			formInfo.setFormulationTime(oeFormInfo.getFormulaTime()/1000);
			formInfo.setColorantCost(oeFormInfo.getFormulaCost());
			formInfo.setSpd(oeFormInfo.getFormulaSpd());
			formInfo.setDeltaEs(oeFormInfo.getFormulaDeltaE());
			formInfo.setAverageDeltaE(oeFormInfo.getFormulaAvgDeltaE());
			formInfo.setMetamerismIndex(oeFormInfo.getFormulaMetidx());
			formInfo.setDeltaEThin(oeFormInfo.getFormulaDeltaE2());
			formInfo.setDeltaEOverDarkThick(oeFormInfo.getFormulaOdDeltaE1());
			formInfo.setDeltaEOverDarkThin(oeFormInfo.getFormulaOdDeltaE2());
			formInfo.setContrastRatioThick(oeFormInfo.getFormulaCr1());
			formInfo.setContrastRatioThin(oeFormInfo.getFormulaCr2());
			formInfo.setRule(oeFormInfo.getFormulaRule());
			formInfo.setDeltaEComment(oeFormInfo.getFormulaDeComment());
			formInfo.setColorEngineVersion(oeFormInfo.getFormulaColorEngVer());
			formInfo.setFormulaExists(oeFormInfo.getFormulaExists());
			formInfo.setFuncStatus(oeFormInfo.getFuncStatus());
			formInfo.setIllums(oeFormInfo.getFormulaIllum());
			//formInfo.setProdNbr(oeFormInfo.getFormulaProdNbr());
			formInfo.setProdRev(oeFormInfo.getFormulaProdRev());
			formInfo.setProcOrder(oeFormInfo.getFormulaProcOrder());
			formInfo.setClrntList(oeFormInfo.getFormulaClrntList());
			formInfo.setEnginePassNbr(oeFormInfo.getFormulaPassNbr());
			formInfo.setFormulationWarning(oeFormInfo.getFormulationWarning());
			formInfo.setDeltaEWarning(oeFormInfo.getDeltaEWarning());
			formInfo.setPercent(oeFormInfo.getFormulaPct());
			formInfo.setFbBase(oeFormInfo.getFormulaFbBase());
			formInfo.setFbSubBase(oeFormInfo.getFormulaFbSubBase());
		} catch(Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			throw e;
		}
		
		return formInfo;
	}
	
	private OeFormInputParms mapCustWebParmsToOeInputParms(CustWebParms custWebParms){
		OeFormInputParms oeFormInputParms = new OeFormInputParms();
		if(debugon && custWebParms==null) System.out.println("top of mapCustWebPartToOeInputParms and custWebParms is null");
		if(debugon && custWebParms!=null) System.out.println("top of mapCustWebPartToOeInputParms and custWebParms is NOT null");
		try {
			if (custWebParms!=null){
				// convert character string to some number
				String buff = custWebParms.getCustomerId();
				int sumOfAsciiDec=0;
				for(int i=0;i<buff.length();i++){
					sumOfAsciiDec+=(int)buff.charAt(i);
				}
				oeFormInputParms.setCustomerId(sumOfAsciiDec);
				oeFormInputParms.setSeqNbr(custWebParms.getSeqNbr());
				oeFormInputParms.setAbbrev(custWebParms.getAbbrev());
				oeFormInputParms.setStoreComp(custWebParms.getStoreComp());
				oeFormInputParms.setColorComp(custWebParms.getColorComp());
				oeFormInputParms.setProdComp(custWebParms.getProdComp());
				oeFormInputParms.setClrntSysId(custWebParms.getClrntSysId());
				oeFormInputParms.setActive(custWebParms.isActive());
				oeFormInputParms.setSwuiTyp(custWebParms.getSwuiTyp());
				oeFormInputParms.setSwuiTitle(custWebParms.getSwuiTitle());
				// build ArrayList for AltColorComp
				ArrayList<String> altColorComp = new ArrayList<String>();
				if(custWebParms.getAltColorComp1()==null){
					altColorComp.add("");
				} else {
					altColorComp.add(custWebParms.getAltColorComp1());
				}
				if(custWebParms.getAltColorComp2()==null){
					altColorComp.add("");
				} else {
					altColorComp.add(custWebParms.getAltColorComp2());
				}
				if(custWebParms.getAltColorComp3()==null){
					altColorComp.add("");
				} else {
					altColorComp.add(custWebParms.getAltColorComp3());
				}
				if(custWebParms.getAltColorComp4()==null){
					altColorComp.add("");
				} else {
					altColorComp.add(custWebParms.getAltColorComp4());
				}
				if(custWebParms.getAltColorComp5()==null){
					altColorComp.add("");
				} else {
					altColorComp.add(custWebParms.getAltColorComp5());
				}
				oeFormInputParms.setAltColorComp(altColorComp);
				// build ArrayList for AltProdComp
				ArrayList<String> altProdComp = new ArrayList<String>();
				if(custWebParms.getAltProdComp1()==null){
					altProdComp.add("");
				} else {
					altProdComp.add(custWebParms.getAltProdComp1());
				}
				if(custWebParms.getAltProdComp2()==null){
					altProdComp.add("");
				} else {
					altProdComp.add(custWebParms.getAltProdComp2());
				}
				if(custWebParms.getAltProdComp3()==null){
					altProdComp.add("");
				} else {
					altProdComp.add(custWebParms.getAltProdComp3());
				}
				if(custWebParms.getAltProdComp4()==null){
					altProdComp.add("");
				} else {
					altProdComp.add(custWebParms.getAltProdComp4());
				}
				if(custWebParms.getAltProdComp5()==null){
					altProdComp.add("");
				} else {
					altProdComp.add(custWebParms.getAltProdComp5());
				}
				oeFormInputParms.setAltProdComp(altProdComp);
				oeFormInputParms.setFormRule(custWebParms.getFormRule());
				oeFormInputParms.setBulkDeep(custWebParms.isBulkDeep());
				oeFormInputParms.setBulkDn(custWebParms.getBulkDn());
				oeFormInputParms.setBulkUp(custWebParms.getBulkUp());
				oeFormInputParms.setBulkStart(custWebParms.getBulkStart());
				oeFormInputParms.setColorPrime(custWebParms.isColorPrime());
				oeFormInputParms.setOpacityCtrl(custWebParms.isOpacityCtrl());
				oeFormInputParms.setFormQtrShot(custWebParms.isFormQtrShot());
				oeFormInputParms.setTargetCr2(custWebParms.isTargetCr2());
				oeFormInputParms.setLastUpd(custWebParms.getLastUpd());
				oeFormInputParms.setCdsAdlFld(custWebParms.getCdsAdlFld());
				oeFormInputParms.setScRequestId("");
				oeFormInputParms.setDbRowId("");
				
			}
		} catch(Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			throw e;
		}
		return oeFormInputParms;
	}
	
	public FormulationResponse prodFamilyFormulate(OeFormInputRequest inputRequest, CustWebParms custWebParms){
		FormulationResponse formResponse = null;
		OeServiceProdDataSet dsProd = null;
		OeServiceColorDataSet dsColor = null;
		
		formResponse = prodFamilyFormulate(inputRequest, custWebParms, dsProd, dsColor);

		return formResponse;
	}

	public FormulationResponse prodFamilyFormulate(OeFormInputRequest inputRequest, CustWebParms custWebParms, OeServiceProdDataSet dsProd, OeServiceColorDataSet dsColor){
		FormulationResponse formResponse = null;
		String enteredSalesNbr = inputRequest.getSalesNbr();
		boolean rule1ForEntered=false, rule1Found=false, rule1ForOther=false;
		
		try {
			PosProd enteredPosProd = posProdDao.read(enteredSalesNbr); 
			List<CdsProdFamily> prodFamilyList = cdsProdFamilyDao.listFullFamilyForPrimaryProdNbr(enteredPosProd.getProdNbr(), true);
			Collections.sort(prodFamilyList,CdsProdFamily.ProcOrderComparator);
			
			if(prodFamilyList.size()>1){
				int prevProcOrder = 0;
				int startProcOrder = 0;
				for(CdsProdFamily thisProdFam: prodFamilyList){
					if(thisProdFam.getProdNbr().equalsIgnoreCase(enteredPosProd.getProdNbr())){
						if(prevProcOrder==0){
							startProcOrder = thisProdFam.getProcOrder();
						} else {
							startProcOrder = prevProcOrder;
						}
					}
					prevProcOrder = thisProdFam.getProcOrder();
				}
	
				HashMap<String,FormulationResponse> responseObjects = new HashMap<String,FormulationResponse>();
	
				// formulate for prodFamily List, go in proc order, you may be able to stop early of entered prod gets a rule 1 formula
				for(CdsProdFamily thisProdFam: prodFamilyList){
					if(thisProdFam.getProcOrder()>=startProcOrder){
						// set sales number
						OeFormInputRequest prodFamRequest = inputRequest;
						PosProd prodFamPosProd = posProdDao.readByProdNbrSzCd(thisProdFam.getProdNbr(), enteredPosProd.getSzCd());
						if (prodFamPosProd==null){
							continue;
						} else {
							prodFamRequest.setSalesNbr(prodFamPosProd.getSalesNbr());
							// formulate
							FormulationResponse prodFamResponse = formulate(prodFamRequest, custWebParms, dsProd, dsColor);
							System.out.println("thisProdNbr " + thisProdFam.getProdNbr() + " next action is " + prodFamResponse.getStatus());
							// check if good formula returned
							if(prodFamResponse.getStatus().equalsIgnoreCase("COMPLETE")){
								responseObjects.put(thisProdFam.getProdNbr(), prodFamResponse);
								// is it a rule 1 and entered prod? if so, add to hash map and leave
								String formRule = prodFamResponse.getFormulas().get(0).getRule();
								if(formRule.equalsIgnoreCase("1") || formRule.startsWith("1") || formRule.equals("11") ||
								   formRule.startsWith("11-") || formRule.startsWith("21") || formRule.startsWith("31")){
									rule1Found = true;
									if(thisProdFam.getProdNbr().equalsIgnoreCase(enteredPosProd.getProdNbr())){
										rule1ForEntered = true;
										// this is the entered product and it is a rule one
										// if this is an UD, then we must continue on to check other bases
										// if not UD then we do not continue formulating others, we will exit loop
										if(!thisProdFam.getBaseType().equalsIgnoreCase("UD")){
											break;
										}
									} else {
										// rule1 for other can override rule1 for entered only if other is not a Luminous base
										if(!thisProdFam.getBaseType().equalsIgnoreCase("LW")) rule1ForOther = true;
									}
								} // end not a rule 1 formula continue formulation
							} else { 
								continue;
							} // end test for complete
						} // end test for prod family product not on file
					} 
				} // end for loop of product family formulation
				
				// was there a rule 1 for entered and no other rule 1 formulas? if so take entered and we are done
				if(rule1ForEntered && !rule1ForOther){
					formResponse = responseObjects.get(enteredPosProd.getProdNbr());
				} else {
					if(responseObjects.size()>0){
						// may need to return two choices, eval enteredPosProd result vs others
						// if enteredPosProd is best, only return enteredPosProd result otherwise return entered plus best alt choice
						String bestProdNbr = pickProdFamilyTopChoice(prodFamilyList, responseObjects);
						if(bestProdNbr==null){
							formResponse = responseObjects.get(enteredPosProd.getProdNbr());
						} else {
							if(bestProdNbr.equalsIgnoreCase(enteredPosProd.getProdNbr())){
								// entered is best, only return one
								formResponse = responseObjects.get(enteredPosProd.getProdNbr());
							} else {
								// build new responseObject with two formulas, first is best choice, second is entered prod
								// set ActionStatus to PICKPRODFAM
								formResponse = new FormulationResponse();
								formResponse.setStatus("PICKPRODFAM");
								List<FormulaInfo> formulas = new ArrayList<FormulaInfo>();
								changeDeWarningToUseAverage(responseObjects.get(bestProdNbr).getFormulas().get(0));
								changeDeWarningToUseAverage(responseObjects.get(enteredPosProd.getProdNbr()).getFormulas().get(0));
								formulas.add(responseObjects.get(bestProdNbr).getFormulas().get(0));
								formulas.add(responseObjects.get(enteredPosProd.getProdNbr()).getFormulas().get(0));
//								formulas.add(changeDeWarningToUseAverage(responseObjects.get(bestProdNbr).getFormulas().get(0)));
//								formulas.add(changeDeWarningToUseAverage(responseObjects.get(enteredPosProd.getProdNbr()).getFormulas().get(0)));
								formResponse.setFormulas(formulas);
							}
						} // end if else best is null
					} else {
						// no formulas returned, just formlate for entered and return that answer
						formResponse = formulate(inputRequest, custWebParms, dsProd, dsColor);
					} // end else no formulas returned
				} // end else no rule1 or multiple rule 1s
			} else {
				// no prodFamilyList or just 1 entry in list... just call formulation and return
				formResponse = formulate(inputRequest, custWebParms, dsProd, dsColor);
			} // else no prod family to process
		} catch(Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			throw e;
		}
		
	
		return formResponse;
	}
	
	private String pickProdFamilyTopChoice(List<CdsProdFamily> prodFamilyList, HashMap<String, FormulationResponse> formulaHashMap){
		String retVal = null;
		
		Collections.sort(prodFamilyList,CdsProdFamily.ProcOrderComparator);

		int bestRule=99, currentRule=99;
		double bestDe=999.999D, currentDe=999.999D;
		String bestProdNbr=null, currentProdNbr=null;
		int bestProcOrder=99, currentProcOrder=99;
		FormulationResponse currentFormObj;
		
		// is the lowest rule a sort by de rule (e.g. 4, 22, 32, 23, 33, 24, 34, 25, more...)
		Integer[] lowDeRule = {4, 22, 23, 24, 25, 26, 27, 32, 33, 34, 35, 36, 37};
		//List<Integer> lowDeRule = Arrays.asList({4, 22, 23, 24, 25, 26, 27, 32, 33, 34, 35, 36, 37});
		boolean pickByDe = true;
		int checkRule=0;
		
		try {
			for (FormulationResponse formResp : formulaHashMap.values()){
				checkRule=convertRuleToInt(formResp.getFormulas().get(0).getRule());
				if(!Arrays.asList(lowDeRule).contains(checkRule)){
					pickByDe = false;
				}
			}
			
			for(CdsProdFamily thisProdFam: prodFamilyList){
				currentFormObj=formulaHashMap.get(thisProdFam.getProdNbr());
				if(currentFormObj==null) continue; // no formulation for product go to next iteration
				if(bestProdNbr==null || bestProdNbr.isEmpty()){
					// first formula set to best
					bestRule = convertRuleToInt(currentFormObj.getFormulas().get(0).getRule());
					bestProdNbr = thisProdFam.getProdNbr();
					bestDe = currentFormObj.getFormulas().get(0).getAverageDeltaE();
					bestProcOrder = thisProdFam.getProcOrder();
				} else {
					currentRule = convertRuleToInt(currentFormObj.getFormulas().get(0).getRule());
					currentProdNbr = thisProdFam.getProdNbr();
					currentDe = currentFormObj.getFormulas().get(0).getAverageDeltaE();
					currentProcOrder = thisProdFam.getProcOrder();
					
					if(pickByDe) {
						if(currentDe<bestDe){
							bestProdNbr = currentProdNbr;
							bestDe = currentDe;
						}
					} else {
						if(currentRule<bestRule){
							bestRule = currentRule;
							bestProdNbr = currentProdNbr;
							bestProcOrder = currentProcOrder;
						} else {
							if(currentRule==bestRule){
								// tied on rule, go by DE
								if(currentProcOrder<bestProcOrder){
									bestRule = currentRule;
									bestProdNbr = currentProdNbr;
									bestProcOrder = currentProcOrder;
								}
							} // end de compare
						} // end rule compare
					}
				} // end if not first item in list
			} // end looping through prodFamily list
		} catch(Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			throw e;
		}
		
		retVal = bestProdNbr;
		
		return retVal;
	}
	
	private void changeDeWarningToUseAverage(FormulaInfo formInfo){
		String startingDeWarn = formInfo.getDeltaEWarning();
		if(startingDeWarn!=null && !startingDeWarn.isEmpty()){
			int idxFormulaIs = startingDeWarn.indexOf("formula is") + 11;
			int idxDeltaE = startingDeWarn.indexOf("Delta E") - 1;
			String newDeWarn = startingDeWarn.substring(0, idxFormulaIs) + String.format("%.2f",formInfo.getAverageDeltaE()) + startingDeWarn.substring(idxDeltaE, startingDeWarn.length());
			formInfo.setDeltaEWarning(newDeWarn);
		}
	}
	
	private int convertRuleToInt(String ruleAsString){
		int retVal = 999;
		try {
			// remove a, b, or c
			String noAlpha = ruleAsString;
			if(noAlpha.indexOf("a")>-1) noAlpha = noAlpha.replace("a", "");
			if(noAlpha.indexOf("b")>-1) noAlpha = noAlpha.replace("b", "");
			if(noAlpha.indexOf("c")>-1) noAlpha = noAlpha.replace("c", "");
			
			// split on hyphen if it exists
			if(noAlpha.indexOf("-")>-1) {
				// rule is before the hyphen
				String preHyphen = noAlpha.split("-")[0];
				retVal = Integer.valueOf(preHyphen);
			} else {
				// cast to int
				retVal = Integer.valueOf(noAlpha);
			}
		} catch(Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			throw e;
		}
		return retVal;
	}

	
	public FormulaInfo scaleFormulaByPercent(FormulaInfo hundredPercentFormula, int percent){
		double tmpShots;
		
		FormulaInfo newFormula = (FormulaInfo) hundredPercentFormula.clone();
		
		try {

			
			List<FormulaIngredient> newIngredients = new ArrayList<FormulaIngredient>();
			
			for(FormulaIngredient ingredient : newFormula.getIngredients()){
				// Apply percent change
				tmpShots = ingredient.getShots() * percent / 100.0D;
				ingredient.setShots((int) Math.round(tmpShots));
				newIngredients.add(ingredient);
				if(debugon) System.out.println("applied " + percent + "% shots are " + ingredient.getShots());
			}
			if(convertShotsToIncr(newIngredients)) newFormula.setIngredients(newIngredients);
		} catch(Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			throw e;
		}

		return newFormula;
	}
	
	
	public boolean convertShotsToIncr(List<FormulaIngredient> ingredients){
		boolean retVal = false;
		
		try {
			if(ingredients == null || ingredients.size()==0){
				// nothing to do, so success is true
				retVal = true;
				if(debugon) System.out.println("convertShotsToIncr and ingredients is empty");
			} else {
				// from db read
				// TODO make an new method to perform the colorant system calculations so it can be shared with other routine
				int incrCount = 0;
				String clrntSysId = ingredients.get(0).getClrntSysId();
				CdsClrntSys clrntSys = clrntSysDao.read(clrntSysId);
				List<CdsClrntIncr> clrntIncrs = clrntIncrDao.listForClrntSys(clrntSysId);
				
				Collections.sort(clrntIncrs,CdsClrntIncr.OzIncrRatioComparator);
				
				int shotSize = Integer.valueOf(clrntSys.getClrntShotSz()); //e.g. CCE s/b 128;
				
				int incr1toOzRatio = 0;
				int incr2toOzRatio = 0;
				int incr3toOzRatio = 0;
				int incr4toOzRatio = 0;
				
				incrCount = clrntIncrs.size();
				if(incrCount==4) {
					incr1toOzRatio = (int) clrntIncrs.get(0).getOzIncrRatio(); //e.g. CCE s/b 1
					incr2toOzRatio = (int) clrntIncrs.get(1).getOzIncrRatio(); //e.g. CCE s/b 32;
					incr3toOzRatio = (int) clrntIncrs.get(2).getOzIncrRatio(); //e.g. CCE s/b 64;
					incr4toOzRatio = (int) clrntIncrs.get(3).getOzIncrRatio(); //e.g. CCE s/b 128;
				} else {
					// must be 3 then, here is how we handle this
					incr1toOzRatio = (int) clrntIncrs.get(0).getOzIncrRatio(); //e.g. 84448 s/b 1
					incr2toOzRatio = (int) clrntIncrs.get(1).getOzIncrRatio(); //e.g. 84448 s/b 48;
					incr3toOzRatio = (int) clrntIncrs.get(2).getOzIncrRatio(); //e.g. 84448 s/b 96;
				}
				
				// calc incr to shots factor
				int incr1ToShots = shotSize/incr1toOzRatio; //e.g. CCE s/b 128
				int incr2ToShots = shotSize/incr2toOzRatio; //e.g. CCE s/b 4
				int incr3ToShots = shotSize/incr3toOzRatio; //e.g. CCE s/b 2
				int incr4ToShots = 0;
				if(incrCount==4) incr4ToShots = shotSize/incr4toOzRatio; //e.g. CCE s/b 1

				// TODO detect this from clrntIncr file...
				boolean ozAsEven = true;

				// walk through ingredients and do the math
				int tmpIncr1 = 0;
				int tmpIncr2 = 0;
				int tmpIncr3 = 0;
				int tmpIncr4 = 0;
				int remaining = 0;
				
				for(FormulaIngredient ingredient : ingredients){
					// calc incr1 
					if(debugon) System.out.println("convertShotsToIncr " + ingredient.getShots() + " " + incr1ToShots + " " + incr2ToShots + " " + incr3ToShots + " " + incr4ToShots);
					remaining = ingredient.getShots();
					if(ozAsEven) {
						tmpIncr1 = (int) remaining / (incr1ToShots);
						if(tmpIncr1 % 2>0) { //if not divisible by 2
							tmpIncr1 -= 1;
						}
					} else {
						tmpIncr1 = (int) remaining / incr1ToShots;
					}
					remaining = remaining - (tmpIncr1 * incr1ToShots);
					
					// calc incr2 
					tmpIncr2 = (int) remaining / incr2ToShots;
					remaining = remaining - (tmpIncr2 * incr2ToShots);
					
					// calc incr 3
					tmpIncr3 = (int) remaining / incr3ToShots;
					remaining = remaining - (tmpIncr3 * incr3ToShots);
					
					// calc incr 4
					if(incrCount==4){
						tmpIncr4 = (int) remaining / incr4ToShots;
						remaining = remaining - (tmpIncr4 * incr4ToShots);
					}
					
					int[] newIncrs = {0,0,0,0};
					newIncrs[0] = tmpIncr1;
					newIncrs[1] = tmpIncr2;
					newIncrs[2] = tmpIncr3;
					newIncrs[3] = tmpIncr4;
					
					if(debugon) System.out.println("output is " + Arrays.toString(newIncrs));
					
					ingredient.setIncrement(newIncrs);
					ingredient.setShotSize(shotSize);
				}
				retVal = true;
			}
		} catch (Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			retVal = false;
			SherColorException se = new SherColorException();
			se.setCode(1002);
			se.setMessage(messageSource.getMessage("1002", new Object[] {}, locale ));
			throw se;
		}

		
		return retVal;
	}

	
	public boolean convertIncrToShots(List<FormulaIngredient> ingredients){
		boolean retVal = false;
		
		try {
			if(ingredients == null || ingredients.size()==0){
				// nothing to do, so success is true
				retVal = true;
			} else {
				// from db read
				// TODO make an new object to hold this information so it can be used in other routine
				int incrCount = 0;
				String clrntSysId = ingredients.get(0).getClrntSysId();
				CdsClrntSys clrntSys = clrntSysDao.read(clrntSysId);
				List<CdsClrntIncr> clrntIncrs = clrntIncrDao.listForClrntSys(clrntSysId);
				
				Collections.sort(clrntIncrs,CdsClrntIncr.OzIncrRatioComparator);
				
				int shotSize = Integer.valueOf(clrntSys.getClrntShotSz()); //e.g. CCE s/b 128;
				
				int incr1toOzRatio = 0;
				int incr2toOzRatio = 0;
				int incr3toOzRatio = 0;
				int incr4toOzRatio = 0;
				
				incrCount = clrntIncrs.size();
				if(incrCount==4) {
					incr1toOzRatio = (int) clrntIncrs.get(0).getOzIncrRatio(); //e.g. CCE s/b 1
					incr2toOzRatio = (int) clrntIncrs.get(1).getOzIncrRatio(); //e.g. CCE s/b 32;
					incr3toOzRatio = (int) clrntIncrs.get(2).getOzIncrRatio(); //e.g. CCE s/b 64;
					incr4toOzRatio = (int) clrntIncrs.get(3).getOzIncrRatio(); //e.g. CCE s/b 128;
				} else {
					// must be 3 then, here is how we handle this
					incr1toOzRatio = (int) clrntIncrs.get(0).getOzIncrRatio(); //e.g. 84448 s/b 1
					incr2toOzRatio = (int) clrntIncrs.get(1).getOzIncrRatio(); //e.g. 84448 s/b 48;
					incr3toOzRatio = (int) clrntIncrs.get(2).getOzIncrRatio(); //e.g. 84448 s/b 96;
				}
				
				// calc incr to shots factor
				int incr1ToShots = shotSize/incr1toOzRatio; //e.g. CCE s/b 128
				int incr2ToShots = shotSize/incr2toOzRatio; //e.g. CCE s/b 4
				int incr3ToShots = shotSize/incr3toOzRatio; //e.g. CCE s/b 2
				int incr4ToShots = 0;
				if(incrCount==4) incr4ToShots = shotSize/incr4toOzRatio; //e.g. CCE s/b 1

				int tmpShots;
				for(FormulaIngredient ingredient : ingredients){
					tmpShots = 0;
					tmpShots += (ingredient.getIncrement()[0] * incr1ToShots);
					tmpShots += (ingredient.getIncrement()[1] * incr2ToShots);
					tmpShots += (ingredient.getIncrement()[2] * incr3ToShots);
					if(incrCount==4) tmpShots += (ingredient.getIncrement()[3] * incr4ToShots);
					if(debugon) System.out.println("output is " + tmpShots);
					ingredient.setShots(tmpShots);
					ingredient.setShotSize(shotSize);
				}
				retVal = true;
			}
		} catch (Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			retVal = false;
			SherColorException se = new SherColorException();
			se.setCode(1001);
			se.setMessage(messageSource.getMessage("1001", new Object[] {}, locale ));
			throw se;
		}
		
		return retVal;
	}

	private double calcTotalOuncesInFormula(List<FormulaIngredient> ingredients){
		double totalOunces = 0D;
		try {
			if(ingredients == null || ingredients.size()==0){
				// nothing to do, so totalOunces remains 0
			} else {
				String clrntSysId = ingredients.get(0).getClrntSysId();
				CdsClrntSys clrntSys = clrntSysDao.read(clrntSysId);
				int shotSize = Integer.valueOf(clrntSys.getClrntShotSz()); //e.g. CCE s/b 128;
				
				int totalShots = 0;
				
				for (FormulaIngredient ingredient : ingredients){
					totalShots += ingredient.getShots();
				}
				
				totalOunces = (double)totalShots / (double)shotSize;
			}
		} catch (Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			SherColorException se = new SherColorException();
			se.setCode(1003);
			se.setMessage(messageSource.getMessage("1003", new Object[] {}, locale ));
			throw se;
		}

		
		return totalOunces;
	}
	
	public boolean fillIngredientInfoFromTintSysId(List<FormulaIngredient> ingredients){
		boolean retVal = true;
		
		try {
			CdsClrntSys clrntSys = clrntSysDao.read(ingredients.get(0).getClrntSysId());
			
			HashMap<String,CdsClrnt> clrntMap = clrntDao.mapTintSysIdForClrntSys(ingredients.get(0).getClrntSysId(), true);
			
			for(FormulaIngredient ingredient : ingredients){
				if(clrntMap.containsKey(ingredient.getTintSysId())){
					ingredient.setFbSysId(clrntMap.get(ingredient.getTintSysId()).getFbSysId());
					ingredient.setName(clrntMap.get(ingredient.getTintSysId()).getName());
					ingredient.setEngSysId(clrntMap.get(ingredient.getTintSysId()).getEngSysId());
					ingredient.setOrganicInd(clrntMap.get(ingredient.getTintSysId()).getOrganicInd());
					ingredient.setStatusInd(clrntMap.get(ingredient.getTintSysId()).getStatusInd());
					ingredient.setExcludeProd(clrntMap.get(ingredient.getTintSysId()).getExcludeProd());
					ingredient.setForceProd(clrntMap.get(ingredient.getTintSysId()).getForceProd());
					ingredient.setShotSize(Integer.parseInt(clrntSys.getClrntShotSz()));
				} else {
					retVal = false;
				}
			}
		} catch(Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			throw e;
		}
		return retVal;
	}

	@Override
	public List<SwMessage> validateFormulation(FormulaInfo formula) {
		List<SwMessage> retList = new ArrayList<SwMessage>();
		List<SwMessage> fadeCheckList;
		List<SwMessage> primerCheckList;
		List<SwMessage> afcdWarningCheckList;
		try {
			fadeCheckList = fadeCheck(formula, false);
			//add the list entries from the fade check list to the return list.
			for (SwMessage item:fadeCheckList) {
				retList.add(item);
			}
			primerCheckList = primerCheck(formula);
			//add the list entries from the fade check list to the return list.
			for (SwMessage item:primerCheckList) {
				retList.add(item);
			}
			
			afcdWarningCheckList = afcdWarningCheck(formula);
			//add the list entries from the fade check list to the return list.
			for (SwMessage item:afcdWarningCheckList) {
				retList.add(item);
			}
			
			
		} catch (Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			throw e;
		}
		return retList;
	}
	
	
	public List<SwMessage> canLabelFormulationWarnings(FormulaInfo formula) {
		if(debugon) System.out.println("top of canLabelFormulationWarnings");
		List<SwMessage> retList = new ArrayList<SwMessage>();
		try {
			if(debugon) System.out.println("calling fadeCheck");
			List<SwMessage> fadeCheckList = fadeCheck(formula, true);
			//add the list entries from the fade check list to the return list.
			if(fadeCheckList!=null){
				for (SwMessage item:fadeCheckList) {
					retList.add(item);
				}
			}
			
			if(debugon) System.out.println("calling canLabelPrimerCheck");
			List<SwMessage> primerCheckList = canLabelPrimerCheck(formula);
			if(primerCheckList!=null){
				//add the list entries from the fade check list to the return list.
				for (SwMessage item:primerCheckList) {
					retList.add(item);
				}
			}

			if(debugon) System.out.println("returning from canLabeFormulationWarning");
			
		} catch (Exception e) {
			logger.error(e.toString() + " " + e.getMessage() + " " + e.getStackTrace());
			throw e;
		}
		
		return retList;
	}
	
	public List<SwMessage> manualFormulationWarnings(FormulaInfo formula) {
		List<SwMessage> retList = new ArrayList<SwMessage>();
		try {
			List<SwMessage> fadeCheckList = fadeCheck(formula, false);
			//add the list entries from the fade check list to the return list.
			for (SwMessage item:fadeCheckList) {
				retList.add(item);
			}
			List<SwMessage> fillLevelCheckList = fillLevelCheck(formula);
			//add the list entries from the fade check list to the return list.
			for (SwMessage item:fillLevelCheckList) {
				retList.add(item);
			}
			
			
		} catch (Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			throw e;
		}
		
		return retList;
	}
	
	private List<SwMessage> primerCheck(FormulaInfo formula) {
		List<SwMessage> retList = new ArrayList<SwMessage>();
		
		try {
			CdsMiscCodes twoCoatWarn = cdsMiscCodesDao.read("WARN", "2COAT-CR2");
			double dCr2Tol = 0.0;
			SwMessage thisMsg;
			String primerId = "";
			CdsColorMast thisColorMast;
			
			int bacPrimerTtlAmt = 0;
		

			//First look for the two coat warning in misc-codes and check for the 2 coat warning.
			if (twoCoatWarn!=null) {
				try {
					dCr2Tol = Double.parseDouble(twoCoatWarn.getMiscName());
				} catch (NullPointerException npe) {
					dCr2Tol = 0.0;
				} catch (NumberFormatException nfe) {
					dCr2Tol = 0.0;
				}
			}
			
			//now check the cr2 value of the formula and see if it is greater than 0 and less than the dCr2Tol.
			//If so, add the "2 coats" error message.
			if(formula.getContrastRatioThin() > 0.0 && formula.getContrastRatioThin() < dCr2Tol) {
				thisMsg = new SwMessage();
				thisMsg.setCode("TWOCOATS");
				thisMsg.setMessage("2 Coats of Paint are REQUIRED to achieve proper hide for this color.");
				thisMsg.setSeverity(Level.WARN);
				retList.add(thisMsg);
			} else {
				//check the cds-color-mast entry for the color and see if it needs a primer warning.
				thisColorMast = colorMastDao.read(formula.getColorComp(), formula.getColorId());
				if (thisColorMast!=null) {
					
					primerId = thisColorMast.getPrimerId();
					if (primerId!=null) {
						primerId=primerId.toUpperCase();
						//let's add the dash for the display.
						primerId = primerId.substring(0, 1) + "-" + primerId.substring(1);
						if (primerId.startsWith("P")) {
							if (formula.getContrastRatioThin() > 0.0) {
								//the BAC primer colorant check was defined in sys-ctl, and since this table is not 
								//ported to Oracle, this stuff was hardcoded for the moment.
								if (formula.getClrntSysId().equalsIgnoreCase("BAC")) {
									for (FormulaIngredient item:formula.getIngredients()) {
										if (item.getTintSysId().equalsIgnoreCase("Y1") ||
												item.getTintSysId().equalsIgnoreCase("L1") ||
												item.getTintSysId().equalsIgnoreCase("R3") ||
												item.getTintSysId().equalsIgnoreCase("R4") ) {
											bacPrimerTtlAmt = bacPrimerTtlAmt + item.getShots();
										}
										
									}
									if (bacPrimerTtlAmt > 512) {
										thisMsg = new SwMessage();
										thisMsg.setCode("BACPRIMER");
										thisMsg.setMessage("To get the accurate color match and acceptable hiding, you must use the " + primerId + " Shade Primer");
										thisMsg.setSeverity(Level.WARN);
										retList.add(thisMsg);
										return retList;
									}
								}
							}
							//At this point, we've dealt with it if it's BAC, so now just check if there's something there.
							thisMsg = new SwMessage();
							thisMsg.setCode("PRIMER");
							thisMsg.setMessage("The formula for this color is based on applying it over the recommended " + primerId + " Shade Primer");
							thisMsg.setSeverity(Level.WARN);
							retList.add(thisMsg);
	
						}
					}
				}
				//if it is null, we want to just do nothing and return.
			}
			
		} catch (Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			throw e;
		}
		return retList;

	}

	private List<SwMessage> fadeCheck(FormulaInfo formula, boolean canLabel) {
		List<SwMessage> retList = new ArrayList<SwMessage>();
		String intExt;
		SwMessage thisMsg;
		CdsProd thisCdsProd;
		boolean comexAlkaliWarn = false;
		boolean comexIntOnlyWarn = false;
		boolean columbiaIntOnlyWarn = false;

		
		try {
			//get the int/ext from cdsProd.  It's possible the fbBase on the formula refers to the base 
			//used for calculation of formula, rather than the product (this happens for products not on 
			//CdsProd).
			thisCdsProd = cdsProdDao.read(formula.getSalesNbr());
			if (thisCdsProd!=null) {
				intExt = thisCdsProd.getIntExt();
			} else {
				intExt = "";
			}
		
			//walk the colorant list.
			for (FormulaIngredient item:formula.getIngredients()) {

				//check for Comex settings.
				if (item.getClrntSysId().equalsIgnoreCase("808") || 
						item.getClrntSysId().equalsIgnoreCase("844") ||
						item.getClrntSysId().equalsIgnoreCase("896") ||
						item.getClrntSysId().equalsIgnoreCase("897") ||
						item.getClrntSysId().equalsIgnoreCase("888")) {
					
					//check for alkali warning.
					if (item.getTintSysId().equalsIgnoreCase("V")) {
						if(canLabel){
							comexAlkaliWarn = true;
						} else {
							thisMsg = new SwMessage();
							thisMsg.setCode("COMEXALKALI");
							thisMsg.setMessage("Formulas containing colorant V are Alkali Sensitive");
							thisMsg.setSeverity(Level.WARN);
							retList.add(thisMsg);
						}
					}
					
					//check for exterior warning
					if (!intExt.equalsIgnoreCase("INTERIOR")) {
						if ((item.getTintSysId().equalsIgnoreCase("AX") ||
								item.getTintSysId().equalsIgnoreCase("RN") ||
								item.getTintSysId().equalsIgnoreCase("R") ||
								item.getTintSysId().equalsIgnoreCase("T")) &&
								item.getOrganicInd()!=null &&
								item.getOrganicInd().equalsIgnoreCase("OR")) {
							if(canLabel){
								comexIntOnlyWarn = true;
							} else {
								thisMsg = new SwMessage();
								thisMsg.setCode("COMEXEXT");
								thisMsg.setMessage("Formulas containing colorant " + item.getTintSysId() + " use for INTERIOR ONLY");
								thisMsg.setSeverity(Level.WARN);
								retList.add(thisMsg);
							}
						}
					}
				} else {
					//check for non-comex messages 
					//If it's non-comex and interior, just return.
					if (intExt.equalsIgnoreCase("INTERIOR")) {
						return retList;
					}
					
					//Check for Columbia specific 
					if (item.getClrntSysId().equalsIgnoreCase("CLB")) {
						if ((item.getTintSysId().equalsIgnoreCase("AX") ||
								item.getTintSysId().equalsIgnoreCase("T")))  {
							if(canLabel){
								columbiaIntOnlyWarn = true;
							} else {
								thisMsg = new SwMessage();
								thisMsg.setCode("CLBEXT");
								thisMsg.setMessage("Formulas containing " + item.getTintSysId() + "-" + item.getName() + " colorant will fade in direct sunlight");
								thisMsg.setSeverity(Level.WARN);
								retList.add(thisMsg);
							}
						}
					} else {	
						if (item.getOrganicInd()!=null && item.getOrganicInd().equalsIgnoreCase("OR")) {
							if(!canLabel){ // don't show on can label
								//Non Columbia specific
								thisMsg = new SwMessage();
								thisMsg.setCode("EXT");
								thisMsg.setMessage("Formulas containing " + item.getTintSysId() + "-" + item.getName() + " colorant will fade in direct sunlight");
								thisMsg.setSeverity(Level.WARN);
								retList.add(thisMsg);
							}
						}
					}
				}
			} // end looping through colorants

			if(canLabel){
				// summarize warnings for can labels
				if(comexIntOnlyWarn) {
					SwMessage message = new SwMessage();
					message.setSeverity(Level.WARN);
					message.setCode("1104");
					message.setMessage(messageSource.getMessage("1104", new Object[] {}, locale ));
					retList.add(message);
				}
				if(comexAlkaliWarn){
					SwMessage message = new SwMessage();
					message.setSeverity(Level.WARN);
					message.setCode("1105");
					message.setMessage(messageSource.getMessage("1105", new Object[] {}, locale ));
					retList.add(message);
				}
				if(columbiaIntOnlyWarn){
					SwMessage messageA = new SwMessage();
					messageA.setSeverity(Level.WARN);
					messageA.setCode("1106");
					messageA.setMessage(messageSource.getMessage("1106", new Object[] {}, locale ));
					retList.add(messageA);
					SwMessage messageB = new SwMessage();
					messageB.setSeverity(Level.WARN);
					messageB.setCode("1107");
					messageB.setMessage(messageSource.getMessage("1107", new Object[] {}, locale ));
					retList.add(messageB);
				}
			} // end adding can label summary warnings

	
		} catch (Exception e) {
			logger.error(e.toString() + " " + e.getMessage() + " " + e.getStackTrace());
			throw e;
		}
		return retList;
	}
	
	private List<SwMessage> afcdWarningCheck(FormulaInfo formula){
		List<SwMessage> retList = new ArrayList<SwMessage>();
		CdsFormulaChgList cdsFormulaChgListRec;
		Date today = new Date();
		SwMessage thisMsg;
		
		try {
			// check for warnings for Product and Color
			cdsFormulaChgListRec = cdsFormulaChgListDao.read(formula.getColorComp(), formula.getColorId(), formula.getSalesNbr(),formula.getClrntSysId());
			if (cdsFormulaChgListRec != null) {
				if (cdsFormulaChgListRec.getTypeCode().equalsIgnoreCase("W")
						&& (cdsFormulaChgListRec.getEffDate().before(today) || cdsFormulaChgListRec.getEffDate().equals(today))
						&& (cdsFormulaChgListRec.getExpDate().after(today) || cdsFormulaChgListRec.getExpDate().equals(today))) {
					thisMsg = new SwMessage();
					thisMsg.setCode("AFCD");
					thisMsg.setMessage("A formula change has occurred for this color.  If this is an ongoing project " +
			                  "use Copy Color to access previous formula.  If this is a new project proceed " +
			                  "with this new color formula.");
					thisMsg.setSeverity(Level.WARN);
					retList.add(thisMsg);
				}
			}
	
			
		}  catch (SherColorException e) {
			throw e;	
		}  catch (Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			throw e;		
		}
		return retList;
	}

	private List<SwMessage> canLabelPrimerCheck(FormulaInfo formula){
		List<SwMessage> messageList = new ArrayList<SwMessage>();
		
		try {
			CdsColorMast cdsColorMast = colorMastDao.read(formula.getColorComp(), formula.getColorId());
			if(cdsColorMast!=null) {
				if(cdsColorMast.getPrimerId()!=null && cdsColorMast.getPrimerId().startsWith("P")){
					SwMessage message = new SwMessage();
					message.setSeverity(Level.WARN);
					message.setCode("1101");
					message.setMessage(messageSource.getMessage("1101", new Object[] {cdsColorMast.getPrimerId()}, locale ));
					messageList.add(message);
				} else {
					if(cdsColorMast.getPrimerId()!=null && cdsColorMast.getPrimerId().startsWith("M")){
						SwMessage message = new SwMessage();
						message.setSeverity(Level.WARN);
						message.setCode("1102");
						message.setMessage(messageSource.getMessage("1102", new Object[] {cdsColorMast.getPrimerId()}, locale ));
						messageList.add(message);
					} else {
						if(cdsColorMast.getColorComp().equalsIgnoreCase("MAB") 
						   && cdsColorMast.getPalette().equalsIgnoreCase("COLOR QUEST")
						   && cdsColorMast.getColorName().contains("#")){
							SwMessage message = new SwMessage();
							message.setSeverity(Level.WARN);
							message.setCode("1103");
							message.setMessage(messageSource.getMessage("1103", new Object[] {}, locale ));
							messageList.add(message);
						}
					}
				}
			}
		} catch (SherColorException e) {
			throw e;	
		} catch (Exception e) {
			logger.error(e.toString() + " " + e.getMessage() + " " + e.getStackTrace());
			throw e;		
		}
		return messageList;
	}
	
	public List<SwMessage> fillLevelCheck(FormulaInfo formula){
		List<SwMessage> messageList = new ArrayList<SwMessage>();
		double formulaOz = 0D;
		
		try {
			ProductFillInfo productFillInfo = productService.getProductFillInfo(formula.getSalesNbr(), formula.getClrntSysId());
			
			// how much colorant in formula (converted to oz)
			formulaOz = calcTotalOuncesInFormula(formula.getIngredients());

			if(debugon) System.out.println("Fill Level Check: FormOz=" + formulaOz + " prodMaxLoad=" + productFillInfo.getProductMaxLoad() + " prodMaxOverLoad=" + productFillInfo.getProductMaxOverLoad());
			// if productMaxOverLoad is set, use this number for the check, they cannot go over this number
			if(productFillInfo.getProductMaxOverLoad()>0D){
				if(formulaOz>productFillInfo.getProductMaxOverLoad()){
					// ACTUAL cannot be overidden
					SwMessage message = new SwMessage();
					message.setSeverity(Level.ERROR);
					message.setCode("1153");
					message.setMessage(messageSource.getMessage("1153", new Object[] {formulaOz, productFillInfo.getProductMaxOverLoad()}, locale ));
					messageList.add(message);
					if(debugon) System.out.println("adding message" + message.getSeverity() + " " + message.getMessage());
				}
			}
			if(messageList.size()==0){
				// productMaxOverLoad is not set, use product max load and warn them when they go over
				if(productFillInfo.getProductMaxLoad() > 0D){
					if(formulaOz>productFillInfo.getProductMaxLoad()){
						// ACTUAL can be overridden
						SwMessage message = new SwMessage();
						message.setSeverity(Level.WARN);
						message.setCode("1151");
						message.setMessage(messageSource.getMessage("1151", new Object[] {formulaOz, productFillInfo.getProductMaxLoad()}, locale ));
						messageList.add(message);
						if(debugon) System.out.println("adding message" + message.getSeverity() + " " + message.getMessage());
					}
				} else {
					// productMaxLoad is not set, use an estimated max load and warn them when they go over
					if(productFillInfo.getEstimateMaxLoad()>0D && formulaOz > productFillInfo.getEstimateMaxLoad()){
						// ESTIMATE can be overridden
						SwMessage message = new SwMessage();
						message.setSeverity(Level.WARN);
						message.setCode("1152");
						message.setMessage(messageSource.getMessage("1152", new Object[] {formulaOz, productFillInfo.getEstimateMaxLoad()}, locale ));
						messageList.add(message);
						if(debugon) System.out.println("adding message" + message.getSeverity() + " " + message.getMessage());
					}
				}
			}
			
		} catch (SherColorException e) {
			throw e;	
		} catch (Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			throw e;		
		}
		return messageList;
		
	}
	
	public Double[] projectCurve(FormulaInfo formInfo, CustWebParms custWebParms){
		Double[] retVal = null;
		
		// building this first
		// "cdsColorantTbl": [{
        // 		"clrnt-tint-sys-id": ["CCE", "B1", "R2", "Y3", "", "", "", "", ""],
        // 		"clrnt-shots": [0, 18, 1, 8, 0, 0, 0, 0, 0]
        //  }],
		OeFormInputColorants currentFormula = new OeFormInputColorants();
		// first entry in list is for colorant system with shots set to zero
		currentFormula.getTintSysIds().add(formInfo.getClrntSysId());
		currentFormula.getShots().add(0);
		
		for(FormulaIngredient thisIngredient : formInfo.getIngredients()){
			currentFormula.getTintSysIds().add(thisIngredient.getTintSysId());
			currentFormula.getShots().add(thisIngredient.getShots());
		}
		ArrayList<OeFormInputColorants> cdsColorantTbl = new ArrayList<OeFormInputColorants>();
		cdsColorantTbl.add(currentFormula);
		
		// wrap up into formula request for OE
		OeFormInputRequest serviceReq = new OeFormInputRequest();
		serviceReq.setColorComp(formInfo.getColorComp());
		serviceReq.setColorId(formInfo.getColorId());
		serviceReq.setSalesNbr(formInfo.getSalesNbr());
		serviceReq.setClrntSysId(formInfo.getClrntSysId());
		serviceReq.setProjection(true);
	
		OeFormInput oeInput = new OeFormInput();

		OeFormInputDataSet oeInputDataSet = new OeFormInputDataSet();

		ArrayList<OeFormInputRequest> serviceReqList = new ArrayList<OeFormInputRequest>();
		serviceReqList.add(serviceReq);
		oeInputDataSet.setFormInputRequest(serviceReqList);

		oeInputDataSet.setCurrentIngredients(cdsColorantTbl);
		
		if(custWebParms!=null){
			ArrayList<OeFormInputParms> oeParms = new ArrayList<OeFormInputParms>();
			oeParms.add(mapCustWebParmsToOeInputParms(custWebParms));
			oeInputDataSet.setFormInputParms(oeParms);
		}
		
		oeInput.setFormulationIn(oeInputDataSet);

		OeServiceProdDataSet dsProd = new OeServiceProdDataSet();
		OeServiceColorDataSet dsColor = new OeServiceColorDataSet();
		OeServiceColorantDataSet dsColorantSys = new OeServiceColorantDataSet();

		// call OE formulation
		OeFormResp oeFormResp = null;
		
		//String oeBaseUrl = "http://stscsb01.sherwin.com:8080/rest/SherColorService/formulationReq";
		URL initUrl;
		HttpURLConnection conn = null;

		try {
			initUrl = new URL(oeBaseUrl.toString()+"/formulationReq");
			conn = (HttpURLConnection) initUrl.openConnection();
			//do as post... 
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept","application/json");

			if(debugon) System.out.println("using oe server " + oeBaseUrl.toString()+"/formulationReq");
			// NEW for Scenario Formulation which affects all formulation..
			if(dsProd==null) dsProd = new OeServiceProdDataSet();
			if(dsColor==null) dsColor = new OeServiceColorDataSet();
			// Double wrap dsProd and dsColor for Rod
			OeServiceProdDataSetWrapper oeProdWrapper = new OeServiceProdDataSetWrapper();
			oeProdWrapper.setOeProdDataSet(dsProd);
			OeServiceColorDataSetWrapper oeColorWrapper = new OeServiceColorDataSetWrapper();
			oeColorWrapper.setOeColorDataSet(dsColor);
			OeServiceColorantDataSetWrapper oeColorantWrapper = new OeServiceColorantDataSetWrapper();
			oeColorantWrapper.setOeColorantDataSet(dsColorantSys);
			
			OeServiceScenarioFormulation oeScenarioFormulation = new OeServiceScenarioFormulation();
			oeScenarioFormulation.setOeFormInput(oeInput);
			oeScenarioFormulation.setDsProdWrapper(oeProdWrapper);
			oeScenarioFormulation.setDsColorWrapper(oeColorWrapper);
			oeScenarioFormulation.setDsColorantWrapper(oeColorantWrapper);
			
			OeServiceRequestWrapper request = new OeServiceRequestWrapper();
			request.setOeServiceScenarioFormulation(oeScenarioFormulation);
			
			//Gson gson = new Gson();
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
			String inputBody = gson.toJson(request);
			
			if(debugon) System.out.println(inputBody);
			
			OutputStream os = conn.getOutputStream();
			os.write(inputBody.getBytes());
			os.flush();
			
			int responseCode = conn.getResponseCode();
			if(debugon) System.out.println("responsecode is " + responseCode);
			
			if(responseCode==200 || responseCode==201){
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = br.readLine()) != null){
					response.append(inputLine);
				}
				
				if(debugon) System.out.println("json response from create= " + response.toString());
				GsonBuilder builder = new GsonBuilder();
				//Gson gson = builder.create();
				gson = builder.setDateFormat("yyyy-MM-dd").create();
				oeFormResp = gson.fromJson(response.toString(), OeFormResp.class);
				//BKP 6-12-2017 Added close of buffered reader based on Veracode scan
				if (br != null)
					br.close();
				retVal = oeFormResp.getFormulationDataSet().getFormRespClrntRow().get(0).getProjCurve(); 
			} else {
				oeFormResp = buildOeErrorResponse("ERROR", Integer.toString(responseCode), "BAD HTTP RESPONSE");
			}
			
			/* json response should be something like
		 * {"dsFormulation":{"cdsColorantTbl":[{"scRequestID":"w2764Z+VZqwXFEbO2J+qxQ",
			 *                                      "FormulaSource":"SHER-COLOR FORMULA",
			 *                                      "clrnt-tint-sys-id":["CCE","R3","Y3","","","","","",""],
			 *                                      "clrnt-nm":["Color Cast","Magenta","Deep Gold","","","","","",""],
			 *                                      "clrnt-shots":[0,1,12,0,0,0,0,0,0],
			 *                                      "clrnt-incr1":["OZ","-","-","","","","","",""],
			 *                                      "clrnt-incr2":["32","-","3","","","","","",""],
			 *                                      "clrnt-incr3":["64","-","-","","","","","",""],
			 *                                      "clrnt-incr4":["128","1","-","","","","","",""],
			 *                                      "clrnt-qual":["","128","128","128","128","128","","",""],
			 *                                      "clrnt-status-ind":["","","","","","","","",""],
			 *                                      "formula-exists":false,
			 *                                      "func-status":"Formulation",
			 *                                      "clrnt-code":["","A60R3","A60Y3","","","","","",""],
			 *                                      "proj-curve":[0.0,0.0,0.0,0.0,43.81288588,61.70915961,69.58636642,72.18809724,74.47647452,75.85933805,76.48068666,76.82107687,77.13646293,77.43864655,78.77060771,80.07381558,81.33333921,82.38351345,83.71261954,84.57371593,84.98997092,85.2851212,85.66749096,85.95892191,85.74874997,85.41392684,85.26083827,85.19907594,85.2439642,85.55861712,86.01545095,86.15936637,86.02883816,85.67422628,85.09408236,0.0,0.0,0.0,0.0,0.0],
			 *                                      "formulation-time":325.0,
			 *                                      "formula-cost":0.0144336524,
			 *                                      "formula-spd":0.0371129555,
			 *                                      "formula-illum":["D65","A","F2"],
			 *                                      "formula-deltae":[0.2518051655,0.2414650732,0.2390319262],
			 *                                      "formula-avgdeltae":0.0,
			 *                                      "formula-metidx":0.0375673287,
			 *                                      "formula-deltae2":0.2217015561,
			 *                                      "formula-oddeltae1":0.8270003815,
			 *                                      "formula-oddeltae2":3.8482965711,
			 *                                      "formula-cr1":99.2051310208,
			 *                                      "formula-cr2":94.0466623293,
			 *                                      "formula-rule":"1",
			 *                                      "formula-prod-nbr":"",
			 *                                      "formula-proc-order":0,
			 *                                      "formula-prod-rev":"",
			 *                                      "formula-clrnt-list":"",
			 *                                      "formula-de-comment":"dE Rank=2,best de=.1253792449,forms lt35=2",
			 *                                      "formula-color-eng-ver":"2DK",
			 *                                      "formula-pass-nbr":3,
			 *                                      "FormulationWarning":"",
			 *                                      "dbRowiD":null,
			 *                                      "deltaeWarning":""}]}}
			 */

		} catch (MalformedURLException e) {
			//e.printStackTrace();
			logger.error(e.toString() + " " + e.getMessage());
			//oeFormResp = buildOeErrorResponse("ERROR", "999", "MalformedURLException");
			retVal = null;
		} catch (ConnectException e) {
			//e.printStackTrace();
			logger.error(e.toString() + " " + e.getMessage());
			//oeFormResp = buildOeErrorResponse("ERROR", "999", "ConnectException");
			retVal = null;
		} catch (IOException e) {
			//e.printStackTrace();
			logger.error(e.toString() + " " + e.getMessage());
			//oeFormResp = buildOeErrorResponse("ERROR", "999", "IOException");
			retVal = null;
		} finally {
			if(conn!=null) conn.disconnect();
//			if(oeFormResp==null){
//				oeFormResp = new OeFormResp();
//				oeFormResp = buildOeErrorResponse("ERROR", "999", "Unknown Error");
//			}
		}
		
		return retVal;
	}
	
	public String getOeBaseUrl() {
		return oeBaseUrl;
	}

	public void setOeBaseUrl(String oeBaseUrl) {
		this.oeBaseUrl = oeBaseUrl;
	}
	
}

