package com.sherwin.login.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.login.dao.SWUserDao;
import com.sherwin.login.domain.SWUser;


@Service
@Transactional
public class SWUserServiceImpl implements SWUserService {
	static Logger logger = LogManager.getLogger(SWUserServiceImpl.class);
	
	@Autowired
	SWUserDao swUserDao;
	
	public SWUser readSWUser(String loginID) {
		SWUser returnRec;
		try {
			returnRec = swUserDao.read(loginID);
		} catch (RuntimeException e){
			logger.error(e.getMessage());
			throw e;
//		} catch(Exception e) {
//			logger.error(e.getMessage());
//			throw e;
		}
		return returnRec;
	}
	
	public boolean disableActiveUser(String loginID) {
		boolean returnRec = false;
		try {
			returnRec = swUserDao.disableActiveUser(loginID);
		} catch (RuntimeException e){
			logger.error(e.getMessage());
			throw e;
		}
		return returnRec;
	}
	
	public String getLoginId(String loginID) {
		String returnRec = null;
		try {
			returnRec = swUserDao.getLoginId(loginID);
		} catch (RuntimeException e){
			logger.error(e.getMessage());
			throw e;
		}
		return returnRec;
	}
	
	public boolean updatePasswordChangeDate(String loginID) {
		boolean returnRec = false;
		try {
			returnRec = swUserDao.updatePasswordChangeDate(loginID);
		} catch (RuntimeException e){
			logger.error(e.getMessage());
			throw e;
		}
		return returnRec;
	}
	
	public SWUserDao getSWUserDao() {
		return swUserDao;
	}

	public void setSWUserDao(SWUserDao swUserDao) {
		this.swUserDao = swUserDao;
	}

}
