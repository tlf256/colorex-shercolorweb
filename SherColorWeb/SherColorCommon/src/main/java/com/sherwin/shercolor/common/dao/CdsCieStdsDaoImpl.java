package com.sherwin.shercolor.common.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.sherwin.shercolor.common.domain.CdsCieStds;
import com.sherwin.shercolor.common.domain.CdsCieStdsPK;

@Repository
public class CdsCieStdsDaoImpl implements CdsCieStdsDao {

	@Autowired
	private SessionFactory sessionFactory;
	static Logger logger = LogManager.getLogger(CdsCieStdsDaoImpl.class);

	public CdsCieStds read(String illumCode) {
		CdsCieStds record = null;
		CdsCieStdsPK pkey = new CdsCieStdsPK();

		Session session = getSessionFactory().getCurrentSession();

		pkey.setIllumCode(illumCode);

		try {
			record = (CdsCieStds) session.get(CdsCieStds.class, pkey);
		} catch (HibernateException e) {
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, pkey.toString(), e.getMessage()));
			throw (e);
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
