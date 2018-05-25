package com.sherwin.shercolor.common.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.dao.CustWebColorantsTxtDao;
import com.sherwin.shercolor.common.dao.CustWebTinterEventsDao;
import com.sherwin.shercolor.common.dao.CustWebTinterEventsDetailDao;
import com.sherwin.shercolor.common.domain.CustWebColorantsTxt;
import com.sherwin.shercolor.common.domain.CustWebDevices;
import com.sherwin.shercolor.common.domain.CustWebTinterEvents;
import com.sherwin.shercolor.common.domain.CustWebTinterEventsDetail;
import com.sherwin.shercolor.common.domain.FormulaInfo;
import com.sherwin.shercolor.common.exception.SherColorException;
import com.sherwin.shercolor.util.domain.SwMessage;

@Service
@Transactional
public class TinterServiceImpl implements TinterService {
	static Logger logger = LogManager.getLogger(TinterServiceImpl.class);

	@Autowired
	CustWebColorantsTxtDao colorantsTxtDao;
	
	@Autowired
	CustWebTinterEventsDao tintEventsDao;
	
	@Autowired
	CustWebTinterEventsDetailDao teDetailDao;
	
	@Autowired
	private ResourceBundleMessageSource messageSource;

	@Autowired
	Locale locale;
	

	public List<SwMessage> preDispenseCheck(CustWebDevices deviceInfo, FormulaInfo formInfo){
		List<SwMessage> messageList= null;
		
		//TODO insert code here...
		
		return messageList;
	}
	
	public HashMap<String, CustWebColorantsTxt> getCanisterMap(String customerId, String clrntSysId, String tinterModel, String tinterSerialNbr){
		HashMap<String,CustWebColorantsTxt> colorantMap = null;
		try {
			List<CustWebColorantsTxt> recordList = colorantsTxtDao.listForUniqueTinter(customerId, clrntSysId, tinterModel, tinterSerialNbr);
			
			if(recordList!=null && recordList.size()>0) {
				colorantMap = new HashMap<String, CustWebColorantsTxt>();
				for(CustWebColorantsTxt record : recordList){
					if(record.getClrntCode()!=null && !record.getClrntCode().equalsIgnoreCase("NA")){
						colorantMap.put(record.getClrntCode(), record);
					}
				}
			}
		} catch (Exception e) {
			colorantMap = null;
		}

		return colorantMap;
	}
	
	public List<CustWebColorantsTxt> getCanisterList(String customerId, String clrntSysId, String tinterModel, String tinterSerialNbr){
		List<CustWebColorantsTxt> recordList = null;
		try {
			recordList = colorantsTxtDao.listForUniqueTinter(customerId, clrntSysId, tinterModel, tinterSerialNbr);
		} catch (Exception e) {
			//TODO Code tinter exception here
		}

		return recordList;
	}
	
		
		@Override
		public int conditionalSaveColorantsTxt(List<CustWebColorantsTxt> colorantList){
			int rc=0;
		
			for(CustWebColorantsTxt colorant : colorantList){
				CustWebColorantsTxt c1 = colorantsTxtDao.read(colorant.getCustomerId(), colorant.getClrntSysId(), colorant.getTinterModel(),
						colorant.getTinterSerialNbr(), colorant.getClrntCode(), colorant.getPosition());				
				if(c1 != null){  //colorants text already there for that customer/tinter
					rc=1;
					break;
				}
				boolean success=colorantsTxtDao.create(colorant);
				if(success==false){
					rc = -1;
					System.out.println("Could not save colorantstxt to DB for " + colorant.getClrntCode());
				}
			}
			return rc;
		}
	@Override
	public boolean saveColorantsTxt(List<CustWebColorantsTxt> colorantList){
		boolean rc=true;
	
		for(CustWebColorantsTxt colorant : colorantList){
			boolean success=colorantsTxtDao.create(colorant);
			if(success==false){
				rc = false;
				System.out.println("Could not save colorantstxt to DB for " + colorant.getClrntCode());
			}
		}
		return rc;
	}
	

	@Override
	public List<String> listOfColorantSystemsByCustomerId(String customerId){
		 List<String> list = null;
		 list = colorantsTxtDao.listOfColorantSystemsByCustomerId(customerId);
		 return list;
	}
	@Override
	public List<String> listOfModelsForCustomerId(String customerId, String clrntSysId){
		 List<String> list = null;
		 list = colorantsTxtDao.listOfModelsForCustomerId(customerId, clrntSysId);
		 return list;
	}
	
	public List<SwMessage> decrementFormulaDispense(CustWebDevices deviceInfo, FormulaInfo formInfo){
		List<SwMessage> messageList= null;
		
		//TODO insert code here...
		
		return messageList;
		
	}
	
	public boolean hasAutoNozzleCover(String customerId, String clrntSysId, String tinterModel, String tinterSerialNbr){
		boolean retVal = true; // true is the default
		
		// for now just add models that don't have an automatic nozzle cover
		if(tinterModel.equalsIgnoreCase("COROB D200")) retVal = false;
		
		return retVal;
	}
	
