package com.sherwin.shercolor.common.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CustWebParms;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/spring/shercolorcommon.xml" })
@Transactional
public class CustWebParmsDaoTest {

	@Autowired
	private CustWebParmsDao target;

	@Test
	public void testCreate() {
		assertNotNull(target);
	}

	@Test
	public void testPositiveRead() {

		String customerId = "DRM";
		int seqNbr = 1;


		CustWebParms result = target.read(customerId, seqNbr);

		assertNotNull(result);
	}

	@Test
	public void testNegativeRead() {
		String customerId = "JUNK";
		int seqNbr = 0;

		CustWebParms result = target.read(customerId, seqNbr);

		assertNull(result);
	}


	@Test
	public void testPositiveReadByCustId() {
		String customerId = "DRM";

		CustWebParms result = target.readByCustId(customerId);

		assertNotNull(result);
	}


	@Test
	public void testNegativeReadByCustId() {
		String customerId = "JUNK";

		CustWebParms result = target.readByCustId(customerId);

		assertNull(result);
	}
	
	@Test
	public void testPositiveReadAllByCustId() {
		String customerId = "CCF";

		List<CustWebParms> result = target.readAllByCustId(customerId);

		System.out.println("CustWebParms count for " + customerId + " is " + result.size());
		assertNotNull(result);
	}
	
	@Test
	public void testNegativeReadAllByCustId() {
		String customerId = "JUNK";

		List<CustWebParms> result = target.readAllByCustId(customerId);

		System.out.println("CustWebParms count for " + customerId + " is " + result.size());
		assertNotNull(result);
	}
}
