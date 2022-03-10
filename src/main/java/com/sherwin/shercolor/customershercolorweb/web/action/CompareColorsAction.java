package com.sherwin.shercolor.customershercolorweb.web.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.sherwin.shercolor.common.domain.CdsColorMast;
import org.owasp.encoder.Encode;
import org.owasp.encoder.Encoder;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.colormath.domain.ColorCoordinates;
import com.sherwin.shercolor.common.exception.SherColorException;
import com.sherwin.shercolor.common.service.ColorMastService;
import com.sherwin.shercolor.common.service.ColorService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.autoComplete;
import com.sherwin.shercolor.util.domain.SwMessage;
import org.springframework.stereotype.Component;

@Component
public class CompareColorsAction extends ActionSupport implements SessionAware, LoginRequired {
	private static final long serialVersionUID = 1L;
	static Logger logger = LoggerFactory.getLogger(CompareColorsAction.class);
	private Map<String, Object> sessionMap;
	private String reqGuid;
	private boolean match;
	private int lookupControlNbr;
	private String SW;
	private String COMPETITIVE;
	private String CUSTOMMATCH;
	private String MEASURE;
	private Map<String, String> sourceOptions;
	private List<autoComplete> options;
	private String selectedCoType;
	private String selectedCoTypes;
	private String defaultCoTypeValue = "SW";
	private String partialColorNameOrId;
	private String colorComp;
	private String colorId;
	private String colorData;
	private List<String> colorCompanies;
	private String selectedCompany;
	private boolean measure;
	private boolean compare;
	
	@Autowired
	private ColorService colorService;
	
	@Autowired
	private ColorMastService colorMastService;
	
