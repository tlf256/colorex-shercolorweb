package com.sherwin.shercolor.common.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import com.sherwin.shercolor.common.domain.CustWebDealerCustOrd;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.List;

	@FixMethodOrder(MethodSorters.NAME_ASCENDING)
	@RunWith(SpringJUnit4ClassRunner.class)
	@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
	@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
	@Transactional
	public class CustWebDealerCustOrdDaoTest {
		
		@Autowired
		private CustWebDealerCustOrdDao target;

		private String customerId 		= "444444444";
		private String dlrCustId  		= "9999";
		private int    controlNbr       = 12345; 
		private String custOrderNbr 	= "ABC12345";
		private String comments 		= "Need delivery by 10/26/2017";
		private String updateUser 		= "JDD";

		private CustWebDealerCustOrd custWebDealerCustOrd = new CustWebDealerCustOrd();

		@Before
		public void setupDealerCust() {
			java.util.Date convertedDate = null;
			String dateString = "10/24/2017"; 
		    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy"); 
		    try {
				convertedDate = dateFormat.parse(dateString);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			custWebDealerCustOrd.setCustomerId(customerId);
			custWebDealerCustOrd.setDlrCustId(dlrCustId);
			custWebDealerCustOrd.setControlNbr(controlNbr);
			custWebDealerCustOrd.setCustOrderNbr(custOrderNbr);
			custWebDealerCustOrd.setComments(comments);
			custWebDealerCustOrd.setDateAdded(convertedDate);
			custWebDealerCustOrd.setDateUpdated(convertedDate);
			custWebDealerCustOrd.setUpdateUser(updateUser);
			System.out.println("Before - Create customer web dealer customer -> " + customerId + " " + dlrCustId 
					+ " " + controlNbr);
		}
		
		@Test
		public void testaClassCreate() {
			assertNotNull(target);
			System.out.println("A. Create Class CustWebDealerCustOrd record ");
		}
		
		@Test
		public void testbCreateNewDealerCustOrd() {
			boolean result = target.create(custWebDealerCustOrd);
			assertTrue(result);
			System.out.println("B. Create CustWebDealerCustOrd record " + custWebDealerCustOrd.getCustomerId() + " " +
					custWebDealerCustOrd.getDlrCustId() + " " + custWebDealerCustOrd.getControlNbr() + 
					" result is " + result);
		}
		
		@Test
		public void testcReadDealerCustOrd() {
			boolean result = target.create(custWebDealerCustOrd);
			assertTrue(result);
			CustWebDealerCustOrd custWebDealerCustOrd  = target.read(customerId, dlrCustId, controlNbr);
			assertNotNull(custWebDealerCustOrd);
			System.out.println("C. Read CustWebDealerOrd record " + custWebDealerCustOrd.getCustomerId() + " "
				 + custWebDealerCustOrd.getDlrCustId() + " " + custWebDealerCustOrd.getControlNbr());
		}
		
		@Test
		public void testdReadDealerCustOrdNotFound() {
			String customerId = "666666666";
			String dlrCustId = "6666";
			int controlNbr = 666666;
			CustWebDealerCustOrd custWebDealerCustOrd  = target.read(customerId, dlrCustId, controlNbr);
			assertNull(custWebDealerCustOrd);
			System.out.println("D. Read CustWebDealerOrd record not found " + customerId + " "
				 + dlrCustId + " " + controlNbr);
		}
		
		@Test
		public void testeUpdateDealerCustOrd() {
			boolean result = target.create(custWebDealerCustOrd);
			assertTrue(result);
			custWebDealerCustOrd.setComments("Update delivery date is 10/25/2017 5pm");
			result = target.update(custWebDealerCustOrd);
			System.out.println("E. Update CustWebDealer record " + custWebDealerCustOrd.getCustomerId() + " " +
					custWebDealerCustOrd.getDlrCustId() + " " +
					custWebDealerCustOrd.getControlNbr() + " " + custWebDealerCustOrd.getComments() +
					" result is " + result);
		}

		@Test
		public void testfGetDealerCustOrdList() {
			boolean result = target.create(custWebDealerCustOrd);
			assertTrue(result);
			String customerId = "444444444";
			String dlrCustId = "9999";
			List<CustWebDealerCustOrd> listCustWebDealerCustOrd = target.listCustOrders(customerId, dlrCustId);
			assertNotNull(listCustWebDealerCustOrd);
			System.out.println("F. List CustWebDealer records  for " + listCustWebDealerCustOrd.get(0).getCustomerId() + " " +
					listCustWebDealerCustOrd.get(0).getDlrCustId() + " " +
					listCustWebDealerCustOrd.get(0).getControlNbr() + " " +
					" - count in list is " +  listCustWebDealerCustOrd.size());
		}
		
		@Test
		public void testgGetDealerCustOrdListNotFound() {
			String customerId = "No Exist";
			String dlrCustId = "No Exist";
			List<CustWebDealerCustOrd> listCustWebDealerCustOrd = target.listCustOrders(customerId, dlrCustId);
			assertNotNull(listCustWebDealerCustOrd);
			System.out.println("G. List CustWebDealer records not found for " + customerId + " " + dlrCustId +  
					" - count in list is " +  listCustWebDealerCustOrd.size());
		}
		
		@Test
		public void testhDeleteDealerCustOrd() {
			boolean result = target.create(custWebDealerCustOrd);
			assertTrue(result);
			result = target.delete(custWebDealerCustOrd);
			assertTrue(result);
			System.out.println("H. Delete CustWebDealer record " + customerId +  
					" " + dlrCustId + " " + controlNbr + " result is " + result);
		}

}
