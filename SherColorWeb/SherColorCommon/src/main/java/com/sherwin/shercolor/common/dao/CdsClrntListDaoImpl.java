package com.sherwin.shercolor.common.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.sherwin.shercolor.common.domain.CdsClrntList;
import com.sherwin.shercolor.common.domain.CdsClrntListPK;

@Repository
@Transactional
public class CdsClrntListDaoImpl implements CdsClrntListDao{
    /** Global variables defined */
	@Autowired  /** Annotation - permits struts to set up class to make available to Java */ 
	private SessionFactory sessionFactory;
	static Logger logger = LogManager.getLogger(CdsClrntListDaoImpl.class);
	
	public CdsClrntList read(String clrntListId, String clrntSysId, String tintSysId){
        /** Local defined variables go into the method here */
		/** Object type and name of variable - benefit when multiple fields in record are part of key */
		CdsClrntList record = null;
		CdsClrntListPK pkey = new CdsClrntListPK();

		/* Interface between struts and screen management */
		Session session = getSessionFactory().getCurrentSession();

		pkey.setClrntListId(clrntListId);
		pkey.setClrntSysId(clrntSysId);
		pkey.setTintSysId(tintSysId);
		
		try {
			record = (CdsClrntList) session.get(CdsClrntList.class,pkey);
		} catch (HibernateException e){
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, pkey.toString(), e.getMessage()));
			throw(e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return record;
	}
	
	@SuppressWarnings("unchecked")
	public HashMap<String,CdsClrntList> mapTintSysIdForClrntList(String clrntListId, String clrntSysId){
		HashMap<String,CdsClrntList> map = new HashMap<String,CdsClrntList>();
		
		List<CdsClrntList> recordList = null;
		Session session = sessionFactory.getCurrentSession();

		String hql = "from CdsClrntList ccl where ccl.clrntListId = :clrntListId and ccl.clrntSysId = :clrntSysId";
		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("clrntListId", clrntListId)
					.setParameter("clrntSysId", clrntSysId);
			recordList = query.list();
		} catch (HibernateException e) {
			String msg = "Error reading record clrntListId %s and clrntSysId %s : %s  ";
			logger.error(String.format(msg, clrntListId, clrntSysId, e.getMessage()));
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		for(CdsClrntList clrntList : recordList){
			map.put(clrntList.getTintSysId(), clrntList);
		}
		
		return map;
		
	}
	
    /** Generated from Source -> Generate Getters and Setters - Selected from list */
	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		CdsClrntSysDaoImpl.logger = logger;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


}
