package com.sherwin.shercolor.customershercolorweb.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.sherwin.shercolor.common.domain.CustWebColorantsTxt;
import com.sherwin.shercolor.common.domain.CustWebTranCorr;
import com.sherwin.shercolor.common.domain.FormulaIngredient;
import com.sherwin.shercolor.common.service.TinterService;
import com.sherwin.shercolor.common.service.TranHistoryService;
import com.sherwin.shercolor.customershercolorweb.web.model.CorrectionInfo;
import com.sherwin.shercolor.customershercolorweb.web.model.CorrectionStep;
import com.sherwin.shercolor.customershercolorweb.web.model.DispenseItem;
import com.sherwin.shercolor.customershercolorweb.web.model.RequestObject;
import com.sherwin.shercolor.customershercolorweb.web.model.TinterInfo;

@Service
public class CorrectionInfoBuilderImpl implements CorrectionInfoBuilder{
	static Logger logger = LogManager.getLogger(CorrectionInfoBuilder.class.getName());

	TranHistoryService tranHistoryService;
	TinterService tinterService;
	
	public CorrectionInfoBuilderImpl(TranHistoryService tranHistoryService, TinterService tinterService){
		this.tranHistoryService = tranHistoryService;
		this.tinterService = tinterService;
	}

	public CorrectionInfo getCorrectionInfo(RequestObject reqObj, List<CustWebTranCorr> tranCorrList){
		logger.trace("Inside CorrBuilder.getCorrInfo");
		CorrectionInfo correction = new CorrectionInfo();
		List<DispenseItem> dispenseItemList = new ArrayList<DispenseItem>();;
		List<DispenseItem> openDispItemList = new ArrayList<DispenseItem>();;
		correction.setCorrectionStepList(new ArrayList<CorrectionStep>());
		
		TinterInfo tinter = reqObj.getTinter();
		HashMap<String,CustWebColorantsTxt> colorantMap = tinterService.getCanisterMap(reqObj.getCustomerID(), tinter.getClrntSysId(), tinter.getModel(), tinter.getSerialNbr());
		
		if(tranCorrList.size()==0){
			correction.setNextUnitNbr(1);
			correction.setCycle(1);
			correction.setLastStep(0);
			correction.setCorrStatus("NONE");
			correction.setAcceptedContNbr(0);
		} else {
			int maxQty=reqObj.getQuantityDispensed();
			Map<Integer,Integer> discardedMap = new HashMap<Integer,Integer>();
			Map<Integer,Integer> skippedMap = new HashMap<Integer,Integer>();
			
			int currentAcceptedCycle = 0; 
			int acceptedUnitNbr = 0;
			List<FormulaIngredient> acceptedIngredients = new ArrayList<FormulaIngredient>();
			int prevAcceptedUnitNbr = 0; // use for tracking when Accepted unitNbr changes while we are looping
			int prevOpenUnitNbr = 0; // use for tracking when Open unitNbr changes while we are looping
			//map to correctionHistory model
			for(CustWebTranCorr tranCorr : tranCorrList){
				CorrectionStep addMe = new CorrectionStep();
				addMe.setCycle(tranCorr.getCycle());
				addMe.setUnitNbr(tranCorr.getUnitNbr());
				addMe.setStep(tranCorr.getStep());
				addMe.setReason(tranCorr.getReason());
				addMe.setStatus(tranCorr.getStatus());
				addMe.setUserId(tranCorr.getUserId());
				SimpleDateFormat jsdf = new SimpleDateFormat("EE MMM d y H:m:s 'GMT'Z (zz)");
				addMe.setJsDateTime(jsdf.format(tranCorr.getDateTime()));
				addMe.setDateTime(tranCorr.getDateTime());
				addMe.setCorrMethod(tranCorr.getCorrMethod());
				addMe.setMergedWithOrig(tranCorr.isMergedWithOrig());
				addMe.setClrntSysId(tranCorr.getClrntSysId());
				// convert clrnt items to ingredients
				List<FormulaIngredient> ingredients = tranHistoryService.mapTranCorrClrntFieldsToIngredientList(tranCorr);
				//Prevents first cycle's formula ingredients from being overwritten with the last element in the list.
				List<FormulaIngredient> sCopy = new ArrayList<>(ingredients);
				addMe.setIngredients(sCopy);
				// Store accepted correction formula and set boolean
				if (tranCorr.getStatus().equalsIgnoreCase("ACCEPTED") && (acceptedUnitNbr == 0 || acceptedUnitNbr == tranCorr.getUnitNbr())) {
					if (currentAcceptedCycle != tranCorr.getCycle()) {
						acceptedIngredients.clear();
						currentAcceptedCycle = tranCorr.getCycle();
					}
					if (tranCorr.getUnitNbr() == acceptedUnitNbr) {
						for (FormulaIngredient ingredient : ingredients) {
							acceptedIngredients.add(ingredient);
						} 	
					} else {
						acceptedIngredients = ingredients;
					}
					
					acceptedUnitNbr = tranCorr.getUnitNbr();
				}
				
				if(tranCorr.getStatus().equalsIgnoreCase("OPEN")){
					correction.setCorrStatus("MIDUNIT");
					correction.setCycle(tranCorr.getCycle());
					correction.setNextUnitNbr(tranCorr.getUnitNbr());
					dispenseItemList.clear();
					correction.setAcceptedContNbr(0);
					if(prevOpenUnitNbr!=tranCorr.getUnitNbr()) openDispItemList.clear();
					for(FormulaIngredient ingr : ingredients){
						DispenseItem dispItem = new DispenseItem();
						dispItem.setClrntCode(ingr.getTintSysId());
						dispItem.setUom(ingr.getShotSize());
						dispItem.setShots(ingr.getShots());
						if(colorantMap!=null && colorantMap.containsKey(ingr.getTintSysId())) dispItem.setPosition(colorantMap.get(ingr.getTintSysId()).getPosition());
						// append to list or add to item with same colorant code
						boolean merged = false;
						for(DispenseItem checkMe : openDispItemList){
							if(checkMe.getClrntCode().equalsIgnoreCase(ingr.getTintSysId())){
								checkMe.setShots(checkMe.getShots()+ingr.getShots());
								merged = true;
								break;
							}
						}//end checkMe loop
						if(!merged) openDispItemList.add(dispItem);
					} // end ingredient loop
					prevOpenUnitNbr = tranCorr.getUnitNbr();
				} else {
					if(tranCorr.getStatus().equalsIgnoreCase("DISCARDED") || tranCorr.getStatus().equalsIgnoreCase("PREVIOUSLY DISCARDED")){
						// Force accepted formula reset as this status marks that the formula has been thrown away
						acceptedIngredients.clear();
						acceptedUnitNbr = 0;
						if(!discardedMap.containsKey(tranCorr.getUnitNbr())) discardedMap.put(tranCorr.getUnitNbr(), tranCorr.getCycle());
					}
					if(tranCorr.getStatus().equalsIgnoreCase("SKIPPED") || tranCorr.getStatus().equalsIgnoreCase("PREVIOUSLY SKIPPED")){
						if(!skippedMap.containsKey(tranCorr.getUnitNbr())) skippedMap.put(tranCorr.getUnitNbr(), tranCorr.getCycle());
					}
					// not open
					if(maxQty==tranCorr.getUnitNbr()){
						// it is last canister
						correction.setCorrStatus("NEWCYCLE");
						correction.setCycle(tranCorr.getCycle() + 1);
						correction.setNextUnitNbr(1);
						dispenseItemList.clear();
						openDispItemList.clear();
						correction.setAcceptedContNbr(0);
					} else {
						correction.setCorrStatus("MIDCYCLE");
						correction.setCycle(tranCorr.getCycle());
						correction.setNextUnitNbr(tranCorr.getUnitNbr() + 1);
						//any accepted steps? if so, put in dispenseItemsList for next container
						
						//if(tranCorr.getStatus().equalsIgnoreCase("ACCEPTED")){
						if(acceptedUnitNbr != 0) {
							dispenseItemList.clear();
							correction.setAcceptedContNbr(acceptedUnitNbr);
							for(FormulaIngredient ingr : acceptedIngredients){
								DispenseItem dispItem = new DispenseItem();
								dispItem.setClrntCode(ingr.getTintSysId());
								dispItem.setUom(ingr.getShotSize());
								dispItem.setShots(ingr.getShots());
								if(colorantMap!=null && colorantMap.containsKey(ingr.getTintSysId())) dispItem.setPosition(colorantMap.get(ingr.getTintSysId()).getPosition());
								// append to list or add to item with same colorant code
								boolean merged = false;
								for(DispenseItem checkMe : dispenseItemList){
									if(checkMe.getClrntCode().equalsIgnoreCase(ingr.getTintSysId())){
										checkMe.setShots(checkMe.getShots()+ingr.getShots());
										merged = true;
										break;
									}
								}//end checkMe loop
								if(!merged) dispenseItemList.add(dispItem);
							} // end ingredient loop
							prevAcceptedUnitNbr = tranCorr.getUnitNbr();
						} else {
							correction.setAcceptedContNbr(0);
						} // end else Status is not ACCEPTED
					} // end else not last canister
				} // end else not open
				correction.setLastStep(tranCorr.getStep());
				
				correction.getCorrectionStepList().add(addMe);
			} // end for each tranCorr history
			
			correction.setOpenDispenseList(openDispItemList);
			correction.setAcceptedDispenseList(dispenseItemList);
			
			// load up skipped and discarded array
			correction.setSkippedCont(Arrays.copyOf(correction.getSkippedCont(), skippedMap.size()));
			int i = 0;
			for(Integer item : skippedMap.keySet()){
				correction.getSkippedCont()[i] = item.intValue();
				i++;
			}
			correction.setDiscardedCont(Arrays.copyOf(correction.getDiscardedCont(), discardedMap.size()));
			i = 0;
			for(Integer item : discardedMap.keySet()){
				correction.getDiscardedCont()[i] = item.intValue();
				i++;
			}
			
		} // end tranCorr history avail or not

		
		return correction;
	}
}
