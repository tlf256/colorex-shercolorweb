package com.sherwin.shercolor.common.dao;


import static org.junit.Assert.assertNotNull;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CdsProdList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional

//@Rollback

public class CdsProdListDaoTest {

	@Autowired
	CdsProdListDao target;
	
	@Test
	public void testCreate() {
		assertNotNull(target);
	}
	
	@Test
	public void testPositiveReadPk() {
		String salesNbr ="650592058";
		String prodListId ="NO ZVOC";
		CdsProdList result = target.read(prodListId,salesNbr);
		if (result!=null) {
			System.out.println("Read by prodListId and salesNbr -> " + result.getProdListId() +  " " + result.getSalesNbr());
		}
		assertNotNull(result);
	}

	@Test
	public void testFailedReadPk() {
		String salesNbr ="999999999";
		String prodListId ="NO NO";
		CdsProdList result = target.read(prodListId,salesNbr);
		if (result == null) {
			System.out.println("Read by prodListId and salesNbr NOT FOUND - Test SUCCESSFUL!");
		}
		assertTrue(result == null);
	}
	
	@Test
	public void testPositiveGetAllExclusions() {
		String salesNbr ="100342294";

		List<CdsProdList> result = target.getAllExclusions(salesNbr);
		if (result!=null) {
			System.out.println("GetAllExclusions returned a list of size -> " + result.size());
		}
		assertNotNull(result);
		if (result.size()!=1) {
			fail();
		}
	}

	@Test
	public void testPositiveMultipleGetAllExclusions() {
		String salesNbr ="100535731";

		List<CdsProdList> result = target.getAllExclusions(salesNbr);
		if (result!=null) {
			System.out.println("GetAllExclusions returned a list of size -> " + result.size());
		}
		assertNotNull(result);
		if (result.size()!=2) {
			fail();
		}
	}
	
	@Test
	public void testNegativeGetAllExclusions() {
		String salesNbr ="99999999";

		List<CdsProdList> result = target.getAllExclusions(salesNbr);
		if (result!=null) {
			System.out.println("GetAllExclusions returned a list of size -> " + result.size());
		}
		if (result.size()!=0) {
			fail();
		}
	}
	
	
}
