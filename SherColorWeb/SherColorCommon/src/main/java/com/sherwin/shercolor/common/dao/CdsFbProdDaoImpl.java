package com.sherwin.shercolor.common.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CdsFbProd;
import com.sherwin.shercolor.common.domain.CdsFbProdPK;

@Repository
@Transactional
public class CdsFbProdDaoImpl implements CdsFbProdDao {
	
	static Logger logger = LogManager.getLogger(CdsFbColorDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
		

	@Override
	public CdsFbProd read(String prodComp, String salesNbr, String fbBase) {
		CdsFbProd record = null;
		CdsFbProdPK pkey = new CdsFbProdPK();
		
		Session session = sessionFactory.getCurrentSession();

		pkey.setProdComp(prodComp);
		pkey.setSalesNbr(salesNbr);
		pkey.setFbBase(fbBase);
		
		try {
			record = (CdsFbProd) session.get(CdsFbProd.class,pkey);
		}
		catch (HibernateException e) {
			String msg = "Error reading %s record from CdsFbProd : %s ";
			logger.error(String.format(msg, pkey.toString(), e.getMessage()));
			throw(e);
		}
		catch(RuntimeException e) {
			logger.error(e.getMessage());
			throw e;
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
