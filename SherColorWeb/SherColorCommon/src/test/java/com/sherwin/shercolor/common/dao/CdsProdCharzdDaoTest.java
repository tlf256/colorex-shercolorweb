package com.sherwin.shercolor.common.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CdsProdCharzd;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional

public class CdsProdCharzdDaoTest {


	@Autowired
	CdsProdCharzdDao target;
	
	@Test
	public void testCreate() {
		assertNotNull(target);
	}
	
	@Test
	public void testPositiveReadPk() {
		String prodNbr ="B20W02651";
		String clrntSysId ="CCE";
		CdsProdCharzd result = target.read(prodNbr,clrntSysId);
		if (result!=null) {
			System.out.println("Read by prodNbr -> " + result.getProdNbr() + " " + result.getClrntSysId() + " Test is SUCCESSFUL!");
		}
		assertNotNull(result);
	}

	@Test
	public void testFailedReadPk() {
		String prodNbr ="B20W02659";
		String clrntSysId ="CCC";
		CdsProdCharzd result = target.read(prodNbr,clrntSysId);
		if (result == null) {
			System.out.println("Read by prodNbr NOT FOUND - Test is SUCCESSFUL!");
		}
		assertTrue(result == null);
	}
	
	@Test 
	public void testListForProdNbr(){
		String prodNbr ="B20W02651";
		boolean activeOnly = true;
		List<CdsProdCharzd> result = target.listForProdNbr(prodNbr, activeOnly);
		
		if (result!=null) {
			System.out.println("List by Product Number retrieved  - test is SUCCESSFUL!");
		}
		assertNotNull(result);
	}
	
	@Test 
	public void testFalseListForProdNbr(){
		String prodNbr ="B20W02658";
		boolean activeOnly = true;
		List<CdsProdCharzd> result = target.listForProdNbr(prodNbr, activeOnly);
		
		if (result.isEmpty()) {
			System.out.println("List by Product Number NOT retrieved  - test is SUCCESSFUL!");
		}
		assertNotNull(result);
	}
	

	
}
