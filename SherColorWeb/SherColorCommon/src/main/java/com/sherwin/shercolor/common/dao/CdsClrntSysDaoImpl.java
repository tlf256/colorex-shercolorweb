package com.sherwin.shercolor.common.dao;

/**  import java.util.ArrayList; */
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


/** Import Class for use */
import com.sherwin.shercolor.common.domain.CdsClrntSys;
import com.sherwin.shercolor.common.domain.CdsClrntSysPK;

@Repository
@Transactional
public class CdsClrntSysDaoImpl implements CdsClrntSysDao{
    /** Global variables defined */
	@Autowired  /** Annotation - permits struts to set up class to make available to Java */ 
	private SessionFactory sessionFactory;
	static Logger logger = LogManager.getLogger(CdsClrntSysDaoImpl.class);
	
	public CdsClrntSys read(String ClrntSysId){
        /** Local defined variables go into the method here */
		/** Object type and name of variable - benefit when multiple fields in record are part of key */
		CdsClrntSys record = null;
		CdsClrntSysPK pkey = new CdsClrntSysPK();

		/* Interface between struts and screen management */
		Session session = getSessionFactory().getCurrentSession();

		pkey.setClrntSysId(ClrntSysId);
		
		try {
			record = (CdsClrntSys) session.get(CdsClrntSys.class,pkey);
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
	@Override
	public List<CdsClrntSys> listForClrntSysId(Boolean activeOnly) {
			List<CdsClrntSys> recordList = null;
			
			Session session = getSessionFactory().getCurrentSession();
			
			StringBuilder hql = new StringBuilder();

			hql.append("from CdsClrntSys ccm ");
			if (activeOnly) {
				hql.append("where ccm.effDate <= sysdate() and (ccm.expDate > sysdate() or ccm.expDate is null)");
			} 
			hql.append(" order by ccm.defaultInd, ccm.clrntSysId ");
			
			try {
				Query query = session.createQuery(hql.toString());
				
				recordList = query.list();
				
			} catch (HibernateException e){
				String msg = "Error reading %s record : %s  ";
				logger.error(String.format(msg, hql.toString(), e.getMessage()));
				throw(e);
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw e;
			}
			
			return recordList;

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
