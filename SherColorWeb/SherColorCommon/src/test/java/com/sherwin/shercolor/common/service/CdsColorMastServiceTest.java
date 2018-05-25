package com.sherwin.shercolor.common.service;

//import static org.junit.Assert.assertTrue;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CdsColorMast;
import com.sherwin.shercolor.util.domain.SwMessage;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional
//@Rollback
public class CdsColorMastServiceTest {
	@Autowired
	private ColorMastService target;

	@Test
	public void testCreate() {
		assertNotNull(target);
	}
	
	@Test
	public void testPositiveAutocompleteColor() {
		String partialColorNameOrId ="DOV";
	
		String[] result = target.autocompleteColor(partialColorNameOrId);
		
		if(result!=null){
			System.out.println("Autocomplete Color found " + result.length + " records with " + partialColorNameOrId + " in the color name or id. They are ...");
			for (String item:result){
				System.out.println("this " + item + " contains " + partialColorNameOrId);
			}		}

		assertNotNull(result);
	}
	
	@Test
	public void testPositiveAutocompleteCompetColor() {
		String partialColorNameOrId ="DOV";
	
		List<CdsColorMast> result = target.autocompleteCompetitiveColor(partialColorNameOrId);
		
		if(result!=null){
			System.out.println("Autocomplete Competitive Color found " + result.size() + " records with " + partialColorNameOrId + " in the color name or id. They are ...");
			for (CdsColorMast item:result){
				System.out.println("this " + item.getColorName() + " contains " + partialColorNameOrId);
			}		}

		assertNotNull(result);
	}
	
	@Test
	public void testPositiveAutocompleteSWColor() {
		String partialColorNameOrId ="DOV";
		
		List<CdsColorMast> result = target.autocompleteSWColor(partialColorNameOrId);
		
		if(result!=null){
			System.out.println("Autocomplete SW Color found " + result.size() + " records with " + partialColorNameOrId + " in the color name or id. They are ...");
			for (CdsColorMast item:result){
				System.out.println("this " + item.getColorName() + " contains " + partialColorNameOrId);
			}		}

		assertNotNull(result);
	}
	
	@Test
	public void testPositiveListColorCompanies() {

		String[] result = target.listColorCompanies();
		
		if(result!=null){
			System.out.println("List Color Companies found " + result.length + " records. They are ...");
			for (String item:result){
				System.out.println( item);
			}		}

		assertNotNull(result);
	}

	@Test
	public void testPositiveRead() {
	
		CdsColorMast result = target.read("SHERWIN-WILLIAMS", "40029");
		assertNotNull(result);
	}

	@Test
	public void testNegativeRead() {
	
		CdsColorMast result = target.read("NOTACOMPANY", "NOTACOLOR");
		assertNull(result);
	}
	
	@Test
	public void testPositiveValidation() {
	
		List<SwMessage> result = target.validate("SHERWIN-WILLIAMS", "40029");
		assertNotNull(result);
		if (result.size()!=0) {
			fail();
		}
	}

	@Test
	public void testNegativeValidation() {
	
		List<SwMessage> result = target.validate("NOTACOMPANY", "NOTACOLOR");
		assertNotNull(result);
		if (result.size()==0) {
			fail();
		}
		System.out.println("Test Negative Validation found " + result.size() + " records. They are ...");
		for (SwMessage item:result){
			System.out.println(item.getSeverity().toString() + " " + item.getCode() + " " + item.getMessage());
		}
		
	}
}
