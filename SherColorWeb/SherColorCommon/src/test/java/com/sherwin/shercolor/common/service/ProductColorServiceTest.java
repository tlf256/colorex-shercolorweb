package com.sherwin.shercolor.common.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


import com.sherwin.shercolor.util.domain.SwMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional
public class ProductColorServiceTest {

	@Autowired
	ProductColorService target;
	
	@Test
	public void testCreate() {
		assertNotNull(target);
	}

	@Test
	public void testCheckForceBaseAssignment_Pass(){
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "6931"; // color requires yellow chromatic
		String salesNbr = "640399135"; // Duration Home Bright Yellow
		
		System.out.println("--TestCheckForceBaseAssignment_Pass " + colorComp + " " + colorId + " " + salesNbr + " ---------------");

		List<SwMessage> swMessages = null;
		swMessages = target.validate(colorComp, colorId, salesNbr);
		
		for(SwMessage thisMsg : swMessages){
			System.out.println("ErrorCode=" + thisMsg.getCode() + " " + thisMsg.getMessage());
		}
			
		System.out.println(" Message Count is " + swMessages.size());
		assertTrue(swMessages.size()==0);
	}
	

	@Test
	public void testCheckForceBaseAssignment_Fail(){
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "6931"; // color requires yellow chromatic
		String salesNbr = "650187636"; // Duration Home Ultradeep
		
		System.out.println("--TestCheckForceBaseAssignment_Fail " + colorComp + " " + colorId + " " + salesNbr + " ---------------");

		List<SwMessage> swMessages = null;
		swMessages = target.validate(colorComp, colorId, salesNbr);
		
		for(SwMessage thisMsg : swMessages){
			System.out.println("ErrorCode=" + thisMsg.getCode() + " " + thisMsg.getMessage());
		}
			
		System.out.println(" Message Count is " + swMessages.size());
		assertTrue(swMessages.size()>0 && swMessages.get(0).getCode().equals("1420"));
	}
	
	
	@Test
	public void testCheckVinylSafeRestrictions_Pass(){
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "VS306"; // vinyl safe color
		String salesNbr = "640392346"; // vinyl safe product Super Paint Exterior Satin 
		
		System.out.println("--TestCheckVinylSafeRestrictions_Pass " + colorComp + " " + colorId + " " + salesNbr + " ---------------");

		List<SwMessage> swMessages = null;
		swMessages = target.validate(colorComp, colorId, salesNbr);
		
		for(SwMessage thisMsg : swMessages){
			System.out.println("ErrorCode=" + thisMsg.getCode() + " " + thisMsg.getMessage());
		}
			
		System.out.println(" Message Count is " + swMessages.size());
		assertTrue(swMessages.size()==0);
	}

	@Test
	public void testCheckVinylSafeRestrictions_Fail(){
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "VS306"; // vinyl safe color
		String salesNbr = "640399671"; // A100 not a vinyl safe product  
		
		System.out.println("--TestCheckVinylSafeRestrictions_Fail " + colorComp + " " + colorId + " " + salesNbr + " ---------------");
		
		List<SwMessage> swMessages = null;
		swMessages = target.validate(colorComp, colorId, salesNbr);
		
		for(SwMessage thisMsg : swMessages){
			System.out.println("ErrorCode=" + thisMsg.getCode() + " " + thisMsg.getMessage());
		}
			
		System.out.println(" Message Count is " + swMessages.size());
		assertTrue(swMessages.size()>0 && swMessages.get(0).getCode().equals("1423"));
	}

	@Test
	public void testCheckProductExcludedFromColor_Pass(){
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "3503";
		String salesNbr = "640359287"; // regular stain (not an SS stain)
		
		System.out.println("--TestCheckProductExcludedFromColor_Pass " + colorComp + " " + colorId + " " + salesNbr + " ---------------");
		
		List<SwMessage> swMessages = null;
		swMessages = target.validate(colorComp, colorId, salesNbr);
		
		for(SwMessage thisMsg : swMessages){
			System.out.println("ErrorCode=" + thisMsg.getCode() + " " + thisMsg.getMessage());
		}
			
		System.out.println(" Message Count is " + swMessages.size());
		assertTrue(swMessages.size()==0);
	}


	@Test
	public void testCheckProductExcludedFromColor_Fail(){
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "3503";
		String salesNbr = "650869258"; // an SS type stain
		
		System.out.println("--TestCheckProductExcludedFromColor_Fail " + colorComp + " " + colorId + " " + salesNbr + " ---------------");
		
		List<SwMessage> swMessages = null;
		swMessages = target.validate(colorComp, colorId, salesNbr);
		
		for(SwMessage thisMsg : swMessages){
			System.out.println("ErrorCode=" + thisMsg.getCode() + " " + thisMsg.getMessage());
		}
			
		System.out.println(" Message Count is " + swMessages.size());
		assertTrue(swMessages.size()>0 && swMessages.get(0).getCode().equals("1422"));
	}


