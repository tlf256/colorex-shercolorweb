package com.sherwin.shercolor.common.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.sherwin.shercolor.common.domain.CustWebDealerCust;
import com.sherwin.shercolor.common.domain.CustWebDealerCustPK;

@Repository
public class CustWebDealerCustDaoImpl implements CustWebDealerCustDao{

	@Autowired
	private SessionFactory sessionFactory;

	static Logger logger = Logger.getLogger(CustWebDealerDaoImpl.class.getName());
	
	public boolean create(CustWebDealerCust custWebDealerCust){
		Session session = getSessionFactory().getCurrentSession();
		try{
			session.save(custWebDealerCust);
			return true;
		}
		catch (HibernateException he){
			String msg = "Error CREATING CustWebDealerCust record : %s";
			logger.error(String.format(msg, he.getMessage()));
			throw (he);
		}
		catch (Exception e){
			logger.error(e.getMessage());
			throw (e);
		}
	};
	
	@Override
	public CustWebDealerCust read(String customerId, String dlrCustId){
		CustWebDealerCust custWebDealerCust = null;
		CustWebDealerCustPK pkey = new CustWebDealerCustPK();
		
		Session session = getSessionFactory().getCurrentSession();

		pkey.setCustomerId(customerId);
		pkey.setDlrCustId(dlrCustId);
		
		try {
			custWebDealerCust = (CustWebDealerCust) session.get(CustWebDealerCust.class, pkey);
		} catch (HibernateException e){
			String msg = "Error READING CustWebDealerCust record by unique key %s : %s ";
			logger.error(String.format(msg, pkey.toString(), e.getMessage()));
			throw(e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return custWebDealerCust;
	}
	
	@Override
	public boolean update(CustWebDealerCust custWebDealerCust){
		Session session = getSessionFactory().getCurrentSession();
		try{
			session.update(custWebDealerCust);
			return true;	
		}
		catch (HibernateException he){
			String msg = "Error UPDATING CustWebDealerCust record : %s";
			logger.error(String.format(msg, he.getMessage()));
			throw (he);
		}
		catch (Exception e){
			logger.error(e.getMessage());
			throw (e);
		}	
	}
	
	@Override
	public boolean delete(CustWebDealerCust custWebDealerCust){
		Session session = getSessionFactory().getCurrentSession();
		try{
			session.delete(custWebDealerCust);
			return true;	
		}
		catch (HibernateException he){
			String msg = "Error DELETING CustWebDealerCust record : %s";
			logger.error(String.format(msg, he.getMessage()));
			throw (he);
		}
		catch (Exception e){
			logger.error(e.getMessage());
			throw (e);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<CustWebDealerCust> listCustomers(String customerId){
		List<CustWebDealerCust> listDealerCustomers = new ArrayList<CustWebDealerCust>();
		Session session = sessionFactory.getCurrentSession();
		String hql = "from CustWebDealerCust d where d.customerId = :customerId";
		
		try {
			Query query = session.createQuery(hql.toString()).setParameter("customerId", customerId);
			listDealerCustomers = query.list();
			}
			catch (HibernateException he){
				String msg = "Error READING LIST CustWebDealerCust list: %s";
				logger.error(String.format(msg, he.getMessage()));
				throw (he);
			}
			catch (Exception e){
				logger.error(e.getMessage());
				throw (e);
			}
		return listDealerCustomers;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CustWebDealerCust> listCustomersAutocomplete(String customerId, String partialKey){
		String dlrCustId = partialKey + "%";
		String dlrCustName = partialKey + "%";
		List<CustWebDealerCust> listDealerCustomers = new ArrayList<CustWebDealerCust>();
		Session session = sessionFactory.getCurrentSession();
		String hql = "from CustWebDealerCust d where d.customerId like :customerId" +
			 " and (d.dlrCustId like :dlrCustId or d.dlrCustName like :dlrCustName)";
		customerId = customerId + "%";
		try {
			Query query = session.createQuery(hql.toString()).setParameter("customerId", customerId)
					.setParameter("dlrCustId", dlrCustId)
 					.setParameter("dlrCustName", dlrCustName);
			listDealerCustomers = query.list();
			}
			catch (HibernateException he){
				String msg = "Error READING for AUTOCOMPLETE CustWebDealerCust record list: %s";
				logger.error(String.format(msg, he.getMessage()));
				throw (he);
			}
			catch (Exception e){
				logger.error(e.getMessage());
				throw (e);
			}
		return listDealerCustomers;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		CustWebDealerCustDaoImpl.logger = logger;
	};

}
