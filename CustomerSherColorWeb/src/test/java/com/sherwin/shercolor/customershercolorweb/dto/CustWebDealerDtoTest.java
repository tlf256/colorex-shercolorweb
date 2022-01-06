package com.sherwin.shercolor.customershercolorweb.dto;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.sherwin.shercolor.customershercolorweb.web.dto.CustWebDealerDto;

public class CustWebDealerDtoTest {

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
