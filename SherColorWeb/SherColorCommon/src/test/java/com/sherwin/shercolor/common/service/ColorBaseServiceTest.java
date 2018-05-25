package com.sherwin.shercolor.common.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional
public class ColorBaseServiceTest {
	@Autowired
	private ColorBaseService target;

	@Test
	public void testCreate() {
		assertNotNull(target);
	}
	
	@Test
	public void testPositiveGetInteriorBases() {
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "1648";
		String prodComp = "SW";

		
		List<String> result = target.InteriorColorBaseAssignments(colorComp, colorId, prodComp);
		
		if(result!=null){
			System.out.println("Interior Color Base Assignments found " + result.size() + " records with " + colorComp + " " + colorId + " " + prodComp +". They are ...");
			for (String item:result){
				System.out.println("--->" + item);
			}		}

		assertNotNull(result);
	}
	
	@Test
	public void testNegativeGetInteriorBases() {
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "NOTAREALCOLOR";
		String prodComp = "SW";

		
		List<String> result = target.InteriorColorBaseAssignments(colorComp, colorId, prodComp);
		
		if(result!=null){
			System.out.println("Interior Color Base Assignments found " + result.size() + " records with " + colorComp + " " + colorId + " " + prodComp +". They are ...");
			for (String item:result){
				System.out.println("--->" + item);
			}		}

		assertNull(result);
	}
	
	@Test
	public void testPositiveGetExteriorBases() {
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "1648";
		String prodComp = "SW";

		
		List<String> result = target.ExteriorColorBaseAssignments(colorComp, colorId, prodComp);
		
		if(result!=null){
			System.out.println("eXTerior Color Base Assignments found " + result.size() + " records with " + colorComp + " " + colorId + " " + prodComp +". They are ...");
			for (String item:result){
				System.out.println("--->" + item);
			}		}

		assertNotNull(result);
	}

	@Test
	public void testNegativeGetExteriorBases() {
		String colorComp = "SHERWIN-WILLIAMS";
		String colorId = "NOTAREALCOLOR";
		String prodComp = "SW";

		
		List<String> result = target.ExteriorColorBaseAssignments(colorComp, colorId, prodComp);
		
		if(result!=null){
			System.out.println("Exterior Color Base Assignments found " + result.size() + " records with " + colorComp + " " + colorId + " " + prodComp +". They are ...");
			for (String item:result){
				System.out.println("--->" + item);
			}		}

		assertNull(result);
	}
	
	@Test
	public void testAutoBaseSW(){
		String colorComp = "PPG";
		String colorId = "104-7";
		String prodComp = "SW";

		
		List<String> result = target.GetAutoBase(colorComp, colorId, prodComp);
		
		if(result!=null){
			System.out.println("SW Auto Base returned " + result.size() + " bases for " + colorComp + " " + colorId + " " + prodComp +". They are ...");
			for (String item:result){
				System.out.println("--->" + item);
			}		}

		assertNotNull(result);
	}

	@Test
	public void testAutoBaseOther(){
		String colorComp = "PPG";
		String colorId = "104-7";
		String prodComp = "GENERAL";
		
		List<String> result = target.GetAutoBase(colorComp, colorId, prodComp);
		
		if(result!=null){
			System.out.println("GENERAL Auto Base returned " + result.size() + " bases for " + colorComp + " " + colorId + " " + prodComp +". They are ...");
			for (String item:result){
				System.out.println("--->" + item);
			}		}

		assertNotNull(result);
	}

	@Test
	public void testAutoBaseFail(){
		String colorComp = "MAB";
		String colorId = "200-1361";
		String prodComp = "SW";

		
		List<String> result = target.GetAutoBase(colorComp, colorId, prodComp);
		
		if(result!=null && result.size()>0){
			System.out.println("**FAIL** Auto Base returned " + result.size() + " bases for " + colorComp + " " + colorId + " " + prodComp +". They are ...");
			for (String item:result){
				System.out.println("--->" + item);
			}		}

		assertTrue(result.size()==0);
	}


}
