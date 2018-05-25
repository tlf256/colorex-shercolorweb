package com.sherwin.shercolor.common.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;



import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CdsClrntList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional

public class CdsClrntListDaoTest {

	@Autowired
	private CdsClrntListDao target;

    /** always start with this - highlight and right clock to Run As JUnit Test */
	@Test
	public void testCreate() {
		assertNotNull(target);
	}
	
	@Test
	public void testSuccessReadCdsClrntList() {
		String clrntListId ="LIMIT-1";
		String clrntSysId = "CCE";
		String tintSysId = "Y1";
		CdsClrntList result = target.read(clrntListId, clrntSysId, tintSysId);
		if(result!=null) {
			System.out.println("Read by testSuccessRedCdsClrntList is SUCCESSFUL -> " + result.getClrntListId() + " " + result.getClrntSysId() + " " + result.getTintSysId());
		}
		assertNotNull(result);
	}

	
	@Test
	public void testFailReadCdsClrntListBogusList() {
		String clrntListId ="LIMIT-9";
		String clrntSysId = "CCE";
		String tintSysId = "Y1";
		CdsClrntList result = target.read(clrntListId, clrntSysId, tintSysId);

		assertTrue(result == null);
   }

	@Test
	public void testFailReadCdsClrntListBogusClrntSys() {
		String clrntListId ="LIMIT-1";
		String clrntSysId = "809";
		String tintSysId = "Y1";
		CdsClrntList result = target.read(clrntListId, clrntSysId, tintSysId);

		assertTrue(result == null);
   }
	

	@Test
	public void testFailReadCdsClrntListBogusClrntId() {
		String clrntListId ="LIMIT-1";
		String clrntSysId = "CCE";
		String tintSysId = "Y7";
		CdsClrntList result = target.read(clrntListId, clrntSysId, tintSysId);

		assertTrue(result == null);
   }

}
