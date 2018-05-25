package com.sherwin.shercolor.common.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

import java.util.List;

import com.sherwin.shercolor.common.domain.CustWebActiveProds;

	@RunWith(SpringJUnit4ClassRunner.class)
	@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
	@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
	@Transactional
	public class CustWebActiveProdsDaoImplTest {
		
		@Autowired
		private CustWebActiveProdsDao target;

		String salesnbrs[] = { "650096571", "650096589", "650096597", "640357968", "640357976", "650186935", 
				"650869928", "650866262", "650187693"	}; 
		
		String salesnbrsnot[] = { "650096570", "650096580", "650096590", "650096600", "650096610", "650175930", 
				"650187620", "650187630", "650187650" };	 
		
		String rexnbrs[] = { "A98W01251", "A98W01253", "A98T01254" }; 
		
		String rexnbrsnot[] = { "A98W01252", "A98W01254", "A98T01256" }; 

		@Test
		public void testCreate() {
			assertNotNull(target);
		}
		
		@Test
		public void getAllActiveProds() {
			List<CustWebActiveProds> result; // = new ArrayList<CustWebActiveProds>();
			result = target.getAllActiveProds();
			assertNotNull(result);
			System.out.println("1. All CUSTWEBACTIVEPRODS have been listed - number of records " + result.size());
		}
		
		@Test
		public void getActiveProdBySalesNbr() {
			for (int x = 0; x < 9; x++){
				String salesnbr = salesnbrs[x];
				CustWebActiveProds result = target.getActiveProdBySalesNbr(salesnbr);
				System.out.println("2. Sales number found -> " + salesnbr + " " + result.getProdNbr() + "-" + result.getSizeCd() 
					+ " " + result.getUpc());
				assertNotNull(result);
			}
		}
		
		@Test
		public void getActiveProdBySalesNbrNotFound() {
			for (int x = 0; x < 9; x++){
				String salesnbr = salesnbrsnot[x];
				CustWebActiveProds result = target.getActiveProdBySalesNbr(salesnbr);
				System.out.println("3. Sales number not found -> " + salesnbr);
				assertNull(result);
			}
		}

		@Test
		public void getActiveProdByRexSize() {
			for (int x = 0; x < 3; x++){
				String rexnbr = rexnbrs[x];
				CustWebActiveProds result = target.getActiveProdByRexSize(rexnbr, "14");
				System.out.println("4. Product found -> " + rexnbr + " " + result.getProdNbr() + "-" + result.getSizeCd()
					+ " " + result.getUpc());
				assertNotNull(result);
			}
		}
		
		@Test
		public void getActiveProdByRexSizeNotFound() {
			for (int x = 0; x < 3; x++){
				String rexnbr = rexnbrsnot[x];
				CustWebActiveProds result = target.getActiveProdByRexSize(rexnbr, "14");
				System.out.println("5. Product not found -> " + rexnbr);
				assertNull(result);
			}
		}

}

