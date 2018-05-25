package com.sherwin.shercolor.common.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CustWebDealer;
import com.sherwin.shercolor.common.domain.CustWebDealerCust;
import com.sherwin.shercolor.common.domain.CustWebDealerCustOrd;

import org.springframework.test.context.transaction.TransactionConfiguration;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Before;

		@FixMethodOrder(MethodSorters.NAME_ASCENDING)
		@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
		@RunWith(SpringJUnit4ClassRunner.class)
		@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
		@Transactional
		public class CustomerOrderServiceTest {
			
			@Autowired
			private CustomerOrderService target;

			private CustWebDealer custWebDealer = new CustWebDealer();

			private CustWebDealerCust custWebDealerCust = new CustWebDealerCust();

			private CustWebDealerCustOrd custWebDealerCustOrd = new CustWebDealerCustOrd();

			private String customerId 		= "444444444";
			private String dealerName 		= "Test Dealer";
			private String dlrPhoneNbr 		= "999-999-9999";

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
			private int    homeStore 		= 4314;
			
			private int    controlNbr       = 12345; 
			private String custOrderNbr 	= "ABC12345";
			private String comments 		= "Test Comments";


			@Before
			public void setupRecords() {
				java.util.Date convertedDate = null;
				String dateString = "10/24/2017"; 
			    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy"); 
			    try {
					convertedDate = dateFormat.parse(dateString);
				} catch (Exception e) {
					e.printStackTrace();
				} 
				
			    custWebDealer.setCustomerId(customerId);
			    custWebDealer.setDealerName(dealerName);
			    custWebDealer.setDlrPhoneNbr(dlrPhoneNbr);
				custWebDealer.setComments(comments);
				custWebDealer.setHomeStore(homeStore);
				//System.out.println("\tSet-up customer web dealer record " + custWebDealer.getCustomerId());
			
			    custWebDealerCust.setCustomerId(customerId);
			    custWebDealerCust.setDlrCustId(dlrCustId);
			    custWebDealerCust.setDlrCustName(dlrCustName);
			    custWebDealerCust.setDlrCustPhoneNbr(dlrCustPhoneNbr);
			    custWebDealerCust.setDlrCustAddress(dlrCustAddress);
			    custWebDealerCust.setDlrCustCity(dlrCustCity);
			    custWebDealerCust.setDlrCustState(dlrCustState);
			    custWebDealerCust.setDlrCustZip(dlrCustZip);
			    custWebDealerCust.setDlrCustCountry(dlrCustCountry);
			    custWebDealerCust.setDlrCustContact(dlrCustContact);
			    custWebDealerCust.setUpdateUser(updateUser);
				custWebDealerCust.setDateAdded(convertedDate);
				custWebDealerCust.setDateUpdated(convertedDate);
			    custWebDealerCust.setComments(comments);
				//System.out.println("\tSet-up customer web dealer customer record " + custWebDealer.getCustomerId() + 
				//		" " + custWebDealerCust.getDlrCustId());
			
			    custWebDealerCustOrd.setCustomerId(customerId);
			    custWebDealerCustOrd.setDlrCustId(dlrCustId);
			    custWebDealerCustOrd.setControlNbr(controlNbr);
			    custWebDealerCustOrd.setCustOrderNbr(custOrderNbr);
			    custWebDealerCustOrd.setComments(comments);
			    custWebDealerCustOrd.setDateAdded(convertedDate);
			    custWebDealerCustOrd.setDateUpdated(convertedDate);
			    custWebDealerCustOrd.setUpdateUser(updateUser);
				//System.out.println("\tSet-up customer web dealer customer order record " + custWebDealer.getCustomerId() + 
				//		" " + custWebDealerCust.getDlrCustId() + " " + custWebDealerCustOrd.getControlNbr());
			}
			
			
			@Test
			public void testaClassCreate() {
				assertNotNull(target);
				System.out.println("A. Call service - create Class CustOrderService record ");
			}
			
			@Test
			public void testbCreateNewDealer() {
				boolean result = target.createDealer(custWebDealer);
				assertNotNull(result);
				System.out.println("B. Call service - create new dealer - " + result);
			}
			
			@Test
			public void testcReadDealer() {
				target.createDealer(custWebDealer);
				assertNotNull(custWebDealer);
				System.out.println("\tCreate test dealer ");
				CustWebDealer result = target.readDealer(customerId);
				assertNotNull(result);
				System.out.println("C. Call service - read dealer - " + result.getCustomerId());
			}
			
			@Test
			public void testdReadDealerNotFound() {
				CustWebDealer result = target.readDealer("NOT EXIST");
				assertNull(result);
				System.out.println("D. Call service - read dealer not found - " + result);
			}
			
			@Test
			public void testeUpdateDealer() {
				target.createDealer(custWebDealer);
				assertNotNull(custWebDealer);
				System.out.println("\tCreate test dealer ");
				boolean result = target.updateDealer(custWebDealer);
				assertTrue(result);
				System.out.println("E. Call service - update dealer - " + result);
			}

			@Test
			public void testfGetDealerList() {
				boolean result = target.createDealer(custWebDealer);
				assertTrue(result);
				System.out.println("\tCreate test dealer ");
				List<CustWebDealer> custWebDealerList = target.listDealers();
				assertFalse(custWebDealerList.isEmpty());
				System.out.println("F. Call service - list dealer - list count is  " + custWebDealerList.size());
			}
			
			@Test
			public void testgDeleteDealer() {
				boolean result = target.createDealer(custWebDealer);
				assertTrue(result);
				System.out.println("\tCreate test dealer ");
				result = target.deleteDealer(custWebDealer);
				assertTrue(result);
				System.out.println("H. Call service - delete dealer - " + result);
			}

			@Test
			public void testiCreateNewDealerCust() {
				boolean result = target.createDealerCust(custWebDealerCust);
				assertTrue(result);
				System.out.println("I. Call service - create new dealer customer - " + result);
			}
			
			@Test
			public void testjReadDealerCust() {
				boolean result = target.createDealer(custWebDealer);
				assertTrue(result);
				System.out.println("\tCreate test dealer customer ");
				result = target.createDealerCust(custWebDealerCust);
				CustWebDealerCust custWebDealerCust = target.readDealerCust(customerId, dlrCustId);
				assertNotNull(custWebDealerCust);
				System.out.println("J. Call service - read dealer cust - " + custWebDealerCust.getCustomerId() + 
						" " + custWebDealerCust.getDlrCustId());
			}
			
			@Test
			public void testkReadDealerCustNotFound() {
				CustWebDealerCust result = target.readDealerCust("NOT EXIST", "NOT EXIST");
				assertNull(result);
				System.out.println("K. Call service - read dealer cust not found - " + result);
			}
			
			@Test
			public void testlUpdateDealerCust() {
				boolean result = target.createDealerCust(custWebDealerCust);
				assertTrue(result);
				System.out.println("\tCreate test dealer customer ");
				result = target.updateDealerCust(custWebDealerCust);
				assertTrue(result);
				System.out.println("L. Call service - update dealer cust - " + result);
			}

			@Test
			public void testnDealerCustListNotFound() {
				List<CustWebDealerCust> result = target.listDealerCustomers("NOT EXIST");
				assertTrue(result.isEmpty());
				System.out.println("N. Call service list dealer not found  - list count is " + result.size());
			}
			
			@Test
			public void testmGetDealerCustList() {
				boolean result = target.createDealerCust(custWebDealerCust);
				assertTrue(result);
				System.out.println("\tCreate test dealer customer ");
				List<CustWebDealerCust> custWebDealerCust = target.listDealerCustomers("888888888");
				assertFalse(custWebDealerCust.isEmpty());
				System.out.println("M. Call service list dealer -  list count is " + custWebDealerCust.size()  );
			}
			
			@Test
			public void testoDeleteDealerCust() {
				boolean result = target.createDealerCust(custWebDealerCust);
				assertTrue(result);
				System.out.println("\tCreate test dealer customer ");
				result = target.deleteDealerCust(custWebDealerCust);
				assertTrue(result);
				System.out.println("O. Call service - update dealer - " + result);
			}

			@Test
			public void testpCreateNewDealerCustOrd() {
				boolean result = target.createDealerCustOrd(custWebDealerCustOrd);
				assertTrue(result);
				System.out.println("P. Call service - create new dealer customer order - " + result);
			}
			
			@Test
			public void testqReadDealerCustOrd() {
				boolean result = target.createDealerCustOrd(custWebDealerCustOrd);
				assertTrue(result);
				System.out.println("\tCreate test dealer customer order");
				CustWebDealerCustOrd custWebDealerCustOrd = target.readDealerCustOrd(customerId, dlrCustId, controlNbr);
				assertNotNull(custWebDealerCustOrd);
				System.out.println("Q. Call service - read dealer customer order - " + custWebDealerCustOrd.getCustomerId() + " " 
						+ custWebDealerCustOrd.getDlrCustId() + " " + custWebDealerCustOrd.getControlNbr());
			}
			
			@Test
			public void testrReadDealerCustOrdNotFound() {
				CustWebDealerCustOrd result = target.readDealerCustOrd("NOT EXIST", "NOT EXIST", 0);
				assertNull(result);
				System.out.println("R. Call service - read dealer cust ord not found");
			}
			
			@Test
			public void testsUpdateDealerCustOrd() {
				boolean result = target.createDealerCustOrd(custWebDealerCustOrd);
				assertTrue(result);
				System.out.println("\tCreate test dealer customer order");
				result = target.updateDealerCustOrd(custWebDealerCustOrd);
				assertTrue(result);
				System.out.println("S. Call service - update dealer cust ord - " + result);
			}

			@Test
			public void testtGetDealerCustOrdList() {
				boolean result = target.createDealerCustOrd(custWebDealerCustOrd);
				assertTrue(result);
				System.out.println("\tCreate test dealer customer order");
				List<CustWebDealerCustOrd> custWebDealerCustOrd = target.listDealerCustOrders(customerId, dlrCustId);
				assertNotNull(result);
				System.out.println("T. Call service - list dealer customer orders - list count is " 
						+  custWebDealerCustOrd.size());
			}
			
			@Test
			public void testuGetDealerCustOrdListNotFound() {
				List<CustWebDealerCustOrd> result = target.listDealerCustOrders("NOT EXIST", "NOT EXIST");
				assertTrue(result.isEmpty());
				System.out.println("U. Call service - list dealer customer orders not found ");
			}
			
			@Test
			public void testvDeleteDealerCustOrd() {
				boolean result = target.createDealerCustOrd(custWebDealerCustOrd);
				assertTrue(result);
				System.out.println("\tCreate test dealer customer order");
				result = target.deleteDealerCustOrd(custWebDealerCustOrd);
				assertTrue(result);
				System.out.println("V. Call service - delete dealer customer order - " + result);
			}

			@Test
			public void testwDealerCustListAutoNotFound() {
				List<CustWebDealerCust> result = null;
				result = target.listDealerCustomersAutocomplete("NOT EXIST", "NOT");
				assertTrue(result.isEmpty());
				System.out.println("W. Call service - list dealer cust autocomplete not found ");
			}
			
			@Test
			public void testxGetDealerCustListAuto() {
				boolean result = target.createDealerCustOrd(custWebDealerCustOrd);
				assertTrue(result);
				System.out.println("\tCreate test dealer customer order");
				List<CustWebDealerCust> custWebDealerCust = target.listDealerCustomersAutocomplete("999999999", "9");
				assertFalse(custWebDealerCust.isEmpty());
				System.out.println("X. Call service list dealer cust autocomplete - list count is " + custWebDealerCust.size() );
			}

}
