package com.sherwin.shercolor.customershercolorweb.web.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.colormath.domain.ColorCoordinates;
import com.sherwin.shercolor.common.domain.CdsClrnt;
import com.sherwin.shercolor.common.domain.CustWebParms;
import com.sherwin.shercolor.common.domain.CustWebTran;
import com.sherwin.shercolor.common.domain.FormulaInfo;
import com.sherwin.shercolor.common.domain.FormulaIngredient;
import com.sherwin.shercolor.common.service.ColorService;
import com.sherwin.shercolor.common.service.ColorantService;
import com.sherwin.shercolor.common.service.CustomerService;
import com.sherwin.shercolor.common.service.FormulationService;
import com.sherwin.shercolor.common.service.TranHistoryService;
import com.sherwin.shercolor.customershercolorweb.web.model.JobField;
import com.sherwin.shercolor.customershercolorweb.web.model.ManualIngredient;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.util.domain.SwMessage;

public class ProcessManualFormulaAction extends ActionSupport implements SessionAware, LoginRequired {
	private Map<String, Object> sessionMap;
	
	private static final long serialVersionUID = 1L;
	static Logger logger = LogManager.getLogger(ProcessManualFormulaAction.class.getName());
	
	private String reqGuid;
	private FormulaInfo displayFormula;
	private int percentOfFormula;
	private List<ManualIngredient> ingredientList;
	private List<String> availClrntNameId;
	private String colorId;
	private String colorName;
	private List<JobField> jobFields;
	private int recDirty;
	private boolean userWarningOverride = false;
	private boolean adjByPercentVisible;
	private List<String> previousWarningMessages;
	private String selectedColorantFocus ="MfUserNextAction_ingredientList_0__selectedColorant";
	

	private boolean debugOn = false;
	
	@Autowired
	TranHistoryService tranHistoryService;
	
	@Autowired
	ColorantService colorantService;
	
	@Autowired
	FormulationService formulationService;
	
	@Autowired
	CustomerService customerService;
	
	@Autowired
	ColorService colorService;
	
	public String display() {
		
		try {
			recDirty=1;
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			 
			jobFields = reqObj.getJobFieldList();
			 
			if(!reqObj.getColorComp().equalsIgnoreCase("CUSTOM")){
				// change to custom and replace color id with word Manual
				reqObj.setColorComp("CUSTOM");
				reqObj.setColorID("MANUAL");
			}
			 
			colorId = reqObj.getColorID();
			colorName = reqObj.getColorName();
			 
			displayFormula = new FormulaInfo();
			if(reqObj.isVinylExclude()){
				displayFormula.setSource("MANV");
				displayFormula.setSourceDescr("CUSTOM MANUAL VINYL SAFE MATCH");
			} else {
				displayFormula.setSource("MAN");
				displayFormula.setSourceDescr("CUSTOM MANUAL MATCH");
			}
			displayFormula.setClrntSysId(reqObj.getClrntSys());
			 
			// setup header
			List<String> incrHdr = colorantService.getColorantIncrementHeader(reqObj.getClrntSys());
			 
			displayFormula.setIncrementHdr(incrHdr);
			 
			//Setup blank edit formula (8 ingredients, all zeroes)
			//List<FormulaIngredient> editIngredients = new ArrayList<FormulaIngredient>();
			List<String> incrs = new ArrayList<String>();
			incrs.add("0");
			incrs.add("0");
			incrs.add("0");
			if(incrHdr.size()>3) incrs.add("0");

			ingredientList = new ArrayList<ManualIngredient>();
			for(int i=0; i<8; i++){
				ManualIngredient item = new ManualIngredient();
				 
				item.setIncrements(incrs);
				 
				ingredientList.add(item);
			}
			 
			logger.debug("in display,DONE  building ingredientList, size is " + ingredientList.size());
			if(reqObj.getDisplayFormula()!=null){
				// Merge current formula values into ingredientList
				FormulaInfo currentFormula = reqObj.getDisplayFormula();
				for(int i=0;i<currentFormula.getIngredients().size();i++){
					ingredientList.get(i).setSelectedColorant(currentFormula.getIngredients().get(i).getTintSysId()+"-"+currentFormula.getIngredients().get(i).getName());
					List<String> currIncr = new ArrayList<String>();
					for(int j=0;j<currentFormula.getIngredients().get(i).getIncrement().length;j++){
						currIncr.add(String.valueOf(currentFormula.getIngredients().get(i).getIncrement()[j]));
						//ingredientList.get(i).getIncrements().set(j, String.valueOf(currentFormula.getIngredients().get(i).getIncrement()[j]));
					 }
					ingredientList.get(i).setIncrements(currIncr);
				 }
				//set the focus variable name to ingredients + 1.  If there are 8 ingredients, leave set to 0.
				if (currentFormula.getIngredients().size() < 8 && currentFormula.getIngredients().size() != 0 ) {
					selectedColorantFocus ="MfUserNextAction_ingredientList_" + currentFormula.getIngredients().size() + "__selectedColorant";
				}
				
				displayFormula.setIngredients(reqObj.getDisplayFormula().getIngredients());
			}
			 
			//editFormula.setIngredients(editIngredients);
			
			
			//Check if we have a record for Formula and Display Formula present, if neither found, no Adjust by Percent option
			CustWebTran webTran = tranHistoryService.readTranHistory(reqObj.getCustomerID(), reqObj.getControlNbr(), 1);
			
			if (webTran == null && reqObj.getDisplayFormula() == null) {
				logger.debug("Formula not saved or display formula null, no Adjust By Percent");
				adjByPercentVisible = true;
			}
	 
			// setup colorant list for drop down
			fillColorantList(reqObj);

			reqObj.setDisplayFormula(displayFormula);
				
			sessionMap.put(reqGuid, reqObj);

			 
		} catch (Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			return ERROR;
		}
		
		 return INPUT;
	}
	
