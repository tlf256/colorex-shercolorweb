package com.sherwin.shercolor.common.dao;

/**  import java.util.ArrayList; */
/**  import java.util.List; */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
/** import org.hibernate.Query;  */
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/** Import Class for use */
import com.sherwin.shercolor.common.domain.CdsClrntCost;
import com.sherwin.shercolor.common.domain.CdsClrntCostPK;

@Repository
@Transactional
public class CdsClrntCostDaoImpl implements CdsClrntCostDao {
    /** Global variables defined */
	@Autowired  /** Annotation - permits struts to set up class to make available to Java */ 
	private SessionFactory sessionFactory;
	static Logger logger = LogManager.getLogger(CdsClrntCostDaoImpl.class);
	
	public CdsClrntCost read(String clrntSysId, String tintSysId){
        /** Local defined variables go into the method here */
		/** Object type and name of variable - benefit when multiple fields in record are part of key */
		CdsClrntCost record = null;
		CdsClrntCostPK pkey = new CdsClrntCostPK();

		/* Interface between struts and screen management */
		Session session = getSessionFactory().getCurrentSession();

		pkey.setClrntSysId(clrntSysId);
		pkey.setTintSysId(tintSysId);
		
		try {
			record = (CdsClrntCost) session.get(CdsClrntCost.class,pkey);
		} catch (HibernateException e){
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, pkey.toString(), e.getMessage()));
			throw(e);
		}  catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return record;
	}
	
    /** Generated from Source -> Generate Getters and Setters - Selected from list */
	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		CdsClrntCostDaoImpl.logger = logger;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}


}
