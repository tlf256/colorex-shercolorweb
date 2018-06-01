package com.sherwin.shercolor.customershercolorweb.web.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.hibernate.HibernateException;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.CustWebTran;
import com.sherwin.shercolor.common.domain.CustWebTranCorr;
import com.sherwin.shercolor.common.domain.FormulaInfo;
import com.sherwin.shercolor.common.domain.FormulaIngredient;
import com.sherwin.shercolor.common.service.FormulationService;
import com.sherwin.shercolor.common.service.TinterService;
import com.sherwin.shercolor.common.service.TranHistoryService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.util.domain.SwMessage;

public class SaveNewJobAction  extends ActionSupport  implements SessionAware, LoginRequired {

	private static final long serialVersionUID = 1L;

	static Logger logger = LogManager.getLogger(SaveNewJobAction.class.getName());
	private Map<String, Object> sessionMap;
	private String reqGuid;
	private FormulaInfo displayFormula;
	private int controlNbr;
	private int qtyDispensed;
	private String jsDateString;
	
	private int cycle; //correction cycle
	
	@Autowired
	TranHistoryService tranHistoryService;
	@Autowired
	FormulationService formulationService;
	@Autowired
	TinterService tinterService;
	
	public String execute(){
		String retVal = null;
		System.out.println("Inside SaveNewJobAction execute");
		CustWebTran custWebTran = new CustWebTran();
		
		try {
			//logger.info("About to get object from map");
			//logger.info("reqGuid is " + reqGuid);
			RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
			displayFormula = reqObj.getDisplayFormula();
			
			CustWebTran origCustWebTran = null;
			if(reqObj.getControlNbr()>0) origCustWebTran = tranHistoryService.readTranHistory(reqObj.getCustomerID(), reqObj.getControlNbr(), reqObj.getLineNbr());

			// map RequestObject to CustWebTran record format
			//logger.info("about to map reqObj to custWebTran ");
			custWebTran = mapRequestObjectToCustWebTranObject(reqObj,origCustWebTran);
			
			if(reqObj.getQuantityDispensed()>0) custWebTran.setTranCode("DISP");
			else custWebTran.setTranCode("SAVE");

			// Need to use local pc's date, it comes in on jsDateString in javascript format
			SimpleDateFormat jsdf = new SimpleDateFormat("EE MMM d y H:m:s 'GMT'Z (zz)");
			Date localeDateTime;
			if(jsDateString!=null && !jsDateString.isEmpty()){
				localeDateTime = jsdf.parse(jsDateString);
			} else {
				localeDateTime = new Date();
			}

			custWebTran.setLastTranDate(localeDateTime);

			//logger.info("determine saveNew or update. controlNbr is " + custWebTran.getControlNbr());
			if(custWebTran.getControlNbr()>0){
				this.controlNbr = custWebTran.getControlNbr(); //for ajax call to read
				// Update
				custWebTran.setInitTranDate(reqObj.getInitTranDate());
				//logger.info("its an update call updateTranHistory ");
				SwMessage errMsg = tranHistoryService.updateTranHistory(custWebTran);
				if(errMsg==null){
					reqObj.setControlNbr(custWebTran.getControlNbr());
					reqObj.setLineNbr(custWebTran.getLineNbr());
					reqObj.setLastTranDate(custWebTran.getLastTranDate());
					reqObj.setInitTranDate(custWebTran.getInitTranDate());
					SwMessage statusMsg = new SwMessage();
					statusMsg.setMessage("Successfully Updated Job Number " + custWebTran.getControlNbr());
					if(reqObj.getDisplayMsgs()==null) reqObj.setDisplayMsgs(new ArrayList<SwMessage>());
					reqObj.getDisplayMsgs().add(statusMsg);
					//addActionMessage("Successfully Updated Job Number " + custWebTran.getControlNbr());
					sessionMap.put(reqGuid, reqObj);
				    retVal = SUCCESS;
				} else {
					logger.error("Received updateTranTranHistory errMsg " + errMsg.getCode() + " " + errMsg.getMessage());
				    retVal = ERROR;
				}
			} else {
				// Create
				custWebTran.setInitTranDate(localeDateTime);
				// save job through tranHistoryService
				//logger.info("its a create call saveNewTranHistory ");
				SwMessage errMsg = tranHistoryService.saveNewTranHistory(custWebTran);
				if(errMsg==null){
					reqObj.setControlNbr(custWebTran.getControlNbr());
					reqObj.setLineNbr(custWebTran.getLineNbr());
					reqObj.setLastTranDate(custWebTran.getLastTranDate());
					reqObj.setInitTranDate(custWebTran.getInitTranDate());
					SwMessage statusMsg = new SwMessage();
					statusMsg.setMessage("Successfully Saved as Job Number " + custWebTran.getControlNbr());
					if(reqObj.getDisplayMsgs()==null) reqObj.setDisplayMsgs(new ArrayList<SwMessage>());
					reqObj.getDisplayMsgs().add(statusMsg);
					//addActionMessage("Successfully Saved as Job Number " + custWebTran.getControlNbr());
					sessionMap.put(reqGuid, reqObj);
					this.controlNbr = custWebTran.getControlNbr(); //for ajax call to read
				    retVal = SUCCESS;
				} else {
					logger.error("Received saveNewTranHistory errMsg " + errMsg.getCode() + " " + errMsg.getMessage());
				    retVal = ERROR;
				}
			}

		} catch (HibernateException he) {
			logger.error("HibernateException Caught: " + he.toString() + " " + he.getMessage());
			he.printStackTrace();
			retVal = ERROR;
		} catch (Exception e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage());
			e.printStackTrace();
			retVal = ERROR;
		}