	private void fillColorantList(RequestObject reqObj){
		// Make sure to get vinyl restricted list and product restricted list
		List<CdsClrnt> clrntList = colorantService.getColorantList(reqObj.getClrntSys(), reqObj.getSalesNbr(), reqObj.isVinylExclude());
		availClrntNameId = new ArrayList<String>();
		for(CdsClrnt clrnt : clrntList){
			availClrntNameId.add(clrnt.getTintSysId() + "-" + clrnt.getName());
		}
	}
	
	public String percentAdjustment(){
		String retVal;
		
		try{
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			// get displayFormula and apply percent of formula, return ingredientList
			
			displayFormula = reqObj.getDisplayFormula();
			
			logger.debug("in percentAdjustment, origFormula ingredient count returned is " + displayFormula.getIngredients().size());
			for(FormulaIngredient item : displayFormula.getIngredients()){
				logger.debug("item " + item.getTintSysId() + " " + item.getName() + " " + Arrays.toString(item.getIncrement()));
			}
			
			logger.debug("in percentAdjustment, about to call scaleByPercent");
			displayFormula = formulationService.scaleFormulaByPercent(displayFormula, percentOfFormula);
			
			logger.debug("in percentAdjustment, new Formula ingredient count returned is " + displayFormula.getIngredients().size());
			for(FormulaIngredient item : displayFormula.getIngredients()){
				logger.debug("item " + item.getTintSysId() + " " + item.getName() + " " + Arrays.toString(item.getIncrement()));
			}
			
			reqObj.setDisplayFormula(displayFormula);
			
			sessionMap.put(reqGuid, reqObj);

			retVal = SUCCESS;
		} catch (Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			System.err.println("Exepction thrown!, Percent Addition Calculation Failed");
			retVal = ERROR;
		}

		return retVal;
	}
	
