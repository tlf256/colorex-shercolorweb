package com.sherwin.shercolor.common.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.beans.IntrospectionException;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.dao.CdsProdDao;
import com.sherwin.shercolor.common.util.JavaBeanTester;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional
public class OeServiceCdsProdTest {

	@Autowired
	CdsProdDao cdsProdDao;
	
	private OeServiceCdsProd target;
	
	@Test
	public void testCreate() {
		target = new OeServiceCdsProd();
		assertNotNull(target);
	}
	
	@Test
	public void testBeanProperties() {
		try {
			JavaBeanTester.test(OeServiceCdsProd.class, "lastUpdate");
		} catch (IntrospectionException e) {
			fail();
		}
		
		target = new OeServiceCdsProd();
		
		Date lastUpdate = new Date();
		
		target.setLastUpdate(lastUpdate);
	}

	@Test
	public void testAssignValue() {
		try {
			target = new OeServiceCdsProd();
			
			Date lastUpdate = new Date();
			
			target.setLastUpdate(lastUpdate);
		} catch (Exception e) {
			fail();
		}
		
	}

	@Test
	public void testCreateFromCdsProd() {
		try {
			CdsProd cdsProd = cdsProdDao.read("640413571");
			
			System.out.println("read in cdsProd, quality is " + cdsProd.getQuality());
			
			target = new OeServiceCdsProd(cdsProd);
			
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		
	}


	

}
