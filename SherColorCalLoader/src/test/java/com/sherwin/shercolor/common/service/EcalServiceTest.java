package com.sherwin.shercolor.common.service;

//Done 10/21/2014
import static org.junit.Assert.*;


import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import com.sherwin.shercolor.common.dao.CustWebEcalDao;
import com.sherwin.shercolor.common.domain.CustWebEcal;
import com.sherwin.shercolor.common.service.EcalService;


	@RunWith(SpringJUnit4ClassRunner.class)
	@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})

	
	public class EcalServiceTest {
	// 05/04/2015 - Checked after new DE exception implemented.
		
		
		public CustWebEcalDao MockDao(){
			
			
			return ( new CustWebEcalDao(){
				List<CustWebEcal> ecalList = new ArrayList<CustWebEcal>();
				CustWebEcal myEcal = new CustWebEcal();
			
				
				public void fill(){
					CustWebEcal ecal = new CustWebEcal();
					ecal.setCustomerid("12345");
					ecalList.add(ecal);
				}
				
				

				@Override
				public List<CustWebEcal> getEcalTemplate(String colorantid, String tintermodel) {
					CustWebEcal ecal = new CustWebEcal();
					ecal.setCustomerid("DEFAULT");
					ecalList.removeAll(ecalList);
					ecalList.add(ecal);
					return ecalList;
				}



				@Override
				public List<CustWebEcal> getEcalList(String customerid) {
					fill();
					return ecalList;
				}



				@Override
				public List<CustWebEcal> getEcalList(String customerid, String tintermodel, String tinterserial) {
					fill();
					return ecalList;
				}

				@Override
				public List<CustWebEcal> getEcalList(String customerid, String colorantid, String tintermodel,
						String tinterserial) {
					fill();
						return ecalList;
				}

				@Override
				public int uploadEcal(CustWebEcal ecal) {
					// TODO Auto-generated method stub	
					return 0;
				}

				

				@Override
				public CustWebEcal selectGData(String colorantId) {
					myEcal.setColorantid(colorantId);
					return myEcal;
				}



				@Override
				public CustWebEcal selectEcal(String filename) {
					myEcal.setFilename(filename);
					return myEcal;
				}

				@Override
				public int deleteEcal(String filename) {
					return 1;				
				}
				
			});
		}
		
		@Autowired
		private EcalService target;
		
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
			CustWebEcal ecal = target.selectEcal(filename);
			assertEquals(filename,ecal.getFilename());
		}
	
		@Test
		public void testgetEcalTemplate(){
			
			String colorantid="CCE";
			String tintermodel="CorobD600";
		List<CustWebEcal> l = target.getEcalTemplate(colorantid, tintermodel);
		assertEquals(1,l.size());
		}	
		
		@Test
		public void  testgetEcalList(){
			String customerid="12345";
			String colorantid="CCE";
			String tintermodel="CorobD600";
			String tinterserial="12345";
			List<CustWebEcal> list = target.getEcalList(customerid, colorantid, tintermodel, tinterserial);
			assertNotNull(list.get(0));
		}
		
		@Test
		public void selectGData(){
			String colorantId = "CCE";
			CustWebEcal ecal=target.selectGData(colorantId);
			assertNotNull(ecal);
		}
		
		@Test
		public void selectEcal(){
			String filename = "test";
			CustWebEcal ecal=target.selectEcal(filename);
			assertNotNull(ecal);
		}
		@Test
		public void uploadEcal(){
			CustWebEcal ecal = new CustWebEcal();
			target.uploadEcal(ecal);
		}
		

}
