package com.sherwin.shercolor.common.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sherwin.shercolor.common.domain.CdsColorXyzRgbConvert;
import com.sherwin.shercolor.common.domain.CdsColorXyzRgbConvertPK;

public class CdsColorXyzRgbConvertDaoImpl implements CdsColorXyzRgbConvertDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	static Logger logger = LogManager.getLogger(CdsColorXyzRgbConvertDaoImpl.class);


	public CdsColorXyzRgbConvert read(String illuminant, int observer){
		CdsColorXyzRgbConvert record = null;
		CdsColorXyzRgbConvertPK pkey = new CdsColorXyzRgbConvertPK();
		
		Session session = getSessionFactory().getCurrentSession();

		pkey.setIlluminant(illuminant);
		pkey.setObserver(observer);
		
		try {
			record = (CdsColorXyzRgbConvert) session.get(CdsColorXyzRgbConvert.class,pkey);
		} catch (HibernateException e){
			String msg = "Error reading %s record from CdsColorXyzRgbConvert : %s ";
			logger.error(String.format(msg, pkey.toString(), e.getMessage()));
			throw(e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return record;

	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
