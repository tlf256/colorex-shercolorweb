package com.sherwin.login.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import com.sherwin.login.domain.SWUser;
import com.sherwin.login.domain.SWUserPK;


@Repository
@Transactional
public class SWUserDaoImpl implements SWUserDao {
	static Logger logger = LogManager.getLogger(SWUserDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public boolean disableActiveUser(String loginID)  {
		boolean result = false;

		Session session = getSessionFactory().getCurrentSession();

		try{
			SWUser record = read(loginID);
			if (record!=null) {
				record.setActiveUser("N");
				session.update(record);
				result = true;
			}
		} catch (HibernateException e) {
			String msg = "Error updating record : %s  ";
			logger.error(String.format(msg, e.getMessage()));
			e.printStackTrace();
			throw(e);
//	Why does this need to be commented out, when it works and compiles in shercolorcommon DAO examples?		
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//			throw e;
		}
 
		return result;
	
	}

	public SWUser read(String loginID) {
		SWUser record = null;
		SWUserPK pkey = new SWUserPK();
		
		Session session = sessionFactory.getCurrentSession();

		pkey.setLoginID(loginID);
		
		try {
			record = (SWUser) session.get(SWUser.class,pkey);
		}
		catch (HibernateException e) {
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, pkey.toString(), e.getMessage()));
			throw(e);
		}
		catch(RuntimeException e) {
			logger.error(e.getMessage());
			throw e;
		} 
//		catch (Exception e) {
//			logger.error(e.getMessage());
//			throw e;
//		}
		return record;
	};
	
	public boolean update(SWUser swUserRec){
		boolean result = false;

		Session session = getSessionFactory().getCurrentSession();

		try{
			session.update(swUserRec);
			result = true;
		} catch (HibernateException e) {
			String msg = "Error updating record : %s  ";
			logger.error(String.format(msg, e.getMessage()));
			e.printStackTrace();
			throw(e);
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//			throw e;
		}
 
		return result;
		
	}
	
	@SuppressWarnings("unchecked")
	public String getLoginId(String inLoginId) {
		List<SWUser> recordList = new ArrayList<SWUser>();
		
		Session session = getSessionFactory().getCurrentSession();
		
		try {
			//we want to compare the inbound login ID, LOWER CASE VERSION, to the 
			//Lower case version of the login ID in the table.  This results in having
			//a case insensitive login id.
			if (inLoginId != null) {
				Query lgnQuery = session.createQuery("from SWUser where lower(loginID) = :loginID ");
				lgnQuery.setParameter("loginID", inLoginId.toLowerCase());
				recordList = lgnQuery.list();
				if (recordList.size()==1) {
					return recordList.get(0).getLoginID();
				} else {
					if (recordList.size() > 1) {
						//log this as an event - we should not have this situation, and if
						//we do, we don't want to return the incorrect user/settings.
						logger.error("Multiple SWUser entries for lower(" + inLoginId + ")");
					}
					//if recordList is empty, then the user wasn't found/not on file.  Return null.
				}
			} else {
				logger.error("inbound login id came in as null");
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		} 
		return null;
	}

	@Override
	public boolean updatePasswordChangeDate(String loginID) {
		return false;
	}


	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
