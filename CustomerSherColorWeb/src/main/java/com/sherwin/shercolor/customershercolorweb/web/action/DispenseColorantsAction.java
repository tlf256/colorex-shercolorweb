package com.sherwin.shercolor.customershercolorweb.web.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

//import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.beans.factory.annotation.Autowired;

import com.opensymphony.xwork2.ActionSupport;
import com.sherwin.shercolor.common.domain.FormulaIngredient;
import com.sherwin.shercolor.common.service.ColorantService;
import com.sherwin.shercolor.common.service.FormulationService;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterCanister;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterInfo;

@SuppressWarnings("serial")
public class DispenseColorantsAction extends ActionSupport implements SessionAware, LoginRequired{
	
	static Logger logger = LogManager.getLogger(DispenseColorantsAction.class);
	private String reqGuid;
	private Map<String, Object> sessionMap;
	private TinterInfo tinter;
	private List<String> incrHdr;
	private int[] incrementArray;
	private List<TinterCanister> sortedCanList;
	private int shots;
	private int UOM;
	
	@Autowired
	ColorantService colorantService;
	
	@Autowired
	FormulationService formulationService;

	public String display() {
		//Creating initial header 
		
		try {
			if(!StringUtils.isEmpty(reqGuid)) {
				RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
				tinter = reqObj.getTinter();
				reqObj.setClrntSys(tinter.getClrntSysId());
				setUOM(Integer.parseInt(colorantService.getColorantSystem(tinter.getClrntSysId()).getClrntShotSz()));
				
				//Header setup
				setIncrHdr(colorantService.getColorantIncrementHeader(reqObj.getClrntSys()));
				
				//Creating sorted canisterList for display
				if (!tinter.getCanisterList().isEmpty()) {
					
					setSortedCanList(new ArrayList<TinterCanister>(tinter.getCanisterList()));
					
				    Collections.sort(sortedCanList, new Comparator<TinterCanister>() {
				        @Override
				        public int compare(TinterCanister c1, TinterCanister c2) {
				        	if(StringUtils.isNotBlank(c1.getClrntName()) && StringUtils.isNotBlank(c2.getClrntName())){
				        		return c1.getClrntName().compareTo(c2.getClrntName());
				        	}
				        	else return 0;
				        }
				    });
				}
			}
			else logger.error("reqGuid is empty");
		}
		catch (RuntimeException e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage() + e.getCause(), e);
			return ERROR;
		}
		
		return SUCCESS;
	}
	
	public String convert() {
		RequestObject reqObj = (RequestObject) sessionMap.get(reqGuid);
		tinter = reqObj.getTinter();
		setUOM(Integer.parseInt(colorantService.getColorantSystem(tinter.getClrntSysId()).getClrntShotSz()));
		
		try {		
			//Create Formula Ingredient list
			List<FormulaIngredient> ingredients = new ArrayList<FormulaIngredient>();
			FormulaIngredient ingredient = new FormulaIngredient();
			ingredient.setClrntSysId(tinter.getClrntSysId());
			ingredient.setIncrement(incrementArray);
			ingredients.add(ingredient);
			
			//Convert increments to shots, set shots
			
			if(formulationService.convertIncrToShots(ingredients)) {
				setShots(ingredients.get(0).getShots());
				return SUCCESS;
			}
			else {
				logger.error("formulaService.convertIncrToShots - Conversion failed.");
				return ERROR;
			}
		}
		catch (RuntimeException e) {
			logger.error("Exception Caught: " + e.toString() +  " " + e.getMessage(), e);
			return ERROR;
		}
	}
	

	public String getReqGuid() {
		return reqGuid;
	}
	public void setReqGuid(String reqGuid) {
		this.reqGuid = reqGuid;
	}
	public void setSessionMap(Map<String, Object> sessionMap) {
		this.sessionMap = sessionMap;
	}
	public void setSession(Map<String, Object> sessionMap) {
		this.setSessionMap(sessionMap);
	}
	public TinterInfo getTinter() {
		return tinter;
	}
	public void setIncrementArray(int[] incrementArray) {
		this.incrementArray = incrementArray;
	}
	public int getShots() {
		return shots;
	}

	public List<String> getIncrHdr() {
		return incrHdr;
	}

	public void setIncrHdr(List<String> incrHdr) {
		this.incrHdr = incrHdr;
	}

	public int getUOM() {
		return UOM;
	}

	public void setShots(int shots) {
		this.shots = shots;
	}
	
	public void setUOM(int uOM) {
		UOM = uOM;
	}

	public List<TinterCanister> getSortedCanList() {
		return sortedCanList;
	}

	public void setSortedCanList(List<TinterCanister> sortedCanList) {
		this.sortedCanList = sortedCanList;
	}
	
}
