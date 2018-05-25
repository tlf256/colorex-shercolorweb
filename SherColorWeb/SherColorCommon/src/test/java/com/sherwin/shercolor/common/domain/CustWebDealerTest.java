package com.sherwin.shercolor.common.domain;



import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.beans.IntrospectionException;
import org.junit.Test;

import com.sherwin.shercolor.common.domain.CustWebDealer;
import com.sherwin.shercolor.common.util.JavaBeanTester;


public class CustWebDealerTest {

	private CustWebDealer target;
	
	@Test
	public void testCreate() {
		target = new CustWebDealer();
		assertNotNull(target);
	}
	
	@Test
	public void testBeanProperties() {
		try {
			JavaBeanTester.test(CustWebParms.class, "customerId");
		} catch (IntrospectionException e) {
			fail();
		}
		
		target = new CustWebDealer();
		
		String customerId = "R12345";
		
		target.setCustomerId(customerId);
	}

}

