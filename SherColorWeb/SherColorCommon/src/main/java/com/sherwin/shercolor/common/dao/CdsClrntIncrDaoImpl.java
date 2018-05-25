package com.sherwin.shercolor.common.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sherwin.shercolor.common.domain.CdsClrntIncr;
import com.sherwin.shercolor.common.domain.CdsClrntIncrPK;

public class CdsClrntIncrDaoImpl implements CdsClrntIncrDao {
	@Autowired
	private SessionFactory sessionFactory;
	static Logger logger = LogManager.getLogger(CdsClrntIncrDaoImpl.class);

	public CdsClrntIncr read(String clrntSysId, String incr) {
		CdsClrntIncr record = null;
		CdsClrntIncrPK pkey = new CdsClrntIncrPK();

		Session session = getSessionFactory().getCurrentSession();

		pkey.setClrntSysId(clrntSysId);
		pkey.setIncr(incr);

		try {
			record = (CdsClrntIncr) session.get(CdsClrntIncr.class, pkey);
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

	@SuppressWarnings("unchecked")
	public List<CdsClrntIncr> listForClrntSys(String clrntSysId) {
		List<CdsClrntIncr> recordList = null;
		Session session = sessionFactory.getCurrentSession();

		String hql = "from CdsClrntIncr cci where cci.clrntSysId like :clrntSysId";

		try {
			Query query = session.createQuery(hql.toString()).setParameter("clrntSysId", clrntSysId);
			recordList = query.list();
		} catch (HibernateException e) {
			String msg = "Error reading record with ClrntSysId %s : %s  ";
			logger.error(String.format(msg, clrntSysId, e.getMessage()));
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return recordList;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
}