	public CustWebTinterEvents getLastPurgeDateAndUser(String customerId, String clrntSysId, String tinterModel, String tinterSerialNbr){
		CustWebTinterEvents tintEvent = null;
		
		try{
			tintEvent = tintEventsDao.findLastPurge(customerId, clrntSysId, tinterModel, tinterSerialNbr);
			if(tintEvent!=null){
				List<CustWebTinterEventsDetail> recordList = teDetailDao.listForGuid(tintEvent.getGuid());
				for(CustWebTinterEventsDetail item : recordList){
					if(item.getType()!=null && item.getType().equalsIgnoreCase("PURGE USER")){
						tintEvent.setEventDetails(item.getName());
					}
				}
			}
			
		} catch (Exception e) {
			//TODO anything more to do here? just return null tintEvetn?
			logger.error(e.getMessage());
		}
		
		return tintEvent;
	}
	
	public List<SwMessage> writeTinterEventAndDetail(CustWebTinterEvents tintEvent, List<CustWebTinterEventsDetail> teDetails){
		List<SwMessage> errorList = new ArrayList<SwMessage>();
		
		try{
			String masterGuid = tintEventsDao.create(tintEvent);
			
			if(masterGuid!=null){
				if(teDetails.size()>0){
					for (CustWebTinterEventsDetail item : teDetails){
						item.setGuid(masterGuid);
						boolean insertOk = teDetailDao.create(item);
						if(!insertOk){
							SwMessage errMsg = new SwMessage();
							errMsg.setSeverity(Level.ERROR);
							errMsg.setCode("1510");
							errMsg.setMessage(messageSource.getMessage("1510", new Object[] {}, locale ));
							errorList.add(errMsg);
							break;
						}
					}
				}
			} else {
				SwMessage errMsg = new SwMessage();
				errMsg.setSeverity(Level.ERROR);
				errMsg.setCode("1510");
				errMsg.setMessage(messageSource.getMessage("1510", new Object[] {}, locale ));
				errorList.add(errMsg);
			}
		} catch (SherColorException se) {
			SwMessage errMsg = new SwMessage();
			errMsg.setCode(String.valueOf(se.getCode()));
			errMsg.setMessage(se.getLocalizedMessage());
			errMsg.setSeverity(Level.ERROR);
			errorList.add(errMsg);
		} catch (Exception e) {
			logger.error(e.getMessage());
			SwMessage errMsg = new SwMessage();
			errMsg.setSeverity(Level.ERROR);
			errMsg.setCode("1510");
			errMsg.setMessage(messageSource.getMessage("1510", new Object[] {}, locale ));
			errorList.add(errMsg);
		}
		
		return errorList;
		
	}

	public List<SwMessage> updateColorantLevels(List<CustWebColorantsTxt> colorantsTxtList){
		List<SwMessage> errorList = new ArrayList<SwMessage>();
		
		//Walk through list of CustWebColorantsTxt and call the dao.update method
		try{
			if(colorantsTxtList==null || colorantsTxtList.size()==0){
				// Add error to list and break out
				SwMessage errMsg = new SwMessage();
				errMsg.setSeverity(Level.ERROR);
				errMsg.setCode("1521"); //empty or null list sent in 
				errMsg.setMessage(messageSource.getMessage("1521", new Object[] {}, locale ));
				errorList.add(errMsg);
			}
			for(CustWebColorantsTxt colorantsTxt : colorantsTxtList ){
				if(!colorantsTxtDao.update(colorantsTxt)){
					SwMessage errMsg = new SwMessage();
					errMsg.setSeverity(Level.ERROR);
					errMsg.setCode("1522"); // failed on colorant update
					errMsg.setMessage(messageSource.getMessage("1522", new Object[] {colorantsTxt.getClrntCode(),colorantsTxt.getPosition()}, locale ));
					errorList.add(errMsg);
					break;
				}
			}
			if(errorList.size()>0){
				//TODO if any errors don't commit any
				
			}
			
		} catch (SherColorException se) {
			SwMessage errMsg = new SwMessage();
			errMsg.setCode(String.valueOf(se.getCode()));
			errMsg.setMessage(se.getLocalizedMessage());
			errMsg.setSeverity(Level.ERROR);
			errorList.add(errMsg);
		} catch (Exception e) {
			logger.error(e.getMessage());
			SwMessage errMsg = new SwMessage();
			errMsg.setSeverity(Level.ERROR);
			errMsg.setCode("1520");
			errMsg.setMessage(messageSource.getMessage("1520", new Object[] {}, locale ));
			errorList.add(errMsg);
			
		}
		
		return errorList;
	}
	@Override
	public boolean deleteColorantTxtItem(CustWebColorantsTxt colorant){
		
		boolean success = false;
		try{
			success=colorantsTxtDao.delete(colorant);
		}
		catch(Exception e){
			logger.error(e.getMessage());
		}
		return success;
		
	}

}
