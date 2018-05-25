package com.sherwin.shercolor.common.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CdsFbColor;
import com.sherwin.shercolor.common.domain.CdsFbColorPK;

@Repository
@Transactional
public class CdsFbColorDaoImpl implements CdsFbColorDao {
	
	static Logger logger = LogManager.getLogger(CdsFbColorDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
		

	@Override
	public CdsFbColor read(String colorComp, String colorId, String clrntSysId, String fbBase) {
		CdsFbColor record = null;
		CdsFbColorPK pkey = new CdsFbColorPK();
		
		Session session = sessionFactory.getCurrentSession();

		pkey.setColorComp(colorComp);
		pkey.setColorId(colorId);
		pkey.setClrntSysId(clrntSysId);
		pkey.setFbBase(fbBase);
		
		try {
			record = (CdsFbColor) session.get(CdsFbColor.class,pkey);
		}
		catch (HibernateException e) {
			String msg = "Error reading %s record from CdsFBColor : %s ";
			logger.error(String.format(msg, pkey.toString(), e.getMessage()));
			throw(e);
		}
		catch(RuntimeException e) {
			logger.error(e.getMessage());
			throw e;
		} 
		catch (Exception e) {
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
