package com.sherwin.shercolor.common.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.sherwin.shercolor.common.domain.CdsColorBase;
import com.sherwin.shercolor.common.domain.CdsColorBasePK;

public class CdsColorBaseDaoImpl implements CdsColorBaseDao{
	@Autowired
	private SessionFactory sessionFactory;
	static Logger logger = LogManager.getLogger(CdsColorBaseDaoImpl.class);

	
	public CdsColorBase read(String colorComp, String colorId, String ieFlag, int seqNbr){
		CdsColorBase record = null;
		CdsColorBasePK pkey = new CdsColorBasePK();
		
		Session session = getSessionFactory().getCurrentSession();

		pkey.setColorComp(colorComp);
		pkey.setColorId(colorId);
		pkey.setIeFlag(ieFlag);
		pkey.setSeqNbr(seqNbr);
		
		try {
			record = (CdsColorBase) session.get(CdsColorBase.class,pkey);
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
	public List<CdsColorBase> listForColorCompIdIntExt(String colorComp, String colorId, String ieFlag){
		List<CdsColorBase> recordList = new ArrayList<CdsColorBase>();
		StringBuilder hql = new StringBuilder();

		Session session = getSessionFactory().getCurrentSession();

		hql.append("from CdsColorBase ccb ");
		hql.append("where ccb.colorComp = :colorComp ");
		hql.append("  and ccb.colorId = :colorId ");
		hql.append("  and ccb.ieFlag = :ieFlag ");

		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("colorComp",colorComp)
					.setParameter("colorId",  colorId)
					.setParameter("ieFlag", ieFlag);
			
			recordList = query.list();
			
		} catch (HibernateException e){
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, hql.toString(), e.getMessage()));
			e.printStackTrace();
			throw(e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return recordList;
	}
	
	@SuppressWarnings("unchecked")
	public List<CdsColorBase> listForceBasesForColorCompIdIntExt(String colorComp, String colorId, String ieFlag){
		List<CdsColorBase> recordList = new ArrayList<CdsColorBase>();
		StringBuilder hql = new StringBuilder();

		Session session = getSessionFactory().getCurrentSession();

		hql.append("from CdsColorBase ccb ");
		hql.append("where ccb.colorComp = :colorComp ");
		hql.append("  and ccb.colorId = :colorId ");
		hql.append("  and ccb.ieFlag = :ieFlag ");
		hql.append("  and ccb.seqNbr >= 100 ");
		hql.append("  and (ccb.base = 'BRIGHT YELLOW' or ccb.base = 'LIGHT YELLOW' or ccb.base = 'VIVID YELLOW' or ccb.base = 'YELLOW' or ");
		hql.append("       ccb.base = 'REAL RED' or ccb.base = 'PRIMARY RED' or ccb.base = 'RED') ");

		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("colorComp",colorComp)
					.setParameter("colorId",  colorId)
					.setParameter("ieFlag",  ieFlag);
			
			recordList = query.list();
			
		} catch (HibernateException e){
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, hql.toString(), e.getMessage()));
			e.printStackTrace();
			throw(e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return recordList;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<CdsColorBase> listForColorCompIdIntExtProdComp(String colorComp, String colorId, String ieFlag, String prodComp){
		List<CdsColorBase> recordList = new ArrayList<CdsColorBase>();
		StringBuilder hql = new StringBuilder();

		Session session = getSessionFactory().getCurrentSession();

		hql.append("from CdsColorBase ccb ");
		hql.append("where ccb.colorComp = :colorComp ");
		hql.append("  and ccb.colorId = :colorId ");
		hql.append("  and ccb.ieFlag = :ieFlag ");
		hql.append("  and ccb.prodComp = :prodComp ");

		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("colorComp",colorComp)
					.setParameter("colorId",  colorId)
					.setParameter("ieFlag", ieFlag)
					.setParameter("prodComp", prodComp);
			
			recordList = query.list();
			
		} catch (HibernateException e){
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, hql.toString(), e.getMessage()));
			e.printStackTrace();
			throw(e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		return recordList;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> listBasesForColorCompIdIntExtProdComp(String colorComp, String colorId, String ieFlag, String prodComp){
		List<String> result = new ArrayList<String>();
		List<CdsColorBase> recordList = new ArrayList<CdsColorBase>();
		StringBuilder hql = new StringBuilder();

		Session session = getSessionFactory().getCurrentSession();

		hql.append("from CdsColorBase ccb ");
		hql.append("where ccb.colorComp = :colorComp ");
		hql.append("  and ccb.colorId = :colorId ");
		hql.append("  and ccb.ieFlag = :ieFlag ");
		hql.append("  and ccb.prodComp = :prodComp ");

		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("colorComp",colorComp)
					.setParameter("colorId",  colorId)
					.setParameter("ieFlag", ieFlag)
					.setParameter("prodComp", prodComp);
			
			recordList = query.list();
			
		} catch (HibernateException e){
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, hql.toString(), e.getMessage()));
			e.printStackTrace();
			throw(e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		
		for(CdsColorBase item : recordList){
			result.add(item.getBase());
		}
		
		return result;
	}
	
	public List<CdsColorBase> listForColorCompId(String colorComp, String colorId){
		List<CdsColorBase> recordList = new ArrayList<CdsColorBase>();
		StringBuilder hql = new StringBuilder();

		Session session = getSessionFactory().getCurrentSession();

		hql.append("from CdsColorBase ccb ");
		hql.append("where ccb.colorComp = :colorComp ");
		hql.append("  and ccb.colorId = :colorId ");

		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("colorComp",colorComp)
					.setParameter("colorId",  colorId);
			
			recordList = query.list();
			
		} catch (HibernateException e){
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, hql.toString(), e.getMessage()));
			e.printStackTrace();
			throw(e);
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
