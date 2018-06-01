package com.sherwin.login.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.login.domain.SWUserComments;

@Repository
@Transactional
public class SWUserCommentDaoImpl implements SWUserCommentDao {
	static Logger logger = LogManager.getLogger(SWUserCommentDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public SWUserComments createOrUpdateEntry(SWUserComments instance) throws Exception {

		Session session = getSessionFactory().getCurrentSession();
		
		try {

			session.saveOrUpdate(instance);
			
		} catch (RuntimeException re) {
			logger.error("save failed", re);
		}
		
		return instance;
	 }
	

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

}
