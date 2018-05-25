package com.sherwin.shercolor.common.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Repository;

import com.sherwin.shercolor.common.domain.CustWebTran;
import com.sherwin.shercolor.common.domain.CustWebTranCorr;
import com.sherwin.shercolor.common.domain.CustWebTranCorrPK;
import com.sherwin.shercolor.common.domain.CustWebTranPK;
import com.sherwin.shercolor.common.exception.SherColorException;

@Repository
public class CustWebTranCorrDaoImpl implements CustWebTranCorrDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	static Logger logger = Logger.getLogger(CustWebTranCorrDaoImpl.class.getName());

	@Autowired
	private ResourceBundleMessageSource messageSource;
	
	@Autowired
	Locale locale;
	
	public boolean create(CustWebTranCorr custWebTranCorr){
		boolean result = false;

		Session session = getSessionFactory().getCurrentSession();

		try{
			// get next step for this correction
			System.out.println("in dao, getting next step number");
			int nextStep = getNextStep(custWebTranCorr.getCustomerId(), custWebTranCorr.getControlNbr(), custWebTranCorr.getLineNbr(), custWebTranCorr.getCycle(), custWebTranCorr.getUnitNbr());
			System.out.println("in dao, next step number is" + nextStep);

			custWebTranCorr.setStep(nextStep);
			
			if(custWebTranCorr!=null){
				CustWebTranCorr currentRecord = read(custWebTranCorr.getCustomerId(), custWebTranCorr.getControlNbr(), custWebTranCorr.getLineNbr(), custWebTranCorr.getCycle(), custWebTranCorr.getUnitNbr(), custWebTranCorr.getStep());
				if (currentRecord==null){
					session.save(custWebTranCorr);
					result = true;
				} else {
					//Record already exists so get next control number didn't work 
					SherColorException se = new SherColorException();
					se.setCode(1531);
					se.setMessage(messageSource.getMessage("1531", new Object[] {}, locale ));
					String msg = "Error creating record : %s  ";
					logger.error(String.format(msg, se.getMessage()));
					throw se;
				}
			} else {
				SherColorException se = new SherColorException();
				se.setCode(1532);
				se.setMessage(messageSource.getMessage("1532", new Object[] {}, locale ));
				String msg = "Error creating record : %s  ";
				logger.error(String.format(msg, se.getMessage()));
				throw se;
			}
		} catch (HibernateException he) {
			String msg = "Error creating record : %s  ";
			logger.error(String.format(msg, he.getMessage()));
			he.printStackTrace();
			throw(he);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
 
		logger.info("end of create and about to return result equal to " + result);
		return result;
		
	}
	
	public CustWebTranCorr read(String customerId, int controlNbr, int lineNbr, int cycle, int unitNbr, int step){
		CustWebTranCorr record = null;
		CustWebTranCorrPK pkey = new CustWebTranCorrPK();

		Session session = getSessionFactory().getCurrentSession();

		pkey.setCustomerId(customerId);
		pkey.setControlNbr(controlNbr);
		pkey.setLineNbr(lineNbr);
		pkey.setCycle(cycle);
		pkey.setUnitNbr(unitNbr);
		pkey.setStep(step);
		
		try {
			record = (CustWebTranCorr) session.get(CustWebTranCorr.class, pkey);
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
	
	public boolean update(CustWebTranCorr custWebTranCorr){
		boolean result = false;

		Session session = getSessionFactory().getCurrentSession();

		try{
			session.update(custWebTranCorr);
			result = true;
		} catch (HibernateException e) {
			String msg = "Error updating record : %s  ";
			logger.error(String.format(msg, e.getMessage()));
			e.printStackTrace();
			throw(e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
 
		return result;
		
	}
	
	public boolean delete(CustWebTranCorr custWebTranCorr){
		boolean result = false;
		 
		Session session = getSessionFactory().getCurrentSession();

		try{
			session.delete(custWebTranCorr);
			result = true;
		} catch (HibernateException e) {
			String msg = "Error creating record : %s  ";
			logger.error(String.format(msg, e.getMessage()));
			e.printStackTrace();
			throw(e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
 
		return result;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<CustWebTranCorr> listForCustomerOrderLine(String customerId, int controlNbr, int lineNbr){
		List<CustWebTranCorr> recordList = new ArrayList<CustWebTranCorr>();
		StringBuilder hql = new StringBuilder();

		Session session = getSessionFactory().getCurrentSession();

		hql.append("from CustWebTranCorr cwtc ");
		hql.append("where cwtc.customerId = :customerId ");
		hql.append("  and cwtc.controlNbr = :controlNbr ");
		hql.append("  and cwtc.lineNbr = :lineNbr ");
		hql.append("order by cwtc.cycle, cwtc.unitNbr, cwtc.step ");

		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("customerId",customerId)
					.setParameter("controlNbr",controlNbr)
					.setParameter("lineNbr",lineNbr)
					;
			
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
	private int getNextStep(String customerId, int controlNbr, int lineNbr, int cycle, int unitNbr){
		Long lastStep = null;
		int nextStep = 0;
		Session session = getSessionFactory().getCurrentSession();
		StringBuilder sql = new StringBuilder();
		
		try {
			sql.append("select Step ");
			sql.append("from CustWebTranCorr cwtc ");
			sql.append("where cwtc.customerId = :customerId ");
			sql.append("  and cwtc.controlNbr = :controlNbr ");
			sql.append("  and cwtc.lineNbr = :lineNbr ");
			sql.append("  and cwtc.cycle = :cycle ");
			sql.append("  and cwtc.unitNbr = :unitNbr ");
			sql.append("order by cwtc.step desc ");
			
			Query query = session.createSQLQuery(sql.toString())
					.addScalar("step", new LongType())
					.setParameter("customerId", customerId)
					.setParameter("controlNbr", controlNbr)
					.setParameter("lineNbr", lineNbr)
					.setParameter("cycle", cycle)
					.setParameter("unitNbr", unitNbr)
					;

			//List<Object[]> stepNumbers = query.list();
			List<Long> stepNumbers = query.list();
			for(Long row : stepNumbers){
				if(row!=null) lastStep = (Long) row;
				break;
			}
			
			if(lastStep!=null) {
				nextStep = lastStep.intValue() + 1;
			} else {
				nextStep = 1;
			}

			
		} catch (HibernateException e) {
			String msg = "Error reading all records : %s  ";
			logger.error(String.format(msg, e.getMessage()));
			e.printStackTrace();
			throw(e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return nextStep;
	}

	
	@SuppressWarnings("unchecked")
	public List<CustWebTranCorr> listForTranCorrCycleUnit(String customerId, int controlNbr, int lineNbr, int cycle, int unitNbr){
		List<CustWebTranCorr> recordList = new ArrayList<CustWebTranCorr>();
		StringBuilder hql = new StringBuilder();

		Session session = getSessionFactory().getCurrentSession();

		hql.append("from CustWebTranCorr cwtc ");
		hql.append("where cwtc.customerId = :customerId ");
		hql.append("  and cwtc.controlNbr = :controlNbr ");
		hql.append("  and cwtc.lineNbr = :lineNbr ");
		hql.append("  and cwtc.cycle = :cycle ");
		hql.append("  and cwtc.unitNbr = :unitNbr ");
		hql.append("order by cwtc.step ");

		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("customerId",customerId)
					.setParameter("controlNbr",controlNbr)
					.setParameter("lineNbr",lineNbr)
					.setParameter("cycle",cycle)
					.setParameter("unitNbr",unitNbr)
					;
			
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
