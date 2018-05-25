package com.sherwin.shercolor.common.service;

import java.util.List;

import com.sherwin.shercolor.common.domain.CustWebParms;
import com.sherwin.shercolor.common.domain.FormulaInfo;
import com.sherwin.shercolor.common.domain.FormulaIngredient;
import com.sherwin.shercolor.common.domain.FormulationResponse;
import com.sherwin.shercolor.common.domain.OeFormInputRequest;
import com.sherwin.shercolor.common.domain.OeServiceColorDataSet;
import com.sherwin.shercolor.common.domain.OeServiceProdDataSet;
import com.sherwin.shercolor.util.domain.SwMessage;

public interface FormulationService {

	public FormulationResponse formulate(String clrntSysId, String colorComp, String colorId, String salesNbr);

	public FormulationResponse formulate(OeFormInputRequest inputRequest, CustWebParms custWebParms);
	
	public FormulationResponse formulate(OeFormInputRequest inputRequest, CustWebParms custWebParms, OeServiceProdDataSet dsProd, OeServiceColorDataSet dsColor);
	
	public FormulationResponse prodFamilyFormulate(OeFormInputRequest inputRequest, CustWebParms custWebParms);

	public FormulationResponse prodFamilyFormulate(OeFormInputRequest inputRequest, CustWebParms custWebParms, OeServiceProdDataSet dsProd, OeServiceColorDataSet dsColor);

	public FormulaInfo scaleFormulaByPercent(FormulaInfo formula, int percent);
	
	public boolean convertShotsToIncr(List<FormulaIngredient> ingredients);
	
	public boolean convertIncrToShots(List<FormulaIngredient> ingredients);
	
	public boolean fillIngredientInfoFromTintSysId(List<FormulaIngredient> ingredients);
	
	public List<SwMessage> validateFormulation(FormulaInfo formula);
	
	public List<SwMessage> canLabelFormulationWarnings(FormulaInfo formula);
	
	public List<SwMessage> manualFormulationWarnings(FormulaInfo formula);
	
	public Double[] projectCurve(FormulaInfo formInfo, CustWebParms custWebParms);
	
	public List<SwMessage> fillLevelCheck(FormulaInfo formula);
}
