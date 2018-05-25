package com.sherwin.shercolor.common.dao;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CustWebDealerCust;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.List;

	@FixMethodOrder(MethodSorters.NAME_ASCENDING)
	@RunWith(SpringJUnit4ClassRunner.class)
	@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
	@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
	@Transactional
	public class CustWebDealerCustDaoTest {
		
		@Autowired
		private CustWebDealerCustDao target;

		private String customerId 		= "444444444";
		private String dlrCustId  		= "9999";
		private String dlrCustName 		= "Test Customer";
		private String dlrCustPhoneNbr 	= "999-999-9999";
		private String dlrCustAddress 	= "123 Main St.";
		private String dlrCustCity 		= "Cleveland";
		private String dlrCustState 	= "OH";
		private String dlrCustZip 		= "44115";
		private String dlrCustCountry 	= "US";
		private String dlrCustContact 	= "John Doe";
		private String updateUser 		= "JDD";

		private CustWebDealerCust custWebDealerCust = new CustWebDealerCust();

		@Before
		public void setupDealerCust() {
			custWebDealerCust.setCustomerId(customerId);
			custWebDealerCust.setDlrCustId(dlrCustId);
			custWebDealerCust.setDlrCustName(dlrCustName);
			custWebDealerCust.setDlrCustAddress(dlrCustAddress);
			custWebDealerCust.setDlrCustCity(dlrCustCity);
			custWebDealerCust.setDlrCustState(dlrCustState);
			custWebDealerCust.setDlrCustZip(dlrCustZip);
			custWebDealerCust.setDlrCustCountry(dlrCustCountry);
			custWebDealerCust.setDlrCustContact(dlrCustContact);
			custWebDealerCust.setDlrCustPhoneNbr(dlrCustPhoneNbr);
			java.util.Date convertedDate = null;
			String dateString = "10/24/2017"; 
		    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy"); 
		    try {
				convertedDate = dateFormat.parse(dateString);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			custWebDealerCust.setDateAdded(convertedDate);
			custWebDealerCust.setDateUpdated(convertedDate);
			custWebDealerCust.setUpdateUser(updateUser);
			System.out.println("Before - Create customer web dealer customer -> " + customerId + " " + dlrCustId 
					+ " " + dlrCustName);
		}
		
		@Test
		public void testaClassCreate() {
			assertNotNull(target);
			System.out.println("A. Create Class CustWebDealer record ");
		}
		
		@Test
		public void testbCreateNewDealerCust() {
			boolean result = target.create(custWebDealerCust);
			assertTrue(result);
			System.out.println("B. Create CustWebDealer record " + custWebDealerCust.getCustomerId() + " " +
					custWebDealerCust.getDlrCustId() + " result is " + result);
		}
		
		@Test
		public void testcReadDealerCust() {
			boolean result = target.create(custWebDealerCust);
			assertTrue(result);
			CustWebDealerCust custWebDealerCust = target.read(customerId, dlrCustId);
			assertNotNull(custWebDealerCust);
			System.out.println("C. Read CustWebDealer record " + custWebDealerCust.getCustomerId()
					+ " " + custWebDealerCust.getDlrCustId());
		}
		
		@Test
		public void testdReadDealerCustNotFound() {
			String customerId = "No Exist";
			String dlrCustId  = "No Exist";
			CustWebDealerCust custWebDealerCust = target.read(customerId, dlrCustId);
			assertNull(custWebDealerCust);
			System.out.println("D. Read CustWebDealer record " + customerId
					+ " not found ");
		}
		
		@Test
		public void testeUpdateDealerCust() {
			boolean result = target.create(custWebDealerCust);
			assertTrue(result);
			CustWebDealerCust custWebDealerCust = target.read(customerId, dlrCustId);
			custWebDealerCust.setDlrCustContact("John Smith");
			result = target.update(custWebDealerCust);
			assertTrue(result);
			System.out.println("E. Update CustWebDealer record " + custWebDealerCust.getCustomerId() + " " +
					custWebDealerCust.getDlrCustId() + " result is " + result);
		}

		@Test
		public void testfGetDealerCustList() {
			boolean result = target.create(custWebDealerCust);
			assertTrue(result);
			List<CustWebDealerCust> listCustWebDealerCust = target.listCustomers(customerId);
			assertNotNull(listCustWebDealerCust);
			System.out.println("F. List CustWebDealerCust records " + listCustWebDealerCust.get(0).getCustomerId() + 
					" count in list is " + listCustWebDealerCust.size());
		}

		@Test
		public void testgGetDealerCustListAutocompleteByNumber() {
			boolean result = target.create(custWebDealerCust);
			assertTrue(result);
			String customerId = "444444444";
			String partialKey = "9";
			List<CustWebDealerCust> listCustWebDealerCust = target.listCustomersAutocomplete(customerId, partialKey);
			assertNotNull(listCustWebDealerCust);
			System.out.println("G. List CustWebDealerCust Autocomplete records " + customerId + "  " + partialKey +
					" count in list is" + listCustWebDealerCust.size());
		}
		
		@Test
		public void testhGetDealerCustListAutocompleteByName() {
			boolean result = target.create(custWebDealerCust);
			assertTrue(result);
			String customerId = "444444444";
			String partialKey = "T";
			List<CustWebDealerCust> listCustWebDealerCust = target.listCustomersAutocomplete(customerId, partialKey);
			assertNotNull(listCustWebDealerCust);
			System.out.println("H. List CustWebDealerCust autocomplete records " + customerId + 	" " + partialKey);
		}
		
		@Test
		public void testiDeleteDealerCust() {
			boolean result = target.create(custWebDealerCust);
			assertTrue(result);
			result = target.delete(custWebDealerCust);
			assertTrue(result);
			System.out.println("I. Delete CustWebDealer record " + custWebDealerCust.getCustomerId() + " result is " + result);
		}

}
