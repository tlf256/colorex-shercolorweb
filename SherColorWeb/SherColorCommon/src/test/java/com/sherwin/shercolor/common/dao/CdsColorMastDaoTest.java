package com.sherwin.shercolor.common.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.shercolor.common.domain.CdsColorMast;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon.xml"})
@Transactional
public class CdsColorMastDaoTest {
	@Autowired
	private CdsColorMastDao target;

	@Test
	public void testCreate() {
		assertNotNull(target);
	}
	
	@Test
	public void testPositivelistColorCompanies() {
	
		
		List<String> result = target.listOfColorCompanies();
		
		if(result!=null){
			System.out.println("Retrieved list of color companies from '" + result.get(0) + "' to '" + result.get(result.size()-1) + "'");
		}

		assertNotNull(result);
	}
	
	@Test
	public void testPositiveReadCdsColorMast() {
		String colorComp ="SHERWIN-WILLIAMS";
		String colorId = "6522";
		
		CdsColorMast result = target.read(colorComp, colorId);
		
		if(result!=null){
			System.out.println(result.getColorComp()+ " " + result.getColorId() + " " + result.getColorName() + " " + result.getPalette());
		}

		assertNotNull(result);
	}

	@Test
	public void testFailedReadCdsColorMast() {
		String colorComp ="JUNK";
		String colorId = "JUNK";
		
		CdsColorMast result = target.read(colorComp, colorId);
		
		assertTrue(result == null);
	}

	@Test
	public void testSearchByPartialName() {
		String searchValue = "DOVER WHITE";
		
		List<CdsColorMast> result = target.listForColorNameMatch(searchValue, false);

		if (result!=null) {
			System.out.println("found " + result.size() + " records with " + searchValue + " in the color name. They are ...");
			for (CdsColorMast item:result){
				System.out.println("this " + item.getColorComp()+ " " + item.getColorId() + " " + item.getColorName() + " contains " + searchValue);
			}
		}
		
		assertTrue(result.size() > 0 );
	}

	@Test
	public void testSearchByFullName() {
		String searchValue = "DOVER WHITE";
		
		List<CdsColorMast> result = target.listForColorNameMatch(searchValue, true);

		if (result!=null) {
			System.out.println("found " + result.size() + " records with " + searchValue + " as the color name. They are ...");
			for (CdsColorMast item:result){
				System.out.println("this " + item.getColorComp() + " " + item.getColorId() + " " + item.getColorName() + " exactly matches " + searchValue);
			}
		}

		assertTrue(result.size() > 0);
	}

	@Test
	public void testFailedSearchByFullName() {
		String searchValue = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
		
		List<CdsColorMast> result = target.listForColorNameMatch(searchValue, true);

		assertTrue(result.size()==0);
	}

	@Test
	public void testSearchByPartialId() {
		String searchValue = "6385";
		
		List<CdsColorMast> result = target.listForColorIdMatch(searchValue, false);
		
		if (result!=null) {
			System.out.println("found " + result.size() + " records with " + searchValue + " in the color id. They are ...");
			for (CdsColorMast item:result){
				System.out.println("this " + item.getColorComp() + " " + item.getColorId() + " contains " + searchValue);
			}
		}
		
		assertTrue(result.size() > 0 );
	}

	@Test
	public void testSearchByFullId() {
		String searchValue = "638";
		
		List<CdsColorMast> result = target.listForColorIdMatch(searchValue, true);

		if (result!=null) {
			System.out.println("found " + result.size() + " records with " + searchValue + " as the color id. They are ...");
			for (CdsColorMast item:result){
				System.out.println("this " + item.getColorComp() + " " + item.getColorId() + " " + item.getColorName() + " exactly matches " + searchValue);
			}
		}

		assertTrue(result.size() > 0);
	}

	@Test
	public void testFailedSearchByFullId() {
		String searchValue = "XXXXXXXXXXXXXXX";
		
		List<CdsColorMast> result = target.listForColorIdMatch(searchValue, true);

		assertTrue(result.size()==0);
	}

