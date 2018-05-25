package com.sherwin.shercolor.common.dao;

import static org.junit.Assert.*;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CustWebLoginTransform;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/spring/shercolorcommon.xml" })
@Transactional
public class CustWebLoginTransformDaoTest {

		@Autowired
		private CustWebLoginTransformDao target;

		@Test
		public void testCreate() {
			assertNotNull(target);
		}
		
		@Test
		public void testPositiveCreate() {
			CustWebLoginTransform webLoginXform = new CustWebLoginTransform();
			
			webLoginXform.setKeyField("shercolortest");
			webLoginXform.setCustomerId("CCF2");
			webLoginXform.setMasterAcctName("SC test acct");
			webLoginXform.setAcctComment("test Comment");
						
			boolean result = target.create(webLoginXform);
			assertTrue(result);
			
		}
		
		@Test
		public void testPositiveDelete() {
			CustWebLoginTransform webLoginXform = new CustWebLoginTransform();
			
			webLoginXform.setCustomerId("CCF");
			webLoginXform.setKeyField("shercolorpen");
			webLoginXform.setMasterAcctName("SC Penetration Tester");
			webLoginXform.setAcctComment("The Comment");

			
			boolean result = target.delete(webLoginXform);
			assertTrue(result);
			
		}

		@Test
		public void testPositiveRead() {
			String keyField = "shercolorpen";
			
			CustWebLoginTransform result = target.read(keyField);

			assertNotNull(result);
		}

		@Test
		public void testNegativeRead() {
			String keyField = "JUNK";
				
			CustWebLoginTransform result = target.read(keyField);

			assertNull(result);
		}
		
	}
