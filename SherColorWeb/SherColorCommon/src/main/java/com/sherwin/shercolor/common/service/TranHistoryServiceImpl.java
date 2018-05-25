package com.sherwin.shercolor.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.dao.CustWebTranCorrDao;
import com.sherwin.shercolor.common.dao.CustWebTranDao;
import com.sherwin.shercolor.common.domain.CustWebTran;
import com.sherwin.shercolor.common.domain.CustWebTranCorr;
import com.sherwin.shercolor.common.domain.FormulaIngredient;
import com.sherwin.shercolor.common.exception.SherColorException;
import com.sherwin.shercolor.util.domain.SwMessage;

@Service
public class TranHistoryServiceImpl implements TranHistoryService{

	static Logger logger = LogManager.getLogger(TranHistoryServiceImpl.class.getName());

	@Autowired
	CustWebTranDao custWebTranDao;
	
	@Autowired
	CustWebTranCorrDao custWebTranCorrDao;
	
	@Autowired
	private ResourceBundleMessageSource messageSource;
	
	@Autowired
	Locale locale;

	@Autowired
	FormulationService formulationService;
	
	@Transactional
	public CustWebTran readTranHistory(String customerId, int controlNbr, int lineNbr){
		CustWebTran record = null;
		try {
			record = custWebTranDao.read(customerId, controlNbr, lineNbr);
		} catch (SherColorException se) {
			logger.error(se.getMessage());
			throw se;
		} catch (HibernateException he) {
			logger.error(he.toString() + " " + he.getMessage());
			throw he;
		} catch(Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			throw e;
		}
		return record;
	}
	@Transactional
	public SwMessage saveNewTranHistory (CustWebTran custWebTran){

		// default result to unknown write error, will be overwritten with null if successful or with exception if thrown
		SwMessage result = new SwMessage();
		result.setCode("1500");
		result.setMessage(messageSource.getMessage("1500", new Object[] {}, locale ));
		result.setSeverity(Level.ERROR);
		
		try {
			if(custWebTran==null){
				//logger.info("in service and custWebTran is null");
			} else {
				//logger.info("in service and custWebTran is NOT null");
			}
			if(custWebTranDao.create(custWebTran)){
				// ok set result to null
				result = null;
			}
			//logger.info("back from dao.create");
		} catch (SherColorException se) {
			logger.error(se.getMessage());
			result = new SwMessage();
			result.setCode(String.valueOf(se.getCode()));
			result.setMessage(se.getLocalizedMessage());
			result.setSeverity(Level.ERROR);
		} catch (HibernateException he) {
			logger.error(he.getMessage());
			throw he;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		//logger.info("End of saveNewTranHistory and returning result equal to " + result);
		return result;
	}
	
	@Transactional
	public SwMessage updateTranHistory (CustWebTran custWebTran){
		// default result to unknown write error, will be overwritten with null if successful or with exception if thrown
		SwMessage result = new SwMessage();
		result.setCode("1500");
		result.setMessage(messageSource.getMessage("1500", new Object[] {}, locale ));
		result.setSeverity(Level.ERROR);
		
		try {
			if(custWebTran==null){
				result = new SwMessage();
				result.setCode("1503");
				result.setMessage(messageSource.getMessage("1503", new Object[] {}, locale ));
				result.setSeverity(Level.ERROR);
			} else {
				if(custWebTranDao.update(custWebTran)){
					result=null;
				}
			}
		} catch (SherColorException se) {
			logger.error(se.getMessage());
			result = new SwMessage();
			result.setCode(String.valueOf(se.getCode()));
			result.setMessage(se.getLocalizedMessage());
			result.setSeverity(Level.ERROR);
		} catch (HibernateException he) {
			logger.error(he.getMessage());
			throw he;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		//logger.info("End of updateTranHistory and returning result equal to " + result);
		return result;
		
	}
	
	@Transactional
	public List<CustWebTran> getCustomerJobs(String customerId){
		List<CustWebTran> recordList = new ArrayList<CustWebTran>();
		
		try{
			recordList = custWebTranDao.listForCustomerId(customerId);
			
		} catch (SherColorException se) {
			logger.error(se.toString() + " " + se.getMessage());
		} catch (HibernateException he) {
			logger.error(he.toString() + " " + he.getMessage());
			throw he;
		} catch(Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			throw e;
		}
		
		return recordList;
		
		
	}
	
	@Transactional
	public List<CustWebTranCorr> getCorrections(String customerId, int controlNbr, int lineNbr){
		List<CustWebTranCorr> recordList = new ArrayList<CustWebTranCorr>();
		
		try{
			recordList = custWebTranCorrDao.listForCustomerOrderLine(customerId,controlNbr,lineNbr);
		} catch (SherColorException se) {
			logger.error(se.toString() + " " + se.getMessage());
		} catch (HibernateException he) {
			logger.error(he.toString() + " " + he.getMessage());
			throw he;
		} catch(Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			throw e;
		}
		
		return recordList;
	}
	
	@Transactional
	public SwMessage saveNewTranCorrection(CustWebTranCorr custWebTranCorr){
		// default result to unknown write error, will be overwritten with null if successful or with exception if thrown
		SwMessage result = new SwMessage();
		result.setCode("1500");
		result.setMessage(messageSource.getMessage("1500", new Object[] {}, locale ));
		result.setSeverity(Level.ERROR);
		
		try {
			if(custWebTranCorrDao.create(custWebTranCorr)){
				// ok set result to null
				result = null;
			}
			//logger.info("back from dao.create");
		} catch (SherColorException se) {
			logger.error(se.getMessage());
			result = new SwMessage();
			result.setCode(String.valueOf(se.getCode()));
			result.setMessage(se.getLocalizedMessage());
			result.setSeverity(Level.ERROR);
		} catch (HibernateException he) {
			logger.error(he.getMessage());
			throw he;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return result;
		
	}
	
	@Transactional
	public SwMessage updateTranCorrectionStatus(String customerId, int controlNbr, int lineNbr, int cycle, int unitNbr, String status){
		// default result to unknown write error, will be overwritten with null if successful or with exception if thrown
		SwMessage result = new SwMessage();
		result.setCode("1500");
		result.setMessage(messageSource.getMessage("1500", new Object[] {}, locale ));
		result.setSeverity(Level.ERROR);
		
		try {
			List<CustWebTranCorr> tranCorrList = custWebTranCorrDao.listForTranCorrCycleUnit(customerId, controlNbr, lineNbr, cycle, unitNbr);
			
			for (CustWebTranCorr tranCorr : tranCorrList){
				tranCorr.setStatus(status);
				if(!custWebTranCorrDao.update(tranCorr)){
					result.setCode("1500");
					result.setMessage(messageSource.getMessage("1500", new Object[] {}, locale ));
					result.setSeverity(Level.ERROR);
					break;
				}
				result = null;
			}
					
			//logger.info("back from dao.create");
		} catch (SherColorException se) {
			logger.error(se.getMessage());
			result = new SwMessage();
			result.setCode(String.valueOf(se.getCode()));
			result.setMessage(se.getLocalizedMessage());
			result.setSeverity(Level.ERROR);
		} catch (HibernateException he) {
			logger.error(he.getMessage());
			throw he;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return result;
		
		
	}

	@Transactional
	public List<CustWebTranCorr> getAcceptedCorrectionsForCycle(String customerId, int controlNbr, int lineNbr, int cycle){
		List<CustWebTranCorr> recordList = new ArrayList<CustWebTranCorr>();
		
		try{
			List<CustWebTranCorr> tranCorrList = custWebTranCorrDao.listForCustomerOrderLine(customerId,controlNbr,lineNbr);
			for (CustWebTranCorr tranCorr : tranCorrList){
				if(tranCorr.getCycle()==cycle && tranCorr.getStatus().equalsIgnoreCase("ACCEPTED")){
					recordList.add(tranCorr);
				}
			}
		} catch (SherColorException se) {
			logger.error(se.toString() + " " + se.getMessage());
		} catch (HibernateException he) {
			logger.error(he.toString() + " " + he.getMessage());
			throw he;
		} catch(Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			throw e;
		}
		
		return recordList;
		
	}
	
	@Transactional
	public SwMessage updateTranCorrectionMerged(String customerId, int controlNbr, int lineNbr, int cycle){
		// default result to unknown write error, will be overwritten with null if successful or with exception if thrown
		SwMessage result = new SwMessage();
		result.setCode("1500");
		result.setMessage(messageSource.getMessage("1500", new Object[] {}, locale ));
		result.setSeverity(Level.ERROR);
		
		try {
			List<CustWebTranCorr> tranCorrList = custWebTranCorrDao.listForCustomerOrderLine(customerId, controlNbr, lineNbr);
			
			for (CustWebTranCorr tranCorr : tranCorrList){
				if(tranCorr.getCycle()==cycle){
					tranCorr.setMergedWithOrig(true);
					if(!custWebTranCorrDao.update(tranCorr)){
						result.setCode("1500");
						result.setMessage(messageSource.getMessage("1500", new Object[] {}, locale ));
						result.setSeverity(Level.ERROR);
						break;
					}
				}
				result = null;
			}
					
			//logger.info("back from dao.create");
		} catch (SherColorException se) {
			logger.error(se.getMessage());
			result = new SwMessage();
			result.setCode(String.valueOf(se.getCode()));
			result.setMessage(se.getLocalizedMessage());
			result.setSeverity(Level.ERROR);
		} catch (HibernateException he) {
			logger.error(he.getMessage());
			throw he;
		} catch(Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return result;
		
	}

	public List<FormulaIngredient> mapTranCorrClrntFieldsToIngredientList(CustWebTranCorr tranCorr){
		System.out.println("inside tranHistoryService mapTranCorrClrntFIeldsToIngr list");
		List<FormulaIngredient> ingredientList = new ArrayList<FormulaIngredient>();
		if(tranCorr.getClrnt1()!=null){
			FormulaIngredient ingr = new FormulaIngredient();
			ingr.setClrntSysId(tranCorr.getClrntSysId());
			ingr.setTintSysId(tranCorr.getClrnt1());
			ingr.setShots(tranCorr.getClrntAmt1());
			ingredientList.add(ingr);
		}
		if(tranCorr.getClrnt2()!=null){
			FormulaIngredient ingr = new FormulaIngredient();
			ingr.setClrntSysId(tranCorr.getClrntSysId());
			ingr.setTintSysId(tranCorr.getClrnt2());
			ingr.setShots(tranCorr.getClrntAmt2());
			ingredientList.add(ingr);
		}
		if(tranCorr.getClrnt3()!=null){
			FormulaIngredient ingr = new FormulaIngredient();
			ingr.setClrntSysId(tranCorr.getClrntSysId());
			ingr.setTintSysId(tranCorr.getClrnt3());
			ingr.setShots(tranCorr.getClrntAmt3());
			ingredientList.add(ingr);
		}
		if(tranCorr.getClrnt4()!=null){
			FormulaIngredient ingr = new FormulaIngredient();
			ingr.setClrntSysId(tranCorr.getClrntSysId());
			ingr.setTintSysId(tranCorr.getClrnt4());
			ingr.setShots(tranCorr.getClrntAmt4());
			ingredientList.add(ingr);
		}
		if(tranCorr.getClrnt5()!=null){
			FormulaIngredient ingr = new FormulaIngredient();
			ingr.setClrntSysId(tranCorr.getClrntSysId());
			ingr.setTintSysId(tranCorr.getClrnt5());
			ingr.setShots(tranCorr.getClrntAmt5());
			ingredientList.add(ingr);
		}
		if(tranCorr.getClrnt6()!=null){
			FormulaIngredient ingr = new FormulaIngredient();
			ingr.setClrntSysId(tranCorr.getClrntSysId());
			ingr.setTintSysId(tranCorr.getClrnt6());
			ingr.setShots(tranCorr.getClrntAmt6());
			ingredientList.add(ingr);
		}
		if(tranCorr.getClrnt7()!=null){
			FormulaIngredient ingr = new FormulaIngredient();
			ingr.setClrntSysId(tranCorr.getClrntSysId());
			ingr.setTintSysId(tranCorr.getClrnt7());
			ingr.setShots(tranCorr.getClrntAmt7());
			ingredientList.add(ingr);
		}
		if(tranCorr.getClrnt8()!=null){
			FormulaIngredient ingr = new FormulaIngredient();
			ingr.setClrntSysId(tranCorr.getClrntSysId());
			ingr.setTintSysId(tranCorr.getClrnt8());
			ingr.setShots(tranCorr.getClrntAmt8());
			ingredientList.add(ingr);
		}

		if(ingredientList.size()>0){
			System.out.println("inside mapTranCorrClrntFIeldsToIngr list about to call form service...");
			formulationService.convertShotsToIncr(ingredientList);
			System.out.println("inside mapTranCorrClrntFIeldsToIngr list about to call form service2...");
			formulationService.fillIngredientInfoFromTintSysId(ingredientList);
			System.out.println("inside mapTranCorrClrntFIeldsToIngr list back from call form service2...");
		}

		return ingredientList;
	}

	@Transactional
	public List<CustWebTran> getCustomerOrder(int controlNbr){
		List<CustWebTran> recordList = new ArrayList<CustWebTran>();
		
		try{
			recordList = custWebTranDao.listForControlNbr(controlNbr);
			
		} catch (SherColorException se) {
			logger.error(se.toString() + " " + se.getMessage());
		} catch (HibernateException he) {
			logger.error(he.toString() + " " + he.getMessage());
			throw he;
		} catch(Exception e) {
			logger.error(e.toString() + " " + e.getMessage());
			throw e;
		}
		
		return recordList;
		
		
	}
	


}