	@Test
	public void testCheckAfcdDelete_Pass(){
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "6048";
		String salesNbr = "650187636"; // ultradeep base which is the correct base for this color
		
		System.out.println("--TestCheckAfcdDelete_Pass " + colorComp + " " + colorId + " " + salesNbr + " ---------------");
		
		List<SwMessage> swMessages = null;
		swMessages = target.validate(colorComp, colorId, salesNbr);
		
		for(SwMessage thisMsg : swMessages){
			System.out.println("ErrorCode=" + thisMsg.getCode() + " " + thisMsg.getMessage());
		}
			
		System.out.println(" Message Count is " + swMessages.size());
		assertTrue(swMessages.size()==0);
	}

	@Test
	public void testCheckAfcdDelete_Fail(){
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "6048";
		String salesNbr = "650866262"; // accent base
		
		System.out.println("--TestCheckAfcdDelete_Fail " + colorComp + " " + colorId + " " + salesNbr + " ---------------");
		
		List<SwMessage> swMessages = null;
		swMessages = target.validate(colorComp, colorId, salesNbr);
		
		for(SwMessage thisMsg : swMessages){
			System.out.println("ErrorCode=" + thisMsg.getCode() + " " + thisMsg.getMessage());
		}
			
		System.out.println(" Message Count is " + swMessages.size());
		assertTrue(swMessages.size()>0 && swMessages.get(0).getCode().equals("1425"));
	}

	@Test
	public void testCheckProductColorBaseMatch_Pass(){
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "3503";
		String salesNbr = "640359287"; // regular stain (not an SS stain)
		
		System.out.println("--TestCheckProductColorBaseMatch_Pass " + colorComp + " " + colorId + " " + salesNbr + " ---------------");
		List<SwMessage> swMessages = null;
		swMessages = target.validate(colorComp, colorId, salesNbr);
		
		for(SwMessage thisMsg : swMessages){
			System.out.println("ErrorCode=" + thisMsg.getCode() + " " + thisMsg.getMessage());
		}
			
		System.out.println(" Message Count is " + swMessages.size());
		assertTrue(swMessages.size()==0);
	}

	@Test
	public void testCheckProductColorBaseMatchAutobase_Pass(){
		String colorComp = "BEHR";
		String colorId = "UL140-7";
		String salesNbr = "650046329"; // Interior Super Paint Satin Deep
		
		System.out.println("--TestCheckProductColorBaseMatchAutobase_Pass " + colorComp + " " + colorId + " " + salesNbr + " ---------------");
		List<SwMessage> swMessages = null;
		swMessages = target.validate(colorComp, colorId, salesNbr);
		
		for(SwMessage thisMsg : swMessages){
			System.out.println("ErrorCode=" + thisMsg.getCode() + " " + thisMsg.getMessage());
		}
			
		System.out.println(" Message Count is " + swMessages.size());
		assertTrue(swMessages.size()==0);
	}

	@Test
	public void testCheckProductColorBaseMatch_Fail(){
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "6385";
		String salesNbr = "640359287"; // regular stain (not an SS stain)
		
		System.out.println("--TestCheckProductColorBaseMatch_Fail " + colorComp + " " + colorId + " " + salesNbr + " ---------------");
		List<SwMessage> swMessages = null;
		swMessages = target.validate(colorComp, colorId, salesNbr);
		
		for(SwMessage thisMsg : swMessages){
			System.out.println("ErrorCode=" + thisMsg.getCode() + " " + thisMsg.getMessage());
		}
			
		System.out.println(" Message Count is " + swMessages.size());
		assertTrue(swMessages.size()>0 && swMessages.get(0).getCode().equals("1424"));
	}

	@Test
	public void testCheckProductColorBaseMatchAutobase_Fail(){
		String colorComp = "BEHR";
		String colorId = "UL140-7";
		String salesNbr = "650096498"; // Interior Duration Home Matte HRB
		
		System.out.println("--TestCheckProductColorBaseMatchAutobase_Fail " + colorComp + " " + colorId + " " + salesNbr + " ---------------");
		List<SwMessage> swMessages = null;
		swMessages = target.validate(colorComp, colorId, salesNbr);
		
		for(SwMessage thisMsg : swMessages){
			System.out.println("ErrorCode=" + thisMsg.getCode() + " " + thisMsg.getMessage());
		}
			
		System.out.println(" Message Count is " + swMessages.size());
		assertTrue(swMessages.size()>0 && swMessages.get(0).getCode().equals("1424"));
	}
	
}
