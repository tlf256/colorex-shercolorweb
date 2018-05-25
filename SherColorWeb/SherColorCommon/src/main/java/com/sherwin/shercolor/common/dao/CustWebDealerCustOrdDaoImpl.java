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

import com.sherwin.shercolor.common.domain.CustWebDealerCustOrd;
import com.sherwin.shercolor.common.domain.CustWebDealerCustOrdPK;

@Repository
public class CustWebDealerCustOrdDaoImpl implements CustWebDealerCustOrdDao{

	@Autowired
	private SessionFactory sessionFactory;

	static Logger logger = Logger.getLogger(CustWebDealerDaoImpl.class.getName());
	
	@Override
	public boolean create(CustWebDealerCustOrd custWebDealerCustOrd){
		Session session = getSessionFactory().getCurrentSession();
		try{
			session.save(custWebDealerCustOrd);
			return true;
		}
		catch (HibernateException he){
			String msg = "Error CREATING CustWebDealerCustOrd record : %s";
			logger.error(String.format(msg, he.getMessage()));
			throw (he);
		}
		catch (Exception e){
			logger.error(e.getMessage());
			throw (e);
		}
	};
	
	@Override
	public CustWebDealerCustOrd read(String customerId, String dlrCustId, int controlNbr){
		CustWebDealerCustOrd custWebDealerCustOrd = null;
		CustWebDealerCustOrdPK pkey = new CustWebDealerCustOrdPK();
		
		Session session = getSessionFactory().getCurrentSession();

		pkey.setCustomerId(customerId);
		pkey.setDlrCustId(dlrCustId);
		pkey.setControlNbr(controlNbr);

		try {
			custWebDealerCustOrd = (CustWebDealerCustOrd) session.get(CustWebDealerCustOrd.class, pkey);
		} catch (HibernateException e){
			String msg = "Error READING CustWebDealerCustOrd record unique key %s : %s ";
			logger.error(String.format(msg, pkey.toString(), e.getMessage()));
			throw(e);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

		return custWebDealerCustOrd;
	}
	
	@Override
	public boolean update(CustWebDealerCustOrd custWebDealerCustOrd){
		Session session = getSessionFactory().getCurrentSession();
		try{
			session.update(custWebDealerCustOrd);
			return true;	
		}
		catch (HibernateException he){
			String msg = "Error UPDATING CustWebDealerCustOrd record : %s";
			logger.error(String.format(msg, he.getMessage()));
			throw (he);
		}
		catch (Exception e){
			logger.error(e.getMessage());
			throw (e);
		}	
	}
	
	@Override
	public boolean delete(CustWebDealerCustOrd custWebDealerCustOrd){
		Session session = getSessionFactory().getCurrentSession();
		try{
			session.delete(custWebDealerCustOrd);
			return true;	
		}
		catch (HibernateException he){
			String msg = "Error DELETING CustWebDealerCustOrd record : %s";
			logger.error(String.format(msg, he.getMessage()));
			throw (he);
		}
		catch (Exception e){
			logger.error(e.getMessage());
			throw (e);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CustWebDealerCustOrd> listCustOrders(String customerId, String dlrCustId){
		List<CustWebDealerCustOrd> listDealerCustOrds = new ArrayList<CustWebDealerCustOrd>();
		Session session = sessionFactory.getCurrentSession();
		String hql = "from CustWebDealerCustOrd d where d.customerId = :customerId and d.dlrCustId = :dlrCustId";
		
		try {
			Query query = session.createQuery(hql.toString()).setParameter("customerId", customerId)
					.setParameter("dlrCustId", dlrCustId);
			listDealerCustOrds = query.list();
			}
			catch (HibernateException he){
				String msg = "Error READING LIST CustWebDealerCust record list: %s";
				logger.error(String.format(msg, he.getMessage()));
				throw (he);
			}
			catch (Exception e){
				logger.error(e.getMessage());
				throw (e);
			}		return listDealerCustOrds;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static void setLogger(Logger logger) {
		CustWebDealerCustDaoImpl.logger = logger;
	};

}