	public String execute() {
		String retVal = SUCCESS;
		try{
			logger.debug("Start of execute, reqGuid is " + reqGuid);
			recDirty=1;
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			logger.debug("successfully read reqObj");

			reqObj.setColorID(colorId);
			reqObj.setColorName(colorName);
			// Fill in more display formula attributes
			reqObj.getDisplayFormula().setSalesNbr(reqObj.getSalesNbr());
			reqObj.getDisplayFormula().setColorComp(reqObj.getColorComp());
			reqObj.getDisplayFormula().setColorId(reqObj.getColorComp());
			reqObj.getDisplayFormula().setProdNbr(reqObj.getProdNbr());
			reqObj.getDisplayFormula().setSizeCode(reqObj.getSizeCode());

			//check user input, first make sure all positive integers used...
			boolean positiveValueEntered = false;
			boolean badValueEntered = false;
			logger.debug("ready to look through ingredientlist");
			for(ManualIngredient item : ingredientList){
				positiveValueEntered = false;
				badValueEntered = false;
				for(int i = 0; i< item.getIncrements().size(); i++){
					if(item.getIncrements().get(i).trim().isEmpty() || item.getIncrements().get(i).trim().equals("-")){
						logger.debug("empty increment or single hyphen, force 0");
						item.getIncrements().set(i, "0");
					} else {
						//this increment not empty, not hyphen...
						try {
							int testInt = Integer.parseInt(item.getIncrements().get(i));
							if(testInt>0){
								logger.debug("increment greater than 0, OK");
								positiveValueEntered = true;
							} else if (testInt<0) {
								logger.debug("increment is less than 0, return error");
								badValueEntered = true;
							} else {
								logger.debug("increment is 0, OK");
								item.getIncrements().set(i, "0");
							}
						} catch (NumberFormatException e) {
							// not a number
							logger.debug("increment value not a number, return error");
							badValueEntered = true;
						}
					}
				}
				// done with increment values, what did we see?
				if(badValueEntered){
					if(item.getSelectedColorant().equalsIgnoreCase("-1")){
						// Invalid Increment, no colorant selected, what to do?
					} else {
						addActionError("Invalid increment value(s) entered for " + item.getSelectedColorant());
						retVal = INPUT;
					}
				} else if(positiveValueEntered){
					// value entered, did they select a colorant?
					if(item.getSelectedColorant().equalsIgnoreCase("-1")){
						// No colorant selected, Error to user
						addActionError("Increment value(s) entered but no colorant selected. Please select a colorant or change the values to zero.");
						retVal = INPUT;
					}
				} else {
					//no value entered, did they select a colorant?
					if(!item.getSelectedColorant().equalsIgnoreCase("-1")){
						// Colorant selected, Error to user
						addActionError("No increment value(s) entered for " + item.getSelectedColorant());
						retVal = INPUT;
					}
				}
			}// end looping ManualIgredients and checking positive integers enteres

			// second part of basic entry processing, make a list of ingredients entered and populate ingredient objects
			if(retVal.equalsIgnoreCase(SUCCESS)){
				//Build new ingredients list...
				List<FormulaIngredient> newIngredients = new ArrayList<FormulaIngredient>();
				List<String> newClrntCheck = new ArrayList<String>();
				List<String> dupClrntWarnList = new ArrayList<String>();
				
				for(ManualIngredient item : ingredientList){
					if(!item.getSelectedColorant().equalsIgnoreCase("-1")){
						// they selected a colorant
						logger.debug("they selected a colorant, add to new");
						FormulaIngredient addMe = new FormulaIngredient();
						addMe.setClrntSysId(reqObj.getClrntSys());
						addMe.setTintSysId(item.getSelectedColorant().split("-")[0]);
						int i = 0;
						int[] incrs = new int[item.getIncrements().size()];
						for(String amt : item.getIncrements()){
							try {
								incrs[i]=Integer.parseInt(amt.trim());
								//if(incrs[i]<0) incrs[i]=0;
							} catch (NumberFormatException e) {
								incrs[i]=0;
							}
							i++;
						}
						logger.debug("adding clrnt/incrs to new formula" + addMe.getClrntSysId() + " " + addMe.getTintSysId() + " " + Arrays.toString(incrs));
						addMe.setIncrement(incrs);
						newIngredients.add(addMe);

						// dup clrnt check
						if(newClrntCheck.size()<1){
							//first clrnt, add to list
							newClrntCheck.add(addMe.getTintSysId());
						} else {
							// check duplicate check list to see if this clrnt is already there
							if(newClrntCheck.contains(addMe.getTintSysId())){
								// this is a dup clrnt, add this to the list of duplicate colorant entries
								// when adding to list of dup colorants, check if this colorant is already being reported as dup, if so, don't tell them a 2nd, 3rd, 4th, etc time
								logger.debug("Duplicate Colorant Found");
								if(dupClrntWarnList.size()>0){
									if(!dupClrntWarnList.contains(addMe.getTintSysId())){
										// dup not yet reported for this colorant, add to list
										logger.debug("not already on dup warn list, add it");
										dupClrntWarnList.add(addMe.getTintSysId());
									} else { 
										logger.debug("already on dup warn list, skip it");
									}
								} else {
									// first dup reported, just add
									dupClrntWarnList.add(addMe.getTintSysId());
									logger.debug("dup warn list empty, add it");
								}
							} else {
								// no dupliate, add to running list of colorant to check against
								newClrntCheck.add(addMe.getTintSysId());
							}
						} // end dup checking
					} // end they selected a colorant from drop down
				}// end looping ManualIgredients

				// any duplicate warnings are reported as errors so user will have to correct before moving on...
				if(dupClrntWarnList.size()>0){
					logger.debug("dup warn list has " + dupClrntWarnList.size() + " entries");
					for(String dupClrnt : dupClrntWarnList){
						addActionError("Colorant " + dupClrnt + " appears more than one time in your formula. Please correct.");
					}
					retVal = INPUT;
				}
				if(newIngredients.size()>0){
					if(formulationService.convertIncrToShots(newIngredients)){
						// now make incr from shots...
						if(formulationService.convertShotsToIncr(newIngredients)){ 
							if(formulationService.fillIngredientInfoFromTintSysId(newIngredients)){
								//reqObj.getDisplayFormula().setIngredients(newIngredients);
								displayFormula = reqObj.getDisplayFormula();
								displayFormula.setIngredients(newIngredients);
							} else {
								// Handle failure to fill ingredient info from cdsClrnt tables
								addActionError("Invalid Formula Entered");
								retVal = INPUT;
							}
						} else {
							// Handle failure to convert Shots to Incr
							addActionError("Invalid Formula Entered");
							retVal = INPUT;
						}
					} else {
						// Handle failure to convert Increments to Shots
						addActionError("Invalid Formula Entered");
						retVal = INPUT;
					}
				} else {
					// No ingredients entered by user, error back to user
					addActionError("No colorants or amounts have been entered. You must select at least one colorant and enter an amount for the formula to be accepted.");
					retVal = INPUT;
				}
			} // end second part of basic entry processing

			// if no basic screen entry errors, go check for product/colorant combination errors
			if(retVal.equalsIgnoreCase(SUCCESS)){
				// check for manual formula errors/warnings/info messages
				List<SwMessage> allMsgs = formulationService.manualFormulationWarnings(reqObj.getDisplayFormula());

				// first check if any new warnings
				boolean alreadyWarned = false;
				int newWarningCount = 0;
				for(SwMessage item:allMsgs) {
					if(item.getSeverity().isInRange(Level.WARN, Level.WARN)){
						// check if already in list that has been overridden, if so don't put in current list
						alreadyWarned = false;
						if(previousWarningMessages!=null){
							for(String prevMsg : previousWarningMessages){
								if(prevMsg.equalsIgnoreCase(item.getMessage())){
									alreadyWarned = true;
								}
							}
						}
						if(!alreadyWarned) newWarningCount+=1;
					}
				}
				
				List<String> currentWarningMessages = new ArrayList<String>();  //list of all warnings on this pass
				for(SwMessage item:allMsgs) {
					logger.debug("in execute, adding action message " + item.getSeverity() + " " + item.getMessage());
					if(item.getSeverity().isInRange(Level.FATAL,Level.ERROR)){
						//addFieldError("reqGuid", item.getMessage());
						addActionError(item.getMessage());
						retVal = INPUT;
					} else {
						if(item.getSeverity().isInRange(Level.WARN, Level.WARN)){
							// check if already in list that has been overridden, if so don't put in current list
							if(!userWarningOverride || newWarningCount>0){
								// user hasn't seen this yet or didn't choose override, show it
								logger.debug("it is a warning, show in action message");
								addActionMessage(item.getMessage());
								currentWarningMessages.add(item.getMessage());
								retVal = INPUT; // show same screen with warnings to override
							}
						}
					}
				}
				if(previousWarningMessages!=null){
					previousWarningMessages.clear();
				} else {
					previousWarningMessages = new ArrayList<String>();
				}
				if(currentWarningMessages.size()>0){
					for(String warning : currentWarningMessages){
						previousWarningMessages.add(warning);
					}
					userWarningOverride = false;  // reset checkbox to unchecked
				}
				//reqObj.setDisplayMsgs(allMsgs); nothing carries forward, all messages return "INPUT" to keep them on the screen
				
				//and post the validation message list to the request object for use in printing.
				List<SwMessage> canLabelMsgs = formulationService.canLabelFormulationWarnings(displayFormula);
				reqObj.setCanLabelMsgs(canLabelMsgs);
				
			} // end checking for product/colorant errors/warnings
			

			if(retVal.equalsIgnoreCase(SUCCESS)){
				String rgbHex = null;
				BigDecimal[] curveArray = new BigDecimal[40];
				CustWebParms  custWebParms = customerService.getDefaultCustWebParms(reqObj.getCustomerID()); 
				custWebParms.setClrntSysId(reqObj.getClrntSys());
				// all looks good. go project curve and update reqObj rgbhex
				Double[] projCurve = formulationService.projectCurve(displayFormula, custWebParms);
				if(projCurve!=null){
					boolean allZero = true;
					for (int i=0; i < 40; i++) {
						if(projCurve[i]!=0D) allZero = false;
						curveArray[i] = new BigDecimal(projCurve[i]);
					}
				
					if(!allZero){
						// Try getting an RGB value for the object.
						ColorCoordinates colorCoord = colorService.getColorCoordinates(curveArray, "D65");
						if (colorCoord != null) rgbHex = colorCoord.getRgbHex();

						if(rgbHex!=null) reqObj.setRgbHex(rgbHex);
					}
				}
				
				if(reqObj.getControlNbr()>0) {
					SwMessage saveReminder = new SwMessage();
					saveReminder.setSeverity(Level.INFO);
					saveReminder.setMessage("Changes have been made but not yet saved");
					if(reqObj.getDisplayMsgs()==null) reqObj.setDisplayMsgs(new ArrayList<SwMessage>());
					reqObj.getDisplayMsgs().add(saveReminder);
				}
			}

			logger.debug("putting map back into session");
			sessionMap.put(reqGuid, reqObj);
			
			if(retVal.equalsIgnoreCase(INPUT)){
				fillColorantList(reqObj);
			}
			
			
		} catch (Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			logger.error(e);
			retVal = ERROR;
		}
		return retVal;
		 
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

	public FormulaInfo getDisplayFormula() {
		return displayFormula;
	}
	public void setDisplayFormula(FormulaInfo displayFormula) {
		this.displayFormula = displayFormula;
	}

	public List<String> getAvailClrntNameId() {
		return availClrntNameId;
	}

	public void setAvailClrntNameId(List<String> availClrntNameId) {
		this.availClrntNameId = availClrntNameId;
	}

	public List<ManualIngredient> getIngredientList() {
		return ingredientList;
	}

	public void setIngredientList(List<ManualIngredient> ingredientList) {
		this.ingredientList = ingredientList;
	}

	public String getColorId() {
		return colorId;
	}

	public String getColorName() {
		return colorName;
	}

	public void setColorId(String colorId) {
		this.colorId = colorId;
	}

	public void setColorName(String colorName) {
		this.colorName = colorName;
	}

	public List<JobField> getJobFields() {
		return jobFields;
	}

	public void setJobFields(List<JobField> jobFields) {
		this.jobFields = jobFields;
	}

	public int getRecDirty() {
		return recDirty;
	}

	public void setRecDirty(int recDirty) {
		this.recDirty = recDirty;
	}

	public boolean isUserWarningOverride() {
		return userWarningOverride;
	}

	public void setUserWarningOverride(boolean userWarningOverride) {
		this.userWarningOverride = userWarningOverride;
	}

	public List<String> getPreviousWarningMessages() {
		return previousWarningMessages;
	}

	public void setPreviousWarningMessages(List<String> previousWarningMessages) {
		this.previousWarningMessages = previousWarningMessages;
	}

	public String getSelectedColorantFocus() {
		return selectedColorantFocus;
	}

	public void setSelectedColorantFocus(String selectedColorantFocus) {
		this.selectedColorantFocus = Encode.forHtml(selectedColorantFocus);
	}

	public boolean isAdjByPercentVisible() {
		return adjByPercentVisible;
	}

	public void setPercentOfFormula(int percentOfFormula) {
		this.percentOfFormula = percentOfFormula;
	}
	
	
}
