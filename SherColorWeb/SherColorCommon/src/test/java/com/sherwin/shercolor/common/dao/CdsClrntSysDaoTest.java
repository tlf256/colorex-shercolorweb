package com.sherwin.shercolor.common.dao;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CdsClrntSys;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional

public class CdsClrntSysDaoTest {

	@Autowired
	private CdsClrntSysDao target;

    /** always start with this - highlight and right clock to Run As JUnit Test */
	@Test
	public void testCreate() {
		assertNotNull(target);
	}
	
	@Test
	public void testSuccessReadCdsClrntSys() {
		String clrntSysId ="CCE";
		CdsClrntSys result = target.read(clrntSysId);
		if(result!=null) {
			System.out.println("Read by testSuccessReadCdsClrntSys is SUCCESSFUL -> " + result.getClrntSysId()+ " " + result.getClrntSysName());
		}
		assertNotNull(result);
	}

	@Test
	public void testFailedReadCdsClrntSys() {
		String clrntSysId ="JUNK";
		CdsClrntSys result = target.read(clrntSysId);
		assertTrue(result == null);
	}

	@Test
	public void testSuccessActiveOnlyYes() {
	   //String clrntSysId = "CCE";
	   Boolean activeOnly = true;
	   List<CdsClrntSys> result = target.listForClrntSysId(activeOnly); 
	   if(result!=null){
/* 			System.out.println(result.getClrntSysId()+ " " + result.getClrntSysName() + " " + result.getEffDate() + " " + result.getExpDate()); */
			System.out.println("Test of SuccessActiveOnlyYes Successful, result count is " + result.size());
		}

		assertNotNull(result);
		if (result.size()!=8) {
			fail();
		}
	}

	@Test
	public void testSuccessActiveOnlyNo() {
	  // String clrntSysId = "CHR";
	   Boolean activeOnly = false;
	   List<CdsClrntSys> result = target.listForClrntSysId(activeOnly); 
	   if(result!=null){
/* 			System.out.println(result.getClrntSysId()+ " " + result.getClrntSysName() + " " + result.getEffDate() + " " + result.getExpDate()); */
			System.out.println("Test of SuccessActiveOnlyNo Successful, result count is " + result.size());
		}

		assertNotNull(result);
		if (result.size()!=17) {
			fail();
		}
	}

}
