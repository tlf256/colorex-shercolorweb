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

import com.sherwin.shercolor.common.domain.CdsProdFamily;
import com.sherwin.shercolor.common.domain.CdsProdFamilyPK;

public class CdsProdFamilyDaoImpl implements CdsProdFamilyDao {
	
	static Logger logger = LogManager.getLogger(CdsProdFamilyDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public CdsProdFamily read(String name, String prodNbr) {
		CdsProdFamily record = null;
		CdsProdFamilyPK pkey = new CdsProdFamilyPK();
		
		Session session = sessionFactory.getCurrentSession();

		pkey.setName(name);
		pkey.setProdNbr(prodNbr);
		
		try {
			record = (CdsProdFamily) session.get(CdsProdFamily.class,pkey);
		}
		catch (HibernateException e) {
			String msg = "Error reading %s record : %s  ";
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

	@SuppressWarnings("unchecked")
	public CdsProdFamily readByProdNbrPrimary(String prodNbr) {
		List<CdsProdFamily> recordList = null;
		
		Session session = sessionFactory.getCurrentSession();

		String hql = "from CdsProdFamily cpf where cpf.prodNbr like :prodNbr and cpf.primay = 1";
		
		// BMS Fixed 10/27/2016 - need to return the primary product family record for the prod number provided

		// Best code I have for non-pkey read where the key is expected to be unique.
		// Thinking this could return a list (see next method below) because a product could be used in more than 1 product family.
		// Think a red or yellow base product in one quality used in another family where red or yellow not available.
		// Therefore, this should not be a valid method.  Suggest we drop and use the last method below as alternative.
		// Different Query class methods are referenced in web examples, but these methods are not 
		// available in this application context. I will continue to look for better code example.
		
		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("prodNbr",prodNbr);
			recordList = query.list();
			if (recordList.size()>0) {
				  return recordList.get(0);
			}	  
		}
		catch (HibernateException e) {
			String msg = "Error reading %s record with prodNbr %s : %s  ";
			logger.error(String.format(msg, hql.toString(), prodNbr, e.getMessage()));
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
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<CdsProdFamily> listForProdNbr(String prodNbr) {
		List<CdsProdFamily> recordList = null;
		Session session = sessionFactory.getCurrentSession();

		// get full family for this family name
		String hql = "from CdsProdFamily cpf where cpf.prodNbr = :prodNbr order by cpf.name";
		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("prodNbr",prodNbr);
			recordList = query.list();
		} catch (HibernateException e){
			String msg = "Error reading %s record with prodNbr %s : %s  ";
			logger.error(String.format(msg, hql.toString(), prodNbr, e.getMessage()));
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return recordList;
	}

	@SuppressWarnings("unchecked")
	public List<CdsProdFamily> listFullFamilyForPrimaryProdNbr(String prodNbr, boolean strictlyPrimary) {
		List<CdsProdFamily> recordList = null;
		Session session = sessionFactory.getCurrentSession();

		try {
			// get primary family name for this product
			CdsProdFamily primaryProdFamily = readByProdNbrPrimary(prodNbr);
			
			//if primaryProdFamily is null, check if they want non primary listing (strictlyPrimary)
			//if strictlyPrimary then return the recordList as a new zero filled list.
			if (primaryProdFamily==null) {
				if(strictlyPrimary){
					recordList = new ArrayList<CdsProdFamily>();
				} else {
					// get list of family for prodNbr and pick first off list
					List<CdsProdFamily> altProdFamilyList = listForProdNbr(prodNbr);
					if(altProdFamilyList.size()>0){
						primaryProdFamily = altProdFamilyList.get(0);
					} else {
						// alternate didn't find any, return empty list
						recordList = new ArrayList<CdsProdFamily>();
					}
				}
			}
			
			if(primaryProdFamily!=null){
				String familyName = primaryProdFamily.getName();
				
				// get full family for this family name
				String hql = "from CdsProdFamily cpf where cpf.name = :familyName";
				try {
					Query query = session.createQuery(hql.toString())
							.setParameter("familyName",familyName);
					recordList = query.list();
				} catch (HibernateException e){
					String msg = "Error reading %s record with familyName %s : %s  ";
					logger.error(String.format(msg, hql.toString(), familyName, e.getMessage()));
					throw e;
				}
			} else {
				recordList = new ArrayList<CdsProdFamily>();
			}
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
