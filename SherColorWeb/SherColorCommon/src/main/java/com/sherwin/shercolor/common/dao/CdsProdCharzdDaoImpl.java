package com.sherwin.shercolor.common.dao;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.sherwin.shercolor.common.domain.CdsProdCharzd;
import com.sherwin.shercolor.common.domain.CdsProdCharzdPK;

@Repository
@Transactional
public class CdsProdCharzdDaoImpl implements CdsProdCharzdDao {
	
	static Logger logger = LogManager.getLogger(CdsProdCharzdDaoImpl.class);
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public CdsProdCharzd read(String prodNbr, String clrntSysId){
		CdsProdCharzd record = null;
		CdsProdCharzdPK pkey = new CdsProdCharzdPK();
		
		Session session = sessionFactory.getCurrentSession();

		pkey.setProdNbr(prodNbr);
		pkey.setClrntSysId(clrntSysId);

		try {
			record = (CdsProdCharzd) session.get(CdsProdCharzd.class,pkey);
		}
		catch (HibernateException e) {
			String msg = "Error reading %s record : %s  ";
			logger.error(String.format(msg, pkey.toString(), e.getMessage()));
			throw e;
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
	};
	
	
	@SuppressWarnings("unchecked")
	public List<CdsProdCharzd> listForProdNbr(String prodNbr, boolean activeOnly){
			List<CdsProdCharzd> recordList = null;
			Session session = sessionFactory.getCurrentSession();
			String activeValue = "";
			
			if (activeOnly == true){
				activeValue = "ACT";
			}
			else{
				activeValue = "DIS";
			}
			
			String hql = "from CdsProdCharzd cpc where cpc.prodNbr like :prodNbr and actStatus = :activeOnly";
			try {
				Query query = session.createQuery(hql.toString())
					.setParameter("prodNbr",prodNbr)
					.setParameter("activeOnly", activeValue);
				recordList = query.list();
			} catch (HibernateException e){
				String msg = "Error reading record with prodNbr %s and activeOnly %s : %s  ";
				logger.error(String.format(msg, prodNbr, activeValue, e.getMessage()));
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
	};
	
	
	}
