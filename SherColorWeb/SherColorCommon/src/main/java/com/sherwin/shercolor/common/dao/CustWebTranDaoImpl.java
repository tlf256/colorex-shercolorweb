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
import com.sherwin.shercolor.common.domain.CustWebTranPK;
import com.sherwin.shercolor.common.exception.SherColorException;

@Repository
public class CustWebTranDaoImpl implements CustWebTranDao{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	static Logger logger = Logger.getLogger(CustWebTranDaoImpl.class.getName());

	@Autowired
	private ResourceBundleMessageSource messageSource;
	
	@Autowired
	Locale locale;
	
	@Autowired
	CustWebJobFieldsDao jobFieldsDao;

	public boolean create(CustWebTran custWebTran){
		boolean result = false;

		Session session = getSessionFactory().getCurrentSession();

		try{
			// Get next control number for the customer
			int controlNbr = getNextControlNbr(custWebTran.getCustomerId());
			
			logger.info("inside dao create and controlNbr = " + controlNbr);
			
			if(controlNbr>0 && custWebTran!=null){
				logger.info("inside create and here are some values " + custWebTran.getCustomerId() + custWebTran.getColorType() + custWebTran.getColorComp());
				custWebTran.setControlNbr(controlNbr);
				custWebTran.setLineNbr(1);
				CustWebTran currentRecord = read(custWebTran.getCustomerId(), custWebTran.getControlNbr(), custWebTran.getLineNbr());
				if (currentRecord==null){
					logger.info("inside dao create before session.save ");
					session.save(custWebTran);
					logger.info("inside dao create after session.save ");
					result = true;
				} else {
					//Record already exists so get next control number didn't work 
					SherColorException se = new SherColorException();
					se.setCode(1501);
					se.setMessage(messageSource.getMessage("1501", new Object[] {}, locale ));
					String msg = "Error creating record : %s  ";
					logger.error(String.format(msg, se.getMessage()));
					throw se;
				}
			} else {
				SherColorException se = new SherColorException();
				se.setCode(1501);
				se.setMessage(messageSource.getMessage("1501", new Object[] {}, locale ));
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
	
	public CustWebTran read(String customerId, int controlNbr, int lineNbr){
		CustWebTran record = null;
		CustWebTranPK pkey = new CustWebTranPK();

		Session session = getSessionFactory().getCurrentSession();

		pkey.setCustomerId(customerId);
		pkey.setControlNbr(controlNbr);
		pkey.setLineNbr(lineNbr);
		
		try {
			record = (CustWebTran) session.get(CustWebTran.class, pkey);
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
	
	public boolean update(CustWebTran custWebTran){
		boolean result = false;

		Session session = getSessionFactory().getCurrentSession();

		try{
			session.update(custWebTran);
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
	
	public boolean delete(CustWebTran custWebTran){
		boolean result = false;
		 
		Session session = getSessionFactory().getCurrentSession();

		try{
			session.delete(custWebTran);
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
	public List<CustWebTran> listForCustomerId(String customerId){
		List<CustWebTran> recordList = new ArrayList<CustWebTran>();
		StringBuilder hql = new StringBuilder();

		Session session = getSessionFactory().getCurrentSession();

		hql.append("from CustWebTran cwt ");
		hql.append("where cwt.customerId = :customerId ");
		hql.append("order by cwt.controlNbr ");

		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("customerId",customerId);
			
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
	public List<CustWebTran> listForTranCriteria(CustWebTran cwtCriteria){
		List<CustWebTran> recordList = new ArrayList<CustWebTran>();
		StringBuilder hql = new StringBuilder();

		Session session = getSessionFactory().getCurrentSession();
		
		if(cwtCriteria.getCustomerId()!=null){

			hql.append("from CustWebTran cwt ");
			hql.append("where cwt.CustomerId = :customerId ");
			
			if(cwtCriteria.getJobField01()!=null && cwtCriteria.getJobField01().isEmpty())
				hql.append("  and cwt.JobField01 like :jobField01 ");
			if(cwtCriteria.getJobField01()!=null && cwtCriteria.getJobField01().isEmpty())
				hql.append("  and cwt.JobField02 like :jobField02 ");
			if(cwtCriteria.getJobField01()!=null && cwtCriteria.getJobField01().isEmpty())
				hql.append("  and cwt.JobField03 like :jobField03 ");
			if(cwtCriteria.getJobField01()!=null && cwtCriteria.getJobField01().isEmpty())
				hql.append("  and cwt.JobField04 like :jobField04 ");
			if(cwtCriteria.getJobField01()!=null && cwtCriteria.getJobField01().isEmpty())
				hql.append("  and cwt.JobField05 like :jobField05 ");
			if(cwtCriteria.getJobField01()!=null && cwtCriteria.getJobField01().isEmpty())
				hql.append("  and cwt.JobField06 like :jobField06 ");
			if(cwtCriteria.getJobField01()!=null && cwtCriteria.getJobField01().isEmpty())
				hql.append("  and cwt.JobField07 like :jobField07 ");
			if(cwtCriteria.getJobField01()!=null && cwtCriteria.getJobField01().isEmpty())
				hql.append("  and cwt.JobField08 like :jobField08 ");
			if(cwtCriteria.getJobField01()!=null && cwtCriteria.getJobField01().isEmpty())
				hql.append("  and cwt.JobField09 like :jobField09 ");
			if(cwtCriteria.getJobField01()!=null && cwtCriteria.getJobField01().isEmpty())
				hql.append("  and cwt.JobField10 like :jobField10 ");
			if(cwtCriteria.getColorId()!=null && !cwtCriteria.getColorId().isEmpty())
				hql.append("  and cwt.ColorId like :colorId ");
			if(cwtCriteria.getColorName()!=null && !cwtCriteria.getColorName().isEmpty())
				hql.append("  and cwt.ColorName like :colorName ");
			
			try {
				Query query = session.createQuery(hql.toString())
						.setParameter("colorComp",cwtCriteria.getCustomerId());
				if(cwtCriteria.getJobField01()!=null && !cwtCriteria.getJobField01().isEmpty())
					query.setParameter("jobField01",cwtCriteria.getJobField01() + "%");
				if(cwtCriteria.getJobField02()!=null && !cwtCriteria.getJobField02().isEmpty())
					query.setParameter("jobField02",cwtCriteria.getJobField02() + "%");
				if(cwtCriteria.getJobField03()!=null && !cwtCriteria.getJobField03().isEmpty())
					query.setParameter("jobField03",cwtCriteria.getJobField03() + "%");
				if(cwtCriteria.getJobField04()!=null && !cwtCriteria.getJobField04().isEmpty())
					query.setParameter("jobField04",cwtCriteria.getJobField04() + "%");
				if(cwtCriteria.getJobField05()!=null && !cwtCriteria.getJobField05().isEmpty())
					query.setParameter("jobField05",cwtCriteria.getJobField05() + "%");
				if(cwtCriteria.getJobField06()!=null && !cwtCriteria.getJobField06().isEmpty())
					query.setParameter("jobField06",cwtCriteria.getJobField06() + "%");
				if(cwtCriteria.getJobField07()!=null && !cwtCriteria.getJobField07().isEmpty())
					query.setParameter("jobField07",cwtCriteria.getJobField07() + "%");
				if(cwtCriteria.getJobField08()!=null && !cwtCriteria.getJobField08().isEmpty())
					query.setParameter("jobField08",cwtCriteria.getJobField08() + "%");
				if(cwtCriteria.getJobField09()!=null && !cwtCriteria.getJobField09().isEmpty())
					query.setParameter("jobField09",cwtCriteria.getJobField09() + "%");
				if(cwtCriteria.getJobField10()!=null && !cwtCriteria.getJobField10().isEmpty())
					query.setParameter("jobField10",cwtCriteria.getJobField10() + "%");
				if(cwtCriteria.getColorId()!=null && !cwtCriteria.getColorId().isEmpty())
					query.setParameter("colorId",cwtCriteria.getColorId() + "%");
				if(cwtCriteria.getColorName()!=null && !cwtCriteria.getColorName().isEmpty())
					query.setParameter("colorName",cwtCriteria.getColorName() + "%");
				
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
		} else {
			SherColorException se = new SherColorException();
			se.setCode(1502);
			se.setMessage(messageSource.getMessage("1502", new Object[] {}, locale ));
			logger.error(se.getMessage());
			throw se;
		}

		return recordList;
		
	}
	
	@SuppressWarnings("unchecked")
	private int getNextControlNbr(String customerId){
		Long lastControlNbr = null;
		int nextControlNbr = 0;
		Session session = getSessionFactory().getCurrentSession();
		
		try {
			String sql = "select ControlNbr, LineNbr from CustWebTran where CustomerId = :customerId order by controlNbr desc";

			System.out.println("in getnextcontrolnbr before createSQLQuery");
			Query query = session.createSQLQuery(sql)
					.addScalar("ControlNbr", new LongType())
					.addScalar("LineNbr", new LongType())
					.setParameter("customerId", customerId);
			
			System.out.println("in getnextcontrolnbr after createSQLQuery");
			List<Object[]> rows = query.list();
			System.out.println("in getnextcontrolnbr before looping through rows");
			for(Object[] row : rows){
				System.out.println("in getnextcontrolnbr inside loop through rows before setting lastControlNbr");
				if(row!=null && row[0]!=null) lastControlNbr = (Long) row[0];
				break;
			}
			System.out.println("in getnextcontrolnbr after looping through rows");
			
			if(lastControlNbr!=null) {
				nextControlNbr = lastControlNbr.intValue() + 1;
			} else {
				nextControlNbr = 1;
			}

			System.out.println("in getnextcontrolnbr after incrementing lastControlNbr");
			
		} catch (HibernateException e) {
			String msg = "Error reading all records : %s  ";
			logger.error(String.format(msg, e.getMessage()));
			e.printStackTrace();
			throw(e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return nextControlNbr;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	@SuppressWarnings("unchecked")
	public List<CustWebTran> listForControlNbr(int controlNbr){
		List<CustWebTran> recordList = new ArrayList<CustWebTran>();
		StringBuilder hql = new StringBuilder();

		Session session = getSessionFactory().getCurrentSession();

		hql.append("from CustWebTran cwt ");
		hql.append("where cwt.controlNbr = :controlNbr ");
		hql.append("order by cwt.lineNbr ");

		try {
			Query query = session.createQuery(hql.toString())
					.setParameter("controlNbr",controlNbr);
			
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
	

}
