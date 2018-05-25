package com.sherwin.shercolor.common.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/** import java.util.List; */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CdsClrntCost;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional

public class CdsClrntCostDaoTest {

	@Autowired
	private CdsClrntCostDao target;

    /** always start with this - highlight and right clock to Run As JUnit Test */
	@Test
	public void testCreate() {
		assertNotNull(target);
	}
	
	@Test
	public void testSuccessReadCdsClrntCost() {
		String clrntSysId = "CCE";
		String tintSysId = "Y1";
		CdsClrntCost result = target.read(clrntSysId, tintSysId);
		if(result!=null) {
			System.out.println("Read by testSuccessReadCdsClrntCost is SUCCESSFUL" /** -> " + result.getClrntSysId() + " " + result.getTintSysId()*/ );
		}
		assertNotNull(result);
	}

	
	@Test
	public void testFailReadCdsClrntCostBogusClrntSys() {
		String clrntSysId = "809";
		String tintSysId = "Y1";
		CdsClrntCost result = target.read(clrntSysId, tintSysId);

		assertTrue(result == null);
   }
	

	@Test
	public void testFailReadCdsClrntCostBogusClrntId() {
		String clrntSysId = "CCE";
		String tintSysId = "Y7";
		CdsClrntCost result = target.read(clrntSysId, tintSysId);

		assertTrue(result == null);
   }

}
