package com.sherwin.login.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.login.dao.SWUserCommentDao;
import com.sherwin.login.domain.SWUserComments;


@Service
@Transactional
public class SWUserCommentServiceImpl implements SWUserCommentService {
	static Logger logger = LogManager.getLogger(SWUserCommentServiceImpl.class);
	
	@Autowired
	SWUserCommentDao swUserCommentDao;
	
	public boolean createOrUpdateEntry(SWUserComments thisComment) {
		boolean theResult = false;
		try {
			swUserCommentDao.createOrUpdateEntry(thisComment);
			theResult = true;
		} catch (Exception se) {
			logger.error(se.getMessage());
			theResult = false;
		} 
		return theResult;
	}

}
