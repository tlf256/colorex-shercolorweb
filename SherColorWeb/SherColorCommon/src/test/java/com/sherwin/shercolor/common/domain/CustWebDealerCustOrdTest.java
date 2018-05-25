package com.sherwin.shercolor.common.domain;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.beans.IntrospectionException;
import org.junit.Test;

import com.sherwin.shercolor.common.domain.CustWebDealerCustOrd;
import com.sherwin.shercolor.common.util.JavaBeanTester;


public class CustWebDealerCustOrdTest {

	private CustWebDealerCustOrd target;
	
	@Test
	public void testCreate() {
		target = new CustWebDealerCustOrd();
		assertNotNull(target);
	}
	
	@Test
	public void testBeanProperties() {
		try {
			JavaBeanTester.test(CustWebParms.class, "dateAdded");
		} catch (IntrospectionException e) {
			fail();
		}
		
		target = new CustWebDealerCustOrd();
		
		int controlNbr = 12345;
		
		target.setControlNbr(controlNbr);
	}

}

