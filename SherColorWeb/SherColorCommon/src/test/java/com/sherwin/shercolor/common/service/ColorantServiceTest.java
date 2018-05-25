package com.sherwin.shercolor.common.service;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CdsClrnt;
import com.sherwin.shercolor.common.domain.CdsClrntSys;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional
public class ColorantServiceTest {

	@Autowired
	private ColorantService target;
	
	@Test
	public void testCreate() {
		assertNotNull(target);
	}
	
	@Test
	public void testGetAvailableColorantSystemsStains() {
		String salesNbr ="1040351";
	
		List<CdsClrntSys> result = target.getAvailableColorantSystems(salesNbr);
		
		if(result!=null){
			System.out.println("Using sales nbr " + salesNbr + " found " + result.size() + " colorant systems...");
			for (CdsClrntSys item:result){
				System.out.println(item.getClrntSysId() + " " + item.getClrntSysName());
			}		
		}

		assertNotNull(result);
		if (result.size() != 8) {
			fail();
		}
	}
	

	
	@Test
	public void testGetAvailableColorantSystemsNoExclusions() {
		String salesNbr ="NOEXCLUSIONS";
	
		List<CdsClrntSys> result = target.getAvailableColorantSystems(salesNbr);
		
		if(result!=null){
			System.out.println("Using sales nbr " + salesNbr + " found " + result.size() + " colorant systems...");
			for (CdsClrntSys item:result){
				System.out.println(item.getClrntSysId() + " " + item.getClrntSysName());
			}		
		}

		assertNotNull(result);
		if (result.size() != 8) {
			fail();
		}
	}
	
	@Test
	public void testGetAvailableColorantSystemsOneExclusion() {
		String salesNbr ="100551472";
	
		List<CdsClrntSys> result = target.getAvailableColorantSystems(salesNbr);
		
		if(result!=null){
			System.out.println("Using sales nbr " + salesNbr + " found " + result.size() + " colorant systems...");
			for (CdsClrntSys item:result){
				System.out.println(item.getClrntSysId() + " " + item.getClrntSysName());
			}		
		}

		assertNotNull(result);
		if (result.size() != 7) {
			fail();
		}
	}
	
	@Test
	public void testGetAvailableColorantSystemsTwoExclusions() {
		String salesNbr ="100535731";
	
		List<CdsClrntSys> result = target.getAvailableColorantSystems(salesNbr);
		
		if(result!=null){
			System.out.println("Using sales nbr " + salesNbr + " found " + result.size() + " colorant systems...");
			for (CdsClrntSys item:result){
				System.out.println(item.getClrntSysId() + " " + item.getClrntSysName());
			}		
		}

		assertNotNull(result);
		if (result.size() != 6) {
			fail();
		}
	}

	
	@Test
	public void testGetColorList() {
		String clrntSysId = "CCE";
	
		List<CdsClrnt> result = target.getColorantList(clrntSysId);
		
		if(result!=null){
			System.out.println("Get Colorants for " + clrntSysId + " found " + result.size() + " colorants...");
			for (CdsClrnt item:result){
				System.out.println(item.getTintSysId() + " " + item.getName());
			}		
		}

		assertNotNull(result);
		if (result.size() != 10) {
			fail();
		}
	}
	
	@Test
	public void testGetColorList_FAIL() {
		String clrntSysId = "junk";
	
		List<CdsClrnt> result = target.getColorantList(clrntSysId);
		
		if(result!=null){
			System.out.println("Get Colorants for " + clrntSysId + " found " + result.size() + " colorants...");
			for (CdsClrnt item:result){
				System.out.println(item.getTintSysId() + " " + item.getName());
			}		
		}

		assertNotNull(result);
		if (result.size() != 0) {
			fail();
		}
	}

	@Test
	public void testGetVinylSafeColorants() {
		String clrntSysId = "CCE";
		String salesNbr = "640392569";
		boolean vinylSafeRequested = true;
	
		List<CdsClrnt> result = target.getColorantList(clrntSysId, salesNbr, vinylSafeRequested);
		
		if(result!=null){
			System.out.println("Get Vinyl Safe Colorants for " + clrntSysId + " found " + result.size() + " colorants...");
			for (CdsClrnt item:result){
				System.out.println(item.getTintSysId() + " " + item.getName());
			}		
		}

		assertNotNull(result);
		if (result.size() != 9) {
			fail();
		}
	}
	
	@Test
	public void testGetCoolFeelColorants() {
		String clrntSysId = "CCE";
		String salesNbr = "650931603";
		boolean vinylSafeRequested = false;
	
		List<CdsClrnt> result = target.getColorantList(clrntSysId, salesNbr, vinylSafeRequested);
		
		if(result!=null){
			System.out.println("Get Cool Feel Colorants for " + clrntSysId + " found " + result.size() + " colorants...");
			for (CdsClrnt item:result){
				System.out.println(item.getTintSysId() + " " + item.getName());
			}		
		}

		assertNotNull(result);
		if (result.size() != 9) {
			fail();
		}
	}
	

	@Test
	public void testGetColorantSystem(){
		String clrntSysId = "CCE";
		CdsClrntSys result = null;
		result = target.getColorantSystem(clrntSysId);
		
		System.out.println("Test Get Colorant System for " + clrntSysId + " returned name " + result.getClrntSysName());
		assertNotNull(result);
	}
	
	@Test
	public void testGetIncrementHeader(){
		String clrntSysId = "CCE";
		List<String> result;
		
		result = target.getColorantIncrementHeader(clrntSysId);
		
		System.out.println("Test Get Increment Header for " + clrntSysId + " returned " + result.toString());
		assertTrue(result.size()>0);
		
		
	}
	
}
