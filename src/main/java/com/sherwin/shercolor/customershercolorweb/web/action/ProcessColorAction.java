package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;


//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;


import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.colormath.domain.ColorCoordinates;
import com.sherwin.shercolor.common.domain.CdsColorMast;
import com.sherwin.shercolor.common.domain.CdsProd;
import com.sherwin.shercolor.common.domain.CustWebParms;
import com.sherwin.shercolor.common.domain.CustWebSpectroRemote;
import com.sherwin.shercolor.common.exception.SherColorException;
import com.sherwin.shercolor.common.service.ColorBaseService;
import com.sherwin.shercolor.common.service.ColorMastService;
import com.sherwin.shercolor.common.service.ColorService;
import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.common.service.ProductService;
import com.sherwin.shercolor.common.validation.ColorValidator;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.autoComplete;
import com.sherwin.shercolor.util.domain.SwMessage;



public class ProcessColorAction extends ActionSupport implements SessionAware, LoginRequired {
	private ColorMastService colorMastService;
	private ColorBaseService colorBaseService;
	private ColorService colorService;
	private CustomerService customerService;
	private ColorValidator colorValidator;
	private Map<String, Object> sessionMap;
	private ProductService productService;

	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(ProcessColorAction.class);
	
	private CdsColorMast thisColor;
	private String reqGuid;
	private String colorData;
	
	private String colorComp;
	private String colorID;
	private String colorName;
	
	private String intBases;
	private String extBases;
	
	//This will be passed in from a customer parms record - but in the interim, default to SW.
	private String prodComp = "SW";
	
	private String selectedCoType;
	private String selectedCoTypes;
	private String selectedCompany;
	private String partialColorNameOrId; 
	private List<autoComplete> options;
	
	private Map<String, String> cotypes = new LinkedHashMap<String, String>();
	private ArrayList<String> colorCompanies = new ArrayList<String>();
	private ArrayList<String> natColorCompanies = new ArrayList<String>();

	private String message;
	
	private String defaultCoTypeValue = "SW";
	
	private String SW;
	private String COMPETITIVE;
	private String CUSTOM;
	private String CUSTOMMATCH;
	private String SAVEDMEASURE;
	private String NAT;

	private List<CustWebSpectroRemote> savedMeasurements;
	private List<String> curvesList;
	private String measuredCurve;
	private String measuredName;
	
	
	private void buildCotypesMap() {
		SW = getText("processColorAction.SherwinWilliams");
		COMPETITIVE = getText("processColorAction.competitive");
		CUSTOM = getText("processColorAction.customManual");
		CUSTOMMATCH = getText("processColorAction.customMatch");
		SAVEDMEASURE = getText("processColorAction.savedCi62Measurement");
		NAT = getText("processColorAction.natColor");

		cotypes.put("SW",SW);
		cotypes.put("COMPET",COMPETITIVE);
		cotypes.put("CUSTOM",CUSTOM);
		cotypes.put("CUSTOMMATCH", CUSTOMMATCH);
		cotypes.put("SAVEDMEASURE", SAVEDMEASURE);
		cotypes.put("NAT", NAT);

		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		
		//Only display National Accounts if the Customer Type is STORE or DRAWDOWN and not a self tinting customer
		if (!reqObj.getCustomerType().equals("STORE") && !reqObj.getCustomerType().equals("DRAWDOWN")) {
			cotypes.remove("NAT");
		}
		
		if (reqObj.getSpectro().getSerialNbr()==null) {
			 //No  device, so remove the Custom Match option from cotypes.
			 cotypes.remove("CUSTOMMATCH");
		 }
		 // load saved Ci62 remote measurements for the Saved Measure option
		 String customerId = reqObj.getCustomerID();
		 savedMeasurements = customerService.getCustWebSpectroRemotes(customerId);
		 if (savedMeasurements.size() == 0) {
			 cotypes.remove("SAVEDMEASURE");
		 }
		 curvesList = new ArrayList<String>();
		 for (CustWebSpectroRemote measure : savedMeasurements) {
			 String curve = Arrays.toString(measure.getMeasuredCurve()).replace("[", "").replace("]", "");
			 curvesList.add(curve);
		 }
	}
	