	public String display() {
		try {
			
			buildSourceOptionsMap();
			buildCompaniesList();
			
			return SUCCESS;
		} catch(RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	public String execute() {
		try {
			logger.info("excuting compare colors...");
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			Map<String, ColorCoordinates> coordMap = new HashMap<String, ColorCoordinates>();
			colorComp = "";
			colorId = "";
			
			if (selectedCoTypes.equalsIgnoreCase("CUSTOMMATCH")) {
				logger.info("match source");
				setMatch(true);
				return "match";
			} else if (selectedCoTypes.equalsIgnoreCase("MEASURE")) {
				logger.info("measure source");
				setMeasure(true);
				return "measure";
			} else {
				logger.info("sw or competitive source");
				//SW or compet color
				if (colorData.equals("")) {
					colorData = partialColorNameOrId.trim();
				}
				
				parseColorData(colorData);
				
				List<SwMessage> errlist = colorMastService.validate(colorComp, colorId);
				if (errlist.size()>0) {
					for(SwMessage item:errlist) {
						addFieldError("partialColorNameOrId", item.getMessage());
					}
					
					if (selectedCoTypes.equalsIgnoreCase("SW")) {
						setDefaultCoTypeValue("SW");
					} else {
						setDefaultCoTypeValue("COMPET");
					}
					
					buildCompaniesList();
					buildSourceOptionsMap();
					return INPUT;
				} else {
					
					ColorCoordinates colorCoord = colorService.getColorCoordinates(colorComp, colorId, "D65");
					
					coordMap.put("standard", colorCoord);
					
					reqObj.setColorCoordMap(coordMap);
					
					sessionMap.put(reqGuid, reqObj);
				}
			}
			
			return SUCCESS;
		} catch(RuntimeException e) {
			logger.error(e.getMessage(), e);
			return ERROR;
		}
	}
	
	private void buildSourceOptionsMap() {
		sourceOptions = new LinkedHashMap<String, String>();
		
		SW = getText("compareColors.sherwinWilliamsColor");
		COMPETITIVE = getText("compareColors.competitiveColor");
		CUSTOMMATCH = getText("compareColors.existingCustomMatch");
		MEASURE = getText("compareColors.measureColor");
		
		sourceOptions.put("SW", SW);
		sourceOptions.put("COMPET",COMPETITIVE);
		sourceOptions.put("CUSTOMMATCH", CUSTOMMATCH);
		sourceOptions.put("MEASURE", MEASURE);
	}
	
	private void buildCompaniesList() {
		colorCompanies = new ArrayList<String>();
		colorCompanies.add(getText("processColorAction.all")); 
		 
		String [] colorCompaniesArray = colorMastService.listColorCompanies(false);
		for (String company : colorCompaniesArray) {
			if (!company.equals("SHERWIN-WILLIAMS")){
				colorCompanies.add(company);
			}
		}
	}
	
	public String listColors() {
		
		try {

			partialColorNameOrId = URLDecoder.decode(partialColorNameOrId, "UTF-8");

			logger.debug("decoded partialColorNameOrId - " + partialColorNameOrId);

			if (selectedCoType.equals("SW")) {
				options = mapToOptions(colorMastService.autocompleteSWColor(partialColorNameOrId.toUpperCase()),"SW");
			} else {
				if (selectedCoType.equals("COMPET")) {
					if (selectedCompany.equals(getText("processColorAction.all"))){
						options = mapToOptions(colorMastService.autocompleteCompetitiveColor(partialColorNameOrId.toUpperCase()), "COMPET");
					} else {
						options = mapToOptions(colorMastService.autocompleteCompetitiveColorByCompany(partialColorNameOrId.toUpperCase(), selectedCompany), "COMPET");
					}
				} else {
					options = new ArrayList<autoComplete>();
					options.add(new autoComplete(getText("processColorAction.manual"),"MANUAL"));
				}
			}
		}
		catch (SherColorException e){
			logger.error(e.getMessage(), e);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return SUCCESS;

	}
	
	private List<autoComplete> mapToOptions(List<CdsColorMast> colorList, String coType) {
		List<autoComplete> outList = new ArrayList<>();
		String theLabel;
		String theValue;
		
		if (colorList != null) {
			int index = 0;
			for (CdsColorMast item : colorList) {
				
				if (coType=="SW") {
					//only display locID if present.
					if (item.getLocId()==null) {
						theLabel = item.getColorId() + " " + item.getColorName();
					} else {
						theLabel = item.getColorId() + " " + item.getColorName() + " " +  item.getLocId();
					}
					theValue = item.getColorComp() + " " + item.getColorId();
				} else {
					theLabel = item.getColorComp() + " " + item.getColorId() + " " + item.getColorName();
					theValue = item.getColorComp() + " " + item.getColorId();
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
		
		try {
			colorData = URLDecoder.decode(colorData, "UTF-8");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		if (colorData.equals("")) {
			// The user typed nothing, so do nothing
		} 
		else if (colorData.equals("[]")){
			// The user typed a color id or name that does not exist
			setColorId(partialColorNameOrId);
			if (selectedCoTypes.equalsIgnoreCase("SW")) {
				setColorComp("SHERWIN-WILLIAMS");
			} else {
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
					if (partialColorNameOrId.equals(theValue)) {
						foundMatch = true;
						setColorId(data[0].replaceAll("colorNumber:", ""));
						setColorComp(data[1].replaceAll("companyName:", ""));
						break;
					}
				}
				if (foundMatch == false) {
					setColorId(partialColorNameOrId);
					if (selectedCoTypes.equalsIgnoreCase("SW")) {
						setColorComp("SHERWIN-WILLIAMS");
					} else {
						setColorComp("COMPETITIVE");
					}
				}
			} 
			// colordata does not contain JSON so it directly assigns the colordata
			// as the colorId and then uses the selectedCoTypes to assign the colorComp
			else {
				setColorId(colorData);
				if (selectedCoTypes.equalsIgnoreCase("SW")) {
					setColorComp("SHERWIN-WILLIAMS");
				} else {
					setColorComp("COMPETITIVE");
				}
			}	
		}
	}
	
	public void setSession(Map<String, Object> sessionMap) {
		this.setSessionMap(sessionMap);		
	}

	public Map<String, Object> getSessionMap() {
		return sessionMap;
	}

	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}

	public String getReqGuid() {
		return reqGuid;
	}

	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}

	public boolean isMatch() {
		return match;
	}

	public void setMatch(boolean match) {
		this.match = match;
	}

	public int getLookupControlNbr() {
		return lookupControlNbr;
	}

	public void setLookupControlNbr(int lookupControlNbr) {
		this.lookupControlNbr = lookupControlNbr;
	}
	
	public Map<String, String> getSourceOptions() {
		return sourceOptions;
	}

	public void setSourceOptions(Map<String, String> sourceOptions) {
		this.sourceOptions = sourceOptions;
	}
	
	public List<autoComplete> getOptions() {
		return options;
	}

	public void setOptions(List<autoComplete> options) {
		this.options = options;
	}
	
	public String getSelectedCoType() {
		return selectedCoType;
	}

	public void setSelectedCoType(String selectedCoType) {
		this.selectedCoType = selectedCoType;
	}
	
	public String getSelectedCoTypes() {
		return selectedCoTypes;
	}

	public void setSelectedCoTypes(String selectedCoTypes) {
		this.selectedCoTypes = selectedCoTypes;
	}
	
	public String getDefaultCoTypeValue() {
		return defaultCoTypeValue;
	}

	public void setDefaultCoTypeValue(String defaultCoTypeValue) {
		this.defaultCoTypeValue = defaultCoTypeValue;
	}

	public String getPartialColorNameOrId() {
		return partialColorNameOrId;
	}

	public void setPartialColorNameOrId(String partialColorNameOrId) {
		this.partialColorNameOrId = Encode.forHtmlContent(partialColorNameOrId);
	}
	
	public String getColorComp() {
		return colorComp;
	}

	public void setColorComp(String colorComp) {
		this.colorComp = colorComp;
	}

	public String getColorId() {
		return colorId;
	}

	public void setColorId(String colorId) {
		this.colorId = colorId;
	}

	public String getColorData(String colorData) {
		return colorData;
	}
	
	public void setColorData(String colorData) {
		this.colorData = colorData;
	}
	
	public List<String> getColorCompanies() {
		return colorCompanies;
	}
	
	public void setColorCompanies(List<String> colorCompanies) {
		this.colorCompanies = colorCompanies;
	}
	
	public String getSelectedCompany() {
		return selectedCompany;
	}
	
	public void setSelectedCompany(String selectedCompany) {
		this.selectedCompany = selectedCompany;
	}

	public boolean isMeasure() {
		return measure;
	}

	public void setMeasure(boolean measure) {
		this.measure = measure;
	}

	public boolean isCompare() {
		return compare;
	}

	public void setCompare(boolean compare) {
		this.compare = compare;
	}

}
