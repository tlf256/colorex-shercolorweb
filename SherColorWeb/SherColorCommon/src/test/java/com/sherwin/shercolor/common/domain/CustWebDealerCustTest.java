package com.sherwin.shercolor.common.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.beans.IntrospectionException;
import java.util.Date;

import org.junit.Test;

import com.sherwin.shercolor.common.domain.CustWebDealerCust;
import com.sherwin.shercolor.common.util.JavaBeanTester;


public class CustWebDealerCustTest {

	private CustWebDealerCust target;
	
	@Test
	public void testCreate() {
		target = new CustWebDealerCust();
		assertNotNull(target);
	}
	
	@Test
	public void testBeanProperties() {
		try {
			JavaBeanTester.test(CustWebParms.class, "dateAdded");
		} catch (IntrospectionException e) {
			fail();
		}
		
		target = new CustWebDealerCust();
		
		Date dateAdded = new Date();
		
		target.setDateAdded(dateAdded);
	}

}