	//return company type list for radio options
	public Map<String, String> getCotypes() {
		return cotypes;
	}
	
	//return default company value
	public String getDefaultCoTypeValue(){
		return defaultCoTypeValue;
	}
	
	public String listColors() {
		
		try {
			
			partialColorNameOrId = URLDecoder.decode(partialColorNameOrId, "UTF-8");
			logger.debug(Encode.forJava("decoded partialColorNameOrId - " + partialColorNameOrId));

			if (StringUtils.equals(selectedCoType, "SW")) {
				options = mapToOptions(colorMastService.autocompleteSWColor(partialColorNameOrId.toUpperCase()),"SW");
			} else {
				if (StringUtils.equals(selectedCoType, "COMPET")) {
					if (selectedCompany.equals(getText("processColorAction.all"))){
						options = mapToOptions(colorMastService.autocompleteCompetitiveColor(partialColorNameOrId.toUpperCase()), "COMPET");
					} else {
						options = mapToOptions(colorMastService.autocompleteCompetitiveColorByCompany(partialColorNameOrId.toUpperCase(), selectedCompany), "COMPET");
					}
					
				} 
				else if (StringUtils.equals(selectedCoType, "NAT")) {
					if (selectedCompany.equals(getText("processColorAction.all"))){
						options = mapToOptions(colorMastService.autocompleteNatColor(partialColorNameOrId.toUpperCase()), "NAT");
					} else {
						options = mapToOptions(colorMastService.autocompleteNatColorByCompany((partialColorNameOrId.toUpperCase()), selectedCompany), "NAT");
					}
				} 
				else {
					options = new ArrayList<autoComplete>();
					options.add(new autoComplete(getText("processColorAction.manual"),"MANUAL"));
				}
			}
		}
		catch (SherColorException e){
			//String messageId = Integer.toString(e.getCode());
			message = e.getMessage();
			logger.error(Encode.forJava(message), e);
		} catch (UnsupportedEncodingException e) {
			message = e.getMessage();
			logger.error(e.getMessage(), e);
		}
		
		return SUCCESS;

	}
	
	private List<autoComplete> mapToOptions(List<CdsColorMast> colorList, String coType) {
		List<autoComplete> outList = new ArrayList<autoComplete>();
		String theLabel;
		String theValue;
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);

