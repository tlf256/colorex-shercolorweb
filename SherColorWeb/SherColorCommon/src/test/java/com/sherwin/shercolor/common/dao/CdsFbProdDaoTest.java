package com.sherwin.shercolor.common.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CdsFbProd;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional

public class CdsFbProdDaoTest {

	@Autowired
	CdsFbProdDao target;
	
	@Test
	public void testCreate() {
		assertNotNull(target);
	}
	
	@Test
	public void testPositiveReadPk() {
		String prodComp = "SW";
		String salesNbr = "640385811";
		String fbBase = "B21W10251";
		CdsFbProd result = target.read(prodComp,salesNbr,fbBase);
		if (result!=null) {
			System.out.println("Read by product company, sales number and formula book base Test is SUCCESSFUL!");
		}
		assertNotNull(result);
	}

	@Test
	public void testFailedReadPk() {
		String prodComp = "SW";
		String salesNbr = "640385811";
		String fbBase = "B61W10251";
		CdsFbProd result = target.read(prodComp,salesNbr,fbBase);
		if (result == null) {
			System.out.println("Read by product company, sales number and formula book base  NOT FOUND - Test is SUCCESSFUL!");
		}
		assertTrue(result == null);
	}
	

}