	@Test
	public void testAutocompleteByName() {
		String searchValue = "DOVER";
		
		List<String> result = target.listForAutocompleteColorName(searchValue);

		if (result!=null) {
			System.out.println("Autocomplete by Name found " + result.size() + " records with " + searchValue + " in the color name. They are ...");
			for (String item:result){
				System.out.println("this " + item + " contains " + searchValue);
			}
		}
		
		assertTrue(result.size() > 0 );
	}

	@Test
	public void testAutocompleteBySwName() {
		String searchValue = "DOVER";
		
		List<String> result = target.listForAutocompleteSwColorName(searchValue);

		if (result!=null) {
			System.out.println("Autocomplete by SwName found " + result.size() + " records with " + searchValue + " in the color name. They are ...");
			for (String item:result){
				System.out.println("this " + item + " contains " + searchValue);
			}
		}
		
		assertTrue(result.size() > 0 );
	}

	@Test
	public void testAutocompleteById() {
		String searchValue = "638";
		
		List<String> result = target.listForAutocompleteColorId(searchValue);

		if (result!=null) {
			System.out.println("Autocomplete by Id found " + result.size() + " records with " + searchValue + " in the color name. They are ...");
			for (String item:result){
				System.out.println("this " + item + " contains " + searchValue);
			}
		}
		
		assertTrue(result.size() > 0 );
	}

	@Test
	public void testAutocompleteBySwId() {
		String searchValue = "638";
		
		List<String> result = target.listForAutocompleteSwColorId(searchValue);

		if (result!=null) {
			System.out.println("Autocomplete by SwId found " + result.size() + " records with " + searchValue + " in the color name. They are ...");
			for (String item:result){
				System.out.println("this " + item + " contains " + searchValue);
			}
		}
		
		assertTrue(result.size() > 0 );
	}

	@Test
	public void testStringSwColorAutocomplete() {
		String searchValue = "193";
		
		List<String> result = target.stringListForSwColorAutocomplete(searchValue);

		if (result!=null) {
			System.out.println("String SW Color Autocomplete found " + result.size() + " records with " + searchValue + " in the color id, name or loc code. They are ...");
			for (String item:result){
				System.out.println("--->" + item + " starts with " + searchValue);
			}
		}
		
		assertTrue(result.size() > 0 );
	}

	@Test
	public void testSwColorAutocomplete() {
		String searchValue = "193";
		
		List<CdsColorMast> result = target.listForSwColorAutocomplete(searchValue);

		if (result!=null) {
			System.out.println("SW Color Autocomplete found " + result.size() + " records with " + searchValue + " in the color id, name or loc code. They are ...");
			for (CdsColorMast item:result){
				System.out.println("--->" + item.getColorName() + " starts with " + searchValue);
			}
		}
		
		assertTrue(result.size() > 0 );
	}
	
	@Test
	public void testStringCompetColorAutocomplete() {
		String searchValue = "DOVER";
		
		List<String> result = target.stringListForCompetColorAutocomplete(searchValue);

		if (result!=null) {
			System.out.println("String Compet Color Autocomplete found " + result.size() + " records with " + searchValue + " in the color id or name. They are ...");
			for (String item:result){
				System.out.println("--->" + item + " starts with " + searchValue);
			}
		}
		
		assertTrue(result.size() > 0 );
	}
	
	@Test
	public void testCompetColorAutocomplete() {
		String searchValue = "193";
		
		List<CdsColorMast> result = target.listForCompetColorAutocomplete(searchValue);

		if (result!=null) {
			System.out.println("Compet Color Autocomplete found " + result.size() + " records with " + searchValue + " in the color id, name or loc code. They are ...");
			for (CdsColorMast item:result){
				System.out.println("--->" + item.getColorName() + " starts with " + searchValue);
			}
		}
		
		assertTrue(result.size() > 0 );
	}

	

}
