package com.sherwin.shercolor.common.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.beans.IntrospectionException;
import java.util.Date;

import org.junit.Test;

import com.sherwin.shercolor.common.domain.CustWebParms;
import com.sherwin.shercolor.common.util.JavaBeanTester;


public class CustWebParmsTest {

	private CustWebParms target;
	
	@Test
	public void testCreate() {
		target = new CustWebParms();
		assertNotNull(target);
	}
	
	@Test
	public void testBeanProperties() {
		try {
			JavaBeanTester.test(CustWebParms.class, "lastUpd");
		} catch (IntrospectionException e) {
			fail();
		}
		
		target = new CustWebParms();
		
		Date lastUpdate = new Date();
		
		target.setLastUpd(lastUpdate);
	}

}
