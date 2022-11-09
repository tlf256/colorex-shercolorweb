package com.sherwin.shercolor.customershercolorweb.web.action;

import java.io.FileInputStream;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.owasp.encoder.Encode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CustWebCustomerProfile;
import com.sherwin.shercolor.common.domain.CustWebDevices;
import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.common.service.EulaService;
import com.sherwin.shercolor.common.service.TinterService;
import com.sherwin.shercolor.common.service.TranHistoryService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.SpectroInfo;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterInfo;

import org.springframework.core.env.Environment;


public class LoginAction extends ActionSupport  implements SessionAware, LoginRequired {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient Map<String, Object> sessionMap;
	private transient CustomerService customerService;
	private transient EulaService eulaService;

	@Autowired
	private transient Environment environment;

	@Autowired
	private transient TinterService tinterService;
	
	@Autowired
	private transient TranHistoryService tranHistoryService;
	
	static Logger logger = LogManager.getLogger(LoginAction.class);
	private transient RequestObject reqObj;
	private String reqGuid;
	private String guid1;
	private String acct;
	private String first;
	private String last;
	private String id;
	private String token;
	private boolean newSession;
	private boolean siteHasTinter;
	private boolean sessionHasTinter;
	private boolean siteHasPrinter;
	private transient TinterInfo tinter;
	private boolean siteHasSpectro;
	private transient SpectroInfo spectro;
	private boolean reReadLocalHostTinter;
	private String sherLinkURL;
	private String loMessage;
	private int tintQueueCount;
	private String customerType;
	private int daysUntilPwdExp;

	
	public String execute() {
		String custName = "";
		String returnStatus = "SUCCESS";
		Properties prop = new Properties();
		Properties propTest = new Properties();
		String dbEnv = "";
		sherLinkURL = "";
		String sherLinkTokenWSURL = "";
		String testMode = "";
		String testModeFirst = "";
		String testModeLast = "";
		String testModeAcct = "";
		customerType = "";
		newSession = true;
		siteHasTinter = false;
	
		siteHasPrinter = false;
		sessionHasTinter = false;
		siteHasSpectro = false;
		
		String userId = "";
		
		try {
			// Get the testmode.properties file data first.  If the file is not present we are not in test mode.
			try {
				logger.debug("ready to load testmode");
				propTest.load(new FileInputStream("/web_apps/server/shercolor/deploy/customershercolorwebtestmode.properties"));
				//20170829 - BKP - adding a test mode toggle.
				logger.debug("loaded testmode");
				testMode = propTest.getProperty("testMode");
				logger.debug("got testmode property it is " + testMode);
				testModeFirst = propTest.getProperty("testModeFirst");
				testModeLast = propTest.getProperty("testModeLast");
				testModeAcct = propTest.getProperty("testModeAcct");
			} catch (IOException ioe) {
				testMode = "";
			}
			// Get the customershercolorweb.properties file data next.
			if (ArrayUtils.contains(environment.getActiveProfiles(),"test"))
				prop.load(this.getClass().getResourceAsStream("/customershercolorweb.properties"));
			else
				prop.load(Files.newInputStream(Paths.get("/web_apps/server/shercolor/deploy/customershercolorweb.properties")));

			dbEnv = prop.getProperty("dbEnv");
			sherLinkURL = prop.getProperty("sherLinkLoginUrl." + dbEnv);
			sherLinkTokenWSURL = prop.getProperty("sherLinkTokenSWUrl." + dbEnv);
						
			//logger.info("sherLinkLoginUrl = " + sherLinkURL + " and dbEnv=" + dbEnv);
			logger.debug("got other properties");
			logger.debug(Encode.forJava("sherLinkTokenWSURL is " + sherLinkTokenWSURL));
			logger.debug(Encode.forJava("sherLinkURL is " + sherLinkURL));

			logger.info(Encode.forJava("DEBUG reqGuid="+reqGuid));
			 if (reqGuid==null || reqGuid.isEmpty()) {
				 // we've never set anything.  Check the account and if it's not empty, try using it for a login.
				 //System.setProperty("jsse.enableSNIExtension", "false");
				 //call the Sher-link validation service to confirm we have a valid token before logging in.
				 logger.debug("ready to validate token, testmode = " + testMode);
				 //BKP 2018-02-26 - no longer validating token, if here, we should be okay.
				 //if (!testMode.equals("inTesting") && !validateToken(id,token,sherLinkTokenWSURL)  ) {
				 if (!testMode.equals("inTesting") && (guid1==null | guid1.isEmpty())) {
//					 //invalid login due to guid talking to a session object is empty or not set.
					 //we should never get here, but just in case...
					 logger.error(Encode.forJava("Invalid Login - Token authentication failure - guid1 = " + guid1 + "  testMode is " + testMode));
					 returnStatus = "NONE";
					 
				 } else {
					logger.info(Encode.forJava("DEBUG reqGuid not empty " + reqGuid));
					logger.debug(Encode.forJava("Guid1 = " + guid1));
					RequestObject loginReqObj = (RequestObject) sessionMap.get(guid1);
					if (loginReqObj==null) {
						 logger.info("DEBUG loginReqObj is null - probably a session timeout");
						 loMessage = getText("global.yourSessionHasExpired");
						 return LOGIN;
					 }
					acct = loginReqObj.getCustomerID();
					first = loginReqObj.getFirstName();
					last = loginReqObj.getLastName();
					//01-14-2019*BKP*get the user login from guid1 and post it in the RequestObject for the reqGuid.
					//               this will allow for inside the application password changes.
					userId = loginReqObj.getUserId();
					//03-01-2019*BKP*pass through the "days until password expires field as well.
					daysUntilPwdExp = loginReqObj.getDaysUntilPasswdExpire();
					
					logger.debug("successfully validated");
					if (acct==null || acct.isEmpty()) {
						 //It's empty, this may be for testing.  Use CCF for the moment.
						 //commenting this out for production
						 //20170829 - BKP - adding a test mode toggle.
						 if (testMode.equals("inTesting")) {
							 if (testModeAcct!=null &&  !testModeAcct.isEmpty()) acct = Encode.forHtml(testModeAcct);
							 else acct = "CCF";
							 //hard coding these values for local validation
							 if (testModeFirst!=null &&  !testModeFirst.isEmpty()) first = Encode.forHtml(testModeFirst);
							 else first = "John";
							 if (testModeLast!=null &&  !testModeLast.isEmpty()) last = Encode.forHtml(testModeLast);
							 else last = "Doe";
						 }
					 }
					 else{
							logger.info(Encode.forJava("DEBUG acct not empty "+ Encode.forHtml(acct)));
					 }
	
					 //Read the Customer table and get the ID.
					 String theCustWebParmsKey = GetCustWebParmsKey(loginReqObj);
					 custName = customerService.getDefaultCustomerTitle(theCustWebParmsKey).trim();
					 if (!custName.isEmpty()){
							// add customerID and custName to the session
							reqObj = new RequestObject();
							reqGuid = UUID.randomUUID().toString().replace("-", "");
							reqObj.setGuid(reqGuid);
							reqObj.setCustomerID(Encode.forHtml(theCustWebParmsKey));
							reqObj.setFirstName(Encode.forHtml(first));
							reqObj.setLastName(Encode.forHtml(last));
							reqObj.setCustomerName(custName);
							reqObj.setSherLinkURL(sherLinkURL);
							reqObj.setUserId(userId);
							reqObj.setDaysUntilPasswdExpire(daysUntilPwdExp);
							reqObj.setTintQueueCount(tranHistoryService.getActiveCustomerTintQueue(reqObj.getCustomerID(), false).size());
							
							logger.debug(Encode.forJava("DEBUG new reqGuid created "+ reqGuid));
							List<CustWebDevices> spectroList = customerService.getCustSpectros(Encode.forHtml(reqObj.getCustomerID()));
							spectro = new SpectroInfo();
							
							//Set the CustomerType in order to enable or disable functionality later in the code based on the type of store
							//For example, self tinting customers do not need National Account functionality enabled
							CustWebCustomerProfile profile = customerService.getCustWebCustomerProfile(reqObj.getCustomerID());
							if (profile != null) {
								String custType = profile.getCustomerType();
								
								if (custType != null && (custType.trim().toUpperCase().equals("DRAWDOWN") || custType.trim().toUpperCase().equals("STORE"))){
									customerType = custType.trim().toUpperCase();
									if (customerType == null) {
										customerType = "";
									}
								}
							}
							reqObj.setCustomerType(customerType);

							if (spectroList.size()==1) {
								reqObj.setSpectroModel(spectroList.get(0).getDeviceModel());
								reqObj.setSpectroSerialNbr(spectroList.get(0).getSerialNbr());
								spectro.setModel(spectroList.get(0).getDeviceModel());
								spectro.setSerialNbr(spectroList.get(0).getSerialNbr());
								siteHasSpectro = true;
							} else {
								if (spectroList.size()==0) {
									reqObj.setSpectroModel("");
									reqObj.setSpectroSerialNbr("");
									spectro.setModel("");
									spectro.setSerialNbr("");
								} else {
									//Multiple spectros.  Handle this better.
									reqObj.setSpectroModel(spectroList.get(0).getDeviceModel());
									reqObj.setSpectroSerialNbr(spectroList.get(0).getSerialNbr());
									spectro.setModel(spectroList.get(0).getDeviceModel());
									spectro.setSerialNbr(spectroList.get(0).getSerialNbr());
									siteHasSpectro = true;
								}
							}
							reqObj.setSpectro(spectro);
							
							newSession = true; // this will trigger the welcome page to do "on login only" activities... (e.g. read config for tinter from device handler)
							tinter = new TinterInfo();
							reqObj.setTinter(tinter);
							List<String> tinterList = tinterService.listOfModelsForCustomerId(Encode.forHtml(reqObj.getCustomerID()), null);
							if(tinterList.size()>0) siteHasTinter = true;
							setIsPrinterConfigured();
						 	sessionMap.put(reqObj.getGuid(), reqObj);
						 	returnStatus = "SUCCESS";
					 } else {
						 //invalid login.  Log the data passed to us from the external server
						 logger.error(Encode.forJava("Invalid Login - id=" + id + " first=" + Encode.forHtml(first) + " last=" + Encode.forHtml(last) + " acct=" + Encode.forHtml(acct) + " token=" + token));
						 logger.error("Probably missing CustWebParms entry for the login...");
						 returnStatus = "NONE";
					 }
				 }

			 } else {
				 // reset the existing request object.
				 logger.info(Encode.forJava("DEBUG ready to reset request object using reqGuid"+ reqGuid));
				 RequestObject origReqObj = (RequestObject) sessionMap.get(reqGuid);
				 if (origReqObj==null) {
					 logger.info("DEBUG origReqObj is null - probably a session timeout");
					 loMessage = getText("global.yourSessionHasExpired");
					 return LOGIN;
				 }
				 acct = origReqObj.getCustomerID();
				 first = origReqObj.getFirstName();
				 last = origReqObj.getLastName();
				 userId = origReqObj.getUserId();
				 daysUntilPwdExp = origReqObj.getDaysUntilPasswdExpire();
				 sherLinkURL = origReqObj.getSherLinkURL();
				 custName = origReqObj.getCustomerName();
				 customerType = origReqObj.getCustomerType();
				 origReqObj.reset();
				 origReqObj.setCustomerID(acct);
				 origReqObj.setCustomerName(custName);
				 origReqObj.setGuid(reqGuid);
				 origReqObj.setFirstName(first);
				 origReqObj.setLastName(last);
				 origReqObj.setSherLinkURL(sherLinkURL);
				 origReqObj.setUserId(userId);
				 origReqObj.setDaysUntilPasswdExpire(daysUntilPwdExp);
				 origReqObj.setTintQueueCount(tranHistoryService.getActiveCustomerTintQueue(acct,false).size());
				 origReqObj.setCustomerType(customerType);
				 newSession = false;
				 tinter=origReqObj.getTinter();
				 if(origReqObj.getTinter()!=null && origReqObj.getTinter().getModel()!=null && !origReqObj.getTinter().getModel().isEmpty()){
					 siteHasTinter = true;
					 sessionHasTinter = true;
				 }
				 spectro=origReqObj.getSpectro();
				 //BKP 07/22/2019 Modified to match getTinter if logic above to rectify Veracode eror
				 if(origReqObj.getSpectro()!=null && origReqObj.getSpectro().getModel()!=null && origReqObj.getSpectro().getModel().isEmpty()) {
					 siteHasSpectro = true;
				 }
				 
				 sessionMap.put(origReqObj.getGuid(), origReqObj);
				 returnStatus = "SUCCESS";
			 }
			
			 //remove the attribute from the session cookie that says we are logged in.
			 HttpServletRequest request = ServletActionContext.getRequest();
			 request.logout();
			 
			 if (returnStatus == "SUCCESS"){
				 //Finally, we are good to display the welcome page - we are in
				 //BKP 07/16/2019 Wait, check to see if the user has accepted the EULA.  
				 //If not, they need to route to the EULA page.
				 if (reqGuid!=null) {
					 logger.info("reqguid is not null");
					 RequestObject finalReqObj = (RequestObject) sessionMap.get(reqGuid);
					 if (finalReqObj!=null) {
						 logger.info("finalReqObj is not null");
						 String eulaCustId = finalReqObj.getCustomerID();
						 if (eulaCustId!=null) {
							 logger.info(Encode.forJava("eulaCustId is " + eulaCustId ));
							 String eulaAcceptanceCode = eulaService.getAcceptanceCode("CUSTOMERSHERCOLORWEB", eulaCustId);
							 if(eulaAcceptanceCode!=null) {
								 logger.info("eulaAcceptanceCode is not null");
								 //We have an activation code - this user needs to approve the eula.
								 return "eula";
							 } else {
								 logger.info("eulaAcceptanceCode is null");
								 //no activation code, the user already activated, head on in.
								 return SUCCESS;
							 }
						 } else {
							 logger.info("eulaCustId is null");
							 //the customer id was null, on what looks to be a valid requestObject?
							 //should never happen, but log it and return NONE
							 return NONE;
						 }
					 } else {
						 logger.info("finalReqObj is null");
						 //Unable to get the request object we just wrote from the sessionMap.
						 //should never happen, but log it and return NONE
						 return NONE;
					 }
				 } else {
					 logger.info("reqGuid is null");
					 //should probably never happen, but if so, return NONE
					 return NONE;
				 }
			 	//return SUCCESS;
			 } else {
				 //bad/invalid entry
				 return NONE;
			 }
		     
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	private String GetCustWebParmsKey(RequestObject theRO) {
		String theCustWebParmsKey = "";
		
		//the acct nbr from SW_USER is defined as a 9 digit integer, but the RequestObject has the 
		//customer ID defined as a String.  Map it back to int for comparing but make sure to handle
		//failures, in case we decide to have this field be an actual string in the future.
		int acctNbr = 0;
		try {
			acctNbr = Integer.parseInt(theRO.getCustomerID());
		} catch (NumberFormatException nfe) {
			acctNbr = 0;
		}
		try {
			//check if it is a valid account number
			if (acctNbr !=0 && (acctNbr < 400000000 ||acctNbr > 400000015 )) {
				//this is a valid account number, use this for the customer ID.
				
				//20180425-BKP-Wait, let's check if there's an entry on the transform table for this customer.
				//We may have an external customer with multiple sites that they want to keep separate.
				theCustWebParmsKey = customerService.getLoginTransformCustomerId(Encode.forHtml(theRO.getUserId())).trim();
				
				if (theCustWebParmsKey.isEmpty()) {
					//no transform found, so use the current account number.
					theCustWebParmsKey = Integer.toString(acctNbr);
				}
				
				return theCustWebParmsKey;
			}
			
			//Generic or invalid account number.
			//Check if it is a sales rep.  If so, we will use the territory number (if valid).
			if (theRO.isSalesRep()) {
				theCustWebParmsKey = theRO.getTerritory();
				logger.error(Encode.forJava("It is a sales rep, territory is " + theRO.getTerritory()));
				//check to assure this is not null, blank or some other generic.
				if (theCustWebParmsKey==null) {
					//It's null, what do we return instead?
					theCustWebParmsKey = "";
					throw new Exception(theRO.getUserId() + " is flagged as a sales rep but the territory is invalid");
				} else {
					if(theCustWebParmsKey.isEmpty() || theCustWebParmsKey.equals("0") || theCustWebParmsKey.equals("100")) {
						//It's a non valid entry, what do we return instead?
						theCustWebParmsKey = "";
						throw new Exception(theRO.getUserId() + " is flagged as a sales rep but the territory is invalid");
					} else {
						//valid territory, use it for CUSTWEBPARMS key.
						return theCustWebParmsKey;
					}
				}
			}
			
			
			//Not a sales rep, is it a store employee?  If so, we'll use home store (if valid)
			if (theRO.isStoreEmp()) {
				logger.error("It is a store emp, store  is " + theRO.getHomeStore());
				theCustWebParmsKey = Integer.toString(theRO.getHomeStore());
				//check to assure this is not null, blank or some other generic.
				if(theCustWebParmsKey.equals("0") || theCustWebParmsKey.equals("9989")) {
					//It's a non valid entry, what do we return instead?
					theCustWebParmsKey = "";
					throw new Exception(theRO.getUserId() + " is flagged as a store employee but the home store is invalid");
				} else {
					//valid territory, use it for CUSTWEBPARMS key.
					return theCustWebParmsKey;
				}
			}
			
			//Check if it's an internal employee (Gems Emp ID is not null/blank/0)
//			if (theRO.getGemsEmpId()!=null) {
//				theCustWebParmsKey = theRO.getGemsEmpId();
//				//check to assure this is not null, blank or some other generic.
//				if(!theCustWebParmsKey.equals("0") && !theCustWebParmsKey.isEmpty()) {
//					//valid GEMS Emp ID, use it for CUSTWEBPARMS key.
//					return theCustWebParmsKey;
//				}
//			}
			
			//2018-04-20
			//internal employees will be bounced off of the transform table as well.  That allows individual
			//internal employees in a group to potentially "share" a single CustWebParms/history item.
			
			//None of the "known" entries are valid, so use the transform table with the user id.
			theCustWebParmsKey = customerService.getLoginTransformCustomerId(Encode.forHtml(theRO.getUserId())).trim();
			
			if (theCustWebParmsKey.isEmpty()) {
				theCustWebParmsKey = "";
				throw new Exception(theRO.getUserId() + " has no entry defined in CustWebLoginTransform");
			}
			
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return theCustWebParmsKey;
	}
	private void setIsPrinterConfigured() {
		
		logger.debug("Looking for printer devices");

		List<CustWebDevices> devices = customerService.getCustDevices(reqObj.getCustomerID());
		for (CustWebDevices d: devices) {
		
			if(d.getDeviceType().equalsIgnoreCase("PRINTER")) {
				reqObj.setPrinterConfigured(true);
				setSiteHasPrinter(true);
				
				logger.debug(Encode.forJava("Device " + d.getDeviceModel() + " found for " + reqObj.getCustomerID() + " - " + d.getDeviceType()));
			}
			else {
				setSiteHasPrinter(false);
			}
		}

	}
	
//	@SuppressWarnings("restriction")
//	private boolean validateToken(String theId,String theToken, String theSendUrl) {
//		boolean returnStatus = false;
//		logger.info("begin validate token");
//		try {
//            // Confirm the token and id are not null - there's no sense creating a soap request if 
//			// either is null.  Just return false.
//			if (theToken==null || theId==null) {
//				 logger.debug("in validateToken, token or id was null, returning false");
//				return false;
//			}
//			
//			// Create SOAP Connection
//			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
//		    SOAPConnection soapConnection = soapConnectionFactory.createConnection();
//		    
//            // Trust to certificates
//            //doTrustToCertificates();
//            
//		    // Send SOAP Message to SOAP Server
//	        //String url = "https://stap1ecowv.sherwin.com/sher-link/StoresEcommerceService";
//	        SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(theId,theToken), theSendUrl);
//		    
//	        System.out.print("Response SOAP Message:");
//	        soapResponse.writeTo(System.out);
//	        logger.debug();
//	        //parse response to find the true/false flag.
//	        //NodeList resultList = soapResponse.getSOAPBody().getElementsByTagName("result");
//	        //String isTrueOrFalse = resultList.item(0).getNodeValue();
//	        
//	        SOAPPart mySPart = soapResponse.getSOAPPart();
////	        if (mySPart != null) {
////	        	logger.debug("mySPart not null");
////	        }
//
//	        SOAPEnvelope myEnvp = mySPart.getEnvelope();
//	        
////	        if (mySPart != null) {
////	        	logger.debug("myEnvp not null");
////	        }
//	        
//	        SOAPBody myBody = myEnvp.getBody();
//	        
////	        if (myBody != null) {
////	        	logger.debug("myBody not null");
////	        }
//	        
//	        String isTrueOrFalse = "init";
//	        NodeList returnList = myBody.getElementsByTagName("result");
//			for (int x = 0; x < returnList.getLength(); x++) {
//				NodeList innerList = returnList.item(x).getChildNodes();
//				for (int y = 0; y < innerList.getLength(); y++) {
//					Node tokenNode = innerList.item(y);
//					isTrueOrFalse = tokenNode.getNodeValue();
//				}
//			}
//
//	        
//	        //Name ElName = myEnvp.createName("result");
////	        Name ElName = myEnvp.createName("ns1:validateTokenResponse");
////	        
////	        if (ElName != null) {
////	        	logger.debug("ElName not null");
////	        }
////	        logger.debug("firstchildnodename is " + myBody.getFirstChild().getNodeName());
////	        logger.debug("firstchilds child node value is " + myBody.getFirstChild().getFirstChild().getNodeValue());
////	        
////	        SOAPBodyElement sbe = (SOAPBodyElement) myBody.getFirstChild();
////	        logger.debug("sbe firstchildnodename is " + sbe.getFirstChild().getNodeName());
////	        
////	        SOAPBodyElement sbe2 = (SOAPBodyElement) sbe.getFirstChild();
////	      
////	        //Get the value
////	        String isTrueOrFalse = sbe2.getNodeValue();
//
////	        logger.debug("isTrueOrFalse is " + isTrueOrFalse);
//	        
//	        if (isTrueOrFalse.equalsIgnoreCase("true")) {
//	        	returnStatus = true;
//	        } else {
//	        	returnStatus = false;
//	        }
//		    	
//			return returnStatus;
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//			logger.error("ErrorDetail",e);
//			logger.debug(e);
//			return false;
//		}
//		
//	}
//
//	@SuppressWarnings("restriction")
//	private static SOAPMessage createSOAPRequest(String theId,String theToken) throws Exception {
//        MessageFactory messageFactory = MessageFactory.newInstance();
//        SOAPMessage soapMessage = messageFactory.createMessage();
//        soapMessage.setProperty(SOAPMessage.CHARACTER_SET_ENCODING, "UTF-8");
//        soapMessage.setProperty(SOAPMessage.WRITE_XML_DECLARATION, "true");
//        SOAPPart soapPart = soapMessage.getSOAPPart();
//
//        String serverURI = "http://sherlink.sherwin.com/sher-link/StoresEcommerceService";
//        logger.debug("token " + theToken);
//        //BKP - 20170524 - add a quick fix to convert blank characters in the token back to plus signs.
//        //RFC-1866 treats + signs in the query portion of HTML as spaces.
//        String theNewToken = theToken.replaceAll(" ", "+");
//        logger.debug("NEW token " + theNewToken);
//        
//        // SOAP Envelope
//        SOAPEnvelope envelope = soapPart.getEnvelope();
//        envelope.addNamespaceDeclaration("stor", serverURI);
//
//        // SOAP Body
//        SOAPBody soapBody = envelope.getBody();
//        SOAPElement soapBodyElem = soapBody.addChildElement("validateToken", "stor");
//        SOAPElement soapBodyElem1 = soapBodyElem.addChildElement("loginId");
//        soapBodyElem1.addTextNode(theId);
//        //SOAPElement soapBodyElem2 = soapBodyElem.addChildElement("secondaryIdentifier");
//        //soapBodyElem2.addTextNode(theAcct);
//        SOAPElement soapBodyElem3 = soapBodyElem.addChildElement("token");
//        soapBodyElem3.addTextNode(theNewToken);
//
//        soapMessage.saveChanges();
//
//        /* Print the request message */
//        System.out.print("Request SOAP Message:");
//        soapMessage.writeTo(System.out);
//
//        return soapMessage;
//    }
//	

//	static public void doTrustToCertificates() throws Exception {
//        //Security.addProvider(new Provider());
//        TrustManager[] trustAllCerts = new TrustManager[]{
//                new X509TrustManager() {
//                    public X509Certificate[] getAcceptedIssuers() {
//                        return null;
//                    }
// 
//                    public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
//                        return;
//                    }
// 
//                    public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
//                        return;
//                    }
//
//                }
//        };
// 
//        SSLContext sc = SSLContext.getInstance("SSL");
//        sc.init(null, trustAllCerts, new SecureRandom());
//        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//        HostnameVerifier hv = new HostnameVerifier() {
//            public boolean verify(String urlHostName, SSLSession session) {
//                if (!urlHostName.equalsIgnoreCase(session.getPeerHost())) {
//                    logger.debug("Warning: URL host '" + urlHostName + "' is different to SSLSession host '" + session.getPeerHost() + "'.");
//                }
//                return true;
//            }
//        };
//        HttpsURLConnection.setDefaultHostnameVerifier(hv);
//    }
	
	public void setSession(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;

	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public EulaService getEulaService() {
		return eulaService;
	}

	public void setEulaService(EulaService eulaService) {
		this.eulaService = eulaService;
	}
	
	public String getGuid1() {
		return guid1;
	}

	public void setGuid1(String guid1) {
		this.guid1 = guid1;
	}
	
	public String getReqGuid() {
		return reqGuid;
	}

	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}

	public String getAcct() {
		return acct;
	}

	public void setAcct(String acct) {
		this.acct = Encode.forHtml(acct);
	}



	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = Encode.forHtml(first);
	}



	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = Encode.forHtml(last);
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = Encode.forHtml(id);
	}



	public String getToken() {
		return token;
	}



	public void setToken(String token) {
		this.token = Encode.forHtml(token);
	}

	public boolean isNewSession() {
		return newSession;
	}

	public boolean isSiteHasTinter() {
		return siteHasTinter;
	}

	public TinterInfo getTinter() {
		return tinter;
	}

	public boolean isSiteHasSpectro() {
		return siteHasSpectro;
	}

	public SpectroInfo getSpectro() {
		return spectro;
	}

	public boolean isSessionHasTinter() {
		return sessionHasTinter;
	}

	public boolean isReReadLocalHostTinter() {
		return reReadLocalHostTinter;
	}

	public void setReReadLocalHostTinter(boolean reReadLocalHostTinter) {
		this.reReadLocalHostTinter = reReadLocalHostTinter;
	}

	public String getSherLinkURL() {
		return sherLinkURL;
	}

	public void setSherLinkURL(String sherLinkURL) {
		this.sherLinkURL = Encode.forHtml(sherLinkURL);
	}
	
	public String getLoMessage() {
		return loMessage;
	}

	public void setLoMessage(String loMessage) {
		this.loMessage = Encode.forHtml(loMessage);
	}
	
	public int getDaysUntilPwdExp() {
		return daysUntilPwdExp;
	}

	public void setDaysUntilPwdExp(int daysUntilPwdExp) {
		this.daysUntilPwdExp = daysUntilPwdExp;
	}

	public boolean isSiteHasPrinter() {
		return siteHasPrinter;
	}

	public void setSiteHasPrinter(boolean siteHasPrinter) {
		this.siteHasPrinter = siteHasPrinter;
	}

	public int getTintQueueCount() {
		return tintQueueCount;
	}
}