		//BKP 2018-03-09 check foe null/empty colorList
		if (colorList != null) {
			int index = 0;
			for (CdsColorMast item : colorList) {
				
				if (StringUtils.equals(coType, "SW")) {
					//only display locID if present.
					if (item.getLocId()==null) {
						theLabel = item.getColorId() + " " + item.getColorName();
					} else {
						theLabel = item.getColorId() + " " + item.getColorName() + " " +  item.getLocId();
					}
					//theValue = item.getColorComp() + Character.toString((char) 31) + " " + item.getColorId();
					//theValue = item.getColorComp() + Character.toString((char) 0) + " " + item.getColorId();
					theValue = item.getColorComp() + " " + item.getColorId();
				} else {
					theLabel = item.getColorComp() + " " + item.getColorId() + " " + item.getColorName();
					//theValue = item.getColorComp() + Character.toString((char) 31) + " " + item.getColorId();
					//theValue = item.getColorComp() + Character.toString((char) 0) + " " + item.getColorId();
					theValue = item.getColorComp() + " " + item.getColorId();
				}
				
				if (reqObj.getCustomerType().equals("STORE") || reqObj.getCustomerType().equals("DRAWDOWN")) {
					if (item.getOldColorName() != null) {
						theLabel = theLabel + getText("processColorAction.oldName",new String[] {item.getOldColorName()});  
					}
				}

				autoComplete autoComplete = new autoComplete(theLabel,theValue);
				autoComplete.setColorNumber(item.getColorId());
				autoComplete.setCompanyName(item.getColorComp());
			    outList.add(autoComplete);
			    if (index > 98) {
			    	break;
			    }
			    index++;
			} 
		}
		return outList;
	}
	
	private void parseColorData(String colorData) {
		
		String colorEntry = new String("");
		try {
			colorData = URLDecoder.decode(colorData,"UTF-8");
			colorEntry = StringEscapeUtils.unescapeHtml4(partialColorNameOrId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		if (colorData.equals("")) {
			// The user typed nothing, so do nothing
		} 
		else if (colorData.equals("[]")){
			// The user typed a color id or name that does not exist
			setColorID(partialColorNameOrId);
			if (selectedCoTypes.equalsIgnoreCase("SW")) {
				setColorComp("SHERWIN-WILLIAMS");
			} 
			else if (selectedCoTypes.equalsIgnoreCase("NAT")) {
				setColorComp("NATIONAL ACCOUNTS");
			} 
			else {
				setColorComp("COMPETITIVE");
			}
		}
		else {
			// Colordata contains JSON so it sequentially gets broken down to parse an array of
			// autocomplete results and returns the value typed into the search bar
			if (colorData.contains("[")) {
				colorData = colorData.replace("[", "");
				colorData = colorData.replace("]", "");
				colorData = colorData.replace("{", "");
				colorData = colorData.replace("\"", "");
				String[] outList = colorData.split("},");
				boolean foundMatch = false;
				for (String record : outList) {
					String[] data = record.split(",");
					String theValue = data[3].replaceAll("value:", "");
					// The below replace statement fixes a bug when there
					// is only one object in the autocomplete list
					theValue = theValue.replace("}", "");
					if (colorEntry.equals(theValue)) {
						foundMatch = true;
						setColorID(data[0].replaceAll("colorNumber:", ""));
						setColorComp(data[1].replaceAll("companyName:", ""));
						break;
					}
				}
				if (foundMatch == false) {
					setColorID(partialColorNameOrId);
					if (selectedCoTypes.equalsIgnoreCase("SW")) {
						setColorComp("SHERWIN-WILLIAMS");
					} 
					else if (selectedCoTypes.equalsIgnoreCase("NAT")) {
						setColorComp("NATIONAL ACCOUNTS");
					} 
					else {
						setColorComp("COMPETITIVE");
					}
				}
			} 
			// colordata does not contain JSON so it directly assigns the colordata
			// as the colorId and then uses the selectedCoTypes to assign the colorComp
			else {
				setColorID(colorData);
				if (selectedCoTypes.equalsIgnoreCase("SW")) {
					setColorComp("SHERWIN-WILLIAMS");
				} 
				else if (selectedCoTypes.equalsIgnoreCase("NAT")) {
					setColorComp("NATIONAL ACCOUNTS");
				} 
				else {
					setColorComp("COMPETITIVE");
				}
			}	
		}
	}
	public String execute() {
		
		colorComp = "";
		colorID = "";
		String rgbHex = null;
		String primerId = null;
		Boolean vinylColor = false;
		String colorType = "";
		String closestSwColorName = null;
		String closestSwColorId = null;
		CdsColorMast closestSwColor = null;
		

		
		try {
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			reqObj.setProductChosenFromDifferentBase(false);
			
			//Okay, there is data there.  Interpret it.
			if (selectedCoTypes.equalsIgnoreCase("CUSTOM")) {
				//WHAT DO WE DO HERE?
				//TO DO
				colorComp = "CUSTOM";
				colorID   = "MANUAL";
				colorName = partialColorNameOrId.trim();
				if (colorName.isEmpty()) {
					colorName = "COLOR";
				}
				colorType = "CUSTOM";
				
			} else if (selectedCoTypes.equalsIgnoreCase("CUSTOMMATCH")) {
				colorComp = "CUSTOM";
				colorID   = "MATCH";
				colorName = partialColorNameOrId.trim();
				if (colorName.isEmpty()) {
					colorName = "COLOR";
				}
				colorType = "CUSTOMMATCH";

			} else if (selectedCoTypes.equalsIgnoreCase("SAVEDMEASURE")) {
				colorComp = "CUSTOM";
				colorID   = "MATCH";
				colorName = measuredName;
				if (colorName.isEmpty()) {
					colorName = "COLOR";
				}
				colorType = "SAVEDMEASURE";
			} else {
					
				// This conditional helps prevent times where colorData doesn't get populated
				// when the user quickly enters in a number and does a next operation before
				// the auto complete gets triggered
				if (colorData.equals("")) {
					colorData = partialColorNameOrId.trim();
				}
				// Following method call should be able to cover all conditionals previously
				// implemented here
				
				parseColorData(colorData);
				
				if (selectedCoTypes.equalsIgnoreCase("SW")) {
					colorType = "SHERWIN-WILLIAMS";
				} 
				else if (selectedCoTypes.equalsIgnoreCase("NAT")) {
					colorType = "NATIONAL ACCOUNTS";
				}
				else {
					colorType = "COMPETITIVE";
				}
				
				// Try getting an RGB value for the object.
				ColorCoordinates colorCoord = colorService.getColorCoordinates(colorComp, colorID, "D65");
				if (colorCoord != null) {
					rgbHex = colorCoord.getRgbHex();
				}
//				setThisColor(colorMastService.read(colorComp, colorID));
				
				//We should have thisColor set.  Or not.  If not, throw a validation error?

				List<SwMessage> errlist = colorMastService.validate(colorComp, colorID);
				if (errlist.size()>0) {
					for(SwMessage item:errlist) {
						addFieldError("partialColorNameOrId", item.getMessage());
					}
					//PSCWEB-159 - set the default radio button based on SW vs Competitive
					if (selectedCoTypes.equalsIgnoreCase("SW")) {
						defaultCoTypeValue = "SW";
					} 
					else if (selectedCoTypes.equalsIgnoreCase("NAT")) {
						defaultCoTypeValue = "NAT";
					}
					else {
						defaultCoTypeValue = "COMPET";
					}
					// repopulate company dropdown and color type list
					buildCompaniesList();
					buildNatCompaniesList();
					buildCotypesMap();
					return INPUT;
				} else {
					//validated successfully, call a read and set ThisColor
					//also set primerId and vinylColor fields.
					thisColor = colorMastService.read(colorComp, colorID);
					this.setColorName(thisColor.getColorName());
					primerId = thisColor.getPrimerId();
					vinylColor = thisColor.getIsVinylSiding();
					
					if (reqObj.getCustomerType().equals("STORE") || reqObj.getCustomerType().equals("DRAWDOWN")) {
						if (thisColor.getOldColorName() != null ) {
							addActionMessage(getText("processColorAction.oldNameAlert",new String[]{thisColor.getOldColorName(),thisColor.getColorName()}));
						}
					}
				}
				
				String custID = reqObj.getCustomerID();
				buildBaseLists(custID);
				
				// get closest SW color if user picked a competitive one, or comes back null if no close match found
				if (selectedCoTypes.equalsIgnoreCase("COMPET")) {
					closestSwColor = colorService.findClosestSwColor(thisColor.getColorComp(), thisColor.getColorId());	
					if (closestSwColor != null) {
						closestSwColorName = closestSwColor.getColorName();
						closestSwColorId = closestSwColor.getColorId();
					}
				}
			} 
			
			//set the successful information into the request object.
			reqObj.setColorComp(colorComp);
			if (colorID==null) {colorID="";}
			reqObj.setColorID(colorID);
			if (colorName==null) {colorName="";}
			reqObj.setColorName(colorName);
			reqObj.setIntBases(intBases);
			reqObj.setExtBases(extBases);
			reqObj.setRgbHex(rgbHex);
			reqObj.setPrimerId(primerId);
			reqObj.setColorVinylOnly(vinylColor);
			reqObj.setColorType(colorType);
			if (closestSwColor != null) {
				reqObj.setClosestSwColorName(closestSwColorName);
				reqObj.setClosestSwColorId(closestSwColorId);
			} else {
				reqObj.setClosestSwColorName("");
				reqObj.setClosestSwColorId("");
			}
			sessionMap.put(reqGuid, reqObj);
			if (colorType.equals("CUSTOMMATCH")) {
				return "measure";
			} else if (colorType.equals("SAVEDMEASURE")) {
				return "savedMeasure";
			} else {
				return SUCCESS;
			}
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	
	// User hit the backup button on the Color page
	public String backItUp() {
		try {
			//blank out the color info as we are backing up.
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			reqObj.setColorComp("");
			reqObj.setColorID("");
			reqObj.setColorName("");
			reqObj.setIntBases("");
			reqObj.setExtBases("");
			reqObj.setRgbHex("");
			reqObj.setPrimerId(null);
			reqObj.setColorType("");
			reqObj.setColorVinylOnly(false);
			sessionMap.put(reqGuid, reqObj);
			
			if(reqObj.getJobFieldList() != null && reqObj.getJobFieldList().size() > 0) {
				return SUCCESS;
			} else {
				return "restart";
			}
		     
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String display() {
		try {
			buildCotypesMap();
			buildCompaniesList();
			buildNatCompaniesList();

		    return SUCCESS;
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	private void buildCompaniesList() {
		colorCompanies.add(getText("processColorAction.all")); 
		 
		String [] colorCompaniesArray = colorMastService.listColorCompanies(false);
		for (String company : colorCompaniesArray) {
			if (!company.equals("SHERWIN-WILLIAMS")){
				colorCompanies.add(company);
			}
		}
	}
	
	private void buildNatCompaniesList() {
		natColorCompanies.add(getText("processColorAction.all")); 
		 
		String [] colorCompaniesArray = colorMastService.listColorCompanies(true);
		for (String company : colorCompaniesArray) {
			natColorCompanies.add(company);
		}
	}
	
	private void buildBaseLists(String custID) {
		List<String> baseList;
		Set<String> interiorBases = new HashSet<>();
		Set<String> exteriorBases = new HashSet<>();

		//If the color has a restricted product list. Pull the bases from the products that are allowed to use it.
		if(thisColor.getForceProd() != null && !thisColor.getForceProd().trim().equals("")) {
			List<CdsProd> forcedProducts = productService.getForcedProducts(thisColor.getForceProd());
			for (int i = 0; i < forcedProducts.size(); i++) {
				String usage = forcedProducts.get(i).getIntExt().toUpperCase();
				switch(usage) {
				case "INTERIOR":
					interiorBases.add(forcedProducts.get(i).getBase());
					break;
				case "EXTERIOR":
					exteriorBases.add(forcedProducts.get(i).getBase());
					break;
				case "INT/EXT":
					interiorBases.add(forcedProducts.get(i).getBase());
					exteriorBases.add(forcedProducts.get(i).getBase());
					break;
				default:
					logger.warn("UNDEFINED USAGE: {}", usage);
					break;
				}						
			}
			setIntBases(StringUtils.join(interiorBases, ','));
			setExtBases(StringUtils.join(exteriorBases, ','));
		//If color references another color so use the base assignments from that color.
		} else if(thisColor.getXrefId() != null && thisColor.getXrefComp() != null) {
			baseList = colorBaseService.InteriorColorBaseAssignments(thisColor.getXrefComp(), thisColor.getXrefId(), prodComp);
			setIntBases(StringUtils.join(baseList, ','));
			
			baseList = colorBaseService.ExteriorColorBaseAssignments(thisColor.getXrefComp(), thisColor.getXrefId(), prodComp);
			setExtBases(StringUtils.join(baseList, ','));
		//No special cases. Try to find the bases for the given company and color.
		} else {
			baseList = colorBaseService.InteriorColorBaseAssignments(thisColor.getColorComp(), thisColor.getColorId(), prodComp);
			setIntBases(StringUtils.join(baseList, ','));
		
			baseList = colorBaseService.ExteriorColorBaseAssignments(thisColor.getColorComp(), thisColor.getColorId(), prodComp);
			setExtBases(StringUtils.join(baseList, ','));
		}
		
		//confirm that at least one of the intBases and ExtBases lists are populated.  If neither are populated,
		//call the autobase routine.
		if (intBases == null && extBases == null) {
			logger.info("JXL. Bases are still empty. Calling autobase..");
			//call autobase
			CustWebParms bobo = customerService.getDefaultCustWebParms(custID);
			String custProdComp = bobo.getProdComp();
			baseList = colorBaseService.GetAutoBase(thisColor.getColorComp(), thisColor.getColorId(), custProdComp );
			setIntBases(StringUtils.join(baseList, ','));
			setExtBases(StringUtils.join(baseList, ','));
		}		
	}
	
	public List<autoComplete> getOptions() {
		return options;
	}

	public void setOptions(List<autoComplete> options) {
		this.options = options;
	}

	public String getPartialColorNameOrId() {
		return partialColorNameOrId;
	}

	public void setPartialColorNameOrId(String partialColorNameOrId) {
		this.partialColorNameOrId = Encode.forHtmlContent(partialColorNameOrId.trim());
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = Encode.forHtml(message);
	}

	public ColorMastService getColorMastService() {
		return colorMastService;
	}

	public void setColorMastService(ColorMastService colorMastSvc) {
		this.colorMastService = colorMastSvc;
	}
	
	public String getSelectedCoType() {
		return selectedCoType;
	}

	public void setSelectedCoType(String selectedCoType) {
		this.selectedCoType = Encode.forHtml(selectedCoType);
	}

	public CdsColorMast getThisColor() {
		return thisColor;
	}

	public void setThisColor(CdsColorMast thisColor) {
		this.thisColor = thisColor;
	}

	public ColorBaseService getColorBaseService() {
		return colorBaseService;
	}

	public void setColorBaseService(ColorBaseService colorBaseService) {
		this.colorBaseService = colorBaseService;
	}

	public String getExtBases() {
		return extBases;
	}

	public void setExtBases(String extBases) {
		this.extBases = extBases;
	}

	public String getIntBases() {
		return intBases;
	}

	public void setIntBases(String intBases) {
		this.intBases = intBases;
	}

	public String getSelectedCoTypes() {
		return selectedCoTypes;
	}

	public void setSelectedCoTypes(String selectedCoTypes) {
		this.selectedCoTypes = Encode.forHtml(selectedCoTypes);
	}

	public ColorValidator getColorValidator() {
		return colorValidator;
	}

	public void setColorValidator(ColorValidator colorValidator) {
		this.colorValidator = colorValidator;
	}

	public void setSession(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;		
	}

	public CustomerService getCustomerService() {
		return customerService;
	}

	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

	public String getColorComp() {
		return colorComp;
	}

	public void setColorComp(String colorComp) {
		this.colorComp = colorComp;
	}

	public String getColorID() {
		return colorID;
	}

	public void setColorID(String colorID) {
		this.colorID = colorID;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public String getReqGuid() {
		return reqGuid;
	}

	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}
	
	public String getColorData(String colorData) {
		return colorData;
	}
	
	public void setColorData(String colorData) {
		this.colorData = Encode.forHtmlContent(colorData);
	}

	public ColorService getColorService() {
		return colorService;
	}

	public void setColorService(ColorService colorService) {
		this.colorService = colorService;
	}

	public ArrayList<String> getColorCompanies() {
		return colorCompanies;
	}

	public void setColorCompanies(ArrayList<String> colorCompanies) {
		this.colorCompanies = colorCompanies;
	}
	
	public String getSelectedCompany() {
		return selectedCompany;
	}
	
	public void setSelectedCompany(String selectedCompany) {
		this.selectedCompany = selectedCompany;
	}

	public List<CustWebSpectroRemote> getSavedMeasurements() {
		return savedMeasurements;
	}

	public void setSavedMeasurements(List<CustWebSpectroRemote> savedMeasurements) {
		this.savedMeasurements = savedMeasurements;
	}
	
	public List<String> getCurvesList() {
		return curvesList;
	}

	public void setCurvesList(List<String> curvesList) {
		this.curvesList = curvesList;
	}
	
	public ArrayList<String> getNatColorCompanies() {
		return natColorCompanies;
	}

	public void setNatColorCompanies(ArrayList<String> natColorCompanies) {
		this.natColorCompanies = natColorCompanies;
	}
	
	public String getMeasuredCurve() {
		return measuredCurve;
	}

	public void setMeasuredCurve(String measuredCurve) {
		this.measuredCurve = measuredCurve;
	}
	public String getMeasuredName() {
		return measuredName;
	}

	public void setMeasuredName(String measuredName) {
		this.measuredName = measuredName;
	}

	public ProductService getProductService() {
		return productService;
	}

	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
}
