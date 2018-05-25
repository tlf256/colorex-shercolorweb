package com.sherwin.shercolor.cal.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Cache;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.cal.domain.Boo;
import com.sherwin.shercolor.cal.domain.CalTemplate;
import com.sherwin.shercolor.cal.domain.Ecal;




@EnableTransactionManagement
@Repository
@Transactional
public class CalDaoImpl implements CalDao{
	
      private final static Logger log = LogManager.getLogger(CalDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	protected Session getCurrentSession(){
		return sessionFactory.getCurrentSession();
	}
	
	protected void ClearCache(){
		Cache cache = sessionFactory.getCache();

		if (cache != null) {
		  
		    cache.evictCollectionRegions();
		    cache.evictDefaultQueryRegion();
		    cache.evictEntityRegions();
		    cache.evictQueryRegions();
		    cache.evictNaturalIdRegions();
		}
	}
	
	 /* Read the file and returns the byte array
     * @param file
     * @return the bytes of the file
     */
	@Override
    public byte[] readFile(String file) {
        ByteArrayOutputStream bos = null;
        try {
            File f = new File(file);
            FileInputStream fis = new FileInputStream(f);
            byte[] buffer = new byte[1024];
            bos = new ByteArrayOutputStream();
            for (int len; (len = fis.read(buffer)) != -1;) {
                bos.write(buffer, 0, len);
            }
            fis.close();
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e2) {
            System.err.println(e2.getMessage());
        }
        return bos != null ? bos.toByteArray() : null;
    }
    
   
	 /* Read the file and returns the byte array
     * @param file
     * @return the bytes of the file
     */
	@Override
    public void writeFile(CalTemplate myblob) {
        InputStream input = null;
        try {
            File f = new File(myblob.getId());
            FileOutputStream fos = new FileOutputStream(f);
            Blob data = myblob.getData();
            try {
				input = data.getBinaryStream();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            byte[] buffer = new byte[1024];
            while (input.read(buffer) > 0) {
                fos.write(buffer);
            }
           
            fos.close();
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e2) {
            System.err.println(e2.getMessage());
        }
      
    }
  @Override  
 public void booHoo(){
	 Boo hoo = new Boo();
	 hoo.setHoo("ooooo");
	 getCurrentSession().save(hoo);
	 getCurrentSession().flush();
	// getCurrentSession().close();
	 
 }
    	
    	
    
    	@Override
    	public CalTemplate selectBlob(String filename)  {
        	
    		CalTemplate myblob=null;
			try {
				if (getCurrentSession() != null) {
					getCurrentSession().clear(); // internal cache clear
				}
				 Query query = getCurrentSession().createQuery("from CalTemplate where id = :filename");
				 query.setParameter("filename", filename);
				 myblob = (CalTemplate)query.uniqueResult();
				 //writeFile(myblob);
			} catch (HibernateException e) {
				e.printStackTrace();
			}
			return myblob;
		}
    	@Override
    	public Ecal selectEcal(String filename)  {
        	
    		Ecal myecal=null;
			try {
				if (getCurrentSession() != null) {
					getCurrentSession().clear(); // internal cache clear
				}
				 Query query = getCurrentSession().createQuery("from Ecal where filename = :filename");
				 query.setParameter("filename", filename);
				 myecal = (Ecal)query.uniqueResult();
				 //writeFile(myblob);
			} catch (HibernateException e) {
				e.printStackTrace();
			}
			return myecal;
		}
    	@Override
    	public void deleteEcal(String filename){
    		Ecal myecal=null;
			try {
				if (getCurrentSession() != null) {
					getCurrentSession().clear(); // internal cache clear
				}
				 Query query = getCurrentSession().createSQLQuery("delete from CUSTWEBECAL where filename = :filename");
				 query.setParameter("filename", filename);
				 query.executeUpdate();
			} catch (HibernateException e) {
				e.printStackTrace();
			}
    	}
    	@Override
    	public List<Ecal> getEcalList( String customerid,  String tintermodel,	String tinterserial){
    		List<Ecal> ecal = null;
    		
    		Query query = getCurrentSession().createQuery("from Ecal where customerid = :customerid, tintermodel=:tintermodel, tinterserial=:tinterserial order by uploaddate desc, uploadtime desc");
    		query.setParameter("customerid", customerid);
    		query.setParameter("tintermodel", tintermodel);
    		query.setParameter("tinterserial", tinterserial);
    		ecal =query.list();
    		return ecal;
    	}
    	
    	@Override
    	public List<Ecal> getEcalList( String customerid,  String colorantid, String tintermodel,	String tinterserial){
    		
    		List<Ecal> ecalList=null;
    		if(colorantid ==null){
    			ecalList = getEcalList(customerid,tintermodel,tinterserial);
    		}
    		else{
    		Query query = getCurrentSession().createQuery("from Ecal where customerid = :customerid and "
    			+ " colorantid=:colorantid and tintermodel=:tintermodel and tinterserial=:tinterserial"
    				+ " order by uploaddate desc, uploadtime desc");
    		query.setParameter("customerid", customerid);
    		query.setParameter("colorantid", colorantid);
    		query.setParameter("tintermodel", tintermodel);
    		query.setParameter("tinterserial", tinterserial);
    		ecalList = query.list();
    		}
    		return ecalList;
    	}
    	
    	@Override
    	public void uploadEcal(Ecal ecal){
    		try {
				if (getCurrentSession() != null) {
					getCurrentSession().clear(); // internal cache clear
				}
				System.out.println("Dao Saving.");
				if(ecal!=null && ecal.getCustomerid()!=null && ecal.getData() !=null){
					System.out.println("Dao Ecal not null.");
					getCurrentSession().save(ecal); 
					getCurrentSession().flush();
					System.out.println("Dao Saved.");

				}
				else if(ecal.getData()==null){
					System.out.println("No data in Ecal Upload");
				}
    		} catch (HibernateException e) {
				e.printStackTrace();
			}
    	}

	
    	
  	}
