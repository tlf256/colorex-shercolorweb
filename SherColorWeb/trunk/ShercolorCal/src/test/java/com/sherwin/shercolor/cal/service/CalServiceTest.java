package com.sherwin.shercolor.cal.service;
//Done 10/21/2014
import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
import com.sherwin.shercolor.cal.service.CalService;


	@RunWith(SpringJUnit4ClassRunner.class)
	@ContextConfiguration(locations = {"classpath:spring.xml"})
	@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
	@Transactional

	public class CalServiceTest {
	// 05/04/2015 - Checked after new DE exception implemented.
		
		
		public CalDao MockDao(){
			
			
			return ( new CalDao(){
				List<Ecal> ecalList = new ArrayList<Ecal>();
				Ecal myEcal = new Ecal();
			
				
				public void fill(){
					Ecal ecal = new Ecal();
					ecal.setCustomerid("12345");
					ecalList.add(ecal);
				}
				
				@Override
				public byte[] readFile(String file) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public void writeFile(CalTemplate myblob) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public CalTemplate selectBlob(String filename) {
					
					return new CalTemplate();
				}

				@Override
				public void booHoo() {
					// TODO Auto-generated method stub
					
				}

				@Override
				public List<Ecal> getEcalList(String customerid, String tintermodel, String tinterserial) {
					fill();
					return ecalList;
				}

				@Override
				public List<Ecal> getEcalList(String customerid, String colorantid, String tintermodel,
						String tinterserial) {
					fill();
						return ecalList;
				}

				@Override
				public void uploadEcal(Ecal ecal) {
					// TODO Auto-generated method stub				
				}

				

				@Override
				public Ecal selectEcal(String filename) {
					myEcal.setFilename(filename);
					return myEcal;
				}

				@Override
				public void deleteEcal(String filename) {
					// TODO Auto-generated method stub				
				}
				
			});
		}
		
		@Autowired
		private CalService target;
		
		@Before
		public void setUp(){
			
			target.setDao(MockDao());
		}
		
		
		@Test
		public void testCreate() {
			System.out.println("testCreate");
			assertNotNull(target);
		}
		
		
		@Test
		public void testselectEcal(){
			String filename = "test";
			Ecal ecal = target.selectEcal(filename);
			assertEquals(filename,ecal.getFilename());
		}
		@Test
		public void  testgetCalTemplate(){
			String filename = "test";
			CalTemplate t = target.getCalTemplate(filename);
					assertNotNull(t);
		}
		
		@Test
		public void  testgetEcalList(){
			String customerid="12345";
			String colorantid="CCE";
			String tintermodel="CorobD600";
			String tinterserial="12345";
			List<Ecal> list = target.getEcalList(customerid, colorantid, tintermodel, tinterserial);
			assertNotNull(list.get(0));
		}
		
		@Test
		public void selectEcal(){
			String filename = "test";
			Ecal ecal=target.selectEcal(filename);
			assertNotNull(ecal);
		}
		@Test
		public void uploadEcal(){
			Ecal ecal = new Ecal();
			target.uploadEcal(ecal);
		}
		

}
