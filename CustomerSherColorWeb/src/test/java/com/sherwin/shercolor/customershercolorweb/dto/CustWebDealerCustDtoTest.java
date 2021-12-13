package com.sherwin.shercolor.customershercolorweb.dto;


import static org.junit.Assert.*;

import java.beans.IntrospectionException;

import org.junit.Before;
import org.junit.Test;

import com.sherwin.shercolor.customershercolorweb.web.dto.CustWebDealerDto;

public class CustWebDealerCustDtoTest {

	private CustWebDealerDto target;
	
	@Before
	public void setUp(){
		target = new CustWebDealerDto();
	}
	
	@Test
	public void testCreate() {
		assertNotNull(target);
	}

}
