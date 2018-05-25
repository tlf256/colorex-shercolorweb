package com.sherwin.shercolor.cal.dao;
//Done 10/21/2014
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Hibernate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.cal.dao.CalDao;
import com.sherwin.shercolor.cal.domain.CalTemplate;
import com.sherwin.shercolor.cal.domain.Ecal;



	@RunWith(SpringJUnit4ClassRunner.class)
	@ContextConfiguration(locations = {"classpath:spring.xml"})
	@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
	@Transactional

	public class DaoTest {
	// 05/04/2015 - Checked after new DE exception implemented.
		
		
		
		@Autowired
		private CalDao target;
		
		@Test
		public void testCreate() {
			System.out.println("testCreate");
			assertNotNull(target);
		}
		@Test
		public void testbooHoo(){
			target.booHoo();
		}
		@Test
		public void testinsert(){
			//target.insertBlob("c:\\templates\\", "CCE_COROBD600.zip");
		}
		@Test
		public void testEcalUpload(){
			final String UPLOADDIR="\\SWUploads\\";
			final String filename ="CCE_COROBD600_434344_20170912_1131.zip";
			Ecal ecal = new Ecal();
			Path path = Paths.get(UPLOADDIR + filename);
			byte[] data=null;
			try {
				data = Files.readAllBytes(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String[] arr = filename.split("_");
			String time = arr[4].substring(1, 4);
		    ecal.setCustomerid("TEST");
		    ecal.setColorantid(arr[0]);
		    ecal.setTintermodel(arr[1]);
		    ecal.setTinterserial(arr[2]);
		    ecal.setFilename(filename);
		    ecal.setUploaddate(arr[3]);
		    ecal.setUploadtime(time);
		    ecal.setData(data);
		    target.deleteEcal(filename);
		    target.uploadEcal(ecal);
		}
		@Test
		public void testGetEcalList(){
			final String filename ="CCE_COROBD600_434344_20170912_1131.zip";
			String[] arr = filename.split("_");
			String time = arr[4].substring(1, 4);
			String customerid = "TEST";
			String colorantid = arr[0];
			String tintermodel = arr[1];
			String tinterserial = arr[2];
			List<Ecal>  ecalList = target.getEcalList(customerid, colorantid, tintermodel, tinterserial);
			assertNotNull(ecalList);
			
		}
		@Test
		public void testSelectEcal(){
			final String filename ="CCE_COROBD600_434344_20170912_1131.zip";
			Ecal ecal = target.selectEcal(filename);
			assertNotNull(ecal);
		}
		@Test
		public void test(){
			Calendar cal = Calendar.getInstance();
			final DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			final DateFormat ttt = new SimpleDateFormat("HHmm");
			System.out.println(sdf.format(cal.getTime()));
			System.out.println(ttt.format(cal.getTime()));
		}
		
		  public byte[] readFile(File f) {
		        ByteArrayOutputStream bos = null;
		        try {
		            
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
		@Test
		public void testiterateFiles(){
			String myDirectoryPath="c:\\templates\\";
			Calendar cal = Calendar.getInstance();
			final DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			final DateFormat ttt = new SimpleDateFormat("HHmm");
			String date = (sdf.format(cal.getTime()));
			String time = (ttt.format(cal.getTime()));
			 File dir = new File(myDirectoryPath);
			  File[] directoryListing = dir.listFiles();
			  if (directoryListing != null) {
			    for (File file : directoryListing) {
			    	Ecal ecal = new Ecal();
			    	String[] arr = file.getName().split("_");
			    	ecal.setColorantid(arr[0]);
			    	ecal.setCustomerid("DEFAULT");
			    	ecal.setFilename(file.getName());
			    	String arr1 = arr[1];
			    	String[] arr2 = arr1.split("\\.");
			    	String model = arr2[0];
			    	ecal.setTintermodel(model);
			    	ecal.setTinterserial(null);
			    	ecal.setUploaddate(date);
			    	ecal.setUploadtime(time);
			    	ecal.setData(readFile(file));
			    	System.out.println(file.getName());
			    	try{
			    		//target.deleteEcal(file.getName());
			    	//	target.uploadEcal(ecal);
			    	}
			    	catch(Exception ex){
			    		System.out.println(ex.getMessage());
			    	}
			    	
			    }
			  }
		}

}
