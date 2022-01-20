package com.sherwin.login.service;

import com.sherwin.login.repository.SwUserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.login.domain.SWUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


@Service
@Transactional
public class SWUserServiceImpl implements SWUserService {
	static Logger logger = LogManager.getLogger(SWUserServiceImpl.class);
	
	@Autowired
	SwUserRepository swUserRepository;
	
	public SWUser readSWUser(String loginID) {
		SWUser returnRec;
		try {
			returnRec = swUserRepository.findByLoginID(loginID);
		} catch (RuntimeException e){
			logger.error(e.getMessage());
			throw e;
//		} catch(Exception e) {
//			logger.error(e.getMessage());
//			throw e;
		}
		return returnRec;
	}
	
	public int disableActiveUser(String loginID) {
		int returnRec = 0;
		try {
			returnRec = swUserRepository.disableActiveUser(loginID);
		} catch (RuntimeException e){
			logger.error(e.getMessage());
			throw e;
		}
		return returnRec;
	}
	
	public String getLoginId(String loginID) {
		String returnRec = null;
		try {
			returnRec = swUserRepository.getLoginID(loginID);
		} catch (RuntimeException e){
			logger.error(e.getMessage());
			throw e;
		}
		return returnRec;
	}

	public boolean updatePasswordChangeDate(String loginID)  {
		boolean result = false;

		try{
			String corpUser = null;
			String isSaleRep = null;
			Calendar theCal = Calendar.getInstance();

			SWUser record = swUserRepository.findByLoginID(loginID);
			if (record!=null) {
				//calculate the date...
				if( record.getCorpUser() == null ) {
					corpUser = "N";
				} else {
					corpUser = record.getCorpUser();
				}

				if( record.getIsSalesRep() == null ) {
					isSaleRep = "N";
				} else {
					isSaleRep = record.getIsSalesRep();
				}

				//corporate users and sales reps do not expire.
				//everyone else has 90 days until they have to reset their password.
				if ( corpUser.equals( "N") && isSaleRep.equals( "N") ) {
					theCal.add(Calendar.DAY_OF_MONTH, 90);
				} else {
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
					Date startDate = sdf.parse("12/31/2099");
					Date dd = new Date(startDate.getTime());
					theCal.setTime(dd);
				}

				record.setChangePassword(theCal.getTime());
				swUserRepository.save(record);
				result = true;
			}
		} catch (HibernateException e) {
			String msg = "Error updating record : %s  ";
			logger.error(String.format(msg, e.getMessage()));
			e.printStackTrace();
			throw(e);
		} catch (ParseException e) {
			String msg = "Error updating record : %s  ";
			logger.error(String.format(msg, e.getMessage()));
			e.printStackTrace();
		}

		return result;

	}
}
