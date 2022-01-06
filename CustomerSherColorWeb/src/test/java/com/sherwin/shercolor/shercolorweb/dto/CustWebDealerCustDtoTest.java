package com.sherwin.shercolor.shercolorweb.dto;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.sherwin.shercolor.shercolorweb.web.dto.CustWebDealerDto;

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