		//logger.info("SaveNewJobAction returning " + retVal);
		return retVal;

	}

	public String saveOnPrint(){
		System.out.println("inside action to saveOnPrint");
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);

		System.out.println("inside action about to execute");
		String retVal = this.execute();
		System.out.println("inside action back from execute");
		
		return retVal;
	}
	
	public String bumpDispense(){
		// add 1 to quantityDispensed and put back in map for execute to do it's thing
		System.out.println("inside action to bumpDispense");
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		qtyDispensed = reqObj.getQuantityDispensed();   //for ajax call to read
		qtyDispensed++;
		System.out.println("inside action to bumpDispense, qtyDispensed will be " + qtyDispensed);
		reqObj.setQuantityDispensed(qtyDispensed);
		sessionMap.put(reqGuid, reqObj);

		System.out.println("inside action about to execute");
		String retVal = this.execute();
		System.out.println("inside action back from execute");
		
		return retVal;
	}
	
	public String mergeCorrWithStartForm(){
		String retVal;

		System.out.println("Inside mergeCorrWithStartForm");
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		//last container in the correction process, merge this cycle with current formula
		FormulaInfo currentFormula = reqObj.getDisplayFormula();
		
		//get dispensed correction formula for the cycle
		List<CustWebTranCorr> acceptedCorrThisCycle = tranHistoryService.getAcceptedCorrectionsForCycle(reqObj.getCustomerID(), reqObj.getControlNbr(), reqObj.getLineNbr(),cycle);

		//Build accepted formula, may be several steps
		int prevAcceptedContNbr = 0;
		List<FormulaIngredient> acceptedFormula = new ArrayList<FormulaIngredient>();
		for(CustWebTranCorr acceptedCorr : acceptedCorrThisCycle){
			if(acceptedCorr.getStatus().equalsIgnoreCase("ACCEPTED")){
				if(acceptedCorr.getUnitNbr()!=prevAcceptedContNbr) acceptedFormula.clear();
				List<FormulaIngredient> stepIngredientList = tranHistoryService.mapTranCorrClrntFieldsToIngredientList(acceptedCorr);
				for(FormulaIngredient addIngr : stepIngredientList){
					System.out.println("looking to add " + addIngr.getTintSysId() + " to acceptedFormula");
					boolean merged = false;
					for(FormulaIngredient totaledIngr : acceptedFormula){
						if(totaledIngr.getTintSysId().equalsIgnoreCase(addIngr.getTintSysId())){
							System.out.println("adding " + addIngr.getTintSysId() + " shots " + addIngr.getShots() + " to acceptedFormula " + totaledIngr.getShots());
							totaledIngr.setShots(totaledIngr.getShots()+addIngr.getShots());
							merged = true;
						}
					}
					if(!merged) acceptedFormula.add(addIngr);
				}
				prevAcceptedContNbr = acceptedCorr.getUnitNbr();
			}
		}
		
		//Build accepted formula with current formula
		if(acceptedFormula.size()>0) {
			List<FormulaIngredient> currentIngredients = currentFormula.getIngredients();
			for(FormulaIngredient addIngr : acceptedFormula){
				System.out.println("looking to add " + addIngr.getTintSysId() + " to currentFormula");
				boolean merged = false;
				for(FormulaIngredient totaledIngr : currentIngredients){
					if(totaledIngr.getTintSysId().equalsIgnoreCase(addIngr.getTintSysId())){
						System.out.println("adding " + addIngr.getTintSysId() + " shots " + addIngr.getShots() + " to acceptedFormula " + totaledIngr.getShots());
						totaledIngr.setShots(totaledIngr.getShots()+addIngr.getShots());
						merged = true;
					}
				}
				if(!merged) currentIngredients.add(addIngr);
			}
			formulationService.convertShotsToIncr(currentIngredients);
			currentFormula.setIngredients(currentIngredients);
		}
		
		//Update DB (tranCorr) set merged to true
		SwMessage updateResult = tranHistoryService.updateTranCorrectionMerged(reqObj.getCustomerID(), reqObj.getControlNbr(), reqObj.getLineNbr(), cycle);
		if(updateResult==null){
			if(!reqObj.getColorComp().equalsIgnoreCase("CUSTOM")){
				// change to custom and replace color id with word Manual
				reqObj.setColorComp("CUSTOM");
				reqObj.setColorID("MANUAL");
			}
			if(reqObj.isVinylExclude()){
				currentFormula.setSource("MANV");
				currentFormula.setSourceDescr("CUSTOM MANUAL VINYL SAFE MATCH");
			} else {
				currentFormula.setSource("MAN");
				currentFormula.setSourceDescr("CUSTOM MANUAL MATCH");
			}
			
			//Stuff it back in reqObj
			reqObj.setDisplayFormula(currentFormula);
			sessionMap.put(reqGuid, reqObj);
			//write it to DB (tran) by triggering savecorrmerge
			System.out.println("inside saveTranMergeWithCorr about to execute");
			retVal = this.execute();
			System.out.println("inside saveTranMergeWithCorr back from execute");
		} else {
			retVal = ERROR;
		}
		
		return retVal;
		
	}
	
	private CustWebTran mapRequestObjectToCustWebTranObject(RequestObject reqObj, CustWebTran origTran){
		System.out.println("Inside SaveNewJobAction mapRequestObjectToCustWebTranObject");
		CustWebTran custWebTran = new CustWebTran();
		custWebTran.setCustomerId(reqObj.getCustomerID());
		custWebTran.setControlNbr(reqObj.getControlNbr());
		custWebTran.setLineNbr(reqObj.getLineNbr());
		custWebTran.setColorType(reqObj.getColorType());
		custWebTran.setColorComp(reqObj.getColorComp());
		custWebTran.setColorId(reqObj.getColorID());
		custWebTran.setColorName(reqObj.getColorName());
		custWebTran.setPrimerId(reqObj.getPrimerId());
		custWebTran.setRgbHex(reqObj.getRgbHex());
		custWebTran.setSalesNbr(reqObj.getSalesNbr());
		custWebTran.setProdNbr(reqObj.getProdNbr());
		custWebTran.setSizeCode(reqObj.getSizeCode());
		custWebTran.setClrntSysId(reqObj.getClrntSys());
		custWebTran.setUserId(reqObj.getFirstName() + " " + reqObj.getLastName());
		custWebTran.setFormSource(reqObj.getDisplayFormula().getSource());
		custWebTran.setFormMethod(reqObj.getDisplayFormula().getSourceDescr());
		custWebTran.setInitTranDate(reqObj.getInitTranDate());
		custWebTran.setLastTranDate(reqObj.getLastTranDate());
		custWebTran.setVinylSafe(reqObj.isVinylExclude());
		
		//formula fields
		FormulaInfo displayFormula = reqObj.getDisplayFormula();
		int ctr=1;
		for(FormulaIngredient clrnt : displayFormula.getIngredients()){
			switch (ctr) {
			case 1: 
				custWebTran.setClrnt1(clrnt.getTintSysId());
				custWebTran.setClrntAmt1(clrnt.getShots());
				custWebTran.setShotSize(clrnt.getShotSize());
				break;
			case 2: 
				custWebTran.setClrnt2(clrnt.getTintSysId());
				custWebTran.setClrntAmt2(clrnt.getShots());
				break;
			case 3: 
				custWebTran.setClrnt3(clrnt.getTintSysId());
				custWebTran.setClrntAmt3(clrnt.getShots());
				break;
			case 4: 
				custWebTran.setClrnt4(clrnt.getTintSysId());
				custWebTran.setClrntAmt4(clrnt.getShots());
				break;
			case 5: 
				custWebTran.setClrnt5(clrnt.getTintSysId());
				custWebTran.setClrntAmt5(clrnt.getShots());
				break;
			case 6: 
				custWebTran.setClrnt6(clrnt.getTintSysId());
				custWebTran.setClrntAmt6(clrnt.getShots());
				break;
			case 7: 
				custWebTran.setClrnt7(clrnt.getTintSysId());
				custWebTran.setClrntAmt7(clrnt.getShots());
				break;
			case 8: 
				custWebTran.setClrnt8(clrnt.getTintSysId());
				custWebTran.setClrntAmt8(clrnt.getShots());
				break;
			}
			ctr++;
		}
		custWebTran.setFormPct(displayFormula.getPercent());
		
		if(displayFormula.getDeltaEs()==null) {
			Double[] des = new Double[3];
			Arrays.fill(des, 0D);
			displayFormula.setDeltaEs(des);
		}
		if(displayFormula.getDeltaEs().length>0) custWebTran.setDeltaEPrimary(displayFormula.getDeltaEs()[0]);
		if(displayFormula.getDeltaEs().length>1) custWebTran.setDeltaESecondary(displayFormula.getDeltaEs()[1]);
		if(displayFormula.getDeltaEs().length>2) custWebTran.setDeltaETertiary(displayFormula.getDeltaEs()[2]);
		
		if(displayFormula.getAverageDeltaE()==null) {
			custWebTran.setAverageDeltaE(0D);
		} else {
			custWebTran.setAverageDeltaE(displayFormula.getAverageDeltaE());
		}
		if(displayFormula.getContrastRatioThick()==null){
			custWebTran.setCrThick(0D);
		} else {
			custWebTran.setCrThick(displayFormula.getContrastRatioThick());
		}
		if(displayFormula.getContrastRatioThin()==null){
			custWebTran.setCrThin(0D);
		} else {
			custWebTran.setCrThin(displayFormula.getContrastRatioThin());
		}
		if(displayFormula.getColorantCost()==null){
			custWebTran.setEngDecisionValue(0D);
		} else {
			custWebTran.setEngDecisionValue(displayFormula.getColorantCost());
		}
		custWebTran.setFormulaRule(displayFormula.getRule());
		if(displayFormula.getIllums()!=null){
			if(displayFormula.getIllums().length>0)
				custWebTran.setIllumPrimary(displayFormula.getIllums()[0]);
			if(displayFormula.getIllums().length>1)
				custWebTran.setIllumSecondary(displayFormula.getIllums()[1]);
			if(displayFormula.getIllums().length>2)
				custWebTran.setIllumTertiary(displayFormula.getIllums()[2]);
		}
		custWebTran.setColorEngVer(displayFormula.getColorEngineVersion());
		if(displayFormula.getSpd()==null){
			custWebTran.setSpd(0D);
		} else {
			custWebTran.setSpd(displayFormula.getSpd());
		}
		if(displayFormula.getMetamerismIndex()==null){
			custWebTran.setMetamerismIndex(0D);
		} else {
			custWebTran.setMetamerismIndex(displayFormula.getMetamerismIndex());
		}
		if(displayFormula.getFormulationTime()==null){
			custWebTran.setFormulationTime(0D);
		} else {
			custWebTran.setFormulationTime(displayFormula.getFormulationTime());
		}
		
		double[] projCurve = new double[40];
		Arrays.fill(projCurve, 0D);
		if(displayFormula.getProjectedCurve()!=null && displayFormula.getProjectedCurve().length==40){
			for(int i=0;i<40;i++){
				projCurve[i] = displayFormula.getProjectedCurve()[i];
			}
		}
		custWebTran.setProjCurve(projCurve);
		
		double[] measCurve = new double[40];
		Arrays.fill(measCurve, 0D);
		if(displayFormula.getMeasuredCurve()!=null && displayFormula.getMeasuredCurve().length==40){
			for(int i=0;i<40;i++){
				measCurve[i] = displayFormula.getMeasuredCurve()[i];			}
		}
		custWebTran.setMeasCurve(measCurve);
		
		//custom job fields
		if(reqObj.getJobFieldList()!=null){
			if(reqObj.getJobFieldList().size()>0)
				custWebTran.setJobField01(reqObj.getJobFieldList().get(0).getEnteredValue());
			if(reqObj.getJobFieldList().size()>1)
				custWebTran.setJobField02(reqObj.getJobFieldList().get(1).getEnteredValue());
			if(reqObj.getJobFieldList().size()>2)
				custWebTran.setJobField03(reqObj.getJobFieldList().get(2).getEnteredValue());
			if(reqObj.getJobFieldList().size()>3)
				custWebTran.setJobField04(reqObj.getJobFieldList().get(3).getEnteredValue());
			if(reqObj.getJobFieldList().size()>4)
				custWebTran.setJobField05(reqObj.getJobFieldList().get(4).getEnteredValue());
			if(reqObj.getJobFieldList().size()>5)
				custWebTran.setJobField06(reqObj.getJobFieldList().get(5).getEnteredValue());
			if(reqObj.getJobFieldList().size()>6)
				custWebTran.setJobField07(reqObj.getJobFieldList().get(6).getEnteredValue());
			if(reqObj.getJobFieldList().size()>7)
				custWebTran.setJobField08(reqObj.getJobFieldList().get(7).getEnteredValue());
			if(reqObj.getJobFieldList().size()>8)
				custWebTran.setJobField09(reqObj.getJobFieldList().get(8).getEnteredValue());
			if(reqObj.getJobFieldList().size()>9)
				custWebTran.setJobField10(reqObj.getJobFieldList().get(9).getEnteredValue());
		}
		
		custWebTran.setQuantityDispensed(reqObj.getQuantityDispensed());

		if(origTran!=null){
			// if correction and orig values are empty, fill them with origTran record fields
			if(origTran.getQuantityDispensed()>0 && origTran.getOrigColorType()==null){
				//fill in orig values
				custWebTran.setOrigColorType(origTran.getColorType());
				custWebTran.setOrigColorComp(origTran.getColorComp());
				custWebTran.setOrigColorId(origTran.getColorId());
				custWebTran.setOrigFormMethod(origTran.getFormMethod());
				custWebTran.setOrigFormSource(origTran.getFormSource());
				custWebTran.setOrigClrnt1(origTran.getClrnt1());
				custWebTran.setOrigClrnt2(origTran.getClrnt2());
				custWebTran.setOrigClrnt3(origTran.getClrnt3());
				custWebTran.setOrigClrnt4(origTran.getClrnt4());
				custWebTran.setOrigClrnt5(origTran.getClrnt5());
				custWebTran.setOrigClrnt6(origTran.getClrnt6());
				custWebTran.setOrigClrnt7(origTran.getClrnt7());
				custWebTran.setOrigClrnt8(origTran.getClrnt8());
				custWebTran.setOrigClrntAmt1(origTran.getClrntAmt1());
				custWebTran.setOrigClrntAmt2(origTran.getClrntAmt2());
				custWebTran.setOrigClrntAmt3(origTran.getClrntAmt3());
				custWebTran.setOrigClrntAmt4(origTran.getClrntAmt4());
				custWebTran.setOrigClrntAmt5(origTran.getClrntAmt5());
				custWebTran.setOrigClrntAmt6(origTran.getClrntAmt6());
				custWebTran.setOrigClrntAmt7(origTran.getClrntAmt7());
				custWebTran.setOrigClrntAmt8(origTran.getClrntAmt8());
			}
		}
		return custWebTran;
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

	public FormulaInfo getDisplayFormula() {
		return displayFormula;
	}

	public void setDisplayFormula(FormulaInfo displayFormula) {
		this.displayFormula = displayFormula;
	}

	public int getControlNbr() {
		return controlNbr;
	}

	public int getQtyDispensed() {
		return qtyDispensed;
	}

	public void setJsDateString(String jsDateString) {
		this.jsDateString = Encode.forHtml(jsDateString);
	}

	public void setCycle(int cycle) {
		this.cycle = cycle;
	}

}
