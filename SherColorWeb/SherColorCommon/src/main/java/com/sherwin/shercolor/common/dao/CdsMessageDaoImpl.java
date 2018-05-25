package com.sherwin.shercolor.common.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sherwin.shercolor.common.domain.CdsMessage;
import com.sherwin.shercolor.common.domain.CdsMessagePK;

@Repository
public class CdsMessageDaoImpl implements CdsMessageDao {

	@Autowired
	private SessionFactory sessionFactory;
	static Logger logger = LogManager.getLogger(CdsMessageDaoImpl.class);

	public CdsMessage read(String cdsMessageId, String module) {
		CdsMessage record = null;
		CdsMessagePK pkey = new CdsMessagePK();

		Session session = getSessionFactory().getCurrentSession();

		pkey.setCdsMessageId(cdsMessageId);
		pkey.setModule(module);

		try {
			record = (CdsMessage) session.get(CdsMessage.class, pkey);
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
