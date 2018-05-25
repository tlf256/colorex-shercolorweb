package com.sherwin.shercolor.common.dao;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.junit.runners.MethodSorters;

import com.sherwin.shercolor.common.domain.CustWebDealer;

import static org.junit.Assert.*;

import java.util.List;


	@FixMethodOrder(MethodSorters.NAME_ASCENDING)
	@RunWith(SpringJUnit4ClassRunner.class)
	@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
	@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
	@Transactional
	public class CustWebDealerDaoTest {
		
		@Autowired
		private CustWebDealerDao target;

		private CustWebDealer custWebDealer = new CustWebDealer();

		private String customerId 	= "444444444";
		private String dealerName 	= "Test Dealer";
		private String dlrPhoneNbr 	= "999-999-9999";
		private int    homeStore 	= 4314;
		private String comment 		= "Dao Test Dealer";

		@Before
		public void setupDealer() {
			custWebDealer.setCustomerId(customerId);
			custWebDealer.setDealerName(dealerName);
			custWebDealer.setDlrPhoneNbr(dlrPhoneNbr);
			custWebDealer.setHomeStore(homeStore);
			custWebDealer.setComments(comment);
			System.out.println("Before - Setup customer web dealer -> " + customerId);
		}
		
		@Test
		public void testaClassCreate() {
			assertNotNull(target);
			System.out.println("Test A. Class Create CustWebDealer record success ");
		}
		
		@Test
		public void testbCreateNewDealer() {
			boolean result = target.create(custWebDealer);
			assertTrue(result);
			System.out.println("Test B. Create CustWebDealer record " + custWebDealer.getCustomerId() + 
					" result is " + result);
		}

		@Test
		public void testcReadDealer() {
			boolean result = target.create(custWebDealer);
			assertNotNull(result);
			CustWebDealer custWebDealer = target.read(customerId);
			assertNotNull(custWebDealer);
			System.out.println("Test C. Read CustWebDealer record " + custWebDealer.getCustomerId());
		}
		
				
		@Test
		public void testdReadDealerNotFound() {
			CustWebDealer custWebDealer = target.read("NOT EXIST");
			assertNull(custWebDealer);
			System.out.println("Test D. Read CustWebDealer record not found ");
		}
		
		@Test
		public void testeUpdateDealer() {
			boolean result = target.create(custWebDealer);
			assertTrue(result);
			result = target.update(custWebDealer);
			assertTrue(result);
			System.out.println("Test E. Update CustWebDealer record " + custWebDealer.getCustomerId() + 
					" result is " + result);
		}

		@Test
		public void testfGetDealerList() {
			boolean result = target.create(custWebDealer);
			assertTrue(result);
			result = target.update(custWebDealer);
			List<CustWebDealer> listCustWebDealer = target.listDealers();
			assertFalse(listCustWebDealer.isEmpty());
			System.out.println("Test F. List CustWebDealer records - list count is " + listCustWebDealer.size());
		}
		
		@Test
		public void testgDeleteDealer() {
			boolean result = target.create(custWebDealer);
			assertTrue(result);
			result = target.delete(custWebDealer);
			assertTrue(result);
			System.out.println("Test H. Delete CustWebDealer record " + customerId + " result is " + result);
		}

}



